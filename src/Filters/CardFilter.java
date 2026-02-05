package Filters;

import Interfaces.Drawable;
import Interfaces.PixelFilter;
import Objects.Card;
import Objects.Edge;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;

public class CardFilter implements PixelFilter, Drawable {
    short white = 255, black = 0;
    ArrayList<int[]> corners = new ArrayList<>();

    @Override
    public DImage processImage(DImage img) {
        DImage vImage = edge(vihanify(img));
//take the innermost part of the card trace and remove all the pixels inside of it so we take out the shape
        ArrayList<Edge> cardEdges = findEdges(vImage);
        thing(cardEdges.get(0), img);
        return vImage;
    }

    public DImage makePink(DImage img, ArrayList<Edge> cardEdges){
        return img;
//        short[][] red = img.getRedChannel();
//        short[][] green = img.getGreenChannel();
//        short[][] blue = img.getBlueChannel();
//        for (Edge edge : cardEdges) {
//            ArrayList<int[]> points = edge.getEdgePoints();
//            for (int[] point : points) {
//                red[point[0]][point[1]] = 255;
//                green[point[0]][point[1]] = 0;
//                blue[point[0]][point[1]] = 255;
//            }
//        }
//        img.setColorChannels(red, green, blue);
//        return img;
    }

    public DImage vihanify(DImage vImage){
        short[][] red = vImage.getRedChannel();
        short[][] green = vImage.getGreenChannel();
        short[][] blue = vImage.getBlueChannel();

        for (int r = 0; r < red.length; r++){
            for (int c = 0; c < red[r].length; c++) {
                if (red[r][c] + green[r][c] + blue[r][c] > 600){

                    red[r][c] = white;
                    green[r][c] = white;
                    blue[r][c] = white;
                }else{
                    red[r][c] = black;
                    green[r][c] = black;
                    blue[r][c] = black;
                }
            }
        }
        vImage.setColorChannels(red, green, blue);
        return vImage;
    }

//    public Card detectAttrs(int upperLeftX, int upperLeftY, int lowerRightX, int lowerRightY, DImage img, Edge edge){
//        int[][] imgGrid = img.getColorPixelGrid();
//        int[][] subImage = new int[lowerRightY-upperLeftY][lowerRightX-upperLeftX];
//        for (int r = upperLeftY; r < lowerRightY; r++) {
//            for (int c = upperLeftX; c < lowerRightX; c++) {
//                subImage[r-upperLeftY][c-upperLeftX] = imgGrid[r][c];
//            }
//        }
//        DImage cardImage = new DImage(upperLeftX-lowerRightX, upperLeftY-lowerRightY);
//        cardImage.setPixels(subImage);
//        //restrict image to make background fully white by stepping inward
//        short[][] bwSubImage = cardImage.getBWPixelGrid();
//        outer:
//        while(true){
//            for (int step = 0; step < subImage[0].length/2-1; step++) {
//                if(bwSubImage[step][step] > 230 && )
//            }
//        }
//
//        int numShapes = 0;
//        int shapeType = 0;
//        int color = 0;
//        int shading = 0;
//        return new Card(upperLeftX, upperLeftY, lowerRightX, lowerRightY, cardImage, numShapes, shapeType, color, shading);
//    }

    public DImage edge(DImage vImage){
        DImage edges = new DImage(vImage);
        short[][] grid = edges.getBWPixelGrid();
        short[][] newGrid = new short[grid.length][grid[0].length];
        for (int i = 1; i < edges.getHeight(); i++) {
            for (int j = 1; j < edges.getWidth(); j++) {
                int xDif = grid[i][j]-grid[i][j-1];
                int yDif = grid[i][j]-grid[i-1][j];
                if(Math.sqrt(xDif*xDif+yDif*yDif)>250) newGrid[i][j] = white;
            }
        }
        edges.setPixels(newGrid);
        return edges;
    }

