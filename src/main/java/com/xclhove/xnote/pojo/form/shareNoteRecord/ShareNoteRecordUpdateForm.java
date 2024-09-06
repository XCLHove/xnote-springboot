package com.xclhove.xnote.pojo.form.shareNoteRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShareNoteRecordUpdateForm {
    private Integer id;
    private Timestamp expireTime = new Timestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7);
}
