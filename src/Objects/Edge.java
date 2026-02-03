package Objects;

import java.util.ArrayList;

public class Edge {
    ArrayList<int[]> edge;

    public Edge(){
        edge = new ArrayList<>();
    }
    public Edge(ArrayList<int[]> edge){
        this.edge = edge;
    }

    public void addEdgePoint(int[] e) {
        edge.add(e);
    }

    public ArrayList<int[]> getEdgePoints() {
        return edge;
    }

    private boolean checkIfEdgeMakesCardShape() {
        for (int i = 0; i < edge.size(); i++) {
            int[] currEdge = edge.get(i);
            for (int j = i + 1; j < edge.size(); j++) {
                int[] compareEdge = edge.get(j);
                int distX = Math.abs(currEdge[0] - compareEdge[0]);
                int distY = Math.abs(currEdge[1] - compareEdge[1]);
                if (distX < 50 && distY < 50){
//old and abandoned :(
                }
                return false;
            }
        }
        return true;
    }
    public boolean containsPoint(int r, int c){
        for (int i = 0; i < edge.size(); i++) {
            int[] point = edge.get(i);
            if(point[0] == r && point[1] == c) return true;
        }
        return false;
    }
}
