package top.shot.analytics.shotanalytics.model_dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShellingCardInAnalytics {
  private final SimpleStringProperty date;
  private final SimpleStringProperty position;
  private final SimpleIntegerProperty strafing;
  private final SimpleIntegerProperty numbersCannonades;
  private final SimpleStringProperty strafingTime;

  public ShellingCardInAnalytics(String date,String position,
      int strafing, int numbersCannonades, String strafingTime){
    this.date = new SimpleStringProperty(date);
    this.position = new SimpleStringProperty(position);
    this.strafing = new SimpleIntegerProperty(strafing);
    this.numbersCannonades = new SimpleIntegerProperty(numbersCannonades);
    this.strafingTime = new SimpleStringProperty(strafingTime);
  }

  public String getDate() {
    return date.get();
  }

  public SimpleStringProperty dateProperty() {
    return date;
  }

  public int getStrafing() {
    return strafing.get();
  }

  public SimpleIntegerProperty strafingProperty() {
    return strafing;
  }

  public int getNumbersCannonades() {
    return numbersCannonades.get();
  }

  public SimpleIntegerProperty numbersCannonadesProperty() {
    return numbersCannonades;
  }

  public String getStrafingTime() {
    return strafingTime.get();
  }

  public SimpleStringProperty strafingTimeProperty() {
    return strafingTime;
  }

  public String getPosition() {
    return position.get();
  }

  public SimpleStringProperty positionProperty() {
    return position;
  }

}
