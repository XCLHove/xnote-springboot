package com.xclhove.xnote.pojo.table;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xclhove.xnote.pojo.enums.NoteIsPublic;
import com.xclhove.xnote.pojo.es.NoteDoc;
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
public class Note implements Serializable {
    @TableId
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private Timestamp releaseTime;
    private Timestamp updateTime;
    private NoteIsPublic isPublic;
    private Integer typeId;
    
    public NoteDoc toNoteDoc() {
        return BeanUtil.copyProperties(this, NoteDoc.class);
    }
}