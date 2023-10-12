package com.example.oa.bean;

import java.io.Serializable;

public class Lesson implements Serializable {
    public String id;
    public String name;
    public String teacher;
    public String starTime;
    public String dayOfweek;
    public String des;
    public String location;
    public String lessontime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getDayOfweek() {
        return dayOfweek;
    }

    public void setDayOfweek(String dayOfweek) {
        this.dayOfweek = dayOfweek;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLessontime() {
        return lessontime;
    }

    public void setLessontime(String lessontime) {
        this.lessontime = lessontime;
    }
}
