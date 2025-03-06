package com.qst.action.user;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.User;
import com.qst.service.IUserAdminService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user/save.action")
public class SaveServlet extends BaseServlet {
  private final IUserAdminService userAdminService =
      ServiceFactory.getService(IUserAdminService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User user = new User();
    req.getParameter("");
    user.setId(RequestUtil.getInt(req, "id"));
    user.setLogin(RequestUtil.getString(req, "login"));
    user.setPasswd(RequestUtil.getString(req, "passwd"));
    user.setName(RequestUtil.getString(req, "name"));
    user.setType(RequestUtil.getInt(req, "type"));
    user.setStatus(RequestUtil.getInt(req, "status"));
    try {
      userAdminService.saveUser(user);
      addMessage(req, "用户信息已保存");
      // req.setAttribute("_message_key", "用户信息已保存");
      WebUtil.redirect(req, resp, "/user/view.action?id=" + user.getId());
    } catch (ExamException ex) {
      req.setAttribute("user", user);
      addError(req, ex.getMessage());
      // req.setAttribute("ERROR_KEY", ex.getMessage());
      WebUtil.forward(req, resp, "/user/create.jsp");
    }
  }
}