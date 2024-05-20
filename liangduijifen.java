package com.example.bmiceshi;

import import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class liangduijifen extends AppCompatActivity {

    private TextView teamAScoreTextView;
    private TextView teamBScoreTextView; // 假设你也有一个用于队伍B的TextView
    private Button teamAOnePointButton;
    private Button teamATwoPointButton;
    private Button teamAThreePointButton;
    // ... 初始化队伍B的按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teamAScoreTextView = findViewById(R.id.teamAScore);
        teamBScoreTextView = findViewById(R.id.teamBScore); // 假设你也有一个teamBScore TextView的ID
        teamAOnePointButton = findViewById(R.id.teamAOnePoint);
        teamATwoPointButton = findViewById(R.id.teamATwoPoint);
        teamAThreePointButton = findViewById(R.id.teamAThreePoint);
        // ... 初始化队伍B的按钮

        teamAOnePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(teamAScoreTextView, 1);
            }
        });

        teamATwoPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(teamAScoreTextView, 2);
            }
        });

        teamAThreePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(teamAScoreTextView, 3);
            }
        });

        teamAScoreTextView = findViewById(R.id.teamAScore);
        teamBScoreTextView = findViewById(R.id.teamBScore); // 假设你也有一个teamBScore TextView的ID
        teamBOnePointButton = findViewById(R.id.teamBOnePoint);
        teamBTwoPointButton = findViewById(R.id.teamBTwoPoint);
        teamBThreePointButton = findViewById(R.id.teamBThreePoint);
        // ... 初始化队伍B的按钮

        teamBOnePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(teamBScoreTextView, 1);
            }
        });

        teamBTwoPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(teamBScoreTextView, 2);
            }
        });

        teamBThreePointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScore(teamBScoreTextView, 3);
            }
        });

        // ... 为队伍B的按钮设置类似的点击监听器
    }

    private void updateScore(TextView textView, int points) {
        int currentScore = Integer.parseInt(textView.getText().toString());
        int newScore = currentScore + points;
        textView.setText(String.valueOf(newScore));
    }
}}