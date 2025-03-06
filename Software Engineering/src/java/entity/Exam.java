package com.qst.entity;

import java.sql.Timestamp;
import java.util.List;

public class Exam extends BaseEntity { // 新建试题

  private Integer testPersonnelId;
  private Integer scheduleId;
  private Timestamp beginTime;
  private Timestamp endTime;
  private Schedule schedule;
  private List<ExamQuestion> examQuestions; // 考试试题，包括试题，学生答案，本题分数等
  private String result;
  private int questionIndex;
  public ExamQuestion getQuestion() {
    if (questionIndex >= examQuestions.size()) {
      questionIndex = 0;
    } else if (questionIndex < 0) {
      questionIndex = examQuestions.size() - 1;
    }
    return examQuestions.get(questionIndex);
  }

  /**
   *
   * @return 已考时间（分钟）
   */
  public int getPassedTime() {
    return (int) (System.currentTimeMillis() - beginTime.getTime()) / (1000 * 60);
  }

  public int getQuestionIndex() {
    return questionIndex;
  }

  public void setQuestionIndex(int questionIndex) {
    this.questionIndex = questionIndex;
  }

  public Integer getTestPersonnelId() {
    return testPersonnelId;
  }

  public List<ExamQuestion> getExamQuestions() {
    return examQuestions;
  }

  public void setExamQuestions(List<ExamQuestion> examQuestions) {
    this.examQuestions = examQuestions;
  }

  public void setTestPersonnelId(Integer testPersonnelId) {
    this.testPersonnelId = testPersonnelId;
  }

  public Integer getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
  }

  public Timestamp getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(Timestamp beginTime) {
    this.beginTime = beginTime;
  }

  public Timestamp getEndTime() {
    return endTime;
  }

  public void setEndTime(Timestamp endTime) {
    this.endTime = endTime;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  @Override
  public String toString() {
    return "Exam [testPersonnelId=" + testPersonnelId + ", scheduleId=" + scheduleId
        + ", beginTime=" + beginTime + ", endTime=" + endTime + ", schedule=" + schedule
        + ", examQuestions=" + examQuestions + ", result=" + result
        + ", questionIndex=" + questionIndex + "]";
  }
}
