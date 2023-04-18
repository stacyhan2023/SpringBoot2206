package com.tedu.springboot2206.controller;

import com.tedu.springboot2206.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/deleteUser")
    public void delete(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理删除用户！！！！！");
        String username=request.getParameter("username");
        System.out.println("要删除的用户是："+username);

    }

    @RequestMapping("/userList")
    public void userList(HttpServletRequest request,HttpServletResponse response){

        System.out.println("开始处理动态页面！！！！！");
        /*
        1读取users目录下的所有obj文件并反序列化，然后将得到的所有user对象存入一个list集合
        2生成html页面并将所有用户信息体现在其中将其发送给浏览器
        * */
        List<User> userList=new ArrayList<>();

        //1读取users目录下的所有obj文件
        File[] subs=userDir.listFiles(f->f.getName().endsWith("obj"));

        //1.2将每个文件反序列化得到user对象


        for(File file:subs){//新循环可以遍历循环和数组
//            for(int i=0;i<subs.size();i++)
//            File file=List.get(i);

//            for(int i=0;i<subs.length();i++)
//            File file=subs[i];
            try (FileInputStream fis=new FileInputStream(file);
                 ObjectInputStream ois=new ObjectInputStream(fis);
            ){
                User user= (User)ois.readObject();
                userList.add(user);
                } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println(userList);

        //2生成html页面并将所有用户信息体现在其中将其发送给浏览器
        try {
            response.setContentType("text/html;charset=utf-8");

            PrintWriter pw=response.getWriter();
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println(" <title>我的首页</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println(" <center>");
            pw.println("<h1>用户列表</h1>");
            pw.println("<table border=\"3\">");

            pw.println("<tr>");
            pw.println(" <td>用户名</td>");
            pw.println(" <td>密码</td>");
            pw.println(" <td>昵称</td>");
            pw.println(" <td>年龄</td>");
            pw.println(" <td>操作</td>");
            pw.println("</tr>");
            /*
            * <form action=,method=>
            <input type name=>
            * </form>
            * 用超链接*/


        for(User user:userList){
            pw.println("<tr>");
            pw.println(" <td>"+user.getUsername()+"</td>");
            pw.println(" <td>"+user.getPassword()+"</td>");
            pw.println(" <td>"+user.getNickname()+"</td>");
            pw.println(" <td>"+user.getAge()+"</td>");
            pw.println(" <td><a href='/deleteUser?username="+user.getUsername()+"'>删除</a></td>");
            pw.println("</tr>");
        }

            pw.println("<table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @RequestMapping("/loginUser")
    public void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理登陆！！！！！");
        //1
        String username=request.getParameter("username");
        String password=request.getParameter("password");

        if(username==null||username.isEmpty()||password==null||password.isEmpty()){
            try {
                response.sendRedirect("/login_info_error.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //2处理登陆,根据登陆用户的用户名去users目录下寻找该用户信息
        File file= new File(userDir,username+".obj");
        if(file.exists()){//文件存在则说明用户存在（用户名输入正确）
            //反序列化文件中该用户曾经的用户信息
            try (FileInputStream fis= new FileInputStream(file);
                 ObjectInputStream ois=new ObjectInputStream(fis);
                 ){
                User user=(User)ois.readObject();
                //比较密码是否一致
                if(user.getPassword().equals(password)){
                    //登陆成功
                    response.sendRedirect("/login_success.html");
                    return;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
            //登陆失败
        try {
            response.sendRedirect("/login_fail.html");
        } catch (IOException e) {
            e.printStackTrace();
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
