package api;

import dao.Image;
import dao.ImageDao;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @PACKAGE_NAME: api
 * @NAME: ImageShowServlet
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/2/18
 **/
public class ImageShowServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  throws IOException {
       //也是将文件转成io流的形式
        //首先获取id，然后获取iamge，在获取路径，在输入到字节数组中，在输出到请求中
        String imageId=req.getParameter("imageId");
        if (imageId==null||imageId.equals("")){
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false,\"reason\":\"imageId输入不正确\"}");
            return;
        }
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectOne(Integer.parseInt(imageId));
        if (image==null){
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false,\"reason\":\"数据库中不存在此图片\"}");
            return;
        }
        resp.setContentType(image.getContentType());
        File file=new File(image.getPath());
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream= null;
        byte[] buffer=new byte[1204];
        while (true){
            int res = inputStream.read(buffer);
            if (res==-1){
                break;
            }
            outputStream = resp.getOutputStream();
            outputStream.write(buffer);
        }

        try {
            while (true){
                int res = inputStream.read(buffer);
                if (res==-1){
                    break;
                }
                outputStream = resp.getOutputStream();
                outputStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false,\"reason\":\"图片输出失败\"}");
        }finally {
                inputStream.close();
                outputStream.close();
        }
        resp.setStatus(200);
        resp.setContentType(image.getContentType());
    }
}
