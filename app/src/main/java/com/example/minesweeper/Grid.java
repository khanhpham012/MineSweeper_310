package com.example.minesweeper;

import android.graphics.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Grid {
    private final ArrayList<Block> blocks;
    private ArrayList<Point> randomFour;
    private final int COL;
    private final int ROW;
    private int FLAGS = 4;

    public Grid(int ROW, int COL){
        this.ROW = ROW;
        this.COL = COL;
        blocks = new ArrayList<>();
        for(int i = 0; i < ROW; i++){
            for(int j = 0; j < COL; j++){
                Point point = new Point(i,j);
                Block newBlock = new Block(point); //initialize all new Block with empty value
                blocks.add(newBlock);
            }
        }
        this.placeMine();
    }

    public Block getBlock(int index){
        return blocks.get(index);
    }

    public Block findBlock(int row, int col){
        return blocks.get(row * COL + col);
    }

    public void placeMine(){
        Random rand = new Random();
        randomFour = new ArrayList<>();

        //Randomly create locations of four mines
        for (int i = 0; i < 4; i++) {
            int randomRow = rand.nextInt(10);
            int randomCol = rand.nextInt(8);
            Point node = new Point();
            node.set(randomRow, randomCol);
            while (randomFour.contains(node)){
                randomRow = rand.nextInt(10);
                randomCol = rand.nextInt(8);
                node.set(randomRow,randomCol);
            }
            randomFour.add(node);
        }

        //place the bomb into the grid
        for(Point mine: randomFour){
            Block block_mine = findBlock(mine.x, mine.y);
            block_mine.setMine(true);
        }

        setAdjacentMine();
    }
    public boolean placeFlag(Block b){
        if(FLAGS != 0){
            b.setFlagged(true);
            FLAGS--;
            return true;
        }
        return false;
    }
    //return a list of adjacent block
    public ArrayList<Block> getAdjBlocks(Block block) {
        Point p = block.getPoint(); //p.x -> row, p.y -> col
        ArrayList<Block> adjBlocks = new ArrayList<>();

        //Left Block
        if (p.x - 1 >= 0 && p.y - 1 >= 0) {
            Block aboveLeft = findBlock( p.x - 1, p.y - 1);
            adjBlocks.add(aboveLeft);
        }
        if (p.y - 1 >= 0) {
            Block aboveCenter = findBlock(p.x,p.y - 1 );
            adjBlocks.add(aboveCenter);
        }
        if (p.x + 1 < ROW && p.y - 1 >= 0) {
            Block aboveRight = findBlock(p.x + 1, p.y - 1);
            adjBlocks.add(aboveRight);
        }

        //Center Block
        if(p.x - 1 >= 0) {
            Block centerLeft = findBlock(p.x - 1, p.y);
            adjBlocks.add(centerLeft);
        }
        if(p.x + 1 < ROW){
            Block centerRight = findBlock(p.x + 1, p.y);
            adjBlocks.add(centerRight);
        }

        //Right Block
        if(p.x - 1 >= 0 && p.y + 1 < COL){
            Block belowLeft = findBlock(p.x - 1, p.y + 1);
            adjBlocks.add(belowLeft);
        }
        if(p.y + 1 < COL){
            Block belowCenter = findBlock(p.x, p.y + 1);
            adjBlocks.add(belowCenter);
        }
        if(p.x + 1 < ROW && p.y + 1 < COL){
            Block belowRight = findBlock(p.x + 1, p.y + 1);
            adjBlocks.add(belowRight);
        }

        return adjBlocks;
    }
    //return the number of mine at a block
    public void setAdjacentMine(){
        for(Block block: blocks){
            Point p = block.getPoint();
            int count = 0;

            //aboveLeft
            Point aboveLeft = new Point(p.x - 1 , p.y - 1);
            if(aboveLeft.x >= 0 && aboveLeft.y >= 0 && randomFour.contains(aboveLeft)){
                count++;
            }

            //aboveCenter
            Point aboveCenter = new Point(p.x, p.y - 1);
            if(aboveCenter.y >= 0 && randomFour.contains(aboveCenter)){
                count++;
            }

            //aboveRight
            Point aboveRight = new Point(p.x + 1, p.y - 1);
            if(aboveRight.x < ROW && aboveRight.y >= 0 && randomFour.contains(aboveRight)){
                count++;
            }

            //centerLeft;
            Point centerLeft = new Point(p.x - 1, p.y);
            if(centerLeft.x >= 0 && randomFour.contains(centerLeft)){
                count++;
            }

            //centerRight;
            Point centerRight = new Point(p.x + 1, p.y);
            if(centerRight.x < ROW && randomFour.contains(centerRight)){
                count++;
            }

            //belowLeft
            Point belowLeft = new Point(p.x - 1, p.y + 1);
            if(belowLeft.x >= 0 && belowLeft.y < COL && randomFour.contains(belowLeft)){
                count++;
            }

            //belowCenter
            Point belowCenter = new Point(p.x, p.y + 1);
            if(belowCenter.y < COL && randomFour.contains(belowCenter)){
                count++;
            }

            //belowRight
            Point belowRight = new Point(p.x + 1, p.y + 1);
            if(belowRight.x < ROW && belowRight.y < COL && randomFour.contains(belowRight)){
                count++;
            }

            block.setAdjMine(count);
        }
    }
}
