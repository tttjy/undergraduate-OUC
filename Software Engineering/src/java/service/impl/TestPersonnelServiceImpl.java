package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.dao.DAOFactory;
import com.qst.dao.TestPersonnelDAO;
import com.qst.dao.UserDAO;
import com.qst.entity.TestPersonnel;
import com.qst.entity.User;
import com.qst.service.ITestPersonnelService;
import java.sql.SQLException;
import java.util.List;

public class TestPersonnelServiceImpl implements ITestPersonnelService {
  private final TestPersonnelDAO personDAO = DAOFactory.getDAO(TestPersonnelDAO.class);
  private final UserDAO userDAO = DAOFactory.getDAO(UserDAO.class);

  // 通过团队ID查找相关的测试人员
  @Override
  public List<TestPersonnel> findByTeamId(Integer id) {
    try {
      return personDAO.findByTeam(id);
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错");
    }
  }

  @Override
  public TestPersonnel findById(int id) {
    try {
      TestPersonnel stu = personDAO.findById(id);
      stu.setUser(UserDAO.findById(id));
      return stu;
    } catch (SQLException ex) {
      throw new ExamException("数据库查询出错", ex);
    }
  }
  // query用来查询固定的参测人员信息。
  @Override
  public List<TestPersonnel> query(int teamId, String name, String stdNo) {
    try {
      return personDAO.query(teamId, name, stdNo);
    } catch (SQLException ex) {
      throw new ExamException("不存在该参测人员");
    }
  }

  /**
   * 整个方法的目的是在给定的测试人员信息或用户信息发生变化时，同时更新数据库中的对应信息。
   * 如果更新过程中出现了任何错误或异常，它将抛出一个自定义的异常，并提供相应的错误消息。
   */
  @Override
  public void updateTestPersonnel(TestPersonnel s) {
    // 开始尝试更新测试人员或用户信息
    try {
      // 从传入的TestPersonnel对象s中获取User对象
      User user = s.getUser();
      // 根据TestPersonnel对象s的ID从数据库中查找对应的User对象
      User u = UserDAO.findById(s.getId());
      // 将找到的User对象的名称设置为从TestPersonnel对象s中获取的User对象的名称
      u.setName(user.getName());
      // 如果从TestPersonnel对象s中获取的User对象的status不等于0
      if (user.getStatus() != 0) {
        // 将找到的User对象的status设置为从TestPersonnel对象s中获取的User对象的status
        u.setStatus(user.getStatus());
      }
      // 更新数据库中查找到的User对象的信息
      userDAO.update(u);
      // 更新数据库中TestPersonnel对象s的信息
      personDAO.update(s);
      // 如果以上操作都成功完成，则退出try块，不会执行catch块中的代码
    } catch (SQLException ex) {
      // 如果在更新过程中出现任何SQL异常（例如：数据库连接失败，查询错误等）
      // 抛出一个新的ExamException异常，异常消息为"更新参测人员出错"，同时将原始的SQLException异常作为原因传递给新的ExamException异常
      throw new ExamException("更新参测人员出错", ex);
      // 如果出现异常，则会执行catch块中的代码，不会执行catch块之后的代码
    }
  }
  // 实现将参测人员信息插入到数据库中。

  /**
   * 首先，对于列表中的每个测试人员（TestPersonnel）对象，它首先通过电话号码尝试在数据库中查找该人员信息。如果找不到（即temp为null），那么就将这个新的测试人员信息添加到数据库中。
   *
   * 如果找到了相同电话号码的已存在测试人员（即temp不为null），那么它会检查这个已存在的测试人员的队伍ID（TeamId）和新测试人员的队伍ID是否相同。如果不同，那么就会抛出一个异常，因为这意味着这个电话号码已经在其他测试人员那里注册了。
   *
   * 如果队伍ID相同，那么它会将新测试人员的ID设置为已存在测试人员的ID，然后用这个新的ID更新数据库中的测试人员信息。
   *
   * 如果在整个过程中出现任何SQL异常，那么就会抛出一个新的异常，说明导入测试人员出错。
   *
   * 总的来说，这个方法的作用是在一个测试人员列表中查找并更新电话号码已存在的人员信息，或者添加电话号码未存在的人员信息。
   *
   */
  @Override
  public void importTestPersonnel(List<TestPersonnel> testPersonnelList) {
    try {
      for (int i = 0; i < testPersonnelList.size(); i++) {
        TestPersonnel s = testPersonnelList.get(i);
        TestPersonnel temp = personDAO.findByphone(s.getPhone());
        if (temp == null) // 添加学生
        {
          addTestPersonnel(s);
        } else // 更新已存在学生
        {
          if (temp.getTeamId() != s.getTeamId()) {
            throw new ExamException("导入第" + (i + 1) + "个参测人员出错,该手机号" + s.getPhone()
                + "已在其他参测人员注册");
          }
          s.setId(temp.getId());
          updateTestPersonnel(s);
        }
      }
    } catch (SQLException ex) {
      throw new ExamException("导入参测人员出错", ex);
    }
  }
  @Override
  public void addTestPersonnel(TestPersonnel s) {
    try {
      User u = s.getUser();
      if (u.getType() == 0) {
        u.setType(4);
      }

      u.setStatus(1);
      if (u.getPasswd() == null || u.getPasswd().equals("")) {
        u.setPasswd(User.PASSWORD);
      }

      u.setLogin(s.getPhone()); // 学号作为默认登录名
      userDAO.insert(u);
      s.setId(u.getId());
      personDAO.insert(s);
    } catch (SQLException ex) {
      throw new ExamException("添加参测人员出错", ex);
    }
  }

  @Override
  public TestPersonnel deleteTestPersonnel(int id) {
    try {
      User user = UserDAO.findById(id);
      if (user.getLastLogin() != null) {
        throw new ExamException("该账户已启用，不能删除");
      }
      TestPersonnel testPersonnel = personDAO.findById(id);
      personDAO.delete(id);
      userDAO.delete(id);

      testPersonnel.setUser(user);
      return testPersonnel;
    } catch (SQLException e) {
      throw new ExamException("删除用户出错", e);
    }
  }
}
