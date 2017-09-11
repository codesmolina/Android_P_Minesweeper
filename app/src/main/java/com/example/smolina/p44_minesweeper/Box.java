package com.example.smolina.p44_minesweeper;

/**
 * Created by smolina on 4/09/17.
 */

public class Box {

    public int x, y, width;
    public int bundle = 0;
    public boolean uncovered = false;

    public void assignXY(int x, int y, int width){
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public boolean into(int X, int Y){
        if(X>=this.x && X<=this.x + width && Y >= this.y && Y<=this.y + width)
            return true;
        else
            return false;
    }

}
