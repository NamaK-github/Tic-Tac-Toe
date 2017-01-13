import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by NamaK on 29.12.2016.
 */
public class Application extends JFrame {

    private Field field;

    public Application() {
        setTitle("X-O");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(600, 200, 505, 554);
        setResizable(false);
        setLayout(new BorderLayout());
        field = new Field(3);
        Options options = new Options(this);
        JPanel buttons = new JPanel(new GridLayout());
        JButton startButton = new JButton("Новая игра");
        JButton optionsButton = new JButton("Настройки");
        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        optionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                options.setVisible(true);
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.startGame();
            }
        });
        buttons.add(startButton);
        buttons.add(optionsButton);
        buttons.add(exitButton);
        add(field,BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void createMap(int lines, int symToWin, boolean aiOn){
        field.startGame(lines, symToWin, aiOn);
    }
}
