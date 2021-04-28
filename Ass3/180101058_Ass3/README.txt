Steps to run the testcases:
1. ghci PartA < test
2. ghci PartB < test
3. ghci PartC < test

PLEASE NOTE: These commands will do the following:
-> Compile the respective files
-> Then display the input testcases (as a list)
-> Then run the function on the testcases and display the outputs (as a list)

Note: Testcases are defined in the Haskell files, the 'test' file just displays the input and output lists

To test the functions on some other input:
1. ghci
2. :l PartA
3. square_root 23.56                // Enter the input number/float as argument
4. :l PartB
5. fib 200                          // Enter the input N as argument  
6. :l PartC
7. qsort [12, 2, 4, 5, 18]          // Enter the input list as argument   