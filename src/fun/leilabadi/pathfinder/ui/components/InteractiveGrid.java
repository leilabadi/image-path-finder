package fun.leilabadi.pathfinder.ui.components;

public interface InteractiveGrid {

    void loadData(int[][] data);

    int[][] getData();

    void setActivationState(int activationState);
}
