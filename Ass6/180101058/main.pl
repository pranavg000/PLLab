% Finding shortest path from source to destination using Breadth First Search

:-set_prolog_flag(answer_write_options, [max_depth(0)]).  % Makes sure that list doesn't get truncated
:-ensure_loaded('Mazedata.pl').                           % loads Mazedata.pl knowledge base
:-dynamic(in_queue/1).                                    % Making some rules dynamic
:-dynamic(parent/2).
:-dynamic(visited/1).
:-dynamic(faultynode/1).

% Helper inner loop to loop through all non-faulty adjacent elements of node
inner_loop(X) :- mazelink(X,NextLoc), \+ faultynode(NextLoc), \+ visited(NextLoc),                      % Checking if adjacent node is not faulty and not visited
                 assertz(parent(X,NextLoc)), assertz(visited(NextLoc)), assertz(in_queue(NextLoc):-!),  % Inserting parent, visited, in_queue fact to perform BFS
                 inner_loop(X), !.                                                                      % Recursively calling till all the adjacent nodes are exhausted
inner_loop(_).                                                                                          % If all the adjacent nodes are exhausted return true

% Function to perform the BFS
bfs(Y) :- in_queue(X), retract(in_queue(X):-!), inner_loop(X), bfs(Y), !.        % Matches the first element of queue and retracts it, then calls the inner loop
bfs(_) :- retractall(visited(_)), retractall(in_queue(_):-!), !.                 % If queue is empty, then exit BFS by retracting all the visited and inqueue rules

% Helper function to reverse a list
reverse_path([], ArrReverse, ArrReverse).                                           % If input list is empty set result to Acc
reverse_path([G|RArr], Acc, ArrReverse) :- reverse_path(RArr, [G|Acc], ArrReverse). % Otherwise insert the head in Acc and continue recursively

% Heper function to find path from X to Y, given the parents already computed by bfs
find_path(X, X, [X]):- retractall(parent(_,_)), !.                      % If X == Y, Path is just [X] and remove all the parent entries
find_path(X, Y, [Y|Rest]) :- parent(NY,Y), find_path(X, NY, Rest), !.   % Otherwise insert parent of Y in the Path and continue recursively

% Function to find shortest path from Src to Dest
shortest_path(Src, Dest, _) :- (faultynode(Src); faultynode(Dest)), writeln("Src and Dst can not be faultynodes"),!.     % Print error msg if src or dest are faulty
shortest_path(Src, Dest, Result) :- assertz(in_queue(Src):-!), assert(visited(Src)), bfs(Dest),             % Insert the source node in_queue fact and call bfs
                               find_path(Src,Dest,RResult),                                          % After BFS is complete, find_path using the parent rules
                               reverse_path(RResult, [], Result), !.                            % Reverse the path to start with src and finish at dest

% Function to Dynamically Add a faulty node.
add_faultynode(X) :- faultynode(X), writeln("This node is already a faultynodes"),!.    % Don't insert if node already faulty
add_faultynode(X) :- assertz(faultynode(X)).

% Function to Dynamically Remove faulty node 
remove_faultynode(X) :- \+ faultynode(X), writeln("This node is not a faultynode"),!.    % Don't remove if node not faulty
remove_faultynode(X) :- retract(faultynode(X)).