package com.example.fivecrowns;

import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.Vector;

public class round implements Serializable {

    //The round number
    int roundNo;

    //Player number 1
    static player player1 = new player(1);

    //Player number 2
    static player player2 = new player(2);

    //The draw pile
    Vector<card> drawPile = new Vector<card>();

    //The discard pile
    Vector<card> discardPile = new Vector<card>();

    //String to store who's turn it is
    static String whosTurn = "Player 1";

    /* *********************************************************************
    Function Name: dealCards
    Parameters: Deck object from which to deal the cards
    Purpose: Deals cards to players
    Algorithm: Shuffle the deck and then give out the number of cards in use in this specific round to both the players, i.e. deal the cards
    ********************************************************************* */
    public void dealCards(deck d) {
        //Shuffle the deck
        d.shuffleDeck();

        //Add the two players to a list for easy initialization
        Vector<player> playersList = new Vector<player>();
        playersList.add(player1);
        playersList.add(player2);

        //Giving out cards to players in the players list
        for (int i = 0; i < playersList.size(); i++) {
            for (int j = 0; j < roundNo + 2; j++) {

                //Give the card on top of deck to the player
                playersList.get(i).addToHand(d.getCardAt(0));

                //Remove the card on top of deck
                d.removeCardAt(0);
            }
        }
    }

    /* *********************************************************************
    Function Name: instantiatePiles()
    Parameters: Deck object from which to instantiate the piles
    Purpose: Used during setting up the round, this function will set up the draw and discard piles
    Algorithm: All the remaining cards in the deck after dealing will be put into the draw pile then the top of the draw pile will be put into the discard pile
    ********************************************************************* */
    public void instantiatePiles(deck d) {

        //Iterate through the deck and put the cards into the draw pile
        for (int i = 0; i < d.getDeckSize() - 1; i++) {

            //Add top card to draw pile
            drawPile.add(d.getCardAt(0));

            //Remove top card from draw pile
            d.removeCardAt(0);
        }

        //The last remaining card will be given to the discard pile
        discardPile.add(d.getCardAt(0));
    }

    /* *********************************************************************
    Function Name: round
    Purpose: Constructor for the game round, it sets up a round
    Algorithm: Constructs the round with appropriate game number, deals cards, instantiates piles and players
    ********************************************************************* */
    public round(int roundNo) {

        //Clear player hands
        player1.clearHand();
        player2.clearHand();

        //Set the round no
        this.roundNo = roundNo;

        //Create a deck
        deck d = new deck();

        //Deal the cards from the deck
        dealCards(d);

        //Instantiate piles
        instantiatePiles(d);
    }

    /* *********************************************************************
   Function Name: getPlayerCardsAsString
   Return: Vector of strings containing the cards in player hands as strings, if an invalid player number is given returns empty vector
   Purpose: So that the game activity knows what cards is in the hand of a player
   ********************************************************************* */
    public Vector<String> getPlayerCardsAsString(int number) {
        if (number == 1) {
            return player1.getHandAsString();
        } else if (number == 2) {
            return player2.getHandAsString();
        } else {
            Log.e("Error:", "The provided player number doesn't exist");
            System.exit(-1);
        }
        return null;
    }

    /* *********************************************************************
   Function Name: getDiscardPileAsString
   Return: Gets the cards in the discard pile as a vector of strings
   Purpose: So that other classes can know what cards the discard pile has
   ********************************************************************* */
    public Vector<String> getDiscardPileAsString() {

        //Creating a temporary string vector to store the cards as strings
        Vector<String> discardPileAsString = new Vector<String>();

        for (int i = 0; i < discardPile.size(); i++) {
            discardPileAsString.add(discardPile.get(i).getCardAsString());
        }
        return discardPileAsString;
    }

    //Drop player card, calls upon player functionality to do so
    public void dropCard(int playerNo, String c) {
        if(playerNo == 1) {
            player1.dropCard(c);
        }
        else if(playerNo == 2) {
            player2.dropCard(c);
        }

        //Add the card to the discard pile
        discardPile.insertElementAt(new card(Character.toString(c.charAt(0)), Character.toString(c.charAt(1))), 0);
    }

