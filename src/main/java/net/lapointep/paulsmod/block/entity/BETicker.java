package net.lapointep.paulsmod.block.entity;

public class BETicker {

    int counter = 0;
    int max_counter = 0;

    public BETicker(int pMax_counter) {
        max_counter = pMax_counter;
        counter = 0;
    }

    public boolean notReadyToRun() {
        counter++;
        if (counter >= max_counter) {
            counter = 0;
            return false;
        }
        return true;
    }

    public void setCounter(int pCounter) {
        max_counter = pCounter;
    }

    public int getCounter() {
        return max_counter;
    }
}
