package top.shot.analytics.shotanalytics;
import java.sql.Connection;
import java.sql.Statement;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import top.shot.analytics.shotanalytics.jdbc_manager.DatabaseConnection;
import top.shot.analytics.shotanalytics.model_dto.ShellingCard;
import top.shot.analytics.shotanalytics.table.ShellingCardInTable;

public class ShotAnalytics extends Application {

  Button saveButton, updateButton, deleteButton;
  Label datePickerLabel, strafing, numbersCannonades,
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

    //  CRUD картки з БД
    saveShellingCard();
    updateShellingCard();
    deleteShellingCard();




    // Графік для відображення аналітики
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(startStrafingData);
    lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Analytics");

    // Створення сцени та відображення
    GridPane gridPane = new GridPane();
    gridPane.add(lineChart, 0, 8, 2, 1);

    setGridPaneElements(gridPane, primaryStage);
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
    datePickerLabel = new Label();
    datePickerLabel.setText("Дата");
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

  private void setGridPaneElements(GridPane gridPane, Stage primaryStage) {
    BorderPane borderPane = new BorderPane();



// Встановлюємо таблицю
    TableView<ShellingCardInTable> tableView = new TableView<>();
    setTableColumns(tableView);

    ObservableList<ShellingCardInTable> cardList = FXCollections.observableArrayList();
    cardList.add(new ShellingCardInTable("2023-06-11", "Don","Arta",
        1, 5, "10:30", "10:40"));
    tableView.setItems(cardList);
    borderPane.setRight(tableView);

    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.setPadding(new Insets(10,10,10,10));
    gridPane.add(datePickerLabel, 0, 0);
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

    gridPane.add(saveButton, 0, 7);
    HBox buttonsBox = new HBox(20); // 20 - падінг між кнопками
    buttonsBox.getChildren().addAll(updateButton, deleteButton);
    gridPane.add(buttonsBox, 1, 7);
    // Встановлюємо поля вводу у ліву частину
    VBox inputBox = new VBox(10);
    inputBox.getChildren().addAll(datePicker, positionLabel, positionInput, weaponTypeLabel, weaponTypeInput,
        strafing, strafingInput, numbersCannonades, numbersCannonadesInput, startStrafing, startStrafingInput,
        endStrafing, endStrafingInput, saveButton, updateButton, deleteButton);
//    inputBox.getChildren().add(gridPane);
    borderPane.setLeft(inputBox);

    Scene borderScene = new Scene(borderPane, 800, 600); // Встановіть ширину і висоту сцени за потребою
    Scene gridPaneScene = new Scene(gridPane, 800, 600); // Встановіть ширину і висоту сцени за потребою
// Встановіть сцену на вашому Stage
    primaryStage.setScene(borderScene);
//    primaryStage.setScene(gridPaneScene);
// Покажіть Stage
    primaryStage.show();

  }

  private void setTableColumns(TableView<ShellingCardInTable> tableView) {
    TableColumn<ShellingCardInTable, String> dateColumn = new TableColumn<>("Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<ShellingCardInTable, String> positionColumn = new TableColumn<>("Position");
    positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

    TableColumn<ShellingCardInTable, String> weaponTypeColumn = new TableColumn<>("WeaponType");
    weaponTypeColumn.setCellValueFactory(new PropertyValueFactory<>("weaponType"));

    TableColumn<ShellingCardInTable,Integer> strafingColumn = new TableColumn<>("Strafing");
    strafingColumn.setCellValueFactory(new PropertyValueFactory<>("strafing"));

    TableColumn<ShellingCardInTable,Integer> cannonadesColumn = new TableColumn<>("NumberCannonades");
    cannonadesColumn.setCellValueFactory(new PropertyValueFactory<>("numbersCannonades"));

    TableColumn<ShellingCardInTable, String> startStrafingColumn = new TableColumn<>("StartStrafing");
    startStrafingColumn.setCellValueFactory(new PropertyValueFactory<>("startStrafing"));

    TableColumn<ShellingCardInTable, String> endStrafingColumn = new TableColumn<>("EndStrafing");
    endStrafingColumn.setCellValueFactory(new PropertyValueFactory<>("endStrafing"));

    tableView.getColumns().addAll(
        dateColumn, positionColumn, weaponTypeColumn,
        strafingColumn, cannonadesColumn, startStrafingColumn, endStrafingColumn);

  }

  private void updateChart(ShellingCard shellingCard) {
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