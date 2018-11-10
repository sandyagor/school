package com.sandyagor.ball.beans;

public class Line {
    private Coordinate xCoord;
    private Coordinate yCoord;

    public Coordinate getxCoord() {
        return xCoord;
    }

    public void setxCoord(Coordinate xCoord) {
        this.xCoord = xCoord;
    }

    public Coordinate getyCoord() {
        return yCoord;
    }

    public void setyCoord(Coordinate yCoord) {
        this.yCoord = yCoord;
    }

    @Override
    public String toString() {
        return xCoord.toString()+"-->"+yCoord.toString();
    }
    public Line(){};
    public Line(Coordinate xCoord,Coordinate yCoord){
        this.xCoord=xCoord;
        this.yCoord=yCoord;
    }

}
