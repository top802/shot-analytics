package top.shot.analytics.shotanalytics.jpa_manager;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

  public Connection databaseConnection;

  public Connection getDatabaseConnection(){
    String databaseName = "shot_analytics";
    String databaseUserName = "";
    String databasePassword = "";
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
