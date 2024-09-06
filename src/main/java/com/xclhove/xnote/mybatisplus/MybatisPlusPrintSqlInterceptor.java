package com.xclhove.xnote.mybatisplus;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 基于Mybatis Plus的SQL输出拦截器。
 * 完美的输出打印 SQL 及执行时长、statement。
 * 注意：该插件有性能损耗，不建议生产环境使用。
 * @author xclhove
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class MybatisPlusPrintSqlInterceptor implements Interceptor {
    
    /**
     * 是否输出到控制台
     */
    private boolean outConsole = false;
    
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String statement = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        long start = System.currentTimeMillis();
        Object returnValue = invocation.proceed();
        long time = System.currentTimeMillis() - start;
        showSql(configuration, boundSql, time, statement);
        return returnValue;
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
    
    private void showSql(Configuration configuration, BoundSql boundSql, long elapsed, String statement) {
        String logText = formatMessage(DateUtil.now(), elapsed, getSqlWithValues(boundSql.getSql(), buildParameterValues(configuration, boundSql)), statement);
        if (Boolean.TRUE == outConsole) {
            // 打印红色 SQL 日志
            System.err.println(logText);
        } else {
            log.info("\n{}", logText);
        }
    }
    
    // com.baomidou.mybatisplus.core.MybatisParameterHandler#setParameters
    private static Map<Integer, Object> buildParameterValues(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Map<Integer, Object> parameterValues = new HashMap<>();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    parameterValues.put(i, new Value(value));
                }
            }
            return parameterValues;
        }
        return Collections.emptyMap();
    }
    
    public static String formatMessage(String now, long elapsed, String sql, String statement) {
        return StringUtils.isNotBlank(sql) ? " Consume Time：" + elapsed + " ms " + now + " (" + statement + ")" +
                "\n Execute SQL：" + sql.replaceAll("[\\s]+", " ") + "\n" : "";
    }
    
    public static String getSqlWithValues(String statementQuery, Map<Integer, Object> parameterValues) {
        final StringBuilder sb = new StringBuilder();
        
        // iterate over the characters in the query replacing the parameter placeholders
        // with the actual values
        int currentParameter = 0;
        for (int pos = 0; pos < statementQuery.length(); pos++) {
            char character = statementQuery.charAt(pos);
            if (statementQuery.charAt(pos) == '?' && currentParameter <= parameterValues.size()) {
                // replace with parameter value
                Object value = parameterValues.get(currentParameter);
                sb.append(value != null ? value.toString() : new Value().toString());
                currentParameter++;
            } else {
                sb.append(character);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 基于p6spy的简易数据类型转换类。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Value {
        public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
        public static final String DATABASE_DIALECT_DATE_FORMAT = NORM_DATETIME_PATTERN;
        public static final String DATABASE_DIALECT_TIMESTAMP_FORMAT = NORM_DATETIME_PATTERN;
        public static final String DATABASE_DIALECT_BOOLEAN_FORMAT = "numeric";
        
        private Object value;
        
        @Override
        public String toString() {
            return convertToString(this.value);
        }
        
        public String convertToString(Object value) {
            if (value == null) {
                return  "NULL";
            }
            
            String result;
            if (value instanceof byte[]) {
                result = new String((byte[]) value);
            } else if (value instanceof Timestamp) {
                result = new SimpleDateFormat(DATABASE_DIALECT_TIMESTAMP_FORMAT).format(value);
            } else if (value instanceof Date) {
                result = new SimpleDateFormat(DATABASE_DIALECT_DATE_FORMAT).format(value);
            } else if (value instanceof Boolean) {
                if ("numeric".equals(DATABASE_DIALECT_BOOLEAN_FORMAT)) {
                    result = Boolean.FALSE.equals(value) ? "0" : "1";
                } else {
                    result = value.toString();
                }
            } else {
                result = value.toString();
            }
            result = quoteIfNeeded(result, value);
            return result;
        }
        
        private String quoteIfNeeded(String stringValue, Object obj) {
            if (stringValue == null) {
                return null;
            }
            if (Number.class.isAssignableFrom(obj.getClass()) || Boolean.class.isAssignableFrom(obj.getClass())) {
                return stringValue;
            } else {
                return "'" + escape(stringValue) + "'";
            }
        }
        
        private String escape(String stringValue) {
            return stringValue.replaceAll("'", "''");
        }
        
    }
}


