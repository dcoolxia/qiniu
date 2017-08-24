package com.test;

import com.qiniu.util.Auth;

public class Test {

    public static void main(String[] args) throws Exception {
        String ACCESS_KEY = "OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq";
        String SECRET_KEY = "ZXJEPSE7rnwITOquIimBwR0oQjXDTu4zhx9TMh6q";
        String bucket = "space";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
    }

}
