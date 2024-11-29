package fr.cyu.schoolmanagementsystem.entity;

public enum WeekDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY;

    public static WeekDay fromDayOfWeek(java.time.DayOfWeek dayOfWeek) {
        return WeekDay.valueOf(dayOfWeek.name());
    }
}

