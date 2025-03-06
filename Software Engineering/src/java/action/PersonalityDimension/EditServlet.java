package com.qst.action.PersonalityDimension;

import com.qst.BaseServlet;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.entity.PersonalityDimension;
import com.qst.service.IDimensionService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/dimension/edit.action")
public class EditServlet extends BaseServlet {
  private final IDimensionService dimensionService =
      ServiceFactory.getService(IDimensionService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 通过问卷维度ID查找问卷维度信息，并在dimension文件夹下的edit.jsp页面显示
    PersonalityDimension kp = dimensionService.findDimensionById(RequestUtil.getInt(req, "id"));
    req.setAttribute("dimension", kp);
    WebUtil.forward(req, resp, "/dimension/edit.jsp");
  }
}
