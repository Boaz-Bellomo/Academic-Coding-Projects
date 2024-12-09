import java.util.Date;

public interface TaskIterable extends Iterable<Task> {
    void setScanningDueDate(Date date);
}