    public ArrayList<int[]> traceEdge(int r, int c, DImage vImage) {
        short[][] grid = vImage.getBWPixelGrid();
        short[][] visited = new short[grid.length][grid[0].length];
        ArrayList<int[]> points = new ArrayList<>();
        int curRow = r, curCol = c;
        int[] rotR = {-1,-1, 0, 1, 1, 1, 0,-1};
        int[] rotC = { 0, 1, 1, 1, 0,-1,-1,-1};
        int sDir = 0;
        outer:
        do {
            visited[curRow][curCol] = 1;
            for (int i = 0; i < 9; i++) {
                int stepR = rotR[(sDir + i) % 8];
                int stepC = rotC[(sDir + i) % 8];
                if(i==8){
                    break outer;
                }
                if (curRow + stepR >= 0 && curRow + stepR < grid.length &&
                        curCol + stepC >= 0 && curCol + stepC < grid[0].length &&
                        grid[curRow + stepR][curCol + stepC] == white &&
                        visited[curRow + stepR][curCol + stepC] != 1) {
                    points.add(new int[]{curRow + stepR, curCol + stepC});
                    //System.out.println(curRow +" "+ curCol + ", " + (stepR +" "+ stepC));
                    visited[curRow][curCol] = 1;
                    curRow += stepR;
                    curCol += stepC;
                    sDir = (i + 4 + sDir) % 8;
                    break;
                }
            }
        } while (!(r == curRow && c == curCol));

        for (int i = 0; i < points.size(); i++) {
            grid[points.get(i)[0]][points.get(i)[1]] = 127;
        }
        vImage.setPixels(grid);
        if(points.size()>300) {
            System.out.println(points.size() + "list length");
            return points;
        } else {
            return new ArrayList<>();
        }
    }

    public ArrayList<Edge> findEdges(DImage vImage) {
        short[][] grid = vImage.getBWPixelGrid();
        ArrayList<Edge> edges = new ArrayList<>();
        for (int r = 0; r < vImage.getHeight(); r++) {
            for (int c = 0; c < vImage.getWidth(); c++) {
                if(grid[r][c] == white){
//                    System.out.println(r+ ", "+c);
                    if(edges.isEmpty()) edges.add(new Edge(traceEdge(r, c, vImage)));
                    else if (!pointAlreadyFound(edges, r, c)){
                         Edge curEdge = new Edge(traceEdge(r, c, vImage));
                        if(curEdge.size()>0)  edges.add(curEdge);
                    }
                    grid[r][c]=127;
                }
            }
        }
        System.out.println("total edges: " + edges.size());
        ArrayList<Edge> outCards = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int len = 0, index = 0;
            for (Edge edge:edges) {
                if(edge.size() > len){
                    len = edge.size();
                    index = edges.indexOf(edge);
                }
            }
            outCards.add(new Edge(edges.get(index).getEdgePoints()));
            edges.remove(index);
        }
        System.out.println("card edges: " + outCards.size());
        for (int i = 0; i < outCards.size(); i++) {
            System.out.println(outCards.get(i).size());
        }
        return outCards;
    }

    public void thing (Edge edge /*given edges of 12 cards that may be slanted*/,  double removeparam) {
        ArrayList<int[]> points = edge.getEdgePoints();
        ArrayList<int[]> currCorhners = new ArrayList<>();
        int lowRow = Integer.MAX_VALUE;
        int lowRowCol = Integer.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
//            //get point with lowest row with the lowest col
//            if(points.get(i)[0] < lowRow){
//                lowRow = points.get(i)[0];
//                lowRowCol = points.get(i)[1];
//            System.out.println(lowRow + ", " + lowRowCol);
//        }
            if (points.get(i)[0] < lowRow) {
                lowRow = points.get(i)[0];
                lowRowCol = points.get(i)[1];
            } else if (points.get(i)[0] == lowRow) {
                if (points.get(i)[1] < lowRowCol) {
                    lowRowCol = points.get(i)[1];
                }
            }
    }}

    public void thing(Edge edge, DImage img){
        ArrayList<int[]> points = edge.getEdgePoints();
        //find the largest difference between the edges and add that coordinate to coners arraylist.
        double maxDist = 0;
        for (int r = 0; r < points.size(); r++) {
            for (int i = 0; i < points.size(); i++) {
                double distance = Math.sqrt(Math.pow(points.get(r)[0] - points.get(i)[0], 2) + Math.pow(points.get(r)[1] - points.get(i)[1], 2));
                if (distance > maxDist) {
                    maxDist = distance;
                    //implement
//                    if (!corners.contains(points.get(r))) corners.add(points.get(r));
//                    if (!corners.contains(points.get(i))) corners.add(points.get(i));
                }
            }
        }
    }

     public boolean pointAlreadyFound(ArrayList<Edge> edges, int r, int c) {
         for(Edge edge : edges){
                 if(edge.containsPoint(r, c)) return true;
         }
         return false;
     }


    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        for (int[] corner : corners){
            window.ellipse(corner[1], corner[0], 10, 10);
        }
    }
}
