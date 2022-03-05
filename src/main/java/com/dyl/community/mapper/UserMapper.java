package com.dyl.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dyl.community.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author admin
 * @Entity com.dyl.community.entity.User
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);
}
