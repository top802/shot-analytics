package top.shot.analytics.shotanalytics.model_dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShellingCardInTable {

  private final SimpleIntegerProperty id;
  private final SimpleStringProperty date;
  private final SimpleStringProperty position;
  private final SimpleStringProperty weaponType;
  private final SimpleIntegerProperty strafing;
  private final SimpleIntegerProperty numbersCannonades;
  private final SimpleStringProperty startStrafing;
  private final SimpleStringProperty endStrafing;


  public ShellingCardInTable(int id, String date, String position, String weaponType,
      int strafing, int numbersCannonades,
      String startStrafing, String endStrafing) {
    this.id = new SimpleIntegerProperty(id);
    this.date = new SimpleStringProperty(date);
    this.position = new SimpleStringProperty(position);
    this.weaponType = new SimpleStringProperty(weaponType);
    this.strafing = new SimpleIntegerProperty(strafing);
    this.numbersCannonades = new SimpleIntegerProperty(numbersCannonades);
    this.startStrafing = new SimpleStringProperty(startStrafing);
    this.endStrafing = new SimpleStringProperty(endStrafing);

  }

  public int getId() {
    return id.get();
  }


  public SimpleIntegerProperty idProperty() {return id;}

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

  public String getStartStrafing() {
    return startStrafing.get();
  }

  public SimpleStringProperty startStrafingProperty() {
    return startStrafing;
  }

  public String getEndStrafing() {
    return endStrafing.get();
  }

  public SimpleStringProperty endStrafingProperty() {
    return endStrafing;
  }

  public String getPosition() {
    return position.get();
  }

  public SimpleStringProperty positionProperty() {
    return position;
  }

  public String getWeaponType() {
    return weaponType.get();
  }

  public SimpleStringProperty weaponTypeProperty() {
    return weaponType;
  }
}
