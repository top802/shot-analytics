module top.shot.analytics.shotanalytics {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.persistence;
  requires org.jetbrains.annotations;
  requires lombok;
  requires java.sql;
  requires mysql.connector.java;

  opens top.shot.analytics.shotanalytics to javafx.fxml;
  exports top.shot.analytics.shotanalytics;
}