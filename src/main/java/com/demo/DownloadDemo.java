package com.demo;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.qiniu.cdn.CdnManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

/**
 * 下载 
 * @author Administrator
 *
 */
public class DownloadDemo {
    //设置好账号的ACCESS_KEY和SECRET_KEY
//    String ACCESS_KEY = "l-TBo-f2VVGY_7YxL0aHpcyGBDG56ZAelXIfcDq2";
//    String SECRET_KEY = "iMyGl0I-qUE8rv9K0hTAZ4WcpkSh58yTnz3HT1Nh";
    String ACCESS_KEY = "OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq";
    String SECRET_KEY = "ZXJEPSE7rnwITOquIimBwR0oQjXDTu4zhx9TMh6q";
    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //构造私有空间的需要生成的下载的链接
//    String URL = "http://bucketdomain/key";
    String URL = "http://oq4xdy5rg.bkt.clouddn.com/image-cat.jpg";
//    String URL = "http://oq4xdy5rg.bkt.clouddn.com/book.txt";
    String STYLE = "?imageMogr2/auto-orient/thumbnail/!50p/blur/1x0/quality/75|watermark/2/text/5aWz56We572R/font/"
            + "5a6L5L2T/fontsize/240/fill/IzhERENEMw==/dissolve/50/gravity/SouthEast/dx/10/dy/10|imageslim";

    public static void main(String args[]) throws Exception {
//        new DownloadDemo().getRemoteFile("d:/flower.jpg");
        new DownloadDemo().download();
        /*
         * http://oq4xdy5rg.bkt.clouddn.com/flower.jpg?e=30&token=OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq:oZqS6-KaD_p0ovlaIt6vdBskXIg=
         */
    }

    public void download() {
        //调用privateDownloadUrl方法生成下载链接,第二个参数可以设置Token的过期时间
        String downloadRUL = auth.privateDownloadUrl(URL, 30);
        System.out.println(downloadRUL);
    }
    
    /**
     * 防盗链
     */
    public void timestampAntiLeechUrl() {
        String host = "http://video.example.com";
        String fileName = "基本概括.mp4";
        //查询参数
        StringMap queryStringMap = new StringMap();
        queryStringMap.put("name", "七牛");
        queryStringMap.put("year", 2017);
        queryStringMap.put("年龄", 28);
        //链接过期时间
        long deadline = System.currentTimeMillis() / 1000 + 3600;
        //签名密钥，从后台域名属性中获取
        String encryptKey = "xxx";
        String signedUrl;
        try {
            signedUrl = CdnManager.createTimestampAntiLeechUrl(host, fileName,
                    queryStringMap, encryptKey, deadline);
            System.out.println(signedUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
   /**
    * 下载返回InputStream流
    * @param strUrl
    * @param fileName
    * @return
    * @throws Exception
    */
   public boolean getRemoteFile(String fileName) throws Exception {
     String downloadRUL = auth.privateDownloadUrl(URL+STYLE, 3600);
     URL url = new URL(downloadRUL);
     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
     InputStream input = new DataInputStream(conn.getInputStream());
//     DataInputStream input = new DataInputStream(conn.getInputStream());
     File file = new File(fileName);
     FileUtils.copyInputStreamToFile(input, file);
     
//     DataOutputStream output = new DataOutputStream(new FileOutputStream(fileName));
//     byte[] buffer = new byte[1024 * 8];
//     int count = 0;
//     while ((count = input.read(buffer)) > 0) {
//       output.write(buffer, 0, count);
//     }
//     output.close();
//     input.close();
     return true;
   }
}