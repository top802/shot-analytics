package top.shot.analytics.shotanalytics.db_query;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQuery {

  public static void createShotAnalyticsDatabase(Connection conn){
    String query = createShotAnalyticsTable();
      try (Statement statement = conn.createStatement()) {
        statement.executeUpdate(query);
    } catch (SQLException e) {
        System.out.println("error here");
      System.out.println(e.getMessage());
    }
  }

  public static String createShotAnalyticsTable() {
    String query = "CREATE TABLE IF NOT EXISTS shot_analytics"
        + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
        + "  date_picker VARCHAR(25),"
        + "  position VARCHAR(45),"
        + "  weapon_type VARCHAR(45),"
        + "  strafing INT(11),"
        + "  numbersCannonades INT(11),"
        + "  startStrafing TIME,"
        + "  endStrafing TIME);";
   return query;
  }

  public static String insertShellingCardQuery(
      String date, String position, String weaponType, int strafing, int numbersCannonades,
      String startStrafing, String endStrafing){
    String query = String.format("INSERT INTO shot_analytics "
            + "(`date_picker`, `strafing`, `numbersCannonades`, `startStrafing`, `endStrafing`, `position`, `weapon_type`)"
            + " VALUES ('%s', %d, %d, '%s', '%s', '%s', '%s')",date, strafing, numbersCannonades,
            startStrafing, endStrafing, position, weaponType);
    return query;
  }

  public static String updateShellingCardQuery(
      String date, int strafing, int numbersCannonades,
      String startStrafing, String endStrafing,
      String position, String weaponType, int id
  ){
    String query = String.format("UPDATE shot_analytics SET"
            + " `date_picker` = '%s',`strafing` = '%d', `numbersCannonades` = '%d',"
            + " `startStrafing` = '%s', `endStrafing` = '%s',"
            + " `position` = '%s', `weapon_type` = '%s' WHERE (`id` = '%d');",
        date, strafing, numbersCannonades, startStrafing, endStrafing,
        position, weaponType, id);
    return query;
  }
// sql for MySQL
//  public static String getQueryAnalyticPerDayForOnePosition(String date, String position){
//    String query = "SELECT date_picker, position,"
//        + " CONCAT(TIME_FORMAT(MIN(startStrafing), '%H:%i'),' - ',"
//        + " TIME_FORMAT(DATE_ADD(MIN(startStrafing), INTERVAL 1 HOUR), '%H:%i'))"
//        + " AS strafingTime, SUM(strafing) AS strafing, SUM(numbersCannonades) AS numbersCannonades"
//        + " FROM shot_analytics WHERE date_picker = '"+date+"' AND position = '"+position+"'"
//        + " GROUP BY date_picker, position, HOUR (startStrafing)"
//        + " ORDER BY date_picker, position, HOUR (startStrafing)";
//    return query;
//  }

  public static String getQueryAnalyticPerDayForOnePosition(String date, String position){
    String query = "SELECT date_picker, position,"
        + " strftime('%H:%M', MIN(startStrafing)) || ' - ' || strftime('%H:%M', datetime(MIN(startStrafing), '+1 hour')) AS strafingTime,"
        + " SUM(strafing) AS strafing, SUM(numbersCannonades) AS numbersCannonades"
        + " FROM shot_analytics WHERE date_picker = '"+date+"' AND position = '"+position+"'"
        + " GROUP BY date_picker, position, strftime('%H', startStrafing)"
        + " ORDER BY date_picker, position, strftime('%H', startStrafing)";
    return query;
  }

  public static String getQueryAnalyticPerDaysForALLPositions(String date){
    String query = "SELECT date_picker, position,"
        + " strftime('%H:%M', MIN(startStrafing)) || ' - ' || strftime('%H:%M', datetime(MIN(startStrafing), '+1 hour')) AS strafingTime,"
        + " SUM(strafing) AS strafing, SUM(numbersCannonades) AS numbersCannonades"
        + " FROM shot_analytics WHERE date_picker = '"+date+"'"
        + " GROUP BY date_picker, position, strftime('%H', startStrafing)"
        + " ORDER BY date_picker, position, strftime('%H', startStrafing)";
    return query;
  }

  public static String getQueryAnalyticBetweenDaysForOnePosition(String firstDay, String lastDay, String position){
    String query = "SELECT date_picker, position,"
        + " strftime('%H:%M', MIN(startStrafing)) || ' - ' || strftime('%H:%M', datetime(MIN(startStrafing), '+1 hour')) AS strafingTime,"
        + " SUM(strafing) AS strafing, SUM(numbersCannonades) AS numbersCannonades"
        + " FROM shot_analytics WHERE"
        + " date_picker BETWEEN '"+firstDay+"' AND '"+lastDay+"' AND position = '"+position+"'"
        + " GROUP BY date_picker, position, strftime('%H', startStrafing)"
        + " ORDER BY date_picker, position, strftime('%H', startStrafing)";
    return query;
  }

  public static String getQueryAnalyticBetweenDaysForAllPositions(String firstDay, String lastDay){
    String query = "SELECT date_picker, position,"
        + " strftime('%H:%M', MIN(startStrafing)) || ' - ' || strftime('%H:%M', datetime(MIN(startStrafing), '+1 hour')) AS strafingTime,"
        + " SUM(strafing) AS strafing, SUM(numbersCannonades) AS numbersCannonades"
        + " FROM shot_analytics WHERE"
        + " date_picker BETWEEN '"+firstDay+"' AND '"+lastDay+"'"
        + " GROUP BY date_picker, position, strftime('%H', startStrafing)"
        + " ORDER BY date_picker, position, strftime('%H', startStrafing)";
    return query;
  }
  public static String deleteShellingCardQuery(int id) {
    String query = String.format("DELETE FROM shot_analytics WHERE (`id` = '%s');", id);
    return  query;
  }

  public static String getAllShellingCards() {
    return "SELECT * FROM shot_analytics";
  }
}
