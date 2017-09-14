package com.urban.piper.utility;

import android.text.TextUtils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Krishna Upadhya on 9/11/2017.
 */

public class DateTimeUtility {

    public static final String DISPLAY_DOB_DATE_FORMAT = "MM/dd/yyyy";

    public static String getUpdatedDateString(Long seconds) {
        if (seconds < 5) {
            return "Just now";
        } else if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 120) {
            return "A minute ago";
        } else {
            long minutes = seconds / 60;
            if (minutes < 60) {
                return minutes + " minutes ago";
            } else if (minutes < 120) {
                return "An hour ago";
            } else if (minutes < (24 * 60)) {
                return (int) (Math.floor(minutes / 60)) + " hours ago";
            } else {
                int days = (int) ((minutes / 60) / 24);
                if (days < 2) {
                    return "Yesterday";
                } else if (days < 7) {
                    return days + " days ago";
                } else if (days < 14) {
                    return "Last week";
                } else if (days < 30) {
                    return (int) (Math.floor(days / 7)) + " weeks ago";
                } else if (days < 60) {
                    return "Last month";
                } else if (days < 365) {
                    return (int) (Math.floor(days / 30)) + " months ago";
                } else if (days < 731) {
                    return "Last year";
                } else {
                    return (int) (Math.floor(days / 365)) + " years ago";
                }
            }
        }
    }


    public static String getConvertedDate(long timeInSeconds) {

        Long differenceInSeconds = null;
        String dateString = null;
        try {
            Calendar cal = Calendar.getInstance();
            if (cal != null) {
                long currentTimeInSeconds = cal.getTimeInMillis() / 1000;
                differenceInSeconds = currentTimeInSeconds - timeInSeconds;
                if (differenceInSeconds != null) {
                    dateString = getUpdatedDateString(differenceInSeconds);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String getFormattedTime(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static String getFormattedTime(long timeMilliSeconds, String format) {
        return getFormattedTime(new Date(timeMilliSeconds), format);
    }

    public static Date dateFormatter(String dateString, String dateFormat) {
        SimpleDateFormat setDateFormatter;
        Date formattedDate = null;
        if (!TextUtils.isEmpty(dateString)) {
            setDateFormatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            try {
                formattedDate = setDateFormatter.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return formattedDate;
    }

    public static String getDayOfMonthSuffix(final int month) {
        try {
            if (month >= 11 && month <= 13) {
                return month + "th";
            }
            switch (month % 10) {
                case 1:
                    return month + "st";
                case 2:
                    return month + "nd";
                case 3:
                    return month + "rd";
                default:
                    return month + "th";

            }
        } catch (Exception e) {
            LogUtility.d("getMonth -- ", e.getMessage());
            return "";
        }
    }

    public static String getMonth(int month) {
        String displayMonth = "";
        try {
            if (month > 0) {
                displayMonth = new DateFormatSymbols().getMonths()[month - 1];
            }
        } catch (Exception e) {
            LogUtility.d("getMonth -- ", e.getMessage());
            return displayMonth;
        }
        return displayMonth;
    }

}
