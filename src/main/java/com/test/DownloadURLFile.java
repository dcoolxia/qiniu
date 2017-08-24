package com.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class DownloadURLFile {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        String url = "http://oq4xdy5rg.bkt.clouddn.com/flower.jpg?e=30&token=OkICyPFGQmLcO5RTA3CjzPJD9V4d4n5pcWhBJdkq:oZqS6-KaD_p0ovlaIt6vdBskXIg=";
        url = "http://oqfrd89vi.bkt.clouddn.com/6ccd8f3464f7f68777d8d46c233989bda57145be";
//        String res1 = downloadFromUrl(url,"d:/", "flower.jpg");
//        System.out.println(res1);
        
//        String res2 = getFileFromUrl(url,"d:/", "flower.jpg");
//        System.out.println(res2);
        
        getRemoteFile(url, "d:/flower.jpg");
    }

    public static String downloadFromUrl(String url, String dir, String fileName) {

        try {
            URL httpurl = new URL(url);
            // String fileName = getFileNameFromUrl(url);
            File file = new File(dir + fileName);
            FileUtils.copyURLToFile(httpurl, file);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }
    
    public static String getFileFromUrl(String url, String dir, String fileName) {

        try {
            URL httpurl = new URL(url);
            // String fileName = getFileNameFromUrl(url);
            File file1 = new File(dir + fileName);
            File file2 = FileUtils.toFile(httpurl);
            System.out.println(file2);
//            FileUtils.copyFile(file2, file1);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }

    public static String getFileNameFromUrl(String url) {
        String name = new Long(System.currentTimeMillis()).toString() + ".X";
        int index = url.lastIndexOf("/");
        if (index > 0) {
            name = url.substring(index + 1);
            if (name.trim().length() > 0) {
                return name;
            }
        }
        return name;
    }
    
    /**
     * 通过HTTP方式获取文件
     *    
     * @param strUrl
     * @param fileName
     * @return
     * @throws IOException
     */
   public static boolean getRemoteFile(String strUrl, String fileName) throws Exception {
     URL url = new URL(strUrl);
     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
     InputStream input = new DataInputStream(conn.getInputStream());
//     DataInputStream input = new DataInputStream(conn.getInputStream());
     File file = new File(fileName);
     //通过工具类写出
     FileUtils.copyInputStreamToFile(input, file);
     
     //通过流写出
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