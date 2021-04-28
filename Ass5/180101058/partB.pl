sub_list([], _) :- !.                               % Base case: If X is empty
sub_list([H|XT], [H|YT]) :- sub_list(XT, YT), !.    % If head of X, Y are same, recursively call on tails of arrays
sub_list(X, [_|YT]) :- sub_list(X, YT), !.          % If not, we recursively call on the tail of Y
