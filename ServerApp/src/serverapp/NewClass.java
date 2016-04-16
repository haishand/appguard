/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

/**
 *
 * @author haishand
 */
public class NewClass {
    public boolean isValidSudoku(char[][] board) {
        int[] used = new int[10];
        
        // check row
        for (int i=0; i<9; i++) {
            for (int k=0; k<10; k++) used[k] = 0;
            for (int j=0; j<9; j++) {
                if (board[i][j]=='.') {
                    continue;
                }
                if (used[board[i][j]-'0'] != 0) {
                    return false;
                }
                used[board[i][j]-'0']=1;
            }
        }
        
        // check col
        for (int i=0; i<9; i++) {
            for (int k=0; k<10; k++) used[k] = 0;
            for (int j=0; j<9; j++) {
                if (board[j][i]=='.') {
                    continue;
                }                
                if (used[board[j][i]-'0'] != 0) return false;
                used[board[j][i]-'0']=1;
            }
        }      
        
        // check squares
        for (int r=0; r<9; r+=3) {
            for (int c=0; c<9; c+=3) {
                for (int k=0; k<10; k++) used[k] = 0;
                for (int i=0; i<=2; i++) {
                    for (int j=0;j<=2; j++) {
                    if (board[r+i][c+j]=='.') {
                        continue;
                    }                        
                    if (used[board[r+i][c+j]-'0'] != 0) return false;
                        used[board[r+i][c+j]-'0'] = 1;
                    }                
                }
            }
        }
        
        return true;
        
    }
    
    public static void main(String[] args) {
        char[][] aa = {
            {'.','8','7','6','5','4','3','2','1'},
            {'2','.','.','.','.','.','.','.','.'},
            {'3','.','.','.','.','.','.','.','.'},
            {'4','.','.','.','.','.','.','.','.'},
            {'5','.','.','.','.','.','.','.','.'},
            {'6','.','.','.','.','.','.','.','.'},
            {'7','.','.','.','.','.','.','.','.'},
            {'8','.','.','.','.','.','.','.','.'},
            {'9','.','.','.','.','.','.','.','.'}
        };
        System.out.println(new NewClass().isValidSudoku(aa));
    }
}
