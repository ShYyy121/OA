package com.example.oa.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Oa")
public class Oa {
    @TableId
    public String id;
    public String userid;
    public String lessonId;

    public String teacher;
    public String status;
    public String time;
    public String reason;
    public String rejectreason;
    @TableField(exist = false)
    public User user;
    @TableField(exist = false)
    public Lesson lesson;
}
