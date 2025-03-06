package com.qst;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MysqlCjAbandonedConnectionCleanupListener implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      System.out.println("ServletContext 初始化");
    } catch (Exception e) {
      System.out.println("ServletContext 初始化失败");
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    try {
      System.out.println("ServletContext 销毁并释放JDBC连接资源");
      Enumeration<Driver> drivers = DriverManager.getDrivers();
      while (drivers.hasMoreElements()) {
        Driver driver = drivers.nextElement();
        DriverManager.deregisterDriver(driver);
        System.out.println("unregistering jdbc Driver: " + driver);
      }
      AbandonedConnectionCleanupThread.uncheckedShutdown();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("销毁工作异常");
    }
  }
}
