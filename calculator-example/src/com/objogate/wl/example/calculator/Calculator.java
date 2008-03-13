package com.objogate.wl.example.calculator;

import static java.awt.Font.BOLD;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.objogate.exception.Defect;

public class Calculator extends JFrame {
    public static final String MAIN_WINDOW = "mainWindow";
    public static final String DISPLAY = "display";
    public static final String DIV_BUTTON = "div";
    public static final String MUL_BUTTON = "mul";
    public static final String EQUALS_BUTTON = "equals";
    public static final String SUB_BUTTON = "sub";
    public static final String ADD_BUTTON = "add";

    interface Function {
        int calc(int x, int y);
    }

    private static final Function ADD = new Function() {
        public int calc(int x, int y) {
            return x + y;
        }
    };

    private static final Function SUB = new Function() {
        public int calc(int x, int y) {
            return x - y;
        }
    };

    private static final Function MUL = new Function() {
        public int calc(int x, int y) {
            return x * y;
        }
    };

    private static final Function DIV = new Function() {
        public int calc(int x, int y) {
            return x / y;
        }
    };

    private final JTextField display;
    private int x;
    private Function func;

    
    public Calculator() {
        super("Calculator");
        display = new JTextField(12);
        display.setName(DISPLAY);
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(display.getFont().deriveFont(32f).deriveFont(BOLD));
        clearInput();
        
        JButton[] numberButtons = new JButton[10];
        for (int i = 0; i <= 9; i++) {
            numberButtons[i] = digitButton(i);
        }
        
        setLayout(new GridBagLayout());
        
        final Insets defaultInsets = new Insets(2,2,2,2);

        add(display, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            gridwidth = REMAINDER;
            insets = defaultInsets;
            fill = BOTH;
        }});

        for (int i = 0; i < 9; i++) {
            JButton button = numberButtons[9-i];
            final int row = 1 + i / 3;
            final int col = 2 - (i % 3);
            
            add(button, new GridBagConstraints() {{
                gridy = row;
                gridx = col;
                weightx = 1;
                weighty = 1;
                insets = defaultInsets;
                fill = BOTH;
            }});
        }
        add(numberButtons[0], new GridBagConstraints() {{
            gridx = 1;
            gridy = 4;
            weightx = 1;
            weighty = 1;
            insets = defaultInsets;
            fill = BOTH;
        }});

        add(functionButton("+", ADD_BUTTON, ADD), new GridBagConstraints() {{
            gridx = 3;
            gridy = 1;
            weightx = 1;
            weighty = 1;
            insets = defaultInsets;
            fill = BOTH;
        }});

        add(functionButton("-", SUB_BUTTON, SUB), new GridBagConstraints() {{
            gridx = 4;
            gridy = 1;
            weightx = 1;
            weighty = 1;
            insets = defaultInsets;
            fill = BOTH;
        }});

        add(functionButton("x", MUL_BUTTON, MUL), new GridBagConstraints() {{
            gridx = 3;
            gridy = 2;
            weightx = 1;
            weighty = 1;
            insets = defaultInsets;
            fill = BOTH;
        }});

        add(functionButton("\u00f7", DIV_BUTTON, DIV), new GridBagConstraints() {{
            gridx = 4;
            gridy = 2;
            weightx = 1;
            weighty = 1;
            insets = defaultInsets;
            fill = BOTH;
        }});

        JButton equalsButton = button("=", EQUALS_BUTTON);
        equalsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                equalsButtonClicked();
            }
        });
        add(equalsButton, new GridBagConstraints() {{
            gridx = 3;
            gridy = 3;
            gridwidth=2;
            gridheight=2;
            weightx = 1;
            weighty = 1;
            insets = defaultInsets;
            fill = BOTH;
        }});
        
        pack();
    }

    private JButton digitButton(final int digit) {
        JButton button = button(String.valueOf(digit), digitButtonName(digit));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                digitButtonClicked(digit);
            }
        });
        return button;
    }

    private void digitButtonClicked(int digit) {
        if (display.getText().equals("0")) {
            display.setText(String.valueOf(digit));
        }
        else {
            appendDigit(digit);
        }
    }

    private void appendDigit(int digit) {
        Document document = display.getDocument();
        try {
            document.insertString(document.getLength(), String.valueOf(digit), null);
        } catch (BadLocationException e) {
            throw new Defect("failed to append character to text", e);
        }
    }

    private JButton functionButton(String text, String name, final Function function) {
        JButton addButton = button(text, name);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                functionButtonClicked(function);
            }
        });
        return addButton;
    }

    private void functionButtonClicked(Function function) {
        x = parseInput();
        func = function;
        clearInput();
    }

    private void clearInput() {
        display.setText("0");
    }

    private int parseInput() {
        return Integer.parseInt(display.getText());
    }
    
    private void equalsButtonClicked() {
        int y = parseInput();
        x = func.calc(x, y);
        display.setText(String.valueOf(x));
        display.selectAll();
        func = null;
    }

    public static String digitButtonName(int i) {
        return "button" + i;
    }

    private JButton button(String text, String name) {
        JButton button = new JButton(text);
        button.setName(name);
        button.setMargin(null);
        return button;
    }

    public static void main(String... args) {
        Calculator calculator = new Calculator();
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        calculator.setName(MAIN_WINDOW);
        calculator.setVisible(true);
    }
}
