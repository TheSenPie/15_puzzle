import java.awt.Font;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private Tiles tiles = null;

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Paint the background white
        clearScreen("#C19A6B", g);

        if (tiles == null)
            return;
        // Making some space for counter
        int height = this.getHeight() - 20;

        // Take smaller side as a width and height for square
        float dimension = this.getWidth() > height ? height : this.getWidth();

        // Width and height of each cell
        float cellSize = dimension / ((float) Tiles.SIZE);


        g.setColor(java.awt.Color.BLACK);

        // Font to write digits in cells
        Font font = new Font("SansSerif", Font.PLAIN, (int)(cellSize * 0.3));
        g.setFont(font);


        // Drawing the grid
        for (int i = 0; i < Tiles.SIZE; i++) {
            for (int j = 0; j < Tiles.SIZE; j++) {
                if (tiles.getTile(i, j) == 0)
                    continue;
                g.drawRect((int)(cellSize * i + cellSize * 0.1), (int)(cellSize * j + cellSize * 0.1), (int)(cellSize * 0.8), (int)(cellSize * 0.8));

                g.drawString(tiles.getTile(i, j) + "", (int)(cellSize * i + (cellSize / 2 - font.getSize() / 2)), (int)(cellSize * j + font.getSize2D() + (cellSize / 2 - font.getSize() / 2)));
            }
        }

        // Showing the counter, message if solved, or error
        g.setFont(new Font("SansSerif", Font.PLAIN, (int)(cellSize * 0.1)));
        if (tiles.isSolved()) {
            g.drawString("You solved the puzzle in " + tiles.getMoveCount() + " moves!", (int)(cellSize * 0.1), (int)(this.getHeight() - cellSize * 0.1));
        } else if (!tiles.isSolvable()) {
            g.drawString("The game is not solvable. Try another.", (int)(cellSize * 0.1), (int)(this.getHeight() - cellSize * 0.1));
        } else {
            g.drawString("Moves: " + tiles.getMoveCount(), (int)(cellSize * 0.1), (int)(this.getHeight() - cellSize * 0.1));
        }
    }

    public void clearScreen(String hex, java.awt.Graphics g) {
        g.setColor(java.awt.Color.decode(hex));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void display(Tiles t) {
        tiles = t;
        repaint();
    }
}