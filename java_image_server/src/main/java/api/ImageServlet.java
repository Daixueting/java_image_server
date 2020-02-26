package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.Image;
import dao.ImageDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @PACKAGE_NAME: api
 * @NAME: ImageServlet
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/2/16
 **/
public class ImageServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //分为全部查询和查询一个
        String imageId=req.getParameter("imageId");
        if(imageId==null){
            selectAll(req,resp);
        }else if (imageId.equals("")){
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false, \"reason\":\"imageId输入不正确\"}");
            return;
        } else{
            selcetOne(imageId,resp);
        }
    }

    private void selcetOne(String imageId, HttpServletResponse resp) {
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectOne(Integer.parseInt(imageId));
        Gson gson=new GsonBuilder().create();
        try {
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            String jsonData = gson.toJson(image);
            resp.getWriter().write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectAll(HttpServletRequest req, HttpServletResponse resp) {
        ImageDao imageDao=new ImageDao();
        List<Image> images=imageDao.selectAll();
        Gson gson=new GsonBuilder().create();
        try {
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            String jsonData = gson.toJson(images);
            resp.getWriter().write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gson.toJson(images);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //使用的是fileupload的jar包将图片编程流的形式进行传输,理论上是支持每次传送多个文件，但是我们规定每次传送一个文件
        DiskFileItemFactory diskFileItemFactory=new DiskFileItemFactory();
        ServletFileUpload servletFileUpload=new ServletFileUpload(diskFileItemFactory);
        List<FileItem> fileItems=null;

        try {
            fileItems=servletFileUpload.parseRequest(req);
        } catch (FileUploadException e) {
            e.printStackTrace();
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false ,\"reason\":\"请求解析失败了\"}");
            return;
        }
        FileItem item=fileItems.get(0);

        if (item.getSize()==0){
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false, \"reason\":\"上传文件不能为空\"}");
            return;
        }
        //将FileItem对象转化成为image对象存入数据库
        Image image=new Image();
        image.setImageName(item.getName());
        image.setSize((int) item.getSize());
        image.setContentType(item.getContentType());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        image.setUploadTime(simpleDateFormat.format(new Date()));
        //按照图片内容计算md5
        image.setMd5(DigestUtils.md5Hex(item.get()));
        image.setPath("image"+File.separator+ image.getMd5());
        ImageDao imageDao=new ImageDao();
        imageDao.insert(image);
        //在存入磁盘上,首先判断md5是否存在
      //  Image imageMd5=imageDao.selectMd5(DigestUtils.md5Hex(item.get()));
      //  if (imageMd5==null) {
            File file = new File(image.getPath());
            file.getParentFile().mkdirs();
            try {
                item.write(file);
            } catch (Exception e) {
                e.printStackTrace();

                resp.setContentType("application/json; charset=utf-8");
                resp.getWriter().write("{ \"ok\": false, \"reason\": \"写磁盘失败\" }");
                return;
            }
     //    }
//        resp.setStatus(200);
//        resp.setContentType("application/json; charset=utf-8");
//        resp.getWriter().write("{ \"ok\": true }");
          resp.sendRedirect("index.html");//重定向
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //根据id要进行数据删除和磁盘删除
        String imageId=req.getParameter("imageId");
        //先验证id的正确性
        if (imageId==null||imageId.equals("")){
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false,\"reason\":\"imageId输入有错误\"}");
            return;
        }
         //数据库删除,首先保证数据库中有这个数据
        ImageDao imageDao=new ImageDao();
        Image image=imageDao.selectOne(Integer.parseInt(imageId));
        if (image==null){
            resp.setStatus(200);
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().write("{\"OK\":false,\"reason\":\"数据库中不存在这imageId\"}");
            return;
        }
        imageDao.delete(Integer.parseInt(imageId));
        //进行磁盘删除,也要确定数据库中是否还存在md5，如果存在 就不进行删除
        Image imageMd5=imageDao.selectMd5(image.getMd5());
        if (imageMd5 == null) {
            File file=new File(image.getPath());
            file.delete();
         }
        resp.setStatus(200);
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write("{\"OK\":true}");
    }

}


