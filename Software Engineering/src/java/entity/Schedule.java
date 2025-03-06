package com.qst.entity;

import java.util.Date;

public class Schedule extends BaseEntity { // 创建测试安排实体类以及批次管理实体类
  private String beginDate;
  private String endDate;
  private int duration;
  private int status;
  private int assessmentId;
  private int teamId;
  private int creatorId;
  private Date createDate;
  private Team team;
  private User creator;
  private AssessmentType assessmentType;
  private int questionNumber;
  public int getQuestionNumber() {
    return questionNumber;
  }

  public void setQuestionNumber(int questionNumber) {
    this.questionNumber = questionNumber;
  }

  public String getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getAssessmentId() {
    return assessmentId;
  }

  public void setAssessmentId(int assessmentId) {
    this.assessmentId = assessmentId;
  }

  public int getTeamId() {
    return teamId;
  }

  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }

  public int getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(int creatorId) {
    this.creatorId = creatorId;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  public AssessmentType getAssessmentType() {
    return assessmentType;
  }
  public void setAssessmentType(AssessmentType aType) {
    this.assessmentType = aType;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  static {
    addDesc(
        Schedule.class, "s0", "作废", "s1", "未考", "s2", "考试中", "s3", "已结束"); // 信息记录提示
  }

  public String getStatusDesc() {
    return getDesc(Schedule.class, "s", status);
  } // 省略get,set方法
}
