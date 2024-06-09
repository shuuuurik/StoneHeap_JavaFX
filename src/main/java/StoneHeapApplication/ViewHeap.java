package StoneHeapApplication;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ViewHeap {
    private Heap heap;
    private GridPane grid;
    private Circle [] circles;

    private void createPane() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(30);
        grid.setVgap(30);
        grid.setPadding(new Insets(25, 25, 25, 25));
    }
    private void createCircles() {
        circles = new Circle[heap.getNumber()];
        for (int i = 0; i < heap.getNumber(); ++i) {
                circles[i] = new Circle(10, Color.DARKGREY);
                grid.add(circles[i], i % 10, i / 10);
            if (heap.getStone(i) == 0)
                circles[i].setVisible(false);
        }
    }

    public GridPane getPane() {
        return grid;
    }
    public Circle[] getCircles() {
        return circles;
    }

    public void setHeap (Heap heap) {
        this.heap = heap;
    }

    public ViewHeap(Heap heap) {
        setHeap(heap);
        createPane();
        createCircles();
    }
}
