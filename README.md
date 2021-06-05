# ComputerVision
Project based on Computer Vision and Image Processing

1. Aim -> To Solve Game of UNBLOCK ME
2. Language, Libraries -> JAVA, OpenCV
3. Algorithms/Data structures -> Image Processing, Hash-Map, BruteForce-Path-Search
4. Things Done (3 things)
	1.Game Development -> Developed Game UNBLOCK ME in JAVA GUI (SWING)
		*Get Game State from Image (Live Video)
		*Pass Game state to Logic -> Get Solution -> Execute
	2.Puzzle Extraction
		*Image from Camera (of Mobile Game State) -> Recognise State of Game (Size & Shape (Template) Matching after pre-processing) -> Validity check -> repeat
			*Divide image in grids -> if value>threshold -> tile is present -> make a grid map -> combine tiles based on shapes & logic -> game state
		*If State is captured properly -> Pass to Game Development
	3.Logic
		*Once state of Game is acquired -> Solve it -> Find Series-of-MOVES which would solve the game
		*Stores VisitedStates in Array -> Brute Force Search (don't visit already visited states) -> Keep searching until find solution
