import java.util.ArrayList;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import java.util.stream.IntStream;

public class WalkSAT {
    public WalkSAT() {}

    ArrayList<Vector<Integer>> unsatClauses = new ArrayList<Vector<Integer>>();
    ArrayList<Vector<Integer>> currClauses = new ArrayList<Vector<Integer>>();
    ArrayList<Integer> trueLitList = new ArrayList<Integer>();
    boolean initialAssigned = false;
    int numFlips = 0;

    private Random rand = new Random();




    boolean Solver (ArrayList<Vector<Integer>> clauses, int maxFlips, int numLits, double p) {
        currClauses = clauses;

        //program will loop through the steps for the given number of maxFlips
        while (numFlips < maxFlips) {
            //This will initially set all the literals to their random values and and find the unsatisfactory clauses
            if (!initialAssigned) {
                initialLitAssign(numLits);
                initialAssigned = true;
                unsatClausesAssign();
            }

            //if there are not any unsat clauses, return true
            if (unsatClauses.size() < 1) {
                return true;
            }

            //for random value p, either go to a function that will change the value
            // of a random literal or go to one that will minimize the number of new constraints

            if (Math.random() <= p) {
                findFlipValRand();
            } else {
                findFlipVal(numLits);
            }

            //find the new list of unsat claueses
            unsatClausesAssign();

            //loop through for given amount of flips
            numFlips++;

        }
        //if no solution found, return false
        return false;
    }


    void initialLitAssign(int literals) {
        //will assign all initial literals to t or f
        for (int i = 1; i <= literals; i++) {
            int r1 = rand.nextInt(2);
            if (r1 == 0) {
                trueLitList.add(i);
            } else if (r1 == 1) {
                trueLitList.add(-1 * i);
            } else {
                System.out.println("ERROR");
            }
        }
    }

    //creates a new list of clauses that are unsat given the list of literal assignments
    void unsatClausesAssign() {
        unsatClauses.clear();
        boolean foundT = false;
        for (int i = 0; i < currClauses.size(); i++) {
            for (int j = 0; j < currClauses.get(i).size(); j++) {
                if (trueLitList.contains(currClauses.get(i).get(j))) {
                    foundT = true;
                }
            }
            if (!foundT) {  unsatClauses.add(currClauses.get(i)); }
            foundT = false;
        }
    }

    // Gives a new value to a random literal
    void findFlipValRand() {
        int r1 = rand.nextInt(unsatClauses.size());
        int r2 = rand.nextInt(unsatClauses.get(r1).size());

        int litToChange = unsatClauses.get(r1).get(r2);

        trueLitList.set(trueLitList.indexOf(litToChange * -1), litToChange);
    }

    //gives a new value to a literal that will create the least amount of new constraints
    void findFlipVal(int numLits) {
        ArrayList<Vector<Integer>> tempClauses = new ArrayList<Vector<Integer>>();
        ArrayList<Integer> bestTempLiterals = new ArrayList<Integer>();
        ArrayList<Vector<Integer>> tempUnsatClauses = new ArrayList<Vector<Integer>>();
        int currUnsatClausesCount = unsatClauses.size();
        ArrayList<Integer> tempLitList = new ArrayList<Integer>();
        for (int i = 0; i < currClauses.size(); i++) {
            tempClauses.add(currClauses.get(i));
        }

        for (int k = 0; k < trueLitList.size(); k++) {
            tempLitList.add(trueLitList.get(k));
        }

        for (int j = 0; j < trueLitList.size(); j++) {
            tempLitList.clear();
            for (int k = 0; k < trueLitList.size(); k++) {
                tempLitList.add(trueLitList.get(k));
            }

            int index = tempLitList.indexOf(tempLitList.get(j));
            tempLitList.set(index, tempLitList.get(j) * -1);

            tempUnsatClauses = getTempUnsatClausesAssign(tempClauses, tempLitList);

            if (tempUnsatClauses.size() < currUnsatClausesCount) {
                bestTempLiterals.clear();
                for (int k = 0; k < tempLitList.size(); k++) {
                    bestTempLiterals.add(tempLitList.get(k));
                }
                currUnsatClausesCount = tempUnsatClauses.size();
            }
        }
        if (bestTempLiterals.size() < 2) {
            bestTempLiterals = trueLitList;
        }
        trueLitList = bestTempLiterals;

    }

