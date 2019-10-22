package scanfu.com.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

//import weishang.com.weishang.bean.MyApplication;
//import weishang.com.weishang.bean.ReportResonItem;


/**
 * Created by Administrator on 2016/11/24 0024.
 */

public class HttpUtils {
    private static final int TIME_OUT = 2 * 60 * 60 * 1000;//超时时间
    private static final String CHARSET = "utf-8";//设置编码

    public static String httpGet(String url) {

        String res = null;

        if (url == null || url.length() <= 0)
            return res;
        HttpGet getMethod = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(getMethod); //发起GET请求
            res = EntityUtils.toString(response.getEntity(), "utf-8");//获取服务器响应内容

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    public static String httpPost(String url, List<NameValuePair> par) {
        String res = null;
        if (url == null) {
            return res;
        }
        HttpPost postMethod = new HttpPost(url);
        try {
            postMethod.setEntity(new UrlEncodedFormEntity(par, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(postMethod);
            res = EntityUtils.toString(httpResponse.getEntity(), "utf-8");//获取服务器响应内容

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String httpPost(String urlstr, String content) {

        HttpURLConnection conn = null;
        String json = "";
        OutputStream os;
        int result;

        try {
            URL url = new URL(urlstr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(7000);
            conn.setReadTimeout(7000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            os = conn.getOutputStream();

            os.write(content.getBytes("utf-8"));
            os.close();
            conn.connect();
            result = conn.getResponseCode();
            if (result == 200) {
                InputStream in = conn.getInputStream();
                json = new String(LoadUtils.load(in));
            }
        } catch (Exception e) {
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return json;


    }

    public static String httpUpLoad(File file, String urlStr, String json)
            throws IOException {
        final String BOUNDARYSTR = UUID.randomUUID().toString();
        final String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
        HttpURLConnection conn;
        // URL url = new
        // URL("http://localhost/kaopurServer/rest/upload.do?validuid=40324032");
        URL url = new URL(urlStr);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true); // 允许输出流
        conn.setRequestMethod("POST"); // 请求方式
        conn.setRequestProperty("Charset", "utf-8"); // 设置编码
        conn.setRequestProperty("Content-Type", "multipart/mixed;boundary="
                + BOUNDARYSTR);
        final BufferedOutputStream dos = new BufferedOutputStream(
                conn.getOutputStream());

        // 文本段
        StringBuffer sb = new StringBuffer();
        sb.append(BOUNDARY);
        sb.append("Content-Disposition: form-data; name=\"jsonbody\"\r\n");
        sb.append("Content-Type: application/json; charset=UTF-8\r\n\r\n");
        sb.append(json);
        // sb.append("{\"mediaType\":\"image\",\"category\":\"gravatar\",\"flag\":\"1\"}");
        sb.append("\r\n");
        dos.write(sb.toString().getBytes("utf-8"));
        // 文本段

        // ---第二个参数
        sb.append(BOUNDARY);
        sb.append("Content-Disposition:form-data;name=\"");
        sb.append("orgcode");
        sb.append("\"\r\n\r\n");
        sb.append("00");
        sb.append("\r\n");
        // ---第二个参数

        // multi-part
        // final File file = new File("C:/IMG_2072.JPG");
        // final File file = new File("C:/xiaoxin.jpg");
        dos.write(BOUNDARY.getBytes("utf-8"));
        StringBuffer filenamesb = new StringBuffer();
        filenamesb
                .append("Content-Disposition: form-data; Content-Type: text/plain; name=\"attach\"; filename=\""
                        + file.getName() + "\"\r\n\r\n");
        dos.write(filenamesb.toString().getBytes("utf-8"));

        try {
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            dos.write("\r\n".getBytes());
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // multi-part

        dos.write(("--" + BOUNDARYSTR + "--\r\n").getBytes());
        dos.flush();
        dos.close();
        int i = conn.getResponseCode();
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String line;
        StringBuffer sb2 = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            sb2.append(line);
        }
        rd.close();

        return sb2.toString();
    }


    public static String uploadFile(File headFile, String urlStr, String jsonStr, String attach, String type) {
        String result = null;
        HttpURLConnection conn = null;
        int res = 0;

        final String BOUNDARYSTR = UUID.randomUUID().toString();
        final String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
        String CONTENT_TYPE = "multipart/form-data";//内容类型
        InputStream is = null;
        InputStream input = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);//允许输入流
            conn.setDoOutput(true);//允许输出流
            conn.setUseCaches(false);//不允许使用缓存
            conn.setRequestMethod("POST");//请求方式:GET、POST
            conn.setRequestProperty("Charset", CHARSET);//设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARYSTR);

            final BufferedOutputStream dos = new BufferedOutputStream(conn.getOutputStream());
            StringBuffer sb = new StringBuffer();

            //设置参数
            sb.append(BOUNDARY);
            sb.append("Content-Disposition: form-data;name=\"jsonbody\"\r\n\r\n" + jsonStr);
            sb.append("\r\n");

            dos.write(sb.toString().getBytes(CHARSET));
            dos.write(BOUNDARY.getBytes(CHARSET));

            StringBuffer filenamesb = new StringBuffer();
            filenamesb.append("Content-Disposition: form-data;Content-type:" + type +
                    ";name=\"attach\";filename=\"" + headFile.getName() + "\r\n\r\n");

            dos.write(filenamesb.toString().getBytes(CHARSET));

            //输出文件
            try {
                is = new FileInputStream(headFile);
                byte[] bytes = new byte[512];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                dos.write("\r\n".getBytes());

            } catch (Exception e) {
                result = "上传失败" + e.getMessage();
                if (is != null) {
                    is.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
                return result;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            dos.write(("--"+BOUNDARYSTR+"--\r\n").getBytes());
            dos.flush();
            dos.close();
            res=conn.getResponseCode();
            if(res==200){
                input = conn.getInputStream();
                StringBuffer sb1=new StringBuffer();
                int ss;
                while((ss=input.read())!=-1){
                    sb1.append((char)ss);
                }
                result=sb1.toString();
            }else{
                result="上传失败，请重新上传！";
            }

        } catch (Exception e) {
            result="上传失败:"+e.getMessage();
        }finally {
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                conn.disconnect();
                conn=null;
            }
        }
        return result;
    }
}
