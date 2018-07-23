package application.util;

public class SearchOption {

	public static final SearchOption FILE = new SearchOption(0, "File");
	public static final SearchOption LEVEL = new SearchOption(1, "Level");
	public static final SearchOption SOURCE = new SearchOption(2, "Source");
	public static final SearchOption EVENTID = new SearchOption(3, "Event ID");
	public static final SearchOption CATEGORY = new SearchOption(4, "Task Category");
	public static final SearchOption DESC = new SearchOption(5, "Description");
	
	
	private int key;
	private String value;
	
	private SearchOption() {
		
	}
	
	private SearchOption(int key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public int getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isEqualTo (String s) {
		if (this.value.equals(s) ) {
			return true;
		}
		
		return false;
	}
}







