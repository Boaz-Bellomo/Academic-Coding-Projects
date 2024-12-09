/**
 * a clock class in a HH:MM format
 */
public class Clock {
    int hour;
    int minute;

    /**
     * constructor for clock
     * @param hour an int representing the hours
     * @param minute an int representing the minutes
     */
    public Clock(int hour, int minute) {
        if (hour > 23 || hour < 0)
            this.hour = 0;
        else this.hour = hour;
        if (minute > 59 || minute < 0)
            this.minute = 0;
        else this.minute = minute;
    }

    /**
     * present the Clock
     * @return the Clock in HH:MM format
     */
    @Override
    public String toString() {
        String strHours;
        String strMinute;
        if (hour < 10)
            strHours = "0" + hour;
        else
            strHours = Integer.toString(hour);

        if (minute < 10)
            strMinute = "0" + minute;
        else
            strMinute = Integer.toString(minute);

        return strHours + ":" + strMinute;
    }

    /**
     * compare 2 clocks depending on hours and minutes
     * @param other clock
     * @return true/ false
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || other instanceof Clock) return false; // check if works
        return hashCode() == other.hashCode();
    }

    /**
     * represent clock by hash code
     * @return num of minutes from 00:00
     */
    @Override
    public int hashCode() {
        return hour * 60 + minute;
    }
}
