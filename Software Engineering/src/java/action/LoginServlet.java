package com.qst.action;

import com.qst.*;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import com.qst.service.ITestPersonnelService;
import com.qst.service.IUserService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login.action")
public class LoginServlet extends BaseServlet {
  private final IUserService userService = ServiceFactory.getService(IUserService.class);
  private final ITestPersonnelService testPersonnelService =
      ServiceFactory.getService(ITestPersonnelService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String login = RequestUtil.getString(req, "login");
    String password = RequestUtil.getString(req, "password");
    int menu = RequestUtil.getInt(req, "menu");
    try {
      User user = userService.login(login, password);
      req.getSession().setAttribute(Constant.CURRENT_USER, user);
      if (user.getType() == 4) {
        TestPersonnel stu = testPersonnelService.findById(user.getId());
        req.getSession().setAttribute(Constant.CURRENT_TESTPERSONNEL, stu);
      }

      // 菜单为数据库读取,受权限控制,作为高级任务实现
      WebUtil.redirect(req, resp, "/index.jsp");

    } catch (ExamException ex) {
      System.out.println(ex.getMessage());
      addError(req, ex.getMessage());

      req.setAttribute("login", login);
      WebUtil.forward(req, resp, "/login.jsp");
    }
  }
}
