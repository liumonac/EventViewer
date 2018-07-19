package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class MainApp extends Application {

    private static final Charset CHARSET = Charset.forName("UTF-8");
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    
    private ObservableList<LogEvent> events = FXCollections.observableArrayList();
    private ObservableList<String> logFilesDisplay = FXCollections.observableArrayList();
    
    private HashMap<String, LogFile> logFiles = new HashMap<String, LogFile>();

    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("EventViewer");

        initRootLayout();

        showEventViewer();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
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

    /**
     * Shows the person overview inside the root layout.
     */
    public void showEventViewer() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EventViewer.fxml"));
            AnchorPane eventViewer = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(eventViewer);
            
            EventViewController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //getters
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public ObservableList<LogEvent> getEventData() {
        return events;
    }
    
    
    public ObservableList<String> getOpenFiles() {
        return logFilesDisplay;
    }
    

    public static void main(String[] args) {
        launch(args);
    }
    
    //file helper
    public void loadEventsFile(File file) {
    	if (file == null) {
    		return;
    	}

	    if (logFiles.get(file.getPath()) != null) {
		    Alert alert = new Alert(AlertType.INFORMATION);
		    alert.setTitle("Error");
		    alert.setHeaderText("File already loaded:\n" + file.getPath());
		    alert.show();
	    } else {
	
	        try {
	            BufferedReader reader = Files.newBufferedReader(file.toPath(), CHARSET);
	
	            ArrayList<LogEvent> logEvents = new ArrayList<LogEvent>();
	            String line = reader.readLine();
	            
	            Boolean multiline = false;
	            String combined = "";
	            LogEvent iter = new LogEvent();
	          
	            if (line != null) {  //skip first row header
	            	line = reader.readLine();
	            }
	      
	            while (line != null) {
	            	String [] str = line.split(",");
	    	
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

	            	line = reader.readLine();
	            }
	            
	            //handle last row
	            if (multiline) {
        			multiline = false;
        			iter.setDescription(combined);
	            }
	      
	            LogFile log = new LogFile (file.getPath(), file.getName(), logEvents);
	            logFiles.put(file.getPath(), log);
	            logFilesDisplay.add (file.getPath());
	            events.addAll(logEvents);
	
	            reader.close();
	
	        } catch (Exception e) { // catches ANY exception
	        	Alert alert = new Alert(AlertType.INFORMATION);
	        	alert.setTitle("Error");
	        	alert.setHeaderText("Could not load data from file:\n" + file.getPath());
	        	alert.show();
	        }
	      
	    }
    }
    
    public void handleRemove(String item) {
    	String filePath = item;
    	
    	LogFile log = logFiles.get(filePath);
    	
    	if (log != null) {
        	//remove file
        	events.clear();
        	logFiles.remove(filePath);
        	logFilesDisplay.remove(filePath);
        	
        	//refresh display
        	for (Map.Entry<String,LogFile> entry : logFiles.entrySet()) {
        		LogFile lf = entry.getValue();
        		events.addAll(lf.getEvents());
        	}
    	}
    	
    }
}