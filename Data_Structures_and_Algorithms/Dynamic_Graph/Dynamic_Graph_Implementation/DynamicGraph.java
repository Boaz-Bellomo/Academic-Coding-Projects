public class DynamicGraph {

    private GraphNode head;
    private int graphSize = 0;
    private GraphNode sortedHead;

    /**
     * default constructor.
     * creates an empty tree with no nodes or edges.
     * time complex - O(1)
     */
    public DynamicGraph(){
        head = null;
    }

    /**
     * create new node with a given key that is attached to no edge.
     * @param nodeKey of new node
     * @return pointer to new node
     * time complex - O(1)
     */
    public GraphNode insertNode(int nodeKey){
        GraphNode node = new GraphNode(nodeKey);
        if (head != null) {
            head.setPrev(node);
            node.setNext(head);
        }
        head = node;

        node.setPrev(null);
        graphSize++;
        return node;
    }

    /**
     * delete given node only if there is no in or out edges.
     * @param node to delete.
     * time complex - O(1)
     */
    public void deleteNode(GraphNode node){
        if (node.getInDegree() == 0 && node.getOutDegree() ==0){
            if (node.getPrev() == null){
                head = node.getNext();
                head.setPrev(null);
            } else {
                node.getPrev().setNext(node.getNext());
            }

            if (node.getNext() == null){
                node.getPrev().setNext(null);
            } else {
                node.getNext().setPrev(node.getPrev());
            }
            graphSize--;
        }
    }

    /**
     * adds new edges from src to dst.
     * @param src from node.
     * @param dst to node.
     * @return pointer to new edge.
     * time complex - O(1)
     */
    public GraphEdge insertEdge(GraphNode src, GraphNode dst){
        GraphEdge edge = new GraphEdge(src,dst);

        if(src.getOutHead() == null){
            src.setOutHead(edge);
        } else {
            src.getOutHead().setPrevOut(edge);
            edge.setNextOut(src.getOutHead());
            src.setOutHead(edge);
        }

        if(dst.getInHead() == null){
            dst.setInHead(edge);
        } else {
            dst.getInHead().setPrevIn(edge);
            edge.setNextIn(dst.getInHead());
            dst.setInHead(edge);
        }

        src.addOutDegree();
        dst.addInDegree();
        return edge;
    }

    /**
     * erases given edge from graph.
     * @param edge to erase.
     * time complex - O(1)
     */
    public void deleteEdge(GraphEdge edge){
        // delete in src
        edge.getSrc().decreaseOutDegree();
        // if sole edge
        if (edge.getSrc().getOutDegree() == 0){
            edge.getSrc().setOutHead(null);
        }
        //if first in out-list
        else if (edge.getPrevOut() == null){
            edge.getSrc().setOutHead(edge.getNextOut());
            edge.getNextOut().setPrevOut(null);
        }
        // if last in out-list
        else if (edge.getNextOut() == null){
            edge.getPrevOut().setNextOut(null);
        } else {
            edge.getPrevOut().setNextOut(edge.getNextOut());
            edge.getNextOut().setPrevOut(edge.getPrevOut());
        }

        // delete in dst
        edge.getDst().decreaseInDegree();
        // if sole edge
        if (edge.getDst().getInDegree() == 0){
            edge.getDst().setInHead(null);
        }
        //if first in in-list
        else if (edge.getPrevIn() == null){
            edge.getDst().setInHead(edge.getNextIn());
            edge.getNextIn().setPrevIn(null);
        }
        // if last in in-list
        else if (edge.getNextIn() == null){
            edge.getPrevIn().setNextIn(null);
        } else {
            edge.getPrevIn().setNextIn(edge.getNextIn());
            edge.getNextIn().setPrevIn(edge.getPrevIn());
        }

        edge.setPrevOut(null);
        edge.setPrevIn(null);
        edge.setNextOut(null);
        edge.setNextIn(null);
    }

    /**
     * @param source to run bfs from.
     * @return rooted tree of the shortest distances.
     * time complex - O(n + m)
     */
    public RootedTree bfs(GraphNode source){
        // init BFS
        Queue queue = new Queue();
        queue = bfsInit(source, queue);
        RootedTree tree = new RootedTree();
        tree.insert(source);

        // for all nodes in graph
        while (queue.getSize() != 0){
            GraphNode currentNode = queue.dequeue();

            // for all nodes that are connected to currentNode
            GraphEdge currentEdge = currentNode.getOutHead();
            GraphNode currentSubNode;
            while (currentEdge != null){
                currentSubNode = currentEdge.getDst();
                if(currentSubNode.getColor() == 0){
                    // handle BFS values
                    currentSubNode.setColor(1);
                    currentSubNode.setDistance(currentNode.getDistance()+1);

                    // handle tree building
                    currentSubNode.setTreeParent(currentNode);
                    currentNode.insertChild(currentSubNode);

                    queue.enqueue(currentSubNode);
                }
                currentEdge = currentEdge.getNextOut();
            }
            currentNode.setColor(2);
        }

        return tree;
    }

    /**
     * initialise graph for BFS
     * @param source to start BFS from
     * @param queue to insert source to.
     * @return queue whit only source in it.
     * time complex - O(n)
     */
    private Queue bfsInit(GraphNode source, Queue queue){
        GraphNode currentNode = head;
        while (currentNode != null){
            if (currentNode != source){
                //reset privies BFS values
                currentNode.setColor(0);
                currentNode.setDistance(Double.POSITIVE_INFINITY);
                // reset privies tree
                currentNode.setTreeParent(null);
                currentNode.setLeftChild(null);
                currentNode.setRightSibling(null);
            }
            currentNode = currentNode.getNext();
        }
        //reset privies BFS values
        source.setColor(1);
        source.setDistance(0);

        // reset privies tree
        source.setTreeParent(null);
        source.setLeftChild(null);
        source.setRightSibling(null);

        queue.enqueue(source);
        return queue;
    }

    /**
     * returns a rooted tree of all scc in graph.
     * @return rooted tree, the root in virtual, and etch of the subtrees created by if childes are a scc.
     * time complex - O(n + m)
     */
    public RootedTree scc(){
        dfs(false);
        graphSort();
        dfs(true);
        return buildSCCTree();
    }

    /**
     * initialise graph for DFS
     * time complex - O(n)
     */
    private void dfsInit(){
        GraphNode currentNode = head;
        while (currentNode != null){
            //reset DFS values
            currentNode.setColor(0);
            currentNode.setDiscovered(-1);
            currentNode.setFinished(-1);

            //reset tree values
            currentNode.setTreeParent(null);
            currentNode.setLeftChild(null);
            currentNode.setRightSibling(null);

            currentNode = currentNode.getNext();
        }
    }

    /**
     * run DFS
     * @param isTrans to determent how to iterate on edges and when to build the tree
     * time complex - O(n+m)
     */
    public void dfs(Boolean isTrans){
        dfsInit();
        int time = 0;

        // iterate on all nodes
        GraphNode currentNode;
        if (!isTrans){currentNode = head;}
        else{currentNode = sortedHead;}
        while (currentNode != null){
            if (currentNode.getColor() == 0){
                time = dfsVisit(currentNode, time, isTrans);
            }
            if (!isTrans){currentNode = currentNode.getNext();}
            else{currentNode = currentNode.getNextSCC();}
        }
    }

    /**
     * DFS visit
     * @param source node to iterate from
     * @param time of original node
     * @param isTrans to determent how to iterate on edges and when to build the tree
     * @return new nodes time
     * time complex - O(m)
     */
    private int dfsVisit(GraphNode source, int time, Boolean isTrans){
        time++;
        source.setDiscovered(time);
        source.setColor(1);
        GraphNode currentSubNode;

        // if not trans, iterate normally
        if (!isTrans) {
            GraphEdge currentEdge = source.getOutHead();
            while (currentEdge != null) {
                currentSubNode = currentEdge.getDst();
                if (currentSubNode.getColor() == 0) {
                    // set parent value
                    currentSubNode.setTreeParent(source);
                    source.insertChild(currentSubNode);

                    // step into dfs
                    time = dfsVisit(currentSubNode, time, false);
                }
                currentEdge = currentEdge.getNextOut();
            }
        }

        // if is trans iterate in the opposite direction
        else {
            GraphEdge currentEdge = source.getInHead();
            while (currentEdge != null) {
                currentSubNode = currentEdge.getSrc();
                if (currentSubNode.getColor() == 0) {
                    // set parent value
                    currentSubNode.setTreeParent(source);
                    source.insertChild(currentSubNode);

                    // step into dfs
                    time = dfsVisit(currentSubNode, time, true);
                }
                currentEdge = currentEdge.getNextIn();
            }
        }
        source.setColor(2);
        time++;
        source.setFinished(time);
        return time;
    }

    /**
     * attach all CC elements under SCC's tree root
     * @return final tree
     * time complex - O(n)
     */
     public RootedTree buildSCCTree(){
        //init tree
        RootedTree tree = new RootedTree();
        GraphNode root = new GraphNode(0);
        tree.insert(root);

        // insert CC elements as roots children
        GraphNode currentNode = sortedHead;
        while (currentNode != null){
            if (currentNode.getTreeParent() == null){
                currentNode.setTreeParent(root);
                root.insertChild(currentNode);
            }
            currentNode = currentNode.getNextSCC();
        }
     return tree;
     }


    /**
     * change the order of the graph in decreasing order of "finished"
     * time complex - O(n)
     */
    public void graphSort(){
        // prep graph for counting sort
        GraphNode[] unsorted = new GraphNode[graphSize];
        GraphNode currentNode = head;
        int i = 0;
        while (currentNode != null){
            currentNode.setValueFiled(currentNode.getFinished());
            unsorted[i] = currentNode;
            currentNode = currentNode.getNext();
            i++;
        }

        // sort graph
        GraphNode[] sorted = countingSort(unsorted, graphSize, 2*graphSize);

        // build tree from sorted graph
        sortedHead = sorted[graphSize-1];
        sortedHead.setNextSCC(sorted[graphSize-2]);
        sortedHead.setPrevSCC(null);
        for (int j = graphSize -2 ;  j > 0; j--){
            sorted[j].setNextSCC(sorted[j-1]);
            sorted[j].setPrevSCC(sorted[j+1]);
        }
        sorted[0].setNextSCC(null);
        sorted[0].setPrevSCC(sorted[1]);
    }

    /**
     * counting sort
     * @param arr array to sort
     * @param size of array
     * @param maxVal in array
     * @return sorted arr
     * time complex O(n) - maxVal in our case is dependent on n
     */
    private GraphNode[] countingSort(GraphNode[] arr, int size, int maxVal){
        GraphNode[] B = new GraphNode[size];
        int[] C = new int[maxVal];
        for (int i = 0; i<maxVal; i++){
            C[i] = 0;
        }

        for(int j = 0; j < size; j++){
            C[arr[j].getValueFiled()-1]++;
        }

        for (int i = 1; i<maxVal; i++){
            C[i] = C[i]+ C[i-1];
        }

        for (int j = size-1; j >= 0; j--){
            B[C[arr[j].getValueFiled()-1]-1] = arr[j];
            C[arr[j].getValueFiled()-1]--;
        }
        return B;
    }

}
