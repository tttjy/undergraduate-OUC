package com.qst.action.user;

import com.qst.BaseServlet;
import com.qst.WebUtil;
import com.qst.entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user/create.action")
public class CreateServlet extends BaseServlet {
  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User user = new User();
    user.setStatus(1);
    user.setType(1);
    req.setAttribute("user", user);
    WebUtil.forward(req, resp, "/user/create.jsp");
  }
}
