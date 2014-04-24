package gameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Applet extends JApplet {

    private MainPanel mp;
    private JPanel jp = new JPanel();
    private JFrame frame = new JFrame();

    private static JButton jb;
    private boolean jframe = false;

    @Override
    public void init() {
        mp = new MainPanel();

        setName("Game Of Life");

        jb = new JButton("Click here to start");
        jp.setLayout(new BorderLayout(5, 5));
        jp.setPreferredSize(jb.getPreferredSize());
        jp.setBackground(Color.WHITE);
        jp.add(jb, BorderLayout.CENTER);
        setSize(jp.getPreferredSize());
        add(jp);
        setVisible(true);

        jb.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (Applet.jb.getMouseListeners().length == 2 && !jframe) {
                    jframe = true;
                    frame.setTitle("Game Of Life");
                    frame.setBackground(Color.WHITE);
                    frame.setResizable(false);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.add(mp.panel1);
                    frame.pack();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
