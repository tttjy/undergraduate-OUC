package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.action.question.QuestionQueryParam;
import com.qst.dao.AssessmentTypeDAO;
import com.qst.dao.DAOFactory;
import com.qst.dao.ExamDAO;
import com.qst.dao.ExamQuestionDAO;
/*import com.qst.dao.ExamQuestionDAO;*/
import com.qst.dao.ScheduleDAO;
import com.qst.entity.Exam;
import com.qst.entity.ExamQuestion;
import com.qst.entity.Question;
import com.qst.entity.Schedule;
import com.qst.entity.TestPersonnel;
import com.qst.service.IExamService;
import com.qst.service.IQuestionService;
import com.qst.service.ServiceFactory;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamServiceImpl
    implements IExamService { // 通过本类中的findQuestion方法为本次测试随机添加考题，最终将本测试返回给Servlet控制层。
  private final ExamDAO examDAO = DAOFactory.getDAO(ExamDAO.class);
  private final ScheduleDAO scheduleDAO = DAOFactory.getDAO(ScheduleDAO.class);
  private final AssessmentTypeDAO aTypeDAO = DAOFactory.getDAO(AssessmentTypeDAO.class);
  private final ExamQuestionDAO examQuestionDAO = DAOFactory.getDAO(ExamQuestionDAO.class);
  private final IQuestionService questionService =
      ServiceFactory.getService(IQuestionService.class);

  @Override
  public List<Schedule> findScheduleByTestPersonnel(TestPersonnel personnel) {
    try {
      /**
       * 查询本学生已经参加的所有考试
       */
      List<Exam> exams = examDAO.findByTestPersonnel(personnel.getId());

      /**
       * 本学生所在班级所有的考试安排
       */
      List<Schedule> schedules = scheduleDAO.findByTeamId(personnel.getTeamId());
      Map<Integer, Schedule> map = new HashMap<>();
      for (Schedule h : schedules) {
        h.setAssessmentType(aTypeDAO.findById(h.getAssessmentId()));
        map.put(h.getId(), h);
      }
      // 标记已经考过的考试
      for (Exam m : exams) {
        try {
          map.get(m.getScheduleId()).getExtras().put("exam", m);
        } catch (Exception e) {
          // TODO: handle exception
          e.getMessage();
        }
      }
      return schedules;
    } catch (SQLException ex) {
      throw new ExamException("查询考试出错", ex);
    }
  }
  @Override
  public Exam begin(TestPersonnel personnel, int scheduleId) {
    try {
      Schedule schedule = scheduleDAO.findById(scheduleId);
      Exam exam = examDAO.findByTestPersonnelAndSchedule(personnel.getId(), schedule.getId());
      // 判断该考生能否参加本次考试
      isCanExam(personnel, exam, schedule);
      if (exam == null) {
        exam = new Exam();
        exam.setTestPersonnelId(personnel.getId());
        exam.setScheduleId(scheduleId);
        exam.setBeginTime(new Timestamp(System.currentTimeMillis()));
      }
      List<ExamQuestion> examQustions = new ArrayList<>();
      QuestionQueryParam param = new QuestionQueryParam();
      param.setAssessmentId(schedule.getAssessmentId());
      param.setStatus(2); // 有效

      param.setType(1); // 单选题
      examQustions.addAll(
          findQuestion(personnel.getId(), param, (schedule.getQuestionNumber() / 4), 2));
      // 抽取多选题
      param.setType(2); // 多选题
      examQustions.addAll(
          findQuestion(personnel.getId(), param, (schedule.getQuestionNumber() / 4), 2));
      param.setType(3); // 多选题
      examQustions.addAll(
          findQuestion(personnel.getId(), param, (schedule.getQuestionNumber() / 4), 2));
      param.setType(4); // 多选题
      examQustions.addAll(
          findQuestion(personnel.getId(), param, (schedule.getQuestionNumber() / 4), 2));
      exam.setExamQuestions(examQustions);

      return exam;
    } catch (SQLException e) {
      throw new ExamException("数据库访问出错，不能开始考试", e);
    }
  }
  private List<ExamQuestion> findQuestion(
      int stuId, QuestionQueryParam param, int count, int score) {
    List<Question> questions = questionService.find(param);
    // 打乱list的顺序和洗牌一样
    Collections.shuffle(questions);
    // count 为考试安排中指定的试题数量，questions.size()为按要求查找出来的试题数量
    count = questions.size() > count ? count : questions.size();
    // 如果按要求查找出来的试题数量为50，考试只要求20，所以截取20个题
    List<Question> tempList = questions.subList(0, count);

    List<ExamQuestion> examQustions = new ArrayList<>();
    for (Question q : tempList) {
      ExamQuestion eq = new ExamQuestion();
      eq.setQuestionId(q.getId());
      eq.setQuestion(q);
      eq.setTestPersonnelId(stuId);
      // 考试安排时，会指定每个单选或者多选的分数
      eq.setScore(score);
      examQustions.add(eq);
    }
    return examQustions;
  }
  private boolean isCanExam(TestPersonnel personnel, Exam exam, Schedule schedule) {
    if (exam != null) {
      throw new ExamException("已经考过了，不能再考");
    }
    if (schedule.getTeamId() != personnel.getTeamId()) {
      throw new ExamException("不是指定批次的参测人员，不能参加考试");
    }
    if (schedule.getStatus() == 0) {
      throw new ExamException("本次考试已取消");
    }
    if (schedule.getStatus() == 1) {
      throw new ExamException("本次考试还没有开始");
    }
    if (schedule.getStatus() == 3) {
      throw new ExamException("本次考试已结束");
    }
    return true;
  }
  @Override
  public void end(Exam exam) {
    try {
      // 设定交卷时间
      exam.setEndTime(new Timestamp(System.currentTimeMillis()));
      Map<Integer, Integer> map = new HashMap<>();
      map.put(1, 0);
      map.put(2, 0);
      map.put(3, 0);
      map.put(4, 0);
      // 判卷
      for (ExamQuestion eq : exam.getExamQuestions()) {
        // 判断该题学生答题是否正确
        eq.setRight(eq.getQuestion().isAnswerRight(eq.getAnswer()));
        if (eq.isRight()) {
          int a = map.get(eq.getQuestion().getType()) + 1;
          map.put(eq.getQuestion().getType(), a);
        } else {
          int a = map.get(eq.getQuestion().getType()) - 1;
          map.put(eq.getQuestion().getType(), a);
        }
      }
      StringBuffer stf = new StringBuffer();
      for (int i : map.keySet()) {
        if (map.get(i) > 0) {
          if (i == 1) {
            stf.append("J");
          } else if (i == 2) {
            stf.append("T");
          } else if (i == 3) {
            stf.append("S");
          } else {
            stf.append("E");
          }
        } else {
          if (i == 1) {
            stf.append("P");
          } else if (i == 2) {
            stf.append("F");
          } else if (i == 3) {
            stf.append("N");
          } else {
            stf.append("I");
          }
        }
      }
      stf.reverse();
      exam.setResult(stf.toString());
      // 保存考试
      if (exam.getId() != null) {
        examDAO.updateExam(exam);
      } else {
        examDAO.saveExam(exam);
      }
      // 保存学生的答题，备查
      examQuestionDAO.deleteByExam(exam.getId());
      for (ExamQuestion eq : exam.getExamQuestions()) {
        eq.setExamId(exam.getId());
        examQuestionDAO.save(eq);
      }
    } catch (SQLException e) {
      throw new ExamException("数据库访问出错，不能开始考试", e);
    }
  }

  public void saveExam(Exam exam) throws SQLException {
    examDAO.saveExam(exam);
    for (ExamQuestion q : exam.getExamQuestions()) {
      q.setExamId(exam.getId());
      examQuestionDAO.save(q);
    }
  }
  public List<Exam> examResult(int id) throws SQLException {
    return examDAO.findById(id);
  }
}
