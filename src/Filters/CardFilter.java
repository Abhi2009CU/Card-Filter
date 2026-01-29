package Filters;

import Interfaces.Drawable;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

public class CardFilter implements PixelFilter, Drawable {
    short white = 255, black = 0;
    ArrayList<int[]> corners = new ArrayList<>();

    @Override
    public DImage processImage(DImage img) {
        DImage vImage = edge(vihanify(img));
        corners = findCorners(vImage);
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
                if(Math.sqrt(xDif*xDif+yDif*yDif)>200) newGrid[i][j] = white;
            }
        }
        edges.setPixels(newGrid);
        return edges;
    }

    public ArrayList<int[]> findCorners(DImage vImage) {
        short[][] image = vImage.getBWPixelGrid();

        ArrayList<int[]> points = new ArrayList<>();

        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[r].length; c++) {
                if (Math.abs(image[r][c] - white) < 20) {

                }
            }
        }

        return new ArrayList<>();

    }

    public ArrayList<int[]> findCorners(DImage vImage, int num){
        short[][] bwpic = vImage.getBWPixelGrid();
        ArrayList<int[]> lefttopcorners = new ArrayList<>();

        for (int r = 0; r < bwpic.length; r++) {
            for (int c = 0; c < bwpic[r].length; c++) {
                if (Math.abs(bwpic[r][c] - white) < 20
                        && Math.abs(bwpic[r][c-1] - white) > 20
                        && Math.abs(bwpic[r][c+1] - white) < 20
                        && Math.abs(bwpic[r+1][c] - white) < 20
                        && Math.abs(bwpic[r-1][c] - white) > 20
                ){
                    lefttopcorners.add(new int[]{c,r});
                }
            }
        }
        return lefttopcorners;
    }


    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        for (int[] corner : corners){
            window.ellipse(corner[0], corner[1], 10, 10);
        }
    }
}
