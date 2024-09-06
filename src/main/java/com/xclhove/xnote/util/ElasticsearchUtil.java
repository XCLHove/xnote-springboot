package com.xclhove.xnote.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * @author xclhove
 */
public final class ElasticsearchUtil {
    private ElasticsearchUtil() {
    }
    
    public static AnalysisResponse analyze(String url, String ...text) {
        AnalysisRequest analysisRequest = new AnalysisRequest();
        analysisRequest.setAnalyzer("ik_max_word");
        analysisRequest.setText(Arrays.asList(text));
        String jsonString = JSON.toJSONString(analysisRequest);
        
        try (HttpResponse response = HttpRequest
                .get(url + "/_analyze")
                .body(jsonString)
                .header("Content-Type", "application/json")
                .execute()
        ) {
            String body = response.body();
            return JSON.parseObject(body, AnalysisResponse.class);
        }
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class AnalysisRequest {
        private String analyzer = "standard";
        private List<String> text;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class AnalysisResponse {
        @JsonProperty("tokens")
        private List<Token> tokens;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Token {
        @JsonProperty("token")
        private String token;
        @JsonProperty("start_offset")
        private Integer startOffset;
        @JsonProperty("end_offset")
        private Integer endOffset;
        @JsonProperty("type")
        private String type;
        @JsonProperty("position")
        private Integer position;
        
        public boolean isNotSingleChar() {
            return token.length() != 1;
        }
    }
}
