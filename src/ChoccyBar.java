import java.util.*;
public class ChoccyBar {
  int row = 3;
  int col = 3;
  String board[][] = new String[row][col];
  String tempBoard[][] = board;
  int moldyRow;
  int moldyCol;
  
  public static void main(String[] args) {
    ChoccyBar game = new ChoccyBar();
    game.playGame();
  }
  
  public void playGame(){
    System.out.println("Welcome to ChoccyBar.  The rows/columns are in this order: 0, 1, 2.  The choccy bar is 3 x 3, and you, the player, will always go second.  Good luck.");
    
    boardMaker();
    moldySelector();
    
    boolean gameplay = true;
    boolean playerLast = false;

    while(gameplay){
      System.out.println("--- WINNING COMPUTER TURN ---");
      winningComputerTurn();
      playerLast = false;

      if(winCheck(playerLast))
        break;

      System.out.println("--- PLAYER TURN ---");
      playerTurn();
      playerLast = true;

      if(winCheck(playerLast))
        break;

      System.out.println("--- LOSING COMPUTER TURN ---");
      losingComputerTurn();
      playerLast = false;

      if(winCheck(playerLast))
        break;

      System.out.println("--- PLAYER TURN ---");
      playerTurn();
      playerLast = true;

      if(winCheck(playerLast))
        break;
    }
  }

  //checks the board if the player or computer won
  public boolean winCheck(boolean p){
    for(int i = 0; i < row; i++){
      for(int j = 0; j < col; j++){
        if(board[i][j].equals("c"))
          return false;
      }
    }
    if(p==true){
      for(int i = 0; i < 5; i++)
          System.out.println("--------------------------");
      drawBoard();
      System.out.println("YOU WIN");
    }
    else{
      for(int i = 0; i < 5; i++)
          System.out.println("--------------------------");
      drawBoard();
      System.out.println("YOU LOSE");
    }

    return true;
  }

  public void moldySelector(){
    Random random = new Random();
    moldyRow = random.nextInt(3);
    moldyCol = random.nextInt(3);
    
    //Test Code
    //moldyRow = 1;
    //moldyCol = 1;

    board[moldyRow][moldyCol] = "m";
  }

  public void boardMaker(){
     for(int i = 0; i<row; i++){
        for(int j = 0; j<col; j++){
             board[i][j] = "c";
         }
    }
  }
  
  public void drawBoard() {
    for(int i = 0; i<row; i++){
      for(int j = 0; j<col; j++){
        System.out.print(board[i][j]+" ");
      }
      System.out.println("");
    }
  }  

  public void winningComputerTurn(){
    drawBoard();
    System.out.println("Thinking...");
    
    //moldy piece is in the middle, any full row or column is fine
    if(moldyRow==1 && moldyCol==1){
      if(vertiCut(0,0,2))
        return;
    }

    //***********************************************************************
    //the best move is a full row or column on the farthest part of the board
    //***********************************************************************
    
    //removes farthest row
    if(moldyCol == 1){
      if(moldyRow == 0){
        if(horiCut(2,0,2))
          return;
      }
      else if(moldyRow == 2){
        if(horiCut(0,0,2))
          return;
      }
    }

    //removes farthest column
    if(moldyRow == 1){
      if(moldyCol == 0){
        if(vertiCut(2,0,2))
          return;
      }
      else if(moldyCol == 2){
        if(vertiCut(0,0,2))
          return;
      }
    }

    //if the moldy piece is in the corner
    if(moldyRow == 0){
      //top left
      if(moldyCol == 0){
        if(vertiCut(2,0,2))
          return;
      }
      //top right
      else if(moldyCol == 2){
        if(vertiCut(0,0,2))
          return;
      }
    }
    else if(moldyRow == 2){
      //bottom left
      if(moldyCol == 0){
        if(vertiCut(2,0,2))
          return;
      }
      //bottom right
      else if(moldyCol == 2){
        if(vertiCut(0,0,2))
          return;
      }
    }

    //if it gets to this point all initial moves are exhausted
    
    //computer checks all possible moves, and checks it through the pcWinCheck() method to see if the computer wins after that move
    for(int k = 0; k<3; k++){
      for(int i = 0; i<3; i++){
        for(int j = 0; j<3; j++){
          if(vertiCheck(k,i,j) && pcWinCheck()){
            vertiCut(k,i,j);
            return;
          }
          else if(horiCheck(k,i,j) && pcWinCheck()){
            horiCut(k,i,j);
            return;
          }
        }
      }
    }

    //if all possible moves return a loss then proceed with any move
    losingComputerTurn();
  }

