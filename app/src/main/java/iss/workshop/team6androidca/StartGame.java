package iss.workshop.team6androidca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import iss.workshop.team6androidca.StartGameAdapter;

public class StartGame extends AppCompatActivity {

    private String[] selectedFilenames = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        gameGridSetup();
    }

    protected void gameGridSetup() {
        GridView gridView = findViewById(R.id.coverGrid);
        GridView gridViewTwo = findViewById(R.id.pictureGrid);

        selectedFilenames = randomisePictures(selectedFilenames);

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
                        if (selectedFilenames[selectedImagesIndexList.get(0)] != selectedFilenames[selectedImagesIndexList.get(1)]) {
                            wrongAnimation(gridViewTwo, selectedImagesIndexList);
                            gridView.getChildAt(selectedImagesIndexList.get(0)).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    gridView.getChildAt(selectedImagesIndexList.get(0)).setAlpha(1);
                                    gridView.getChildAt(selectedImagesIndexList.get(1)).setAlpha(1);
                                }
                            },700);
                        }
                        if (selectedFilenames[selectedImagesIndexList.get(0)] == selectedFilenames[selectedImagesIndexList.get(1)]) {
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

        StartGameAdapter selectedFilenamesAdapter = new StartGameAdapter(this, selectedFilenames);
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
            startActivity(intent);
        }
    }

}