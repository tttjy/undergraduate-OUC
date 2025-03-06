package com.qst.service;

import com.qst.entity.User;

public interface IUserService {
  /*List<User> findUsers();

  User findUserById(int id);


  void updateUser(User user);

  void deleteUserById(int id);

  void resetPassword(int id);*/
  void saveUser(User u);

  User login(String login, String password);

  void changePassword(int userId, String oldPassword, String newPassword);
}
