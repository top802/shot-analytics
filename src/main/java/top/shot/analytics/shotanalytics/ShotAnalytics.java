package top.shot.analytics.shotanalytics;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import top.shot.analytics.shotanalytics.db_query.DatabaseQuery;
import top.shot.analytics.shotanalytics.jdbc_manager.DatabaseConnection;
import top.shot.analytics.shotanalytics.model_dto.ShellingCard;
import top.shot.analytics.shotanalytics.model_dto.ShellingCardInTable;


public class ShotAnalytics extends Application {

  private Button saveButton, updateButton, deleteButton, analyticsButton;
  private Label datePickerLabel, strafing, numbersCannonades,
          startStrafing, endStrafing, positionLabel,
          weaponTypeLabel, positionAnalyticsLabel, dateAnalyticsLabel;
  private DatePicker datePicker, analyticsDate;
  private TextField strafingInput, numbersCannonadesInput,
          startStrafingInput, endStrafingInput, positionInput,
          weaponTypeInput, analyticsPosition;
  private LineChart<String, Number> lineChart;
  private TableView<ShellingCardInTable> tableView;
  private TableView<ShellingCard> analyticTable;
  private String startStrafingData;
  private int id, rowIndex;

  @Override
  public void start(Stage primaryStage) {
//     set inputs fields and labels
    setInputFields();
    setLabels();

    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = databaseConnection.getDatabaseConnection();
    setupDatabaseOperation(connection);
    setupAnalytics(connection);
//      set element on the scene
    setScene(primaryStage, connection);
  }
  private void setLabels() {
    datePickerLabel = new Label();
    datePickerLabel.setText("Дата");
    positionLabel = new Label();
    positionLabel.setText("Позиція");
    dateAnalyticsLabel = new Label("Дата");
    positionAnalyticsLabel = new Label("Позиція");
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
    analyticsButton = new Button("аналітика");
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
    analyticsDate = new DatePicker();
    analyticsPosition = new TextField();
  }

  private void setupAnalytics(Connection connection) {
    createAnalyticsPerDayForPosition(connection);
  }

