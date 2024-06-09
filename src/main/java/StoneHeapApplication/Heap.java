package StoneHeapApplication;

public class Heap {      // куча камней

    private int number;     // изначальное число камней в куче
    private int currentNumber; // текущее число камней в куче
    private int [] stones;

    public Heap (int number){
        this.number = number;
        this.currentNumber = number;
        this.stones = new int [number];
        for (int i = 0; i < number; ++i)
            stones[i] = 1;
    }

    public int getNumber() {return number;}
    public void setNumber(int number) {this.number = number;}

    public int getCurrentNumber() {return currentNumber;}
    public void setCurrentNumber(int number) {this.currentNumber = number;}

    public void deleteStone (int n) {stones[n] = 0; currentNumber--;}
    public int getStone (int n) {return stones[n];}

    public void setStones(int [] a){
        System.arraycopy(a, 0, stones, 0, number);
    }
}
