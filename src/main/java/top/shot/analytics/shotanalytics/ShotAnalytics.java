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
import javafx.scene.Scene;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import top.shot.analytics.shotanalytics.db_query.DatabaseQuery;
import top.shot.analytics.shotanalytics.jdbc_manager.DatabaseConnection;
import top.shot.analytics.shotanalytics.model_dto.ShellingCard;
import top.shot.analytics.shotanalytics.model_dto.ShellingCardInAnalytics;
import top.shot.analytics.shotanalytics.model_dto.ShellingCardInTable;


public class ShotAnalytics extends Application {

  private Button saveButton, updateButton, deleteButton,
      onePositionDayAnalyticsButton, analyticsForSelectedDayPositionButton,
          allPositionAnalyticsForDayButton, analyticsForSelectedDaysForAllButton;
  private Label datePickerLabel, strafing, numbersCannonades,
          startStrafing, endStrafing, positionLabel,
          weaponTypeLabel, positionAnalyticsLabel, firstDayLabel, lastDayLabel;
  private DatePicker datePicker, selectFirstDay, selectLastDay;
  private TextField strafingInput, numbersCannonadesInput,
          startStrafingInput, endStrafingInput, positionInput,
          weaponTypeInput, analyticsPosition;
  private TableView<ShellingCardInTable> shellingCardsTable;
  private TableView<ShellingCardInAnalytics> analyticTable;
  private String startStrafingData;
  private int id, rowIndex;

  @Override
  public void start(Stage primaryStage) {
//     set inputs fields and labels
    setInputFields();
    setLabels();

//    DatabaseConnection databaseConnection = new DatabaseConnection();
    Connection connection = DatabaseConnection.getDatabaseConnection();
//    DatabaseQuery.createShotAnalyticsDatabase(connection);
    createDataBase(connection);
    setupDatabaseOperation(connection);
    setupAnalytics(connection);
//      set element on the scene
    setScene(primaryStage, connection);
  }


  private void setLabels() {
    datePickerLabel = new Label();
    datePickerLabel.setText("Дата обстрілу");
    positionLabel = new Label();
    positionLabel.setText("Позиція");
    firstDayLabel = new Label("Перший день");
    lastDayLabel = new Label("Останній день");
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
    onePositionDayAnalyticsButton = new Button("аналітика за 1 день");
    analyticsForSelectedDayPositionButton = new Button("аналітика за вибрані дні");
    analyticsForSelectedDaysForAllButton = new Button("аналітика  для всіх за вибрані дні");
    allPositionAnalyticsForDayButton = new Button("аналітика для всіх за 1 день");

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
    selectFirstDay = new DatePicker();
    selectLastDay = new DatePicker();
    analyticsPosition = new TextField();
  }

  private void setupAnalytics(Connection connection) {
    createAnalyticsForPositionPerDay(connection);
    createAnalyticsForAllPositionsPerDay(connection);
    createAnalyticsForSelectedDaysForOnePosition(connection);
    createAnalyticsForSelectedDaysForAllPositions(connection);
  }

