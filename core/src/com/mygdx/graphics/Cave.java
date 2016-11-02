package com.mygdx.graphics;

/**
 * Created by VilhjalmurAlex on 02/11/2016.
 */
public class Cave {
    private Cell[] cells;

    public Cave(){
        cells = new Cell[10];
        for(int i = 0; i < 10; i++){
            cells[i] = new Cell();
        }
    }

    public Cell[] getCells(){
        return cells;
    }
}
