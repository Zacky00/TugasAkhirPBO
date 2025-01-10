import java.util.Random;
public class Board extends Sudokuboard {
    private int [][] grid;
    private boolean [][] Iseditable;

    public boolean getIseditable(int a, int b) {
        return Iseditable[a][b];
    }
    public int getgrid(int i, int j){
        return grid[i][j];
    }
    Board(String kesulitan) {
        grid = new int[9][9];
        Iseditable = new boolean[9][9];
        if(kesulitan=="Easy"){
            Generatepuzzle(20);
        }
        else if(kesulitan=="Medium"){
            Generatepuzzle(30);
        }
        else if(kesulitan=="Hard"){
            Generatepuzzle(40);
        }
    }
    @Override
    void Generatepuzzle(int angka) {
        fillBoard(0, 0);
        removeNumbers(angka);
    }
    public boolean fillBoard(int row, int col) {
        if (row == 9) {
            return true;
        }

        int nextRow = col == 8 ? row + 1 : row;
        int nextCol = col == 8 ? 0 : col + 1;

        Random rand = new Random();
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = nums.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        for (int num : nums) {
            if (isValid(row, col, num)) {
                grid[row][col] = num;

                if (fillBoard(nextRow, nextCol)) {
                    return true;
                }

                grid[row][col] = 0;
            }
        }

        return false;
    }
    public void removeNumbers(int count) {
        Random rand = new Random();
        int removed = 0;

        while (removed < count) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            if (grid[row][col] != 0) {
                grid[row][col] = 0;
                Iseditable[row][col] = true;
                removed++;
            }
        }
    }
    public boolean insert(int row, int col, int num) {
        if (!Iseditable[row][col]) {
            System.out.println("Sel ini tidak dapat diubah.");
            return false;
        }

        if (!isValid(row, col, num)) {
            System.out.println("Angka tidak valid di posisi ini.");
            return false;
        }

        grid[row][col] = num;
        Iseditable[row][col] = false;
        return true;
    }
    public boolean isValid(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (grid[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public boolean Iscompleted() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            boolean[] found = new boolean[10];
            for (int j = 0; j < 9; j++) {
                int num = grid[i][j];
                if (found[num]) {
                    return false;
                }
                found[num] = true;
            }
        }
        for (int j = 0; j < 9; j++) {
            boolean[] found = new boolean[10];
            for (int i = 0; i < 9; i++) {
                int num = grid[i][j];
                if (found[num]) {
                    return false;
                }
                found[num] = true;
            }
        }
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                boolean[] found = new boolean[10];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = boxRow * 3 + i;
                        int col = boxCol * 3 + j;
                        int num = grid[row][col];
                        if (found[num]) {
                            return false;
                        }
                        found[num] = true;
                    }
                }
            }
        }
        return true;
    }
}