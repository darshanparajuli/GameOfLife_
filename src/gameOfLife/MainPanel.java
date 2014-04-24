package gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainPanel {

    public static JPanel panel1 = new JPanel();
    private DrawPanel dp = new DrawPanel();
    public static JPanel panel2 = new JPanel();
    public static JPanel panel3 = new JPanel();
    public static JButton start = new JButton("Start");
    public static JButton stop = new JButton("Stop");
    public static JButton reset = new JButton("RESET ALL");
    public static JCheckBox grid = new JCheckBox();
    public static JCheckBox autoResize = new JCheckBox();
    public static JButton step = new JButton("1 Step");
    public static JButton setSpeed = new JButton("Set Speed");
    public static JButton help = new JButton("Mouse Help");
    public static JLabel numGens = new JLabel();
    public static JButton setCellNum = new JButton("Set Num of Cells");
    public static JButton insertPattern = new JButton("Insert Pattern");
    public static JLabel numRowCol = new JLabel();
    public static JLabel state = new JLabel();
    public static JButton fitToWindow = new JButton("Fit to Window");
    public static JLabel updatesPerSecond = new JLabel("Update(s) Per Frame: " + DrawPanel.numUpdatesPerFrame);

    public MainPanel() {
        panel1.setBackground(Color.GRAY);

        panel1.setLayout(new BorderLayout(5, 5));

        panel2.setBackground(new Color(220, 220, 220));
        panel3.setBackground(new Color(220, 220, 220));

        reset.setBackground(panel2.getBackground());
        stop.setBackground(panel2.getBackground());
        start.setBackground(panel2.getBackground());
        step.setBackground(panel2.getBackground());

        panel2.add(state, FlowLayout.LEFT);
        panel2.add(numRowCol, FlowLayout.LEFT);
        panel2.add(numGens, FlowLayout.LEFT);
        panel2.add(updatesPerSecond, FlowLayout.LEFT);
        panel2.add(reset, FlowLayout.LEFT);
        panel2.add(stop, FlowLayout.LEFT);
        panel2.add(start, FlowLayout.LEFT);
        panel2.add(step, FlowLayout.LEFT);

        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

        autoResize.setSelected(false);
        autoResize.setBackground(panel3.getBackground());
        autoResize.setPreferredSize(new Dimension(20, 15));
        grid.setSelected(true);
        grid.setBackground(panel3.getBackground());
        grid.setPreferredSize(new Dimension(20, 15));
        help.setBackground(panel3.getBackground());
        setCellNum.setBackground(panel3.getBackground());
        setSpeed.setBackground(panel3.getBackground());
        insertPattern.setBackground(panel3.getBackground());

        panel3.add(help, FlowLayout.LEFT);
        panel3.add(insertPattern, FlowLayout.LEFT);
        panel3.add(fitToWindow, FlowLayout.LEFT);
        panel3.add(setCellNum, FlowLayout.LEFT);
        panel3.add(setSpeed, FlowLayout.LEFT);
        panel3.add(grid, FlowLayout.LEFT);
        panel3.add(new JLabel("Grid (on/off):"), FlowLayout.LEFT);
        panel3.add(autoResize, FlowLayout.LEFT);
        panel3.add(new JLabel("Infinte(on/off):"), FlowLayout.LEFT);

        panel3.setLayout(new FlowLayout(FlowLayout.LEFT));

        panel1.add(panel2, BorderLayout.NORTH);
        panel1.add(dp, BorderLayout.CENTER);
        panel1.add(panel3, BorderLayout.SOUTH);

        MainPanel.insertPattern.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.insertPattern.getMouseListeners().length == 2) {
                    String temp = (String) JOptionPane.showInputDialog(dp, "Patterns:", "Patterns", JOptionPane.PLAIN_MESSAGE, null, World.patterns, World.patterns[0]);
                    if(temp == null)
                    {
                        return;
                    }
                    try {
                        String x = (String) JOptionPane.showInputDialog(dp, "X cell: ", "Location X", JOptionPane.INFORMATION_MESSAGE);
                        String y = (String) JOptionPane.showInputDialog(dp, "Y cell: ", "Location Y", JOptionPane.INFORMATION_MESSAGE);

                        int xx = Integer.parseInt(x);
                        int yy = Integer.parseInt(y);
                        if (xx >= 5 && yy >= 5) {
                            World.insertPattern(temp, xx, yy);
                        } else if (temp == "Full") {
                            if (xx >= 0 && yy >= 0) {
                                World.insertPattern(temp, xx, yy);
                            }
                        }
                    } catch (Exception ex) {
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        MainPanel.setCellNum.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.setCellNum.getMouseListeners().length == 2) {
                    String tempc = (String) JOptionPane.showInputDialog(dp, "Number of Columns: ", "Set Number of Columns", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(tempc);
                    if(tempc == null)
                    {
                        return;
                    }
                    String tempr = (String) JOptionPane.showInputDialog(dp, "Number of Rows: ", "Set Number of Rows", JOptionPane.INFORMATION_MESSAGE);
                    if(tempr == null)
                    {
                        return;
                    }
                    try {
                        int c = Integer.parseInt(tempc);
                        if (c >= 15) {
                            World.setNumOfCol(c);
                            World.numOfGenerations = 0;
                        }
                        int r = Integer.parseInt(tempr);
                        if (r >= 15) {
                            World.setNumOfRow(r);
                            World.numOfGenerations = 0;
                        }
                    } catch (Exception ex) {
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        MainPanel.start.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.start.getMouseListeners().length == 2) {
                    DrawPanel.canUpdateWorld = true;
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        MainPanel.stop.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.stop.getMouseListeners().length == 2) {
                    DrawPanel.canUpdateWorld = false;
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        MainPanel.reset.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.reset.getMouseListeners().length == 2) {
                    DrawPanel.canReset = true;
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        MainPanel.fitToWindow.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.fitToWindow.getMouseListeners().length == 2) {
                    World.setNumRowCol(DrawPanel.PWIDTH / World.cellWidth, DrawPanel.PHEIGHT / World.cellHeight);
                    DrawPanel.canFitToWindow = true;
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        MainPanel.help.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.help.getMouseListeners().length == 2) {
                    String helpMessage = "Left Click or Left Click + Drag = add cell(s) to that spot\n"
                            + "Right Click or Right Click + Drag = remove cell(s) from that spot \n"
                            + "Mouse Wheel Roll Up or Down = zoom in or out\n"
                            + "Mouse Wheel Click + Drag = move map";
                    JOptionPane.showMessageDialog(dp, helpMessage, MainPanel.help.getText(), JOptionPane.INFORMATION_MESSAGE);
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        MainPanel.grid.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getItemSelectable().getSelectedObjects() != null) {
                    DrawPanel.canDrawGrid = true;
                } else {
                    DrawPanel.canDrawGrid = false;
                }
            }
        });
        MainPanel.autoResize.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getItemSelectable().getSelectedObjects() != null) {
                    World.canAutoResize = true;
                    DrawPanel.OutOfBoundsFactor = 0;
                } else {
                    World.canAutoResize = false;
                    DrawPanel.OutOfBoundsFactor = 1;
                }
            }
        });
        MainPanel.setSpeed.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (MainPanel.setSpeed.getMouseListeners().length == 2) {
                    String fps = (String) JOptionPane.showInputDialog(dp, "Update(s) per frame: ", "Set Speed", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        int f = (Integer.parseInt(fps));
                        if(f > 240){
                            f = 240;
                        } if( f <= 0 ){
                            f = 1;
                        }
                        DrawPanel.numUpdatesPerFrame = f;
                        updatesPerSecond.setText("Update(s) Per Frame: " + DrawPanel.numUpdatesPerFrame);
                    } catch (Exception ex) {
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        MainPanel.step.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (MainPanel.step.getMouseListeners().length == 2) {
                    if (!DrawPanel.canUpdateWorld) {
                        DrawPanel.canStep = true;
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
    }

    public DrawPanel getDp() {
        return dp;
    }
}
