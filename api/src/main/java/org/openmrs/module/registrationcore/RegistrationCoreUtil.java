package org.openmrs.module.registrationcore;

import java.util.Calendar;
import java.util.Date;

public class RegistrationCoreUtil {

    /**
     * Converts an age (specified in year, month, and day) to a birthdate.
     * You can specify the specific date to calculate the age from, or, if
     * not specified, today's date is used. Year, month, or day can all be left blank.
     *
     * Note that if only a year is specified, the month/day is set to January 1;
     *
     * If only the month (or the month and year) is specified, the day is set to the first day of the month
     *
     * For example, if ageOnDate is set to April 3, 2010, and someone is said to be 10 years old,
     * the method would return January 1, 2000; if someone is said to be 2 months old, the
     * method would return February 1, 2000
     */
    public static Date calculateBirthdateFromAge(Integer years, Integer months, Integer days, Date ageOnDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(ageOnDate == null ? new Date() : ageOnDate);

        // set the year
        if (years != null) {
            c.add(Calendar.YEAR, -1 * years);
        }

        // set the month (if not specified, and no day specified, set to first month of year)
        if (months != null && months != 0) {
            c.add(Calendar.MONTH, -1 * months);
        }
        else if (days == null || days == 0){
            c.set(Calendar.MONTH,0);
        }

        // set the day (if not specified, and no month specified, set to first day of month)
        if (days != null && days !=0) {
            c.add(Calendar.DAY_OF_MONTH, -1 * days);
        }
        else if (days == null || days == 0){
            c.set(Calendar.DAY_OF_MONTH,1);
        }

        return c.getTime();
    }


}