import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DatabaseGUI extends Application {
    public static TableView<CriminalRecord> criminalsTable = new TableView<CriminalRecord>();
    public static TableView<CrimeHistory> crimeHistoryTable = new TableView<CrimeHistory>();
    public static TableView<OfficerRecord> officersTable = new TableView<OfficerRecord>();
    public static TableView<OfficerHistory> officerHistoryTable = new TableView<OfficerHistory>();
    
    static TextField criminalSearchTxt = new TextField();
    static Button criminalSearchBtn = new Button("Search");
    static ObservableList<String> criminalDropOptions = FXCollections.observableArrayList();
    static ComboBox<String> criminalCombo = new ComboBox<String>(criminalDropOptions);
    static TextField officerSearchTxt = new TextField();
    static Button officerSearchBtn = new Button("Search");
    static ObservableList<String> officerDropOptions = FXCollections.observableArrayList();
    static ComboBox<String> officerCombo = new ComboBox<String>(officerDropOptions);
    static ObservableList<String> rankDropOptions = DatabaseInterface.officerRankQuery();
    static ComboBox<String> ranksCombo = new ComboBox<String>(rankDropOptions);
    static String criminalComboValue = "";
    static String officerComboValue = "";
    
    static Button refreshBtn = new Button("Refresh");
    
    static Tab criminalTab = new Tab("Criminals");
    static Tab officerTab = new Tab("Officers");
    static TabPane tabPane = new TabPane();
    
    static HBox rightSideSearch = new HBox(criminalCombo, criminalSearchTxt, criminalSearchBtn);
    static HBox leftSideControls = new HBox(refreshBtn);
    
    static  BorderPane borderPane = new BorderPane();
    static VBox mainBox = new VBox(tabPane, borderPane);

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        VBox crimeTablesPane = new VBox(new Node[]{criminalsTable, crimeHistoryTable});
        VBox officerTablesPane = new VBox(new Node[]{officersTable, officerHistoryTable});

        criminalTab.setContent(crimeTablesPane);
        officerTab.setContent(officerTablesPane);

        tabPane.getTabs().addAll(criminalTab, officerTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        DatabaseGUI.criminalTableCol();
        DatabaseGUI.crimeHistoryTableCol();
        DatabaseGUI.officerTableCol();
        DatabaseGUI.officerHistoryTableCol();

        criminalDropOptions.addAll("Name", "Crime", "ID");
        officerDropOptions.addAll("Name", "Rank", "ID");
        rankDropOptions = DatabaseInterface.officerRankQuery();

        ranksCombo.setMinWidth(149.0);

        borderPane.setRight(rightSideSearch);
        borderPane.setLeft(leftSideControls);

        criminalsTable.setItems(DatabaseInterface.allCriminalsQuery());
        officersTable.setItems(DatabaseInterface.allOfficersQuery());
        
        criminalsTable.setOnMouseClicked(e -> crimeHistoryTable.setItems(DatabaseInterface.crimeHistoryByIdQuery((int)getSelectedCriminal())));
        criminalsTable.setOnKeyReleased(e -> crimeHistoryTable.setItems(DatabaseInterface.crimeHistoryByIdQuery((int)getSelectedCriminal())));
        officersTable.setOnMouseClicked(e -> officerHistoryTable.setItems(DatabaseInterface.officerHistoryByIdQuery((int)getSelectedOfficer())));
        officersTable.setOnKeyReleased(e -> officerHistoryTable.setItems(DatabaseInterface.officerHistoryByIdQuery((int)getSelectedOfficer())));
        
        
        tabPane.setOnMouseClicked(e -> {updateTabPane(primaryStage); });
        tabPane.setOnKeyReleased(e -> {updateTabPane(primaryStage); });
        
        criminalSearchTxt.setOnKeyPressed(e->{if(e.getCode()==KeyCode.ENTER) criminalSearch();});
        officerSearchTxt.setOnKeyPressed(e->{if(e.getCode()==KeyCode.ENTER) criminalSearch();});

        criminalSearchBtn.setOnAction(e -> criminalSearch());
        criminalSearchBtn.setOnKeyPressed(e -> { if(e.getCode() == KeyCode.ENTER) criminalSearch();});
        officerSearchBtn.setOnAction(e -> officerSearch());
        officerSearchBtn.setOnKeyPressed(e -> { if(e.getCode() == KeyCode.ENTER) officerSearch();});
        
        refreshBtn.setOnAction(e->{refreshTable();});
        refreshBtn.setOnKeyPressed(e->{ if(e.getCode() == KeyCode.ENTER) refreshTable();});
        
        officerCombo.setOnHidden(e -> {setOfficerCombo();}); 
        officerCombo.setOnKeyReleased(e -> {setOfficerCombo(); });
        
        criminalCombo.setOnHidden(e -> {setCriminalCombo();});
        criminalCombo.setOnKeyReleased(e -> {setCriminalCombo();});       
        
        criminalsTable.prefHeightProperty().bind(mainBox.heightProperty().divide(1.1));
        crimeHistoryTable.prefHeightProperty().bind(mainBox.heightProperty().divide(1.5));
        
        Scene scene = new Scene(mainBox);
        primaryStage.getIcons().add(new Image("file:icon.png"));
        primaryStage.setTitle("Crime Database");
        primaryStage.setMinHeight(300.0);
        primaryStage.setWidth(949.0);
        primaryStage.setMinWidth(949.0);
        primaryStage.setHeight(450.0);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void refreshTable() {
    	Tab tab = tabPane.getSelectionModel().getSelectedItem();
    	
    	if (tab == criminalTab) {
    		criminalsTable.setItems(DatabaseInterface.allCriminalsQuery());
    	}else if (tab == officerTab) {
    		officersTable.setItems(DatabaseInterface.allOfficersQuery());
    	}
    }
    
    public static void setOfficerCombo() {
    	if (officerCombo.getValue() == null) {return;}
        
        if (((String)officerCombo.getValue()).toString() != officerComboValue) {
            officerSearchTxt.clear();
            if (((String)officerCombo.getValue()).toString() == "Rank") {
                rightSideSearch.getChildren().clear();
                rightSideSearch.getChildren().addAll(officerCombo, ranksCombo, officerSearchBtn);
            } else {
                rightSideSearch.getChildren().clear();
                rightSideSearch.getChildren().addAll(officerCombo, officerSearchTxt, officerSearchBtn);
            }
            officerComboValue = ((String)officerCombo.getValue()).toString();
        }
    }
    
    public static void setCriminalCombo() {
    	if (criminalCombo.getValue() == null) {
            return;
        }
        if (((String)criminalCombo.getValue()).toString() != criminalComboValue) {
            criminalSearchTxt.clear();
            criminalComboValue = ((String)criminalCombo.getValue()).toString();
        }
    }
    
    public static void updateTabPane(Stage primaryStage) {
    	 Tab tab3 = tabPane.getSelectionModel().getSelectedItem();
         if (tab3 == officerTab) {
         	officersTable.prefHeightProperty().bind(mainBox.heightProperty().divide(1.1));
            officerHistoryTable.prefHeightProperty().bind(mainBox.heightProperty().divide(1.5));
             if (officerCombo.getValue() == null) {
                 rightSideSearch.getChildren().clear();
                 rightSideSearch.getChildren().addAll(officerCombo, officerSearchTxt, officerSearchBtn);
             } else if (((String)officerCombo.getValue()).toString() == "Rank") {
                 rightSideSearch.getChildren().clear();
                 rightSideSearch.getChildren().addAll(officerCombo, ranksCombo, officerSearchBtn);
             } else {
                 rightSideSearch.getChildren().clear();
                 rightSideSearch.getChildren().addAll(officerCombo, officerSearchTxt, officerSearchBtn);
             }
             primaryStage.setWidth(1102);
             primaryStage.setMinWidth(1102);
         } else if (tab3 == criminalTab) {
             rightSideSearch.getChildren().clear();
             rightSideSearch.getChildren().addAll(criminalCombo, criminalSearchTxt, criminalSearchBtn);
             primaryStage.setWidth(949.0);
             primaryStage.setMinWidth(949.0);
         }
    }

    public static void officerSearch() {
        if (officerCombo.getValue() == null) {return;}
        
        String str = ((String)officerCombo.getValue()).toString();
        if (str != "Rank" && !officerSearchTxt.getText().toString().equals("")) {
            if (str == "ID") {
                try {
                    officersTable.setItems(DatabaseInterface.officerByIdQuery((int)Integer.valueOf(officerSearchTxt.getText().trim())));
                }
                catch (Exception e) {
                    DatabaseGUI.errorStage("Officer Id must be an integer");
                }
            } else if (str == "Name") {
                String[] strArr = officerSearchTxt.getText().trim().split(" ");
                try {
                    officersTable.setItems(DatabaseInterface.officersByNameQuery((String)strArr[0], (String)strArr[1]));
                }
                catch (Exception e) {
                    DatabaseGUI.errorStage("Put a space Between First & Last Name");
                }
            }
        } else if (str == "Rank") {
            try {
                officersTable.setItems(DatabaseInterface.officerByRankQuery((String)((String)ranksCombo.getValue()).toString()));
            }
            catch (Exception e) {
                DatabaseGUI.errorStage("Officer Rank Input Error");
            }
        }
    }

    public static void criminalSearch() {
        if (!criminalSearchTxt.getText().toString().equals("")) {
        	
            if (criminalCombo.getValue() == null) {return;}
            
            String str = ((String)criminalCombo.getValue()).toString();
            if (str == "ID") {
                try {
                    criminalsTable.setItems(DatabaseInterface.criminalIdQuery((int)Integer.valueOf(criminalSearchTxt.getText().trim())));
                }
                catch (Exception e) {
                    DatabaseGUI.errorStage("Criminal Id must be an integer");
                }
            } else if (str == "Name") {
                String[] strArr = criminalSearchTxt.getText().trim().split(" ");
                try {
                	
                	if(strArr.length == 2) {
                    criminalsTable.setItems(DatabaseInterface.criminalFullNameQuery((String)strArr[0], (String)strArr[1]));
                	}
                	else if (strArr.length == 1) {
                		criminalsTable.setItems(DatabaseInterface.criminalNameQuery((String)strArr[0]));
                	}
                	else {
                		 DatabaseGUI.errorStage("Invalid Name Entry");
                	}
                }
                catch (Exception e) {
                    DatabaseGUI.errorStage("Put a space Between First & Last Name");
                }
            } else if (str == "Crime") {
                try {
                    criminalsTable.setItems(DatabaseInterface.criminalsByCrimesQuery((String)criminalSearchTxt.getText().trim()));
                }
                catch (Exception e) {
                    DatabaseGUI.errorStage("Crime Type Input Error");
                }
            }
        }
    }

    public static void officerTableCol() {
        TableColumn<OfficerRecord, String>officerIdCol = new TableColumn<OfficerRecord, String>("ID");
        TableColumn<OfficerRecord, String>officerRankCol = new TableColumn<OfficerRecord, String>("Rank");
        TableColumn<OfficerRecord, String>officerFirstNameCol = new TableColumn<OfficerRecord, String>("First Name");
        TableColumn<OfficerRecord, String>officerLastNameCol = new TableColumn<OfficerRecord, String>("Last Name");
        TableColumn<OfficerRecord, String>officerAddressCol = new TableColumn<OfficerRecord, String>("Address");
        TableColumn<OfficerRecord, String>officerCityCol = new TableColumn<OfficerRecord, String>("City");
        TableColumn<OfficerRecord, String> officerStateCol = new TableColumn<OfficerRecord, String>("State");
        TableColumn<OfficerRecord, String>officerZipCol = new TableColumn<OfficerRecord, String>("Zip");
        TableColumn<OfficerRecord, String>officerPhoneCol = new TableColumn<OfficerRecord, String>("Phone #");
        TableColumn<OfficerRecord, String>officerHeightCol = new TableColumn<OfficerRecord, String>("Height");
        TableColumn<OfficerRecord, String>officerWeightCol = new TableColumn<OfficerRecord, String>("Weight");
        TableColumn<OfficerRecord, String>officerEyesCol = new TableColumn<OfficerRecord, String>("Eyes");
        TableColumn<OfficerRecord, String>officerHairCol = new TableColumn<OfficerRecord, String>("Hair");
        TableColumn<OfficerRecord, String>officerGenderCol = new TableColumn<OfficerRecord, String>("Gender");
        TableColumn<OfficerRecord, String>officerBirthDateCol = new TableColumn<OfficerRecord, String>("Birth Date");
        TableColumn<OfficerRecord, String>officerHireDateCol = new TableColumn<OfficerRecord, String>("Hire Date");
        officerIdCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("id"));
        officerRankCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("rank"));
        officerFirstNameCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("firstName"));
        officerLastNameCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("lastName"));
        officerAddressCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("address"));
        officerCityCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("city"));
        officerStateCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("state"));
        officerZipCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("zip"));
        officerPhoneCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("phone"));
        officerHeightCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("height"));
        officerWeightCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("weight"));
        officerEyesCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("eyes"));
        officerHairCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("hair"));
        officerGenderCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("sex"));
        officerBirthDateCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("birthDate"));
        officerHireDateCol.setCellValueFactory(new PropertyValueFactory<OfficerRecord,String>("hireDate"));
        officersTable.getColumns().add(officerIdCol);
        officersTable.getColumns().add(officerRankCol);
        officersTable.getColumns().add(officerFirstNameCol);
        officersTable.getColumns().add(officerLastNameCol);
        officersTable.getColumns().add(officerAddressCol);
        officersTable.getColumns().add(officerCityCol);
        officersTable.getColumns().add(officerStateCol);
        officersTable.getColumns().add( officerZipCol);
        officersTable.getColumns().add( officerPhoneCol);
        officersTable.getColumns().add( officerHeightCol);
        officersTable.getColumns().add( officerWeightCol);
        officersTable.getColumns().add( officerEyesCol);
        officersTable.getColumns().add( officerHairCol);
        officersTable.getColumns().add( officerGenderCol);
        officersTable.getColumns().add( officerBirthDateCol);
        officersTable.getColumns().add( officerHireDateCol);
    }

    public static void criminalTableCol() {
        TableColumn<CriminalRecord,String> criminalIdCol = new TableColumn<CriminalRecord,String>("ID");
        TableColumn<CriminalRecord,String> criminalFirstNameCol = new TableColumn<CriminalRecord,String>("First Name");
        TableColumn<CriminalRecord,String> criminalLastNameCol = new TableColumn<CriminalRecord,String>("Last Name");
        TableColumn<CriminalRecord,String> criminalAddressCol = new TableColumn<CriminalRecord,String>("Address");
        TableColumn<CriminalRecord,String> criminalCityCol = new TableColumn<CriminalRecord,String>("City");
        TableColumn<CriminalRecord,String> criminalStateCol = new TableColumn<CriminalRecord,String>("State");
        TableColumn<CriminalRecord,String> criminalZipCol = new TableColumn<CriminalRecord,String>("Zip");
        TableColumn<CriminalRecord,String> criminalPhoneCol = new TableColumn<CriminalRecord,String>("Phone #");
        TableColumn<CriminalRecord,String> criminalHeightCol = new TableColumn<CriminalRecord,String>("Height");
        TableColumn<CriminalRecord,String> criminalWeightCol = new TableColumn<CriminalRecord,String>("Weight");
        TableColumn<CriminalRecord,String> criminalEyesCol = new TableColumn<CriminalRecord,String>("Eyes");
        TableColumn<CriminalRecord,String> criminalHairCol = new TableColumn<CriminalRecord,String>("Hair");
        TableColumn<CriminalRecord,String> criminalGenderCol = new TableColumn<CriminalRecord,String>("Gender");
        TableColumn<CriminalRecord,String> criminalBirthDateCol = new TableColumn<CriminalRecord,String>("Birth Date");
        criminalIdCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("id"));
        criminalFirstNameCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("firstName"));
        criminalLastNameCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("lastName"));
        criminalAddressCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("address"));
        criminalCityCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("city"));
        criminalStateCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("state"));
        criminalZipCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("zip"));
        criminalPhoneCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("phone"));
        criminalHeightCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("height"));
        criminalWeightCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("weight"));
        criminalEyesCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("eyes"));
        criminalHairCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("hair"));
        criminalGenderCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("sex"));
        criminalBirthDateCol.setCellValueFactory(new PropertyValueFactory<CriminalRecord,String>("birthDate"));
        criminalsTable.getColumns().add( criminalIdCol);
        criminalsTable.getColumns().add( criminalFirstNameCol);
        criminalsTable.getColumns().add( criminalLastNameCol);
        criminalsTable.getColumns().add( criminalAddressCol);
        criminalsTable.getColumns().add( criminalCityCol);
        criminalsTable.getColumns().add( criminalStateCol);
        criminalsTable.getColumns().add( criminalZipCol);
        criminalsTable.getColumns().add( criminalPhoneCol);
        criminalsTable.getColumns().add( criminalHeightCol);
        criminalsTable.getColumns().add( criminalWeightCol);
        criminalsTable.getColumns().add( criminalEyesCol);
        criminalsTable.getColumns().add( criminalHairCol);
        criminalsTable.getColumns().add( criminalGenderCol);
        criminalsTable.getColumns().add( criminalBirthDateCol);
    }

    public static void officerHistoryTableCol() {
        TableColumn<OfficerHistory,String> arrestedPersonCol = new TableColumn<OfficerHistory,String>("Individual Arrested");
        TableColumn<OfficerHistory,String> crimeCol = new TableColumn<OfficerHistory,String>("Crime");
        TableColumn<OfficerHistory,String> severityCol = new TableColumn<OfficerHistory,String>("Severity");
        TableColumn<OfficerHistory,String> addressCol = new TableColumn<OfficerHistory,String>("Address");
        TableColumn<OfficerHistory,String> cityCol = new TableColumn<OfficerHistory,String>("City");
        TableColumn<OfficerHistory,String> stateCol = new TableColumn<OfficerHistory,String>("State");
        TableColumn<OfficerHistory,String> crimeDateCol = new TableColumn<OfficerHistory,String>("Date");
        TableColumn<OfficerHistory,String> descriptionCol = new TableColumn<OfficerHistory,String>("Description");
        arrestedPersonCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("arrestedPerson"));
        crimeCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("crime"));
        severityCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("severity"));
        addressCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("address"));
        cityCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("city"));
        stateCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("state"));
        crimeDateCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("date"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<OfficerHistory,String>("description"));
        crimeDateCol.setMinWidth(45);
        arrestedPersonCol.setMinWidth(125.0);
        descriptionCol.setMinWidth(355.0);
        severityCol.setMinWidth(90.0);
        stateCol.setMaxWidth(50.0);
        addressCol.setMinWidth(135.0);
        cityCol.setMinWidth(110);
        officerHistoryTable.getColumns().add( arrestedPersonCol);
        officerHistoryTable.getColumns().add( crimeCol);
        officerHistoryTable.getColumns().add( severityCol);
        officerHistoryTable.getColumns().add( addressCol);
        officerHistoryTable.getColumns().add( cityCol);
        officerHistoryTable.getColumns().add( stateCol);
        officerHistoryTable.getColumns().add( crimeDateCol);
        officerHistoryTable.getColumns().add( descriptionCol);
    }

    public static void crimeHistoryTableCol() {
        TableColumn<CrimeHistory,String> crimeCol = new TableColumn<CrimeHistory,String>("Crime");
        TableColumn<CrimeHistory,String> severityCol = new TableColumn<CrimeHistory,String>("Severity");
        TableColumn<CrimeHistory,String> crimeDateCol = new TableColumn<CrimeHistory,String>("Date");
        TableColumn<CrimeHistory,String> arrestingOfficerCol = new TableColumn<CrimeHistory,String>("Arresting Officer");
        TableColumn<CrimeHistory,String> addressCol = new TableColumn<CrimeHistory,String>("Address");
        TableColumn<CrimeHistory,String> cityCol = new TableColumn<CrimeHistory,String>("City");
        TableColumn<CrimeHistory,String> stateCol = new TableColumn<CrimeHistory,String>("State");
        TableColumn<CrimeHistory,String> descriptionCol = new TableColumn<CrimeHistory,String>("Description");
        crimeCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("crime"));
        severityCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("severity"));
        crimeDateCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("date"));
        arrestingOfficerCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("officer"));
        addressCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("address"));
        cityCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("city"));
        stateCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("state"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<CrimeHistory,String>("description"));
        crimeDateCol.setMinWidth(40);
        arrestingOfficerCol.setMinWidth(110.0);
        descriptionCol.setMinWidth(335.0);
        stateCol.setMaxWidth(45.0);
        addressCol.setMinWidth(100.0);
        severityCol.setMinWidth(60);
        cityCol.setMinWidth(90);
        crimeHistoryTable.getColumns().add(crimeCol);
        crimeHistoryTable.getColumns().add(severityCol);
        crimeHistoryTable.getColumns().add(addressCol);
        crimeHistoryTable.getColumns().add(cityCol);
        crimeHistoryTable.getColumns().add(stateCol);
        crimeHistoryTable.getColumns().add(crimeDateCol);
        crimeHistoryTable.getColumns().add(arrestingOfficerCol);
        crimeHistoryTable.getColumns().add(descriptionCol);
    }

    public static int getSelectedOfficer() {
        try {
            OfficerRecord selected = (OfficerRecord)officersTable.getSelectionModel().getSelectedItem();
            int id = selected.id;
            return id;
        }
        catch (NullPointerException selected) {
            return 0;
        }
    }

    public static int getSelectedCriminal() {
        try {
            CriminalRecord selected = (CriminalRecord)criminalsTable.getSelectionModel().getSelectedItem();
            int id = selected.id;
            return id;
        }
        catch (NullPointerException selected) {
            return 0;
        }
    }

    public static void errorStage(String message) {
        Label errorLabel = new Label(message);
        StackPane errorPane = new StackPane(new Node[]{errorLabel});
        Scene errorScene = new Scene((Parent)errorPane);
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");
        errorStage.setMinHeight(100.0);
        errorStage.setMinWidth(200.0);
        errorStage.setWidth(290.0);
        errorStage.setHeight(100.0);
        errorStage.setScene(errorScene);
        errorStage.show();
    }
}