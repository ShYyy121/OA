package com.example.oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oa.bean.Exemption;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.User;
import com.example.oa.common.BaseResult;
import com.example.oa.service.ExemptionServiceImpl;
import com.example.oa.service.LessonServiceImpl;
import com.example.oa.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/exemption")
public class ExemptionController {
    @Autowired
    private ExemptionServiceImpl ExemptionService;
    @Autowired
    private LessonServiceImpl lessonService;

    @Autowired
    private UserServiceImpl userService;


    @PostMapping("/user/add")
    public ResponseEntity<?> add(@RequestBody Exemption exemption) {

        List<Exemption> Exemptions= ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq(Exemption::getUserid,exemption.userid).ne(Exemption::getStatus, "已拒绝"));
        Lesson lesson = lessonService.getById(exemption.getLessonId());
        for (Exemption o : Exemptions) {
            Lesson l = lessonService.getById(o.getLessonId());
            if (l.lessontime.equals(lesson.lessontime) && l.dayOfweek.equals(lesson.dayOfweek)) {
                return BaseResult.error("您已申请过了");
            }
        }

        Exemption one = ExemptionService.getOne(new LambdaQueryWrapper<Exemption>().eq(Exemption::getUserid, exemption.userid).eq(Exemption::getLessonId, exemption.lessonId));
        if (one != null) {
            return BaseResult.error("申请失败，重复申请");
        }
        exemption.status = "审核中";
        ExemptionService.save(exemption);
        return BaseResult.ok("申请成功");
    }

    @PostMapping("/admin/update")
    public ResponseEntity<?> update(@RequestBody Exemption Exemption) {
        ExemptionService.updateById(Exemption);
        return BaseResult.ok("修改成功");
    }
    @PostMapping("/teacher/update")
    public ResponseEntity<?> updateByTeacher(@RequestBody Exemption Exemption) {
        ExemptionService.updateById(Exemption);
        return BaseResult.ok("修改成功");
    }

    @GetMapping("/admin/get")
    public ResponseEntity<?> get(String userid) {
        System.out.println(userid);
        List<Exemption> ExemptionList=null;
        if (userid.equals("admin")){
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq(!userid.equals("admin"), Exemption::getUserid, userid));
            List<Integer> positionsToRemove = new ArrayList<>();

            for (int i = 0; i < ExemptionList.size(); i++) {
                Exemption Exemption = ExemptionList.get(i);
                if (Exemption.status.equals("审核中")) {
                    positionsToRemove.add(i);
                }
            }

            // 从后往前移除元素，避免索引位置变化的影响
            for (int i = positionsToRemove.size() - 1; i >= 0; i--) {
                int position = positionsToRemove.get(i);
                ExemptionList.remove(position);
            }
        }else if (userid.startsWith("teacher")){
             ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq( Exemption::getTeacher, userid));
        }else{
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq( Exemption::getUserid, userid));
        }
        return getUserInfo(ExemptionList);
    }

    @GetMapping("/teacher/get")
    public ResponseEntity<?> getByteacher(String userid) {
        System.out.println(userid);
        List<Exemption> ExemptionList=null;
        if (userid.equals("admin")){
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq(!userid.equals("admin"), Exemption::getUserid, userid));
            List<Integer> positionsToRemove = new ArrayList<>();

            for (int i = 0; i < ExemptionList.size(); i++) {
                Exemption Exemption = ExemptionList.get(i);
                if (Exemption.status.equals("审核中")) {
                    positionsToRemove.add(i);
                }
            }

            // 从后往前移除元素，避免索引位置变化的影响
            for (int i = positionsToRemove.size() - 1; i >= 0; i--) {
                int position = positionsToRemove.get(i);
                ExemptionList.remove(position);
            }
        }else if (userid.startsWith("teacher")){
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq( Exemption::getTeacher, userid));
        }else{
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq( Exemption::getUserid, userid));
        }
        return getUserInfo(ExemptionList);
    }
    @GetMapping("/user/get")
    public ResponseEntity<?> getBystu(String userid) {
        System.out.println(userid);
        List<Exemption> ExemptionList=null;
        if (userid.equals("admin")){
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq(!userid.equals("admin"), Exemption::getUserid, userid));
            List<Integer> positionsToRemove = new ArrayList<>();

            for (int i = 0; i < ExemptionList.size(); i++) {
                Exemption Exemption = ExemptionList.get(i);
                if (Exemption.status.equals("审核中")) {
                    positionsToRemove.add(i);
                }
            }

            // 从后往前移除元素，避免索引位置变化的影响
            for (int i = positionsToRemove.size() - 1; i >= 0; i--) {
                int position = positionsToRemove.get(i);
                ExemptionList.remove(position);
            }
        }else if (userid.startsWith("teacher")){
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq( Exemption::getTeacher, userid));
        }else{
            ExemptionList = ExemptionService.list(new LambdaQueryWrapper<Exemption>().eq( Exemption::getUserid, userid));
        }
        return getUserInfo(ExemptionList);
    }
    private ResponseEntity<?> getUserInfo(List<Exemption> ExemptionList) {
        for (Exemption Exemption : ExemptionList) {
            User user = userService.getById(Exemption.userid);
            Lesson lesson = lessonService.getById(Exemption.lessonId);
            Exemption.user = user;
            Exemption.lesson = lesson;
            Exemption.teacher=lesson.teacher;
        }
        return BaseResult.ok("获取成功", ExemptionList);
    }


}
