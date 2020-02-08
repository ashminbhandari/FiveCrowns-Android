package com.example.fivecrowns;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class gameEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        String info = getIntent().getExtras().getString("info");
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("The game has ended. ");
        alertDialog.setMessage(info);
        alertDialog.show();
    }
}
