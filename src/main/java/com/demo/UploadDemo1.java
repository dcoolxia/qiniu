package com.demo;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;


/**
 * 普通上传
 * @author Administrator
 *
 */
public class UploadDemo1 {
    //设置好账号的ACCESS_KEY和SECRET_KEY
//    String ACCESS_KEY = "l-TBo-f2VVGY_7YxL0aHpcyGBDG56ZAelXIfcDq2";
//    String SECRET_KEY = "iMyGl0I-qUE8rv9K0hTAZ4WcpkSh58yTnz3HT1Nh";
    String ACCESS_KEY = "OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq";
    String SECRET_KEY = "ZXJEPSE7rnwITOquIimBwR0oQjXDTu4zhx9TMh6q";
    //要上传的空间
//    String bucketname = "space";
    String bucketname = "open";
    //上传到七牛后保存的文件名
//    String key = "flower.jpg";
    String key = UUID.randomUUID().toString();
    //上传文件的路径
//    String FilePath = "/.../...";
    String FilePath = "C:/Users/Administrator/Desktop/file/flower.jpg";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    ///////////////////////指定上传的Zone的信息//////////////////
    //第一种方式: 指定具体的要上传的zone
    //注：该具体指定的方式和以下自动识别的方式选择其一即可
    //要上传的空间(bucket)的存储区域为华东时
    // Zone z = Zone.zone0();
    //要上传的空间(bucket)的存储区域为华北时
    // Zone z = Zone.zone1();
    //要上传的空间(bucket)的存储区域为华南时
    // Zone z = Zone.zone2();

    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);

    public static void main(String args[]) throws IOException {
        /*
         * 返回值：{"hash":"FmzNjzRk9_aHd9jUbCM5ib2lcUW-","key":"my1.jpg"}
         * 返回值：{"hash":"FmzNjzRk9_aHd9jUbCM5ib2lcUW-","key":"flower.jpg"}
         */
        
//        new UploadDemo1().uploadPath();
//        new UploadDemo1().uploadFile();
        new UploadDemo1().uploadInput();
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public void uploadPath() throws IOException {
        try {
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
            System.out.println("返回："+res);
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
    
    public void uploadFile() throws IOException {
        try {
            File file = new File(FilePath);
            //调用put方法上传
            Response res = uploadManager.put(file, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
            System.out.println("返回："+res);
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
    
    public void uploadInput() throws IOException {
        try {
            File file = new File(FilePath);
            FileInputStream inputStream = FileUtils.openInputStream(file);
            FileInputStream keyStream = FileUtils.openInputStream(file);
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            String upToken = auth.uploadToken(bucketname);
            try {
                key = DigestUtils.sha1Hex(keyStream);
                System.out.println("生成的key"+key);
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                System.out.println("返回："+response);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        }
    }
    
    

}
