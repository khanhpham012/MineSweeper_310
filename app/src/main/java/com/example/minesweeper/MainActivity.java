package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.widget.TextView;
import android.view.*;
import android.graphics.*;
import android.os.*;
import java.util.*;
import java.util.ArrayDeque;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private int clock = 0;
    private boolean isAxe = true;
    private boolean running = false;
    private final Integer numFlag = 4;
    private final Integer COL = 8;
    private final Integer ROW = 10;

    private ArrayList<Point> randomFour = RandMine.getList();
    private Grid grid = new Grid(COL, ROW);
    private ArrayList<TextView> cell_tvs;
    private int findIndexOfCellTextView(TextView tv) {
        for (int n = 0; n < cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
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
            ArrayList<Point> adjNode = grid.adjacent(block);

            for(Point node: adjNode){
                if(!visited.contains(node)){


                    int countMine = countMine(node);
                    if(countMine == 0 && !result.contains(node)){
                        result.add(node);
                    }
                    queue.add(node);
                    visited.add(node);
                }
            }
        }
        return result;
    }

    //breadth first search for the empty cells
    //toggle between the flags to put flags - pickaxe to flag
    //flags need to be 4 and decrement and increments
    // Start as green
    public void onClickAxeFlag(View view){ //switch flag and axe when clicked
        TextView tv = (TextView) view;
        if (tv.getText().toString().equals("⛏")){
            System.out.println("clicked");
            tv.setText("\uD83D\uDEA9"); //set to flag
            isAxe = false;
        }
        else { //if flag, set to pick
            tv.setText("⛏"); //set to axe
            isAxe = true;
        }
    }

    public void onClickTV(View view) {
        TextView tv = (TextView) view;
        grid.placeMine();

        int n = findIndexOfCellTextView(tv);
        System.out.println("n: " + n);

        //tv.setText(String.valueOf(i) + String.valueOf(j));
        //tv.setText(getString(R.string.mine));

        System.out.println(randomFour);

        if(grid.getBlock(n).getMine() == true){
            System.out.println("bomb");
            tv.setText("\uD83D\uDCA3"); //place mine
            tv.setBackgroundColor(Color.RED);
            //trigger game over message
        }
        else if (tv.getCurrentTextColor() == Color.GRAY) {
            tv.setTextColor(Color.GREEN);
            tv.setBackgroundColor(Color.parseColor("lime"));
//            if(hints.get(n) != null ){
//                System.out.println("number");
//              //  tv.setText((Integer) hints.get(n));
//                tv.setTextColor(Color.WHITE);
//            }
        } else { //if not mine
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.LTGRAY);
        }
        //if(randomFour.contains(n+1)) //think about edge cases


        //else if is mine -> put mine and game over

        onClickStart();
        /**
        ArrayList<Point> hints = BFS(,)

        int n = findIndexOfCellTextView(tv);

        System.out.println(randomFour);
        //System.out.println("preloop: " + hints.containsKey(n));

        if(hints.containsKey(n) && hints.get(n) != null && isAxe == true){ //reveal box if axe is true
            String number = hints.get(n).toString();
            tv.setText(number);
            tv.setTextColor(Color.WHITE);

            tv.setBackgroundColor(Color.LTGRAY);
        }
        if(randomFour.contains(n) && isAxe == true){ //if lands on a mine and has an axe
            System.out.println("bomb");
            tv.setText("\uD83D\uDCA3"); //place mine
            tv.setBackgroundColor(Color.RED);
            //trigger game over message
        }
        else if(isAxe== true && tv.getText().toString() == "\uD83D\uDEA9"){ //if there is already a flag, get off the flag
            tv.setText("");
            flagCount +=1;
        }
        else if (tv.getCurrentTextColor() == Color.GREEN && isAxe == true) { //unveiling when axe is true
            tv.setTextColor(Color.LTGRAY);
            tv.setBackgroundColor(Color.LTGRAY);

        }
        else if (tv.getCurrentTextColor() == Color.GREEN && isAxe == false) { //placing flags
            tv.setText("\uD83D\uDEA9");
            flagCount-=1; //insert logic for if flag count is less than 0
            //   countFlags();
            findViewById(R.id.textView10);


        }

        onClickStart();
         **/
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

        //calcHints();
        runTimer();
    }

    public void onClickEmptyFlag(View view){
        TextView tv = (TextView) view;
        if (tv.getText().toString().equals("")){
            System.out.println("clicked");
            tv.setText("\uD83D\uDEA9"); //set to flag
            isAxe = false;
        }
        else { //if flag, set to pick
            tv.setText("⛏"); //set to axe
            isAxe = true;
        }
    }




}

