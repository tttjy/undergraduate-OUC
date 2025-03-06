package com.qst.action.user;

import com.qst.BaseServlet;
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

@WebServlet("/user/view.action")
public class ViewServlet extends BaseServlet {
  private final IUserAdminService userAdminService = ServiceFactory.getService(IUserAdminService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");
    User user = userAdminService.findUserById(id);
    req.setAttribute("user", user);
    WebUtil.forward(req, resp, "/user/view.jsp");
  }
}
