package com.example.fivecrowns;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

public class analyzer {

    //The collection of cards to be analyzed
    private Vector<card> collection;

    //The joker count
    private int jokerCount;

    //The wildcard count
    private int wildcardCount;

    //Count of special cards which is joker + wildcards
    private int specialCards;

    //The matrix, constructed from the given card collection
    private int theMatrix[][] = new int[5][11];

    //Current round
    private int roundNo;

    //Vector to hold the books made in the hand
    Vector<String> booksMade;

    //Vector to hold the runs made in the hand
    Vector<String> runsMade;

    //Vector to hold the cards that remain in hand
    Vector<String> cardsRemaining;

    //Constructor for the analyzer class
    public analyzer(Vector<card> collection, int roundNo) {

        //Sets up the analyzer with the given collection and round no
        this.collection = collection;
        this.roundNo = roundNo;

        //Initialize the special cards count
        jokerCount = 0;
        wildcardCount = 0;
        specialCards = 0;

        //Initialize the runs made, books made and cardsRemaining vectors
        booksMade = new Vector<String>();
        runsMade = new Vector<String>();
        cardsRemaining = new Vector<String>();

        //Constructs the matrix
        constructMatrix();
    }

    /* *********************************************************************
    Function Name: constructMatrix
    Purpose: Constructs a matrix based on the collection that this instance was set up as. The horizontal values stand for the rank and the vertical values stand for the suit.
    Local Variables: If any local variable is an array, structure or object, list its name and the purpose for which it is used.
    Algorithm: Sets up the matrix according to the integer rank value and the integer suit value of the card
    ********************************************************************* */
    private void constructMatrix() {

        //The matrix all initialized as 0's
        theMatrix = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

        //Only adding cards that are not jokers or wildcards to the matrix. Incrementing jokerCount and wildcardCount if any found
        for (card c : collection) {
            if (!c.isJoker() && c.getRankValue() != roundNo + 2) {
                int suitIndex = c.getSuitValue();
                int rankIndex = c.getRankValue() - 3;

                //-1 for suitIndex means an error occurred
                if (suitIndex != -1) {
                    theMatrix[suitIndex][rankIndex]++;
                }
            }

            //Checking if the card is a joker
            if (c.isJoker()) {

                //Increment the joker count
                jokerCount++;
            }

            //Checking if the card is a wildcard
            if (c.getRankValue() == roundNo + 2) {

                //Increment the wildcard count
                wildcardCount++;
            }
        }

        specialCards = jokerCount + wildcardCount;

        //Print the matrix
        // Loop through all rows
        String row = "";
        for (int i = 0; i < theMatrix.length; i++) {
            // Loop through all elements of current row
            for (int j = 0; j < theMatrix[i].length; j++) {
                row = row + " " + theMatrix[i][j];
            }
            System.out.println(" " + row);
            row = "";
        }

        System.out.println("Wild card count:" + Integer.toString(wildcardCount));
        System.out.println("Joker count:" +Integer.toString(jokerCount));
        System.out.println("Special cards:" +Integer.toString(specialCards));


        for (int i = 0; i < theMatrix.length; i++) {
            // Loop through all elements of current row
            for (int j = 0; j < theMatrix[i].length; j++) {
                List<pair> xMeld = findHorizontalMeld(i,j);
                List<pair> yMeld = findVerticalMeld(j);
                if(xMeld.size() >= 1 || yMeld.size() >= 1) {
                    System.out.println("At" + i + ", " + j + " : " + "xTotal = " + Integer.toString(xMeld.size()) + "yTotal = " + Integer.toString(yMeld.size()));
                }
            }
        }

        System.out.println("Matrix sum is " + Integer.toString(matrixSum()));
    }