    //Pick from either the draw pile or the discard pile
    public void pickCard(int playerNo, String whichPile) {

        if(playerNo == 1) {
            if(whichPile.equals("draw")) {
                player1.pickCard( drawPile.get(0));
                drawPile.remove(0);
            }
            else if(whichPile.equals("discard")) {
                player1.pickCard(discardPile.get(0));
                discardPile.remove(0);
            }
        }
        else if (playerNo == 2) {
            if(whichPile.equals("draw")) {
                player2.pickCard(drawPile.get(0));
                drawPile.remove(0);
            }
            else if(whichPile.equals("discard")) {
                player2.pickCard(discardPile.get(0));
                discardPile.remove(0);
            }
        }
    }

    //Take turn for computer
    public void takeTurn(int playerNo) {
        if(playerNo == 1) {
            brain a = new brain(this, roundNo, playerNo);
            intStrPair b = a.pickCardThink();
            if(b.second.equals("draw")) {
                player1.addToHand(drawPile.get(0));
                Log.d("Picked: ", drawPile.get(0).getCardAsString());
                drawPile.remove(0);
                String toDrop = a.dropCardThink();
                player1.dropCard(toDrop);
                discardPile.insertElementAt(new card(Character.toString(toDrop.charAt(0)), Character.toString(toDrop.charAt(1))), 0);
                Log.d("Dropped: ", toDrop);
            }
            else if(!b.second.equals("draw")) {
                player1.addToHand(discardPile.get(0));
                Log.d("Picked: ", discardPile.get(0).getCardAsString());
                discardPile.remove(0);
                String toDrop = a.dropCardThink();
                player1.dropCard(toDrop);
                Log.d("Dropped: ", toDrop);
                discardPile.insertElementAt(new card(Character.toString(toDrop.charAt(0)), Character.toString(toDrop.charAt(1))), 0);
            }
        }
        else if(playerNo == 2) {
            brain a = new brain(this, roundNo, playerNo);
            intStrPair b = a.pickCardThink();
            if(b.second.equals("draw")) {
                player2.addToHand(drawPile.get(0));
                Log.d("Picked: ", drawPile.get(0).getCardAsString());
                drawPile.remove(0);
                String toDrop = a.dropCardThink();
                player2.dropCard(toDrop);
                discardPile.insertElementAt(new card(Character.toString(toDrop.charAt(0)), Character.toString(toDrop.charAt(1))), 0);
                Log.d("Dropped: ", toDrop);
            }
            else if(!b.second.equals("draw")) {
                player2.addToHand(discardPile.get(0));
                Log.d("Picked: ", discardPile.get(0).getCardAsString());
                discardPile.remove(0);
                String toDrop = a.dropCardThink();
                player2.dropCard(toDrop);
                discardPile.insertElementAt(new card(Character.toString(toDrop.charAt(0)), Character.toString(toDrop.charAt(1))), 0);
                Log.d("Dropped: ", toDrop);
            }
        }
    }


    //Get whos turn it is
    public String getWhosTurn() {
        return whosTurn;
    }

    //Switch turn
    public void switchTurn() {
        if (whosTurn.equals("Player 1")) {
            whosTurn = "Player 2";
        }
        else if(whosTurn.equals("Player 2")) {
            whosTurn = "Player 1";
        }
    }

    //Check if a player has won
    public boolean checkIfWon(int playerNo) {
        if(playerNo == 1) {
            analyzer p1 = new analyzer(player1.getHand(), roundNo);
            return p1.checkMatrix();
        }
        else if(playerNo == 2) {
            analyzer p2 = new analyzer(player2.getHand(), roundNo);
            return p2.checkMatrix();
        }
        return false;
    }

