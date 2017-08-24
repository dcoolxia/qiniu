package com.demo;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.storage.model.FileListing;
import com.qiniu.util.Auth;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;


public class ListDemo {

    public static void main(String args[]) {
        //设置需要操作的账号的AK和SK
        String ACCESS_KEY = "OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq";
        String SECRET_KEY = "ZXJEPSE7rnwITOquIimBwR0oQjXDTu4zhx9TMh6q";
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

        Zone z = Zone.autoZone();
        Configuration c = new Configuration(z);

        //实例化一个BucketManager对象
        BucketManager bucketManager = new BucketManager(auth, c);

        //要列举文件的空间名
        String bucket = "space";

        try {
            //调用listFiles方法列举指定空间的指定文件
            //参数一：bucket    空间名
            //参数二：prefix    文件名前缀
            //参数三：marker    上一次获取文件列表时返回的 marker
            //参数四：limit     每次迭代的长度限制，最大1000，推荐值 100
            //参数五：delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
            FileListing fileListing = bucketManager.listFiles(bucket, "mall-", null, 10, null);
            FileInfo[] items = fileListing.items;
            for (FileInfo fileInfo : items) {
                System.out.println(fileInfo.key);
            }
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            System.out.println(r.toString());
        }
    }
}
