package com.example.controlactivitiesutfpr.Utils;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UtilsDate {

    public static String formatDate(Context context, Date date){

        SimpleDateFormat dateFormat;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            String pattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MM/dd/yyyy");

            dateFormat = new SimpleDateFormat(pattern);

        }else{

            dateFormat = (SimpleDateFormat) DateFormat.getMediumDateFormat(context);
        }

        return dateFormat.format(date);
    }

}

