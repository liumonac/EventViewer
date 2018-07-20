package application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    private static final SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss aa");
    private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("MM/dd/yyyy HH:mm");

    public static String format(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        String result = DATE_FORMAT1.format(calendar.getTime());
        
        return result;
    }

    public static Calendar parse(String dateString) {
    	//fix month 2 digits, 7 to 07
    	if (dateString.matches("\\d/.*")) {
    		dateString = "0" + dateString;
    	}
    	
    	//fix day 2 digits, 7 to 07
    	if (dateString.matches("\\d{2}/\\d/.*")) {
    		String date = dateString.substring(0, 3);
    		dateString = dateString.substring(3);
    		dateString = date + "0" + dateString;
    	}
    	
    	//fix hour 2 digits, 7 to 07
    	if (dateString.matches("\\d{2}/\\d{2}/\\d{4} \\d:.*")) {
    		String date = dateString.substring(0, 11);
    	    String time = dateString.substring(11);
    		dateString = date + "0" + time;
    	}
    	
    	System.out.println(dateString);
    	
        Calendar result = Calendar.getInstance();
        try {
            result.setTime(DATE_FORMAT1.parse(dateString));
            return result;
        } catch (ParseException e1) {
            try {
                result.setTime(DATE_FORMAT2.parse(dateString));
                return result;
            } catch (ParseException e2) {
                return null;
            }
        }
    }

    public static boolean validString(String dateString) {
        try {
            DATE_FORMAT1.parse(dateString);
            return true;
        } catch (ParseException e1) {
            try {
                DATE_FORMAT2.parse(dateString);
                return true;
            } catch (ParseException e2) {
                return false;
            }
        }
        
    }
}
