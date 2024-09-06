package com.xclhove.xnote.pojo.vo;

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
public class SearchUserImageVO implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer imageId;
    private String alias;
    private Timestamp lastDownloadTime;
}
