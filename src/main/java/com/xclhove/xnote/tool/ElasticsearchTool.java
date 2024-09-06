package com.xclhove.xnote.tool;

import com.xclhove.xnote.util.ElasticsearchUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xclhove
 */
@Component
@Data
public class ElasticsearchTool {
    @Value("${spring.elasticsearch.uris}")
    private String url;
    
    public ElasticsearchUtil.AnalysisResponse analyze(String ...text) {
        return ElasticsearchUtil.analyze(url, text);
    }
}
