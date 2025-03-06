package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.dao.DAOFactory;
import com.qst.dao.UserDAO;
import com.qst.entity.User;
import com.qst.service.IUserAdminService;
import java.util.List;

public class UserAdminServiceImpl implements IUserAdminService {
  private final UserDAO userDAO = DAOFactory.getDAO(UserDAO.class);

  @Override
  public List<User> findUsers() {
    return userDAO.findAll();
  }

  @Override
  public User findUserById(int id) {
    return UserDAO.findById(id);
  } // 用户查询
  @Override
  public void saveUser(User u) {
    User temp = userDAO.findByLogin(u.getLogin()); // 检查用户是否已经存在
    if (temp != null) {
      throw new ExamException("登录名已经存在了");
    }
    userDAO.insert(u); // 存在之后用insert函数插入
  }
  @Override
  public void updateUser(User user) {
    User temp = userDAO.findByLogin(user.getLogin());
    if (temp != null && temp.getId() != user.getId()) {
      throw new ExamException("登录名已经存在了");
    }
    userDAO.update(user);
  } // 实现修改用户
  @Override
  public void deleteUserById(int id) {
    User user = UserDAO.findById(id);
    if (user == null) {
      return;
    }
    if (user.getLastLogin() != null) {
      throw new ExamException("该用户不能被删除");
    }
    userDAO.delete(id);
  }
  @Override
  public void resetPassword(int id) {
    UserDAO.updatePassword(id, User.PASSWORD);
  }
}
