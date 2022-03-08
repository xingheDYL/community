package com.dyl.community.mapper;

import org.apache.ibatis.annotations.Param;

import com.dyl.community.entity.Comment;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author admin
 * @Entity com.dyl.community.entity.Comment
 */
@Repository
public interface CommentMapper /*extends BaseMapper<Comment>*/ {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

}




