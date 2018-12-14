
import upload.UploadUtil;

public class UploadDemo {

    public static void main(String[] args) throws Exception {
        testUpload();
    }


    public static void testUpload() {
        String filePath = "test1.jpg";
        long startTime = System.currentTimeMillis();

        String server = null;//http://0.0.0.0:xxxx/upload
        String result = null;
        try {
            result = UploadUtil.upload(server, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("consume time:" + (endTime - startTime) + "ms" + " result:" + result);
    }

}
