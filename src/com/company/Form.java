package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Form extends JDialog {

    private JPanel rootPanel;
    private JButton buttonOK;
    private JButton buttonCancel;

    private JButton addButton;
    private JButton delButton;
    private JButton calkButton;
    private JButton clearButton;
    private JButton fillButton;

    private JTextField input1;
    private JTextField input2;
    private JTextField input3;
    private JTable table1;
    private JButton saveBButton;
    private JButton saveTButton;
    private JButton loadTButton;
    private JButton loadBButton;

    class RecIntegral {
        String Upper, Lower, Step, Result;

        String getUpper() {
            return Upper;
        }

        String getLower() {
            return Lower;
        }

        String getStep() {
            return Step;
        }

        String getResult() {
            return Result;
        }

        void setUpper(String Temp) {
            this.Upper = Temp;
        }

        void setLower(String Temp) {
            this.Lower = Temp;
        }

        void setStep(String Temp) {
            this.Step = Temp;
        }

        void setResult(String Temp) {
            this.Result = Temp;
        }

        void setAll(String Temp1, String Temp2, String Temp3, String Temp4){
            this.setUpper(Temp1);
            this.setLower(Temp2);
            this.setStep(Temp3);
            this.setResult(Temp4);
        }
    }

    static class MyException extends Exception {
        String msg;

        MyException(String code) {
            msg = code;
        }
    }

    public double Calk(String Upper, String Lower, String Step) {
        double sum = 0;
        double limUp = Double.parseDouble(Upper);
        double limDown = Double.parseDouble(Lower);
        double limStep = Double.parseDouble(Step);
        while (limDown + limStep < limUp) {
            sum += ((Math.exp(-limDown) + Math.exp(-(limDown + limStep))) / 2) * limStep;
            limDown += limStep;
        }
        sum += ((Math.exp(-limDown) + Math.exp(-limUp)) / 2) * limStep;
        return sum;
    }

    List<RecIntegral> listA = new ArrayList();

    public Form() {
        setContentPane(rootPanel);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        createTable();

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                double limUp, limDown, step;
                String str_limUp, str_limDown, str_step;
                str_limUp = input1.getText();
                str_limDown = input2.getText();
                str_step = input3.getText();

                try {
                    limUp = Double.parseDouble(str_limUp);
                    limDown = Double.parseDouble(str_limDown);
                    step = Double.parseDouble(str_step);
                    if (limUp < 0.000001 || limUp > 100000)
                        throw new MyException("Неверное значение верхнего предела");

                    else if (limDown < 0.000001 || limDown > 100000)
                        throw new MyException("Неверное значение нижнего предела");
                    else if (limDown > limUp)
                        throw new MyException("Нижний предел должен быть меньше верхнего");
                    else if ((limUp - limDown) < step)
                        throw new MyException("Шаг должен быть меньше интервала интегрирования");

                } catch (MyException e) {
                    ShowMsg(e.msg);
                    return;
                } catch (Exception e) {
                    ShowMsg("Некорректно введены данные");
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                RecIntegral temp = new RecIntegral();
                temp.setUpper(str_limUp);
                temp.setLower(str_limDown);
                temp.setStep(str_step);

                double sum = Calk(str_limUp, str_limDown, str_step);
                temp.setResult(Double.toString(sum));
                model.addRow(new Object[]{model.getRowCount() + 1, str_limUp, str_limDown, str_step, sum});

                listA.add(temp);
                input1.setText("");
                input2.setText("");
                input3.setText("");
                UpdateWindow();
            }
        });

        delButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int SelectedRow;
                try {
                    SelectedRow = table1.getSelectedRow();
                    if (SelectedRow == -1)
                        throw new Exception();
                } catch (Exception e1) {
                    ShowMsg("Не выбрана строка в таблице ");
                    return;
                }
                int RowCount = table1.getRowCount();

                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                listA.remove(SelectedRow);
                model.removeRow(SelectedRow);
                table1.setModel(model);
                if (SelectedRow == RowCount - 1) {
                    table1.changeSelection(SelectedRow - 1, 0, false, false);
                } else {
                    table1.changeSelection(SelectedRow, 0, false, false);
                }
                UpdateWindow();
            }
        });

        calkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row;
                try {
                    row = table1.getSelectedRow();
                    if (row == -1)
                        throw new Exception();
                } catch (Exception e1) {
                    ShowMsg("Не выбрана строка в таблице ");
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                RecIntegral temp = listA.get(row);
                double sum = Calk(temp.getUpper(), temp.getLower(), temp.getStep());
                temp.setResult(Double.toString(sum));
                listA.set(row,temp);
                model.setValueAt(sum, row, 4);
                UpdateWindow();
            }
        });

        fillButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                RecIntegral temp;

                for (RecIntegral recIntegral : listA) {
                    temp = recIntegral;
                    model.addRow(new Object[]{model.getRowCount() + 1, recIntegral.getUpper(), recIntegral.getLower(), recIntegral.getStep(), recIntegral.getResult()});
                }
                listA.addAll(listA);
                UpdateWindow();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (table1.getRowCount() == 0)
                        throw new Exception();
                } catch (Exception e1) {
                    ShowMsg("Таблица пуста");
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                while (model.getRowCount() != 0) {
                    model.removeRow(0);
                }
                UpdateWindow();
            }
        });

        saveTButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Сохранение в текстовом виде");
                fc.showSaveDialog(null);
                File f = fc.getSelectedFile();

                try {
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    int row = model.getRowCount();
                    int col = model.getColumnCount();

                    FileWriter fw = new FileWriter(f);
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            fw.write(model.getValueAt(i, j).toString());
                            fw.write(" ");
                        }
                        fw.write("\n");
                    }
                    fw.close();
                    ShowMsg("Сохранение прошло успешно");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        saveBButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setDialogTitle("Сохранение в двоичном виде");
                fc.showSaveDialog(null);
            }
        });

        loadTButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(null);
                File f = fc.getSelectedFile();
                try {
                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
                    FileReader fr = new FileReader(f);
                    BufferedReader reader = new BufferedReader(fr);
                    String line;
                    String[] split;
                    RecIntegral temp = new RecIntegral();
                    listA.clear();
                    while((line = reader.readLine()) != null) {
                        split = line.split(" ");
                        model.addRow(new Object[]{model.getRowCount() + 1, split[1], split[2], split[3], split[4]});
                        temp.setAll(split[1], split[2], split[3], split[4]);
                        listA.add(temp);
                    }
                    reader.close();
                    fr.close();
                    ShowMsg("Загрузка прошла успешно");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        loadBButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
//                fc.showOpenDialog();
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

    private void createTable() {

        table1.setModel(new DefaultTableModel(
                null,
                new String[]{
                        "#", "Верхняя граница интегрирования", "Нижняя граница интегрирования",
                        "Шаг интегрирования", "Результат"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 4;
            }
        });
    }

    private void ShowMsg(String s) {
        this.setVisible(true);
        JOptionPane.showMessageDialog(null, s);
        this.setVisible(true);
    }

    private void UpdateWindow() {
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Form dialog = new Form();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
