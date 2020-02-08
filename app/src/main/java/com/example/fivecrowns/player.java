package com.example.fivecrowns;

import android.util.Log;

import java.util.Vector;

public class player {

    //What is this player
    String whatAmI = "";

    //The player no
    int playerNo;

    //Vector to store the cards in players hands
    private Vector<card> hand = new Vector<card>();

    //Score of the player
    private int score = 0;

    player(int playerNo) {
        this.playerNo = playerNo;
    }



    /* *********************************************************************
    Function Name: addToHand
    Purpose: Adds provided card to player hand
    ********************************************************************* */
    public void addToHand(card c) {
        hand.add(c);
    }


    /* *********************************************************************
    Function Name: getHandAsString()
    Returns: The hand of player as a vector of strings
    Purpose: Gets the hand of the player as string so that other objects know what cards a player has
    ********************************************************************* */
    public Vector<String> getHandAsString() {

        //Temporary vector to store the cards as string
       Vector <String> temp = new Vector <String> ();

       //Iterating through and adding the strings to the temp vector
        for(int i =0; i < hand.size(); i++) {
            temp.add(hand.get(i).getCardAsString());
        }

        //Returning the temp vector
        return temp;
    }

    /* *********************************************************************
    Function Name: getHand()
    Returns: The hand of player
    Purpose: Gets the hand of the player
    ********************************************************************* */
    public Vector<card> getHand() {
        return hand;
    }

    /* *********************************************************************
    Function Name: dropCard
    Purpose: Overriden by it's children: human and computer.
    ********************************************************************* */
    public void dropCard(String c) {

        //Remove the card c from hand if it exists
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getCardAsString().equals(c)) {
                hand.remove(i);
                return;
            }
        }


    }



    /* *********************************************************************
    Function Name: pickCard
    Purpose: Overriden by it's children: human and computer.
    ********************************************************************* */
    public void pickCard (card c) {

        //Pick from a given pile
        addToHand(c);


    }

    //pickCard for computer
    public String pickCard(round theRound, int roundNo, boolean isComputer, card drawPileCard, card discardPileCard) {
        if(isComputer) {
            brain a = new brain(theRound, roundNo, playerNo);
            intStrPair compPick = a.pickCardThink();
            if(!compPick.second.equals("draw")) {
                addToHand(discardPileCard);
            }
            else if(compPick.second.equals("draw")){
                addToHand(drawPileCard);
            }
            return compPick.second;
        }
        return "";
    }


    //Reset player card
    public void resetCards() {
        hand.clear();
    }

    //Add to player score
    public void addScore(int score) {
        this.score = this.score + score;
    }

    //Gets the player score
    public int getScore() {
        return score;
    }

    //Clear player hand
    public void clearHand() {
        hand.clear();
    }




}