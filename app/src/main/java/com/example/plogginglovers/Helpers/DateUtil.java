package com.example.plogginglovers.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtil {

    public static String dateWithDesiredFormat(String old_format, String new_format, String date){
        SimpleDateFormat format = new SimpleDateFormat(old_format);
        SimpleDateFormat format_desired = new SimpleDateFormat(new_format);
        try {
            return format_desired.format(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
