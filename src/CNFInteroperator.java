import java.io.*;
import java.util.*;

//Takes cnf files as inputs and formats them to be read
public class CNFInteroperator {
    private ArrayList<Vector<Integer>> clauses = new ArrayList<Vector<Integer>>();
    private int literals;
    private String fPath;

    //Initializing values
    public CNFInteroperator(String currentPath) {
        literals = 0;
        fPath = currentPath;
    }

    //reads the file and ignores unnecessary characters
    public static void readFiles(CNFInteroperator cnf) throws IOException {
        //current file
        FileInputStream fstream = new FileInputStream(cnf.fPath);
        //reading each character
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String currLine;

        // reads each line
        while ((currLine = br.readLine()) != null) {
            //removing excess spacing
            currLine = currLine.trim();
            // Seperates each clause into an array with each value
            String[] currLineIndVals = currLine.split("\\s+");
            // creates temporary value
            Vector<Integer> tempClause = new Vector<Integer>(3);

            // finding number of literals
            if (currLineIndVals[0].equals("p")) {
                //start of file
                cnf.literals = Integer.parseInt(currLineIndVals[2]);
            }
            // Ignores lines that start with c, p, %, 0, or ""
            if (!currLineIndVals[0].equals("c") && !currLineIndVals[0].equals("p") && !currLineIndVals[0].equals("%") && !currLineIndVals[0].equals("0") && !currLineIndVals[0].equals("")) {
                // adds literals to clause
                for (int i = 0; i < currLineIndVals.length; i++) {
                    if (Integer.parseInt(currLineIndVals[i]) != 0) {
                        tempClause.addElement(Integer.parseInt(currLineIndVals[i]));
                    }

                    //Checking to make sure a line doesn't contain a +/- val
                    if (i == 0) {
                        if ((currLineIndVals[i + 1]) != null && (currLineIndVals[i + 2]) != null) {
                            if (Integer.parseInt(currLineIndVals[i]) == -Integer.parseInt(currLineIndVals[i + 1]) || -Integer.parseInt(currLineIndVals[i]) == Integer.parseInt(currLineIndVals[i + 2]) || Integer.parseInt(currLineIndVals[i + 1]) == -Integer.parseInt(currLineIndVals[i + 2])) {
                                System.out.println("error");
                            }
                        }
                    }
                }
            }
            // adds clause to list
            if (tempClause.size() > 0) {
                cnf.clauses.add(tempClause);
            }
        }
        //close file
        br.close();
    }


    public static void main(String[] args) throws IOException {
        DPLLEasy();
//        WalkSATEasy();
//        GSATEasy();
//
//        DPLLHard();
//        WalkSATHard();
//        GSATHard();

    }

    static void DPLLEasy() throws IOException {
        CNFInteroperator file;
        File dir = new File("src/CNF_Formulas");
        File[] directoryListing = dir.listFiles();
        Arrays.sort(directoryListing);
        if (directoryListing != null) {
            for (File child : directoryListing) {
                long start = System.currentTimeMillis();
                file = new CNFInteroperator(child.toString());
                readFiles(file);

                DPLL dpll = new DPLL();
                System.out.println(child.toString() + "    " + dpll.Solver(file.clauses) + "     " + (System.currentTimeMillis() - start));
            }
        }
    }

    static void WalkSATEasy() throws IOException {
        CNFInteroperator file;
        File dir = new File("src/CNF_Formulas");
        File[] directoryListing = dir.listFiles();
        Arrays.sort(directoryListing);
        if (directoryListing != null) {
            int correctWS = 0;
            int wrongWS = 0;
            ArrayList<String> incorrectOutcomesWS = new ArrayList<String>();
            for (File child : directoryListing) {
                long totalTime = 0;
                for (int j = 0; j < 10; j++) {
                    long start = System.currentTimeMillis();
//                // WALKSAT
                    file = new CNFInteroperator(child.toString());
                    readFiles(file);

                    WalkSAT walksat = new WalkSAT();
                    boolean isT = walksat.Solver(file.clauses, 2500, file.literals, .5);

                    System.out.println(child.toString() + "   " + isT);
                    totalTime = totalTime + System.currentTimeMillis() - start;
                    if (child.toString().contains("uuf")) {
                        if (isT && j == 0) {
                            wrongWS++;
                        } else {
                            if (j == 0) {
                                correctWS++;
                            }
                        }
                    } else {
                        if (isT) {
                            if (j == 0) {
                                correctWS++;
                            }
                            if (incorrectOutcomesWS.indexOf(child.toString()) > -1) {
                                incorrectOutcomesWS.remove(child.toString());
                                wrongWS--;
                                correctWS++;
                            }
                        } else {
                            if (!incorrectOutcomesWS.contains(child.toString()) && j == 0) {
                                incorrectOutcomesWS.add(child.toString());
                                wrongWS++;
                            }
                        }
                    }
                }

                System.out.println(totalTime / 10);
            }
            System.out.println("correct: " + correctWS);
            System.out.println("incorrect: " + wrongWS);
            System.out.println(incorrectOutcomesWS);
        }
    }

