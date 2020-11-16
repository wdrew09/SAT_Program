import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class DPLL {
    public DPLL() {}

    // checks for satisfiability using dpll
    boolean Solver(ArrayList<Vector<Integer>> clauses) {
        boolean uClause = false;

        Vector<Integer> pLiterals = new Vector<Integer>();
        Vector<Integer> tempLiterals = new Vector<Integer>();

        //make sure it isnt empty
        if (clauses.isEmpty()) { return true; }
        else {
            int numClauses = 0;
            // unit clause must be less than 2
            if (clauses.get(numClauses).size() > 1) { uClause= false; }
            else if (clauses.get(numClauses).size() == 0) { return false; }
            else { uClause= true; }
            numClauses++;

            //repeats for amount of clauses
            while(numClauses < clauses.size() && uClause) {
                if (clauses.get(numClauses).size() > 1) { uClause= false; }
                else if (clauses.get(numClauses).size() == 0) { return false; }
                else { uClause= true; }
                numClauses++;
            }

            //If uClause and literals are correct, return true
            if (uClause) {
                if (checkLiterals(clauses)) { return true; }
            }
        }

        // propagate
        int numClauses = 0;
        if (clauses.get(numClauses).size() == 1) {
            clauses = prop(clauses,clauses.get(numClauses).firstElement());
            numClauses = 0;
        } else { numClauses += 1; }

        //repeat for all clauses
        while(numClauses < clauses.size()) {
            if (clauses.get(numClauses).size() == 1) {
                clauses = prop(clauses,clauses.get(numClauses).firstElement());
                numClauses = 0;
            } else { numClauses += 1; }
        }

        // adds to temp literals
        for (int i = 0; i < clauses.size(); i++){
            for (int j = 0; j < clauses.get(i).size(); j++) {
                int val = clauses.get(i).get(j);
                if (!tempLiterals.contains(val)) {
                    tempLiterals.add(val);
                    if (pLiterals.contains(val * -1)) { pLiterals.remove(pLiterals.indexOf(val * -1)); }
                    else { pLiterals.add(val); }
                }
            }
        }

        // calls pliteralrem
        for (int i = 0; i < pLiterals.size(); i++) {
            clauses = pLiteralRem(clauses,pLiterals.get(i));
        }

        if (clauses.isEmpty()) {
            return true;
        } else {
            numClauses = 0;
            // unit clause must be less than 2
            if (clauses.get(numClauses).size() > 1) { uClause = false; }
            else if (clauses.get(numClauses).size() == 0) {
                return false; }
            else { uClause= true; }
            numClauses += 1;

            //repeats for amount of clauses
            while(numClauses < clauses.size() && uClause) {
                if (clauses.get(numClauses).size() > 1) { uClause = false; }
                else if (clauses.get(numClauses).size() == 0) {
                    return false; }
                else { uClause= true; }
                numClauses += 1;
            }

            if (uClause) {
                if (checkLiterals(clauses)) {

                    return true;
                }
            }
        }

        Vector<Integer> randUClauseA = new Vector<Integer>(1);
        Vector<Integer> randUClauseB = new Vector<Integer>(1);

        int h = ThreadLocalRandom.current().nextInt(0, tempLiterals.size());
        randUClauseA.add(tempLiterals.get(h));
        randUClauseB.add(-1 * tempLiterals.get(h));

        ArrayList<Vector<Integer>> tempClauses = new ArrayList<Vector<Integer>>();
        for (int i = 0; i < clauses.size(); i++) {
            Vector<Integer> q = new Vector<Integer>();
            for (int j = 0; j < clauses.get(i).size(); j++) {
                q.add(clauses.get(i).get(j));
            }
            tempClauses.add(q);
        }

        tempClauses.add(0,randUClauseB);
        clauses.add(0,randUClauseA);


        return (Solver(clauses) || Solver(tempClauses));

    }

    // loops through clauses and finds pure literal
    ArrayList<Vector<Integer>> pLiteralRem(ArrayList<Vector<Integer>> clauses, int lit) {
        int i = 0;
        while (i < clauses.size()){
            if (clauses.get(i).contains(lit)) {
                clauses.remove(i);
                i = 0;
            } else { i++; }
        }
        return clauses;
    }

    ArrayList<Vector<Integer>> prop(ArrayList<Vector<Integer>> clauses, int lit) {
        int i = 0;
        while (i < clauses.size()) {
            // Makes sure clause is correct
            if (clauses.get(i).contains(lit * -1)) {
                clauses.get(i).remove(clauses.get(i).indexOf(lit * -1));
            }

            // look for lit
            if (clauses.get(i).contains(lit)) {
                // clause is true, remove i
                clauses.remove(i);
                i = 0;
            } else { i++; }
        }
        return clauses;
    }

    // looking for non matching lits
    boolean checkLiterals(ArrayList<Vector<Integer>> clauses) {
        Vector<Integer> literalsCh = new Vector<Integer>();
        // looping through clauses, checking for lits
        for(int i = 0; i < clauses.size(); i++){
            // add lit to empty list
            if (!literalsCh.isEmpty()){
                for (int j = 0; j < literalsCh.size(); j++) {
                    if (Math.abs(literalsCh.get(j)) == Math.abs(clauses.get(i).firstElement())) {
                        // checks for t and f literals
                        // if there, clause cannot be sat
                        if (clauses.get(i).firstElement() != literalsCh.get(j)) { return false; }
                    }
                }
            }
            // adding to check every literal
            literalsCh.add(clauses.get(i).firstElement());
        }
        return true;
    }
}