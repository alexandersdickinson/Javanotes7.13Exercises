import javax.swing.JLabel;
import java.awt.*;

public class MirrorText extends JLabel{
	
	private String text;

	public MirrorText(String text){
		
		super(text);
		
	}
	
	public void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D)g;
		g2.translate(getWidth(), 0);
		g2.scale(-1,1);
		super.paintComponent(g2);
		
	}

}