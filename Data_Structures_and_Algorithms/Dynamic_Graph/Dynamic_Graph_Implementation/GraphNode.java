public class GraphNode {
    private final int nodeKey;


    // lined list of in nodes
    private GraphNode next;
    private GraphNode prev;


    // lined list of in edges
    private GraphEdge inHead;
    private int inDegree;


    // lined list of out edges
    private GraphEdge outHead;
    private int outDegree;


    // DFS and BFS attributes
    private int discovered, finished, color;
    private double distance;
    private int valueFiled;
    private GraphNode nextSCC;
    private GraphNode prevSCC;

    // tree attributes
    private GraphNode treeParent;
    private GraphNode leftChild;
    private GraphNode rightSibling;
    private GraphNode lastSibling;



    public GraphNode(int nodeKey) {
        this.nodeKey = nodeKey;
        inHead = null;
        inDegree = 0;
        outHead = null;
        outDegree = 0;
    }

    public void insertChild(GraphNode child){
        if (leftChild == null){
            leftChild = child;
            child.lastSibling = child;
        } else {
            leftChild.lastSibling.rightSibling = child;
            leftChild.lastSibling = child;
        }

        if (child.treeParent != null){
            child.treeParent = this;
        }
    }

    /**
     * @return the out deg of a node
     * time complex - O(1)
     */
    public int getOutDegree(){return outDegree;}

    /**
     * @return the in deg of a node
     * time complex - O(1)
     */
    public int getInDegree(){return inDegree;}

    /**
     * @return the unique node key
     * time complex - O(1)
     */
    public int getKey(){return nodeKey;}

    public void addInDegree() {inDegree++;}

    public void decreaseInDegree() {inDegree--;}

    public void addOutDegree() {outDegree++;}

    public void decreaseOutDegree() {outDegree--;}

    public GraphNode getNext() {return next;}

    public void setNext(GraphNode next) {this.next = next;}

    public GraphNode getPrev() {return prev;}

    public void setPrev(GraphNode prev) {this.prev = prev;}

    public GraphEdge getInHead() {return inHead;}

    public void setInHead(GraphEdge inHead) {this.inHead = inHead;}

    public GraphEdge getOutHead() {return outHead;}

    public void setOutHead(GraphEdge outHead) {this.outHead = outHead;}

    public void setDiscovered(int discovered) {this.discovered = discovered;}

    public void setFinished(int finished) {this.finished = finished;}

    public void setColor(int color) {this.color = color;}

    public int getFinished() {return finished;}

    public int getColor() {return color;}

    public double getDistance() {return distance;}

    public void setDistance(double distance) {this.distance = distance;}

    public int getValueFiled() {return valueFiled;}

    public void setValueFiled(int valueFiled) {this.valueFiled = valueFiled;}

    public GraphNode getTreeParent() {return treeParent;}

    public void setTreeParent(GraphNode treeParent) {this.treeParent = treeParent;}

    public GraphNode getLeftChild() {return leftChild;}

    public void setLeftChild(GraphNode leftChild) {this.leftChild = leftChild;}

    public GraphNode getRightSibling() {return rightSibling;}

    public void setRightSibling(GraphNode rightSibling) {this.rightSibling = rightSibling;}

    public GraphNode getNextSCC() {return nextSCC;}

    public void setNextSCC(GraphNode nextSCC) {this.nextSCC = nextSCC;}

    public void setPrevSCC(GraphNode prevSCC) {this.prevSCC = prevSCC;}

    @Override
    public String toString() {return String.valueOf(getKey());}

}
