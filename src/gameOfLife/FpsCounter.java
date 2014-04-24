package gameOfLife;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FpsCounter {

    private ArrayList<Integer> fps = new ArrayList<Integer>();
    private int tick;
    private static double currFps = 0;
    private static double avgFps;
    private double minFps;
    private double maxFps;
    private long prevTime;
    private int numOfFpsToAvgFrom;

    public static double getFps() {
        return avgFps;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public FpsCounter() {
        this.tick = 0;
        this.avgFps = 0;
        this.minFps = 0;
        this.maxFps = 0;
        this.prevTime = System.nanoTime();
        this.numOfFpsToAvgFrom = 5;
    }

    public FpsCounter(int n) {
        this.tick = 0;
        this.avgFps = 0;
        this.minFps = 0;
        this.maxFps = 0;
        this.prevTime = System.nanoTime();
        this.numOfFpsToAvgFrom = n;
    }

    public void tick() {
        tick++;
    }

    public void displayFps() {
        System.out.print("Avg FPS: " + avgFps + ", ");
        System.out.print("Min: " + minFps + ", ");
        System.out.print("Max: " + maxFps);
        System.out.println();
    }

    public void update() {
        if (System.nanoTime() - prevTime >= 1000000000) {
            if (fps.size() >= numOfFpsToAvgFrom) {
                fps.remove(0);
            }
            currFps = tick;
            fps.add(tick);
            avgFps = getAvgFps(numOfFpsToAvgFrom);
            //minFps = getMinFps();
            //maxFps = getMaxFps();
            tick = 0;
            prevTime = System.nanoTime();
            //displayFps();
        }
    }

    public static double getCurrFps() {
        return currFps;
    }

    public double getAvgFps(int n) {
        double f = 0;
        if (fps.size() > 0) {
            for (Integer i : fps) {
                f += i;
            }

            f = f / fps.size();
        }
        BigDecimal b = new BigDecimal(f).setScale(1, BigDecimal.ROUND_HALF_UP);
        return b.doubleValue();
    }

    public void printFpsArray() {
        for (Integer i : fps) {
            System.out.println(i);
        }
    }

    public double getMinFps() {
        double f = 0;
        if (fps.size() > 0) {
            f = fps.get(0);
            for (Integer i : fps) {
                if (f > i) {
                    f = i;
                }
            }
        }
        return f;
    }

    public double getMaxFps() {
        double f = 0;
        if (fps.size() > 0) {
            f = fps.get(0);
            for (Integer i : fps) {
                if (f < i) {
                    f = i;
                }
            }
        }
        return f;
    }

}
