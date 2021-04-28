import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Updater implements Runnable {
    int branchID;
    AccountsLinkedList accounts; // Linked list to hold Accounts of the Branch
    ConcurrentHashMap<Integer, AccountsLinkedList> hashMap; // Array of Linked List maintained by hashmap
    ArrayList<String> tasks;

    public Updater(int branchID, AccountsLinkedList accounts, ConcurrentHashMap<Integer, AccountsLinkedList> hashMap, ArrayList<String> tasks) {
        this.branchID = branchID; // Initializations are done
        this.accounts = accounts; // Accounts Linked list received as an argument
        this.hashMap = hashMap; // Hashmap received as an argument
        this.tasks = tasks;
    }

    @Override
    public void run() {
        int n = 1000;
        for (int i = 0; i < tasks.size(); i++) {
            // int in = randomTask.getRandomTask(); // New random Task generated
            String instr = tasks.get(i);
            String[] arr = instr.split("\\s");
            int in = Integer.parseInt(arr[0]);
            
            switch (in) { // Appropriate functions are called for each task
                case 0: {
                    cashDeposit(Long.parseLong(arr[1]), Integer.parseInt(arr[2]));
                    break;
                }
                case 1: {
                    cashWithdraw(Long.parseLong(arr[1]), Integer.parseInt(arr[2]));
                    break;
                }
                case 2: {
                    moneyTransfer(Long.parseLong(arr[1]), Long.parseLong(arr[2]), Integer.parseInt(arr[3]));
                    break;
                }
                case 3: {
                    addCustomer(Long.parseLong(arr[1]), Integer.parseInt(arr[2]));
                    break;
                }
                case 4: {
                    deleteCustomer(Long.parseLong(arr[1]));
                    break;
                }
                case 5: {
                    transferCustomer(Long.parseLong(arr[1]), Long.parseLong(arr[2]));
                    break;
                }
            }
        }
    }

    private void addCustomer(long accountNo, int balance) { // Creates a new customer with the balance
        accounts.addAccount(new Account(accountNo, balance));
    }

    private void deleteCustomer(long accountNo) {   // Deletes the account from the Branch's list
        int targetBranch = App.branch(accountNo);
        AccountsLinkedList accs = hashMap.get(targetBranch);
        accs.removeCustomer(accountNo);
    }

    private void transferCustomer(long oldAccountNo, long newAccountNo) { // Transfers the account to another random Branch
        int oldBranch = branchID;
        int newBranch = App.branch(newAccountNo);
        AccountsLinkedList oldAccs = hashMap.get(oldBranch), newAccs = hashMap.get(newBranch);
        Account account = oldAccs.removeCustomer(oldAccountNo);
        if (account != null) {
            account.accountNo = newAccountNo;
            newAccs.addAccount(account);
        } 
    }

    private void moneyTransfer(long accountNo1, long accountNo2, int amount) { // Transfers money from the account to another account
        int b2 = App.branch(accountNo2);
        AccountsLinkedList accounts2 = hashMap.get(b2);
        Account account1 = accounts.search(accountNo1), account2 = accounts2.search(accountNo2);
        if (account1 == null || account2 == null) {
            return;
        }
        if(account1.withdraw(amount))
            account2.deposit(amount);
    }

    private void cashWithdraw(long accountNo, int amount) { // Withdraws from the account
        Account acc = accounts.search(accountNo);
        if (acc != null) {
            acc.withdraw(amount);
        } 
        
    }

    private void cashDeposit(long accountNo, int amount) { // Deposits into the account
        Account acc = accounts.search(accountNo);
        if (acc != null) {
            acc.deposit(amount);
        } 
        
    }
}

class Account { // Nodes of the linked list
    public long accountNo;
    public int balance; // Account balance
    public Account next; // Next pointer to next Linked list node
    ReadWriteLock fineGrainLock; // Fine-grained Lock for specific Customer node

    public Account(long accountNo, int balance) { // Initializations are done
        this.accountNo = accountNo;
        this.balance = balance;
        this.next = null;
        this.fineGrainLock = new ReentrantReadWriteLock(); // Lock is a ReentrantReadWriteLock
    }

    public void deposit(int amount) { // Deposits amount in the account
        fineGrainLock.writeLock().lock(); // Acquires the fineGrain write lock
        balance += amount;
        fineGrainLock.writeLock().unlock(); // Relases the fineGrain write lock
    }

    public boolean withdraw(int amount) {   // Withdraws the amount from the account
        boolean successful = true;
        fineGrainLock.writeLock().lock();   // Acquires the fineGrain write lock
        if(amount <= balance){              // Checks if amount available in account
            balance -= amount;
        }
        else successful = false;
        fineGrainLock.writeLock().unlock(); // Relases the fineGrain write lock
        return successful;
    }
}

class AccountsLinkedList {                  // Linked list for holding branch's accounts
    int size;
    Account head, tail;                     // Head and tail references for the Linked list
    ReadWriteLock courseGrainLock;          // Course-grained Lock for entire Linked List

