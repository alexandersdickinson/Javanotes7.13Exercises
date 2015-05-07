/**
This program displays a table that users can use to create a phone directory.
*/

package PhoneDirectory;

import javax.swing.JFrame;

public class PhoneDirectoryGUI{

	public static void main(String[] args){
		
		JFrame window = new JFrame("Phone Directory");
		window.setResizable(false);
		PhoneDirectoryPanel content = new PhoneDirectoryPanel();
		window.setJMenuBar(content.getMenuBar());
		window.setContentPane(content);
		window.pack();
		window.setVisible(true);
		
	}

}