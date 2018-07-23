package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import application.model.LogEvent;
import application.model.LogFile;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class MainApp extends Application {

    private static final Charset CHARSET = Charset.forName("UTF-8");
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    private ObservableList<LogEvent> eventsDisplay = FXCollections.observableArrayList();
    private ObservableList<LogFile> logFiles = FXCollections.observableArrayList();

/******************************************
 * Initialize
 ******************************************/   
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("EventViewer");

        initRootLayout();

        showEventViewer();
    }

    //window border
    public void initRootLayout() {
        try {
            // Load root layout from FXML file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    //content
    public void showEventViewer() {
        try {
            // Load viewer layout from FXML file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EventViewer.fxml"));
            AnchorPane eventViewer = (AnchorPane) loader.load();

            // Add view into the center of root layout.
            rootLayout.setCenter(eventViewer);
            
            //setup controller for the view
            EventViewController controller = loader.getController();
            controller.setMainApp(this);
            controller.setData();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    //Details Pop-up
    public void showEventDetails(LogEvent event) {
        try {
            // Load the FXML file and create a new stage for the pop-up
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EventDetails.fxml"));
            AnchorPane popup = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage popupStage = new Stage();
            popupStage.setTitle("Event Details");
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(primaryStage);
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);

            // Set the person into the controller.
            EventDetailsController controller = loader.getController();
            controller.setPopupStage(popupStage);
            controller.setEvent(event);

            // Show the dialog and wait until the user closes it
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
/******************************************
 * Getters
 ******************************************/    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public ObservableList<LogEvent> getEventData() {
        return eventsDisplay;
    }
    
    
    public ObservableList<LogFile> getOpenFiles() {
        return logFiles;
    }
    

/******************************************
 * Modify Data
 ******************************************/
    //helper function
    public int getLogIdx(String fileName) {
    	int idx = -1;
    	
    	for (int i = 0; i < logFiles.size(); i++) {
    		if (logFiles.get(i).getFileName().equals(fileName) ) {
    			idx = i;
    			break;
    		}
    	}
    	
    	return idx;
    }
    
    //load events from file
    public void loadEventsFile(File file) {
    	int idx = getLogIdx(file.getName());

    	//if file is already open
	    if (idx != -1 ) {
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setTitle("File Loading");
		    alert.setHeaderText("File with same name already loaded:\n" + file.getName());
		    alert.show();
		//else open file
	    } else {
	
	        try {
	            BufferedReader reader = Files.newBufferedReader(file.toPath(), CHARSET);
	
	            ArrayList<LogEvent> logEvents = new ArrayList<LogEvent>();
	            String line = reader.readLine();
	            
	            Boolean multiline = false;
	            String combined = "";
	            LogEvent iter = new LogEvent();
	          
	            //skip first row header
	            if (line != null) {  
	            	line = reader.readLine();
	            }
	      
	            while (line != null) {
	            	String [] str = line.split(",");
	    	
	            	//handle multi-line items
	            	if (str.length < 6) {
	            		multiline = true;
	            		combined = combined + "\n";
	            		if (str.length > 0) {
	            			combined = combined + str[0];
	            		}	            		
	            	} else {
	            		if (multiline) {
	            			multiline = false;
	            			iter.setDescription(combined);
	            		}
	            		iter = new LogEvent (str[0], str[1], str[2], str[3], str[4], str[5], file.getName());
            			combined = str[5];
	            		logEvents.add (iter);
	            	}
	            	
	            	//System.out.println(combined);  //debugging

	            	line = reader.readLine();
	            }
	            
	            //handle last row
	            if (multiline) {
        			multiline = false;
        			iter.setDescription(combined);
	            }

	            logFiles.add(new LogFile (file.getPath(), file.getName(), logEvents));
	            eventsDisplay.addAll(logEvents);
	
	            reader.close();
	
	        } catch (Exception e) {
	        	Alert alert = new Alert(AlertType.ERROR);
	        	alert.setTitle("Error");
	        	alert.setHeaderText("Could not load data from file:\n" + file.getPath());
	        	alert.show();
	        }
	      
	    }
    }
    
    public void handleRemove(LogFile log) {
    	//remove log file
    	logFiles.remove(log);

    	//remove events associated with the log file, 
    	//more efficient to re-build than to find and remove one event at a time
    	//for (event : log.getEvents()) { events.remove(event) }
    	eventsDisplay.clear();
    	for (LogFile file : logFiles) {
    		eventsDisplay.addAll(file.getEvents());
    	}
    	
    }

}