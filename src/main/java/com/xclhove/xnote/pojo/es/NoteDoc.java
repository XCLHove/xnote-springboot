package com.xclhove.xnote.pojo.es;

import cn.hutool.core.bean.BeanUtil;
import com.xclhove.xnote.constant.EsConstant;
import com.xclhove.xnote.pojo.enums.NoteIsPublic;
import com.xclhove.xnote.pojo.table.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "note")
public class NoteDoc implements Serializable {
    @Id
    private Integer id;
    
    @Field(type = FieldType.Text,
            analyzer = EsConstant.Analyzer.IK_MAX_WORD,
            searchAnalyzer = EsConstant.Analyzer.IK_MAX_WORD,
            copyTo = EsConstant.NOTE_QUERY_FIELD
    )
    private String title;
    
    @Field(type = FieldType.Text,
            analyzer = EsConstant.Analyzer.IK_MAX_WORD,
            searchAnalyzer = EsConstant.Analyzer.IK_MAX_WORD,
            copyTo = EsConstant.NOTE_QUERY_FIELD
    )
    private String content;
    
    @Field(type = FieldType.Keyword)
    private Integer userId;
    
    @Field(type = FieldType.Date)
    private Date releaseTime;
    
    @Field(type = FieldType.Date)
    private Date updateTime;
    
    @Field(type = FieldType.Keyword)
    private NoteIsPublic isPublic;
    
    @Field(type = FieldType.Keyword)
    private Integer typeId;
    
    //@CompletionField(
    //        analyzer = EsConstant.Analyzer.IK_MAX_WORD,
    //        searchAnalyzer = EsConstant.Analyzer.IK_MAX_WORD
    //)
    //private String suggestionField;
    
    public Note toNote() {
        return BeanUtil.copyProperties(this, Note.class);
    }
    
    //public NoteDoc generateSuggestionField() {
    //    suggestionField = "";
    //    if (title != null) {
    //        suggestionField += title;
    //    }
    //    if (content != null) {
    //        suggestionField += content;
    //    }
    //
    //    return this;
    //}
}