package gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    public static int PWIDTH = 800;
    public static int PHEIGHT = 600;
    private boolean running = false;
    private Thread panel;
    private Graphics2D dbg;
    private Image dbImage;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private FpsCounter fpsCounter = new FpsCounter(5);
    private World world;
    public static boolean canUpdateWorld = false, canReset = false, canDrawGrid = true, canStep = false;
    private boolean ctrlDown = false;
    public static int offsetX = 0, offsetY = 0;
    private int prevMousex = 0, prevMousey = 0, prevOffsetX = offsetX, prevOffsetY = offsetY;
    public static int OutOfBoundsFactor = 1;
    private Random rand = new Random();
    private static int period = 1000000000 / 240;
    public static boolean canFitToWindow = false;
    private Timer timer = new Timer();
    public static int numUpdatesPerFrame = 20;

    public DrawPanel() {
        this.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
        world = new World();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        running = true;
        panel = new Thread(this);
        panel.start();
    }

    public void start() {
        if (!running) {
            running = true;
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {

        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;
        beforeTime = System.nanoTime();

        while (running) {
            fpsCounter.update();
            update();
            repaint();

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;

            if (sleepTime > 0) {
                try {
                    panel.sleep(sleepTime / 1000000);
                } catch (InterruptedException ex) {
                }
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {
                excess -= sleepTime;
                overSleepTime = 0L;

                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    panel.yield();
                    noDelays = 0;
                }
            }

            beforeTime = System.nanoTime();

            fpsCounter.tick();
        }
    }

    public void update() {
        if (Main.currFrameDim != null && !Main.currFrameDim.equals(Main.frame.getSize())) {
            this.setPreferredSize(Main.frame.getSize());
            PWIDTH = this.getWidth();
            PHEIGHT = this.getHeight();
            Main.currFrameDim = Main.frame.getSize();
            Main.frame.validate();
        }

        if (canFitToWindow) {
            canFitToWindow = false;
            offsetX = 0;
            offsetY = 0;
            prevOffsetX = offsetX;
            prevOffsetY = offsetY;
        }

        try {
            if (OutOfBoundsFactor > 0) {
                world.clearCellsOutOfBounds();
            }
        } catch (Exception ex) {
        }

        if (canUpdateWorld || canStep) {
            if (timer.sleep(1000 / numUpdatesPerFrame)) {
                world.update();
                canStep = false;
            }
        }
        if (canReset) {
            resetAll();
        }
        MainPanel.numRowCol.setText("Rows: " + world.getMap()[0].length + ", Cols: " + world.getMap().length);
        MainPanel.state.setText("Running: " + canUpdateWorld);
        MainPanel.numGens.setText("Generations: " + World.numOfGenerations);
    }

    public void resetAll() {
        numUpdatesPerFrame = 20;
        MainPanel.grid.setSelected(true);
        MainPanel.autoResize.setSelected(false);
        MainPanel.updatesPerSecond.setText("Update(s) Per Frame: " + DrawPanel.numUpdatesPerFrame);
        OutOfBoundsFactor = 1;
        PWIDTH = this.getWidth();
        PHEIGHT = this.getHeight();
        world.reset(world.getMap());
        World.resetWidthHeight();
        World.setNumRowCol(PWIDTH / World.cellWidth, PHEIGHT / World.cellHeight);
        offsetX = 0;
        offsetY = 0;
        prevMousex = 0;
        prevMousey = 0;
        prevOffsetX = offsetX;
        prevOffsetY = offsetY;
        canUpdateWorld = false;
        canReset = false;
        canStep = false;
        World.numOfGenerations = 0;
        World.canAutoResize = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        // create image
        dbImage = createImage(PWIDTH, PHEIGHT);
        dbg = (Graphics2D) dbImage.getGraphics();
        // clear everything
        dbg.setColor(new Color(50, 50, 50));
        dbg.fillRect(0, 0, PWIDTH, PHEIGHT);
        // draw everything to the image
        draw(dbg);
        // paint image on the screen and clear graphics
        g.drawImage(dbImage, 0, 0, null);
        g.dispose();
    }

    public void draw(Graphics2D g2d) {
        int sizeOffset = 1;
        if ((World.cellHeight < 5 && World.cellWidth < 5) || !canDrawGrid) {
            sizeOffset = 0;
        }
        for (int x = 0; x < world.getMap().length; x++) {
            for (int y = 0; y < world.getMap()[0].length; y++) {
                if (world.getMap()[x][y] == 1) {
                    g2d.setColor(new Color(150, 150, 150));
                    g2d.fill(new Rectangle2D.Double(x * World.cellWidth + offsetX + sizeOffset, y * World.cellHeight + offsetY + sizeOffset, World.cellWidth - sizeOffset, World.cellHeight
                            - sizeOffset));
                    if (World.cellHeight >= 5 && World.cellWidth >= 5) {
                        if (!canDrawGrid) {
                            g2d.setColor(new Color(0, 0, 0));
                        } else {
                            g2d.setColor(new Color(50, 50, 50));
                        }
                        g2d.draw(new Rectangle2D.Double(x * World.cellWidth + offsetX + sizeOffset, y * World.cellHeight + offsetY + sizeOffset, World.cellWidth - sizeOffset, World.cellHeight
                                - sizeOffset));
                    }
                }
            }
        }

        if (World.getCellHeight() > 1 && World.getCellWidth() > 1 && canDrawGrid) {
            g2d.setColor(new Color(2, 2, 2));
            for (int i = OutOfBoundsFactor; i < world.getMap().length - OutOfBoundsFactor + 1; i++) {
                if (i * (World.cellWidth) + offsetX >= 0 && i * (World.cellWidth) + offsetX <= PWIDTH) {
                    g2d.draw(new Line2D.Double(i * (World.cellWidth) + offsetX, 0 + offsetY + World.cellHeight * OutOfBoundsFactor, i * World.cellWidth + offsetX, world.getMap()[0].length
                            * World.cellHeight + offsetY - (World.cellHeight * OutOfBoundsFactor)));
                }
            }
            for (int i = OutOfBoundsFactor; i < world.getMap()[0].length - OutOfBoundsFactor + 1; i++) {
                if (i * World.cellHeight + offsetY >= 0 && i * World.cellHeight + offsetY <= PHEIGHT) {
                    g2d.draw(new Line2D.Double(0 + offsetX + World.cellWidth * OutOfBoundsFactor, i * World.cellHeight + offsetY, world.getMap().length * World.cellWidth + offsetX
                            - (World.cellWidth * OutOfBoundsFactor), i * World.cellHeight + offsetY));
                }
            }
        }
        {
            g2d.setColor(Color.RED);
            g2d.draw(new Line2D.Double(offsetX + World.cellWidth * OutOfBoundsFactor, World.cellHeight * OutOfBoundsFactor + offsetY, world.getMap().length * World.cellWidth + offsetX
                    - (World.cellWidth * OutOfBoundsFactor), World.cellHeight * OutOfBoundsFactor + offsetY));
            g2d.draw(new Line2D.Double(offsetX + World.cellWidth * OutOfBoundsFactor, world.getMap()[0].length * World.cellHeight + offsetY - (World.cellHeight * OutOfBoundsFactor),
                    world.getMap().length * World.cellWidth + offsetX - (World.cellWidth * OutOfBoundsFactor), world.getMap()[0].length * World.cellHeight + offsetY
                    - (World.cellHeight * OutOfBoundsFactor)
            ));
            g2d.draw(new Line2D.Double(World.cellWidth * OutOfBoundsFactor + offsetX, offsetY + World.cellHeight * OutOfBoundsFactor, World.cellWidth * OutOfBoundsFactor + offsetX,
                    world.getMap()[0].length * World.cellHeight + offsetY - (World.cellHeight * OutOfBoundsFactor)));
            g2d.draw(new Line2D.Double(world.getMap().length * World.cellWidth + offsetX - (World.cellWidth * OutOfBoundsFactor), offsetY + World.cellHeight * OutOfBoundsFactor, world.getMap().length
                    * World.cellWidth + offsetX - (World.cellWidth * OutOfBoundsFactor), world.getMap()[0].length * World.cellHeight + offsetY - (World.cellHeight * OutOfBoundsFactor)));
        }
    }

    public static void setFrameNanoseconds(int f) {
        if (f > 0 && f <= 240) {
            period = 1000000000 / f;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 17) {
            ctrlDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == 17) {
            ctrlDown = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.getX() < PWIDTH && e.getY() < PHEIGHT && e.getX() > 0 && e.getY() > 0) {
                try {
                    if (world.getMap()[(e.getX() - offsetX) / World.cellWidth][(e.getY() - offsetY) / World.cellHeight] == 0) {
                        world.setMap((e.getX() - offsetX) / World.cellWidth, (e.getY() - offsetY) / World.cellHeight, 1);
                    }
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println(ex);
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (e.getX() < PWIDTH && e.getY() < PHEIGHT && e.getX() > 0 && e.getY() > 0) {
                try {
                    if (world.getMap()[(e.getX() - offsetX) / World.cellWidth][(e.getY() - offsetY) / World.cellHeight] == 1) {
                        world.setMap((e.getX() - offsetX) / World.cellWidth, (e.getY() - offsetY) / World.cellHeight, 0);
                    }
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println(ex);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocus();
        prevMousex = e.getX();
        prevMousey = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        prevOffsetX = offsetX;
        prevOffsetY = offsetY;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!ctrlDown && e.getModifiers() == 16) {
            if (e.getX() < PWIDTH && e.getY() < PHEIGHT && e.getX() > 0 && e.getY() > 0) {
                try {
                    if (world.getMap()[((e.getX() - offsetX) / World.cellWidth)][(e.getY() - offsetY) / World.cellHeight] == 0) {
                        world.setMap((e.getX() - offsetX) / World.cellWidth, (e.getY() - offsetY) / World.cellHeight, 1);
                    }
                } catch (IndexOutOfBoundsException ex) {
                    // System.err.println(ex);
                }
            }
        } else if (e.getModifiers() == 4) {
            if (e.getX() < PWIDTH && e.getY() < PHEIGHT && e.getX() > 0 && e.getY() > 0) {
                try {
                    if (world.getMap()[(e.getX() - offsetX) / World.cellWidth][(e.getY() - offsetY) / World.cellHeight] == 1) {
                        world.setMap((e.getX() - offsetX) / World.cellWidth, (e.getY() - offsetY) / World.cellHeight, 0);
                    }
                } catch (IndexOutOfBoundsException ex) {
                    // System.err.println(ex);
                }
            }
        }

        if (e.getModifiers() == 17) {
            if (e.getX() < PWIDTH && e.getY() < PHEIGHT && e.getX() > 0 && e.getY() > 0) {
                try {

                    if (world.getMap()[(e.getX() - offsetX) / World.cellWidth][(prevMousey - offsetY) / World.cellHeight] == 0) {
                        world.setMap((e.getX() - offsetX) / World.cellWidth, (prevMousey - offsetY) / World.cellHeight, 1);
                    }
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println(ex);
                }
            }
        }
        if (e.getModifiers() == 8 || (e.getModifiers() == 18 && e.getX() < PWIDTH && e.getY() < PHEIGHT && e.getX() > 0 && e.getY() > 0)) {
            offsetX = (e.getX() + prevOffsetX) - prevMousex;
            offsetY = (e.getY() + prevOffsetY) - prevMousey;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public static void log(int s) {
        System.out.println(s);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int val1 = World.cellHeight - e.getWheelRotation();
        int val2 = World.cellWidth - e.getWheelRotation();
        if (e.getModifiers() != 2 && val1 > 1 && val1 <= 30 && val2 > 1 && val2 <= 30) {
            offsetX += ((PWIDTH * World.cellWidth) - (PWIDTH * (World.cellWidth + e.getWheelRotation()))) / (Math.pow(World.cellHeight, 1000));
            offsetY += ((PHEIGHT * World.cellHeight) - (PHEIGHT * (World.cellHeight + e.getWheelRotation()))) / (Math.pow(World.cellWidth, 1000));
            prevOffsetX = offsetX;
            prevOffsetY = offsetY;
            World.setCellHeight(val1);
            World.setCellWidth(val2);
        }
    }

    private class Timer {

        private long prevTime;
        private long currTime;
        private long diff;

        public Timer() {
            this.prevTime = System.currentTimeMillis();
            this.currTime = System.currentTimeMillis();
            this.diff = 0;
        }

        public boolean sleep(int sleepTime) {
            diff = currTime - prevTime;
            if (diff >= sleepTime) {
                currTime = System.currentTimeMillis();
                prevTime = currTime;
                diff = 0;
                return true;
            }
            currTime = System.currentTimeMillis();
            return false;
        }
    }
}
