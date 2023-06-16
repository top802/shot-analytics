package top.shot.analytics.shotanalytics;
import java.sql.Connection;
import java.sql.ResultSet;
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
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import top.shot.analytics.shotanalytics.jdbc_manager.DatabaseConnection;
import top.shot.analytics.shotanalytics.model_dto.ShellingCard;
import top.shot.analytics.shotanalytics.model_dto.ShellingCardInTable;


public class ShotAnalytics extends Application {

  private Button saveButton, updateButton, deleteButton;
  private Label datePickerLabel, strafing, numbersCannonades,
          startStrafing, endStrafing, positionLabel,
          weaponTypeLabel;
  private DatePicker datePicker;
  private TextField strafingInput, numbersCannonadesInput,
          startStrafingInput, endStrafingInput, positionInput,
          weaponTypeInput;
  private LineChart<String, Number> lineChart;
  private String startStrafingData;
  private TableView<ShellingCardInTable> tableView;

  @Override
  public void start(Stage primaryStage) {
//     set inputs fields and labels
    setInputFields();
    setLabels();
    //  CRUD operation within DB
    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.getDatabaseConnection();
    saveShellingCard(connection);
    updateShellingCard();
    deleteShellingCard();
//      set element on the scene
    setScene(primaryStage, connection);
  }

  private void saveShellingCard(Connection connection) {
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

      saveShellingCardToDataBase(shellingCard, connection);
//    set chart
      setChart(shellingCard);
    });
  }

  private void updateShellingCard() {
  }

  private void deleteShellingCard() {
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

  private void setInputFields() {
    positionInput = new TextField();
    weaponTypeInput = new TextField();
    strafingInput = new TextField();
    numbersCannonadesInput = new TextField();
    startStrafingInput = new TextField();
    endStrafingInput = new TextField();
    datePicker = new DatePicker();
  }

  private void setScene(Stage primaryStage, Connection connection) {
    primaryStage.setTitle("Картка обстрілку");
    System.out.println("here");
//  crate view table
    tableView = new TableView<>();
    tableView.setPadding(new Insets(10,10,10,10));
    setValueToColumns(tableView);
    ObservableList<ShellingCardInTable> cardList = FXCollections.observableArrayList();

    try(Statement statement = connection.createStatement()){
      ResultSet resultSet = statement.executeQuery("SELECT * FROM analytics;");
      while (resultSet.next()){
        ShellingCardInTable shellingCardInTable = new ShellingCardInTable(
            resultSet.getString("date_picker"),
            resultSet.getString("position"),
            resultSet.getString("weapon_type"),
            resultSet.getInt("strafing"),
            resultSet.getInt("numbersCannonades"),
            resultSet.getString("startStrafing"),
            resultSet.getString("endStrafing")
            );
        cardList.add(shellingCardInTable);
      }
    }catch (Exception exception){
      exception.getStackTrace();
    }
    tableView.setItems(cardList);
    // create horizontal box for 3 buttons
    HBox buttonsBox = new HBox(20); // 20 - відстань між кнопками
    buttonsBox.getChildren().addAll(saveButton, updateButton, deleteButton);
//  graph
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(startStrafingData);
    lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Analytics");

    VBox inputBox = new VBox(10);
    inputBox.setPadding(new Insets(10,10,10,10));

    inputBox.getChildren().addAll(datePickerLabel, datePicker, positionLabel, positionInput, weaponTypeLabel, weaponTypeInput,
        strafing, strafingInput, numbersCannonades, numbersCannonadesInput, startStrafing, startStrafingInput,
        endStrafing, endStrafingInput, buttonsBox, lineChart);

    SplitPane sceneElements = new SplitPane();
    sceneElements.setDividerPositions(0.5);
    sceneElements.getItems().addAll(inputBox, tableView);


    Scene scene = new Scene(sceneElements, 1400, 700); // Встановіть ширину і висоту сцени за потребою
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  private void setValueToColumns(TableView<ShellingCardInTable> tableView) {
    TableColumn<ShellingCardInTable, String> dateColumn = new TableColumn<>("Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    dateColumn.setPrefWidth(85);
    TableColumn<ShellingCardInTable, String> positionColumn = new TableColumn<>("Position");
    positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
    positionColumn.setPrefWidth(100);
    TableColumn<ShellingCardInTable, String> weaponTypeColumn = new TableColumn<>("WeaponType");
    weaponTypeColumn.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
    weaponTypeColumn.setPrefWidth(100);
    TableColumn<ShellingCardInTable,Integer> strafingColumn = new TableColumn<>("Strafing");
    strafingColumn.setCellValueFactory(new PropertyValueFactory<>("strafing"));
    strafingColumn.setPrefWidth(75);
    TableColumn<ShellingCardInTable,Integer> cannonadesColumn = new TableColumn<>("NumberCannonades");
    cannonadesColumn.setCellValueFactory(new PropertyValueFactory<>("numbersCannonades"));
    cannonadesColumn.setPrefWidth(150);
    TableColumn<ShellingCardInTable, String> startStrafingColumn = new TableColumn<>("StartStrafing");
    startStrafingColumn.setCellValueFactory(new PropertyValueFactory<>("startStrafing"));
    strafingColumn.setPrefWidth(75);
    TableColumn<ShellingCardInTable, String> endStrafingColumn = new TableColumn<>("EndStrafing");
    endStrafingColumn.setCellValueFactory(new PropertyValueFactory<>("endStrafing"));
    endStrafingColumn.setPrefWidth(75);


    tableView.getColumns().addAll(
        dateColumn, positionColumn, weaponTypeColumn,
        strafingColumn, cannonadesColumn, startStrafingColumn, endStrafingColumn);

  }

  private void setChart(ShellingCard shellingCard) {
    // збереження в базу даних
    buildGraph(shellingCard.getDatePicker(), shellingCard.getNumbersCannonades());
  }

  private void saveShellingCardToDataBase(ShellingCard shellingCard, Connection connection) {

    String query = String.format("INSERT INTO `shot_analytics`.`analytics` "
        + "(`date_picker`, `strafing`, `numbersCannonades`, `startStrafing`, `endStrafing`, `position`, `weapon_type`)"
        + " VALUES ('%s', %d, %d, '%s', '%s', '%s', '%s')",shellingCard.getDatePicker(), shellingCard.getStrafing(), shellingCard.getNumbersCannonades(),
        shellingCard.getStartStrafing(), shellingCard.getEndStrafing(), shellingCard.getPosition(), shellingCard.getWeaponType());
    System.out.println(query);
    try {
      Statement statement = connection.createStatement();
      statement.execute(query);
      statement.close();

    }catch (Exception exception){
      exception.getStackTrace();
    }
    showTable(connection);
  }

  private void showTable(Connection connection) {
    ObservableList<ShellingCardInTable> cardList = FXCollections.observableArrayList();

    try(Statement statement = connection.createStatement()){
      ResultSet resultSet = statement.executeQuery("SELECT * FROM analytics;");
      while (resultSet.next()){
        ShellingCardInTable shellingCardInTable = new ShellingCardInTable(
            resultSet.getString("date_picker"),
            resultSet.getString("position"),
            resultSet.getString("weapon_type"),
            resultSet.getInt("strafing"),
            resultSet.getInt("numbersCannonades"),
            resultSet.getString("startStrafing"),
            resultSet.getString("endStrafing")
        );
        cardList.add(shellingCardInTable);
      }
    }catch (Exception exception){
      exception.getStackTrace();
    }
    tableView.setItems(cardList);
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