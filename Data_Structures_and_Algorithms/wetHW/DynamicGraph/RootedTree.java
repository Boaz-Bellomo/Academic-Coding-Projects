import java.io.DataOutputStream;
import java.io.IOException;

public class RootedTree {
    GraphNode root;
    private String downLine =  System.lineSeparator();

    /**
     * default constructor
     * creates an empty tree with no nodes
     * time complex - O(1)
     */
    public RootedTree(){
        root= null;
    }

    public void insert(GraphNode node) {
        if (node.getTreeParent() == null) {
            this.root = node;
        } else if (node.getTreeParent().getLeftChild() == null) {
            node.getTreeParent().setLeftChild(node);
        } else {
            node.setRightSibling(node.getTreeParent().getLeftChild());
            node.getTreeParent().setLeftChild(node);
        }
    }

    /**
     * prints all keys of nodes in tree to stream.
     * different layers will be printed in different lines s.t. layer i prints in line i+1.
     * keys will be printed from left to right, separated by ',' except for the last one.
     * @param out stream to print to.
     * time complex - O(k)
     */
    public void printByLayer(DataOutputStream out) throws IOException {
        Queue queue = new Queue();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            int size = queue.getSize();

            for (int i = 0; i < size; i++) {
                GraphNode node = queue.dequeue();
                out.writeBytes(node.toString());
                if(i < size - 1) {
                    out.writeBytes(",");
                }

                // Enqueue the left child and its siblings
                GraphNode sibling = node.getLeftChild();
                while (sibling != null) {
                    queue.enqueue(sibling);
                    sibling = sibling.getRightSibling();
                }
            }
            if (!queue.isEmpty()){
                out.writeBytes(downLine);
            }
        }
        out.flush();
    }


    /**
     * prints all keys of nodes in tree to stream in preorder.
     * keys will be separated by ',' except for the last one, all in a single line.
     * @param out stream to print to.
     * time complex - O(k)
     */
    public void preorderPrint(DataOutputStream out) throws IOException {
        recursivePreorder(root, out);
    }

    /**
     * implements preorder recursion
     * @param node subtree root
     * @param out stream to print to
     * time complex - O(k)
     */
    public void recursivePreorder(GraphNode node, DataOutputStream out) throws IOException {
        if (node == null) {
            return;
        }
        out.writeBytes(node.toString());
        if (node.getLeftChild() != null){
            out.writeBytes(",");
            recursivePreorder(node.getLeftChild(), out);
        }
        if(node.getRightSibling() != null){
            out.writeBytes(",");
            recursivePreorder(node.getRightSibling(),out);
        }
    }
}
