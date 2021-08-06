package com.swking.controller;

import com.swking.annotation.LoginRequired;
import com.swking.entity.User;
import com.swking.service.FollowService;
import com.swking.service.LikeService;
import com.swking.service.UserService;
import com.swking.type.ReturnData;
import com.swking.util.BlogCommunityUtil;
import com.swking.util.GlobalConstant;
import com.swking.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/17
 * @File : UserController
 * @Desc :
 **/
@Slf4j
@RestController
@Api(tags = "User API")
@RequestMapping("/user")
public class UserController implements GlobalConstant {

    @Value("${blogCommunity.path.upload}")
    private String uploadPath;

    @Value("${blogCommunity.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private UserHolder userHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @GetMapping(path = "/test")
    public ReturnData test(){
        return ReturnData.error();
    }

    @LoginRequired
    @PostMapping(path = "/upload")
    @ApiOperation("更新头像")
    public ReturnData uploadHeader(@RequestParam(value = "headerImage") MultipartFile headerImage) {
        if (headerImage == null) {
            return ReturnData.error().message("您还没有选择图片!");
        }

        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return ReturnData.error().message("文件的格式不正确!");
        }

        // 生成随机文件名
        fileName = BlogCommunityUtil.GenerateUUID() + suffix;
        // 确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("上传文件失败: " + e.getMessage());
//            return ReturnData.error(ResultCodeEnum.ERROR_FILE_UPLOAD);
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径(web访问路径)
        // http://localhost:8080/community/user/header/xxx.png
        User user = userHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        return ReturnData.success().message("更新头像成功！");
    }

    @GetMapping(path = "/header/{fileName}")
    @ApiOperation("获取头像")
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
            log.error("读取头像失败: " + e.getMessage());
        }
    }

    // 个人主页
    @GetMapping(path = "/profile/{userId}")
    @ApiOperation("个人主页")
    public ReturnData getProfilePage(@PathVariable("userId") int userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        Map<String, Object> data = new HashMap<>();
        // 用户
        data.put("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        data.put("likeCount", likeCount);

       // 关注数量
       long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        data.put("followeeCount", followeeCount);
       // 粉丝数量
       long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
       data.put("followerCount", followerCount);
       // 是否已关注
       boolean hasFollowed = false;
       if (userHolder.getUser() != null) {
           hasFollowed = followService.hasFollowed(userHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
       }
        data.put("hasFollowed", hasFollowed);

        return ReturnData.success().data(data);
    }
}
