package com.example.fivecrowns;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOError;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class GameActivity extends AppCompatActivity {

    //Player 1 layout for player 1 cards
    private LinearLayout player1Layout = null;

    //Player 2 layout for player 2 cards
    private LinearLayout player2Layout = null;

    //The discard pile layout
    private LinearLayout discardPileLayout = null;

    //The draw pile image
    private ImageView drawPileImage = null;

    //The pick card
    private ImageView pickCardImage = null;

    //The drop card
    private ImageView dropCardImage = null;

    //Round instance that we will be communicating with
    private static round theRound = null;

    //Time for next round yet?
    private boolean nextRound = false;

    //Who won the toss
    private static String whoWon = "";

    private static String whoWonToss = "";
    //Round number
    private static int roundNo = 1;

    static boolean loadGameDone = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String loadPressed = getIntent().getExtras().getString("loadPressed");
        String startPressed = getIntent().getExtras().getString("startPressed");


        if(!loadGameDone) {
            if (loadPressed != null) {
                if (loadPressed.equals("loadPressed")) {

                    Log.d("HERE", "HERE");
                    file fileRead = new file(this);
                    try {
                        fileRead.read();
                    } catch (IOException e) {

                    }

                    Log.d("Who's turn is: ", fileRead.nextPlayer);
                    Log.d("Round number is: ", Integer.toString(fileRead.roundNo));

                    for (int i = 0; i < fileRead.computerHand.size(); i++) {
                        Log.d("Computer hand: ", fileRead.computerHand.get(i).getCardAsString());
                    }
                    Log.d("Computer score", Integer.toString(fileRead.computerScore));
                    for (int i = 0; i < fileRead.humanHand.size(); i++) {
                        Log.d("Human hand: ", fileRead.humanHand.get(i).getCardAsString());

                    }
                    Log.d("Human score", Integer.toString(fileRead.humanScore));

                    for (int i = 0; i < fileRead.drawPile.size(); i++) {
                        Log.d("Draw pile: ", fileRead.drawPile.get(i).getCardAsString());

                    }
                    for (int i = 0; i < fileRead.discardPile.size(); i++) {
                        Log.d("Discard pile: ", fileRead.discardPile.get(i).getCardAsString());

                    }


                    theRound = new round(fileRead.roundNo);
                    theRound.discardPile = fileRead.discardPile;
                    theRound.drawPile = fileRead.drawPile;
                    theRound.player1.clearHand();
                    theRound.player2.clearHand();
                    roundNo = fileRead.roundNo;

                    if (fileRead.nextPlayer.equals("Human")) {
                        for (int i = 0; i < fileRead.humanHand.size(); i++) {
                            theRound.player1.addToHand(fileRead.humanHand.get(i));

                        }

                        for (int i = 0; i < fileRead.computerHand.size(); i++) {
                            theRound.player2.addToHand(fileRead.computerHand.get(i));
                        }
                        theRound.player1.addScore(fileRead.humanScore);
                        theRound.player2.addScore(fileRead.computerScore);
                    } else if (fileRead.nextPlayer.equals("Computer")) {
                        for (int i = 0; i < fileRead.computerHand.size(); i++) {
                            theRound.player1.addToHand(fileRead.computerHand.get(i));
                        }

                        for (int i = 0; i < fileRead.humanHand.size(); i++) {
                            theRound.player2.addToHand(fileRead.humanHand.get(i));
                        }
                        theRound.player1.addScore(fileRead.computerScore);
                        theRound.player2.addScore(fileRead.humanScore);
                    }
                    if (fileRead.nextPlayer.equals("Human")) {
                        whoWon = "Human";
                        theRound.coinTossResult(whoWon);

                    } else if (fileRead.nextPlayer.equals("Computer")) {
                        whoWon = "Computer";
                        theRound.coinTossResult(whoWon);
                    }

                    theRound.coinTossResult(whoWon);
                    loadGameDone = true;
                }
            }
        }
            if(startPressed!=null && startPressed.equals("startPressed")) {

                Log.d("HERE", "HERE");
                whoWonToss = getIntent().getExtras().getString("whoWon");
                whoWon = whoWonToss;
                Log.d("whoWon toss ", whoWonToss);
                theRound = new round(roundNo);

                theRound.coinTossResult(whoWonToss);
            }







        Log.d("Round number is: ", Integer.toString(theRound.roundNo));

        //Initializing the linear layouts for player 1 cards, player 2 cards, discard pile, drawpile and a couple of other texts in the screen
        player1Layout = (LinearLayout) findViewById(R.id.player1Layout);
        player2Layout = (LinearLayout) findViewById(R.id.player2Layout);
        discardPileLayout = (LinearLayout) findViewById(R.id.discardPileLayout);
        drawPileImage = (ImageView) findViewById(R.id.drawPileImage);
        pickCardImage = (ImageView) findViewById(R.id.pickCardImage);
        dropCardImage = (ImageView) findViewById(R.id.dropCardImage);

        //Setting the appropriate tags
        drawPileImage.setTag("draw");
        pickCardImage.setTag("notSelected");
        dropCardImage.setTag("notSelected");

        //Update the views
        drawPlayerCards(1);
        drawPlayerCards(2);
        drawDiscardPile();

        //Removing information view
        drawInformation();

        //Setting up the click listeners
        setUpClickListeners();
    }

    //Draw player cards
    public void drawPlayerCards(int i) {

        //Player cards as string
        Vector<String> playerCards = theRound.getPlayerCardsAsString(i);

        //Updating the linear layout for player
        for (int j = 0; j < playerCards.size(); j++) {

            //Creating a temporary ImageView object
            ImageView imgView = new ImageView(this);

            //The path to the drawable
            String imgPath = "@drawable/" + "c" + playerCards.get(j).toLowerCase();

            //The image resource id
            int imageResource = getResources().getIdentifier(imgPath, null, getPackageName());

            //Setting the ImageView with the above image resource id
            imgView.setImageResource(imageResource);

            //Setting the tag for the image view
            Log.d("Setting tag as", playerCards.get(j));
            imgView.setTag(playerCards.get(j));
            Log.d("Tag set as", (String) imgView.getTag());

            if (i == 1) {
                //Adding the card image to player 1's linear layout
                player1Layout.addView(imgView);
            } else if (i == 2) {
                //Adding the card image to player 2's linear layout
                player2Layout.addView(imgView);
            }
        }
    }

    //Updates all views in the screen
    public void drawDiscardPile() {

        //The discard pile as a vector of strings
        Vector<String> discardPile = theRound.getDiscardPileAsString();

        //Updating the discard pile linear layout
        //Creating a temporary ImageView object, to be added to the linear layout
        ImageView imgView = new ImageView(this);

        //The path to the drawable
        String imgPath = "@drawable/" + "c" + discardPile.get(0).toLowerCase();

        //The imageResource
        int imageResource = getResources().getIdentifier(imgPath, null, getPackageName());

        //Setting the ImageView with the above image resource
        imgView.setImageResource(imageResource);

        //Setting the tag for the imageView according to the name of the image
        imgView.setTag(discardPile.get(0));

        //Adding to the linear layout
        discardPileLayout.addView(imgView);

        Log.d("Discard pile : ", discardPile.get(0));
    }

    //Updates the information like round no, whos turn and player scores
    public void drawInformation() {
        //Setting the round no text
        TextView roundInfo = (TextView) findViewById(R.id.roundInfo);
        roundInfo.setText("Round: " + roundNo + "      " + "Turn: " + theRound.getWhosTurn());

        //Setting the player 1 scores
        TextView player1Text = findViewById(R.id.player1Text);
        player1Text.setText("Player 1 -------- Score = " + theRound.getPlayerScore(1) + " " + whoWon);

        //Setting the player 2 scores
        TextView player2Text = findViewById(R.id.player2Text);
        player2Text.setText("Player 2 -------- Score = " + theRound.getPlayerScore(2));
    }

    //Set up click listener for all views
    public void setUpClickListeners() {

        //Setting up click listeners for player 1
        //Getting the children of player 1's linear layout, these are the player one's cards
        final int player1CardCount = player1Layout.getChildCount();

        //Iterating through an assigning click listener for the cards
        for (int i = 0; i < player1CardCount; i++) {

            //Getting the child at index i
            View v = player1Layout.getChildAt(i);

            //Setting the click listener
            v.setOnClickListener(new clickListener());

            if (whoWon.equals("Computer")) {
                v.setOnClickListener(null);
            }
        }

        //Setting up click listeners for player 2
        //Getting the children of player 2's linear layout, these are the player one's cards
        final int player2CardCount = player2Layout.getChildCount();

        //Iterating through an assigning click listener for the cards
        for (int i = 0; i < player2CardCount; i++) {

            //Getting the child at index i
            View v = player2Layout.getChildAt(i);

            //Setting the click listener
            v.setOnClickListener(new clickListener());

            if (whoWon.equals("Human")) {
                v.setOnClickListener(null);
            }
        }

        //Setting up click listeners for the discard pile
        //Getting the children of discard pile, i.e. the cards in it
        final int discardPileCardCount = discardPileLayout.getChildCount();

        //Iterating through an assigning click listener for the cards
        for (int i = 0; i < discardPileCardCount; i++) {

            //Getting the child at index i
            View v = discardPileLayout.getChildAt(i);

            //Setting the click listener
            v.setOnClickListener(new clickListener());
        }

        //Finally, setting up the click listener for draw pile
        ImageView drawPile = findViewById(R.id.drawPileImage);
        drawPile.setOnClickListener(new clickListener());


    }

    //Player selects card for either picking or dropping
    private void playerSelectsCard(ImageView imgView, String pickOrDrop) {

        if (pickOrDrop.equals("drop")) {
            //Get the drop card view
            ImageView dropCard = findViewById(R.id.dropCardImage);

            //Set the drawable of the drop card as the selected card
            dropCard.setImageDrawable(imgView.getDrawable());

            //Set the tag as selected
            Log.d("Setting tag as", (String) imgView.getTag());
            dropCard.setTag(imgView.getTag());
        } else if (pickOrDrop.equals("pick")) {
            //Get the pick card view
            ImageView pickCard = findViewById(R.id.pickCardImage);

            //Set the drawable of the pick card as the selected card
            pickCard.setImageDrawable(imgView.getDrawable());

            //Set the tag as selected
            Log.d("Setting tag as", (String) imgView.getTag());
            pickCard.setTag(imgView.getTag());
        }
    }

    //Implementing click listener
    private final class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (((ViewGroup) view.getParent()).getId() == player1Layout.getId() && theRound.getWhosTurn().equals("Player 1")) {
                //Calling the drop card selector
                playerSelectsCard((ImageView) view, "drop");
            }

            if (((ViewGroup) view.getParent()).getId() == discardPileLayout.getId() || view.getId() == drawPileImage.getId()) {
                //Calling the pick card selector
                playerSelectsCard((ImageView) view, "pick");
            }

            if (((ViewGroup) view.getParent()).getId() == player2Layout.getId() && theRound.getWhosTurn().equals("Player 2")) {
                //Calling the drop card selector for player 2
                playerSelectsCard((ImageView) view, "drop");
            }
        }
    }

    //onClick function for the take turn button
    public void takeTurnButtonClick(View view) throws InterruptedException {
        //Get which player is playing right now, for which player take turn was clicked
        int whichPlayer = Character.getNumericValue(theRound.getWhosTurn().charAt(7));

        if ((pickCardImage.getTag() == "notSelected") || (dropCardImage.getTag()) == "notSelected") {

            if (whichPlayer == 1 && whoWon.equals("Computer")) {
                Log.d("Activity initiates", "computer move");
                theRound.takeTurn(whichPlayer);
                theRound.switchTurn();
                player1Layout.removeAllViews();
                drawPlayerCards(1);
            } else if (whichPlayer == 2 && whoWon.equals("Human")) {
                Log.d("Activity initiates", "computer move");
                theRound.takeTurn(whichPlayer);
                theRound.switchTurn();
                player2Layout.removeAllViews();
                drawPlayerCards(2);

            } else {
                //The player has not yet selected to pick a card or to drop a card, so send them an alert
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("You are yet to complete your card selection.");
                alert.show();
            }
        } else {
            //Pick from draw or discard pile
            if (pickCardImage.getTag().equals("draw")) {
                //Call the pick card function with the appropriate string code
                theRound.pickCard(whichPlayer, "draw");
            } else {
                theRound.pickCard(whichPlayer, "discard");
            }

            //Drop a card
            theRound.dropCard(whichPlayer, (String) dropCardImage.getTag());

            theRound.switchTurn();

        }

        //Refresh the views
        discardPileLayout.removeAllViews();
        drawDiscardPile();

        player1Layout.removeAllViews();
        player2Layout.removeAllViews();


        //Draw the player's card back again
        drawPlayerCards(1);
        drawPlayerCards(2);

        //Reset the picked and dropped card selection
        pickCardImage.setImageResource(android.R.drawable.screen_background_light_transparent);
        pickCardImage.setTag("notSelected");
        dropCardImage.setImageResource(android.R.drawable.screen_background_light_transparent);
        dropCardImage.setTag("notSelected");


        //Setting up the click listeners back again
        setUpClickListeners();



        //Draws other information, has to be called after switching turn (because the information displayed includes whose turn it is)
        drawInformation();

        if (theRound.checkIfWon(1)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Player 1 can go out now.");
            alert.show();
        } else if (theRound.checkIfWon(2)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Player 2 can go out now.");
            alert.show();
        }

    }

    //onClick method for goOut button
    public void goOutButtonClick(View view) throws InterruptedException {

        //Which player is playing
        final int whichPlayer = Character.getNumericValue(theRound.getWhosTurn().charAt(7));

        //Get if that player has won
        boolean hasWon = theRound.checkIfWon(whichPlayer);

        //Send them an alert based on whether they have won or not
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if (hasWon) {
            String scoreCalculationLogic = theRound.calculateScores();
            AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
            alertDialog.setTitle("Player " + whichPlayer + " has gone out.");
            alertDialog.setMessage("The other player was awarded a score penalty of " + scoreCalculationLogic);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Next round",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            roundNo++;
                            if(roundNo == 12) {
                                Intent gameEndIntent = new Intent(GameActivity.this, gameEnd.class);
                                String scoreInfo = theRound.scoreInfo();
                                gameEndIntent.putExtra("info", scoreInfo);
                                startActivity(gameEndIntent);
                            }
                            theRound.whosTurn = "Player " + whichPlayer;
                            if(whichPlayer == 1) {
                                whoWon = theRound.player1.whatAmI;
                            }
                            else if(whichPlayer == 2) {
                                whoWon = theRound.player2.whatAmI;
                            }
                            dialog.dismiss();
                            GameActivity.super.recreate();
                        }
                    });
            alertDialog.show();

        } else {
            alert.setMessage("Player " + whichPlayer + "  cannot go out yet.");
        }
        alert.show();
    }

    //onClick method for goOut button
    public void helpButtonClick(View view) throws InterruptedException {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String alertMessage = "";
        if (pickCardImage.getTag().equals("notSelected") && dropCardImage.getTag().equals("notSelected")) {
            brain thinker = new brain(theRound, roundNo, Integer.parseInt(Character.toString(theRound.getWhosTurn().charAt(7))));
            intStrPair pickCard = thinker.pickCardThink();
            String dropCard = thinker.dropCardThink();
            alertMessage = "You have not made any selection on which card you may want to pick or which you may want to drop. \n";
            if (!pickCard.second.equals("draw")) {
                alertMessage = alertMessage + "I tried the card from discard pile at each hand position and it turns out picking from discard pile and removing " + pickCard.second + " makes a positive difference towards going out.";


            } else if (pickCard.second.equals("draw")) {
                alertMessage = alertMessage + " I tried the card from discard pile at each hand position and it made no positive difference towards going out. \nSo it is better that you pick from the draw pile.";
                alertMessage = alertMessage + " Considering all the cards you will have in hand from making the above pick, you may want to drop " + dropCard;
            }



        }
        else if(!pickCardImage.getTag().equals("notSelected") && !pickCardImage.getTag().equals("draw")) {
            brain thinker = new brain(theRound, roundNo, Integer.parseInt(Character.toString(theRound.getWhosTurn().charAt(7))));
            intStrPair toDrop = thinker.dropCardThink((String)pickCardImage.getTag());
            alertMessage = "Based on your pick selection of " + pickCardImage.getTag() + " , it is best to drop " + toDrop.second + " because it resulted in the least score.";

        }
        alert.setMessage(alertMessage);
        alert.show();
    }



}
