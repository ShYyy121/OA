package com.example.oa.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.oa.bean.User;
import com.example.oa.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>{
}
