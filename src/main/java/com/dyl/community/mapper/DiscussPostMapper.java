package com.dyl.community.mapper;

//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.dyl.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author admin
 * @Entity com.dyl.community.entity.DiscussPost
 */
@Repository
public interface DiscussPostMapper /*extends BaseMapper<DiscussPost>*/ {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}




