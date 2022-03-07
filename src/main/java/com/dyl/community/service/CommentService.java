package com.dyl.community.service;

import com.dyl.community.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dyl.community.mapper.CommentMapper;
import com.dyl.community.util.CommunityConstant;
import com.dyl.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author admin
 */
@Service
public class CommentService implements CommunityConstant/*extends IService<Comment>*/ {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }
}