    //Check scores
    public String calculateScores() {
        int player1Score = 0;
        analyzer p1 = new analyzer(player1.getHand(), roundNo);
        int player2Score = 0;
        analyzer p2 = new analyzer(player2.getHand(), roundNo);

        //Score calculation logic
        String scoreCalculationLogic = "";

        if(p1.checkMatrix()) {

            //Check player 2's matrix
            p2.checkMatrix();

            //Get the cards that remain
            Vector<String> cardsRemaining = p2.getCardsRemaining();

            scoreCalculationLogic = "";

            //Get the values of cards in hand
            for(int i = 0; i < cardsRemaining.size(); i++) {
                Log.d("Remaining card ", cardsRemaining.get(i));
                card temp = new card(Character.toString(cardsRemaining.get(i).charAt(0)), Character.toString(cardsRemaining.get(i).charAt(1)));
                player2Score = player2Score + temp.getRankValue();
                scoreCalculationLogic = scoreCalculationLogic + " " + cardsRemaining.get(i) + " = " + temp.getRankValue() + " ,";
            }

            //Get the count of special cards that remain
            int specialCardsRemaining = p2.specialCardsRemaining();
            int wildcardCount = p2.originaWildcardCount();

            //First expend wildcards since they cost the least
            for(int i = 0; i < specialCardsRemaining; i++) {
                if(wildcardCount > 0) {
                    scoreCalculationLogic = scoreCalculationLogic + " Wildcard = 20, ";
                    player2Score = player2Score + 20;
                    wildcardCount--;
                }
                else {
                    scoreCalculationLogic = scoreCalculationLogic + " Joker = 50, ";
                    player2Score = player2Score + 50;
                }
            }

            for(int i =0; i < player2.getHand().size(); i++) {
                Log.d("Hand player 2: ", player2.getHand().get(i).getCardAsString());
            }
        }
        if(p2.checkMatrix()) {
            //Check player 2's matrix
            p1.checkMatrix();

            //Get the cards that remain
            Vector<String> cardsRemaining = p1.getCardsRemaining();

            scoreCalculationLogic = "";

            //Get the values of cards in hand
            for(int i = 0; i < cardsRemaining.size(); i++) {
                Log.d("Remaining card ", cardsRemaining.get(i));
                card temp = new card(Character.toString(cardsRemaining.get(i).charAt(0)), Character.toString(cardsRemaining.get(i).charAt(1)));
                player1Score = player1Score + temp.getRankValue();
                scoreCalculationLogic = scoreCalculationLogic + " " + cardsRemaining.get(i) + " = " + temp.getRankValue() + " ,";
            }

            //Get the count of special cards that remain
            int specialCardsRemaining = p1.specialCardsRemaining();
            int wildcardCount = p1.originaWildcardCount();

            //First expend wildcards since they cost the least
            for(int i = 0; i < specialCardsRemaining; i++) {
                if(wildcardCount > 0) {
                    scoreCalculationLogic = scoreCalculationLogic + " Wildcard = 20, ";
                    player1Score = player1Score + 20;
                    wildcardCount--;
                }
                else {
                    scoreCalculationLogic = scoreCalculationLogic + " Joker = 50, ";
                    player1Score = player1Score + 50;
                }
            }
            for(int i =0; i < player1.getHand().size(); i++) {
                Log.d("Hand player 1: ", player1.getHand().get(i).getCardAsString());
            }

            for(int i =0; i < p1.getCardsRemaining().size(); i++) {
                Log.d("Player 1 remaining:", p1.getCardsRemaining().get(i));
            }


        }

        Log.d("Player 1 score is ", " " + player1Score);
        Log.d("Player 2 score is", " " + player2Score);
        player1.addScore(player1Score);
        player2.addScore(player2Score);

        return scoreCalculationLogic;
    }

    //Gets player score
    public int getPlayerScore(int playerNo) {
        if(playerNo == 1) {
            return player1.getScore();
        }
        else if(playerNo == 2) {
            return player2.getScore();
        }

        //For invalid input
        return -1;
    }


    //coin toss result
    public void coinTossResult(String whoWon) {
        if(whoWon.equals("Human")) {
            player1.whatAmI = "Human";
            player2.whatAmI = "Computer";
        }
        else if(whoWon.equals("Computer")) {
            player1.whatAmI ="Computer";
            player2.whatAmI="Human";
        }
    }


    //What is player
    public String whatIs(int playerNo) {
        if(playerNo == 1) {
            return player1.whatAmI;
        }
        else if(playerNo == 2) {
            return player2.whatAmI;
        }
        return "";
    }

    public String scoreInfo() {
        String scoreInfo = "";
        scoreInfo = scoreInfo + player1.getScore() + " " + player2.getScore();

        if(player1.getScore() < player2.getScore()) {
            scoreInfo = " " + "Player 1 has won the game.";
        }
        else {
            scoreInfo = " " + "Player 2 has won the game.";
        }

        scoreInfo = scoreInfo + " The score tallies are: Player 1 =  "  + player1.getScore() + " Player 2: " + player2.getScore();
        return scoreInfo;
    }
}

