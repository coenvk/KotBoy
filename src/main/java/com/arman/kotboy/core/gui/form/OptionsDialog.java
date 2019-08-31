package com.arman.kotboy.core.gui.form;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OptionsDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonApply;
    private JButton buttonDefault;
    private JPanel tabSystem;
    private JPanel tabGraphics;
    private JPanel tabJoypad;
    private JTextField textFieldDmgBorder;
    private JButton buttonDmgBorder;
    private JTextField textFieldGbcBorder;
    private JButton buttonGbcBorder;
    private JTextField textFieldSgbBorder;
    private JButton buttonSgbBorder;
    private JCheckBox checkBoxBootrom;

    private JFileChooser fileChooser;

    public OptionsDialog() {
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);

        fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setDialogTitle("Select border file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap files", "bmp"));

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        buttonApply.addActionListener(e -> onApply());
        buttonDefault.addActionListener(e -> onDefault());

        buttonDmgBorder.addActionListener(e -> selectDmgBorder());
        buttonGbcBorder.addActionListener(e -> selectGbcBorder());
        buttonSgbBorder.addActionListener(e -> selectSgbBorder());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        OptionsDialog dialog = new OptionsDialog();
        dialog.pack();
        dialog.setVisible(true);
//        System.exit(0);
    }

    private void onOK() {
        onApply();
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void onApply() {
    }

    private void onDefault() {
    }

    private void selectDmgBorder() {
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            textFieldDmgBorder.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void selectGbcBorder() {
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            textFieldGbcBorder.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void selectSgbBorder() {
        int ret = fileChooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            textFieldSgbBorder.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

}
