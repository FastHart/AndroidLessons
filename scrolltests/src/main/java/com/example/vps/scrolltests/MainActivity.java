package com.example.vps.scrolltests;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {

    ScrollView scroll;
    TextView text;
    String output_string ="";
    Integer counter =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scroll = (ScrollView) findViewById(R.id.textView1Scroll);
        text = (TextView) findViewById(R.id.textView1);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{scroll.getScrollX(), scroll.getScrollY() + scroll.getHeight()});
        state.putString("output_string", output_string);
        state.putInt("counter", counter);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        counter = savedInstanceState.getInt("counter");
        output_string = savedInstanceState.getString("output_string");
        text.setText(output_string);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
            scroll.post(new Runnable() {
                public void run() {
                    scroll.scrollTo(position[0], position[1]-scroll.getHeight());
                }
            });
    }

    public void fillScreen (Integer counter) {

        for (int i = 1; i < 10; i++) {
            output_string += String.valueOf(counter) + String.valueOf(i) + "\n";
            text.setText(output_string);



/*
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
       }
        //int diff = (text.getBottom()-(scroll.getHeight()+scroll.getScrollY()));
        int bottom = text.getBottom();
        int height = scroll.getHeight();
        int scrollY = scroll.getScrollY();
        int diff = bottom - ( height + scrollY );
        Log.d("SCROLL", "=======");
        Log.d("SCROLL", "bottom" + String.valueOf(bottom));
        Log.d("SCROLL", "height" + String.valueOf(height));
        Log.d("SCROLL", "scroolY" + String.valueOf(scrollY));
        Log.d("SCROLL", "diff" + String.valueOf(diff));

        if ( diff == 0 ) {
            scroll.post(new Runnable() {
                @Override
                public void run() {
                    scroll.fullScroll(View.FOCUS_DOWN);
                    //scroll.scrollTo(0, scroll.getBottom());
                }
            });
        }

        bottom = text.getBottom();
        height = scroll.getHeight();
        scrollY = scroll.getScrollY();
        diff = bottom - ( height + scrollY );
        Log.d("SCROLL", "bottom" + String.valueOf(bottom));
        Log.d("SCROLL", "height" + String.valueOf(height));
        Log.d("SCROLL", "scroolY" + String.valueOf(scrollY));
        Log.d("SCROLL", "diff" + String.valueOf(diff));
    }

    public void onClick(View view) {
        //output_string = "";
        counter++;
        fillScreen(counter);
    }

    public void clean(View view) {
        counter = 0;
        output_string = "";
        text.setText(output_string);
    }
}
