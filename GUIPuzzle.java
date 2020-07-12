import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class GUIPuzzle extends JFrame {
    ConfigurationStore store;
    ArrayList < Tiles > cachedTiles;
    Tiles tiles;
    private GamePanel gamePanel;

    public GUIPuzzle(ConfigurationStore cs) {
        super("15-puzzle");
        store = cs;
        cachedTiles = new ArrayList < Tiles > ();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 768);
        add(createConfigurationsPanel(), BorderLayout.WEST);
        add(createControlPanel(), BorderLayout.SOUTH);
        add(createGamePanel(), BorderLayout.CENTER);
    }

    private void addBorder(JComponent component, String title) {
        Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        Border tb = BorderFactory.createTitledBorder(etch, title);
        component.setBorder(tb);
    }

    private JPanel createGamePanel() {
        gamePanel = new GamePanel();
        addBorder(gamePanel, "Game Panel");
        return gamePanel;
    }

    private JPanel createConfigurationsPanel() {
        JPanel conf = new JPanel();
        addBorder(conf, "Configurations");

        JScrollPane scroll = new JScrollPane();

        // String[] data = store.getConfigurationsSorted().stream().map(e -> e.getData()).toArray(String[]::new);
        Configuration[] data = store.getConfigurationsSorted().stream().toArray(Configuration[]::new);
        JList < Configuration > list = new JList < > (data);
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                JList<Configuration> list = JList.class.cast(e.getSource());
                // JList < Configuration > list = (JList < Configuration > ) e.getSource();
                Configuration c = list.getSelectedValue();

                try {
                    cachedTiles.clear();
                    cachedTiles.add(new MatrixTiles(c));
                    tiles = cachedTiles.get(0);

                    gamePanel.display(tiles);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        conf.setBackground(Color.decode("#C19A6B"));
        scroll.setViewportView(list);
        conf.setLayout(new BorderLayout());
        conf.add(scroll, BorderLayout.CENTER);

        return conf;
    }

    private JPanel createControlPanel() {
        JPanel ctrl = new JPanel();
        addBorder(ctrl, "Controls");
        ctrl.setLayout(new GridLayout(2, 3));

        JButton leftBtn = new JButton("LEFT");
        JButton rightBtn = new JButton("RIGHT");
        JButton upBtn = new JButton("UP");
        JButton downBtn = new JButton("DOWN");
        JButton backBtn = new JButton("< BACK");
        JButton forwardBtn = new JButton("FORWARD >");

        leftBtn.addActionListener(e -> slide(Tiles.Direction.LEFT));
        upBtn.addActionListener(e -> slide(Tiles.Direction.UP));
        rightBtn.addActionListener(e -> slide(Tiles.Direction.RIGHT));
        backBtn.addActionListener(e -> moveBack());
        downBtn.addActionListener(e -> slide(Tiles.Direction.DOWN));
        forwardBtn.addActionListener(e -> moveForward());

        ctrl.add(leftBtn);
        ctrl.add(upBtn);
        ctrl.add(rightBtn);
        ctrl.add(backBtn);
        ctrl.add(downBtn);
        ctrl.add(forwardBtn);

        return ctrl;
    }

    private void slide(Tiles.Direction d) {
        if (tiles != null) {
            switch (d) {
                case UP:
                    if (tiles.getEmptyPositon() / Tiles.SIZE >= Tiles.SIZE - 1) {
                        System.out.println("Error: Illegal move");
                        return;
                    }
                    break;
                case DOWN:
                    if (tiles.getEmptyPositon() / Tiles.SIZE <= 0) {
                        System.out.println("Error: Illegal move");
                        return;
                    }
                    break;
                case LEFT:
                    if ((tiles.getEmptyPositon() + 1) % Tiles.SIZE == 0) {
                        System.out.println("Error: Illegal move");
                        return;
                    }
                    break;
                case RIGHT:
                    if ((tiles.getEmptyPositon()) % Tiles.SIZE == 0) {
                        System.out.println("Error: Illegal move");
                        return;
                    }
                    break;
            }

            try {
                cachedTiles.set(tiles.getMoveCount() + 1, copyTiles(true));
                cachedTiles.subList(tiles.getMoveCount() + 2, cachedTiles.size()).clear();
            } catch (Exception e) {
                cachedTiles.add(tiles.getMoveCount() + 1, copyTiles(true));
            }
            tiles = cachedTiles.get(tiles.getMoveCount() + 1);
            tiles.move(d);
            gamePanel.display(tiles);
        }
    }

    private void moveBack() {
        if (tiles.getMoveCount() > 0) {
            this.tiles = cachedTiles.get(tiles.getMoveCount() - 1);
            gamePanel.display(tiles);
        }
    }

    private void moveForward() {
        if (tiles.getMoveCount() < cachedTiles.size() - 1) {
            this.tiles = cachedTiles.get(tiles.getMoveCount() + 1);
            gamePanel.display(tiles);
        }
    }

    private Tiles copyTiles(boolean useCloning) {
        if (useCloning) {
            try {
                Tiles copy = (Tiles) tiles.clone();
                return copy;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Constructor < ? > ctor = tiles.getClass().getDeclaredConstructor(tiles.getClass());
                Tiles copy = (Tiles) ctor.newInstance(tiles);
                return copy;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            ConfigurationStore cs = new ConfigurationStore("https://bit.ly/2VnEGqS");
            GUIPuzzle gui = new GUIPuzzle(cs);
            gui.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}