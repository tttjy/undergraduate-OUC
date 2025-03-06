package com.qst.action.TestPersonnel;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import com.qst.service.ITestPersonnelService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/testPersonnel/update.action")
public class UpdateServlet extends BaseServlet {
  private final ITestPersonnelService testPersonnelService =
      ServiceFactory.getService(ITestPersonnelService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    User user = new User();
    user.setId(RequestUtil.getInt(req, "id"));
    user.setName(RequestUtil.getString(req, "name"));
    user.setStatus(Integer.parseInt(RequestUtil.getString(req, "status")));
    TestPersonnel testPersonnel = new TestPersonnel();
    testPersonnel.setId(user.getId());
    testPersonnel.setUser(user);
    Date date = Date.valueOf(RequestUtil.getString(req, "birthDate"));
    testPersonnel.setBirthDate(date);
    testPersonnel.setPhone(RequestUtil.getString(req, "phone"));
    testPersonnel.setGender(RequestUtil.getString(req, "gender"));
    try {
      testPersonnelService.updateTestPersonnel(testPersonnel);
      WebUtil.redirect(req, resp, "/testPersonnel/view.action?id=" + user.getId());
    } catch (ExamException ex) {
      ex.printStackTrace();
      addError(req, ex.getMessage());
      req.setAttribute("testPersonnel", testPersonnel);
      WebUtil.forward(req, resp, "/testPersonnel/edit.jsp");
    }
  }
}
