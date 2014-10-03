package game;

import java.util.Random;

public class MazeGenerator {
    
    public int[][] board; //0 = wall
                          //1 = path
                          //2 = blocked path (for maze edges and to block spawns/exits)
    public int numTeams = 2;
    public int[] points;
    
    public MazeGenerator() {
        this(16, 16, 2);
    }
    public MazeGenerator(int mazeRows, int mazeCols, int numTeams) {
        board = new int[mazeRows][mazeCols];
        for(int r = 1; r < mazeRows - 1; r++) {
            for(int c = 1; c < mazeCols - 1; c++) {
                board[r][c] = 1; //STONE
            }
        }
        if(numTeams > 4) {
            numTeams = 4;
        }
        this.numTeams = numTeams;
        points = new int[2 + numTeams * 2];
//        generateMaze();
    }
    public MazeGenerator(int mazeRows, int mazeCols) {
        this(mazeRows, mazeCols, 2);
    }
    
    public final void generateMaze() {
        Random rand = new Random();
        int row, col;
        for(int t = 0; t < numTeams + 1; t++) {
            if(t == 0) {
                //makes exit
                row = rand.nextInt(board.length - 2) + 1;
                col = rand.nextInt(board[0].length - 2) + 1;
                points[0] = row;
                points[1] = col;
//                int exit = rand.nextInt(4);
//                for(int r = row - 2; r < row + 3; r++) {
//                    for(int c = col - 2; c < col + 3; c++) {
//                        if(c == col - 2 || r == row - 2 || c == col + 2 || r == row + 2) {
//                            board[r][c] = 0; //VOID
//                        } else {
//                            board[r][c] = 2; //GRASS
//                        }
//                    }
//                }
            } else {
                //makes spawn
                row = rand.nextInt(board.length - 6) + 3;
                col = rand.nextInt(board[0].length - 6) + 3;
                points[2 * t] = row;
                points[2 * t + 1] = col;
                for(int r = row - 2; r < row + 3; r++) {
                    for(int c = col - 2; c < col + 3; c++) {
                        if(c == col - 2 || r == row - 2 || c == col + 2 || r == row + 2) {
                            board[r][c] = 0; //VOID
                        } else {
                            board[r][c] = 2; //GRASS
                        }
                    }
                }
            }
        }
        //TODO paths
        int r = points[0];
        int c = points[1];
        int dir = 0, lastDir = 0, times = 0;
        while(times < 10) {
            lastDir = dir;
            dir = rand.nextInt(4); //random: 0 to 3
            
            if(lastDir != dir) {
                switch(dir) {
                    case 0: //right
                        if(c < board[0].length - 2);
                            c+=3;
                        break;
                    case 1: //up
                        if(r > 3)
                            r-=3;
                        break;
                    case 2: //left
                        if(c > 3);
                            c-=3;
                        break;
                    case 3: //down
                        if(r < board.length - 2)
                            r+=3;
                        break;
                }
            }
            for(row = r - 1; row <= r + 1; row++) {
                for(col = c - 1; col <= c + 1; col++) {
                    board[row][col] = 2;
                }
            }
            times++;
        }
    }
    
    private boolean checkIfBackwards(int r, int c) {
        return false;
    }
    
    public void printMaze() {
        for(int r = 0; r < board.length; r++) {
            for(int c = 0; c < board[r].length; c++) {
                 System.out.printf("%3d", board[r][c]);
            }
            System.out.println();
        }
    }
   
    public int[][] getBoard() {
        return board;
    }
    public int[] get1DBoard() {
        int[] temp = new int[board.length * board[0].length];
        for(int r = 0; r < board.length; r++) {
            for(int c = 0; c < board[0].length; c++) {
                temp[c + r * board[0].length] = board[r][c];
            }
        }
        return temp;
    }
    
    public void setBoard(int row, int col, int newValue) {
        board[row][col] = newValue;
    }
}
