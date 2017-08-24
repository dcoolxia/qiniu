package com.test;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;


public class BatchDownload {

    public static void main(String args[]) throws Exception {
        //设置需要操作的账号的AK和SK
        String ACCESS_KEY = "key";
        String SECRET_KEY = "key";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);

        //要列举文件的空间名
        String bucket = "bucket";
        
        String url = "http://www.com.cn/";
        
        try {
            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的 marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            FileListing fileListing = bucketManager.listFiles(bucket, "", "eyJjIjowLCJrIjoiZWQxMDI0NTJkYzVhNmU0ZGJhYTIxNzE5ZTdkYWViYzliMTVlNzcxOCJ9", 100, null);
            System.out.println(fileListing.marker);
            FileInfo[] items = fileListing.items;
            int i = 1;
            for (FileInfo fileInfo : items) {
                System.out.println(i++);
                if (fileInfo.key.contains("//") || fileInfo.key.contains(" ")) {
                    System.out.println(fileInfo.key);
                } else {
                    DownloadURLFile.getRemoteFile(url+fileInfo.key, "d:/img/"+ fileInfo.key);
                }
            }
            System.out.println("本次下载完成");
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}
