package top.shot.analytics.shotanalytics.jpa_manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

  public Connection databaseConnection;

  public Connection getDatabaseConnection(){
    String databaseName = "shot_analytics";
    String databaseUserName = "root";
    String databasePassword = "134679258top";
    String url = "jdbc:mysql://localhost:3306/" + databaseName + "?createDatabaseIfNotExist=true&autoReconnect=true";
    try{
      Class.forName("com.mysql.cj.jdbc.Driver");
      databaseConnection = DriverManager.getConnection(url, databaseUserName, databasePassword);
    }catch (Exception exception){
      exception.getStackTrace();
    }
    return databaseConnection;
  }




}
