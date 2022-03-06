package com.dyl.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyl.community.entity.LoginTicket;
import com.dyl.community.entity.User;
import com.dyl.community.mapper.LoginTicketMapper;
import com.dyl.community.mapper.UserMapper;
import com.dyl.community.util.CommunityConstant;
import com.dyl.community.util.CommunityUtil;
import com.dyl.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author admin
 */
@Service
public class UserService implements CommunityConstant/*extends IService<User>*/ {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        //空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }

        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        // 验证账号
        User usertest = userMapper.selectByName(user.getUsername());
        if (usertest != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        usertest = userMapper.selectByEmail(user.getEmail());
        if (usertest != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expireSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("msg", "账号不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账户不存在");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账户未激活");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expireSeconds * 1000));

        loginTicketMapper.insertSelective(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }

    //个人设置修改密码功能
    public Map<String,Object> updatePassword(String password,String newPassword,int id){
        Map<String,Object> map =new HashMap<>();
        User user = userMapper.selectById(id);
        password= CommunityUtil.md5(password+user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","输入密码错误！");
            return map;
        } else {//注意：存入新密码要以加密后的形式存进去
            newPassword= CommunityUtil.md5(newPassword+user.getSalt());
            userMapper.updatePassword(id,newPassword);
        }

        return map;
    }

//    public Map<String, Object> changePassword(User user, String oldPassword, String newPassword, String confirmPassword) {
//        Map<String, Object> map = new HashMap<>();
//        // 验证密码
//        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
//        if (!user.getPassword().equals(oldPassword)) {
//            map.put("oldPasswordMsg", "密码不正确!");
//            return map;
//        }
//        if (StringUtils.isBlank(newPassword)) {
//            map.put("newPasswordMsg", "密码不能为空!");
//            return map;
//        }
//        if(!newPassword.equals(confirmPassword)){
//            map.put("confirmPasswordMsg", "两次输入的密码不一致!");
//            return map;
//        }
//        int id=user.getId();
//        newPassword=CommunityUtil.md5(newPassword + user.getSalt());
//        if(oldPassword.equals(newPassword)){
//            map.put("newPasswordMsg", "旧密码与新密码一致!");
//            return map;
//        }
//        userMapper.updatePassword(id,newPassword);
//        clearCache(user.getId());
//        return map;
//    }
}