    static void GSATEasy() throws IOException {
        CNFInteroperator file;
        File dir = new File("src/CNF_Formulas");
        File[] directoryListing = dir.listFiles();
        Arrays.sort(directoryListing);
        if (directoryListing != null) {
            int correctGS = 0;
            int wrongGS = 0;
            ArrayList<String> incorrectOutcomesGS = new ArrayList<String>();
            for (File child : directoryListing) {
                long totalTime = 0;
                for (int j = 0; j < 10; j++) {
                    long start = System.currentTimeMillis();

//                // GSAT
                    file = new CNFInteroperator(child.toString());
                    readFiles(file);

                    GSAT gsat = new GSAT();
                    boolean isT = gsat.Solver(file.clauses, 2500, file.literals, .5);
                    System.out.println(child.toString() + "   " + isT);

                    totalTime = totalTime + System.currentTimeMillis() - start;
                    if (child.toString().contains("uuf")) {
                        if (isT && j == 0) {
                            wrongGS++;
                        } else {
                            if (j == 0) {
                                correctGS++;
                            }
                        }
                    } else {
                        if (isT) {
                            if (j == 0) {
                                correctGS++;
                            }
                            if (incorrectOutcomesGS.indexOf(child.toString()) > -1) {
                                incorrectOutcomesGS.remove(child.toString());
                                wrongGS--;
                                correctGS++;
                            }
                        } else {
                            if (!incorrectOutcomesGS.contains(child.toString()) && j == 0) {
                                incorrectOutcomesGS.add(child.toString());
                                wrongGS++;
                            }
                        }
                    }
                }
                System.out.println(totalTime / 10);
            }
            System.out.println("correct: " + correctGS);
            System.out.println("incorrect: " + wrongGS);
            System.out.println(incorrectOutcomesGS);
        }
    }

    static void DPLLHard() throws IOException {
        CNFInteroperator file;
        File dir = new File("src/HARD_CNF_Formulas");
        File[] directoryListing = dir.listFiles();
        Arrays.sort(directoryListing);
//        int iterator = 5;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                long start = System.currentTimeMillis();
                if (child.toString().contains("100") && !child.toString().contains("rcnf")) {
//                    if (iterator % 5 == 0) {
                        file = new CNFInteroperator(child.toString());
                        readFiles(file);
//
                        DPLL dpll = new DPLL();
                        System.out.println(child.toString() + "    " + dpll.Solver(file.clauses) + "     " + (System.currentTimeMillis() - start)); //is file sat?
//                    }
//                    iterator++;
                }
            }
        }
    }

    static void WalkSATHard() throws IOException {
        CNFInteroperator file;
        File dir = new File("src/HARD_CNF_Formulas");
        File[] directoryListing = dir.listFiles();
//        int iterator = 5;
        if (directoryListing != null) {
            int correctWS = 0;
            int wrongWS = 0;
            ArrayList<String> incorrectOutcomesWS = new ArrayList<String>();
            for (File child : directoryListing) {
                long totalTime = 0;
                if (!child.toString().contains("rcnf") && !child.toString().contains("op") && !child.toString().contains("README")) {
//                    if (iterator % 5 == 0) {
                        for (int j = 0; j < 10; j++) {
                            long start = System.currentTimeMillis();

//                // WALKSAT
                            file = new CNFInteroperator(child.toString());
                            readFiles(file);

                            WalkSAT walksat = new WalkSAT();
                            boolean isT = walksat.Solver(file.clauses, 2500, file.literals, .5);
                            System.out.println(child.toString() + "   " + isT);

                            totalTime = totalTime + System.currentTimeMillis() - start;
                        }
                        System.out.println(totalTime / 10);
//                    }
//                    iterator++;
                }

            }
        }
    }

    static void GSATHard() throws IOException {
        CNFInteroperator file;
        File dir = new File("src/HARD_CNF_Formulas");
        File[] directoryListing = dir.listFiles();
//        int iterator = 5;
        if (directoryListing != null) {
            for (File child : directoryListing) {
                long totalTime = 0;
                if (!child.toString().contains("rcnf") && !child.toString().contains("op") && !child.toString().contains("README")) {
//                    if (iterator % 5 == 0) {
                        for (int j = 0; j < 10; j++) {
                            long start = System.currentTimeMillis();

//                // GSAT
                            file = new CNFInteroperator(child.toString());
                            readFiles(file);

                            GSAT gsat = new GSAT();
                            boolean isT = gsat.Solver(file.clauses, 2500, file.literals, .5);
                            System.out.println(child.toString() + "   " + isT);

                            totalTime = totalTime + System.currentTimeMillis() - start;
                        }
                        System.out.println(totalTime / 10);
//                    }
//                    iterator++;
                }

            }
        }
    }

}





