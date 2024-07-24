package com.gzhuoj.problem.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

@WebServlet("/upload")
@MultipartConfig //如果是文件上传，必须要设置该注解！
public class UploadServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("文件上传...");
        //设置请求的编码格式
        req.setCharacterEncoding("UTF-8");
        //获取普通表单项（获取参数）
        String uname = req.getParameter("uname"); //表单中表单元素的name属性值
        System.out.println("uname: " + uname);

        //获取Part对象 （Servlet 将 mutipart/form-data 的 POST 请求封装成 Part对象）
        Part part = req.getPart("file");
        //通过Part对象得到上传的文件名
        String fileName = part.getSubmittedFileName();
        System.out.println("上传文件名：" + fileName);
        //得到文件存放的路径
        String filePath = req.getServletContext().getRealPath("/");
        System.out.println("文件存放路径：" + filePath);
        //上传文件到指定目录
        part.write(filePath + "/" + fileName);
    }
}
