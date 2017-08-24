package com.demo;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;

import java.io.IOException;

/**
 * 断点续传
 * @author Administrator
 *
 */
public class UploadDemo2 {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq";
    String SECRET_KEY = "ZXJEPSE7rnwITOquIimBwR0oQjXDTu4zhx9TMh6q";
    //要上传的空间
    String bucketname = "space";
    //上传到七牛后保存的文件名
    String key = "pom.txt";
    //上传文件的路径
    String FilePath = "C:/Users/Administrator/Desktop/pom.txt";

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

    // 覆盖上传
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public void upload() throws IOException {
        //设置断点记录文件保存在指定文件夹或的File对象
        String recordPath = "C:/Users/Administrator/Desktop/record";
        //实例化recorder对象
        Recorder recorder = new FileRecorder(recordPath);
        //实例化上传对象，并且传入一个recorder对象
        UploadManager uploadManager = new UploadManager(c, recorder);

        try {
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
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

    public static void main(String args[]) throws IOException {
        new UploadDemo2().upload();
    }

}