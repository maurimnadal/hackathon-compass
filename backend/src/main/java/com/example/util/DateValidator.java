package com.example.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {
    public static void validateDate(String dateStr) {
        if (dateStr == null || dateStr.length() != 8) {
            throw new IllegalArgumentException("Date must be 8 digits in YYYYMMDD format");
        }

        try {
            int month = Integer.parseInt(dateStr.substring(4, 6));
            int day = Integer.parseInt(dateStr.substring(6, 8));
            int year = Integer.parseInt(dateStr.substring(0, 4));

            // Validate month
            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Date month must be between 1 and 12");
            }

            // Validate day using YearMonth
            int maxDays = YearMonth.of(year, month).lengthOfMonth();
            boolean isLeapYear = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
            
            if (day < 1 || day > maxDays) {
                String leapYearInfo = month == 2 ? (isLeapYear ? " (leap year)" : " (non-leap year)") : "";
                throw new IllegalArgumentException(
                        String.format("Invalid day %d for month %d%s",
                                day, month, leapYearInfo)
                );
            }

            // Now that we know the date is valid, parse it and do additional validations
            LocalDate birthday = parseDate(dateStr);
            LocalDate now = LocalDate.now();

            if (birthday.isAfter(now)) {
                throw new IllegalArgumentException("Date cannot be in the future");
            }

            if (birthday.getYear() < 1900 || birthday.getYear() > now.getYear()) {
                throw new IllegalArgumentException("Year must be between 1900 or current year");
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Date format. Must be YYYYMMDD");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Date format. Must be YYYYMMDD with a valid date");
        }
    }

    public static LocalDate parseDate(String birthdayStr) {
        if (birthdayStr == null || birthdayStr.length() != 8) {
            throw new IllegalArgumentException("Date must be 8 digits in YYYYMMDD format");
        }

        try {
            return LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid Date format. Must be YYYYMMDD with a valid date");
        }
    }
}