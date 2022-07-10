package iss.workshop.team6androidca;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener {
    Button startGamebtn;
    private MediaPlayer bgmplayer = null;
    private int bgmPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        startBGMPlayer(bgmPos);
        startGamebtn = findViewById(R.id.startGamebtn);
        startGamebtn.setOnClickListener(this);

        }

        private void startGamebtn () {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        private void startBGMPlayer ( int bgmPos){

            //play bgm
            bgmplayer = MediaPlayer.create(this, R.raw.backgroud_music);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.startGamebtn){
            startGamebtn();
        }
    }
}
