package com.example.bitcointicker.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static String convertDate(Long milliSeconds){
        try {

            // Creating date format
            DateFormat simple = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss Z");

            // Creating date from milliseconds
            // using Date() constructor
            Date result = new Date(milliSeconds);

            // Formatting Date according to the
            // given format
            System.out.println(simple.format(result));
            return simple.format(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "000000";
    }
}
