import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

class MyCloneable implements Cloneable {
    private int num;

    public MyCloneable(int num) {
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "A: " + num;
    }

    @Override
    public MyCloneable clone() {
        try {
            return (MyCloneable) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}


public class Main {
    public static void main(String[] args) {
        testPartA();
        testPartB();
    }

    /**
     * Tests for part A.
     */
    private static void testPartA() {

        Queue<MyCloneable> q1 = new LinkedListQueue<>();
        LinkedListQueue<MyCloneable> q2 = (LinkedListQueue<MyCloneable>) q1;

        try {
            q1.peek();
        } catch (EmptyQueueException e) {
            System.out.println("The queue is empty");
        }

        try {
            q2.peek();
        } catch (EmptyQueueException e) {
            System.out.println("The queue is empty");
        }
        System.out.println();

        iterateQueue(q1, "q1");
        MyCloneable c1 = new MyCloneable(1);
        q1.enqueue(c1);
        iterateQueue(q1, "q1");
        System.out.println("Peek: " + q1.peek());
        System.out.println("Deque: " + q1.dequeue());
        System.out.println();
        iterateQueue(q1, "q1");

        MyCloneable c2 = new MyCloneable(2);
        q1.enqueue(c1);
        q1.enqueue(c2);

        iterateQueue(q2, "q2");

        LinkedListQueue<MyCloneable> q3 = q2.clone();
        Queue<MyCloneable> q4 = q1.clone();

        System.out.println("Is q1 == q3? " + (q1 == q3));
        System.out.println("Is q2 == q3? " + (q2 == q3));
        System.out.println("Is q1 == q4? " + (q1 == q4));
        System.out.println("Is q2 == q4? " + (q2 == q4));
        System.out.println();
        iterateQueue(q3, "q3");

        c1.setNum(15);
        iterateQueue(q1, "q1");
        iterateQueue(q2, "q2");
        iterateQueue(q3, "q3");
        iterateQueue(q4, "q4");

        q1.enqueue(new MyCloneable(3));
        iterateQueue(q1, "q1");
        iterateQueue(q2, "q2");
        iterateQueue(q3, "q3");
        iterateQueue(q4, "q4");

        q3.enqueue(new MyCloneable(4));
        iterateQueue(q1, "q1");
        iterateQueue(q2, "q2");
        iterateQueue(q3, "q3");
        iterateQueue(q4, "q4");

        q4.enqueue(new MyCloneable(5));
        iterateQueue(q1, "q1");
        iterateQueue(q2, "q2");
        iterateQueue(q3, "q3");
        iterateQueue(q4, "q4");

        System.out.println("\nTesting of part A is over!\n\n");
    }

    /**
     * Iterates over a given queue.
     */
    private static void iterateQueue(Queue<?> q, String name) {
        System.out.println("Starts iterating " + name + "...");
        System.out.println("Queue size: " + q.size());
        System.out.println("Is empty? " + q.isEmpty());

        for (Object obj : q) {
            System.out.println(obj);
        }

        System.out.println("Done iterating");
        System.out.println("");
    }

    /**
     * Tests for part B.
     */
    private static void testPartB() {
        ToDoList list1 = new ToDoList();
        for (Task t : list1) {
            System.out.println("You should not reach here!");
        }

        Task task1 = new Task("Software Engineering HW0", new Date(2022 - 1900, Calendar.OCTOBER, 20));
        Task task2 = new Task("Software Engineering HW1", new Date(2022 - 1900, Calendar.DECEMBER, 8));
        Task task3 = new Task("Software Engineering HW2", new Date(2023 - 1900, Calendar.JANUARY, 1));
        Task task4 = new Task("Software Engineering HW3", new Date(2023 - 1900, Calendar.JANUARY, 12));

        list1.addTask(task1);
        list1.addTask(task4);
        list1.addTask(task3);
        list1.addTask(task2);

        System.out.println("list1: " + list1);

        ToDoList list2 = list1.clone();
        System.out.println("list1: " + list1);
        System.out.println("list2: " + list2);

        System.out.println("Is list1 == list2? " + (list1 == list2));
        System.out.println("Are lists list1 and list2 equal? " + list1.equals(list2));
        System.out.println("Are lists list2 and list1 equal? " + list2.equals(list1));
        System.out.println("Is list1.equals(null)? " + list1.equals(null));

        task3.setDueDate(new Date(2022 - 1900, Calendar.DECEMBER, 18));

        System.out.println("list1: " + list1);
        System.out.println("list2: " + list2);

        System.out.println();

        System.out.println("Are lists list1 and list2 equal? " + list1.equals(list2));
        System.out.println("Are lists list2 and list1 equal? " + list2.equals(list1));


        try {
            list1.addTask(new Task("Software Engineering HW0", new Date(2022 - 1900, Calendar.OCTOBER, 24)));
        } catch (TaskAlreadyExistsException e) {
            System.out.println("Cannot add the task!");
        }
        try {
            list1.addTask(new Task("Software Engineering HW1", new Date(2022 - 1900, Calendar.DECEMBER, 8)));
        } catch (TaskAlreadyExistsException e) {
            System.out.println("Cannot add the task!");
        }
        Task clonedTask1 = task1.clone();
        try {
            list1.addTask(clonedTask1);
        } catch (TaskAlreadyExistsException e) {
            System.out.println("Cannot add the task!");
        }

        System.out.println("task1: " + task1);
        System.out.println("clonedTask1: " + clonedTask1);
        System.out.println("Are lists 11 and clonedTask1 equal? " + task1.equals(clonedTask1));
        System.out.println("Are lists clonedTask1 and task1 equal? " + clonedTask1.equals(task1));
        System.out.println("Is task1 == clonedTask1? " + (task1 == clonedTask1));
        System.out.println("Is task1.equals(null)? " + task1.equals(null));
        System.out.println();


        Date[] dates = {null,
                        new Date(2023 - 1900, Calendar.MARCH, 21),
                        task2.getDueDate(),
                        new Date(2017 - 1900, Calendar.SEPTEMBER, 17),
                        new Date(2022 - 1900, Calendar.DECEMBER, 7),
                        task3.getDueDate()};

        checkScans(list1, dates);

        ToDoList list3 = new ToDoList();

        System.out.println("Are lists list1 and list3 equal? " + list1.equals(list3));
        list3.addTask(task2);
        System.out.println("list3: " + list3);
        System.out.println("Are lists list1 and list3 equal? " + list1.equals(list3));

        list3.addTask(task4);
        System.out.println("list3: " + list3);
        System.out.println("Are lists list1 and list3 equal? " + list1.equals(list3));

        list3.addTask(task1);
        System.out.println("list3: " + list3);
        System.out.println("Are lists list1 and list3 equal? " + list1.equals(list3));

        list3.addTask(task3);
        System.out.println("list3: " + list3);
        System.out.println("Are lists list1 and list3 equal? " + list1.equals(list3));

        System.out.println();
        Set<ToDoList> set = new HashSet<>();
        set.add(list1);
        set.add(list3);
        System.out.println("list1: " + list1);
        System.out.println("list3: " + list3);

        checkScans(list3, dates);

        Task task5 = new Task("Calculus 2 HW10", new Date(2022 - 1900, Calendar.OCTOBER, 25));
        list3.addTask(task5);

        System.out.println("list3: " + list3);
        checkScans(list3, dates);



        System.out.println("Number of element in set: " + set.size());


        System.out.println("\nTesting of part B is over!");
    }

    /**
     * Checks all of the scan types for a given ToDoList using different dates.
     */
    private static void checkScans(ToDoList tdl, Date[] dates) {
        System.out.println("Starts scanning...");
        for (Task t : tdl) {
            System.out.println(t);
            System.out.println("-----------------------------------");
        }

        System.out.println("After initial scanning\n");

        int i = 1;
        for (Date date : dates) {
            System.out.println("Starting scan number " + i);
            tdl.setScanningDueDate(date);
            for (Task t : tdl) {
                System.out.println(t);
                System.out.println("-----------------------------------");
            }
            System.out.println("After scan number " + i++ + "\n");
        }

        System.out.println("Done scanning");
    }
}
