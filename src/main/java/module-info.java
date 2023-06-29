module top.shot.analytics.shotanalytics {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.persistence;
  requires org.jetbrains.annotations;
  requires lombok;
  requires java.sql;
//  requires mysql.connector.java;
  requires org.xerial.sqlitejdbc;

  opens top.shot.analytics.shotanalytics to javafx.fxml;
  opens top.shot.analytics.shotanalytics.model_dto to javafx.base;

  exports top.shot.analytics.shotanalytics;
}