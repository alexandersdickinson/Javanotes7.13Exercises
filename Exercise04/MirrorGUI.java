import javax.swing.JFrame;

public class MirrorGUI{
	
	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		MirrorText content = new MirrorText("This is a test.");
		frame.setContentPane(content);
		frame.pack();
		frame.setVisible(true);
		
	}
	
}