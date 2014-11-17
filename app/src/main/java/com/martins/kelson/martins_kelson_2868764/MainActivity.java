package com.martins.kelson.martins_kelson_2868764;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    ReversiView rv;
    static TextView tv_player, tv_score_white, tv_score_black, tv_turn;
    static Button bt_reset;

    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        tv_player = (TextView) findViewById(R.id.tv_turntext);
        bt_reset = (Button) findViewById(R.id.bt_reset);
        tv_score_white = (TextView) findViewById(R.id.white_score);
        tv_score_black = (TextView) findViewById(R.id.black_score);
        tv_turn = (TextView) findViewById(R.id.tv_turn);
        rv = (ReversiView) findViewById(R.id.reversi_view);

        MainActivity.bt_reset = (Button) findViewById(R.id.bt_reset);
        MainActivity.bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rv.resetGame();
                rv.invalidate();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
