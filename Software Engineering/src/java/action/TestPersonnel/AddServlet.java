package com.qst.action.TestPersonnel;

import com.qst.BaseServlet;
import com.qst.Constant;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.dao.DAOFactory;
import com.qst.dao.UserDAO;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import com.qst.service.ITeamService;
import com.qst.service.ITestPersonnelService;
import com.qst.service.IUserAdminService;
import com.qst.service.ServiceFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * 这段代码处理了添加测试人员的HTTP请求，它从请求中提取信息，执行相应的操作，并根据结果返回成功或错误消息，最后重定向用户到适当的页面。
 * 当参测人员信息输入完成后，单击保存时，前端会发送请求到后端的AddServlet类进行处理，在com.qst.action.testPersonnel包下创建AddServlet类，从请求中获取user对象数据并生成user对象，先判断是否有相同手机号的用户，如果有则在请求中添加提示并返回，否则调用service层方法添加参测人员，最后返回create.jsp页面。
 */
// 使用@WebServlet注解将该类标记为Servlet，并指定URL映射路径为"/testPersonnel/add.action"
// 这三个参数一起协作，通过请求转发将控制从当前Servlet（或Java类）传递给指定的JSP页面，以便生成响应或显示特定的内容。req
// 包含了请求信息，resp 用于构建响应，而 "/testPersonnel/create.jsp"
// 定义了请求转发的目标。这是一种常见的在Web应用中控制流的方式，通常用于将控制从Servlet传递到JSP页面以生成动态内容。
@WebServlet("/testPersonnel/add.action")
// 使用@MultipartConfig注解来支持文件上传
@MultipartConfig
public class AddServlet extends BaseServlet {
  // 获取ITestPersonnelService的实例，用于处理测试人员信息的服务
  private final ITestPersonnelService testPersonnelService =
      ServiceFactory.getService(ITestPersonnelService.class);
  // 获取UserDAO的实例，用于处理用户信息的数据访问
  private final UserDAO dao = DAOFactory.getDAO(UserDAO.class);

  // 覆盖BaseServlet中的doAction方法，处理HTTP请求
  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User user = new User();
    req.getParameter(""); // 该行似乎没有具体的操作，需要检查是否多余
    user.setId(RequestUtil.getInt(req, "id")); // 从请求中获取用户ID
    user.setLogin(RequestUtil.getString(req, "login")); // 从请求中获取用户登录名
    user.setPasswd(User.PASSWORD); // 设置用户密码为默认值
    user.setName(RequestUtil.getString(req, "name")); // 从请求中获取用户姓名
    user.setType(RequestUtil.getInt(req, "type")); // 从请求中获取用户类型
    user.setStatus(RequestUtil.getInt(req, "status")); // 从请求中获取用户状态

    TestPersonnel s = new TestPersonnel();
    s.setUser(user);
    s.setGender(RequestUtil.getString(req, "gender")); // 从请求中获取性别
    s.setPhone(RequestUtil.getString(req, "login")); // 从请求中获取手机号
    s.setTeamId(RequestUtil.getInt(req, "teamId")); // 从请求中获取班级ID
    s.setBirthDate(RequestUtil.getDate(req, "birthDate")); // 从请求中获取出生日期

    try {
      // 检查手机号是否已存在
      if (dao.findByLogin(s.getPhone()) != null) {
        addMessage(req, "该手机号已经存在！");
        int teamId = RequestUtil.getInt(req, "teamId"); // 获取班级ID
        req.setAttribute("teamId", teamId); // 设置班级ID为请求属性
        WebUtil.forward(req, resp, "/testPersonnel/create.jsp"); // 重定向到创建页面
      } else {
        // 添加测试人员
        testPersonnelService.addTestPersonnel(s);
        addMessage(req, "参测人员添加成功"); // 添加成功消息
        WebUtil.redirect(req, resp,
            "/testPersonnel/select.action?teamId=" + s.getTeamId()); // 重定向到测试人员列表页面
      }
    } catch (ExamException ex) {
      req.setAttribute("user", user); // 将用户对象设置为请求属性
      addError(req, ex.getMessage()); // 添加错误消息
      WebUtil.forward(req, resp, "/testPersonnel/create.jsp"); // 重定向到创建页面
    }
  }
}
