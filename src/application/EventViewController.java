package application;

import java.io.File;

import application.model.LogEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EventViewController {
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
	private TableView<String> logFileTable;
	
    @FXML
    private TableColumn<String, String> logFileCol;  
    
    private MainApp mainApp;
    
    public EventViewController() {
    }
    
    @FXML
    private void initialize() {
    	levelCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("level"));
    	displayDateCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("displayDate"));
    	sourceCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("source"));
    	eventIdCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("id"));
    	categoryCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("category"));
    	descCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("desc"));
    	fileNameCol.setCellValueFactory(new PropertyValueFactory<LogEvent, String>("fileName"));
    	
    	logFileCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
    	
    	searchOptionBox.getItems().addAll(
    			"File",
    			"Level",
    			"Source",
    			"Event ID",
    			"Task Category",
    			"Description"
    	);
    	
    	searchOptionBox.setValue("Description");
    	
    	
    }
    
    private boolean stringCompareHelper (String filter, LogEvent logevent) {
        String option = searchOptionBox.getValue();
        
        String compare = "";

        if (option.equals("File")) {
        	compare = logevent.getFileName();
        } else if (option.equals("Level")) {
        	compare = logevent.getLevel();
        } else if (option.equals("Source")) {
        	compare = logevent.getSource();
        } else if (option.equals("Event ID")) {
        	compare = logevent.getId();
        } else if (option.equals("Task Category")) {
        	compare = logevent.getCategory();
        } else {
        	compare = logevent.getDesc();
        }
        
        if (compare != null && compare.toLowerCase().contains(filter)) {
            return true;
        }
        
        return false; // Does not match.
    }
    
    public void searchOptionSelected() {
    	//reset search
    	filterField.setText("");
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
    	// 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<LogEvent> filteredData = new FilteredList<>(mainApp.getEventData(), p -> true);
        
        // 2. Set the filter Predicate whenever the filter changes.
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

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<LogEvent> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(eventTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        eventTable.setItems(sortedData);
        logFileTable.setItems(mainApp.getOpenFiles());
        
        eventTable.setRowFactory( tv -> {
            TableRow<LogEvent> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	LogEvent rowData = row.getItem();
                    System.out.println(rowData.getDesc());
                }
            });
            return row ;
        });
    }

    @FXML
    public void handleAdd() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Csv files (*.csv)", "*.csv");
        chooser.getExtensionFilters().add(extFilter);
        
        File file = chooser.showOpenDialog(new Stage());
    	mainApp.loadEventsFile(file);
    }
    
    @FXML
    public void handleRemove() {
    	String selectedItem = logFileTable.getSelectionModel().getSelectedItem();
    	mainApp.handleRemove(selectedItem);
        logFileTable.getItems().remove(selectedItem);
    }
}
