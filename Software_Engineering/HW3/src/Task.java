import java.util.Date; // is this ok?


public class Task implements Cloneable {
    private final String description;
    private Date dueDate;

    /**
     * constructor for task
     * @param description of the task
     * @param dueDate of the task
     */
    public Task(String description, Date dueDate) {
        this.description = description;
        this.dueDate = dueDate;
    }

    /**
     * @return dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param dueDate set dueDate
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * calculate num of day past from 01/01/1900
     * @return num of days
     */
    public int hashDate(){
        return dueDate.getDay() + (dueDate.getMonth()+1)*31 + dueDate.getYear()*365;
    }

    /**
     * deep clone of task
     * @return clone of task
     */
    @Override
    public Task clone(){
        Task copy;
        try {
            copy = (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        copy.dueDate = (Date) dueDate.clone();
        return copy;
    }

    /**
     * compare two tasks
     * @param o other task
     * @return true / false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task other = (Task) o;
        return this.hashCode() == other.hashCode();
    }

    /**
     * @return unique hash val
     */
    @Override
    public int hashCode() {
        return description.hashCode() + hashDate()*100000;
    }

    /**
     * @return representation of task in string
     */
    @Override
    public String toString() {
        String month, day;
        int year = dueDate.getYear() + 1900;
        if (dueDate.getMonth() < 9){
             month = "0" + (dueDate.getMonth() + 1);
        } else {
             month = String.valueOf(dueDate.getMonth() + 1);
        }

        if (dueDate.getDate() < 10){
            day = "0" + (dueDate.getDate());
        } else {
            day = String.valueOf(dueDate.getDate());
        }

        return description + ", " + day + "." + month + "." + year;
    }
}
