package com.xclhove.xnote.pojo.table;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xclhove.xnote.pojo.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xclhove
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @TableId
    private Integer id;
    private String name;
    private String account;
    @JsonIgnore
    private String password;
    private String email;
    private UserStatus status;
    private Integer homePageNoteId;
    private Long imageStorageSize;
}