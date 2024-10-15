public class Queue {
    //TODO: generic Queue
    static class QueueNode {
        GraphNode data;
        QueueNode next;
        public QueueNode(GraphNode data) {
            this.data = data;
        }
    }

    private QueueNode first;
    private QueueNode last;
    private int size;

    public Queue() {
        first = null;
        size = 0;
    }

    public void enqueue(GraphNode data) {
        QueueNode node = new QueueNode(data);
        if (first == null) {
            first = node;
            last = first;
        } else {
            last.next = node;
            last = node;
        }
        size++;
    }

    public GraphNode dequeue() {
        if (first == null) {
            return null; // or throw an exception to indicate an empty queue
        }

        GraphNode data = first.data;
        first = first.next;
        if (first == null) {
            last = null; // Set last to null when queue becomes empty
        }
        size--;
        return data;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int getSize() {
        return size;
    }
}
