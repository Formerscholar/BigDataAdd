package com.chad.demo.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 表名：t_user
 */
@Data
@TableName(value = "t_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "`name`")
    private String name;

    @TableField(value = "age")
    private Integer age;

    @TableField(value = "sex")
    private Integer sex;

    @TableField(value = "mark")
    private BigDecimal mark;
}
