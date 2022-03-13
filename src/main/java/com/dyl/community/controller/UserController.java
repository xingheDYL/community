package com.dyl.community.controller;

import com.dyl.community.annotation.LoginRequired;
import com.dyl.community.entity.Comment;
import com.dyl.community.entity.DiscussPost;
import com.dyl.community.entity.Page;
import com.dyl.community.entity.User;
import com.dyl.community.service.*;
import com.dyl.community.util.CommunityConstant;
import com.dyl.community.util.CommunityUtil;
import com.dyl.community.util.HostHolder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;

    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;

    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(Model model) {
        // 上传文件名称
        String fileName = CommunityUtil.generateUUID();
        // 设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody", CommunityUtil.getJSONString(0));

        // 生成上传凭证
        Auth auth = Auth.create(accessKey, secretKey);
        String uploadToken = auth.uploadToken(headerBucketName, fileName, 3600, policy);

        model.addAttribute("uploadToken", uploadToken);
        model.addAttribute("fileName", fileName);

        return "/site/setting";
    }


    // 更新头像路径
    @RequestMapping(path = "/header/url", method = RequestMethod.POST)
    @ResponseBody
    public String updateHeaderUrl(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return CommunityUtil.getJSONString(1, "文件名不能为空");
        }

        String url = headerBucketUrl + "/" + fileName;
        userService.updateHeader(hostHolder.getUser().getId(), url);

        return CommunityUtil.getJSONString(0);
    }

    //废弃
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return "/site/setting";
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件的格式不正确!");
            return "/site/setting";
        }

        // 生成随机文件名
        fileName = CommunityUtil.generateUUID().replace("-", "") + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败: " + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    //废弃
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }

    //个人设置页面修改密码功能
    //这里形参用Model类和User类即可，SpringMVC会把传入内容按照User属性填入user
    @RequestMapping(path = "/setting", method = RequestMethod.POST)
    public String updatePassword(Model model, String password, String newPassword, String confirmPassword) {
        if (StringUtils.isBlank(password)) {
            model.addAttribute("passwordMsg", "请输入原始密码！");
            return "/site/setting";
        }
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordMsg", "请输入新密码！");
            return "/site/setting";
        }
        if (StringUtils.isBlank(confirmPassword)) {
            model.addAttribute("confirmPasswordMsg", "请再次输入新密码！");
            return "/site/setting";
        }
        if (!confirmPassword.equals(newPassword)) {
            model.addAttribute("newPasswordMsg", "两次输入的新密码不相同！");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(password, newPassword, user.getId());
        if (map == null || map.isEmpty()) {
            //传给templates注册成功信息
            model.addAttribute("msg", "密码修改成功");
            //跳到回个人设置页面
            model.addAttribute("target", "/logout");
            return "/site/operate-result";
        } else {
            //失败了传失败信息，跳到到原来的页面
            model.addAttribute("passwordMsg", "输入的原始密码错误！");
            return "/site/setting";
        }
    }

    // 个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }

        // 用户
        model.addAttribute("user", user);
        // 获赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);

        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);

        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);

        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

    //跳转到我的帖子页面
    @RequestMapping(path = "/mypost/{userId}", method = RequestMethod.GET)
    public String toMyPost(@PathVariable("userId") int userId,Model model, Page page,
                           @RequestParam(name = "infoMode", defaultValue = "1") int infoMode) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        // 设置分页信息
        page.setLimit(5);
        page.setRows(discussPostService.findDiscussPostRows(user.getId()));
        page.setPath("/user/mypost/"+userId);


        // 查询某用户发布的帖子
        List<DiscussPost> discussPosts = discussPostService.findDiscussPosts(user.getId(), page.getOffset(), page.getLimit(),0);
        List<Map<String, Object>> list = new ArrayList<>();
        if (discussPosts != null) {
            for (DiscussPost post : discussPosts) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                // 点赞数量
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                list.add(map);
            }
            model.addAttribute("discussPosts", list);
        }
        // 帖子数量
        int postCount = discussPostService.findDiscussPostRows(user.getId());
        model.addAttribute("postCount", postCount);
        model.addAttribute("infoMode", infoMode);

        return "site/my-post";
    }

    //跳转到我的评论页面
    @RequestMapping(path = "/mycomment/{userId}", method = RequestMethod.GET)
    public String toMyReply(@PathVariable("userId") int userId,Model model, Page page,
                            @RequestParam(name = "infoMode", defaultValue = "2") int infoMode) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        // 设置分页信息
        page.setLimit(5);
        page.setRows(commentService.findCommentCountById(user.getId()));
        page.setPath("/user/mycomment/"+userId);

        // 获取用户所有评论 (而不是回复,所以在 sql 里加一个条件 entity_type = 1)
        List<Comment> comments = commentService.findCommentsByUserId(user.getId(),page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        if (comments != null) {
            for (Comment comment : comments) {
                Map<String, Object> map = new HashMap<>();
                map.put("comment", comment);

                // 根据实体 id 查询对应的帖子标题
                String discussPostTitle = discussPostService.findDiscussPostById(comment.getEntityId()).getTitle();
                map.put("discussPostTitle", discussPostTitle);

                list.add(map);
            }
            model.addAttribute("comments", list);
        }

        // 回复的数量
        int commentCount = commentService.findCommentCountById(user.getId());
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("infoMode", infoMode);

        return "site/my-comment";
    }
}