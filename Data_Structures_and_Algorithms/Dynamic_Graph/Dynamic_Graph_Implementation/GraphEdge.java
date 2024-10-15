public class GraphEdge {

    private final GraphNode src;
    private GraphEdge prevIn;
    private GraphEdge nextIn;
    private final GraphNode dst;
    private GraphEdge nextOut;
    private GraphEdge prevOut;

    public GraphEdge(GraphNode src, GraphNode dst) {
        this.src = src;
        this.dst = dst;
    }

    public GraphEdge getPrevIn() {return prevIn;}

    public void setPrevIn(GraphEdge prevIn) {this.prevIn = prevIn;}

    public GraphEdge getNextIn() {return nextIn;}

    public void setNextIn(GraphEdge nextIn) {this.nextIn = nextIn;}

    public GraphEdge getNextOut() {return nextOut;}

    public void setNextOut(GraphEdge nextOut) {this.nextOut = nextOut;}

    public GraphEdge getPrevOut() {return prevOut;}

    public void setPrevOut(GraphEdge prevOut) {this.prevOut = prevOut;}

    public GraphNode getSrc() {return src;}

    public GraphNode getDst() {return dst;}

    @Override
    public String toString() {
        return ("(" + src.getKey() + "," + dst.getKey() + ")");
    }
}

