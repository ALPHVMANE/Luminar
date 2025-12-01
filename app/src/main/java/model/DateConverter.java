package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    // Function to convert long to date
    public static String convertMillisToFormattedDate(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault());
        return sdf.format(date);
    }
}
