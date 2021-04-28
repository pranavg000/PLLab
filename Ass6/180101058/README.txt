Note - SWI-Prolog should be installed

Files in the folder - 
    1) main.pl: The file main.pl contains all the required functions for the assignment
    2) testcase.txt: Contains the sample testcase. The queries of the sample testcase should be run in order
    3) Mazedata.pl: Contains the mazedata for the maze of the sample testcase
    4) graph.png: Diagram of the maze of the sample testcase

Queries can be of 3 types - 
    1) shortest_path(Src,Dst,Result)    // Finds the shortest path from Src to Dst if a path exists. Otherwise it returns false
    2) add_faultynode(X)                // Makes the node X faulty.
    3) remove_faultynode(X)             // Makes the node X non-faulty 

The Sample Queries can be found in the file testcase.txt

Steps to run - 
1) swipl
2) consult('main.pl').              // Ensure that Mazedata.pl is in the same folder
3) Run the queries of the file testcase.txt in order