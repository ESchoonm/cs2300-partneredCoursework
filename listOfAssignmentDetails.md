This is just a file to make sure we don't miss anything
To mark the progress of what we still need to do. 
-  haven't started
-E Evie is working on it
-K Kacy is working on it
-p in progress, no assigned individual
D  Completed


-p GameBoard Class
    -E printGameBoard method
    -  Save/ Add move to GameBoard

-p Main Class
    -  Main
    -D  checkPerpendicular Slopes
    -  endcondition
        #all cells are filled, or two perpendicular plays in a row
    -K  read input from file
	#K I started this in a while loop in main, but if there's a way to put it in
	   a method we should do that!
    -  test value for validity
        # this should include perpendicular, and that start and end cells are empty
	# I split this into two methods, one for perpendicular and one for start/end cells
	-D  valid perpendicular
	-   valid start/end cells
    -  addplay to the GameBoard
    -  determine Winner
        #the one with the most amount of cells covered
    K  calculate the slope of line
    -  calculate which cells are highlighted

