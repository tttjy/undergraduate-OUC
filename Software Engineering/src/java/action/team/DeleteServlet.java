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
 * @usage 接收id，根据id从数据库删除班级
 */
@WebServlet("/team/delete.action")
public class DeleteServlet extends BaseServlet {
  private final ITeamService classTeamService = ServiceFactory.getService(ITeamService.class);

  @Override
  protected void doAction(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int id = RequestUtil.getInt(req, "id");
    try {
      classTeamService.deleteTeam(id);
      addMessage(req, "批次已删除");
    } catch (ExamException ex) {
      addError(req, ex.getMessage());
    }
    WebUtil.redirect(req, resp, "/team/list.action");
  }
}
