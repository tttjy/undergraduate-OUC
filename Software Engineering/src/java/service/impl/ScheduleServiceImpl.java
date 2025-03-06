package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.dao.AssessmentTypeDAO;
import com.qst.dao.DAOFactory;
/*import com.qst.dao.ExamDAO;*/
import com.qst.dao.ScheduleDAO;
import com.qst.dao.TeamDAO;
import com.qst.dao.TestPersonnelDAO;
import com.qst.dao.UserDAO;
/*import com.qst.entity.Exam;*/
import com.qst.entity.Schedule;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import com.qst.service.IScheduleService;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleServiceImpl
    implements IScheduleService { // 实现ScheduleService接口并实现findByCreater方法
  private final ScheduleDAO scheduleDAO = DAOFactory.getDAO(ScheduleDAO.class);
  private final UserDAO userDAO = DAOFactory.getDAO(UserDAO.class);
  private final TestPersonnelDAO personDAO = DAOFactory.getDAO(TestPersonnelDAO.class);
  private final AssessmentTypeDAO assessmentDAO = DAOFactory.getDAO(AssessmentTypeDAO.class);
  private final TeamDAO teamDAO = DAOFactory.getDAO(TeamDAO.class);
  /*private ExamDAO examDAO = DAOFactory.getDAO(ExamDAO.class);*/

  @Override
  public List<Schedule> findByCreator(User user) {
    try {
      List<Schedule> scheduleList = scheduleDAO.findByCreatorId(user.getId());
      for (Schedule h : scheduleList) {
        h.setCreator(user);
        h.setAssessmentType(assessmentDAO.findById(h.getAssessmentId()));
        h.setTeam(teamDAO.findById(h.getTeamId()));
      }
      return scheduleList;
    } catch (SQLException ex) {
      throw new ExamException("查询考试安排出错", ex);
    }
  }
  @Override
  public Schedule findById(Integer id) {
    try {
      Schedule h = scheduleDAO.findById(id);
      h.setCreator(UserDAO.findById(h.getCreatorId()));
      h.setAssessmentType(assessmentDAO.findById(h.getAssessmentId()));
      h.setTeam(teamDAO.findById(h.getTeamId()));
      return h;
    } catch (SQLException e) {
      throw new ExamException("查询考试安排出错", e);
    }
  }
  @Override
  public void saveSchedule(Schedule h) {
    try {
      scheduleDAO.save(h);
    } catch (SQLException e) {
      throw new ExamException("添加考试安排出错", e);
    }
  }
  @Override
  public void updateSchedule(Schedule h) {
    try {
      scheduleDAO.update(h);
    } catch (SQLException e) {
      throw new ExamException("更新考试安排出错", e);
    }
  }
  @Override
  public Schedule deleteSchedule(Integer id) {
    try {
      Schedule sche = scheduleDAO.findById(id);
      if (sche.getStatus() > 1) {
        throw new ExamException("只有未开始或作废的安排才能删除");
      }
      scheduleDAO.delete(sche.getId());
      return sche;
    } catch (SQLException e) {
      throw new ExamException("删除考试安排出错", e);
    }
  }
}
