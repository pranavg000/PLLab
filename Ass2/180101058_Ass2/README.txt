The folder contains 2 files 'Generator.java' (Generates the list of tasks for the updaters) and 
'App.java' (which is the main simulation program as per the Assignment)

Steps to run -> 1. javac Generator.java
                2. java Generator 100000          
                3. javac App.java
                4. java App 100000

                The argument in Steps 2,4 is the no of Tasks/Requests per updater. It should be the same for both the steps.
NOTE: The execution time for 10^5 Tasks(per updater) is nearly 70sec
NOTE: For 10^6 Tasks(per updater), the Tasks/Requests list is nearly 2.5GB in size and might cause OutOfMemory Exception
