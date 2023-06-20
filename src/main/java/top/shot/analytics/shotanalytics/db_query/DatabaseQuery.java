package top.shot.analytics.shotanalytics.db_query;

public class DatabaseQuery {

  public static String insertShellingCardQuery(
      String date, String position, String weaponType, int strafing, int numbersCannonades,
      String startStrafing, String endStrafing){
    String query = String.format("INSERT INTO `shot_analytics`.`analytics` "
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
    String query = String.format("UPDATE `shot_analytics`.`analytics` SET"
            + " `date_picker` = '%s',`strafing` = '%d', `numbersCannonades` = '%d',"
            + " `startStrafing` = '%s', `endStrafing` = '%s',"
            + " `position` = '%s', `weapon_type` = '%s' WHERE (`id` = '%d');",
        date, strafing, numbersCannonades, startStrafing, endStrafing,
        position, weaponType, id);
    return query;
  }

  public static String getAnalyticPerDayQuery(String date, String position){
    String query = "SELECT date_picker, position,"
        + " CONCAT(TIME_FORMAT(MIN(startStrafing), '%H:%i'),' - ', TIME_FORMAT(DATE_ADD(MIN(startStrafing), INTERVAL 1 HOUR), '%H:%i'))"
        + " AS strafingTime, SUM(strafing) AS strafing, SUM(numbersCannonades) AS numbersCannonades"
        + " FROM `shot_analytics`.`analytics` WHERE date_picker = '"+date+"' AND position = '"+position+"'"
        + " GROUP BY date_picker, position, HOUR (startStrafing)"
        + " ORDER BY date_picker, position, HOUR (startStrafing)";
    return query;
  }

  public static String deleteShellingCardQuery(int id) {
    String query = String.format("DELETE FROM `shot_analytics`.`analytics` WHERE (`id` = '%s');", id);
    return  query;
  }

  public static String getAll() {
    return "SELECT * FROM analytics";
  }
}