    //Finds a horizontal meld
    private List<pair> findHorizontalMeld(int i, int j) {

        //The list that holds the meld as a co-ordinate pair
        List<pair> xMeld = new ArrayList<>();

        //Start at the jIndex provided
        int jIndex = j;

        //Do while a 0 is not hit, moving towards the right. A 0 is hit means that the run ends here.
        do {

            //If hit 0, break out of the loop, we hit the end of the run
            if (theMatrix[i][jIndex] == 0) {
                break;
            }

            //Push back the indexes of the horizontal meld we are currently finding
            xMeld.add(new pair(i, jIndex));

            //Increase the jIndex so as to move more towards the right
            jIndex++;

            //If we hit the end of the matrix, exit
        } while (jIndex < theMatrix[i].length);

        //We must also search towards the left from the index that was provided
        //Only decrease jIndex for a search towards the left if the jIndex is greater than 0. Otherwise, we run out of bounds
        if (j > 0) {
            jIndex = j - 1;
        }

        //Do while a 0 is not hit, moving towards the left
        do {
            if (theMatrix[i][jIndex] == 0) {
                break;
            }

            //Adding to the index of the run we are currently finding
            xMeld.add(new pair(i, jIndex));

            //Decreasing the jIndex so as to move towards the left
            jIndex--;

            //If we hit the end of the matrix quit out
        } while (jIndex >= 0);

        return xMeld;
    }

    //Finds the vertical meld
    private List<pair> findVerticalMeld(int j) {

        //The list that holds the meld as list of coordinate pairs
        List<pair> yMeld = new ArrayList<>();

        //Check for vertical meld, i.e. a book
        //Start at iIndex of 0
        int iIndex = 0;


        //Move towards the vertical end of the matrix, add up the values of the matrix along the vertical
        while(iIndex < theMatrix.length) {
            if(theMatrix[iIndex][j] >= 1) {
                for(int i = 0; i < theMatrix[iIndex][j]; i++) {
                    yMeld.add(new pair(iIndex, j));
                }
            }
            iIndex++;
        }

        return yMeld;
    }

    /* *********************************************************************
    Function Name: matrixSum
    Purpose: It adds up all the values at all the indexes of the matrix
    Return Value: Integer value of the above genereated sum, the sum of all integer values within the matrix
    Local Variables: matrixSum, integer that holds the sum of the matrix
    Algorithm: Iterate through the matrix and add up the integer values at all indexes of the matrix, return the sum at the end
    ********************************************************************* */
    public int matrixSum() {

        //Initialize the sum as 0
        int matrixSum = 0;

        //Iterate through the matrix and add up the value at matrix indexes to the matrix sum
        for(int i =0; i <theMatrix.length; i++) {
            for(int j=0; j<theMatrix[i].length; j++) {
                matrixSum = matrixSum + theMatrix[i][j];
            }
        }

        //Return the matrix sum as from the above calculation
        return matrixSum;
    }

    /* *********************************************************************
    Function Name: updateMatrix
    Purpose: This function will decrease the matrix value at the given index.
    Parameters: (a). meld, a vector of pair of integers and integers, which hold the co ordinates of the indexes to be updated or decreased
    (b). isY, a bool. Since we need a different treatment for the indexes being updated along the vertical, i.e. for books rather than runs, this bool acts a flag
    Algorithm: If the index to be updated is for a run that is successfully identified in the matrix, then we simply decrease the matrix value at that index by 1.
    However, if the index to be updated is for a book that is successfully identified in the matrix, then we set the matrix value at that index to be 0. This is because
    there can be two cards of the same rank and suit for a book.
    ********************************************************************* */
    private void updateMatrix(List<pair> meld, boolean isY) {

        //Iterate through the meld coordinates provided as argument
        for(int i = 0; i < meld.size(); i++) {

            //If the matrix value at the index is greater than or equal to 1, that means a decrement can be done, then proceed
            if(theMatrix[meld.get(i).first][meld.get(i).second] >= 1) {

                //If the meld coordinates provided is for a book then proceed accordingly
                if(isY) {

                    //Set the matrix value at the index to 0
                    theMatrix[meld.get(i).first][meld.get(i).second] = 0;

                    //Since this counts as a book that was made, convert the matrix index to actual card string value and add it to the booksMade vector
                    for(int k = 0; k < theMatrix[meld.get(i).first][meld.get(i).second]; k++) {
                        booksMade.add(matrixIndexToCardString(meld.get(i).first, meld.get(i).second));
                    }
                }

                //Ohterwise, the cooridnates are for a run
                else {

                    //Decrease the matrix value at that index by 1
                    theMatrix[meld.get(i).first][meld.get(i).second]--;

                    //Push the actual card values corresponding to the matrix indexes into the runs made vector
                    runsMade.add(matrixIndexToCardString(meld.get(i).first, meld.get(i).second));
                }
            }
        }
    }

