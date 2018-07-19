package application.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    private static final SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss aaa");
    private static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public static String format(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        String result = DATE_FORMAT1.format(calendar.getTime());
        
        if (result == null) {
        	result = DATE_FORMAT2.format(calendar.getTime());
        }
        
        return result;
    }

    public static Calendar parse(String dateString) {
        Calendar result = Calendar.getInstance();
        try {
            result.setTime(DATE_FORMAT1.parse(dateString));
            return result;
        } catch (ParseException e) {
            try {
                result.setTime(DATE_FORMAT2.parse(dateString));
                return result;
            } catch (ParseException er) {
                return null;
            }
        }
    }

    public static boolean validString(String dateString) {
        try {
            DATE_FORMAT1.parse(dateString);
            return true;
        } catch (ParseException e) {
            try {
                DATE_FORMAT2.parse(dateString);
                return true;
            } catch (ParseException er) {
                return false;
            }
        }
        
    }
}
