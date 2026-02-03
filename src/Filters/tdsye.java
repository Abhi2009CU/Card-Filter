package Filters;

import Interfaces.Drawable;
import Interfaces.PixelFilter;
import Objects.Edge;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

public class tdsye implements PixelFilter, Drawable {
    short white = 255, black = 0;
    ArrayList<int[]> corners = new ArrayList<>();

    @Override
    public DImage processImage(DImage img) {
        DImage vImage = (vihanify(img));
        //take the innermost part of the card trace and remove all the pixels inside of it so we take out the shape
        //findEdges(vImage);
        return vImage;
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
        boolean foundPoint = false;
        do {
            foundPoint = false;
            visited[curRow][curCol] = 1;
            for (int i = 0; i < 8; i++) {
                int stepR = rotR[(sDir+i)%8];
                int stepC = rotC[(sDir+i)%8];
                if(grid[curRow+stepR][curCol+stepC] == white && visited[curRow+stepR][curCol+stepC] != 1){
                    points.add(new int[]{curRow+stepR, curCol+stepC});
                    curRow += stepR;
                    curCol += stepC;
                    sDir = (i+4+sDir)%8;
                    foundPoint = true;
                    break;
                }
            }

//            outer:
//            for (int tickR = 1; tickR <= 4; tickR++) {
//                for (int tickC = 1; tickC <= 4; tickC++) {
//                    int midR = tickR%2==0 ? tickR : -tickR;
//                    int midC = tickC%2==0 ? tickC : -tickC;
//                    int stepR = midR<2 ? midR/Math.abs(midR) : midR/Math.abs(midR)*2;
//                    int stepC = midC<2 ? midC/Math.abs(midC) : midC/Math.abs(midC)*2;
//                    if(grid[curRow+stepR][curCol+stepC] == white){
//                        points.add(new int[]{curRow+stepR, curCol+stepC});
//                        grid[curRow+stepR][curCol+stepC] = 127;
//                        curRow += stepR;
//                        curCol += stepC;
//                        break outer;
//                    }
//                }
//            }
        } while (!(r==curRow && c==curCol) && foundPoint);
        for (int i = 0; i < points.size(); i++) {
            grid[points.get(i)[0]][points.get(i)[1]] = 127;
        }
        return points;
    }

    public void findEdges(DImage vImage) {
        short[][] grid = vImage.getBWPixelGrid();
        ArrayList<Edge> edges = new ArrayList<>();
        for (int r = 0; r < vImage.getHeight(); r++) {
            for (int c = 0; c < vImage.getWidth(); c++) {
                if(grid[r][c] == white){
                    System.out.println(r+ ", "+c);
                    ArrayList<int[]> edge = traceEdge(r, c, vImage);
                    edges.add(new Edge(edge));
                    grid[r][c]=127;
                }
            }
        }
    }


    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        for (int[] corner : corners){
            window.ellipse(corner[0], corner[1], 10, 10);
        }
    }
}
