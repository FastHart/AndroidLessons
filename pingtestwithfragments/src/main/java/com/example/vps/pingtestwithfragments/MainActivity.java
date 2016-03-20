package com.example.vps.pingtestwithfragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static java.lang.Math.sqrt;


public class MainActivity extends Activity implements PingFragment.TaskCallbacks {

    EditText edit;
    ScrollView ping_lines_window_scroll;
    Integer scroll_just_restored = 0;
    TextView ping_lines_window;
    TextView ping_statistics_window;
    Button button;
    final static String TAG = "MAIN";
    private static final String TAG_PING_FRAGMENT = "ping_fragment";
    private PingFragment mPingFragment;
    Bundle ping_fragment_args = new Bundle();

    Integer ping_count = 150;
    Integer ping_interval = 1; // seconds
    String ping_wait_time = "1";
    Integer is_ping_started = 0;
    String output_lines;
    // counters
    Integer ping_counter = 0;
    Integer received = 0;
    Integer errors = 0;
    Integer loss_percent = 100;
    Double rtt_min = 0.0;
    Double rtt_max = 0.0;
    Double rtt_avg = 0.0;
    Double rtt_summ = 0.0;
    Double rtt_summ2 = 0.0;
    Double rtt_mdev = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find view elements
        edit = (EditText)findViewById(R.id.editText1);
        edit.setText("127.0.0.1");
        ping_lines_window_scroll = (ScrollView) findViewById(R.id.textView1Scroll);
        ping_lines_window = (TextView)findViewById(R.id.textView1);
        ping_lines_window.setHorizontallyScrolling(true);
        ping_statistics_window = (TextView)findViewById(R.id.textView2);
        button = (Button)findViewById(R.id.button1);
        button.setText(R.string.button1_start_ping);