  //checks if the predicted move will win the computer the game
  public boolean pcWinCheck(){
    for(int i = 0; i < row; i++){
      for(int j = 0; j < col; j++){
        if(tempBoard[i][j].equals("c"))
          return false;
      }
    }

    return true;
  }

  //the computer makes a losing move by randomly selecting a move
  public void losingComputerTurn(){
    drawBoard();
    Random rng = new Random();

    boolean run = true;

    while(run){
      System.out.println("Thinking...");

      int choice = rng.nextInt(1);
      int rngStart = rng.nextInt(3);
      int rngEnd = rng.nextInt(3);

      //ensures that the end is greater than the beginning
      while(rngEnd < rngStart){
        rngEnd = rng.nextInt(3);
      }
        
      //computer picked vertical
      if(choice==1){
        int rngCol = rng.nextInt(3);
        
        if(vertiCheck(rngCol, rngStart, rngEnd)){
          vertiCut(rngCol, rngStart, rngEnd);
          run = false;
        }
      }
      //computer picked horizontal
      else{
        int rngRow = rng.nextInt(3);

        if(horiCheck(rngRow, rngStart, rngEnd)){
          horiCut(rngRow, rngStart, rngEnd);
          run = false;
        }
      }
    }
    System.out.println("");
  }

  public void playerTurn(){
    boolean run = true;
    Scanner keyboard = new Scanner(System.in);

    drawBoard();

    //while loop handles the player turn
    while(run){
      int cutStart;
      int cutEnd;
      
      //ask if the player wants to make a horizontal or vertical slice
      System.out.print("Vertical (1) or horizontal (2) slice: ");
      int choice = keyboard.nextInt();

      //if vertical, asks players the start and end row
      if(choice==1){
        System.out.print("What column: ");
        int cutCol = keyboard.nextInt();

        System.out.print("Start row: ");
        cutStart = keyboard.nextInt();
        System.out.print("End row: ");
        cutEnd = keyboard.nextInt();

        if(vertiCut(cutCol, cutStart, cutEnd))
          run = false;
      }
      //if horizontal, asks players the start and end column
      else if(choice==2){
        System.out.print("What row: ");
        int cutRow = keyboard.nextInt();

        System.out.print("Start col: ");
        cutStart = keyboard.nextInt();
        System.out.print("End col: ");
        cutEnd = keyboard.nextInt();

        if(horiCut(cutRow, cutStart, cutEnd))
          run = false;
      }
      else{
        System.out.println("Not a valid choice.");
      }
      
      System.out.println("");
    }
  }

  //checks if horizontal cut is valid (computer use)
  public boolean horiCheck(int cr, int cs, int ce){
    tempBoard = board;

    //checks if the moldy piece is getting cut out
    for(int i = cs; i<=ce; i++){
      if(board[cr][i].equals("m")){
        return false;
       }
    }

    //makes sure that it's not just empty space being cut
    boolean validCut = false;

    for(int i = cs; i<=ce; i++){
      if(!board[cr][i].equals("X")){
        validCut = true;
      }
    }

    if(!validCut){
      return false;
    }

    //makes the intended spaces cut indictated by an X
    for(int i = cs; i<=ce; i++){
      tempBoard[cr][i] = "X";
    }

    boolean entireRow = true;

    //checks if the entire row is cut or not
    for(int i = 0; i < col; i++){
      if(!board[cr][i].equals("X"))
        entireRow = false;
    }

    //if the entire row is cut
    if(entireRow){
      //if the moldy piece is on the below the cut column
      if(moldyRow>cr){
        //cuts out entire block that's not the moldy piece
        for(int i = 0; i <= cr; i++){
          for(int j = 0; j < col; j++){
            tempBoard[i][j] = "X";
          }
        }
      }
      //if the moldy piece is on the above the cut column
      else if(moldyRow<cr){
        //cuts out entire block that's not the moldy piece
        for(int i = cr; i < row; i++){
          for(int j = 0; j < col; j++){
            tempBoard[i][j] = "X";
          }
        }
      }
    }
    return true;
  }

