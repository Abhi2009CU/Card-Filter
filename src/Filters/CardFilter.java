package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.awt.*;
import java.util.ArrayList;

public class CardFilter implements PixelFilter {
    short white = 255, black = 0;

    @Override
    public DImage processImage(DImage img) {
        DImage vImage = edge(vihanify(img));

        return vImage;
    }

    public DImage vihanify(DImage vImage){
        short[][] red = vImage.getRedChannel();
        short[][] green = vImage.getGreenChannel();
        short[][] blue = vImage.getBlueChannel();

        for (int r = 0; r < red.length; r++){
            for (int c = 0; c < red[r].length; c++) {
                if (distToWhite(red[r][c], green[r][c], blue[r][c])<15){
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
    public double distToWhite(int r, int g, int b){

        return Math.sqrt(Math.pow(255-r,2)+Math.pow(255-g,2)+Math.pow(255-b,2));
    }

    public DImage edge(DImage vImage){
        DImage edges = new DImage(vImage);
        short[][] grid = edges.getBWPixelGrid();
        short[][] newGrid = new short[grid.length][grid[0].length];
        for (int i = 1; i < edges.getHeight(); i++) {
            for (int j = 1; j < edges.getWidth(); j++) {
                int xDif = grid[i][j]-grid[i][j-1];
                int yDif = grid[i][j]-grid[i-1][j];
                if(Math.sqrt(xDif*xDif+yDif*yDif)>200) newGrid[i][j] = white;
            }
        }
        edges.setPixels(newGrid);
        return edges;
    }

    public ArrayList findCorners(DImage vImage){
        ArrayList<int[][]> corners = new ArrayList<>();

        return corners;
    }


}
