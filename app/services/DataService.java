package services;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataService {

    public static String toString(Calendar c){
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataAsString = s.format(c.getTime());
        return dataAsString;
    }
}
