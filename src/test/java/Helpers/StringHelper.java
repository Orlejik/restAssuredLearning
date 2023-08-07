package Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringHelper {

    public static String reduceStringLength(Object str, int numberForReduce){
        String regex = "(.{"+numberForReduce+"})$";
        return str.toString().replaceAll(regex, "");
    }

    public static String formatDate(Date date){
        String pattern = "EEE MMM dd kk:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }


}