  //checks if vertical cut is valid (computer use)
  public boolean vertiCheck(int cc, int cs, int ce){
    tempBoard = board;

    //checks if the moldy piece is getting cut out
    for(int i = cs; i<=ce; i++){
      if(board[i][cc].equals("m")){
        return false;
      }
    }

    //makes sure that it's not just empty space being cut
    boolean validCut = false;

    for(int i = cs; i<=ce; i++){
      if(!board[i][cc].equals("X")){
        validCut = true;
      }
    }

    if(!validCut){
      return false;
    }

    //makes the intended spaces cut indictated by an X
    for(int i = cs; i<=ce; i++){
      tempBoard[i][cc] = "X";
    }

    boolean entireCol = true;

    //checks if the entire col is cut or not
    for(int i = 0; i < row; i++){
      if(!board[i][cc].equals("X"))
        entireCol = false;
    }

    //if the entire col is cut
    if(entireCol){
      //if the moldy piece is on the right of the cut column
      if(moldyCol>cc){
        //cuts out entire block that's not the moldy piece
        for(int i = 0; i < row; i++){
          for(int j = 0; j <= cc; j++){
            tempBoard[i][j] = "X";
          }
        }
      }
      //if the moldy piece is on the left of the cut column
      else if(moldyCol<cc){
        //cuts out entire block that's not the moldy piece
        for(int i = 0; i < row; i++){
          for(int j = cc; j < col; j++){
            tempBoard[i][j] = "X";
          }
        }
      }
    }
    return true;
  }

  public boolean vertiCut(int cc, int cs, int ce){
    //checks if the moldy piece is getting cut out
    for(int i = cs; i<=ce; i++){
      if(board[i][cc].equals("m")){
        System.out.println("Cannot cut the mold piece out.");
        return false;
      }
    }

    //makes sure that it's not just empty space being cut
    boolean validCut = false;

    for(int i = cs; i<=ce; i++){
      if(!board[i][cc].equals("X")){
        validCut = true;
      }
    }

    if(!validCut){
      System.out.println("Cannot cut a space that doesn't exist!");
      return false;
    }

    //makes the intended spaces cut indictated by an X
    for(int i = cs; i<=ce; i++){
      board[i][cc] = "X";
    }

    boolean entireCol = true;

    //checks if the entire col is cut or not
    for(int i = 0; i < row; i++){
      if(!board[i][cc].equals("X"))
        entireCol = false;
    }

    //if the entire col is cut
    if(entireCol){
      //if the moldy piece is on the right of the cut column
      if(moldyCol>cc){
        //cuts out entire block that's not the moldy piece
        for(int i = 0; i < row; i++){
          for(int j = 0; j <= cc; j++){
            board[i][j] = "X";
          }
        }
      }
      //if the moldy piece is on the left of the cut column
      else if(moldyCol<cc){
        //cuts out entire block that's not the moldy piece
        for(int i = 0; i < row; i++){
          for(int j = cc; j < col; j++){
            board[i][j] = "X";
          }
        }
      }
    }
    return true;
  }

  public boolean horiCut(int cr, int cs, int ce){
    //checks if the moldy piece is getting cut out
    for(int i = cs; i<=ce; i++){
      if(board[cr][i].equals("m")){
        System.out.println("Cannot cut the mold piece out.");
        return false;
       }
    }

    //makes sure that it's not just empty space being cut
    boolean validCut = false;

    for(int i = cs; i<=ce; i++){
      if(!board[cr][i].equals("X")){
        validCut = true;
      }
    }

    if(!validCut){
      System.out.println("Cannot cut a space that doesn't exist!");
      return false;
    }

    //makes the intended spaces cut indictated by an X
    for(int i = cs; i<=ce; i++){
      board[cr][i] = "X";
    }

    boolean entireRow = true;

    //checks if the entire row is cut or not
    for(int i = 0; i < col; i++){
      if(!board[cr][i].equals("X"))
        entireRow = false;
    }

    //if the entire row is cut
    if(entireRow){
      //if the moldy piece is on the below the cut column
      if(moldyRow>cr){
        //cuts out entire block that's not the moldy piece
        for(int i = 0; i <= cr; i++){
          for(int j = 0; j < col; j++){
            board[i][j] = "X";
          }
        }
      }
      //if the moldy piece is on the above the cut column
      else if(moldyRow<cr){
        //cuts out entire block that's not the moldy piece
        for(int i = cr; i < row; i++){
          for(int j = 0; j < col; j++){
            board[i][j] = "X";
          }
        }
      }
    }
    return true;
  }
}