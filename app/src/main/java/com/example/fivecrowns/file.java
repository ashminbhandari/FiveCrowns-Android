package com.example.fivecrowns;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class file {
    private Context mContext;

    int roundNo;
    int computerScore;
    int humanScore;
    Vector <card> computerHand = new Vector<>();
    Vector<card> humanHand = new Vector<>();
    Vector<card> drawPile = new Vector<>();
    Vector<card>discardPile = new Vector<>();
    String nextPlayer = "";

    public file(Context context) {
        this.mContext = context;
    }

    public void read() throws IOException {
        AssetManager am = mContext.getAssets();

        //Have we not parsed the score for computer yet?
        boolean scoreForComputer = true;

        //Have we not parsed the score for computer yet?
        boolean handForComputer = true;

        try {
            InputStream is = am.open("case.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                //Taking out the round number out of the file
                if (line.contains("Round")) {
                    String numberString = "";
                    for (int i = 7; i < line.length(); i++) {
                        numberString = numberString + line.charAt(i);
                    }
                    roundNo = Integer.parseInt(numberString);
                }

                //Taking out the computer and player scores out of the file
                if (line.contains("Score")) {
                    if (scoreForComputer) {
                        String compScore = "";
                        for (int i = 10; i < line.length(); i++) {
                            compScore = compScore + line.charAt(i);
                        }
                        computerScore = Integer.parseInt(compScore);
                        scoreForComputer = false;
                    }
                    else {
                        String humScore = "";
                        for (int i = 10; i < line.length(); i++) {
                            humScore = humScore + line.charAt(i);
                        }
                        humanScore = Integer.parseInt(humScore);
                    }
                }

                //Taking out the computer hand out of the pile
                if (line.contains("Hand")) {
                    if (handForComputer) {
                        for (int i = 9; i < line.length(); i++) {
                            if (line.charAt(i) != ' ') {
                                String rank = Character.toString(line.charAt(i));
                                String suit = Character.toString(line.charAt(++i));
                                computerHand.add(new card(rank, suit));
                            }
                        }
                        handForComputer = false;
                    }
                    else {
                        for (int i = 9; i < line.length(); i++) {
                            if (line.charAt(i) != ' ') {
                                String rank = Character.toString(line.charAt(i));
                                String suit = Character.toString(line.charAt(++i));
                                humanHand.add(new card(rank, suit));
                            }
                        }
                    }
                }

                //Taking out the draw pile
                if (line.contains("Draw")) {
                    for (int i = 11; i < line.length(); i++) {
                        if (line.charAt(i) != ' ') {
                            String rank = Character.toString(line.charAt(i));
                            String suit = Character.toString(line.charAt(++i));
                            drawPile.add(new card(rank, suit));
                        }
                    }
                }

                //Taking out the discard pile
                //Taking out the draw pile
                if (line.contains("Discard")) {
                    for (int i = 14; i < line.length(); i++) {
                        if (line.charAt(i) != ' ') {
                            String rank = Character.toString(line.charAt(i));
                            String suit = Character.toString(line.charAt(++i));
                            discardPile.add(new card(rank, suit));
                        }
                    }
                }

                //Taking out the next player
                if (line.contains("Next")) {
                    for (int i = 13; i < line.length(); i++) {
                        nextPlayer = nextPlayer + line.charAt(i);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
