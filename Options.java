import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by NamaK on 08.01.2017.
 */
public class Options extends JFrame {

    private Application owner;

    public Options(Application a) {
        owner = a;
        setTitle("Настройки");
        setBounds(700, 300, 300, 200);
        setResizable(false);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JLabel label = new JLabel("  Игроки:");
        label.setFont(new Font("Arial", Font.BOLD, 15));
        add(label);
        ButtonGroup bg = new ButtonGroup();
        JRadioButton rb1 = new JRadioButton("Игрок против компьютера");
        JRadioButton rb2 = new JRadioButton("Игрок против игрока");
        rb1.setSelected(true);
        bg.add(rb1);
        bg.add(rb2);
        add(rb1);
        add(rb2);
        JLabel mapSizeLabel = new JLabel("Размер поля: 3");
        add(mapSizeLabel);
        JSlider slider = new JSlider(3, 10);
        JSlider sliderWin = new JSlider(3, 3);
        slider.setValue(3);
        add(slider);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                mapSizeLabel.setText("Размер поля: " + slider.getValue());
                sliderWin.setMaximum(slider.getValue());
            }
        });
        JLabel symToWin = new JLabel("Кол-во для выигрыша: 3");
        add(symToWin);
        sliderWin.setValue(3);
        add(sliderWin);
        sliderWin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                symToWin.setText("Кол-во для выигрыша: " + sliderWin.getValue());
            }
        });
        JButton button = new JButton("OK");
        add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean aiOn;
                if (rb1.isSelected()){
                    aiOn = true;
                } else {
                    aiOn = false;
                }
                owner.createMap(slider.getValue(), sliderWin.getValue(), aiOn);
                setVisible(false);
            }
        });
    }
}
