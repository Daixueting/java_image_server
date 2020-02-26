package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.JavaImageServerException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: dao
 * @NAME: UtilDao
 * @USER: 代学婷
 * @DESCRIPTION:
 * @DATE: 2020/2/16
 **/
public class ImageDao {

    public void insert(Image image){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        String sql="insert into java_image values(null,?,?,?,?,?,?)";
        try {
            connection=DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,image.getImageName());
            preparedStatement.setInt(2,image.getSize());
            preparedStatement.setString(3,image.getUploadTime());
            preparedStatement.setString(4,image.getContentType());
            preparedStatement.setString(5,image.getPath());
            preparedStatement.setString(6,image.getMd5());
            int re=preparedStatement.executeUpdate();
            if (re!=1){
                throw new JavaImageServerException("图片插入数据库失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JavaImageServerException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,null);
        }

    }

    public Image selectOne(int imageId){
        Connection connection=null;
        String sql="select * from java_image where imageId = ? ";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection=DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,imageId);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Image image=new Image();
                image.setImageId(resultSet.getInt("imageId"));
                image.setImageName(resultSet.getString("imageName"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("uploadTime"));
                image.setContentType(resultSet.getString("contentType"));
                image.setPath(resultSet.getString("path"));
                image.setMd5(resultSet.getString("md5"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return null;
    }
    public List<Image> selectAll(){
        Connection connection=null;
        String sql="select * from java_image";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        List<Image> list=new ArrayList<>();
        try {
            connection=DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Image image=new Image();
                image.setImageId(resultSet.getInt("imageId"));
                image.setImageName(resultSet.getString("imageName"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("uploadTime"));
                image.setContentType(resultSet.getString("contentType"));
                image.setPath(resultSet.getString("path"));
                image.setMd5(resultSet.getString("md5"));
                list.add(image);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return null;
    }

    public void delete(int imageId){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        String sql="delete from java_image where imageId= ? ";
        try {
            connection=DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,imageId);
            int re=preparedStatement.executeUpdate();
            if (re!=1){
                throw new JavaImageServerException("图片从数据库删除失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JavaImageServerException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,null);
        }

    }
    public Image selectMd5(String md5){
        Connection connection=null;
        String sql="select * from java_image where md5 = ? ";
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        try {
            connection=DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,md5);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                Image image=new Image();
                image.setImageId(resultSet.getInt("imageId"));
                image.setImageName(resultSet.getString("imageName"));
                image.setSize(resultSet.getInt("size"));
                image.setUploadTime(resultSet.getString("uploadTime"));
                image.setContentType(resultSet.getString("contentType"));
                image.setPath(resultSet.getString("path"));
                image.setMd5(resultSet.getString("md5"));
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection,preparedStatement,resultSet);
        }
        return null;
    }

    public static void main(String[] args) {
        ImageDao imageDao=new ImageDao();
        Image image1=imageDao.selectOne(41);
        System.out.println(image1.getImageName());
        Image image=imageDao.selectMd5("3fc782252079f0bb3343329c24aa7582");
        System.out.println(image.getImageName());

    }
}
