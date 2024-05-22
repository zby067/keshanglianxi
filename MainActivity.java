package com.example.qiuduijifen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View btn){
        TextView scoreText = findViewById(R.id.scorel);
        int s1= Integer.parseInt(scoreText.getText().toString());
        if(btn.getId()==R.id.btn3){
            scoreText.setText(String.valueOf(i:s1+3));
        }else if(btn.getId()==R.id.btn2){
            scoreText.setText(String.valueOf(i:s1+2));
        }else if(btn.getId()==R.id.btn1){
            scoreText.setText(String.valueOf(i:s1+1));
        }

    }
    public void click(View btn){
        TextView scoreText = findViewById(R.id.scorelb);
        int s2= Integer.parseInt(scoreText.getText().toString());
        if(btn.getId()==R.id.btnb3){
            scoreText.setText(String.valueOf(i:s2+3));
        }else if(btn.getId()==R.id.btnb2){
            scoreText.setText(String.valueOf(i:s2+2));
        }else if(btn.getId()==R.id.btnb1){
            scoreText.setText(String.valueOf(i:s2+1));
        }

    }
    public void resetClick(View btn) {
        TextView scoreText = findViewById(R.id.scorel);
        scoreText.setText(String.valueOf(i:0));

    }
    public void resetClick(View btn) {
        TextView scoreText = findViewById(R.id.scorelb);
        scoreText.setText(String.valueOf(i:0));

    }
}