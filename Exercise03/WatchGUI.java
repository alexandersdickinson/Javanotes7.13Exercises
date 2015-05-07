import javax.swing.JFrame;

public class WatchGUI{
	
	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		StopWatchLabel content = new StopWatchLabel();
		frame.setContentPane(content);
		frame.pack();
		frame.setVisible(true);
		
	}
	
}