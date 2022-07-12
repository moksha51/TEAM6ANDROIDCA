package iss.workshop.team6androidca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import iss.workshop.team6androidca.StartGameAdapter;

public class StartGame extends AppCompatActivity {
    private MediaPlayer bgmplayer = null;
    //private int bgmPos = 4;

    private String[] testImageArray = {
            "hug", "laugh", "peep",
            "snore", "stop", "tired"
    };

    private final String[] pictureCovers = {
            "ic_black", "ic_gray", "ic_black",
            "ic_gray", "ic_black", "ic_gray",
            "ic_black", "ic_gray", "ic_black",
            "ic_gray", "ic_black", "ic_gray",
    };

    private final int scoreTotal = 6;
    private int currentScore = 0;
    private int clickCount = 0;
    private String[] filefilefiles = new String[6];

    //Time count
    TextView timeCount;
    Button startReset;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    boolean timerStarted = false; //this is the default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        startBGMPlayer();

        Intent intent = getIntent();
        for (int u = 0; u < 6; u++) {
            String index = "file" + Integer.toString(u);
            filefilefiles[u] = intent.getStringExtra(index);
            System.out.println(intent.getStringExtra(index));
        }

        //Time Count
        timeCount = findViewById(R.id.timeCount);
        //startReset = findViewById(R.id.startReset);
        timer = new Timer();

        gameGridSetup();
        startGame();
    }
    private void startBGMPlayer ( ){

        //play bgm
        bgmplayer = MediaPlayer.create(StartGame.this, R.raw.sneaky);
        bgmplayer.start();
        bgmplayer.setLooping(true);

    }
    @Override
    protected void onResume () {
        super.onResume();
        bgmplayer.start();
    }

    @Override
    protected void onPause () {
        super.onPause();
        bgmplayer.pause();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        bgmplayer.stop();
        bgmplayer.release();
    }

    protected void gameGridSetup() {
        GridView gridView = findViewById(R.id.coverGrid);
        GridView gridViewTwo = findViewById(R.id.pictureGrid);

        filefilefiles = randomisePictures(filefilefiles);

        StartGameAdapter pictureCoverAdapter = new StartGameAdapter(this, pictureCovers);
        if (gridView != null) {
            gridView.setAdapter(pictureCoverAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (view.getAlpha()==1) {
                        view.setAlpha(0.01F);
                        clickCount++;
                    }
                    if (clickCount==2) {
                        ArrayList<Integer> selectedImagesIndexList = new ArrayList<Integer>();
                        for (int x = 0; x < gridView.getChildCount(); x++) {
                            if (gridView.getChildAt(x).getAlpha() == 0.01F) {
                                selectedImagesIndexList.add(x);
                            }
                        }
                        if (filefilefiles[selectedImagesIndexList.get(0)] != filefilefiles[selectedImagesIndexList.get(1)]) {
                            wrongAnimation(gridViewTwo, selectedImagesIndexList);
                            gridView.getChildAt(selectedImagesIndexList.get(0)).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    gridView.getChildAt(selectedImagesIndexList.get(0)).setAlpha(1);
                                    gridView.getChildAt(selectedImagesIndexList.get(1)).setAlpha(1);
                                }

                            },700);
                        }
                        if (filefilefiles[selectedImagesIndexList.get(0)] == filefilefiles[selectedImagesIndexList.get(1)]) {
                            for (int k : selectedImagesIndexList) {
                                gridView.getChildAt(k).setAlpha(0);
                                gridView.getChildAt(k).setOnClickListener(null);
                            }
                            correctAnimation(gridViewTwo, selectedImagesIndexList);
                            updateScore();
                        }
                        clickCount = 0;
                    }

                }
            });
        }

        StartGameAdapter2 selectedFilenamesAdapter = new StartGameAdapter2(this, filefilefiles);
        if (gridViewTwo != null) {
            gridViewTwo.setAdapter(selectedFilenamesAdapter);
        }

    }

    private String[] randomisePictures(String[] arr) {
        ArrayList<String> arrList = new ArrayList<String>(Arrays.asList(arr));
        arrList.addAll(arrList);
        Collections.shuffle(arrList);
        arr = arrList.toArray(arr);
        return arr;
    }

    private void wrongAnimation(GridView gridView, ArrayList<Integer> arrList) {
        for (int i : arrList) {
            View image = gridView.getChildAt(i) ;
            Animation animation = AnimationUtils.loadAnimation(image.getContext(), R.anim.wrong);
            image.startAnimation(animation);
        }
    }

    private void correctAnimation(GridView gridView, ArrayList<Integer> arrList) {
        for (int i : arrList) {
            View image = gridView.getChildAt(i) ;
            Animation animation = AnimationUtils.loadAnimation(image.getContext(), R.anim.correct);
            image.startAnimation(animation);
        }
    }

    private void updateScore() {
        TextView textView = findViewById(R.id.realScore);
        currentScore++;
        textView.setText(String.valueOf(currentScore));
        if (currentScore == 6) {
            Intent intent = new Intent(StartGame.this, ScoreActivity.class);
            intent.putExtra("time",getTimerText());
            startActivity(intent);

        }
    }

    public void startGame (){
        timerStarted = true;
        startTimer();
    };

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

}