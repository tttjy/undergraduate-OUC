package com.qst.service.impl;

import com.qst.ExamException;
import com.qst.dao.DAOFactory;
import com.qst.dao.UserDAO;
import com.qst.entity.User;
import com.qst.service.IUserService;
import java.sql.Timestamp;

public class UserServiceImpl implements IUserService {
  private final UserDAO userDAO = DAOFactory.getDAO(UserDAO.class);
  /*
          @Override
          public List<User> findUsers() {
                  return userDAO.findAll();
          }

          @Override
          public User findUserById(int id) {

                  return userDAO.findById(id);
          }



          @Override
          public void updateUser(User user) {
                  User temp = userDAO.findByLogin(user.getLogin());
                  if (temp != null && temp.getId() != user.getId()) {
                          throw new ExamException("登录名已经存在了");
                  }
                  userDAO.update(user);
          }

          @Override
          public void deleteUserById(int id) {
                  User user = userDAO.findById(id);
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
                  userDAO.updatePassword(id, User.PASSWORD);

          }*/

  @Override
  public User login(String login, String password) {
    User user = userDAO.findByLogin(login);
    String msg = null;
    if (user == null) {
      msg = "用户不存在";
    } else if (user.getStatus() != 1) {
      msg = "账户已被禁用，不能登录";
    } else if (!user.getPasswd().equals(password)) {
      msg = "密码错误";
    }
    if (msg != null) {
      throw new ExamException(msg);
    }
    userDAO.updateLastLogin(user.getId(), new Timestamp(System.currentTimeMillis()));
    return user;
  }
  @Override
  public void saveUser(User u) {
    User temp = userDAO.findByLogin(u.getLogin());
    if (temp != null) {
      throw new ExamException("登录名已经存在了");
    }
    userDAO.insert(u);
  }

  @Override
  public void changePassword(int userId, String oldPassword, String newPassword) {
    User user = UserDAO.findById(userId);
    if (!user.getPasswd().equals(oldPassword)) {
      throw new ExamException("原密码错误");
    }
    UserDAO.updatePassword(userId, newPassword);
  }
}
