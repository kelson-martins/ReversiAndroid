package com.martins.kelson.martins_kelson_2868764;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kelson on 10/30/2014.
 */
public class ReversiView extends View {

    // an 8x8 array that represents our game board
    private static int[][] board;

    private RectF[][] boxes;

    // information maintaining the width and height of a single cell
    private int cell_width, cell_height;

    // paint objects for the pieces and the board
    private Paint cyan, black, white;

    // rectangle that will represent the size of a cell
    private RectF bounding_box;

    // who is the current player
    private int current_player = 1;
    private int opposing = 2;

    // determines the cell coordinates of the press and the release for making moves
    private int press_x, press_y;

    private int whitePieces = 0;
    private int blackPieces = 0;

    public ReversiView(Context context) {
        super(context);
        init();
    }

    public ReversiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReversiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        cyan  = new Paint(Paint.ANTI_ALIAS_FLAG);
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);

        cyan.setColor(Color.CYAN);
        black.setColor(Color.BLACK);
        white.setColor(Color.WHITE);

        // 1 = Whites
        // 2 = Black
        board = new int[8][8];
        board[3][3] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
        board[4][4] = 1;

        boxes = new RectF[8][8];

        press_x = 0;
        press_y = 0;

        bounding_box = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        cell_width = canvas.getWidth() / 8;
        cell_height = cell_width;

        canvas.save();


        // Draw the Boxes
        for (int x = 0; x < 8; x++) {

            for (int y = 0; y < 8; y++) {

                int l = x * cell_width;
                int t = y * cell_width;
                int r = l + cell_width;
                int b = t + cell_width;

                bounding_box.set(l,t,r,b);

                canvas.drawRect(bounding_box,cyan);
                boxes[x][y] = new RectF(l,t,r,b);

                if (board[x][y] == 1) {
                    canvas.drawCircle(bounding_box.centerX(),bounding_box.centerY(),cell_width/2,white);
                } else if(board[x][y] == 2) {
                    canvas.drawCircle(bounding_box.centerX(),bounding_box.centerY(),cell_width/2,black);
                }
            }
        }

        // Draw the Delimiters
        for (int i = 1; i <= 8; i++) {

            // Draw Vertical Line
            canvas.drawLine(i * cell_width, 0, i * cell_width, 8 * cell_height, black);

            // Draw Horizontal Line
            canvas.drawLine(0 ,i * cell_height, getWidth(), i * cell_height, black);
        }

        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

            // determine te cell that was clicked
            for (int i = 0; i < 8; i++) {

                for (int j = 0; j < 8; j++) {

                    if (boxes[i][j].contains(event.getX(),event.getY()) ) {
                        press_x = i;
                        press_y = j;
                    }
                }
            }

            if (getPiece(press_x,press_y) == 0) {
                if (placeAndReverse(press_x,press_y,true) > 0) {
                    nextTurn(false);
                }
            }

            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private int getPiece(int x, int y) {

        if ( (x == -1 || y == -1) || (x == 8 || y == 8) ){
            return -1;
        }

        return board[x][y];
    }

    private int placeAndReverse(int x, int y, boolean move) {

        if(board[x][y] != 0){
            return 0;
        }

        int dx, dy;
        int totalCaptured = 0;
        for(dx = -1; dx <= 1; dx++){
            for(dy = -1; dy <= 1; dy++){

                // null
                if(dx == 0 && dy == 0){
                    continue;
                }

                // explore for captures
                for(int steps = 1; steps < 8; steps++){
                    int ray_i = x + (dx*steps);
                    int ray_j = y + (dy*steps);

                    // out og bounds
                    if(ray_i < 0 || ray_i >= 8 || ray_j < 0 || ray_j >= 8){
                        break;
                    }

                    // empty cell
                    if(board[ray_i][ray_j] == 0){
                        break;
                    }

                    // capture sequence current player
                    if(board[ray_i][ray_j] == current_player){
                        if(steps > 1){
                            // at least one step, capture the ray.
                            totalCaptured += steps - 1;
                            if(move) { // if move, capture (using the same method for control when move == false)
                                while(steps-- > 0){
                                    board[x + (dx*steps)][y + (dy*steps)] = current_player;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }

        return totalCaptured;
    }

    public void swapPlayer() {

        int temp = opposing;
        opposing = current_player;
        current_player = temp;

        updateUI();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(widthMeasureSpec, 8 * (widthMeasureSpec/8));

    }

    private void updateScores() {

        whitePieces = 0;
        blackPieces = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) {
                    whitePieces++;
                }
                if (board[i][j] == 2) {
                    blackPieces++;
                }
            }
        }

    }

    public void resetGame() {

        board = new int[8][8];
        board[3][3] = 1;
        board[3][4] = 2;
        board[4][3] = 2;
        board[4][4] = 1;

        current_player = 1;
        opposing = 2;
        updateScores();
        updateUI();

    }

    private void updateUI() {

        MainActivity.tv_turn.setText(R.string.player_turn);
        MainActivity.tv_score_white.setText(String.valueOf(whitePieces));
        MainActivity.tv_score_black.setText(String.valueOf(blackPieces));

        if (current_player == 1)

            MainActivity.tv_player.setText(R.string.turn_white);
        else
            MainActivity.tv_player.setText(R.string.turn_black);

        invalidate();

    }

    public boolean isAMovePossible() {

        int i, j;
        for(i=0;i<8;i++){
            for(j=0;j<8;j++){
                int numCaptured = placeAndReverse(i, j, false);
                if(numCaptured > 0) {
                    return true;
                }
            }
        }
        return false;

    }

    public void nextTurn(boolean secondTime) {

        updateScores();
        swapPlayer();

        if(!isAMovePossible()) {
            if(secondTime) {
                gameOver();
            } else {
                nextTurn(true);
            }
        }

        invalidate();

    }

    private void gameOver() {

        MainActivity.tv_turn.setText(R.string.winner);
        if (whitePieces > blackPieces)
            MainActivity.tv_player.setText(R.string.turn_white);
        else if(blackPieces > whitePieces)
            MainActivity.tv_player.setText(R.string.turn_black);
        else {
            MainActivity.tv_player.setText(R.string.draw);
        }
        invalidate();

    }

}
