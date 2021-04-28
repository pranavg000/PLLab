-- Finding fib(n) in O(n) time complexity
fib :: Integer -> Integer
fib n | n == 0 = 0                                          -- Base cases n=0,1 
      | n == 1 = 1
      | otherwise = fib_helper n 1 1 0                      -- Otherwise call fib_helper

fib_helper n index last last_second =                       -- index = last index, last = last fibonacci no, last_second = last second fibonacci no
                                     if n == index then     -- checks if last index = n
                                        last                -- return last fibonacci no
                                     else                   -- otherwise call fib_helper again with updated arguments
                                        fib_helper n (index+1) (last+last_second) last


test_input = [200, 106, 276, 29, 110, 69, 292, 234, 59, 159, 69]
test_output = [fib(x) | x <- test_input]                    -- respective Outputs of the input testcases