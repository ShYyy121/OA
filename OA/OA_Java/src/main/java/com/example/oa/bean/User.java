package com.example.oa.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@TableName("user")
@Entity
public class User {
    @Id
    @GeneratedValue
    public String id;
    public String username;
    public String nickname;
    public String password;
    public String sex;
    public String face;

    public String role;
}
