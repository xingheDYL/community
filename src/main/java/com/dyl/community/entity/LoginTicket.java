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
 * @TableName login_ticket
 */
@TableName(value = "login_ticket")
@Data
public class LoginTicket implements Serializable {
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
    private String ticket;

    /**
     * 0-有效; 1-无效;
     */
    private Integer status;

    /**
     *
     */
    private Date expired;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
