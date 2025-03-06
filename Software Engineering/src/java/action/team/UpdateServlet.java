package com.qst.action.team;

import com.qst.BaseServlet;
import com.qst.ExamException;
import com.qst.RequestUtil;
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
 * @usage 将表单数据更新到数据库,重定向到view.action显示
 */
@WebServlet("/team/update.action")
public class UpdateServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 将表单数据组装为ClassTeam对象
    Team t = new Team();
    t.setId(RequestUtil.getInt(req, "id"));
    t.setName(RequestUtil.getString(req, "name"));
    t.setBeginYear(RequestUtil.getDate(req, "beginYear"));
    t.setStatus(RequestUtil.getInt(req, "status"));
    try {
      classTeamService.updateTeam(t);
      WebUtil.redirect(req, resp, "/team/view.action?id=" + t.getId());
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
      req.setAttribute("team", t);
      WebUtil.forward(req, resp, "/team/edit.jsp");
    }
  }
}
