package com.xclhove.xnote.pojo.vo;

import com.xclhove.xnote.pojo.enums.NoteIsPublic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchNoteVO implements Serializable {
    private Integer id;
    private String title;
    private Integer userId;
    private Timestamp releaseTime;
    private NoteIsPublic isPublic;
    private Timestamp updateTime;
    private Integer typeId;
}