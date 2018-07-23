package application.model;

import java.util.ArrayList;

public class LogFile {
	private String filePath;
	private String fileName;
	
	private ArrayList<LogEvent> eventElements = new  ArrayList<LogEvent>();
	
/******************************************
 * Constructors
 ******************************************/  
	public LogFile() {
		
	}
	
	public LogFile(String filePath, String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
	}
	
	public LogFile(String filePath, String fileName, ArrayList<LogEvent> eventElements) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.eventElements = eventElements;
	}
	
/******************************************
 * Getters
 ******************************************/  
	public String getFilePath() {
		return filePath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public ArrayList<LogEvent> getEvents() {
		return eventElements;
	}
}
