package com.qst.action.team;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
import com.qst.WebUtil;
import com.qst.service.ITeamService;
import com.qst.service.ServiceFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 王世广
 * @usage 接收id参数，查询出要修改的班级,forward到edit.jsp显示
 */
@WebServlet("/team/edit.action")
public class EditServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 获取要查看的班级的id
    Integer id = RequestUtil.getInt(req, "id");
    try {
      req.setAttribute("team", classTeamService.findById(id));
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
    }
    WebUtil.forward(req, resp, "/team/edit.jsp");
  }
}
