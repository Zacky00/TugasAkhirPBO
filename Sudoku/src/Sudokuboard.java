abstract class Sudokuboard{

    abstract void Generatepuzzle(int angka);
    abstract boolean Iscompleted();
    abstract boolean fillBoard(int row, int col);
    abstract void removeNumbers(int count);
    abstract boolean insert(int row, int col, int num);
    abstract boolean isValid(int row, int col, int num);
}