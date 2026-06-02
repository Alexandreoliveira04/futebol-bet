package br.com.futebolbet.ui.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UiTheme {

    public static final Color BG_PRIMARY      = new Color(0x00, 0x0b, 0x18);
    public static final Color BG_CARD         = new Color(0x00, 0x14, 0x28);
    public static final Color BG_CARD_ALT     = new Color(0x00, 0x1c, 0x35);
    public static final Color BG_PILL_INACTIVE = new Color(0x00, 0x1a, 0x30);
    public static final Color ACCENT_GREEN    = new Color(0x00, 0xd1, 0x00);
    public static final Color ACCENT_BLUE     = new Color(0x00, 0x84, 0xff);
    public static final Color ACCENT_BORDER   = new Color(0x00, 0x33, 0x55);
    public static final Color FG_PRIMARY      = Color.WHITE;
    public static final Color FG_MUTED        = new Color(0xb0, 0xb8, 0xc4);
    public static final Color FG_ON_GREEN     = Color.WHITE;
    public static final Color FG_SUCCESS      = new Color(0x00, 0xe0, 0x60);
    public static final Color FG_ERROR        = new Color(0xff, 0x55, 0x55);
    public static final Color FG_WARNING      = new Color(0xff, 0xcc, 0x00);

    private static final Font FONT_BASE  = new Font(Font.DIALOG, Font.PLAIN, 13);
    private static final Font FONT_BOLD  = new Font(Font.DIALOG, Font.BOLD,  13);
    private static final Font FONT_TITLE = new Font(Font.DIALOG, Font.BOLD,  16);
    private static final Font FONT_SUB   = new Font(Font.DIALOG, Font.PLAIN, 12);

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
                BorderFactory.createLineBorder(ACCENT_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)));
    }

    public static Border tabBarBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BORDER, 1),
                new EmptyBorder(10, 14, 10, 14));
    }

    public static void stylePill(JButton button, boolean selected) {
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(FONT_BOLD.deriveFont(12f));
        button.setUI(new BasicButtonUI());

        int padH = 20;
        int padV = 9;

        if (selected) {
            button.setBackground(ACCENT_GREEN);
            button.setForeground(FG_ON_GREEN);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_GREEN.darker(), 1),
                    new EmptyBorder(padV, padH, padV, padH)));
        } else {
            button.setBackground(BG_PILL_INACTIVE);
            button.setForeground(FG_MUTED);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_BORDER, 1),
                    new EmptyBorder(padV, padH, padV, padH)));
        }
    }

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(ACCENT_GREEN);
        button.setForeground(FG_ON_GREEN);
        button.setFont(FONT_BOLD);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_GREEN.darker(), 1),
                new EmptyBorder(9, 20, 9, 20)));
        applyButtonHover(button, ACCENT_GREEN, ACCENT_GREEN.darker());
    }

    public static void styleSecondaryButton(JButton button) {
        button.setBackground(BG_PILL_INACTIVE);
        button.setForeground(FG_PRIMARY);
        button.setFont(FONT_BOLD);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                new EmptyBorder(9, 20, 9, 20)));
        applyButtonHover(button, BG_PILL_INACTIVE, BG_CARD_ALT);
    }

    private static void applyButtonHover(JButton button, Color normal, Color hover) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normal);
            }
        });
    }

    public static void styleLabel(JLabel label, boolean muted) {
        label.setForeground(muted ? FG_MUTED : FG_PRIMARY);
        label.setFont(FONT_BASE);
    }

    public static void styleTitleLabel(JLabel label) {
        label.setForeground(FG_PRIMARY);
        label.setFont(FONT_TITLE);
    }

    public static void styleStatusLabel(JLabel label) {
        label.setFont(FONT_SUB);
        label.setForeground(FG_MUTED);
    }

    public static void setLabelSuccess(JLabel label, String text) {
        label.setText(text);
        label.setForeground(FG_SUCCESS);
    }

    public static void setLabelError(JLabel label, String text) {
        label.setText(text);
        label.setForeground(FG_ERROR);
    }

    public static void setLabelNeutral(JLabel label, String text) {
        label.setText(text);
        label.setForeground(FG_MUTED);
    }

    public static void styleTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(FG_PRIMARY);
        table.setFont(FONT_BASE);
        table.setGridColor(ACCENT_BORDER);
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.getTableHeader().setBackground(BG_PILL_INACTIVE);
        table.getTableHeader().setForeground(FG_PRIMARY);
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(FG_PRIMARY);
        table.setDefaultRenderer(Object.class, new StripedTableCellRenderer());
    }

    public static void styleCombo(JComboBox<?> combo) {
        combo.setBackground(BG_CARD);
        combo.setForeground(FG_PRIMARY);
        combo.setFont(FONT_BASE);
        combo.setBorder(BorderFactory.createLineBorder(ACCENT_BORDER, 1));
    }

    public static void styleTextField(JTextField field) {
        field.setBackground(BG_CARD);
        field.setForeground(FG_PRIMARY);
        field.setFont(FONT_BASE);
        field.setCaretColor(FG_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
    }

    public static void stylePasswordField(JPasswordField field) {
        field.setBackground(BG_CARD);
        field.setForeground(FG_PRIMARY);
        field.setFont(FONT_BASE);
        field.setCaretColor(FG_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)));
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setBackground(BG_CARD);
        spinner.setForeground(FG_PRIMARY);
        spinner.setFont(FONT_BASE);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(BG_CARD);
            tf.setForeground(FG_PRIMARY);
            tf.setFont(FONT_BASE);
            tf.setCaretColor(FG_PRIMARY);
        }
        spinner.setBorder(BorderFactory.createLineBorder(ACCENT_BORDER, 1));
    }

    public static void styleScrollPane(JScrollPane scroll) {
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT_BORDER, 1));
        scroll.getVerticalScrollBar().setBackground(BG_CARD);
        scroll.getHorizontalScrollBar().setBackground(BG_CARD);
    }

    public static JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(ACCENT_BORDER);
        sep.setBackground(BG_PRIMARY);
        return sep;
    }

    public static JPanel createSectionHeader(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PRIMARY);
        header.setBorder(new EmptyBorder(0, 0, 8, 0));

        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(FG_PRIMARY);
        header.add(lbl, BorderLayout.WEST);

        JPanel line = new JPanel();
        line.setBackground(ACCENT_GREEN);
        line.setPreferredSize(new Dimension(0, 2));
        header.add(line, BorderLayout.SOUTH);

        return header;
    }

    public static void applyDarkOptionPaneDefaults() {
        UIManager.put("OptionPane.background", BG_PRIMARY);
        UIManager.put("Panel.background", BG_PRIMARY);
        UIManager.put("Label.foreground", FG_PRIMARY);
        UIManager.put("Button.background", BG_PILL_INACTIVE);
        UIManager.put("Button.foreground", FG_PRIMARY);
    }

    private static final class StripedTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? BG_CARD : BG_CARD_ALT);
                c.setForeground(FG_PRIMARY);
            }
            setBorder(new EmptyBorder(0, 10, 0, 10));
            return c;
        }
    }
}
