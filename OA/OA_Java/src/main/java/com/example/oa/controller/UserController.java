package com.example.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.example.oa.Filter.CustomUserDetails;
import com.example.oa.Filter.CustomUserDetails;
import com.example.oa.bean.User;
import com.example.oa.common.BaseResult;
import com.example.oa.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/login")
    public ResponseEntity<?> login(String username, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username).eq(User::getPassword, password);
        User one = userService.getOne(queryWrapper);
        if (one == null) {
            return BaseResult.error("用户名或密码错误");
        } else {
            System.out.println(one);
//             创建授权信息
            CustomUserDetails userDetails = new CustomUserDetails(one);
            // 在登录成功后进行身份验证和授权
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // 创建密码加密器
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 比对密码
//            if (passwordEncoder.matches(password, one.getPassword())) {
            if (password.equals(one.getPassword())){
                // 密码匹配，登录成功
                return BaseResult.ok("登录成功",one);
            } else {
                // 密码不匹配，登录失败
                return BaseResult.error("用户名或密码错误");
            }

        }
    }

    @GetMapping("/getUserById")
    public ResponseEntity<?> getUserById(String userid) {
        User one = userService.getById(userid);
        return BaseResult.ok("", one);
    }
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User one = userService.getOne(queryWrapper);
        if (one != null) {
            return BaseResult.error("手机号已存在");
        } else {
            if (user.getUsername().equals("admin")){
                user.role= String.valueOf(2);
            } else if (user.getUsername().startsWith("teacher")) {
                user.role= String.valueOf(1);
            }else {
                user.role= String.valueOf(0);
            }
//            // 创建密码加密器
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//// 对密码进行加密
//            String encryptedPassword = passwordEncoder.encode(user.password);
//// 存储加密后的密码
//            user.setPassword(encryptedPassword);
            userService.save(user);
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getId, user.getId());
            User one1 = userService.getOne(queryWrapper);
            if (one1!=null){
                return BaseResult.ok("注册成功");
            }
                boolean save = userService.save(user);
                if (save) {

                } else {
                    return BaseResult.error("注册失败");
                }
                return BaseResult.ok("注册成功");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user) {
        boolean save = userService.updateById(user);
        if (save) {
            return BaseResult.ok("更新成功");
        } else {
            return BaseResult.error("更新失败");
        }
    }

}
