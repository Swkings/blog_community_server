package com.swking;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.swking.dao.DiscussPostMapper;
import com.swking.dao.UserMapper;
import com.swking.entity.DiscussPost;
import com.swking.entity.User;
import com.swking.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @Author: Swking
 * @email : 1114006175@qq.com
 * @Date : 2021/07/11
 * @File : MapperTest
 * @Desc :
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BlogCommunityServerApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("swk");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("swk@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "swk");
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for (DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    public void testSelectUser_Mybatis_plus(){
        // 条件封装
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",101);
        List<User> us = userMapper.selectList(wrapper);
        // [Customer(id=26, customerName=大牛哥, gender=男, telephone=66566, registerTime=null)]
        System.out.println(us);
    }

    @Test
    public void testSelectUser_Mybatis_plus_service(){
        User user = userService.findUserById(101);
        System.out.println(user);
    }
}
