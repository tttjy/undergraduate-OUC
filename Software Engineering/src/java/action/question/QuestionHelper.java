package com.qst.action.question;

import com.qst.RequestUtil;
import com.qst.entity.Choice;
import com.qst.entity.Question;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

// 帮助Servlet层获取前端传来的数据
public class QuestionHelper {
  public static Question createQuestion(HttpServletRequest req) {
    Question q = new Question();
    q.setId(RequestUtil.getInt(req, "id"));
    q.setTitle(RequestUtil.getString(req, "title"));
    q.setDifficulty(RequestUtil.getInt(req, "difficulty"));
    q.setAssessmentId(RequestUtil.getInt(req, "assessmentId"));
    q.setType(RequestUtil.getInt(req, "type"));
    q.setStatus(2);
    q.setHint(RequestUtil.getString(req, "hint"));
    return q;
  }

  public static List<Choice> createChoice(HttpServletRequest req) {
    List<Choice> choiceList = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      Choice choice = new Choice();
      choice.setId(RequestUtil.getInt(req, "id[" + i + "]"));
      choice.setTitle(RequestUtil.getString(req, "title[" + i + "]"));
      choice.setChecked(RequestUtil.getInt(req, "checked[" + i + "]") == 1);
      choiceList.add(choice);
    }
    return choiceList;
  }
}