        String host = String.valueOf(edit.getText());
        ping_fragment_args.putString("host", host);
        // get fragment with ping task
        FragmentManager fm = getFragmentManager();
        mPingFragment = (PingFragment) fm.findFragmentByTag(TAG_PING_FRAGMENT);
        //mPingFragment.setArguments(ping_fragment_args);
        if (mPingFragment == null) {
            mPingFragment = new PingFragment();
            fm.beginTransaction().add(mPingFragment, TAG_PING_FRAGMENT).commit();
        }

    }

    protected void onSaveInstanceState (Bundle state){
        super.onSaveInstanceState(state);
        state.putInt("is_ping_started", is_ping_started);
        state.putInt("ping_counter", ping_counter);
        state.putString("output_lines", output_lines);
        state.putInt("received", received);
        state.putInt("errors", errors);
        state.putInt("loss_percent", loss_percent);
        state.putDouble("rtt_min", rtt_min);
        state.putDouble("rtt_max", rtt_max);
        state.putDouble("rtt_avg", rtt_avg);
        state.putDouble("rtt_summ", rtt_summ);
        state.putDouble("rtt_summ2", rtt_summ2);
        state.putDouble("rtt_mdev", rtt_mdev);
        // save scroll position (bottom position)
        state.putInt("scroll_position", ping_lines_window_scroll.getScrollY() + ping_lines_window_scroll.getHeight());
        //state.putInt("scroll_position", ping_lines_window_scroll.getScrollY());
        Log.d("SCROLL", "save position: " + String.valueOf(ping_lines_window_scroll.getScrollY()+ ping_lines_window_scroll.getHeight()));
    }

    protected void onRestoreInstanceState (Bundle state) {
        super.onRestoreInstanceState(state);
        is_ping_started = state.getInt("is_ping_started");
        if (is_ping_started == 1) { button.setText(R.string.button1_stop_ping); }
        ping_counter = state.getInt("ping_counter");
        output_lines = state.getString("output_lines");
        received = state.getInt("received");
        errors = state.getInt("errors");
        loss_percent = state.getInt("loss_percent");
        rtt_min = state.getDouble("rtt_min");
        rtt_max = state.getDouble("rtt_max");
        rtt_avg = state.getDouble("rtt_avg");
        rtt_summ = state.getDouble("rtt_summ");
        rtt_summ2 = state.getDouble("rtt_summ2");
        rtt_mdev = state.getDouble("rtt_mdev");
        renderLine();
        //ping_lines_window.setText(output_lines);
        // restore scroll position
        final int position = state.getInt("scroll_position");
        Log.d("SCROLL", "restore position: " + String.valueOf(position));
        //if ( ! isScrollOnBottom(ping_lines_window_scroll) ) { scroll_just_restored = ping_counter; }
        ping_lines_window_scroll.post(new Runnable() {
            public void run() {
                ping_lines_window_scroll.scrollTo(0, position - ping_lines_window_scroll.getHeight());
                //ping_lines_window_scroll.scrollTo(0, position);
            }
        });
        //renderLine();

    }

    public void onClick(View view) {
        switch (is_ping_started){
            case 0:
                dropCountersToZero();
                //output_lines = "";
                //ping_lines_window.setText("");
                renderLine();
                button.setText(R.string.button1_stop_ping);
                is_ping_started = 1;
                doPing();
                break;
            case 1:
                stopPing();
                //output_lines = "";
                break;
        }
    }

    public void doPing(){
        if ( (ping_counter < ping_count) && (is_ping_started == 1)) {
            ping_counter++;
            String host = String.valueOf(edit.getText());
            //String command = "ping -c 1 " + " -i " + ping_interval + " -W " + ping_wait_time +" " + host;
            String command = "ping -c 1 " + " -W " + ping_wait_time +" " + host;
            mPingFragment.startPing(command, ping_interval);
        }
        else {
            stopPing();
        }
    }

    public void stopPing(){
        //dropCountersToZero();
        is_ping_started = 0;
        button.setText(R.string.button1_start_ping);
        //calculate_totals();
    }

    public void dropCountersToZero () {
        ping_counter = 0;
        received = 0;
        errors = 0;
        loss_percent = 100;
        rtt_min = 0.0;
        rtt_max = 0.0;
        rtt_avg = 0.0;
        rtt_summ = 0.0;
        rtt_summ2 = 0.0;
        rtt_mdev = 0.0;
        output_lines = "";
    }

    public void updateCounters (Double rtt) {
        received++;
        rtt_summ += rtt;
        rtt_summ2 += rtt * rtt;
        rtt_avg = rtt_summ / received;
        if ( rtt < rtt_min ) { rtt_min = rtt; }
        if ( rtt > rtt_max ) { rtt_max = rtt; }
        Double d = (double) ((ping_counter-received)/ping_counter*100);
        Long L = Math.round(d);
        loss_percent = L.intValue();
        // calculate mdev ref: http://serverfault.com/questions/333116/what-does-mdev-mean-in-ping8
        // mdev = SQRT(SUM(RTT*RTT) / N – (SUM(RTT)/N)^2)
        Log.d("MATH", String.valueOf(rtt_summ) + "\t" + String.valueOf(rtt_summ2) );
        rtt_mdev = sqrt((rtt_summ2/received) - ((rtt_summ/received)*(rtt_summ/received)));
    }

    public boolean isScrollOnBottom (ScrollView mscroll) {
        // Grab the last child placed in the ScrollView, we need it to determinate the bottom position.
        View view = (View) mscroll.getChildAt(mscroll.getChildCount() - 1);
        // Calculate the scroll diff
        int text_size = (int) ping_lines_window.getTextSize();
        int scroll_position = mscroll.getHeight()+mscroll.getScrollY();
        int diff = (view.getBottom()- scroll_position);
        // if diff is zero, then scroll is on bottom;  if scroll position is zero, then scroll is just started
        if( diff < (text_size * 1.2 ) && scroll_position != 0 ) { Log.d("SCROLL", "scroll on bottom! pos:" + String.valueOf(mscroll.getScrollY() + mscroll.getHeight()) + " diff:" + String.valueOf(diff)); return true; }
        else { Log.d("SCROLL", "scroll is not on bottom! pos:" + String.valueOf(mscroll.getScrollY() + mscroll.getHeight()) + " diff:" + String.valueOf(diff) + " text_size=" + String.valueOf(text_size)); return  false; }
    }

    public void scrollToBottom (final ScrollView mscroll) {
        //if (scroll_just_restored == ping_counter){ Log.d("SCROLL", "do not scroll to bottom "); return; }
        //if( isScrollOnBottom(mscroll) ) {
            Log.d("SCROLL", "scroll to bottom ");
            mscroll.post(new Runnable() {
            @Override
            public void run() {
                mscroll.fullScroll(View.FOCUS_DOWN);
                //scroll.scrollTo(0, scroll.getBottom());
            }
        });
        //}
        //else {Log.d("SCROLL", "scroll is not on bottom ");}

    }

    public void renderLine() {
        boolean scroll_flag = isScrollOnBottom(ping_lines_window_scroll);
        ping_lines_window.setText(output_lines);
        if ( scroll_flag ) scrollToBottom(ping_lines_window_scroll);
        // construct statistics line
        String format = "--- ping statistics ---\n";
        format += "%d  packets transmitted, %d  packets received, %d errors, %d%% packet loss\n";
        format += "rtt min/avg/max/mdev %.3f/%.3f/%.3f/%.3f ms";
        String statistics_string = String.format(format, ping_counter, received, errors, loss_percent, rtt_min, rtt_avg, rtt_max, rtt_mdev);
        //String statistics_string = "--- ping statistics ---\n";
        //statistics_string += ping_counter + " packets transmitted, " + received + " packets received, " + loss_percent + "% packet loss\n";
        //statistics_string += "rtt min/avg/max/mdev " + rtt_min + "/" + rtt_avg + "/" + rtt_max + "/" + rtt_mdev + "ms";
        ping_statistics_window.setText(statistics_string);
    }

    public void calculateStats(String input_lines) {
        // extract to $line only second line from input lines
        StringTokenizer tokenizer = new StringTokenizer(input_lines, "\n");
        tokenizer.nextElement();
        String line = tokenizer.nextToken();
        Log.d(TAG, input_lines);
        Log.d(TAG, line);
        Log.d(TAG, String.valueOf(line.length()));
        // if line is (assume the packet is loss): --- 127.0.0.1 ping statistics ---
        Pattern pattern= Pattern.compile("^.+ping statistics ---");
        if ( pattern.matcher(line).matches() ){
            output_lines += "icmp_seq=" + String.valueOf(ping_counter) + " timed out\n";
            return;
        }
        // if line is: From 2.2.0.10: icmp_seq=1 Time to live exceeded
        pattern= Pattern.compile("^.+Time to live exceeded");
        if ( pattern.matcher(line).matches() ){
            // split line to array by spaces.
            // arr_from_line[0] = From
            // arr_from_line[1] = 2.2.0.10:
            // arr_from_line[2] = icmp_seq=1
            // arr_from_line[3] arr_from_line[6]  = Time to live exceeded
            String[] arr_from_line = line.split(" ");
            output_lines += "From " + arr_from_line[1] + " icmp_seq=" + String.valueOf(ping_counter) + " Time to live exceeded\n" ;
            errors++;
            return;
        }
        // if line is: From 192.168.77.169 icmp_seq=1 Destination Host Unreachable
        pattern= Pattern.compile("^.+Destination Host Unreachable");
        if ( pattern.matcher(line).matches() ){
            // split line to array by spaces.
            // arr_from_line[0] = From
            // arr_from_line[1] = 192.168.77.169
            // arr_from_line[2] = icmp_seq=1
            // arr_from_line[3] arr_from_line[5]  = Destination Host Unreachable
            String[] arr_from_line = line.split(" ");
            output_lines += "From " + arr_from_line[1] + " icmp_seq=" + String.valueOf(ping_counter) + " Destination Host Unreachable\n" ;
            errors++;
            return;
        }
        // if line is: 64 bytes from 127.0.0.1: icmp_seq=1 ttl=64 time=0.072 ms
        pattern= Pattern.compile("^\\d+\\sbytes.+");
        if ( pattern.matcher(line).matches() ) {
            // split $line to array by spaces.
            // $arr_from_line[0] = 64
            // $arr_from_line[1] = bytes
            // $arr_from_line[2] = from
            // $arr_from_line[3] = 127.0.0.1:
            // $arr_from_line[4] = icmp_seq=1
            // $arr_from_line[5] = ttl=64
            // $arr_from_line[6] = time=0.072
            // $arr_from_line[7] = ms
            String[] arr_from_line = line.split(" ");

            // get round trip time from arr_from_line[6] = time=0.072
            // Pattern pattern= Pattern.compile("time=(\d+.\d+)");
            // Matcher m=pattern.matcher(arr_from_line[6]);
            //String round_trip_time = m.group(1);
            String[] arr_from_time = arr_from_line[6].split("=");
            String round_trip_time = arr_from_time[1];

            // construct line from scratch
            //String new_line = arr_from_line[0] + arr_from_line[1] + arr_from_line[2] + arr_from_line[3] + " icmp_seq=" + String.valueOf(ping_counter) + arr_from_line[5] + arr_from_line[6] + arr_from_line[7];
            String new_line = arr_from_line[0] + " " + arr_from_line[1] + " " + arr_from_line[2] + " " + arr_from_line[3] + " " + " icmp_seq=" + String.valueOf(ping_counter) + " " + arr_from_line[5] + " time=" + round_trip_time + " " + arr_from_line[7];
            output_lines += new_line + "\n";
            updateCounters(Double.valueOf(round_trip_time));
            return;
        }
    }

    //
    // PingFragment Methods implementation
    //
    @Override
    public void onPreExecute() { }

    @Override
    public void onProgressUpdate(String line) {
        renderLine();
    }

    @Override
    public void onCancelled() {  }

    @Override
    public void onPostExecute(String line) {
            if ( is_ping_started == 1 ) {
                calculateStats(line);
                renderLine();
                doPing();
            }
    }


}