    //gets the unsat clauses for the function above
    ArrayList<Vector<Integer>> getTempUnsatClausesAssign(ArrayList<Vector<Integer>> tempClause, ArrayList<Integer> tempLitList) {
        ArrayList<Vector<Integer>> tempUClauses = new ArrayList<Vector<Integer>>();
        boolean foundT = false;
        for (int i = 0; i < tempClause.size(); i++) {
            for (int j = 0; j < tempClause.get(i).size(); j++) {
                if (tempLitList.contains(tempClause.get(i).get(j))) {
                    foundT = true;
                }
            }
            if (!foundT) {  tempUClauses.add(tempClause.get(i)); }
            foundT = false;
        }

        return tempUClauses;
    }





//    boolean Freebie() {
//        ArrayList<Vector<Integer>> tempClauses = new ArrayList<Vector<Integer>>();
//        ArrayList<Integer> bestTempLiterals = new ArrayList<Integer>();
//        ArrayList<Vector<Integer>> tempUnsatClauses = new ArrayList<Vector<Integer>>();
//        ArrayList<Vector<Integer>> tempSatClauses = new ArrayList<Vector<Integer>>();
//        int currUnsatClausesCount = unsatClauses.size();
//        ArrayList<Integer> tempLitList = new ArrayList<Integer>();
//
//        boolean satChange = false;
//
//        for (int j = 0; j < tempClauses.size(); j++) {
//            if (!unsatClauses.contains(tempClauses.get(j))) {
//                tempSatClauses.add(tempClauses.get(j));
//            }
//        }
//
//        for (int i = 0; i < currClauses.size(); i++) {
//            tempClauses.add(currClauses.get(i));
//        }
//        for (int k = 0; k < trueLitList.size(); k++) {
//            tempLitList.add(trueLitList.get(k));
//        }
//
//        for (int j = 0; j < trueLitList.size(); j++) {
//            tempLitList.clear();
//            for (int k = 0; k < trueLitList.size(); k++) {
//                tempLitList.add(trueLitList.get(k));
//            }
//            int index = tempLitList.indexOf(tempLitList.get(j));
//            tempLitList.set(index, tempLitList.get(j) * -1);
//
//            tempUnsatClauses = getTempUnsatClausesAssignFreebie(tempClauses, tempLitList);
//
//            for (int r = 0; r < tempUnsatClauses.size(); r++) {
//                if (tempSatClauses.contains(tempUnsatClauses.get(r))) {
//                    satChange = true;
//                }
//            }
//
//            if (satChange == false) {
//                trueLitList.clear();
//                System.out.println(tempUnsatClauses.size());
//                for (int g = 0; g < tempLitList.size(); g++) {
//                    trueLitList.add(tempLitList.get(g));
//                }
//                return true;
//            }
//
////            if (tempUnsatClauses.size() < currUnsatClausesCount) {
////                bestTempLiterals.clear();
////                for (int k = 0; k < tempLitList.size(); k++) {
////                    bestTempLiterals.add(tempLitList.get(k));
////                }
////                currUnsatClausesCount = tempUnsatClauses.size();
////            }
//        }
////        if (bestTempLiterals.size() < 2) {
////            bestTempLiterals = trueLitList;
////        }
////        trueLitList = bestTempLiterals;
//
//        return false;
//    }

//    //gets the unsat clauses for the function above
//    ArrayList<Vector<Integer>> getTempUnsatClausesAssignFreebie(ArrayList<Vector<Integer>> tempClause, ArrayList<Integer> tempLitList) {
//        ArrayList<Vector<Integer>> tempUClauses = new ArrayList<Vector<Integer>>();
//        boolean foundT = false;
//        for (int i = 0; i < tempClause.size(); i++) {
//            for (int j = 0; j < tempClause.get(i).size(); j++) {
//                if (tempLitList.contains(tempClause.get(i).get(j))) {
//                    foundT = true;
//                }
//            }
//            if (!foundT) {  tempUClauses.add(tempClause.get(i)); }
//            foundT = false;
//        }
//
//
//
//        return tempUClauses;
//    }
}