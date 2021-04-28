% Using Newton Raphson to find SQRT of X in O(log(X)) time
sqrt_helper(X, Result, Accuracy, L) :- abs(L*L - X) < Accuracy, Result = L, !.                              % If |L*L-X| < Accuracy, Result is found, Result=L
sqrt_helper(X, Result, Accuracy, L) :- abs(Result*Result - X) < Accuracy, !.                              % If |L*L-X| < Accuracy, Result is found, Result=L
sqrt_helper(X, Result, Accuracy, L) :- NL is ((L+X/L)/2), sqrt_helper(X, Result, Accuracy, NL), !.          % Calling sqrt_helper with updated L value

squareroot(_, _, Accuracy) :- print('HELLO2\n'),Accuracy =< 0, print('Invalid arguments!! Accuracy should be > 0.\n'), !.    % Checks if Accuracy value is valid
squareroot(X, _, _) :- print('HELLO3\n'), X < 0, print('Invalid arguments!! X should be >= 0.\n'), !.                         % Checks if X value is valid
squareroot(X, Result, Accuracy) :- print('HELLO4\n'), sqrt_helper(X, Result, Accuracy, X), !.                                 % If X, Accuracy are valid, call sqrt_helper with appropriate Init value
% squareroot(X, Result, Accuracy) :- print('HELLO\n'), abs(Result*Result - X) < Accuracy, !.