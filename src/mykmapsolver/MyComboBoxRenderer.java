package mykmapsolver;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class MyComboBoxRenderer extends JComboBox implements TableCellRenderer
{
	public MyComboBoxRenderer(String[] items)
	{
		super(items);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (isSelected)
		{
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		}
		else
		{
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}

		// Select the current value
		setSelectedItem(value);
		return this;
	}
}

class MyComboBoxEditor extends DefaultCellEditor
{
	public MyComboBoxEditor(String[] items)
	{
		super(new JComboBox(items));
	}
}
