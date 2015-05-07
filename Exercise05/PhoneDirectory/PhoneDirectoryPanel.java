/**
This class represents a table with a list of names and phone numbers.
*/

package PhoneDirectory;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.Vector;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PhoneDirectoryPanel extends JPanel implements ActionListener{
	
	private String[] columnNames = new String[] {"Name", "Number"};
	private JButton addButton;
	private JButton deleteButton;
	private JTextField nameField;
	private JTextField numField;
	private JTable directoryTable;
	private DefaultTableModel tableModel;
	private JFileChooser fileDialog;
	private static final int DEFAULT_ROW_COUNT = 10;//Minimum number of rows displayed in the table. The table can expand as needed.
	
	public PhoneDirectoryPanel(){
		
		setLayout(new BorderLayout());
		tableModel = new DefaultTableModel(columnNames, DEFAULT_ROW_COUNT);
		directoryTable = new JTable(tableModel);
		directoryTable.setGridColor(Color.BLACK);
		JScrollPane scroller = new JScrollPane(directoryTable);
		scroller.setPreferredSize(new Dimension(300, 300));
		add(scroller, BorderLayout.CENTER);
		
		//button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,2));
		
		addButton = new JButton("Add Contact");
		addButton.addActionListener(this);
		buttonPanel.add(addButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		buttonPanel.add(deleteButton);
		
		nameField = new JTextField(10);
		nameField.setText("Name");
		nameField.setActionCommand("nameField");
		nameField.addActionListener(this);
		nameField.addFocusListener(new FocusListener(){
			
			public void focusGained(FocusEvent evt){
				nameField.selectAll();
			}
			
			public void focusLost(FocusEvent evt){}
			
		});
		buttonPanel.add(nameField);
		
		numField = new JTextField(10);
		numField.setText("Number");
		numField.setActionCommand("numField");
		numField.addActionListener(this);
		numField.addFocusListener(new FocusListener(){
			
			public void focusGained(FocusEvent evt){
				numField.selectAll();
			}
			
			public void focusLost(FocusEvent evt){}
			
		});
		buttonPanel.add(numField);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	public void actionPerformed(ActionEvent evt){
		
		switch(evt.getActionCommand()){
			
			case "Open":
				doOpenFile();
				break;
			case "Save":
				doSaveFile();
				break;
			case "Delete":
				deleteContact();
				break;
			case "numField":
				nameField.requestFocus();
			default:
				addContact();
				nameField.selectAll();
				break;
			
		}
		
	}
	
	private void addContact(){
		
		if(directoryTable.getRowCount() <= DEFAULT_ROW_COUNT){
			
			for(int i = 0; i < DEFAULT_ROW_COUNT; i++){
				
				if(directoryTable.getValueAt(i,0) == null || directoryTable.getValueAt(i,0).equals("")){
					directoryTable.setValueAt(nameField.getText(),i,0);
					nameField.setText("Name");
					directoryTable.setValueAt(numField.getText(),i,1);
					numField.setText("Number");
					return;
				}
				else;
				
			}
			
			String[] contact = new String[2];
			contact[0] = nameField.getText();
			nameField.setText("Name");
			contact[1] = numField.getText();
			numField.setText("Number");
			tableModel.addRow(contact);
			
		}
		else{
			
			String[] contact = new String[2];
			contact[0] = nameField.getText();
			nameField.setText("Name");
			contact[1] = numField.getText();
			numField.setText("Number");
			tableModel.addRow(contact);
			
		}
		
	}
	
	private void deleteContact(){
		
		int[] deleteRows = directoryTable.getSelectedRows();
		
		for(int i = deleteRows.length - 1; i >= 0; i--){
			tableModel.removeRow(deleteRows[i]);
			if(directoryTable.getRowCount() < DEFAULT_ROW_COUNT)
				tableModel.addRow(new String[] {null, null});
		}
		
	}
	
	/**
		Saves the file in xml format.
	*/
	private void doSaveFile(){
		
		//write file in xml format.
		//create a file using a file dialog.
		if (directoryTable.getCellEditor() != null)
		      directoryTable.getCellEditor().stopCellEditing();
		
		if(fileDialog == null)
			fileDialog = new JFileChooser(System.getProperty("user.home") + "/documents");
		fileDialog.setDialogTitle("Save Phone Directory");
		int option = fileDialog.showSaveDialog(this);
		if (option != JFileChooser.APPROVE_OPTION)
		  return;  // User canceled or clicked the dialog's close box.
		File selectedFile = fileDialog.getSelectedFile();
		if (!selectedFile.toString().endsWith(".xml")){
			JOptionPane.showMessageDialog(null, "The chosen file needs to be in .xml format.");
		}
		if (selectedFile.exists()) {  // Ask the user whether to replace the file.
		  int response = JOptionPane.showConfirmDialog( null,
		        "The file \"" + selectedFile.getName()
		        + "\" already exists.\nDo you want to replace it?", 
		        "Confirm Save",
		        JOptionPane.YES_NO_OPTION, 
		        JOptionPane.WARNING_MESSAGE );
		  if (response != JOptionPane.YES_OPTION)
		     return;  // User does not want to replace the file.
		}
		
		PrintWriter out;
		try {
			out = new PrintWriter( new FileWriter(selectedFile) );
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null, "ERROR: Can't open data file for output.");
			return;
		}
		out.println("<?xml version=\"1.0\"?>");
		out.println("<phone_directory>");
		Vector tableVector = tableModel.getDataVector();
		Vector contactVector;
		for (int i = 0; i < tableVector.size(); i++) {
			contactVector = (Vector)tableVector.get(i);
			out.print("  <entry name='");
			out.print((String)contactVector.get(0));
			out.print("' number='");
			out.print((String)contactVector.get(1));
			out.println("'/>");
		}
		out.println("</phone_directory>");
		out.close();
		
		if (out.checkError())
			JOptionPane.showMessageDialog(null, "ERROR: Some error occurred while writing data file.");
		
	}
	
	private void doOpenFile(){
		
		if(fileDialog == null)
			fileDialog = new JFileChooser(System.getProperty("user.home") + "/documents");
		fileDialog.setDialogTitle("Open Phone Directory");
		FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter(null, "xml");
		fileDialog.setFileFilter(xmlFilter);
		int option = fileDialog.showOpenDialog(this);
		if(option != JFileChooser.APPROVE_OPTION)
			return;
		File selectedFile = fileDialog.getSelectedFile();
		Vector tableVector = tableModel.getDataVector();
		tableVector.clear();
		
		try {
		    DocumentBuilder docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    Document xmldoc = docReader.parse(selectedFile);
		    Element root = xmldoc.getDocumentElement();
		    if (! root.getTagName().equals("phone_directory"))
		       throw new Exception();
		    NodeList nodes = root.getChildNodes();
		    for (int i = 0; i < nodes.getLength(); i++) {
		       if ( nodes.item(i) instanceof Element ) {
		          Element entry = (Element)nodes.item(i);
		          if (! entry.getTagName().equals("entry"))
		             throw new Exception();
		          String entryName = entry.getAttribute("name");
		          String entryNumber = entry.getAttribute("number");
				  Vector<String> contact = new Vector<String>();
				  if(entryName.equals("null"))
					  contact.add(null);
				  else
					  contact.add(entryName);
				  if(entryNumber.equals("null"))
					  contact.add(null);
				  else
					  contact.add(entryNumber);
				  tableVector.add(contact);
		       }
		    }
		 }
         catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred in opening the phone directory file.");
         }
		
	}
	
	public JMenuBar getMenuBar(){
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		int shortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		KeyStroke openStroke = KeyStroke.getKeyStroke(KeyEvent.VK_O, shortcutMask);
		open.setAccelerator(openStroke);
		open.addActionListener(this);
		KeyStroke saveStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcutMask);
		save.setAccelerator(saveStroke);
		save.addActionListener(this);
		fileMenu.add(open);
		fileMenu.addSeparator();
		fileMenu.add(save);
		menuBar.add(fileMenu);
		return menuBar;
		
	}
	
}