package com.example.minesweeper;

import android.graphics.Point;

public class Block {
    public static final int empty = 0;

    private int adjMine;
    private final Point point;
    private boolean isViewed; //when user already view that block
    private boolean isFlagged;
    private boolean mine;


    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {this.isViewed = viewed;}

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        this.isFlagged = flagged;
    }

    public boolean getMine() {return mine;}

    public void setMine(boolean mine){this.mine = mine;}

    public Point getPoint(){return point;}

    public void setAdjMine(int count){adjMine = count;}

    public int getAdjMine(){return adjMine;}

    public Block (Point point){
        adjMine = empty;
        this.point = point;
        this.mine = false;
        this.isViewed = false;
        this.isFlagged = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Block)) return false;

        Block b = (Block) o;
        return b.point.equals(point);
    }
}
