package com.qst.action.team;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.WebUtil;
import com.qst.entity.Team;
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
 * @usage 为添加班级准备默认值,forward到create.jsp显示
 */
@WebServlet("/team/create.action")
public class CreateServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    Team team = new Team();
    team.setStatus(1);
    try {
      req.setAttribute("team", team);
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
    }
    WebUtil.forward(req, resp, "/team/create.jsp");
  }
}
