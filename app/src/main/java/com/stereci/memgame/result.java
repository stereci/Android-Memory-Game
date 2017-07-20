package com.stereci.memgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class result extends AppCompatActivity {
    public int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreLabel=(TextView)findViewById(R.id.scoreLabel);
        TextView highScoreLabel=(TextView)findViewById(R.id.highScoreLabel);

        score=getIntent().getIntExtra("SCORE",0);
        scoreLabel.setText(score+"");

        SharedPreferences settings=getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highscore=settings.getInt("HIGH_SCORE",0);

        if(score>highscore){
            highScoreLabel.setText("High score: "+score);
            SharedPreferences.Editor editor=settings.edit();
            editor.putInt("HIGH_SCORE",score);
            editor.commit();
        }else{
            highScoreLabel.setText("High score: "+highscore);
        }

    }
    public void tryAgain(View view){
        Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
        startActivity(intent);
    }
}
