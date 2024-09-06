package com.xclhove.xnote.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShareNoteRecordVO implements Serializable {
    private Integer id;
    private String code;
    private Integer noteId;
    private Integer userId;
    private Date expireTime;
    private String title;
}
