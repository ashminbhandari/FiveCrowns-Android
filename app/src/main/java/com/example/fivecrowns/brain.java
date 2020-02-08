package com.example.fivecrowns;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class brain {

    //The round from which the details of the round will be gotten
    private round round;

    //The round no
    private int roundNo;

    //The player no
    private int playerNo;

    //Constructing the brain to think upon the round
    public brain(round round, int roundNo, int playerNo) {
        this.round = round;
        this.roundNo = roundNo;
        this.playerNo = playerNo;
    }

    /* *********************************************************************
Function Name: 'think'
Purpose: 'think' takes the card passed into it into consideration along with the draw pile
and the discard pile (whose information it gets from the round class), to make a decision
on which pile to pick from and which card to drop.
Parameters: (a). 'theCollection', a vector of cards passed by value. It holds the collection of cards that make up
a player's hand, that the function is to consider in making it's decision.
(b). 'mode', a string which signifies the two modes of thinking made available by this function, namely regular and extended.
Regular mode will not look at the draw pile in making it's decision about which card to pick and which to drop, extended mode will.
Return Value: A pair of integer and a card. The integer value can be 0 or 1. 0 means it is best to pick from the discard pile, 1 means
it's best to pick from the draw pile. Meanwhile, the latter of the pair, the card returned, is the card to be dropped by the player.
Local Variables: (a). 'r', the singleton instance of the round class. Through this instance, 'think' can gather information (the state) about the
what is happening in the round being played.
(b). 'discardPileCard', it is a card object. Simply, the card on top of the discard pile.
(c). 'drawPileCard', it is a card object. Simply, the card on top of the draw pile.
(d). 'copyCollection' & 'tempCopy', a vector of card objects, copy of the collection of cards sent as a parameter to the function, any changes 'think' needs to make whilst
in the process of making it's decision is made to the copy instead of the original. If the collection needs to be reset back to the original,
we have the original collection intact.
(e). 'cardMatrixSum' & 'matrixSum', they are vectors of a pair of integer and card. Having tried out the card from discard or draw pile at each hand position,
it 'cardMatrixSum' holds the matrix sum value (can be thought of as the score corresponding to putting the card at that specific hand position),
and the card itself.
(f). 'smallest', it is again a vector of a pair of integer and card. It is the 'cardMatrixSum' that generated the lowest score (or matrix sum).
So it is the best choice to make.
(g). 'temp', it is an object of 'newCardCollection' type. The newCardCollection class helps gather information on what cards in the hand are books, what are runs, how many cards remain,
and what the matrix sum (score) is.
(h). 'cardToDrop', holds the card that is to be dropped by the player. The selection as a result of thought.
(i). 'remCards', vector of strings, the cards that remain without a book or a run match in the user's hand
(j). 'remCardsAsCards', the remaining cards from above as card objects instead of strings
Algorithm: (1). Get the card from the discard pile
(2). Put the discard pile card at each hand position and store the matrix sums (or the scores) of doing so in each hand position
(3). Sort the scores in ascending order
(4). Iterate through the sorted scores, if a score is found such that the score is lesser than the score of the hand as it was originally without adding the discard pile card and removing a card,
then return these values i.e. a pair of integer and card, the integer is 0, which means pick from the discard pile, and the card is the card which is the best to drop
(5). If nothing positive came out of taking the discard pile card and putting it at each hand position by replacing the card at that hand position then it is clear that
one needs to draw from the draw pile.
(6). If extended mode was chosen, then the above algorithm as done with the discard pile card is also done with the draw pile card and the appropriate values returned
(7). If not, then the strategy simply tells you to remove the card with the highest score penalty.
********************************************************************* */
    public intStrPair pickCardThink() {

        Log.d("Pick card think", "here");

        //Getting the player cards as string
        Vector<String> playerCardsAsString= round.getPlayerCardsAsString(playerNo);

        //Getting a copy of player cards as string, in order to make changes
        Vector<String> copyPlayerCardsAsString = round.getPlayerCardsAsString(playerNo);

        //Getting the discard pile cards as string
        String discardPileCard = round.getDiscardPileAsString().get(0);

        //Vector that will hold the matrix sum of all replacements, this will be used to make decision
        ArrayList<intStrPair> matrixSumAndCorrespondingCards = new ArrayList<>();

        //Putting the discard pile in for each position
        for(int i = 0; i < playerCardsAsString.size(); i++) {
            //The card to be removed
            String toRemove = copyPlayerCardsAsString.get(i);

            //Actually erasing the card at index i
            copyPlayerCardsAsString.remove(i);

            //Having erased the card, adding the discard pile card to the collection
            copyPlayerCardsAsString.add(discardPileCard);

            int matrixSum = getMatrixCheckResult(copyPlayerCardsAsString);

            matrixSumAndCorrespondingCards.add(new intStrPair(matrixSum, toRemove));

            //Setting back the collecction to it's original state
            copyPlayerCardsAsString = playerCardsAsString;
        }
        Log.d("Returning", getPickResult((matrixSumAndCorrespondingCards)).second);
        return getPickResult(matrixSumAndCorrespondingCards);
    }


    public int getMatrixCheckResult(Vector<String> hand) {

        //The collection that will be analyzed, we will put discard pile card in for each position\
        Vector<card>collection = new Vector<card>();

        //Make a collection of cards from the string values
        for(int i = 0; i < hand.size(); i++) {
            collection.add(new card(Character.toString(hand.get(i).charAt(0)), Character.toString(hand.get(i).charAt(1))));
        }

        //Running it through the analyzer
        analyzer a = new analyzer(collection, roundNo);

        //Checking the matrix using the analyzer
        a.checkMatrix();

        //Returning the matrix sum of the hand given having checked it for books and runs
        return a.matrixSum();
    }

    //Gets the result of the pick as a string given matrix sums and their corresponding values, picks the lowest one
    public intStrPair getPickResult(ArrayList<intStrPair> vec) {

        //An arbitrary number to compare to
        int lowest = 100000000;

        //Lowest integer, string pair
        intStrPair lowestPair = null;

        //Sorting the vector
        for(int i = 0; i < vec.size(); i++) {
            if(vec.get(i).first<lowest) {
                lowest = vec.get(i).first;
                lowestPair = vec.get(i);
            }
        }

        int originalMatrixSum = getMatrixCheckResult(round.getPlayerCardsAsString(playerNo));

        if(originalMatrixSum <= lowest) {
            return (new intStrPair(originalMatrixSum, "draw"));
        }
        else {
            return lowestPair;
        }
    }

    public String dropCardThink() {
        //Getting the player cards as string
        Vector<String> playerCardsAsString = round.getPlayerCardsAsString(playerNo);

        //Getting a copy of player cards as string, in order to make changes
        Vector<String> copyPlayerCardsAsString = round.getPlayerCardsAsString(playerNo);

        int lowest = 100000000;

        String lowestCard = "";

        for(int i = 0; i < copyPlayerCardsAsString.size(); i++) {
            String cardRemoved = copyPlayerCardsAsString.remove(i);

            int matrixSum = getMatrixCheckResult(copyPlayerCardsAsString);

            if(matrixSum < lowest) {
                lowest = matrixSum;
                lowestCard = cardRemoved;
            }

            copyPlayerCardsAsString = playerCardsAsString;
        }

        return lowestCard;

    }

    public intStrPair dropCardThink(String whichToDrop) {

        Log.d("Pick card think", "here");

        //Getting the player cards as string
        Vector<String> playerCardsAsString= round.getPlayerCardsAsString(playerNo);

        //Getting a copy of player cards as string, in order to make changes
        Vector<String> copyPlayerCardsAsString = round.getPlayerCardsAsString(playerNo);


        //Vector that will hold the matrix sum of all replacements, this will be used to make decision
        ArrayList<intStrPair> matrixSumAndCorrespondingCards = new ArrayList<>();

        //Putting the discard pile in for each position
        for(int i = 0; i < playerCardsAsString.size(); i++) {
            //The card to be removed
            String toRemove = copyPlayerCardsAsString.get(i);

            //Actually erasing the card at index i
            copyPlayerCardsAsString.remove(i);

            //Having erased the card, adding the discard pile card to the collection
            copyPlayerCardsAsString.add(whichToDrop);

            int matrixSum = getMatrixCheckResult(copyPlayerCardsAsString);

            matrixSumAndCorrespondingCards.add(new intStrPair(matrixSum, toRemove));

            //Setting back the collecction to it's original state
            copyPlayerCardsAsString = playerCardsAsString;
        }
        Log.d("Returning", getPickResult((matrixSumAndCorrespondingCards)).second);
        return getPickResult(matrixSumAndCorrespondingCards);
    }


}
