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
 * @TableName discuss_post
 */
@TableName(value = "discuss_post")
@Data
public class DiscussPost implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    private int userId;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String content;

    /**
     * 0-普通; 1-置顶;
     */
    private Integer type;

    /**
     * 0-正常; 1-精华; 2-拉黑;
     */
    private Integer status;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Integer commentCount;

    /**
     *
     */
    private Double score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
