package com.qst.service;

import com.qst.entity.Team;
import java.util.List;

public interface ITeamService {
  List<Team> findAll();

  /**
   * @param creator_id : 班级创建者的id
   * @return 指定教师创建的所有班级
   */
  List<Team> findByCreator(Integer creator_id);

  Team findById(Integer id);

  void saveTeam(Team t);

  void updateTeam(Team t);

  void deleteTeam(int id);
  /**
   *
   * @param id
   *            :要查找的班级id
   * @return：指定id的班级
   */
  /*

   */
  /*
 @usage 更新班级
* @param t
*            : 要更新的班级
*/ /*

 */
  /*
 @usage 添加新班级
* @param t
*            :要保存的班级
*/ /*


 */
  /*
 @usage 删除班级
* @param id
*            ：要删除的班级ｉｄ
*/ /*
    */
}
