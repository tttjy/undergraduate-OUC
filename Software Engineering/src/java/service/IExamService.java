package com.qst.service;

import com.qst.entity.Exam;
import com.qst.entity.Schedule;
import com.qst.entity.TestPersonnel;
import java.sql.SQLException;
import java.util.List;

public interface IExamService {
  /**
   *
   * @param stu
   *            学生
   * @return 查询本学生所有的考试安排，（已考和未考的）
   */
  List<Schedule> findScheduleByTestPersonnel(TestPersonnel personnel);
  Exam begin(TestPersonnel personnel, int scheduleId);
  void end(Exam exam);
  List<Exam> examResult(int id) throws SQLException;
  /*


  */
}
