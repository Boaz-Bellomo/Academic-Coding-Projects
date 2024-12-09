/**
 * an acuter clock class in a HH:MM:SS format
 */
public class AccurateClock extends Clock {
    int seconds;

    /**
     * constructor for accurate clock
     * @param hour an int representing the hour
     * @param minute an int representing the minute
     * @param seconds an int representing the seconds
     */
    public AccurateClock(int hour, int minute, int seconds) {
        super(hour, minute);
        if (seconds > 59 || seconds < 0)
            this.seconds = 0;
        else this.seconds = seconds;
    }

    /**
     * present the accurate Clock
     * @return the accurate Clock in HH:MM:SS format
     */
    @Override
    public String toString() {
        String strSeconds;
        if (seconds < 10)
            strSeconds = "0" + seconds;
        else
            strSeconds = Integer.toString(seconds);

        return super.toString() + ":" + strSeconds;
    }
    /**
     * compare 2 accurate clocks depending on hours, minutes and seconds
     * @param other accurate clock
     * @return true/ false
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || other instanceof AccurateClock) return false;
        return hashCode() == other.hashCode();
    }

    /**
     * represent accurate clock by hash code
     * @return num of seconds from 00:00:00
     */
    @Override
    public int hashCode() {
        return super.hashCode()*60 + seconds;
    }
}
