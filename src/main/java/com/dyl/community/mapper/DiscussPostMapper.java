package com.dyl.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyl.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author admin
 * @Entity com.dyl.community.entity.DiscussPost
 */
@Repository
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {

    /**
     * 通过userId分页查询
     *
     * @param userId userId
     * @param offset offset 第几页
     * @param limit  limit 每页的数量
     * @return List<DiscussPost>
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 通过id查询数量
     *
     * @param userId userId
     * @return int
     * '@Param'注解用于给参数取别名,
     * 如果只有一个参数,并且在<if>里使用,则必须加别名
     */
    int selectDiscussPostRows(@Param("userId") int userId);

//    /**
//     * 通过年龄查询用户信息并分页
//     *
//     * @param page   MyBatis-Plus所提供的分页对象，必须位于第一个参数的位置
//     * @param userId userId
//     * @return Page<DiscussPost>
//     */
//    Page<DiscussPost> selectPage(@Param("page") Page<DiscussPost> page, @Param("userId") Integer userId);


    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

//    int updateType(int id, int type);
//
//    int updateStatus(int id, int status);
//
//    int updateScore(int id, double score);
}




