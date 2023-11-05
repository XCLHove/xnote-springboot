package com.xclhove.xnote.entity.table;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xclhove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @TableId
    private Integer id;
    private String name;
    private String account;
    @JsonIgnore
    private String password;
    private String email;
}
