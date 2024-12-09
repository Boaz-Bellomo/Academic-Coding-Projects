import java.util.Comparator;

public class SortByDate implements Comparator<Task> {

    /**
     * compare two task to etch other
     * @param t1 the first object to be compared.
     * @param t2 the second object to be compared.
     * @return -1/0/1 depending on result
     */
    @Override
    public int compare(Task t1, Task t2) {

        int dateComper = t1.getDueDate().compareTo(t2.getDueDate());

        int discriptionComper = t1.getDescription().compareTo(t2.getDescription());

        return (dateComper == 0) ? discriptionComper : dateComper;
    }
}
