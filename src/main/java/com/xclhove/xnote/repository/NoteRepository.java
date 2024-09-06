package com.xclhove.xnote.repository;

import com.xclhove.xnote.pojo.es.NoteDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author xclhove
 */
public interface NoteRepository extends ElasticsearchRepository<NoteDoc, String> {
}
