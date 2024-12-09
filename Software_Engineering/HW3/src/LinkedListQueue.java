import java.util.Iterator;

public class LinkedListQueue<T extends Cloneable> implements Queue {
    private Node<T> head;
    private int size;

    /**
     * recursive method to add element to queue
     * @param element a generic data to add to queue
     */
    @Override
    public void enqueue(Cloneable element) {
        // if list is empty
        if (head == null){
            // create first node
            this.head = new Node<>(element);
            size = 1;
        } else {
            // add new node to tail
            head.addNode(element);
            size++;
        }
    }

    /**
     * remove first element from queue
     * @return removed element
     */
    @Override
    public Cloneable dequeue() {
        Node firstNode = this.head; // get data
        // if only element in queue
        if (head.getNext() == null){
            this.head = null; // make queue empty
        } else {
            // replace head whit next and remove link from old head to new head
            Node newHead = head.getNext();
            this.head.setNext(null);
            head = newHead;
        }
        size--;
        return firstNode.getData();
    }

    /**
     * @return first element in queue
     * @throws EmptyQueueException if queue is empty
     */
    @Override
    public Cloneable peek() throws EmptyQueueException{
        if (size == 0){
            throw new EmptyQueueException();
        } else {
            return head.getData();
        }
    }

    /**
     * @return queue size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @return if queue is empty
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return deep cloned queue
     */
    @Override
    public LinkedListQueue clone() {
        try {
            LinkedListQueue copy = (LinkedListQueue) super.clone();
            copy.head = this.head.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * @return iterator for queue
     */
    @Override
    public Iterator<Node> iterator() {
        return new SetIterator();
    }

    /**
     * bulid iterator for queue
     */
    private class SetIterator implements Iterator<Node>{
        private int index = 0;
        private Node currentNode = head;

        /**
         * @return if there is another method
         */
        @Override
        public boolean hasNext() {
            return index < size;
        }

        /**
         * @return next element
         */
        @Override
        public Node next() {
            if (index != 0){
                currentNode = currentNode.getNext();
            }
            index++;
            return currentNode;

        }
    }
}