    AccountsLinkedList() {                  // Initializations are done
        size = 0;
        head = null;
        tail = null;
        courseGrainLock = new ReentrantReadWriteLock(); // Lock is a ReentrantReadWriteLock
    }

    public void addAccount(Account account) {   // Adds new customer account to Accounts list
        courseGrainLock.writeLock().lock();     // Acquires courseGrain write lock for entire list
        if (head == null) {
            head = tail = account;
        } else {
            tail.next = account;
            tail = account;
        }
        size++; // Increments size after insert
        courseGrainLock.writeLock().unlock();       // Releases courseGrain write lock for entire list
    }

    public Account removeCustomer(long accountNo) { // Removes customer from list
        if (head == null)
            return null; // Returns if list empty
        Account removedAccount = null;
        courseGrainLock.writeLock().lock();         // Acquires courseGrain write lock for entire list
        if (head.accountNo == accountNo) {          // Checking edge cases
            removedAccount = head;
            if (size == 1) {
                head = tail = null;
            } else {
                head = head.next;
            }
            size--;
        } else {
            Account cur = head.next, prev = head;
            while (cur != null) {                   // Loop through to find and delete node
                if (cur.accountNo == accountNo) {
                    removedAccount = cur;
                    prev.next = cur.next;
                    if (cur.next == null)
                        tail = prev;
                    size--;
                    break;
                }
                prev = cur;
                cur = cur.next;
            }
        }
        courseGrainLock.writeLock().unlock();       // Releases courseGrain write lock for entire list
        if (removedAccount != null)
            removedAccount.next = null;
        return removedAccount;
    }

    public int size() {                             // Returns size of list
        courseGrainLock.readLock().lock();          // Acquires readlock
        int sz = size;
        courseGrainLock.readLock().unlock();        // Releases readlock
        return sz;
    }

    public Account search(long accountNo) {         // Searches and returns account no.
        courseGrainLock.readLock().lock();          // Acquires readlock before traversing list
        Account acc = head;
        while (acc != null) {                       // Traverses list and searches for the account no.
            if (acc.accountNo == accountNo) {
                break;
            }
            acc = acc.next;
        }
        courseGrainLock.readLock().unlock();        // Releases readlock
        return acc;
    }
}

public class App {
    private static final long div = 1000000000;
    private static int noOfTasks;
    public static final int noOfBranches = 10;      // No. of branches = 10
    private static int noOfUpdaters = 10;           // Total no. of updaters = 100

    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println("Specify the no. of Tasks/Requests per updater as argument. See README.txt for steps to run");
            return;
        }
        noOfTasks = Integer.parseInt(args[0]);      // Taking no of Tasks as an argument

        Instant startTime = Instant.now();          // Start time instant of program
        Scanner scanner = null;                     // Scanner to read from file
        try {
            scanner = new Scanner(new File("input.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Reading input from file input.txt");

        ConcurrentHashMap<Integer, AccountsLinkedList> hashMap = new ConcurrentHashMap<Integer, AccountsLinkedList>();   // Array of Linked List maintained by hashmap
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < noOfBranches; i++){
            AccountsLinkedList accounts = new AccountsLinkedList(); // Creating the Linked Lists
            for(int j=0;j<10000;j++){                               // Adding initial accounts to the Accounts list
                accounts.addAccount(new Account(scanner.nextLong(), scanner.nextInt()));
            }
            hashMap.put(i, accounts);                                // Putting the Accounts list in the Hashmap
        }
        System.out.println("Initializations done!");

        
        scanner.nextLine();
        ArrayList<ArrayList<String>> tasks = new ArrayList<>();     // Tasks list for each updater

        for(int i = 0;i<noOfBranches;i++)
            for(int j=0;j<noOfUpdaters;j++){
            tasks.add(new ArrayList<String>());
        }
        
        for(int j=0;j<noOfTasks;j++){
            for(int k=0;k<noOfBranches;k++)
            for (int i = 0; i < noOfUpdaters; i++) {                
                tasks.get(k*noOfBranches+i).add(scanner.nextLine());    // Adding the scanned lines into the tasks list
            }
        }

        System.out.println("Tasks lists initialized!");
        
        for(int i=0;i<noOfBranches;i++)
            for(int j=0;j<noOfUpdaters;j++){
                int branchID = i;
                Updater updater = new Updater(branchID, hashMap.get(branchID), hashMap, tasks.get(noOfBranches*i+j));  // Initialize with appropriate accounts list
                Thread thread = new Thread(updater);
                threads.add(thread);
                thread.start();                                         // Starting the threads with the updaters
            }
        
        System.out.println("Updater threads started!");
        


        for (Thread thread : threads) {                                 
            try {
                thread.join();                                          // Wait for threads to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Instant endTime = Instant.now();                                // End time    
        double execTime = (double)Duration.between(startTime, endTime).toMillis() / 1000;
        System.out.printf("Execution complete!\nExecution time: %f sec\n", execTime);
    }

    public static int branch(long accountNo){                           // get branch from account no.
        return (int)(accountNo/div);
    }

}
