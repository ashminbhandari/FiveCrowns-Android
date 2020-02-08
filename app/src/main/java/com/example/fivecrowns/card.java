package com.example.fivecrowns;

import android.util.Log;

public class card {
    String suit;
    String rank;

    /* *********************************************************************
    Function Name: card
    Purpose: The constructor for the card class, it initializes the card to '-1-1' value.
    ********************************************************************* */
    public card() {
        suit = "-1";
        rank = "-1";
    }

   /* *********************************************************************
    Function Name: card
    Purpose: Constructor for the card class that takes in two string arguments to initialize the card object as
    Parameters: a. rank, string value of the rank of the card
    b. suit, string value of the suit of the card
    ********************************************************************* */
   public card(String rank, String suit) {
       this.suit = suit;
       this.rank = rank;
   }

    /* *********************************************************************
    Function Name: getSuit
    Purpose: A public getter of the suit of the card instance
    Return Value: suit of the card, as a string value
    ********************************************************************* */
    public String getSuit() {
        return suit;
    }

    /* *********************************************************************
    Function Name: getRank
    Purpose: A public getter of the rank of the card instance
    Return Value: rank of the card, as a string value
    ********************************************************************* */
    public String getRank() {
        return rank;
    }

    /* *********************************************************************
    Function Name: getCardAsString
    Purpose: Gets a string value of the card, for example JS is J of spades and so on
    Return Value: A string representation of the card instance
    ********************************************************************* */
    public String getCardAsString() {
        return rank + suit;
    }

    /* *********************************************************************
    Function Name: getRankValue
    Purpose: Gets the integer value of the rank of the card
    Return Value: Integer value of the rank of the card
    Algorithm: Run through a bunch of if's and return the value according to that
    ********************************************************************* */
    public int getRankValue() {

        Log.d("Converting for", rank + suit);
        if (rank.equals("X")) {
            return 10;
        }
        else if (getCardAsString().equals("JS") || getCardAsString().equals("JC") || getCardAsString().equals("JD") || getCardAsString().equals("JH") || getCardAsString().equals("JT")) {
            return 11;
        }
        else if (rank.equals("Q")) {
            return 12;
        }
        else if (rank.equals("K")) {
            return 13;
        }
        else if(getCardAsString().equals("J1") || getCardAsString().equals("J2") || getCardAsString().equals("J3")) {
            return 50;
        }
        else if(rank.equals("D")) {
            return 0;
        }
        else {
            return Integer.parseInt(rank);
        }
    }

    /* *********************************************************************
    Function Name: getSuitValue
    Purpose: Gets the integer value of the suit of the card
    Return Value: Integer value of the suit of the card
    Algorithm: Run through a bunch of if's and return the value according to that
    ********************************************************************* */
    public int getSuitValue() {
        if(suit.equals("S")) {
            return 0;
        }
        else if (suit.equals("C")) {
            return 1;
        }
        else if (suit.equals("D")) {
            return 2;
        }
        else if (suit.equals("H")) {
            return 3;
        }
        else if (suit.equals("T")) {
            return 4;
        }
        return -1; //For error
    }

    /* *********************************************************************
    Function Name: isJoker
    Purpose: Checks whether or not the card is a joker
    Return value: A bool which signifies whether the card is a joker or not
    ********************************************************************* */
    public Boolean isJoker() {
        if(getCardAsString().equals("J1") || getCardAsString().equals("J2") || getCardAsString().equals("J3")) {
            return true;
        }
        return false;
    }
}
