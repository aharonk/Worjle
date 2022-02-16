package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class WordleGUI {
    private int currentGuessRow = 0, currentGuessPos = 0;
    private final WordleModel model = new WordleModel();
    private JPanel keyboard = new JPanel();
    private JPanel gameArea = new JPanel(new GridLayout(6, 5));
    private Window w;

    private final Border border = BorderFactory.createLineBorder(Color.WHITE, 2);
    private JLabel[][] printArea = new JLabel[6][5];
    private final HashMap<String, JButton> buttons = new HashMap<>();

    public WordleGUI() {
        w = new Window();
    }

    public void keyboard() {
        KeyboardListener kl = new KeyboardListener();

        keyboard.setLayout(new GridLayout(3, 1));
        JPanel row1 = new JPanel();
        row1.setLayout(new RelativeLayout(BoxLayout.X_AXIS));
        JPanel row2 = new JPanel();
        row2.setLayout(new RelativeLayout(BoxLayout.X_AXIS));
        JPanel row3 = new JPanel();
        row3.setLayout(new RelativeLayout(BoxLayout.X_AXIS));

        String[] letters = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
        for (String s : letters) {
            JButton b = new JButton(s);
            b.addActionListener(kl);
            buttons.put(s, b);
            row1.add(b, 2f);
        }

        JPanel begin2 = new JPanel();
        row2.add(begin2, 1f);

        letters = new String[]{"A", "S", "D", "F", "G", "H", "J", "K", "L"};
        for (String s : letters) {
            JButton b = new JButton(s);
            b.addActionListener(kl);
            buttons.put(s, b);
            row2.add(b, 2f);
        }

        JPanel end2 = new JPanel();
        row2.add(end2, 1f);
        JButton b1 = new JButton("Enter");
        b1.addActionListener(kl);
        row3.add(b1, 3f);

        letters = new String[]{"Z", "X", "C", "V", "B", "N", "M"};
        for (String s : letters) {
            JButton b = new JButton(s);
            b.addActionListener(kl);
            buttons.put(s, b);
            row3.add(b, 2f);
        }

        JButton b2 = new JButton("\u2190");
        b2.addActionListener(kl);
        row3.add(b2, 3f);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
    }

    private void gameArea() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                JLabel jl = new JLabel();
                jl.setOpaque(true);
                jl.setHorizontalAlignment(JLabel.CENTER);
                jl.setVerticalAlignment(JLabel.CENTER);
                jl.setBorder(border);
                jl.setFont(new Font("Arial", Font.PLAIN, 42));
                gameArea.add(printArea[i][j] = jl);
            }
        }
    }

    private void submit() {
        if (currentGuessPos < 5) {
            JOptionPane.showMessageDialog(gameArea, "Your word is too short!", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder guess = new StringBuilder();
        for (int i = 0; i < printArea[currentGuessRow].length; i++) {
            guess.append(printArea[currentGuessRow][i].getText());
        }

        Response[] response = model.submit(guess.toString());
        if (response == null) {
            JOptionPane.showMessageDialog(gameArea, "Your word is invalid!", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < response.length; i++) {
            setCorrectBackground(printArea[currentGuessRow][i], response[i]);
            setCorrectBackground(buttons.get(printArea[currentGuessRow][i].getText()), response[i]);
        }

        if (response[0] == Response.PLACE && new HashSet<>(Arrays.asList(response)).size() == 1) {
            end('w');
        }

        currentGuessRow++;
        currentGuessPos = 0;

        if (currentGuessRow > 5) {
            end('l');
        }
    }

    private void setCorrectBackground(Component c, Response r) {
        Color current = c.getBackground();
        if (r.equals(Response.PLACE))
            c.setBackground(r.getColor());
        else if (r.equals(Response.LETTER) && !current.equals(Response.GREEN))
            c.setBackground(r.getColor());
        else if (!current.equals(Response.GREEN) && !current.equals(Response.YELLOW))
            c.setBackground(r.getColor());
    }

    private void end(char type) {
        Object[] options = {"Yes", "No"};
        reset(JOptionPane.showOptionDialog(gameArea, "Would you like to play again?",
                type == 'w' ? "Congratulations!" : "Unfortunate.", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]));
    }

    private void reset(int n) {
        if (n == JOptionPane.NO_OPTION) {
            System.exit(0);
        }

        if (n == JOptionPane.YES_OPTION) {
            w.dispose();
            currentGuessRow = 0;
            currentGuessPos = 0;
            keyboard = new JPanel();
            gameArea = new JPanel(new GridLayout(6, 5));
            printArea = new JLabel[6][5];
            w = new Window();
        }
    }

    private class Window extends JFrame {
        public Window() {
            setTitle("Wordle Clone");
            setSize(475, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            // set up keyboard
            keyboard();
            getContentPane().add(keyboard, BorderLayout.SOUTH);
            getContentPane().setBackground(Color.WHITE);

            // set up game area
            gameArea();

            JPanel main = new JPanel(new GridBagLayout());
            main.setBackground(Color.WHITE);
            main.setOpaque(true);
            gameArea.setOpaque(true);
            main.add(gameArea);

            getContentPane().add(new JPanel(), BorderLayout.WEST);
            getContentPane().add(new JPanel(), BorderLayout.EAST);
            getContentPane().add(new JPanel(), BorderLayout.NORTH);
            getContentPane().add(main, BorderLayout.CENTER);

            setVisible(true);
        }
    }

    private class KeyboardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonPressed = ((JButton) e.getSource()).getText();

            switch (buttonPressed) {
                case "\u2190":
                    currentGuessPos = Math.max(currentGuessPos - 1, 0);
                    printArea[currentGuessRow][currentGuessPos].setText("");
                    break;
                case "Enter":
                    submit();
                    break;
                default:
                    if (currentGuessPos < 5)
                        printArea[currentGuessRow][currentGuessPos++].setText(buttonPressed);
                    break;
            }
        }
    }
}
