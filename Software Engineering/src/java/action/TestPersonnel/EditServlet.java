package com.qst.action.TestPersonnel;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.TestPersonnel;
import com.qst.service.ITestPersonnelService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/testPersonnel/edit.action")
public class EditServlet extends BaseServlet {
  private final ITestPersonnelService testPersonnelService =
      ServiceFactory.getService(ITestPersonnelService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 获取学生id
    int id = RequestUtil.getInt(req, "id");
    TestPersonnel testPersonnel = testPersonnelService.findById(id);
    req.setAttribute("testPersonnel", testPersonnel);
    WebUtil.forward(req, resp, "/testPersonnel/edit.jsp");
  }
}