  private void createAnalyticsPerDayForPosition(Connection connection) {
    analyticsButton.setOnAction( event -> {
      String date = analyticsDate.getValue().toString();
      String position = analyticsPosition.getText();
      String query = DatabaseQuery.getAnalyticPerDayQuery(date, position);
      try(Statement statement = connection.createStatement()){
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
          String positionDB = resultSet.getString("position");
          System.out.println(positionDB);
        }


      } catch (SQLException exception){
        System.out.println("ERROR");
        exception.getStackTrace();
      }
    });

//    todo create datapicker and textfield and than create query
  }

  private void setupDatabaseOperation(Connection connection) {
    saveShellingCard(connection);
    updateShellingCard(connection);
    deleteShellingCard(connection);
  }

  private void saveShellingCard(Connection connection) {
    saveButton.setOnAction(event -> {
      var shellingCard = createShellingCard();
      saveShellingCardToDataBase(shellingCard, connection);
      setChart(shellingCard);
    });
  }

  private ShellingCard createShellingCard() {
    String date = datePicker.getValue().toString();
    String strafingData = strafingInput.getText();
    String numbersCannonadesData = numbersCannonadesInput.getText();
    startStrafingData = startStrafingInput.getText();
    String endStrafingData = endStrafingInput.getText();
    String positionData = positionInput.getText();
    String weaponTypeData = weaponTypeInput.getText();

    return new ShellingCard(
        date,Integer.parseInt(strafingData), Integer.parseInt(numbersCannonadesData),
        startStrafingData, endStrafingData,
        positionData, weaponTypeData);
  }

  private void updateShellingCard(Connection connection) {
    updateButton.setOnAction(event -> {
      rowIndex = tableView.getSelectionModel().getSelectedIndex();
      id = Integer.parseInt(String.valueOf(tableView.getItems().get(rowIndex).getId()));
      ShellingCard shellingCard = createShellingCard();
      String query = DatabaseQuery.updateShellingCardQuery(shellingCard.getDatePicker(), shellingCard.getStrafing(),
          shellingCard.getNumbersCannonades(), shellingCard.getStartStrafing(), shellingCard.getEndStrafing(),
          shellingCard.getPosition(), shellingCard.getWeaponType(), id);
      try(Statement statement = connection.createStatement()){
        statement.executeUpdate(query);
        showTable(connection);
      }catch (Exception exception){
        exception.getStackTrace();
      }
    });
  }

  private void deleteShellingCard(Connection connection) {
    deleteButton.setOnAction(event -> {
      rowIndex = tableView.getSelectionModel().getSelectedIndex();
      id = Integer.parseInt(String.valueOf(tableView.getItems().get(rowIndex).getId()));
      try(Statement statement = connection.createStatement()){
        String query = DatabaseQuery.deleteShellingCardQuery(id);
        statement.executeUpdate(query);
        showTable(connection);
      }catch (Exception exception){
        exception.getStackTrace();
      }
    });

  }

  private void setScene(Stage primaryStage, Connection connection) {
    primaryStage.setTitle("Картка обстрілку");
//  crate view table
    tableView = new TableView<>();
    tableView.setPadding(new Insets(10,10,10,10));
    setTableColumns(tableView);
    showTable(connection);
    // create horizontal box for 3 buttons
    HBox buttonsBox = new HBox(20); // 20 - відстань між кнопками
    buttonsBox.getChildren().addAll(saveButton, updateButton, deleteButton);
//  todo create HBox for analytics elements

//  todo create analytics table
    analyticTable = new TableView<>();
    analyticTable.setPadding(new Insets(5,5,5,5));
    setTableColumnsInAnalyticsTable(analyticTable);
// todo delete one day
    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel(startStrafingData);
    lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Analytics");

    VBox inputBox = new VBox(10);
    inputBox.setPadding(new Insets(10,10,10,10));
//  todo need change UI
    HBox analyticInputBox = new HBox(5);
    analyticInputBox.getChildren().addAll(analyticsDate, analyticsPosition, analyticsButton);

    HBox analyticLabelBox = new HBox(5);
    analyticLabelBox.getChildren().addAll(dateAnalyticsLabel, positionAnalyticsLabel);
    VBox analyticsBox = new VBox(5);
    analyticsBox.getChildren().addAll(analyticLabelBox, analyticInputBox);
    inputBox.getChildren().addAll(datePickerLabel, datePicker, positionLabel, positionInput, weaponTypeLabel, weaponTypeInput,
        strafing, strafingInput, numbersCannonades, numbersCannonadesInput, startStrafing, startStrafingInput,
        endStrafing, endStrafingInput, buttonsBox, analyticsBox, analyticTable);

    SplitPane sceneElements = new SplitPane();
    sceneElements.setDividerPositions(0.5);
    sceneElements.getItems().addAll(inputBox, tableView);


    Scene scene = new Scene(sceneElements, 1400, 700); // Встановіть ширину і висоту сцени за потребою
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  private void setTableColumnsInAnalyticsTable(TableView<ShellingCard> analyticTable) {
  }

  private void setTableColumns(TableView<ShellingCardInTable> tableView) {
    TableColumn<ShellingCardInTable, String> dateColumn = new TableColumn<>("Дата");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//    dateColumn.setPrefWidth(85);
    TableColumn<ShellingCardInTable, String> positionColumn = new TableColumn<>("Позиція");
    positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
//    positionColumn.setPrefWidth(75);
    TableColumn<ShellingCardInTable, String> weaponTypeColumn = new TableColumn<>("Зброя");
    weaponTypeColumn.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
    weaponTypeColumn.setPrefWidth(110);
    TableColumn<ShellingCardInTable,Integer> strafingColumn = new TableColumn<>("К-ть обстрілів");
    strafingColumn.setCellValueFactory(new PropertyValueFactory<>("strafing"));
//    strafingColumn.setPrefWidth(100);
    TableColumn<ShellingCardInTable,Integer> cannonadesColumn = new TableColumn<>("К-ть прильотів");
    cannonadesColumn.setCellValueFactory(new PropertyValueFactory<>("numbersCannonades"));
//    cannonadesColumn.setPrefWidth(75);
    TableColumn<ShellingCardInTable, String> startStrafingColumn = new TableColumn<>("Початок обстрілу");
    startStrafingColumn.setCellValueFactory(new PropertyValueFactory<>("startStrafing"));
//    strafingColumn.setPrefWidth(75);
    TableColumn<ShellingCardInTable, String> endStrafingColumn = new TableColumn<>("Кінець обстрілу");
    endStrafingColumn.setCellValueFactory(new PropertyValueFactory<>("endStrafing"));
//    endStrafingColumn.setPrefWidth(75);


    tableView.getColumns().addAll(
        dateColumn, positionColumn, weaponTypeColumn,
        strafingColumn, cannonadesColumn, startStrafingColumn, endStrafingColumn);

  }

  private void setChart(ShellingCard shellingCard) {
    // збереження в базу даних
    buildGraph(shellingCard.getDatePicker(), shellingCard.getNumbersCannonades());
  }

  private void saveShellingCardToDataBase(ShellingCard shellingCard, Connection connection) {
    String query = DatabaseQuery.insertShellingCardQuery(shellingCard.getDatePicker(),
        shellingCard.getPosition(), shellingCard.getWeaponType(),
        shellingCard.getStrafing(), shellingCard.getNumbersCannonades(),
        shellingCard.getStartStrafing(), shellingCard.getEndStrafing());
    try (Statement statement = connection.createStatement()) {
      statement.execute(query);
    } catch (Exception exception){
      exception.getStackTrace();
    }
    showTable(connection);
  }

  private void showTable(Connection connection) {
    ObservableList<ShellingCardInTable> cardList = FXCollections.observableArrayList();
    try(Statement statement = connection.createStatement()){
      String query = DatabaseQuery.getAll();
      ResultSet resultSet = statement.executeQuery(query);
      while (resultSet.next()){
        ShellingCardInTable shellingCardInTable = new ShellingCardInTable(
            resultSet.getInt("id"),
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
    tableView.setRowFactory( rowFactory ->{
      TableRow<ShellingCardInTable> selectRow = new TableRow<>();
      selectRow.setOnMouseClicked( event -> {
        if(event.getClickCount() == 1 && !selectRow.isEmpty()){
          rowIndex = tableView.getSelectionModel().getSelectedIndex();
          setTextToInputFields(rowIndex);
        }
      });
      return selectRow;
    });
    
  }

  private void setTextToInputFields(int rowIndex) {
    LocalDate localDate = LocalDate.parse(tableView.getItems().get(rowIndex).getDate());
    datePicker.setValue(localDate);
    positionInput.setText(tableView.getItems().get(rowIndex).getPosition());
    weaponTypeInput.setText(tableView.getItems().get(rowIndex).getWeaponType());
    strafingInput.setText(String.valueOf(tableView.getItems().get(rowIndex).getStrafing()));
    numbersCannonadesInput.setText(String.valueOf(tableView.getItems().get(rowIndex).getNumbersCannonades()));
    startStrafingInput.setText(tableView.getItems().get(rowIndex).getStartStrafing());
    endStrafingInput.setText(tableView.getItems().get(rowIndex).getEndStrafing());
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