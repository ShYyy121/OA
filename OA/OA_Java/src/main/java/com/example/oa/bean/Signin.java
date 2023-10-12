package com.example.oa.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Signin")
public class Signin {
    @TableId
    public String id;
    public String student;
    public String studentid;
    public String teacher;
    public String time;
    public String issigned;
    public String date;
    public String lessonid;
    @TableField(exist = false)
    public User user;
    @TableField(exist = false)
    public Lesson lesson;
}
