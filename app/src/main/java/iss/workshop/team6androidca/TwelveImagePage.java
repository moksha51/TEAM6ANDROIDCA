package iss.workshop.team6androidca;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TwelveImagePage extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener  {

    //Time count
    TextView timeCount;
    Button startReset;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    boolean timerStarted = false; //this is the default

    //Progress bar
    ProgressBar completionBar;
    int newProgress = 0;


    private final int imagecount = 0;
    ActivityResultLauncher<Intent> rlSelectImage;

    //this code is for for the test out the functionality without Heily's download part
    private final String[] fnames = {
            "hug", "laugh", "peep", "snore", "stop",
            "tired", "full", "what", "afraid", "no_way"
    };

    /*
    this code is to be used for actual submission
    Heily to rename the downloaded image files to "1", "2", "3" etc
    up to 20 for the downloaded files
    private final String[] fnames = {
            "1", "2", "3", "4", "5","6","7","8","9","10",
            "11", "12", "13", "14", "15","16","17","18","19","20"
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twelve_image_page);

        MyCustomAdapter adapter = new MyCustomAdapter(this, 0);
        adapter.setData(fnames);

        GridView gridView = findViewById(R.id.gridView);
        if (gridView != null) {
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this);
        }


        //Time Count
        timeCount = findViewById(R.id.timeCount);
        startReset = findViewById(R.id.startReset);
        timer = new Timer();

        //Progress Bar
        completionBar = findViewById(R.id.xOutOf6ImageSelectCounter);
        completionBar.setMax(6);

        //Assuming the game commences once player enters this page
        startGame();

    }

    protected void registerForResult(){
        rlSelectImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null){
                            String image = data.getStringExtra("");
                            if (image != null){
                                //ask Cher Wah about this
                            }
                        }
                    }
                }
        );
    }

    //Game logic for Halim to figure out how to match the images
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        int selectedImage = v.getId();

        if (id == R.id.grid_image){
            Intent intent = new Intent(this, TwelveImagePage.class);
            rlSelectImage.launch(intent);
            Toast.makeText(this, "This image will be loaded into the game", Toast.LENGTH_SHORT).show();
            if (imagecount >= 0 && imagecount < 6){
                Toast.makeText(this, imagecount + " image(s) has been loaded into the game", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Game will commence shortly",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onClick(View view){
        EditText newUrl = findViewById(R.id.urlBar);
        if(newUrl!= null){
            Intent intent = new Intent();
            intent.putExtra("newURL", newUrl.getText().toString());
            Uri uri = Uri.parse(String.valueOf(newUrl));
            setResult(RESULT_OK, intent);
            finish();
        }
    }



    // ProgressBar and Timer

    public void startGame (){

        if (timerStarted == false) // if timer is not started
        {
            timerStarted = true; //start timer
            startReset.setText("RESTART"); // change button to stop because timer started
            startReset.setTextColor(ContextCompat.getColor(this, R.color.salmon));

            startTimer();
            // not sure how to connect the game to the progress bar
            updateProgress(game, newProgress);

        }
        else {
            timerStarted = false; //stop timer
            startReset.setText("START");

            timerTask.cancel();
        }
    };

    public void updateProgress(View game, int newProgress) {
        if (newProgress == 6) {
            completionBar.setVisibility(View.GONE);
        } else {
            completionBar.setVisibility(View.VISIBLE);
            completionBar.setProgress(newProgress);
        }
    }


    private void startTimer(){
        timerTask = new TimerTask()
        {
            @Override
            public void run(){
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        time++;
                        timeCount.setText(getTimerText());
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(timerTask,0,1000);
    }


    private String getTimerText()
    {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);

    }

    private String formatTime(int seconds, int minutes, int hours){
        return String.format("%2d", hours) + ":" + String.format("%2d", minutes) + ":" + String.format("%2d", seconds);
    }

    public void restartTapped (View v){

        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Restart game");
        resetAlert.setMessage("Are you sure you want to restart the game?");
        resetAlert.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (timerTask != null) // got some task
                {
                    timerTask.cancel();
                    time = 0.0;
                    timerStarted=false;
                    timeCount.setText(formatTime(0,0,0));

                    newProgress = 0;
                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
    }
}