  private void createAnalyticsForPositionPerDay(Connection connection) {
    ObservableList<ShellingCardInAnalytics> cardList = FXCollections.observableArrayList();
    onePositionDayAnalyticsButton.setOnAction( event -> {
      String date = selectFirstDay.getValue().toString();
      String position = analyticsPosition.getText();
      cardList.clear();
      String query = DatabaseQuery.getQueryAnalyticPerDayForOnePosition(date, position);
      try(Statement statement = connection.createStatement()){
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
          ShellingCardInAnalytics shellingCardInAnalytics = new ShellingCardInAnalytics(
              resultSet.getString("date_picker"),
              resultSet.getString("position"),
              resultSet.getInt("strafing"),
              resultSet.getInt("numbersCannonades"),
              resultSet.getString("strafingTime")
          );
          cardList.add(shellingCardInAnalytics);
        }
      } catch (SQLException exception){
        System.out.println("ERROR createAnalyticsForPositionPerDay");
        exception.getStackTrace();
        System.out.println(exception.getMessage());
      }
      analyticTable.setItems(cardList);
    });
  }

  private void createAnalyticsForAllPositionsPerDay(Connection connection) {
    ObservableList<ShellingCardInAnalytics> cardList = FXCollections.observableArrayList();
    allPositionAnalyticsForDayButton.setOnAction( event -> {
      String date = selectFirstDay.getValue().toString();
      cardList.clear();
      String query = DatabaseQuery.getQueryAnalyticPerDaysForALLPositions(date);
      try(Statement statement = connection.createStatement()){
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
          ShellingCardInAnalytics shellingCardInAnalytics = new ShellingCardInAnalytics(
              resultSet.getString("date_picker"),
              resultSet.getString("position"),
              resultSet.getInt("strafing"),
              resultSet.getInt("numbersCannonades"),
              resultSet.getString("strafingTime")
          );
          cardList.add(shellingCardInAnalytics);
        }
      } catch (SQLException exception){
        System.out.println("ERROR");
        exception.getStackTrace();
      }
      analyticTable.setItems(cardList);
    });
  }

  private void createAnalyticsForSelectedDaysForOnePosition(Connection connection) {
    ObservableList<ShellingCardInAnalytics> cardList = FXCollections.observableArrayList();
    analyticsForSelectedDayPositionButton.setOnAction( event -> {
      String firstDay = selectFirstDay.getValue().toString();
      String lastDay = selectLastDay.getValue().toString();
      String position = analyticsPosition.getText();
      cardList.clear();
      String query = DatabaseQuery.getQueryAnalyticBetweenDaysForOnePosition(firstDay, lastDay, position);
      try(Statement statement = connection.createStatement()){
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
          ShellingCardInAnalytics shellingCardInAnalytics = new ShellingCardInAnalytics(
              resultSet.getString("date_picker"),
              resultSet.getString("position"),
              resultSet.getInt("strafing"),
              resultSet.getInt("numbersCannonades"),
              resultSet.getString("strafingTime")
          );
          cardList.add(shellingCardInAnalytics);
        }
      } catch (SQLException exception){
        System.out.println("ERROR  createAnalyticsForSelectedDaysForOnePosition");
        System.out.println(exception.getMessage());
      }
      analyticTable.setItems(cardList);
    });
  }

  private void createAnalyticsForSelectedDaysForAllPositions(Connection connection) {
    ObservableList<ShellingCardInAnalytics> cardList = FXCollections.observableArrayList();
    analyticsForSelectedDaysForAllButton.setOnAction( event -> {
      String firstDay = selectFirstDay.getValue().toString();
      String lastDay = selectLastDay.getValue().toString();
      cardList.clear();
      String query = DatabaseQuery.getQueryAnalyticBetweenDaysForAllPositions(firstDay, lastDay);
      try(Statement statement = connection.createStatement()){
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
          ShellingCardInAnalytics shellingCardInAnalytics = new ShellingCardInAnalytics(
              resultSet.getString("date_picker"),
              resultSet.getString("position"),
              resultSet.getInt("strafing"),
              resultSet.getInt("numbersCannonades"),
              resultSet.getString("strafingTime")
          );
          cardList.add(shellingCardInAnalytics);
        }
      } catch (SQLException exception){
        System.out.println("ERROR   createAnalyticsForSelectedDaysForAllPositions");
        System.out.println(exception.getMessage());
      }
      analyticTable.setItems(cardList);
    });
  }

  private void setupDatabaseOperation(Connection connection) {
    saveShellingCard(connection);
    updateShellingCard(connection);
    deleteShellingCard(connection);
  }

  private void createDataBase(Connection connection) {
    String query = DatabaseQuery.createShotAnalyticsTable();
    try(var statement = connection.createStatement()){
      statement.executeUpdate(query);
    }catch (SQLException exception){
      System.out.println("ERROR to create table with query");
      exception.getStackTrace();
    }
  }

  private void saveShellingCard(Connection connection) {
    saveButton.setOnAction(event -> {
      var shellingCard = createShellingCard();
      saveShellingCardToDataBase(shellingCard, connection);
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
      rowIndex = shellingCardsTable.getSelectionModel().getSelectedIndex();
      id = Integer.parseInt(String.valueOf(shellingCardsTable.getItems().get(rowIndex).getId()));
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
      rowIndex = shellingCardsTable.getSelectionModel().getSelectedIndex();
      id = Integer.parseInt(String.valueOf(shellingCardsTable.getItems().get(rowIndex).getId()));
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
    Label shellingCardsTableLabel = new Label("Картки обстрілу");
    shellingCardsTableLabel.setPadding(new Insets(10,0,0, 10));
    shellingCardsTable = new TableView<>();
    shellingCardsTable.setPadding(new Insets(10,10,10,10));
    setTableColumns(shellingCardsTable);
    showTable(connection);
    // create horizontal box for 3 buttons
    HBox buttonsBox = new HBox(20);
    buttonsBox.setPadding(new Insets(0, 0, 5, 0));
    buttonsBox.getChildren().addAll(saveButton, updateButton, deleteButton);
    Label analyticTableLabel = new Label("Аналітика обстрілів");
    analyticTableLabel.setPadding(new Insets(0,0,0, 10));
    analyticTable = new TableView<>();
    analyticTable.setPadding(new Insets(5,5,5,5));
    setColumnsInAnalyticsTable(analyticTable);
    VBox tablesBox = new VBox(10);
    tablesBox.getChildren().addAll(shellingCardsTableLabel, shellingCardsTable,
                                  analyticTableLabel, analyticTable);
    VBox inputBox = new VBox(5);
    inputBox.setPadding(new Insets(10,10,10,10));

    VBox firstDayBox = new VBox(5);
    firstDayBox.getChildren().addAll(firstDayLabel, selectFirstDay);
    VBox lastDayBox = new VBox(5);
    lastDayBox.getChildren().addAll(lastDayLabel, selectLastDay);

    VBox inputPositionBox = new VBox(5);
    inputPositionBox.getChildren().addAll(positionAnalyticsLabel, analyticsPosition);

    HBox analyticsInputBox = new HBox(5);
    analyticsInputBox.getChildren().addAll(firstDayBox, lastDayBox, inputPositionBox);

    Line line = new Line(0, 0, 670, 0); // Горизонтальна лінія
    line.setStroke(Color.GREY);
    Line lineBetweenInputBoxAndButtons = new Line(0, 0, 670, 0); // Горизонтальна лінія
    lineBetweenInputBoxAndButtons.setStroke(Color.GREY);
    Label buttonsOperations = new Label("Операції з карткою обстрілів:");


    Rectangle rectangleOnePosition = new Rectangle(220, 125); // Прямокутна рамка
    rectangleOnePosition.setStroke(Color.GREY);
    rectangleOnePosition.setFill(Color.TRANSPARENT);
    VBox onePositionsButtonsBox = new VBox(10);
    onePositionsButtonsBox.setPadding(new Insets(10, 10, 10,10));
    Label onePositionLabel = new Label(
        "Виберіть день, або проміжок днів, \nта вкажи позицію для розрахунку:");
    onePositionsButtonsBox.getChildren().addAll( onePositionLabel,
        onePositionDayAnalyticsButton, analyticsForSelectedDayPositionButton);
    StackPane onePositionBox = new StackPane();
    onePositionBox.getChildren().addAll(
        rectangleOnePosition, onePositionsButtonsBox);

    Rectangle rectangleAllPositions = new Rectangle(220, 125);
    rectangleAllPositions.setStroke(Color.GREY);
    rectangleAllPositions.setFill(Color.TRANSPARENT);
    VBox allPositionsButtonBox = new VBox(10);
    allPositionsButtonBox.setPadding(new Insets(10,10,10,10));
    Label allPositionLabel = new Label(
        "Виберіть день, або проміжок днів, \n для розрахунку:");
    allPositionsButtonBox.getChildren().addAll(allPositionLabel,
        allPositionAnalyticsForDayButton, analyticsForSelectedDaysForAllButton);
    StackPane allPositionBox = new StackPane();
    allPositionBox.getChildren().addAll(rectangleAllPositions, allPositionsButtonBox);

    HBox analyticsButtonsBox = new HBox(5);
    analyticsButtonsBox.getChildren().addAll(onePositionBox, allPositionBox);
    Label analyticsLabel = new Label("Побудова аналітики");
    inputBox.getChildren().addAll(datePickerLabel, datePicker, positionLabel,
        positionInput, weaponTypeLabel, weaponTypeInput,
        strafing, strafingInput, numbersCannonades,
        numbersCannonadesInput, startStrafing, startStrafingInput,
        endStrafing, endStrafingInput, lineBetweenInputBoxAndButtons, buttonsOperations,
        buttonsBox, line, analyticsLabel, analyticsInputBox, analyticsButtonsBox);


    SplitPane sceneElements = new SplitPane();
    sceneElements.setDividerPositions(0.49);
    sceneElements.getItems().addAll(inputBox, tablesBox);


    Scene scene = new Scene(sceneElements, 1400, 700); // Встановіть ширину і висоту сцени за потребою
    primaryStage.setScene(scene);
    primaryStage.show();

  }

  private void setColumnsInAnalyticsTable(TableView<ShellingCardInAnalytics> analyticTable) {
    TableColumn<ShellingCardInAnalytics, String> dateColumn = new TableColumn<>("Дата");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    dateColumn.setPrefWidth(100);
    TableColumn<ShellingCardInAnalytics, String> positionColumn = new TableColumn<>("Позиція");
    positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
    positionColumn.setPrefWidth(100);
//    TableColumn<ShellingCardInTable, String> weaponTypeColumn = new TableColumn<>("Зброя");
//    weaponTypeColumn.setCellValueFactory(new PropertyValueFactory<>("weaponType"));
//    weaponTypeColumn.setPrefWidth(110);
    TableColumn<ShellingCardInAnalytics,Integer> strafingColumn = new TableColumn<>("К-ть обстрілів");
    strafingColumn.setCellValueFactory(new PropertyValueFactory<>("strafing"));
    strafingColumn.setPrefWidth(120);
    TableColumn<ShellingCardInAnalytics,Integer> cannonadesColumn = new TableColumn<>("К-ть прильотів");
    cannonadesColumn.setCellValueFactory(new PropertyValueFactory<>("numbersCannonades"));
    cannonadesColumn.setPrefWidth(120);
    TableColumn<ShellingCardInAnalytics, String> strafingTimeColumn = new TableColumn<>("Час обстрілу");
    strafingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("strafingTime"));
    strafingTimeColumn.setPrefWidth(100);
    analyticTable.getColumns().addAll(dateColumn, positionColumn, strafingColumn, cannonadesColumn, strafingTimeColumn);
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

  private void saveShellingCardToDataBase(ShellingCard shellingCard, Connection connection) {
    String query = DatabaseQuery.insertShellingCardQuery(shellingCard.getDatePicker(),
        shellingCard.getPosition(), shellingCard.getWeaponType(),
        shellingCard.getStrafing(), shellingCard.getNumbersCannonades(),
        shellingCard.getStartStrafing(), shellingCard.getEndStrafing());
    try (Statement statement = connection.createStatement()) {
      statement.execute(query);
    } catch (Exception exception){
      System.out.println("Error INSERT INTO");
      System.out.println(exception.getMessage());
      exception.getStackTrace();
    }
    showTable(connection);
  }

  private void showTable(Connection connection) {
    ObservableList<ShellingCardInTable> cardList = FXCollections.observableArrayList();
    try(Statement statement = connection.createStatement()){
      String query = DatabaseQuery.getAllShellingCards();
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
      System.out.println("Error show table");
      exception.getStackTrace();
    }
    shellingCardsTable.setItems(cardList);
    shellingCardsTable.setRowFactory( rowFactory ->{
      TableRow<ShellingCardInTable> selectRow = new TableRow<>();
      selectRow.setOnMouseClicked( event -> {
        if(event.getClickCount() == 1 && !selectRow.isEmpty()){
          rowIndex = shellingCardsTable.getSelectionModel().getSelectedIndex();
          setTextToInputFields(rowIndex);
        }
      });
      return selectRow;
    });
    
  }

  private void setTextToInputFields(int rowIndex) {
    LocalDate localDate = LocalDate.parse(shellingCardsTable.getItems().get(rowIndex).getDate());
    datePicker.setValue(localDate);
    positionInput.setText(shellingCardsTable.getItems().get(rowIndex).getPosition());
    weaponTypeInput.setText(shellingCardsTable.getItems().get(rowIndex).getWeaponType());
    strafingInput.setText(String.valueOf(shellingCardsTable.getItems().get(rowIndex).getStrafing()));
    numbersCannonadesInput.setText(String.valueOf(shellingCardsTable.getItems().get(rowIndex).getNumbersCannonades()));
    startStrafingInput.setText(shellingCardsTable.getItems().get(rowIndex).getStartStrafing());
    endStrafingInput.setText(shellingCardsTable.getItems().get(rowIndex).getEndStrafing());
  }

  public static void main(String[] args) {
    launch(args);
  }
}