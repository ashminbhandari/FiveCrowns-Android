package com.example.fivecrowns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //onClick function for the startGame button
    public void startGame(View view) {
        Intent startGameIntent = new Intent(MainActivity.this, coinToss.class);
        startActivity(startGameIntent);

    }

    //onClick function for the loadGame button
    public void loadGame(View view) throws IOException {


        Intent intent = new Intent(getBaseContext(), GameActivity.class);
        String loadPressed = "loadPressed";
        intent.putExtra("loadPressed", loadPressed);
        startActivity(intent);

    }


}
