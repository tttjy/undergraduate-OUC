package com.qst.action.user;

import com.qst.BaseServlet;
import com.qst.WebUtil;
import com.qst.entity.User;
import com.qst.service.IUserAdminService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user/list.action")
public class ListServlet extends BaseServlet {
  private final IUserAdminService userAdminService =
      ServiceFactory.getService(IUserAdminService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    List<User> userList = userAdminService.findUsers();
    req.setAttribute("userList", userList);
    WebUtil.forward(req, resp, "/user/list.jsp");
  }
}
