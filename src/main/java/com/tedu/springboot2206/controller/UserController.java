package com.tedu.springboot2206.controller;

import com.tedu.springboot2206.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;

@Controller
public class UserController {
    //表示保存所有用户信息的目录users
    private static File userDir;

    static{
        userDir= new File("./users");
        if(!userDir.exists()){
            userDir.mkdirs();
        }

    }

    @RequestMapping("/regUser")
    public void reg(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理注册！！！！！");

        //1.获取注册页面上的表单信息
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String nickname=request.getParameter("nickname");
        String ageStr=request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);


        /*
        String username=request.getParameter("username");
        获取浏览器船体过来的参数username对应的值
        返回的字符串可能存在2中特殊情况：
        1返回空字符串，用户没输入
        2返回null，没有传递参数过来

         */
        if(username==null||username.isEmpty()||password==null||password.isEmpty()||
                nickname==null||nickname.isEmpty()||ageStr==null||ageStr.isEmpty()
        ||!ageStr.matches("[0-9]+")){

            try {
                response.sendRedirect("/reg_info_error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        int age=Integer.parseInt(ageStr);//将年龄转换为int值
        //2.将该注册用户信息以user对象形式表示并序列化到文件中保存
        User user=new User(username,password,nickname,age);

        //把信息写到文件里
        //new File("fancq.obj");
        //file的重载构造器，目录，文件名
        File file=new File(userDir,username+".obj");

        /**判断蝇用户是否存在*/
        if(file.exists()){
            try {
                response.sendRedirect("/have_user.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;


        }


        try (FileOutputStream fos= new FileOutputStream(file);
             ObjectOutputStream oos= new ObjectOutputStream(fos);
        ){
            oos.writeObject(user);
            } catch (IOException e) {
            e.printStackTrace();
        }

        //3给用户回一个页面
        try {
            response.sendRedirect("/reg_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
