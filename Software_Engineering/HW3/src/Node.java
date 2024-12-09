import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Node<T extends Cloneable> implements Cloneable {
    private Cloneable data;
    private Node<T> next;

    /**
     * creat new node that points on nothing
     * @param data that the node holds
     */
    public Node(Cloneable data) {
        this.data = data;
        this.next = null;
    }

    /**
     * recursive method to add a new node to end of queue
     * @param data that new node will hold
     */
    public void addNode(Cloneable data){
        if (this.next == null){
             this.next =  new Node<T>(data);
        } else {
             this.next.addNode(data);
        }
    }

    /**
     * @return nodes data
     */
    public Cloneable getData() {
        return data;
    }

    /**
     * @return nest node
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * @param next the new node to point on
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * @return clone method if data has one
     * @throws NoSuchMethodException if data hase no clone method
     */
    public Method getCloneMethod() throws NoSuchMethodException {
        Class<?> classObj = data.getClass();
        return classObj.getDeclaredMethod("clone");
    }

    /**
     * recursive method to clone all nodes
     * @return the copyed node
     * @throws CloneNotSupportedException if data can not be cloned
     */
    @Override
    public Node clone() throws CloneNotSupportedException {
        try {
            Node copy = (Node) super.clone(); // clone node
            copy.data = (Cloneable) getCloneMethod().invoke(data); // clone data if data's dynamic type has "clone" method

            // if not last node
            if (this.next != null) {
                copy.next = this.next.clone();// ask next node to clone itself
            } else {
                copy.next = null;
            }
            return copy;

        } catch (CloneNotSupportedException e) {
            return null;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return node in string
     */
    @Override
    public String toString() {
        return data.toString();
    }

}
