package com.xclhove.xnote.pojo.table;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class ShareNoteRecord implements Serializable {
    @TableId
    private Integer id;
    private String code;
    private Integer noteId;
    private Integer userId;
    private Timestamp expireTime;
    
    public boolean isExpired() {
        return expireTime.getTime() < System.currentTimeMillis();
    }
}
