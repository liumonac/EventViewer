package application;

import java.io.File;

import application.model.LogEvent;
import application.model.LogFile;
import application.util.SearchOption;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EventViewController {
    private MainApp mainApp;
    
	@FXML
    private TextField filterField;
	@FXML
	private ComboBox<String> searchOptionBox;
	
	@FXML
	private TableView<LogEvent> eventTable;
    @FXML
    private TableColumn<LogEvent, String> levelCol;
    @FXML
    private TableColumn<LogEvent, String> displayDateCol;
    @FXML
    private TableColumn<LogEvent, String> sourceCol;
    @FXML
    private TableColumn<LogEvent, String> eventIdCol;
    @FXML
    private TableColumn<LogEvent, String> categoryCol;
    @FXML
    private TableColumn<LogEvent, String> descCol;    
    @FXML
    private TableColumn<LogEvent, String> fileNameCol;  
    
	@FXML
	private TableView<LogFile> logFileTable;
    @FXML
    private TableColumn<LogFile, String> logFileCol;  
    
/******************************************
 * Constructors
 ******************************************/
    public EventViewController() {
    }
    
    
/******************************************
 * initialize views and set data
 ******************************************/
    @FXML
    private void initialize() {
    	//setup event Table display
    	levelCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("level"));
    	displayDateCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("displayDate"));
    	sourceCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("source"));
    	eventIdCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("id"));
    	categoryCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("category"));
    	descCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("desc"));
    	fileNameCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("fileName"));
    	
    	//add double click listener for opening details
        eventTable.setRowFactory( tv -> {
            TableRow<LogEvent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	LogEvent rowData = row.getItem();
                	mainApp.showEventDetails (rowData);
                }
            });
            return row ;
        });
        
    	//setup log file table display
    	logFileCol.setCellValueFactory(new PropertyValueFactory<LogFile, String>("fileName"));
    	
    	//setup search
    	searchOptionBox.getItems().addAll(
    			SearchOption.FILE.getValue(),
    			SearchOption.LEVEL.getValue(),
    			SearchOption.SOURCE.getValue(),
    			SearchOption.EVENTID.getValue(),
    			SearchOption.CATEGORY.getValue(),
    			SearchOption.DESC.getValue()
    	);
    	searchOptionBox.setValue(SearchOption.DESC.getValue());

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    //helper for setData
    private boolean stringCompareHelper (String filter, LogEvent logevent) {
        String option = searchOptionBox.getValue();
        
        String compare = "";

        if (SearchOption.FILE.isEqualTo(option)) {
        	compare = logevent.getFileName();
        } else if (SearchOption.LEVEL.isEqualTo(option)) {
        	compare = logevent.getLevel();
        } else if (SearchOption.SOURCE.isEqualTo(option)) {
        	compare = logevent.getSource();
        } else if (SearchOption.EVENTID.isEqualTo(option)) {
        	compare = logevent.getId();
        } else if (SearchOption.CATEGORY.isEqualTo(option)) {
        	compare = logevent.getCategory();
        } else {
        	compare = logevent.getDesc();
        }
        
        if (compare != null && compare.toLowerCase().contains(filter)) {
            return true;
        }
        
        return false; // Does not match.
    }

    public void setData () {
    	//Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<LogEvent> filteredData = new FilteredList<>(mainApp.getEventData(), p -> true);
        
        //Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(logevent -> {
                // If filter text is empty, display all entries
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                
                return stringCompareHelper (lowerCaseFilter, logevent);

            });
        });

        // Filtered List cannot be modified, so wrap in SortedList to retain ObservableList sorting 
        SortedList<LogEvent> sortedData = new SortedList<>(filteredData);

        //Bind the SortedList comparator to the TableView comparator.
        //Clicking header of original TableView will now also trigger sort of the SortedList
        sortedData.comparatorProperty().bind(eventTable.comparatorProperty());

        //Add data to the tables.
        eventTable.setItems(sortedData);
        logFileTable.setItems(mainApp.getOpenFiles());
        
    }
    
    
/******************************************
 * functions linked to UI actions
 ******************************************/    
    @FXML
    public void handleAdd() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        
        //only use CSV files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Csv files (*.csv)", "*.csv");
        chooser.getExtensionFilters().add(extFilter);
        //start in current directory
        chooser.setInitialDirectory(new File("."));
        
        File file = chooser.showOpenDialog(new Stage());
        
    	//cancel button clicked
    	if (file == null) {
    		return;
    	}
    	
    	mainApp.loadEventsFile(file);
    }
    
    @FXML
    public void handleRemove() {
    	LogFile selectedItem = logFileTable.getSelectionModel().getSelectedItem();
    	
    	//remove button clicked with no selection
    	if (selectedItem == null) {
        	Alert alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Error");
        	alert.setHeaderText("Select a LogFile to remove");
        	alert.show();
    	} else {
        	int idx = logFileTable.getSelectionModel().getSelectedIndex();
        	System.out.println (idx);
        	mainApp.handleRemove(selectedItem);
            logFileTable.getItems().remove(selectedItem);
    	}
    }
    
    public void searchOptionSelected() {
    	//change text to trigger re-evaluation of filtering of data
    	String s = filterField.getText();
    	filterField.setText("");
    	filterField.setText(s);
    }
}
