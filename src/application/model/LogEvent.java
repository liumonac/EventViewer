package application.model;

import java.util.Calendar;

import application.util.CalendarUtil;

public class LogEvent {
	private String level;
	private String displayDate;
	private String source;
	private String id;
	private String category;
	private String desc;
	private String fileName;
	private Calendar date;
	
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

		if (CalendarUtil.validString (displayDate)) {
			date = CalendarUtil.parse(displayDate);
			this.displayDate = displayDate;
		}
	}

	//getters
	public String getLevel () {
		return level;
	}
	
	public Calendar getDate () {
		return date;
	}
	
	public String getDisplayDate () {
		return displayDate;
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
	
	//setters
	public void setDescription(String desc) {
		this.desc = desc;
	}
}
