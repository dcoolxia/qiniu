package com.demo;

import com.google.gson.Gson;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;


/**
 * 资源管理
 * @author Administrator
 *
 */
public class ManagerDemo {
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq";
    String SECRET_KEY = "ZXJEPSE7rnwITOquIimBwR0oQjXDTu4zhx9TMh6q";
    //要上传的空间
    String bucketname = "space";
    //上传到七牛后保存的文件名
    String key = "flower.jpg";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    
    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);

    //创建上传对象
    UploadManager uploadManager = new UploadManager(c);
    
    BucketManager bucketManager = new BucketManager(auth, c);

    public static void main(String args[]) throws IOException {
        
        new ManagerDemo().refreshUrls();
    }
    
    /**
     * 获取文件信息
     */
    public void fileInfo(){
        try {
            FileInfo fileInfo = bucketManager.stat(bucketname, key);
            System.out.println(fileInfo.hash);
            System.out.println(fileInfo.fsize);
            System.out.println(fileInfo.mimeType);
            System.out.println(fileInfo.putTime);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }
    
    /**
     * 删除文件
     */
    public void fileDelete(){
        try {
            //调用delete方法移动文件
            Response delete = bucketManager.delete(bucketname, key);
            System.out.println("返回："+delete.toString());
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println("异常："+r.statusCode);
            System.out.println("异常："+r.error);
            System.out.println("异常："+r.toString());

        }
    }
    
    /**
     * 设置或更新文件的生存时间
     */
    public void fileDeleteAfterDays(){
        int days = 1;
        try {
            bucketManager.deleteAfterDays(bucketname, key, days);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }
    
    /**
     * 获取空间文件列表
     */
    public void fileList(){
        //文件名前缀
        String prefix = "image";
        //每次迭代的长度限制，最大1000，推荐值 1000
        int limit = 1000;
        //指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
        String delimiter = ".";
        //列举空间文件列表
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketname, prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //处理获取的file list结果
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                System.out.println(item.key);
                System.out.println(item.hash);
                System.out.println(item.fsize);
                System.out.println(item.mimeType);
                System.out.println(item.putTime);
                System.out.println(item.endUser);
            }
        }
    }
    
    /**
     * 目录刷新
     */
    public void refreshDirs(){
      //待刷新的目录列表，目录必须以 / 结尾
        String[] dirs = new String[]{
                "http://oq4xdy5rg.bkt.clouddn.com/space/"
        };
        try {
            CdnManager c = new CdnManager(auth);
            //单次方法调用刷新的目录不可以超过10个，另外刷新目录权限需要联系技术支持开通
            CdnResult.RefreshResult result = c.refreshDirs(dirs);
            System.out.println(result.code);
            //获取其他的回复内容
        } catch (QiniuException e) {
            System.err.println(e.response.toString());
        }
    }
    
    /**
     * 域名刷新
     */
    public void refreshUrls(){
        //待刷新的目录列表，目录必须以 / 结尾
        String[] urls = new String[]{
                "http://oqfrd89vi.bkt.clouddn.com"
        };
        try {
            CdnManager c = new CdnManager(auth);
            //单次方法调用刷新的目录不可以超过10个，另外刷新目录权限需要联系技术支持开通
            CdnResult.RefreshResult result = c.refreshUrls(urls);
            System.out.println(result.code);
            //获取其他的回复内容
        } catch (QiniuException e) {
            System.err.println(e.response.toString());
        }
    }
    
    public void buckets(){
        try {
            String[] buckets = bucketManager.buckets();
            for(String bucket:buckets){
                System.out.println(bucket);
            }
        } catch (QiniuException e) {
            System.err.println(e.response.toString());
        }
    }
    

}
