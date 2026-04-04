package br.com.futebolbet.ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public final class UiTheme {

    public static final Color BG_PRIMARY = new Color(0x00, 0x0b, 0x18);
    public static final Color BG_CARD = new Color(0x00, 0x14, 0x28);
    public static final Color BG_PILL_INACTIVE = new Color(0x00, 0x1a, 0x30);
    public static final Color ACCENT_GREEN = new Color(0x00, 0xd1, 0x00);
    public static final Color ACCENT_BLUE = new Color(0x00, 0x84, 0xff);
    public static final Color FG_PRIMARY = Color.WHITE;
    public static final Color FG_MUTED = new Color(0xb0, 0xb8, 0xc4);
    public static final Color FG_ON_GREEN = Color.WHITE;

    private UiTheme() {
    }

    public static void applyRoot(JFrame frame) {
        frame.getContentPane().setBackground(BG_PRIMARY);
        frame.getRootPane().setBackground(BG_PRIMARY);
    }

    public static void applyPanel(JPanel panel) {
        panel.setBackground(BG_PRIMARY);
        panel.setForeground(FG_PRIMARY);
    }

    public static void applyPanelCard(JPanel panel) {
        panel.setBackground(BG_CARD);
        panel.setForeground(FG_PRIMARY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x00, 0x2a, 0x45), 1),
                new EmptyBorder(12, 12, 12, 12)));
    }

    public static Border tabBarBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x00, 0x2a, 0x45), 1),
                new EmptyBorder(10, 12, 10, 12));
    }

    public static void stylePill(JButton button, boolean selected) {
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
        button.setUI(new BasicButtonUI());
        int padH = 18;
        int padV = 10;
        button.setMargin(new Insets(padV, padH, padV, padH));

        if (selected) {
            button.setBackground(ACCENT_GREEN);
            button.setForeground(FG_ON_GREEN);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_GREEN.darker(), 1),
                    new EmptyBorder(padV - 2, padH - 2, padV - 2, padH - 2)));
        } else {
            button.setBackground(BG_PILL_INACTIVE);
            button.setForeground(FG_MUTED);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0x00, 0x33, 0x55), 1),
                    new EmptyBorder(padV - 2, padH - 2, padV - 2, padH - 2)));
        }
    }

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_GREEN);
        button.setForeground(FG_ON_GREEN);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN.darker(), 1),
                new EmptyBorder(8, 16, 8, 16)));
    }

    public static void styleSecondaryButton(JButton button) {
        button.setBackground(BG_PILL_INACTIVE);
        button.setForeground(FG_PRIMARY);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                new EmptyBorder(8, 16, 8, 16)));
    }

    public static void styleLabel(JLabel label, boolean muted) {
        label.setForeground(muted ? FG_MUTED : FG_PRIMARY);
        label.setBackground(BG_PRIMARY);
    }

    public static void styleTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(FG_PRIMARY);
        table.setGridColor(new Color(0x00, 0x33, 0x55));
        table.getTableHeader().setBackground(BG_PILL_INACTIVE);
        table.getTableHeader().setForeground(FG_PRIMARY);
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(FG_PRIMARY);
    }

    public static void styleCombo(JComboBox<?> combo) {
        combo.setBackground(BG_CARD);
        combo.setForeground(FG_PRIMARY);
    }

    public static void styleTextField(JTextField field) {
        field.setBackground(BG_CARD);
        field.setForeground(FG_PRIMARY);
        field.setCaretColor(FG_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x00, 0x33, 0x55), 1),
                new EmptyBorder(8, 10, 8, 10)));
    }

    public static void stylePasswordField(JPasswordField field) {
        field.setBackground(BG_CARD);
        field.setForeground(FG_PRIMARY);
        field.setCaretColor(FG_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x00, 0x33, 0x55), 1),
                new EmptyBorder(8, 10, 8, 10)));
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setBackground(BG_CARD);
        spinner.setForeground(FG_PRIMARY);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(BG_CARD);
            tf.setForeground(FG_PRIMARY);
            tf.setCaretColor(FG_PRIMARY);
        }
    }

    public static void styleScrollPane(JScrollPane scroll) {
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x00, 0x33, 0x55)));
    }

    public static void applyDarkOptionPaneDefaults() {
        UIManager.put("OptionPane.background", BG_PRIMARY);
        UIManager.put("Panel.background", BG_PRIMARY);
        UIManager.put("Label.foreground", FG_PRIMARY);
    }
}
