package com.qst.action.PersonalityDimension;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.service.IDimensionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dimension/delete.action")
public class DeleteServlet extends BaseServlet {
  private final IDimensionService dimensionService =
      ServiceFactory.getService(IDimensionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");
    try {
      // 通过问卷维度ID删除问卷维度信息
      dimensionService.deleteDimension(id);
      addMessage(req, "问卷维度已删除");
    } catch (ExamException ex) {
      ex.printStackTrace();
      addError(req, ex.getMessage());
    }
    WebUtil.redirect(
        req, resp, "/dimension/list.action?id=" + RequestUtil.getInt(req, "assessmentId"));
  }
}
