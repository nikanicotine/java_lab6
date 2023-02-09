package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class Form extends JDialog {

    private JPanel rootPanel;
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
        setContentPane(rootPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        createTable();

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                double limUp, limDown, step, result;
                String str_limUp, str_limDown, str_step;
                str_limUp = input1.getText();
                str_limDown = input2.getText();
                str_step = input3.getText();

                try {
                    limUp = Double.parseDouble(str_limUp);
                    limDown = Double.parseDouble(str_limDown);
                    step = Double.parseDouble(str_step);
                } catch (Exception e) {
                    ShowMsg("Введено некорректное значение");
                    return;
                }

//                boolean swapAB = false;
//                if (limUp > limDown) {
//                    swapAB = true;
//                    double tmp = limUp;
//                    limUp = limDown;
//                    limDown = tmp;
//                }
                double sumInt = CalcInt(limUp, limDown, step);
//
//                if (swapAB == true) {
//                    sumInt = -sumInt;
//                }
                String str_sumInt = String.valueOf(sumInt);

                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.addRow(new Object[]{str_limUp, str_limDown, str_step});
                Refresh();
                //
            }
        });

        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int SelectedRow = table1.getSelectedRow();
                int RowCount = table1.getRowCount();

                if (SelectedRow == -1) {
                    ShowMsg("Не выбрана строка в таблице ");
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.removeRow(SelectedRow);
                // поясни пж
                table1.setModel(model);
                if (SelectedRow == RowCount - 1) {
                    table1.changeSelection(SelectedRow - 1, 0, false, false);
                } else {
                    table1.changeSelection(SelectedRow, 0, false, false);
                }
                Refresh();
            }
        });

        calkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //heeeelp

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
        rootPanel.registerKeyboardAction(new ActionListener() {
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

    //    public JPanel getRootPanel(){
//        return rootPanel;
//    }
//
    private void createTable() {

        table1.setModel(new DefaultTableModel(
                null,
                new String[]{
                        "Верхняя граница интегрирования ", "Нижняя граница интегрирования",
                        "Шаг интегрирования", "Результат"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 3;
            }
        });
    }

    private double CalcInt(double limUp, double limDown, double step) {
        double i, value, sumInt;
        sumInt = 0;
        for (i = limUp; i < limDown; i += step) {
            value = (i + step) / 2;
            sumInt += Math.exp(-value) * step;
        }

        return sumInt;
    }


    private void ShowMsg(String s) {
        this.setVisible(true);
        JOptionPane.showMessageDialog(null, s);
        this.setVisible(true);
    }

    private void Refresh() {
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Form dialog = new Form();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
