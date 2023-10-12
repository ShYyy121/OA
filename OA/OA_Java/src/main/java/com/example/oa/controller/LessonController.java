package com.example.oa.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.oa.bean.Exemption;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Oa;
import com.example.oa.bean.Signin;
import com.example.oa.common.BaseResult;
import com.example.oa.mapper.LessonMapper;
import com.example.oa.service.ExemptionServiceImpl;
import com.example.oa.service.LessonServiceImpl;
import com.example.oa.service.OaServiceImpl;
import com.example.oa.service.SigninServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Autowired
    private LessonServiceImpl lessonService;
    @Autowired
    private OaServiceImpl oaService;
    @Autowired
    private SigninServiceImpl signinService;
    @Autowired
    private ExemptionServiceImpl exemptionService;


    @PostMapping("/teacher/add")
    public ResponseEntity<?> add(@RequestBody Lesson lesson) {


        // 检查同一老师的课程时间冲突
        boolean isTeacherTimeConflict = isTeacherTimeConflict(lesson);
        if (isTeacherTimeConflict) {
            return BaseResult.error("您的课程时间冲突");
        }

        lessonService.save(lesson);
        return BaseResult.ok("添加成功");
    }

    @PostMapping("/teacher/update")
    public ResponseEntity<?> update(@RequestBody Lesson lesson) {
        boolean isTeacherTimeConflict = isTeacherTimeConflict(lesson);
        if (isTeacherTimeConflict) {
            return BaseResult.error("您的课程时间冲突");
        }

        lessonService.saveOrUpdate(lesson);
        return BaseResult.ok("保存成功",lesson.id);
    }

    @GetMapping("/admin/get")
    public ResponseEntity<?> get() {
        return BaseResult.ok("获取成功", lessonService.list());
    }

    @GetMapping("/user/getBystu")
    public ResponseEntity<?> getBystu(String userid) {
        List<Lesson> lessons=lessonService.list();
        List<Oa> oas = oaService.list(new LambdaQueryWrapper<Oa>().eq(Oa::getUserid,userid));
        List<Integer> positionsToRemove = new ArrayList<>();
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = lessons.get(i);
            for (Oa oa : oas) {
                if (!oa.status.equals("审核拒绝")){
                    if (lesson.id.equals(oa.lessonId)) {
                        positionsToRemove.add(i);
                        break; // 停止内层循环，已找到匹配的OA
                    }
                }
            }
        }
        for (int i = positionsToRemove.size() - 1; i >= 0; i--) {
            int position = positionsToRemove.get(i);
            lessons.remove(position);
        }
        return BaseResult.ok("获取成功", lessons);
    }


    @GetMapping("/user/getByTime")
    public ResponseEntity<?> getByTime(String lessontime, String dayOfweek,String userid) {
        List<Oa> oaList = oaService.list(new LambdaQueryWrapper<Oa>()
                .eq(Oa::getUserid, userid)
                .eq(Oa::getStatus, "审核通过"));

        List<Lesson> lessons = new ArrayList<>();

        for (Oa oa : oaList) {
            Lesson lesson = lessonService.getById(oa.lessonId);
            if (lesson.getDayOfweek().equals(dayOfweek) && lesson.getLessontime().equals(lessontime)) {
                lessons.add(lesson);
            }
        }
        return BaseResult.ok("获取成功", lessons);
    }

    @GetMapping("/user/getById")
    public ResponseEntity<?> getById(String id,String userid) {
        List<Lesson> lessons=null;
        lessons=lessonService.list(new LambdaQueryWrapper<Lesson>().eq(Lesson::getId, id));
        List<Exemption> exemptions=exemptionService.list(new LambdaQueryWrapper<Exemption>().eq(Exemption::getUserid,userid));
        // Find exemptions to remove
        List<Lesson> lessonsToRemove = new ArrayList<>();

        for (Lesson lesson : lessons) {
            boolean hasMatchingExemption = false;
            for (Exemption exemption : exemptions) {
                if (lesson.getId().equals(exemption.getLessonId())) {
                    hasMatchingExemption = true;
                    break;
                }
            }
            if (!hasMatchingExemption) {
                System.out.println(lesson.getName());
                lessonsToRemove.add(lesson);
            }
        }


//        for (Lesson lesson : lessonsToRemove) {
//            System.out.println(lesson.toString());
//        }
        return BaseResult.ok("获取成功", lessonsToRemove);
    }

    @GetMapping("/teacher/getByTeacher")
    public ResponseEntity<?> getByTeacher(String teacher) {
        List<Lesson> lessons=null;
        lessons=lessonService.list(new LambdaQueryWrapper<Lesson>().eq(Lesson::getTeacher, teacher));
        return BaseResult.ok("获取成功",lessons);
    }

    @DeleteMapping("/teacher/delete")
    public ResponseEntity<?> delete(String id) {
        lessonService.removeById(id);
        return BaseResult.ok("删除成功");
    }

    public boolean isTeacherTimeConflict(Lesson lesson) {
        // 查询同一老师的其他课程
        List<Lesson> teacherLessons =lessonService.list(new LambdaQueryWrapper<Lesson>().eq(Lesson::getTeacher, lesson.teacher));

        // 检查是否存在时间冲突的课程
        for (Lesson teacherLesson : teacherLessons) {
            if (isTimeConflict(lesson, teacherLesson)) {
                return true; // 存在时间冲突
            }
        }

        return false; // 不存在时间冲突
    }

    private boolean isTimeConflict(Lesson lesson1, Lesson lesson2) {
        // 判断两个课程的时间是否冲突
        // 这里根据你的业务逻辑和时间表示方式进行判断
        // 可以比较课程的开始时间和结束时间，或者使用其他方式进行判断
        // 返回 true 表示时间冲突，返回 false 表示时间不冲突
        // 示例中使用字符串表示时间，可以根据实际情况修改为具体的时间类型
        if (lesson1.getLessontime().equals(lesson2.getLessontime()) && lesson1.getDayOfweek().equals(lesson2.getDayOfweek())) {
            // 时间冲突
            return true;
        }

        return false;
    }

}
