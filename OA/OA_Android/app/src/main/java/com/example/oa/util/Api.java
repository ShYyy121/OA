package com.example.oa.util;

public class Api {
//    public static final String BASE_URL = "http://192.168.1.5:7892/";
        public static final String BASE_URL = "http://172.22.55.208:7893/";
//        public static final String BASE_URL = "http://172.22.55.208:7893/";
//        public static final String BASE_URL = "http://172.18.36.75:7892/";
//        public static final String BASE_URL = "http://172.22.55.208:7893/";
    public static final String UPLOAD = "common/file/";
    public static final String LOGIN = "user/login";
    public static final String REGISTER = "user/register";
    public static final String UPDATE_USER = "user/update";

    public static final String LESSON_UPDATE = "lesson/teacher/update";
    public static final String LESSON_GET = "lesson/admin/get";
    public static final String LESSON_GETBYTIME = "lesson/user/getByTime";
    public static final String LESSON_GETBYID = "lesson/user/getById";
    public static final String LESSON_GETBYTeacher = "lesson/teacher/getByTeacher";
    public static final String LESSON_GETBYTSTU = "lesson/user/getBystu";

    public static final String LESSON_DELETE = "lesson/teacher/delete";

    public static final String OA_TeacherUPDATE = "oa/teacher/update";
    public static final String OA_UPDATE = "oa/admin/update";

    public static final String OA_ADD = "oa/user/add";
    public static final String OA_GET = "oa/admin/get";
    public static final String OA_StuGET = "oa/user/get";
    public static final String OA_TeacherGET = "oa/teacher/get";

    public static final String Sign_GET = "signin/teacher/get";
    public static final String Sign_ADD = "signin/user/add";

    public static final String EXEMPTION_ADD = "exemption/user/add";
    public static final String EXEMPTION_GET = "exemption/admin/get";
    public static final String EXEMPTION_TeacherGET = "exemption/teacher/get";
    public static final String EXEMPTION_UPDATE = "exemption/admin/update";
    public static final String EXEMPTION_TeacherUPDATE = "exemption/teacher/update";
    public static final String EXEMPTION_StuGET = "exemption/user/get";
}
