import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class GSAT {
    public GSAT() {}

    ArrayList<Vector<Integer>> unsatClauses = new ArrayList<Vector<Integer>>();
    ArrayList<Vector<Integer>> currClauses = new ArrayList<Vector<Integer>>();
    Vector<ArrayList<Vector<Integer>>> attemptedClausesList = new Vector<ArrayList<Vector<Integer>>>();
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

            findFlipVal(numLits);

            //find the new list of unsat claueses
            unsatClausesAssign();


            //loop through for given amount of flips
            if (numFlips < maxFlips) {
                numFlips++;
                Solver(clauses, maxFlips, numLits, p);
            }
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

    //gives a new value to a literal that will create the least amount of new constraints
    void findFlipVal(int numLits) {
        ArrayList<Vector<Integer>> tempClauses = new ArrayList<Vector<Integer>>();
        ArrayList<Integer> bestTempLiterals = new ArrayList<Integer>();
        ArrayList<Vector<Integer>> tempUnsatClauses = new ArrayList<Vector<Integer>>();
        int currUnsatClausesCount = -1;
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

            if ((tempUnsatClauses.size() < currUnsatClausesCount || currUnsatClausesCount == -1) && !attemptedClausesList.contains(tempUnsatClauses)) {
                attemptedClausesList.add(tempUnsatClauses);
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


}