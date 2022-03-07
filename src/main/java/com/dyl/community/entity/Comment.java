package com.dyl.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author admin
 * @TableName comment
 */
@TableName(value = "comment")
@Data
public class Comment implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private Integer userId;

    /**
     *
     */
    private Integer entityType;

    /**
     *
     */
    private Integer entityId;

    /**
     *
     */
    private Integer targetId;

    /**
     *
     */
    private String content;

    /**
     *
     */
    private Integer status;

    /**
     *
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}