package iss.workshop.team6androidca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {
    Button back;
    private MediaPlayer bgmplayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        startBGMPlayer();
        Intent intent = getIntent();
        String timeTaken = intent.getStringExtra("time");
        TextView textView = findViewById(R.id.score);
        textView.setText("Time taken: "+ timeTaken);

        back = findViewById(R.id.back);
        back.setOnClickListener(this);
    }
    private void startBGMPlayer ( ){

        //play bgm
        bgmplayer = MediaPlayer.create(ScoreActivity.this, R.raw.matched_music);
        bgmplayer.start();
        bgmplayer.setLooping(false);

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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.back){
            Intent intent = new Intent(this,HomepageActivity.class);
            startActivity(intent);
        }

    }
}