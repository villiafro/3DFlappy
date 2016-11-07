package com.mygdx.graphics;

/**
 * Created by VilhjalmurAlex on 02/11/2016.
 */
public class Cell {
    boolean upperRight;
    boolean upperLeft;
    boolean downerRight;
    boolean downerLeft;

    public Cell(){
        upperRight = true;
        upperLeft = true;
        downerRight = true;
        downerLeft = true;
    }

    /**
     * ets each cell if it is allowed to draw a wall in the right place
     * @param upR the upper right wall
     * @param upL the upper left wall
     * @param doR the downer right wall
     * @param doL the downer left wall
     */
    public void setCell(boolean upR, boolean upL, boolean doR, boolean doL){
        upperRight = upR;
        upperLeft = upL;
        downerRight = doR;
        downerLeft = doL;
    }

    public boolean isUpperRight(){
        return upperRight;
    }
    public boolean isUpperLeft(){
        return upperLeft;
    }
    public boolean isDownerRight(){
        return downerRight;
    }
    public boolean isDownerLeft(){
        return  downerLeft;
    }
}
