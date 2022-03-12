/**
 * moduleBackendBoard.java
 * This class allows the controllerMainFrame.java class to
 * 	- calculate if there is a win in the game connect4,<p>
 * 	- interface and allow players to add pieces to the column<p>
 *  - display the state of the board in a system.out.println() for troubleshooting<p>
 * And uses a 6x7 2D array to hold the game state of the board<p>
 * This was written specifically for the ICS4U1-FinalCPT-Connect4 Project
 *
 * @author  Ethan Rong, Leo Li, Melanie Young
 * @version 1.0
 * @since   2022-01-26
 */
public class moduleBackendBoard{
	//PROPERTIES
	/**
	 * The board itself, which is made up of a 6x7 int 2D Array
	 * Holds values of 0, 1, 2.
	 * 	- 0 is an empty slot
	 * 	- 1 is a slot taken by player 1
	 * 	- 2 is a slot taken by player 2
	 */
	int intBoard[][] = new int[6][7];

	/**
	 * The number of terms that have gone by. Used to also determine if it is player 1 or player 2 turn
	 * by dividing the number of turns by 2 and seeing the remainder (aka modulus)
	 */
	int intTurn;

	/**
	 * Called outside of the program to help navigate the tiles on the frontend to the correct row
	 */
	int intCurrentRow;

	//Add Position
	/**
	 * Will update intBoard[][] based on the column the current player selects to put their piece
	 * @param intCol the array index (0-5) representing the column the current player wants to put their piece in
	 * @return Returns true if the piece was successfully added, false if the column is full already
	 */
	public boolean addPosition(int intCol){
		for(int intRow = 5; intRow >= 0; intRow--){
			int intPiece = intBoard[intRow][intCol];
			if(intPiece == 0){
				if(intTurn % 2 == 0){
					intBoard[intRow][intCol] = 1;
				}else{
					intBoard[intRow][intCol] = 2;
				}
				intTurn++;
				intCurrentRow = intRow;
				return true;
			}
		}
		return false;
	}
		
	//Check For Winner
	/**
	 * Scans All 4 Angles to Win (Horizontal, Vertical, Diagonal Up-to-Down and Diagonal Down-to-Up)
	 * @param intPlayer the player that you want to check a win for (1 or 2)
	 * @return Returns true if the intPlayer won, false if intPlayer lost
	 */
	public boolean checkWinner(int intPlayer ){
		//Row
		for(int intRow = 0; intRow < 6; intRow++){
			for(int intCol = 0; intCol < 7-3; intCol++){
				for(int intAdder = 0; intAdder < 4; intAdder++){
					if(intBoard[intRow][intCol+intAdder] != intPlayer){
						break;
					}else if(intAdder == 3){
						return true;
					}
				}
			}
		}			
		
		//Col
		for(int intRow = 0; intRow < 6-3; intRow++){
			for(int intCol = 0; intCol < 7; intCol++){
				for(int intAdder = 0; intAdder < 4; intAdder++){
					if(intBoard[intRow+intAdder][intCol] != intPlayer){
						break;
					}else if(intAdder == 3){
						return true;
					}
				}
			}
		}
		
		//Diagonal L-R
		for(int intRow = 3; intRow < 6; intRow++){
			for(int intCol = 0; intCol < 7 - 3; intCol++){
				for(int intAdder = 0; intAdder < 4; intAdder++){
					if(intBoard[intRow-intAdder][intCol+intAdder] != intPlayer){
						break;
					}else if(intAdder == 3){
						return true;
					}
				}
			}
		}
		
		//Diagonal R-L
		for(int intRow = 0; intRow < 6 - 3; intRow++){
			for(int intCol = 0; intCol < 7 - 3; intCol++){
				for(int intAdder = 0; intAdder < 4; intAdder++){
					if(intBoard[intRow+intAdder][intCol+intAdder] != intPlayer){
						break;
					}else if(intAdder == 3){
						return true;
					}
				}
			}
		}
		
		return false;
	}
		
	//Printout Screen (used for troubleshooting)
	/**
	 * Used for troubleshooting to print out the entire board
	 */
	public void printScreen(){
		System.out.println("0\t1\t2\t3\t4\t5\t6");
		System.out.println("------------------------------------------------------------------------------------------");
		for(int intRow = 0; intRow < 6; intRow++){
			String strRow = "";
			for(int intCol = 0; intCol < 7; intCol++){					
				strRow += intBoard[intRow][intCol] + "\t";
			}
			System.out.println(strRow);
		}
	}
		
	//CONSTRUCTOR
	/**
	 * moduleBackendBoard.java Constructor<p>
	 * Sets up the intBoard to a state of all 0s aka empty
	 */
	public moduleBackendBoard(){
		intTurn = 0;
		for(int intRow = 0; intRow < 6; intRow++){
			for(int intCol = 0; intCol < 7; intCol++){
				intBoard[intRow][intCol] = 0;
			}
		}
	}
}


