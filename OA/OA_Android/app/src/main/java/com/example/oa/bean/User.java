package com.example.oa.bean;

import java.io.Serializable;

public class User implements Serializable {
    public String id;
    public String username;
    public String nickname;
    public String password;
    public String sex;
    public String face;

    public String role;

    public String getFace() {
        return face;
    }
}
