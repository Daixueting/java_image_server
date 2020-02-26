import java.io.*;

/**
 * @PACKAGE_NAME: PACKAGE_NAME
 * @NAME: Test
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/2/19
 **/
public class Test {
    public static void main(String[] args) {
        String path="\\image\\"+"daixueting\\"+"lihao";
        File file =new File(path);
        file.getParentFile().mkdirs();
        System.out.println(file.getPath());
    }
}
