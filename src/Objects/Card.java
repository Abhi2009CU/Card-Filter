package Objects;

import core.DImage;

public class Card {
    int upperX, upperY, lowerX, lowerY;
    DImage cardImage;
    int numShapes;
    int shapeType; // 0 = peanut, 1 = oval, 2 = diamond
    int color; // 0 = orange, 1 = green, 2 = purple
    int shading; // 0 = solid, 1 = striped, 2 = open
    public Card(int upperX, int upperY, int lowerX, int lowerY, DImage cardImage, int numShapes, int shapeType, int color, int shading){
        this.cardImage = cardImage;
        this.numShapes = numShapes;
        this.shapeType = shapeType;
        this.color = color;
        this.shading = shading;
        this.upperX = upperX;
        this.upperY = upperY;
        this.lowerX = lowerX;
        this.lowerY = lowerY;
    }

    public DImage getCardImage() {
        return cardImage;
    }

    public int getNumShapes() {
        return numShapes;
    }

    public int getShapeType() {
        return shapeType;
    }

    public int getColor() {
        return color;
    }

    public int getShading() {
        return shading;
    }
}
