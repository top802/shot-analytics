package top.shot.analytics.shotanalytics;
import java.sql.Connection;
import java.sql.Statement;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import top.shot.analytics.shotanalytics.jpa_manager.DatabaseConnection;
import top.shot.analytics.shotanalytics.model_dto.ShellingCard;

public class ShotAnalytics extends Application {

  Button saveButton, updateButton, deleteButton;
  Label showInfo, strafing, numbersCannonades,
        startStrafing, endStrafing, positionLabel,
        weaponTypeLabel;
  DatePicker datePicker;
  TextField strafingInput, numbersCannonadesInput,
      startStrafingInput, endStrafingInput, positionInput,
      weaponTypeInput;
  LineChart<String, Number> lineChart;
  String startStrafingData;

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Картка обстрілку");

    // Календар з можливістю вибору дати
    datePicker = new DatePicker();

    // поля для введення тексту та підписи до них
    setTextFields();
    setLabels();

    saveShellingCard();
    updateShellingCard();
    deleteShellingCard();
    GridPane gridPane = new GridPane();
    setGridPaneElements(gridPane);

    // Графік для відображення аналітики
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(startStrafingData);
    lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Analytics");
    gridPane.add(lineChart, 0, 8, 2, 1);

    // Створення сцени та відображення
    Scene scene = new Scene(gridPane, 500, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }



  private void saveShellingCard() {
    saveButton.setOnAction(event -> {
      String date = datePicker.getValue().toString();
      String strafingData = strafingInput.getText();
      String numbersCannonadesData = numbersCannonadesInput.getText();
      startStrafingData = startStrafingInput.getText();
      String endStrafingData = endStrafingInput.getText();
      String positionData = positionInput.getText();
      String weaponTypeData = weaponTypeInput.getText();

      ShellingCard shellingCard = new ShellingCard(
          date,Integer.parseInt(strafingData), Integer.parseInt(numbersCannonadesData),
          startStrafingData, endStrafingData,
          positionData, weaponTypeData);
      // Виконайте дії зі збереженими даними, наприклад, оновлення графіку
      updateChart(shellingCard);
    });
  }
  private void deleteShellingCard() {
  }

  private void updateShellingCard() {
  }

  private void setLabels() {
    positionLabel = new Label();
    positionLabel.setText("Позиція");
    weaponTypeLabel = new Label();
    weaponTypeLabel.setText("Тип озброєння");
    strafing = new Label();
    strafing.setText("Кількість обстрілів:");
    numbersCannonades = new Label();
    numbersCannonades.setText("Кількіть прильотів:");
    startStrafing = new Label();
    startStrafing.setText("Початок обстрілу:");
    endStrafing = new Label();
    endStrafing.setText("Кінець обстрілу:");
    showInfo = new Label();
    saveButton = new Button();
    saveButton.setText("Зберегти");
    updateButton = new Button();
    updateButton.setText("Оновити");
    deleteButton = new Button();
    deleteButton.setText("Видалити");
  }

  private void setTextFields() {
    positionInput = new TextField();
    weaponTypeInput = new TextField();
    strafingInput = new TextField();
    numbersCannonadesInput = new TextField();
    startStrafingInput = new TextField();
    endStrafingInput = new TextField();
  }

  private void setGridPaneElements(GridPane gridPane) {
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(10,10,10,10));
    gridPane.add(new Label("Date:"), 0, 0);
    gridPane.add(datePicker, 1, 0);
    gridPane.add(positionLabel, 0, 1);
    gridPane.add(positionInput, 1,1);
    gridPane.add(weaponTypeLabel, 0,2);
    gridPane.add(weaponTypeInput, 1,2);
    gridPane.add(strafing, 0, 3);
    gridPane.add(strafingInput, 1, 3);
    gridPane.add(numbersCannonades, 0, 4);
    gridPane.add(numbersCannonadesInput, 1, 4);
    gridPane.add(startStrafing, 0, 5);
    gridPane.add(startStrafingInput, 1, 5);
    gridPane.add(endStrafing, 0, 6);
    gridPane.add(endStrafingInput, 1, 6);

    GridPane.setMargin(saveButton, new Insets(0, 20, 0, 0));
    GridPane.setMargin(updateButton, new Insets(0, 20, 0, 0));
    gridPane.add(saveButton, 0, 7);
    HBox buttonsBox = new HBox(20); // 20 - падінг між кнопками
    buttonsBox.getChildren().addAll(updateButton, deleteButton);
    gridPane.add(buttonsBox, 1, 7);

  }

  private void updateChart(ShellingCard shellingCard) {
    showInfo.setText(shellingCard.toString());
    // збереження в базу даних
    saveShellingCardToDataBase(shellingCard);
    buildGraph(shellingCard.getDatePicker(), shellingCard.getNumbersCannonades());
  }

  private void saveShellingCardToDataBase(ShellingCard shellingCard) {
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connectionToDB = databaseConnection.getDatabaseConnection();
    String query = String.format("INSERT INTO `shot_analytics`.`analytics` "
        + "(`date_picker`, `strafing`, `numbersCannonades`, `startStrafing`, `endStrafing`, `position`, `weapon_type`)"
        + " VALUES ('%s', %d, %d, '%s', '%s', '%s', '%s')",shellingCard.getDatePicker(), shellingCard.getStrafing(), shellingCard.getNumbersCannonades(),
        shellingCard.getStartStrafing(), shellingCard.getEndStrafing(), shellingCard.getPosition(), shellingCard.getWeaponType());
    System.out.println(query);
    try {
      Statement statement = connectionToDB.createStatement();
      statement.execute(query);
      statement.close();

    }catch (Exception exception){
      exception.getStackTrace();
    }
  }

  private void buildGraph(String date, Integer numbersCannonades) {
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.getData().add(new XYChart.Data<>(date, numbersCannonades));
    series.getData().add(new XYChart.Data<>(date, numbersCannonades));
    series.getData().add(new XYChart.Data<>(date, numbersCannonades));
    series.getData().add(new XYChart.Data<>(date, numbersCannonades));

    // Додавання підпису для кожної точки
    for (XYChart.Data<String, Number> data : series.getData()) {
      data.setNode(new HoveredThresholdNode(data.getYValue().toString()));
    }
    lineChart.getData().clear();
    lineChart.getData().add(series);
  }

  public static void main(String[] args) {
    launch(args);
  }
  static class HoveredThresholdNode extends StackPane {
    public HoveredThresholdNode(String valueY) {
      setPrefSize(10, 10);

      final Label label = createDataThresholdLabel(valueY);

      getChildren().setAll(label);
      setCursor(Cursor.NONE);
    }

    private Label createDataThresholdLabel(String valueY) {
      final Label label = new Label(valueY);
      label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
      label.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

      label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
      return label;
    }
  }
}