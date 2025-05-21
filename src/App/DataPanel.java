package App;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DataPanel extends JPanel {

    private final DefaultTableModel tableModel;

    public DataPanel() {
        String[] columnNames = {"Lat / Long", "Starting Coordinates", "Destination Coordinates"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(50);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getTableHeader().setBackground(Color.decode("#58a6f5"));
        table.getTableHeader().setForeground(Color.decode("#000000"));
        add(scrollPane);
    }

    public void addRow(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    public void removeRows() {
        tableModel.setRowCount(0);
    }
}
