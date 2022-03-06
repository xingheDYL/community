package com.dyl.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyl.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author admin
 * @Entity com.dyl.community.entity.LoginTicket
 */
@Repository
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {

//    @Insert({
//            "insert into login_ticket(user_id,ticket,status,expired) ",
//            "values(#{userId},#{ticket},#{status},#{expired})"
//    })
//    @Options(useGeneratedKeys = true, keyProperty = "id")
//    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 添加信息
     *
     * @param loginTicket loginTicket
     * @return int
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(LoginTicket loginTicket);

//    @Select({
//            "select id,user_id,ticket,status,expired ",
//            "from login_ticket where ticket=#{ticket}"
//    })
//    LoginTicket selectByTicket(String ticket);

    /**
     * 通过Ticket查询信息
     *
     * @param ticket ticket
     * @return List<LoginTicket>
     */
    LoginTicket selectByTicket(@Param("ticket") String ticket);


    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);
}
