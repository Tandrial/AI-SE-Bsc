#Projekte

## C 

	BuildYourOwnLisp - http://www.buildyourownlisp.com/
		-TODO

		> Change the grammar to add a new operator such as %.
		› Change the grammar to recognize operators written in textual format add, sub, mul, div.
		› Change the grammar to recognize decimal numbers such as 0.01, 5.21, or 10.2.
		› Change the grammar to make the operators written conventionally, between two expressions.
		› Add the operator %, which returns the remainder of division. For example % 10 6 is 4.
		› Add the operator ^, which raises one number to another. For example ^ 4 2 is 16.
		› Add the function min, which returns the smallest number. For example min 1 5 3 is 1.
		› Add the function max, which returns the biggest number. For example max 1 5 3 is 5.
		› Can you change how lval is defined to use union instead of struct?
		› What are the advantages over using a union instead of struct?
		› Extend parsing and evaluation to support the remainder operator %.
		› Extend parsing and evaluation to support decimal types using a double field.
		› Extend parsing and evaluation to support the remainder operator %.
		› Extend parsing and evaluation to support decimal types using a double field.
		› Create a Macro specifically for testing for the incorrect number of arguments.
		› Create a Macro specifically for testing for being called with the empty list.
		› Add a builtin function cons that takes a value and a Q-Expression and appends it to the front.
		› Add a builtin function len that returns the number of elements in a Q-Expression.
		› Add a builtin function init that returns all of a Q-Expression except the final element.
		› Create a Macro to aid specifically with reporting type errors.
		› Create a Macro to aid specifically with reporting argument count errors.
		› Create a Macro to aid specifically with reporting empty list errors.
		› Change printing a builtin function print its name.
		› Write a function for printing out all the named values in an environment.
		› Redefine one of the builtin variables to something different.
		› Change redefinition of one of the builtin variables to something different an error.
		› Create an exit function for stopping the prompt and exiting.
		› Define a Lisp function that returns the first element from a list.
		› Define a Lisp function that returns the second element from a list.
		› Define a Lisp function that calls a function with two arguments in reverse order.
		› Define a Lisp function that calls a function with arguments, then passes the result to another function.
		› Change variable arguments so at least one extra argument must be supplied before it is evaluated.
		› Create builtin logical operators or ||, and && and not ! and add them to the language.
		› Define a recursive Lisp function that returns the nth item of that list.
		› Define a recursive Lisp function that returns 1 if an element is a member of a list, otherwise 0.
		› Define a Lisp function that returns the last element of a list.
		› Define in Lisp logical operator functions such as or, and and not.
		› Add a specific boolean type to the language with the builtin variables true and false.
		› Adapt the builtin function join to work on strings.
		› Adapt the builtin function head to work on strings.
		› Adapt the builtin function tail to work on strings.
		› Create a builtin function read that reads in and converts a string to a Q-expression.
		› Create a builtin function show that can print the contents of strings as it is (unescaped).
		› Create a special value ok to return instead of empty expressions ().
		› Add functions to wrap all of C's file handling functions such as fopen and fgets.
		› Rewrite the len function using foldl.
		› Rewrite the elem function using foldl.
		› Incorporate your standard library directly into the language. Make it load at start-up.
		› Write some documentation for your standard library, explaining what each of the functions do.
		› Write some example programs using your standard library, for users to learn from.
		› Write some test cases for each of the functions in your standard library.