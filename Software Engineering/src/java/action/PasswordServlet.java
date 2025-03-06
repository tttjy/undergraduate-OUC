package com.qst.action;

import com.qst.BaseServlet;
import com.qst.Constant;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.User;
import com.qst.service.IUserService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/password.action")
public class PasswordServlet extends BaseServlet {
  private final IUserService userService = ServiceFactory.getService(IUserService.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    WebUtil.forward(req, resp, "/password.jsp");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User currentUser = (User) req.getSession().getAttribute(Constant.CURRENT_USER);
    String oldPassword = RequestUtil.getString(req, "oldPassword");
    String newPassword = RequestUtil.getString(req, "newPassword");
    String confirm = RequestUtil.getString(req, "comfirm");
    if (!confirm.equals(newPassword)) {
      addError(req, "两次密码输入不一致");
      WebUtil.forward(req, resp, "/password.jsp");
    } else {
      try {
        userService.changePassword(currentUser.getId(), oldPassword, newPassword);
        addMessage(req, "登录密码已更改，请退出重新登录");
        WebUtil.redirect(req, resp, "/passwordmsg.jsp");
      } catch (ExamException ex) {
        addError(req, ex.getMessage());
        WebUtil.forward(req, resp, "/password.jsp");
      }
    }
  }
}
