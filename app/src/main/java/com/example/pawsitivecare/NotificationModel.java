package com.example.pawsitivecare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationModel implements Comparable<NotificationModel> {
    private String tipeNotifikasi;
    private String date;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public NotificationModel(String tipeNotifikasi, String date) {
        this.tipeNotifikasi = tipeNotifikasi;
        this.date = date;
    }

    public String getTipeNotifikasi() {
        return tipeNotifikasi;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int compareTo(NotificationModel other) {
        try {
            Date thisDate = DATE_FORMAT.parse(this.date);
            Date otherDate = DATE_FORMAT.parse(other.date);
            if (thisDate != null && otherDate != null) {
                return otherDate.compareTo(thisDate); // Descending order
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}







