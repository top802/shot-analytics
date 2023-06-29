package top.shot.analytics.shotanalytics.jdbc_manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  public Connection databaseConnection;

  public static Connection getDatabaseConnection(){
    Connection connection = null;
    try{
      connection = DriverManager
          .getConnection("jdbc:sqlite:src/main/resources/shot_analytics.db");
      System.out.println("create connection");
      DatabaseMetaData meta = connection.getMetaData();
      System.out.println("The driver name is " + meta.getDriverName());
      System.out.println("A new database has been created.");
      return connection;
    }catch (SQLException exception){
      System.out.println("error to create connection");
      System.out.println(exception.getMessage());
    }
    return connection;
  };

//  public Connection getDatabaseConnection(){
//    String databaseName = "shot_analytics";
//    String databaseUserName = "root";
//    String databasePassword = "134679258top";
//    String url = "jdbc:mysql://localhost:3306/" + databaseName + "?createDatabaseIfNotExist=true&autoReconnect=true";
//    try{
//      Class.forName("com.mysql.cj.jdbc.Driver");
//      databaseConnection = DriverManager.getConnection(url, databaseUserName, databasePassword);
//    }catch (Exception exception){
//      exception.getStackTrace();
//    }
//    return databaseConnection;
//  }




}
