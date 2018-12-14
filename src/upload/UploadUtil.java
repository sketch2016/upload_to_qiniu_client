package upload;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadUtil {

    private static final String SERVER = "http://59.110.227.24:9999/uploadfile";

    //private static final String SERVER = "http://localhost:9999/uploadfile";

    public static String upload(String server, String fileName) throws Exception {

        server = (server == null ? SERVER : server);
        String encryptToken = EncryptUtil.getEncryptToken();

        URL url = new URL(server);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setReadTimeout(30 * 1000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("charset", "UIF-8");
        //set boundary
        String boundary = "----------" + System.currentTimeMillis();
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        //设置正文头
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(boundary);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");
        sb.append("\r\n");
        //sb.append("Content-Type:image/*\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        BufferedOutputStream bos = new BufferedOutputStream(conn.getOutputStream());
        // 输出表头
        bos.write(head);

        /*
        BufferedInputStream bis = new BufferedInputStream(getFileInputStream(fileName));
        byte[] buff = new byte[1024 * 8];
        int n = -1;
        while ((n = bis.read(buff))!= -1) {
            bos.write(buff, 0, n);
        }
        */
        //加密数据
        //byte[] data = RsaUtil.publicEncrypt(RsaUtil.file2Byte(new File(fileName)));
        byte[] data = EncryptUtil.encrypt(file2Byte(new File(fileName)), encryptToken);
        bos.write(data);

        // 结尾部分
        byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        bos.write(foot);

        //关闭流
        bos.flush();
        bos.close();
        //bis.close();

        System.out.println("send file success");

        //read response
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        System.out.println("response:" + response.toString());

        //断开连接
        conn.disconnect();

        return response.toString();
    }

    private static FileInputStream getFileInputStream(String fileName) throws FileNotFoundException {
        //String path = new File("").getAbsolutePath();
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);

        return fis;
    }

    private static byte[] file2Byte(File file) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 8];
        int n = -1;
        while ((n = bis.read(buff)) != -1) {
            out.write(buff, 0, n);
        }
        byte[] ret = out.toByteArray();

        out.close();
        bis.close();

        return ret;
    }


}
