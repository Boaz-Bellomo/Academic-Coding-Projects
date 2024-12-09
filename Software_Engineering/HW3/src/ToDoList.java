import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class ToDoList implements Cloneable, TaskIterable {
    private Node head;
    private Date scanDate = null;
    private ArrayList<Task> sortedTask = new ArrayList<>();

    /**
     * empty list constructor
     */
    public ToDoList(){
    head = null;
}

    /**
     * add task to list depending on state of list
     * @param task to add
     */
    public void addTask (Task task) {
        if (head == null) {
            head = new Node(task);
        }
        else {
            Node flag = head;
            while (flag != null){
                if (cmpDescription(task, (Task) flag.getData())){
                    throw new TaskAlreadyExistsException();
                }
                flag = flag.getNext();
            }
            head.addNode(task);
        }
    }

    /**
     * compare between two tasks
     * @param t1 first task
     * @param t2 second task
     * @return true / false
     */
    private boolean cmpDescription(Task t1, Task t2){
            return t1.getDescription().equals(t2.getDescription());
    }

    /**
     * deep cloning of list
     * @return clone of list
     */
    @Override
    public ToDoList clone(){
        ToDoList copy;
        try {
            copy = (ToDoList) super.clone();
            copy.head = this.head.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return copy;
    }

    /**
     * compare 2 lists
     * @param o list to compare to
     * @return true / false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoList other = (ToDoList) o;

        ArrayList<Task> thisSortedTask = sortTaskArray(this.head);
        ArrayList<Task> otherSortedTask = sortTaskArray(other.head);

        if (thisSortedTask.size() != otherSortedTask.size()){
            return false;
        }
        for (int i = 0; i < thisSortedTask.size(); i++){
            if (!thisSortedTask.get(i).equals(otherSortedTask.get(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * sort link list toDoList in ArrayList
     * @param head first node in toDoList
     * @return sorted ArrayList
     */
    private ArrayList<Task> sortTaskArray(Node head){
        ArrayList<Task> sortedArray = new ArrayList<>();
        Node flag = head;
        while (flag != null){
            sortedArray.add((Task) flag.getData());
            flag = flag.getNext();
        }
        // sort array
        Collections.sort(sortedArray, new SortByDate());
        return sortedArray;
    }

    /**
     * calculate a unique hash code for etch list
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 0;
        Node flag = head;
        while (flag != null){
            hash = hash + flag.getData().hashCode();
            flag = flag.getNext();
        }
        return hash;
    }

    /**
     * represent list with string
     * @return all tasks in list
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        Node flag = head;
        while (flag != null){
            str.append("(").append(flag.getData().toString()).append("), ");
            flag = flag.getNext();
        }
        str.delete(str.length()-2, str.length());
        str.append("]");
        return str.toString();
    }

    /**
     * set scanning dueDate
     * @param date scanning dueDate
     */
    @Override
    public void setScanningDueDate(Date date){
        if (date == null){
            scanDate = null;
        } else {
            scanDate = date;
        }
    }

    /**
     * constructor for iterator
     * @return iterator
     */
    @Override
    public Iterator iterator(){
        sortedTask.clear();
        sortedTask = sortTaskArray(this.head);

        return new ToDoListIterator(scanDate);
    }

    /**
     * inner iterator class
     */
    private class ToDoListIterator implements Iterator<Task>{
        private int index = 0;
        private Date scanDate;
        private Task nextTask = null;

        /**
         * constructor for ToDoListIterator
         * @param scanDate scanning dueDate
         */
        public ToDoListIterator(Date scanDate) {
            this.scanDate = scanDate;
        }

        /**
         * @return true if hase next task
         */
        @Override
        public boolean hasNext() {
            if (sortedTask.size() == 0){
                return false;
            }
            if (scanDate == null){
                return index < sortedTask.size();

            } else {
                for (int i = index; i < sortedTask.size(); i++){
                    // check if its due date is lesser or equal than wanted date
                    if (sortedTask.get(i).getDueDate().before(scanDate) || sortedTask.get(i).getDueDate().equals(scanDate)){
                        nextTask = sortedTask.get(i);
                        return true;
                    }
                }
                // if none where found
                return false;
            }
        }

        /**
         * @return next task
         */
        @Override
        public Task next() {
            // if regular scan
            if (scanDate == null){
                // return next task
                return sortedTask.get(index++);

            } else { // if dated scan
                index++;
                return nextTask;
            }
        }
    }
}
