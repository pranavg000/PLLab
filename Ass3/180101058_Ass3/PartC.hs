qsort [] = []                                                                           -- If empty list, just return it
qsort (pivot:xs) =  let left_partition = [ num | num <- xs, num <= pivot ]              -- Take first elem as pivot and partition the rest of the list
                        right_partition = [ num | num <- xs, num > pivot ]              -- to left and right partition
                    in concat [qsort(left_partition), [pivot], qsort(right_partition)]  -- recursively call qsort on left & right partitions and concatinate
                                                                                        -- them with the pivot in between


test_input = [[26, 13, 13], [13, 47, 28, 22], [25,19], [49, 30, 34, 22, 8, 28, 13], [37, 1, 22, 5, 16, 44], [5, 11, 29, 21, 9, 42], [10, 25, 5, 40], [38, 14, 7, 48, 17], [27, 29, 33, 17, 43, 16, 49, 27], [24,7]]
test_output = [qsort(x) | x <- test_input]                                              -- respective Outputs of the input testcases