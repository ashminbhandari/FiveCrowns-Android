package com.example.fivecrowns;

import java.util.Vector;
import java.util.Collections;

public class deck {

    Vector <card> collection = new Vector <card>();

    /* *********************************************************************
    Function Name: deck
    Purpose: Constructor for the deck
    Local Variables: suit[], an array that holds the letter string values of suit: S, C, D, H and T
    rank[], an array that holds the letter string values of the rank starting from 3 and ending at K
    Algorithm: Simply pushes back into the deck, all the cards that are a part of it including three jokerse
    ********************************************************************* */
    public deck() {
        //Array that holds the letter string values of the suits available for this game
        String suit[] = {"S", "C", "D", "H", "T"}; //S - spades, C - clubs, D - diamonds, H - hearts, T - tridents

        //Array that holds the letter string values of the ranks available for this game
        String rank[] = {"3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K"}; //Cards 3 through K - King

        //Iterate through string and rank to create the deck by pushing back into 'collection', the official vector of cards which holds the actual deck for this class
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 11; j++) {
                    card temp = new card(rank[j], suit[i]);
                    collection.add(temp);
                }
            }

            //Set up three jokers and push them back into the collection. Thus, we have a complete deck
            collection.add(new card("J", "1"));
            collection.add(new card("J", "2"));
            collection.add(new card("J", "3"));
        }
    }

    /* *********************************************************************
    Function Name: shuffleDeck
    Purpose: Simply, shuffles the deck
    Algorithm: Uses shuffle provided by collections to shuffle the deck
    ********************************************************************* */
    public void shuffleDeck() {
        Collections.shuffle(collection);
    }

    /* *********************************************************************
    Function Name: getCardAt
    Purpose: Gets the card at certain position in the deck, it gets the value not the reference
    Return Value: The card object value rather than reference
    ********************************************************************* */
    public card getCardAt(int index) {
        return collection.get(index);
    }

    /* *********************************************************************
    Function Name: removeCardAt
    Purpose: Removes the card at given index
    Algorithm: Uses the remove functionality provided by vectors
    ********************************************************************* */
    public void removeCardAt(int index) {
        collection.remove(index);
    }

    /* *********************************************************************
    Function Name: getDeckSize
    Purpose: Gets the size of the deck
    Algorithm: Simply, return the size of the collection (the vector that holds all the cards in the deck)
    ********************************************************************* */
    public int getDeckSize() {
        return collection.size();
    }
}
