package com.qst.action;

import com.qst.BaseServlet;
import com.qst.WebUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logout.action")
public class LogoutServlet extends BaseServlet {
  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getSession().invalidate();
    WebUtil.redirect(req, resp, "/login.jsp");
  }
}
