package com.example.smolina.p44_minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private Board background;
    int x, y;
    private Box[][] boxes;
    private boolean on = true;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout1 = (LinearLayout) findViewById(R.id.layout1);
        background = new Board(this);
        background.setOnTouchListener(this);
        layout1.addView(background);
        boxes = new Box[8][8];
        for(int row = 0; row<8; row++){
            for(int column = 0; column<8; column++){
                boxes[row][column] = new Box();
            }
        }
        this.putBombs();
        this.countBombsPerimeter();

        toast = Toast.makeText(this, "((((..Boooooooooommmm.....))))", Toast.LENGTH_LONG);

    }

    public void restart(View view){
        boxes = new Box[8][8];
        for(int row = 0; row<8; row++){
            for(int column = 0; column<8; column++){
                boxes[row][column] = new Box();
            }
        }
        this.putBombs();
        this.countBombsPerimeter();
        this.on = true;
        toast(false);
        background.invalidate();
    }

    protected void toast(boolean on) {
        if(on == true)
            toast.show();
        else
            toast.cancel();
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (on)
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    if (boxes[r][c].into((int) event.getX(), (int) event.getY())) {
                        boxes[r][c].uncovered = true;
                        if (boxes[r][c].bundle == 80) {
                            toast(true);
                            on = false;
                        } else if (boxes[r][c].bundle == 0)
                            process(r, c);
                        background.invalidate();
                    }
                }
            }
        if(won() && on){
            Toast.makeText(this, "You win... \nCONGRATULATION.", Toast.LENGTH_LONG).show();
            on = false;
        }
        return true;
    }


    class Board extends View{

        public Board(Context context){
            super(context);
        }

        protected void onDraw(Canvas canvas){
            canvas.drawRGB(0, 0, 0);
            int width = 0;
            if(canvas.getWidth() < canvas.getHeight())
                width = background.getWidth();
            else
                width = background.getWidth();
            int qWidth = width / 8;
            Paint paint = new Paint();
            paint.setTextSize(50);
            Paint paint2 = new Paint();
            paint2.setTextSize(50);

            paint2.setTypeface(Typeface.DEFAULT_BOLD);
            paint2.setARGB(255, 0, 105, 235);

            int currentLine = 0;

            for (int r = 0; r<8; r++) {
                for (int c = 0; c < 8; c++) {
                    boxes[r][c].assignXY(c * qWidth, currentLine, qWidth);
                    if(boxes[r][c].uncovered == false)
                        paint.setARGB(146, 30, 173, 194);
                    else
                        paint.setARGB(215, 222, 215, 186);
                    canvas.drawRect(c * qWidth, currentLine, c * qWidth + qWidth - 3, currentLine + qWidth - 3, paint);

                    if ( boxes[r][c].bundle>=1 && boxes[r][c].bundle<=8 && boxes[r][c].uncovered )
                        canvas.drawText(String.valueOf(boxes[r][c].bundle), c * qWidth + (qWidth/2) - 12, currentLine + qWidth / 2 + 10, paint2);
                    if(boxes[r][c].bundle == 80 && boxes[r][c].uncovered){
                        Paint bomb = new Paint();
                        bomb.setARGB(235, 235, 45, 0);
                        canvas.drawCircle(c * qWidth + (qWidth/2), currentLine + (qWidth/2), 10, bomb);
                    }
                }
                currentLine = currentLine + qWidth;
            }
        }
    }

    private void putBombs(){
        int quantity = 8;
        do{
            int row = (int) (Math.random()*8);
            int column = (int) (Math.random()*8);
            if(boxes[row][column].bundle == 0){
                boxes[row][column].bundle = 80;
                quantity--;
            }
        }while (quantity != 0);
    }

    private boolean won(){
        int quantity = 0;
        for(int r = 0; r<8; r++)
            for(int c = 0; c<8; c++)
                if(boxes[r][c].uncovered)
                    quantity++;
        if(quantity == 56)
            return true;
        else
            return false;
    }

    private void countBombsPerimeter(){
        for(int r = 0; r<8; r++){
            for(int c = 0; c<8; c++){
                if(boxes[r][c].bundle == 0) {
                    int quantity = countCoord(r, c);
                    boxes[r][c].bundle = quantity;
                }
            }
        }
    }

    public int countCoord(int row, int column){
        int total = 0;
        if ( row - 1 >= 0 && column - 1 >= 0){
            if(boxes[row-1][column - 1].bundle == 80)
                total++;
        }
        if(row - 1 >= 0){
            if(boxes[row - 1][column].bundle == 80)
                total++;
        }
        if(row - 1 >= 0 && column + 1 < 8){
            if(boxes[row - 1][column + 1].bundle == 80)
                total++;
        }
        if(column + 1 < 8){
            if(boxes[row][column + 1].bundle == 80)
                total++;
        }
        if (row + 1 < 8 && column + 1 < 8){
            if (boxes[row + 1][column + 1].bundle == 80)
                total++;
        }
        if(row + 1 < 8){
            if(boxes[row + 1][column].bundle == 80)
                total++;
        }
        if(row + 1 < 8 && column - 1 >= 0){
            if(boxes[row + 1][column - 1].bundle == 80)
                total++;
        }
        if (column - 1 >= 0){
            if(boxes[row][column - 1].bundle == 80)
                total++;
        }
        return total;
    }

    private void process(int row, int column){
        if(row >= 0 && row < 8 && column >= 0 && column < 8){
            if(boxes[row][column].bundle == 0){
                boxes[row][column].uncovered = true;
                boxes[row][column].bundle = 50;
                process(row, column + 1);
                process(row, column - 1);
                process(row + 1, column);
                process(row - 1, column);
                process(row - 1, column - 1);
                process(row - 1, column + 1);
                process(row + 1, column + 1);
                process(row + 1, column - 1);
            }else if(boxes[row][column].bundle >= 1 && boxes[row][column].bundle <= 8){
                boxes[row][column].uncovered = true;
            }
        }
    }

}
