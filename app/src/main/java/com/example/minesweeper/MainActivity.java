package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.widget.TextView;
import android.view.*;
import android.graphics.*;
import android.os.*;

import java.util.*;


public class MainActivity extends AppCompatActivity {

    private int clock = 0;
    private boolean axeMode = true;
    private boolean running = false;
    private final Integer COL = 8;
    private final Integer ROW = 10;
    private final Grid grid = new Grid(ROW, COL);
    private ArrayList<TextView> cell_tvs;

    private int findIndexOfCellTextView(TextView tv) {
        for (int n = 0; n < cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    private TextView findTextView(int row, int col) {
        return cell_tvs.get(row * COL + col);
    }

    private void onClickStart() {
        running = true;
    }

    private void onClickStop(View view) {
        running = false;
    }

    private void onClickClear(View view) {
        running = false;
        clock = 0;
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.textView11);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {

                int seconds = clock;
                String time = String.format("%d", seconds);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    //search all of the block that has 0 mines
    public ArrayList<Point> BFS(Block block){

        Queue<Point> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();
        ArrayList<Point> result = new ArrayList<>();
        Point p = block.getPoint();

        queue.add(p); // starting point

        while(!queue.isEmpty()){
            Point s = queue.peek();
            queue.remove(s);
            ArrayList<Point> adjBlocks = grid.getAdjBlocks(block);

            for(Point node: adjBlocks){
                if(!visited.contains(node)){
                    Block findNode = grid.findBlock(node.x, node.y);
                    int numMines = findNode.getAdjMine();
                    if(numMines == 0 && !result.contains(node)){
                        result.add(node);
                    }
                    queue.add(node);
                    visited.add(node);
                }
            }
        }
        return result;
    }

    //switch between flag and axe mode
    public void onClickMode(View view){
        TextView tv = (TextView) view;
        //flag -> axe
        if(tv.getText().toString().equals("\uD83D\uDEA9")){
            tv.setText("\u26CF");
            axeMode = true;
        }
        //axe -> flag
        else{
            tv.setText("\uD83D\uDEA9");
            axeMode = false;
        }
    }

    public void onClickTV(View view) {
        TextView tv = (TextView) view;

        int n = findIndexOfCellTextView(tv);
        int row = n / COL;
        int col = n % COL;
        Block block = grid.findBlock(row, col);


        if(axeMode && block.getAdjMine() == 0){
            //find blocks that has 0 mines
            ArrayList<Point> nodes = BFS(block);
            for(Point node: nodes){
                TextView neighbor = findTextView(node.x, node.y);
                neighbor.setText("");
                neighbor.setTextColor(Color.LTGRAY);
                neighbor.setBackgroundColor(Color.LTGRAY);
            }
        }
        //currently on axe mode and block does not have a mine
        else if(axeMode && !block.getMine()){
            String numMines = String.valueOf(block.getAdjMine());

            block.setViewed(true);
            tv.setText(numMines);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.LTGRAY);
        }
        //currently on axe mode and block has a mine
        else if(axeMode && block.getMine()){
            block.setViewed(true);
            tv.setText("\uD83D\uDCA3"); //place mine
            tv.setBackgroundColor(Color.RED);
            onClickStop(view); //stop the clock and output a message "Game over"
        }
        //currently on flag mode and the user places a flag
        else if (!axeMode && grid.placeFlag(block) && tv.getCurrentTextColor() == Color.GREEN) {
            tv.setText("\uD83D\uDEA9");
            //TextView timeView = (TextView) findViewById(R.id.textView10);
            //numFlags --; //insert logic for if flag count is less than 0
            //countFlags();
        }
        //currently on flag mode and the user wants to remove the current flag with green block
        else if(!axeMode && !grid.placeFlag(block) && tv.getText().equals("\uD83D\uDEA9")){
            tv.setText("");
        }
        //currently on axe mode and
        else if (axeMode && tv.getCurrentTextColor() == Color.GREEN) {
            tv.setTextColor(Color.LTGRAY);
            tv.setBackgroundColor(Color.LTGRAY);
        }

        onClickStart();
    }


    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_tvs = new ArrayList<TextView>();
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        LayoutInflater li = LayoutInflater.from(this);

        for (int i = 0; i < ROW; i++) { //rows
            for (int j = 0; j < COL; j++) { //columns

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("visited", false);
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);

                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams(); //getting the stats for the grid dimensions
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                cell_tvs.add(tv);
                grid.addView(tv, lp);

            }
        }

        runTimer();
    }

}

