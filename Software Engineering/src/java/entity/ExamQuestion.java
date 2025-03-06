package com.qst.entity;

public class ExamQuestion { // 学生答题信息记录
  private Integer examId;
  private Integer testPersonnelId;
  private Integer questionId;
  private int[] answer; // 学生答题结果，不是试题的真正答案
  private boolean right; // 学生答题是否正确 ，对应列 result
  private int score; // 本题分数
  private Question question;

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public Integer getExamId() {
    return examId;
  }

  public void setExamId(Integer examId) {
    this.examId = examId;
  }

  public Integer getTestPersonnelId() {
    return testPersonnelId;
  }

  public void setTestPersonnelId(Integer testPersonnelId) {
    this.testPersonnelId = testPersonnelId;
  }

  public Integer getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Integer questionId) {
    this.questionId = questionId;
  }

  public int[] getAnswer() {
    return answer;
  }

  public void setAnswer(int[] answer) {
    this.answer = answer;
  }

  public boolean isRight() {
    return right;
  }

  public void setRight(boolean right) {
    this.right = right;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
