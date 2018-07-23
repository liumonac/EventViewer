package application.model;

import java.util.Calendar;

import application.util.CalendarUtil;

public class LogEvent {
	private String level;
	private String source;
	private String id;
	private String category;
	private String desc;
	private String fileName;
	private Calendar date;
	
/******************************************
 * Constructors
 ******************************************/  
	public LogEvent() {
		
	}
	
	public LogEvent(String desc, String fileName) {
		this.desc = desc;
		this.fileName = fileName;
	}
		
	public LogEvent(String level, String displayDate, String source, String id, String category, String desc, String fileName) {
		this.level = level;
		this.source = source;
		this.id = id;
		this.category = category;
		this.desc = desc;
		this.fileName = fileName;

		date = CalendarUtil.parse(displayDate);
	}

/******************************************
 * Getters
 ******************************************/  
	public String getLevel () {
		return level;
	}
	
	public Calendar getDate () {
		return date;
	}
	
	public String getDisplayDate () {
		return CalendarUtil.format(date);
	}
	
	public String getSource () {
		return source;
	}
	
	public String getId () {
		return id;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getFileName() {
		return fileName;
	}
	
/******************************************
 * Setters
 ******************************************/  
	public void setDescription(String desc) {
		this.desc = desc;
	}
}