    private void pass(int which, int i, int j) {

        //Get the horizontal meld at index i, j
        List<pair> xMeld = findHorizontalMeld(i, j);

        //Get the vertical meld at index i, j
        List<pair> yMeld = findVerticalMeld(j);

        //If it is the first pass
        if(which == 1) {
            //This is where the prioritizing takes place.
            //If the size of the xMeld is smaller than the size of yMeld, and it is a meld (size 3) then give it the priority, then remove that meld from matrix
            if(xMeld.size() < yMeld.size() && xMeld.size() >= 3) {
                updateMatrix(xMeld, false);
            }

            //Else, if the yTotal is smaller than xTotal (give it the priority) and it is a meld, then remove that meld from the amtrix
            else if(yMeld.size() < xMeld.size() && yMeld.size() >= 3) {
                updateMatrix(yMeld, true);
            }
        }

        //If it is the second pass
        if(which == 2) {

            //Clear the matrix, if is a run
            if(xMeld.size() >= 3) {
                updateMatrix(xMeld, false);
            }

            //Update the matrix, if is a book
            else if(yMeld.size() >= 3) {
                updateMatrix(yMeld, true);
            }
        }
    }

    /* *********************************************************************
    Function Name: passIII
    Purpose: The first pass cleared up all the books and runs that needed prioritizing i.e. since a card can only be part of a book or a run, there can be prioritizing issues.
    The second pass cleared up all the books and runs.
    The third pass, this pass will be for the cards that need jokers and wildcards.
    Algorithm: We gather the books and runs that require a joker. We then let the books or runs that need the least amount of special cards have them.
    ********************************************************************* */
    private void passIII() {
        //Only proceed if there are special cards to expend
        if(specialCards > 0) {

            //How many special cards does this specific run need
            int needJokerCount = 0;

            //At which indexes does this run need it's special cards
            Vector<Vector<pair>> needJokers = new Vector<>();

            //Pushing back an empty pair to start up things
            needJokers.add(new Vector<pair>());

            //Iterate through all the horizontal lines of the matrix and gather all the indexes that need speical cards
            //0 to 5 are the cooridnates along the vertical
            for(int m = 0; m < 5; m++) {

                //n is to be incremented, it is the index along the horizontal
                int n = 0;

                //While we don't hit the matrix's end
                while(n != theMatrix[m].length) {

                    //If a joker is needed, which means that the matrix spot is 0, push it back to the needJokers vector
                    if(theMatrix[m][n] == 0) {
                        needJokers.get(needJokerCount).add(new pair(m,n));
                    }

                    //If along this line, we hit a 1, that means, we stopped needing jokers from here on out
                    else if(theMatrix[m][n] == 1) {
                        //We start up another vector pair of integers to hold up all the jokers needed consecutively along the horizontal
                        needJokers.add(new Vector<pair>());

                        //Increase the count of how many places need jokers
                        needJokerCount++;
                    }

                    //Increment the index along horizontal
                    n++;

                    //If we hit the horizontal end of the matrix, a new pair of consecutive jokers need have to be set
                    //up increment the needJokerCount and push back a vector to hold the new pair
                    if(n == theMatrix[m].length) {
                        needJokers.add(new Vector<pair>());
                        needJokerCount++;
                    }
                }
            }

            //Indices that need jokers
            for(int i = 0; i < needJokers.size(); i++) {
                for(int j =0; j<needJokers.get(i).size(); j++) {
                    System.out.print(needJokers.get(i).get(j).first + ", " + needJokers.get(i).get(j).second + " | ");
                }
                System.out.println(" ");
            }

            //Iterate through the needJokers vector and give them jokers and wildCards until we run out
            for(int x = 0; x < needJokers.size(); x++) {
                if(needJokers.get(x).size() <= specialCards) {
                    for(int y = 0; y < needJokers.get(x).size(); y++) {
                        //Increment the matrix value at the needJoker index, since we do have enough jokers or wildcards to expend
                        theMatrix[needJokers.get(x).get(y).first][needJokers.get(x).get(y).second]++;


                        //Decrease the count of special cards that we have
                        specialCards = specialCards - needJokers.get(x).size();



                        //Having given special cards, we can run a passII at this index so as to clear the ru nout
                        pass(2, needJokers.get(x).get(y).first, needJokers.get(x).get(y).second);

                    }
                }
            }

            //We first took a look at all the empty spaces, considering them as space that needed special cards. But the space right after
            //cards in consecution may be left out by the above run through. So we consider cards that are in consecution and the space
            //after that as needing jokers

            //Taking out cards in consecution along the horizontal
            Vector<Vector<pair>> cardsInConsecution = new Vector<Vector<pair>>();

            //Starting out a vector of pair to push back the consecution
            cardsInConsecution.add(new Vector<pair>());

            //Starting at index 0 and move to the vertical end
            int index = 0;
            for(int a = 0; a < 5; a++) {
                int b = 0;
                while(b != theMatrix[a].length) {

                    //If there's a card, then we push back to the vector
                    if(theMatrix[a][b] == 1) {
                        cardsInConsecution.get(index).add(new pair(a, b));
                    }

                    //Otherwise, dont push back but start a new vector of pairs to hold the next cards in consecution
                    else if(theMatrix[a][b] == 0) {
                        cardsInConsecution.add(new Vector<pair>());
                        index++;
                    }

                    //Increment the b index, to move along the horizontal
                    b++;

                    //If we hit the horizontal end, start a new vector of pairs to hold the next card in conseuction
                    if(b == theMatrix[a].length) {
                        cardsInConsecution.add(new Vector<pair>());
                        index++;
                    }
                }
            }

            //Printing out the cards in consecution
            //Indices that need jokers
            for(int i = 0; i < cardsInConsecution.size(); i++) {
                for(int j =0; j<cardsInConsecution.get(i).size(); j++) {
                    System.out.print(cardsInConsecution.get(i).get(j).first + ", " + cardsInConsecution.get(i).get(j).second + " | ");
                }
                System.out.println(" ");
            }

            //We give out special cards to those who need them if we have them
            for(int i = 0; i <cardsInConsecution.size(); i++) {
                if((3- cardsInConsecution.get(i).size()) <= specialCards) {
                    for(int j=0; j<cardsInConsecution.get(i).size();j++) {
                        theMatrix[cardsInConsecution.get(i).get(j).first][cardsInConsecution.get(i).get(j).second]--;
                        specialCards = specialCards - (3- cardsInConsecution.get(i).size());
                    }
                }
            }
        }

        //Acting on the vertical books that need jokers
        if(specialCards > 0) {

            //Getting all the vertical book counts
            Vector<Integer> bookCounts = new Vector<Integer>();

            //Get all the vertical books
            for(int i =0; i<theMatrix[0].length; i++) {
                bookCounts.add(findVerticalMeld(i).size());
            }

            //If a book needs special card, give it to them if we have them
            for(int i = 0; i < bookCounts.size(); i++) {
                if((3-bookCounts.get(i)) <= specialCards) {
                    theMatrix[0][i]++;
                    pass(2,0,i);

                }
            }
        }
    }
    /* *********************************************************************
    Function Name: checkMatrix
    Purpose: It applies passI, passII, passII in that order to each index of the matrix
    Return: bool value of whether the user won or not. Based on if the matrix is all 0's OR not.
    Algorithm: Handles prioritizing issues first with passI, clears out all complete books and runs with passII, clears out cards that need jokers
    with passIII
    ********************************************************************* */
    public boolean checkMatrix() {

        //Set a variable to hold the matrix sum at the beginning
        int prevMatrixSum = 0;

        //Do-while loop until we don't get any changes in the matrix sum having passed through all indexes of the matrix with a for loop
        do {

            //Storing the matrix sum at the beginning of the run through
            prevMatrixSum = matrixSum();

            //Running through all indexes and applying passI to them through the matrix completely
            for(int i =0; i<theMatrix.length; i++) {
                for(int j =0; j<theMatrix[i].length; j++) {
                    pass(1,i,j);
                }
            }
            //Checking if sum reamined the same, if it didn't, maybe there is more clearing to do so do another run. If sum didn't change after another pass
            //just move out of the loop
        }while(prevMatrixSum != matrixSum());

        //Do-while loop until we don't get any changes in the matrix sum having passed through all indexes of the matrix with a for loop
        do {

            //Storing the matrix sum at the beginning of the run through
            prevMatrixSum = matrixSum();

            //Running through all indexes and applying pass no 2 to them through the matrix completely
            for(int i =0; i<theMatrix.length; i++) {
                for(int j =0; j<theMatrix[i].length; j++) {
                    pass(2,i,j);
                }
            }
            //Checking if sum reamined the same, if it didn't, maybe there is more clearing to do so do another run. If sum didn't change after another pass
            //just move out of the loop
        }while(prevMatrixSum != matrixSum());



        //Apply passIII
        passIII();

        //If matrixSum is now 0, then the user can go out
        if(matrixSum() == 0) {
            return true;
        }

        cardsRemaining.clear();

        //If they did not win, store the cards that remain into the remaining cards vector
        for(int i = 0; i < theMatrix.length; i++) {
            for(int j = 0; j < theMatrix[i].length; j++) {
                for(int k = 0; k < theMatrix[i][j]; k++) {
                    cardsRemaining.add(matrixIndexToCardString(i,j));
                }
            }
        }

        //They didn't win, so return false
        return false;
    }

