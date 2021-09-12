import javax.swing.JFrame;

public class GameFrame extends JFrame{

	
	
	GameFrame(){
		
		this.add(new GamePanel()); // creates new instance of GamePanel class and adds it to the JFrame
		this.setTitle("Ussimäng");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack(); // fits the frame depending on the size of the panel inside it
		
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}

}
