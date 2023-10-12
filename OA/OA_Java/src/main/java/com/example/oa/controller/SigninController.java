package com.example.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Oa;
import com.example.oa.bean.Signin;
import com.example.oa.bean.User;
import com.example.oa.common.BaseResult;
import com.example.oa.service.LessonServiceImpl;
import com.example.oa.service.SigninServiceImpl;
import com.example.oa.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signin")
public class SigninController {
    @Autowired
    private SigninServiceImpl signinService;
    @Autowired
    private LessonServiceImpl lessonService;

    @Autowired
    private UserServiceImpl userService;


    @PostMapping("/user/add")
    public ResponseEntity<?> add(@RequestBody Signin signin) {
        Signin signin1=signinService.getOne(new LambdaQueryWrapper<Signin>().eq(Signin::getStudentid, signin.studentid).eq(Signin::getLessonid, signin.lessonid));
        if (signin1!=null ){
            if (signin1.date.equals(signin.date))
            return BaseResult.error("今日您已在该课签过到了，请勿重复签到");
        }
        signinService.save(signin);

        Lesson lesson = lessonService.getById(signin.lessonid);
        return BaseResult.ok("您在"+lesson.name+"课上成功签到");
    }

    @PostMapping("/user/update")
    public ResponseEntity<?> update(@RequestBody Signin signin) {
        signinService.saveOrUpdate(signin);
        return BaseResult.ok("保存成功");
    }

    @GetMapping("/teacher/get")
    public ResponseEntity<?> get(String userid) {
        List<Signin> sList=null;
        sList = signinService.list(new LambdaQueryWrapper<Signin>().eq( Signin::getTeacher, userid));
        return getUserInfo(sList);
    }


    @DeleteMapping("/teacher/delete")
    public ResponseEntity<?> delete(String id) {
        signinService.removeById(id);
        return BaseResult.ok("删除成功");
    }

    private ResponseEntity<?> getUserInfo(List<Signin> sList) {
        for (Signin signin : sList) {
            User user = userService.getById(signin.studentid);
            Lesson lesson = lessonService.getById(signin.lessonid);
            signin.user = user;
            signin.lesson = lesson;
        }
        return BaseResult.ok("获取成功", sList);
    }
}
