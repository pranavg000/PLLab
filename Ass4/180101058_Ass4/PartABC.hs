handle_csv_helper [] arr st = (arr ++ [read st :: Integer])                         -- If string ended, insert st of it in arr
handle_csv_helper (x:xs) arr st =  if x == ','                                      -- If comma, insert last st into arr
                                then handle_csv_helper xs (arr ++ [read st :: Integer]) []
                                else handle_csv_helper xs arr (st ++ [x])           -- else append char in st

handle_csv str = handle_csv_helper str [] []

findGcd :: Integer -> Integer -> Integer
findGcd a b | a >= b =  if b == 0                                                   -- If b is zero return a 
                        then a  
                        else findGcd b (a `mod` b)                                  -- else call recursively on a%b
            | otherwise = findGcd b a

findLcm :: [Integer] -> Integer
findLcm [] = 1                                                                      -- Find lcm of xs and then find lcm of x, l
findLcm (x:xs) = let l = (findLcm xs) in ((x * l) `div` (findGcd x l))


data Node a = Nil | Node (Node a) a (Node a) deriving Show                          -- Node data type for storing node data, left, right children


createBST_helper root [] = root                                                     -- Insert first elem of list in tree
createBST_helper root (x:xs) = createBST_helper (insert root x) xs                  -- and call recursively for rest of list

createBST [] = Nil                                                                  -- Make first elem as root
createBST (x:xs) = createBST_helper (Node Nil x Nil) xs                             -- and call helper to get the tree


insert Nil x = Node Nil x Nil                                                       -- If tree is empty, make root = new node
insert (Node l key r) newVal                                                        -- Else insert in tree by comparing key of root with newVal 
                    | newVal <= key = Node (insert l newVal) key r                   -- Call recursively on left subtree
                    | newVal > key = Node l key (insert r newVal)                   -- Call recursively on right subtree


preorder Nil = []
preorder (Node l x r) = let lef = preorder l                                        -- Recurively find preorder on left and right subtree
                            rig = preorder r
                        in [x] ++ lef ++ rig                                        -- Concat x, lef, rig in the pre-order order


postorder Nil = []
postorder (Node l x r) =    let lef = postorder l
                                rig = postorder r
                            in lef ++ rig ++ [x]                                    -- Concat x, lef, rig in the post-order order


inorder Nil = []
inorder (Node l x r) =  let lef = inorder l
                            rig = inorder r
                        in lef ++ [x] ++ rig                                        -- Concat x, lef, rig in the in-order order



main = do

    -- PartA
    putStrLn "Enter numbers seperated by commas"
    name <- getLine  
    let arr = handle_csv name                                                       -- Split the input_string to list
    putStrLn("\nPart A's output-")
    putStr("List of number: ")
    print (arr)                                                                     -- printing the list

    -- PartB
    putStrLn("\nPart B's output-")
    putStr("LCM: ")
    print (findLcm arr)                                                             -- Find LCM of list of points

    -- PartC
    let root = createBST arr
    putStrLn("\nPart C's output-")
    putStr("Inorder Traversal: ")
    print (inorder(root))                                                           -- Find Inorder Traversal      
    putStr("Preorder Traversal: ")
    print (preorder(root))                                                          -- Find Preorder Traversal
    putStr("Postorder Traversal: ")
    print (postorder(root))                                                         -- Find Postorder Traversal
    


