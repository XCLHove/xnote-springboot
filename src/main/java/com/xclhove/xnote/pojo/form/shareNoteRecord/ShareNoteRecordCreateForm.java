package com.xclhove.xnote.pojo.form.shareNoteRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
public class ShareNoteRecordCreateForm implements Serializable {
    @NotNull(message = "笔记ID不能为空")
    @Positive(message = "笔记ID必须为正整数")
    private Integer noteId;
    /**
     * 默认为7天后过期
     */
    private Timestamp expireTime = new Timestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7);
}
