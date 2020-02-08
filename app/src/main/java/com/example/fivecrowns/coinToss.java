package com.example.fivecrowns;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class coinToss extends AppCompatActivity {
    private String whoWon = "";
    private int toss = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_toss);

        //Generating a number between 1 and 2, 1 is heads, 2 is tails
        toss = (Math.random() <= 0.5) ? 1 : 2;

    }

    public void headsButtonClick(View view) throws InterruptedException {
        if(toss == 1) {
            whoWon = "Human";
        }
        else if(toss == 2) {
            whoWon = "Computer";
        }

        Intent intent = new Intent(getBaseContext(), GameActivity.class);
        intent.putExtra("whoWon", whoWon);
        intent.putExtra("startPressed", "startPressed");
        startActivity(intent);
    }

    public void tailsButtonClick(View view) throws InterruptedException {
        if(toss == 2) {
            whoWon = "Human";
        }
        else if(toss == 1) {
            whoWon = "Computer";
        }

        Intent intent = new Intent(getBaseContext(), GameActivity.class);
        intent.putExtra("whoWon", whoWon);
        intent.putExtra("startPressed", "startPressed");
        startActivity(intent);
    }



}