    /* *********************************************************************
    Function Name: matrixIndexToCardValue
    Purpose: Converts index of the matrix to a string representing the value of the card
    Parameters: i and j that represent the index
    Return Value: string representation of the card
    Algorithm: Adds 3 to the index along x-axis to get the rank value, and just appropriates the suit value according to the index along y-axis
    ********************************************************************* */
    public String matrixIndexToCardString(int i, int j) {
        //The string to be return, initially empty
        String s = "";

        //Add 3 and add appropriate string representation of the rank to the empty string
        if (j + 3 == 10) {
            s = s + "X";
        }
        else if (j + 3 == 11) {
            s = s + "J";
        }
        else if (j + 3 == 12) {
            s = s + "Q";
        }
        else if (j + 3 == 13) {
            s = s + "K";
        }
        else {
            s = s + (j + 3);
        }

        //Add appropriate string representation according to the y-coordinate
        if (i == 0) {
            s = s + "S";
        }
        else if (i == 1) {
            s = s + "C";
        }
        else if (i == 2) {
            s = s + "D";
        }
        else if (i == 3) {
            s = s + "H";
        }
        else if (i == 4) {
            s = s + "T";
        }

        return s;
    }

    //Get remaining cards
    public Vector<String> getCardsRemaining() {
        return cardsRemaining;
    }

    //Get the count of unused special cards
    public int specialCardsRemaining() {
        if(booksMade.size() >= 1 || runsMade.size() >= 1) {
            //You can attach special cards to a any book or a run
            return 0;
        }
        else {
            return specialCards;
        }
    }

    //Get the original joker count
    public int originalJokerCount() {
        return jokerCount;
    }

    //Get the original wildcard count
    public int originaWildcardCount() {
        return wildcardCount;
    }
}
