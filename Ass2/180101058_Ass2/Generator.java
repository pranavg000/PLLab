import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Generator {
    private static double[] prob = { 0.33, 0.66, 0.99, 0.993, 0.996, 1.0 }; // Prob array acc to question
    private static Random rand = new Random();
    private static final long div = 1000000000;
    public static final int noOfBranches = 10;          // No. of branches
    public static final int noOfUpdaters = 10;          // No. of updaters per branch
    public static int noOfTasks;                        // No. of tasks for each updater
    public static ArrayList<Set<Long>> uniqueAccounts = new ArrayList<>();  // Set of unique account numbers per branch
    public static ArrayList<ArrayList<Long>> accounts = new ArrayList<>();  // Array of accounts for each branch
    public static BufferedWriter buffer;


    public static int getRandomTask() {         // Generates random task acc to the
        double p = rand.nextDouble();           // required probs given in Question
        for (int i = 0; i < 6; i++) {
            if (p <= prob[i])
                return i;
        }
        return 1;
    }

    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println("Specify the no. of Tasks/Requests per updater as argument. See README.txt for steps to run");
            return;
        }
        noOfTasks = Integer.parseInt(args[0]);          // Taking no of Tasks as an argument

        FileWriter writer;
        try {
            writer = new FileWriter("input.txt");       // Creating the input.txt file and initializing the writer
            buffer = new BufferedWriter(writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Generating tasks");
        for(int i=0;i<noOfBranches;i++){
            uniqueAccounts.add(new HashSet<Long>());            // Creating the HashSet for unique Account no.
            accounts.add(new ArrayList<Long>());                // Create list for branch account no.
            while(uniqueAccounts.get(i).size() < 10000){
                long accNo = getNewAccountNo(i);                // Get new Acc no and Balance
                int balance = getRandomBalance();
                writeToFile(String.format("%d %d\n", accNo, balance));
                
            }
        }

        for(int k=0;k<noOfTasks;k++){                   
            for(int i=0;i<noOfBranches;i++){
                for(int j=0;j<noOfUpdaters;j++){
                    int in = getRandomTask();           // New random Task generated
                    writeToFile(String.format("%d ", in));
                    switch (in) {                       // Appropriate functions are called for each task
                        case 0: {
                            generateCashDeposit(i);
                            break;
                        }
                        case 1: {
                            generateCashWithdraw(i);
                            break;
                        }
                        case 2: {
                            generateMoneyTransfer(i);
                            break;
                        }
                        case 3: {
                            generateAddCustomer(i);
                            break;
                        }
                        case 4: {
                            generateDeleteCustomer();
                            break;
                        }
                        case 5: {
                            generateTransferCustomer(i);
                            break;
                        }
                    }
                }
            }
        }
        try {
            buffer.close();                                 // Flush & close the write buffer 
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Tasks generated!");
        
    }

    private static void writeToFile(String string){                 // Write string to file input.txt
        try {
            buffer.write(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateTransferCustomer(int branchID) {        // Generate transfer customer task
        long oldAccountNo = getRandAccountFromBranchD(branchID);
        int newBranch = getRandBranchExcept(branchID);
        long newAccountNo = getNewAccountNo(newBranch);
        writeToFile(String.format("%d %d\n", oldAccountNo, newAccountNo));
    }

    private static void generateDeleteCustomer() {              // Generate delete customer task
        long accountNo = getRandAccountD();
        writeToFile(String.format("%d\n", accountNo));
    }

    private static void generateAddCustomer(int branchID) {     // Generate Add customer task
        long accountNo = getNewAccountNo(branchID);
        int balance = getRandomBalance();
        writeToFile(String.format("%d %d\n", accountNo, balance));
    }

    private static void generateMoneyTransfer(int branchID) {               // Generate Money transfer task
        long accountNo1 = getRandAccountFromBranch(branchID), accountNo2 = getRandAccount();
        int amount = getRandomAmount();
        writeToFile(String.format("%d %d %d\n", accountNo1, accountNo2, amount));
    }

    private static void generateCashWithdraw(int branchID) {    // Generate Cash withdraw task
        long accountNo = getRandAccountFromBranch(branchID);
        int amount = getRandomAmount();
        writeToFile(String.format("%d %d\n", accountNo, amount));
    }

    private static void generateCashDeposit(int branchID) {     // Generate Cash deposit Task
        long accountNo = getRandAccountFromBranch(branchID);
        int amount = getRandomAmount();
        writeToFile(String.format("%d %d\n", accountNo, amount));
    }

    public static long getRandomAccountNo(int branchID) {       // get Any random account no
        return getAccountNo(branchID, rand.nextInt((int)div));
    }

    public static long getNewAccountNo(int branchID){           // get new random account no
        while(true){
            long accNo = getRandomAccountNo(branchID);
            if(!uniqueAccounts.get(branchID).contains(accNo)) {
                uniqueAccounts.get(branchID).add(accNo);
                accounts.get(branchID).add(accNo);
                return accNo;
            }
        }
    }

    public static int getRandomAmount(){                        // get random amount for an account
        return rand.nextInt(1000);
    }

    public static int getRandomBalance(){                        // get random amount for an account
        return rand.nextInt(1000000);
    }

    public static void removeAccNo(int branchID, int index){    // Removes AccNo from list of AccNos 
        uniqueAccounts.get(branchID).remove(accounts.get(branchID).get(index));
        int lastIndex = accounts.get(branchID).size()-1;
        Collections.swap(accounts.get(branchID), index, lastIndex);
        accounts.get(branchID).remove(lastIndex);
    }

    public static int branch(long accountNo){                   // get branch from account no.
        return (int)(accountNo/div);
    }

    public static long getAccountNo(int branchID, int id){      // get Account No
        return div*branchID+id;
    }

    private static long getRandAccountFromBranch(int bid) {     // returns random account from the branch BID
        int noa = uniqueAccounts.get(bid).size();
        int index = rand.nextInt(noa);
        return accounts.get(bid).get(index);
    }

    public static long getRandAccountFromBranchD(int branchID){ // returns random account from the branch BID and delete it
        int noa = uniqueAccounts.get(branchID).size();
        int index = rand.nextInt(noa);
        long accNo = accounts.get(branchID).get(index);
        removeAccNo(branchID, index);
        return accNo;

    }

    private static long getRandAccount() {              // return random account from any branch
        int bid = rand.nextInt(noOfBranches);
        return getRandAccountFromBranch(bid);
    }

    private static long getRandAccountD() {             // return random account and delete it
        int bid = rand.nextInt(noOfBranches);
        return getRandAccountFromBranchD(bid);
    }

    private static int getRandBranchExcept(int brid) { // returns random branchId which is not same as brid
        if(noOfBranches == 1) return brid;
        int bid = rand.nextInt(noOfBranches - 1);
        if (bid < brid)
            return bid;
        return bid + 1;
    }

}
