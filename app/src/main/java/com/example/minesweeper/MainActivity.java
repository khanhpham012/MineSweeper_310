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
    public void BFS(Block block){

        Queue<Block> queue = new LinkedList<>();
        Set<Block> visited = new HashSet<>();
        ArrayList<Block> adjBlocks = new ArrayList<>();

        queue.add(block); // starting point

        while(!queue.isEmpty()){
            Block s = queue.peek();
            queue.remove(s);
            ArrayList<Block> testBlocks = grid.getAdjBlocks(s);
            if(visited.contains(s)){
                continue;
            }
            visited.add(s);
            for(Block adjBlock: testBlocks){
                if(!visited.contains(adjBlock)){
                    if(!adjBlocks.contains(adjBlock)){
                        if(adjBlock.getAdjMine() == 0){
                            queue.add(adjBlock);
                        }
                        adjBlocks.add(adjBlock);
                    }
                }
            }
            System.out.print("SIZE queue: " + queue.size());
        }

        for(Block adjBlock: adjBlocks){

            Point p = adjBlock.getPoint();
            int row_p = p.x;
            int col_p = p.y;
            TextView neighbor = cell_tvs.get(row_p * COL + col_p);

            adjBlock.setViewed(true);
            neighbor.setText(String.valueOf(adjBlock.getAdjMine()));
            neighbor.setTextColor(Color.BLACK);
            neighbor.setBackgroundColor(Color.LTGRAY);
        }
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

        //BFS
        if(axeMode && block.getAdjMine() == 0 && !tv.getText().equals("\uD83D\uDEA9")){
            tv.setText("");
            tv.setTextColor(Color.LTGRAY);
            tv.setBackgroundColor(Color.LTGRAY);
            BFS(block);
        }
        //currently on axe mode and block does not have a mine
        if(axeMode && !block.getMine() && !tv.getText().equals("\uD83D\uDEA9")){
            String numMines = String.valueOf(block.getAdjMine());

            block.setViewed(true);
            tv.setText(numMines);
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.LTGRAY);
        }
        //currently on axe mode and block has a mine
        else if(axeMode && block.getMine() && !tv.getText().equals("\uD83D\uDEA9")){
            block.setViewed(true);
            tv.setText("\uD83D\uDCA3"); //place mine
            tv.setBackgroundColor(Color.RED);
            onClickStop(view); //stop the clock and output a message "Game over"
        }
        //currently on flag mode and the user wants to remove the current flag with green block
        else if(!axeMode  && tv.getText().equals("\uD83D\uDEA9")){ //&& !grid.placeFlag(block)
            tv.setText("");
        }
        //currently on flag mode and the user places a flag
        else if (!axeMode  && tv.getCurrentTextColor() == Color.GREEN) { //&& grid.placeFlag(block)
            tv.setText("\uD83D\uDEA9");
            //TextView timeView = (TextView) findViewById(R.id.textView10);
            //numFlags --; //insert logic for if flag count is less than 0
            //countFlags();
        }
        //currently on axe mode and
        else if (!axeMode && tv.getCurrentTextColor() == Color.GREEN && tv.getText().equals("\uD83D\uDEA9")) {
            tv.setText("");
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

