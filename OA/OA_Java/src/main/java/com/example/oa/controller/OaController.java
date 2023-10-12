package com.example.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oa.bean.*;
import com.example.oa.common.BaseResult;
import com.example.oa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/oa")
public class OaController {
    @Autowired
    private OaServiceImpl oaService;
    @Autowired
    private LessonServiceImpl lessonService;

    @Autowired
    private UserServiceImpl userService;


    @PostMapping("/user/add")
    public ResponseEntity<?> add(@RequestBody Oa oa) {

        List<Oa> oas= oaService.list(new LambdaQueryWrapper<Oa>().eq(Oa::getUserid,oa.userid).ne(Oa::getStatus, "已拒绝"));
        Lesson lesson = lessonService.getById(oa.getLessonId());
        for (Oa o : oas) {
            Lesson l = lessonService.getById(o.getLessonId());
            if (l.lessontime.equals(lesson.lessontime) && l.dayOfweek.equals(lesson.dayOfweek)) {
                return BaseResult.error("申请失败，时间冲突");
            }
        }

        Oa one = oaService.getOne(new LambdaQueryWrapper<Oa>().eq(Oa::getUserid, oa.userid).eq(Oa::getLessonId, oa.lessonId));
        if (one != null) {
            return BaseResult.error("申请失败，重复申请");
        }
        oa.status = "审核中";
        oaService.save(oa);
        return BaseResult.ok("申请成功");
    }

    @PostMapping("/admin/update")
    public ResponseEntity<?> update(@RequestBody Oa oa) {
        oaService.updateById(oa);
        return BaseResult.ok("修改成功");
    }
    @PostMapping("/teacher/update")
    public ResponseEntity<?> updateByTeacher(@RequestBody Oa oa) {
        oaService.updateById(oa);
        return BaseResult.ok("修改成功");
    }

    @GetMapping("/admin/get")
    public ResponseEntity<?> get(String userid) {
        System.out.println(userid);
        List<Oa> oaList=null;
        if (userid.equals("admin")){
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq(!userid.equals("admin"), Oa::getUserid, userid));
            List<Integer> positionsToRemove = new ArrayList<>();

            for (int i = 0; i < oaList.size(); i++) {
                Oa oa = oaList.get(i);
                if (oa.status.equals("审核中")) {
                    positionsToRemove.add(i);
                }
            }

            // 从后往前移除元素，避免索引位置变化的影响
            for (int i = positionsToRemove.size() - 1; i >= 0; i--) {
                int position = positionsToRemove.get(i);
                oaList.remove(position);
            }
        }else if (userid.startsWith("teacher")){
             oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq( Oa::getTeacher, userid));
        }else{
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq( Oa::getUserid, userid));
        }
        return getUserInfo(oaList);
    }

    @GetMapping("/teacher/get")
    public ResponseEntity<?> getByteacher(String userid) {
        System.out.println(userid);
        List<Oa> oaList=null;
        if (userid.equals("admin")){
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq(!userid.equals("admin"), Oa::getUserid, userid));
            List<Integer> positionsToRemove = new ArrayList<>();

            for (int i = 0; i < oaList.size(); i++) {
                Oa oa = oaList.get(i);
                if (oa.status.equals("审核中")) {
                    positionsToRemove.add(i);
                }
            }

            // 从后往前移除元素，避免索引位置变化的影响
            for (int i = positionsToRemove.size() - 1; i >= 0; i--) {
                int position = positionsToRemove.get(i);
                oaList.remove(position);
            }
        }else if (userid.startsWith("teacher")){
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq( Oa::getTeacher, userid));
        }else{
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq( Oa::getUserid, userid));
        }
        return getUserInfo(oaList);
    }
    @GetMapping("/user/get")
    public ResponseEntity<?> getBystu(String userid) {
        System.out.println(userid);
        List<Oa> oaList=null;
        if (userid.equals("admin")){
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq(!userid.equals("admin"), Oa::getUserid, userid));
            List<Integer> positionsToRemove = new ArrayList<>();

            for (int i = 0; i < oaList.size(); i++) {
                Oa oa = oaList.get(i);
                if (oa.status.equals("审核中")) {
                    positionsToRemove.add(i);
                }
            }

            // 从后往前移除元素，避免索引位置变化的影响
            for (int i = positionsToRemove.size() - 1; i >= 0; i--) {
                int position = positionsToRemove.get(i);
                oaList.remove(position);
            }
        }else if (userid.startsWith("teacher")){
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq( Oa::getTeacher, userid));
        }else{
            oaList = oaService.list(new LambdaQueryWrapper<Oa>().eq( Oa::getUserid, userid));
        }
        return getUserInfo(oaList);
    }
    private ResponseEntity<?> getUserInfo(List<Oa> oaList) {
        for (Oa oa : oaList) {
            User user = userService.getById(oa.userid);
            Lesson lesson = lessonService.getById(oa.lessonId);
            oa.user = user;
            oa.lesson = lesson;
            oa.teacher=lesson.teacher;
        }
        return BaseResult.ok("获取成功", oaList);
    }


}
