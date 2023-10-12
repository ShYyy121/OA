package com.example.oa.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Lesson")
public class Lesson {
    @TableId
    public String id;
    public String name;
    public String teacher;
    public String starTime;
    public String dayOfweek;
    public String des;
    public String location;
    public String lessontime;
}
