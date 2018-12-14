package upload;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;


public class EncryptUtil {

    public static String getEncryptToken() throws Exception {
        //URL url = new URL("http://192.168.8.127:9999/getToken1");
        URL url = new URL("http://59.110.227.24:9999/getToken1");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10 * 1000);
        conn.setDoInput(true);
        conn.setRequestProperty("charset", "UIF-8");
        conn.setRequestProperty("Content-Type", "text/plain");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        //BigInteger b = new BigInteger(128, new Random());
        reader.close();
        conn.disconnect();
        System.out.println("getEncryptToken response:" + response.toString());

        return response.toString();
    }


    public static byte[] encrypt(byte[] bytes, String encryptToken) {
        BigInteger b = new BigInteger(encryptToken);
        System.out.println("encrypt token:" + b);
        byte[] tokens = b.toByteArray();
        int tokensLength = tokens.length;

        byte[] result = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            result[i] = (byte) (bytes[i] ^ tokens[i % tokensLength]);
        }

        return result;
    }
}
