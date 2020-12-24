import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    public Game() {
        initUI();
    }

    private void initUI() {
        add(new Level());
        pack();
        setTitle("ooh");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Game hi = new Game();
            hi.setVisible(true);
        });
    }
}
