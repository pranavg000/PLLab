-- Finding square root using binary search
square_root n = sqrt_helper 0 n n                           -- Calling sqrt_helper with appropriate arguments
sqrt_helper l r n   |   r-l < 0.0000001 = r                 -- Base condition if r,l are too close return l
                    |   otherwise = let mid = (l+r)/2 in    -- [l,r] is the range in which the sqrt will reside
                        if mid*mid < n then                 -- Checking if mid is less than sqrt(n) then update l
                            sqrt_helper (mid+0.0000001) r n
                        else 
                            sqrt_helper l mid n             -- If false, update r and call sqrt_helper again

test_input = [121.0, 23.56, 19.0, 16.0, 27.085, 8.867, 27.571, 4.637, 30.104, 19.680]
test_output = [square_root(x) | x <- test_input]            -- respective Outputs of the input testcases 
