package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Form extends JDialog {

    private Object[][] array = new String[][] {{ "Сахар" , "кг", "1.5", "" },
            { "Мука"  , "кг", "4.0", "" },
            { "Молоко", "л" , "2.2", "" }};

    private Object[] columnsHeader = new String[] {"Верхняя граница интегрирования ", "граница интегрирования",
            "Шаг интегрирования", "Результат"};

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JButton addButton;
    private JButton delButton;
    private JButton calkButton;

    private JTextField input1;
    private JTextField input2;
    private JTextField input3;
    private JTable table1;

    public Form() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addButton.setVisible(false); //это я просто смотрю, че как работает!!!
                buttonOK.setBackground(Color.red);
                //table1.addColumn(); //?
                input1.setEditable(false); // гыг
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Form dialog = new Form();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
