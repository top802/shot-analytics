package top.shot.analytics.shotanalytics.model_dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ShellingCard {

  private Long id;
  private String datePicker;

  private int strafing;

  private int numbersCannonades;

  private String startStrafing;

  private String endStrafing;

  private String rangeStrafing;

  private String position;

  private String weaponType;


  public ShellingCard(String datePicker, int strafing, int numbersCannonades,
      String startStrafing, String endStrafing,
      String position, String weaponType) {
    this.datePicker = datePicker;
    this.strafing = strafing;
    this.numbersCannonades = numbersCannonades;
    this.startStrafing = startStrafing;
    this.endStrafing = endStrafing;
    this.position = position;
    this.weaponType = weaponType;
  }


}