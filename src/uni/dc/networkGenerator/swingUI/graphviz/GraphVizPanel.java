package uni.dc.networkGenerator.swingUI.graphviz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GraphVizPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JScrollPane 	scrollPane 	= new JScrollPane();
	private JLabel			imageLabel	= new JLabel("No GraphViz output generated");
	private StringBuilder 	dotString 	= null;
	private GraphViz		graphViz	= new GraphViz();
	
	public GraphVizPanel() {
		super(new BorderLayout());
		this.scrollPane = new JScrollPane(imageLabel);
		this.add(scrollPane,BorderLayout.CENTER);
		imageLabel.setMinimumSize(new Dimension(128, 128));
	}
	
	public void setDot(StringBuilder dotString){
		this.dotString = dotString;		
		updateImage();
	}
	
	private void updateImage(){
		try {
			Image img = graphViz.renderToImage(dotString);			
			imageLabel.setIcon(new ImageIcon(img));
			imageLabel.setText(null);
		} catch (IOException e){
			imageLabel.setText(e.getMessage());
			e.printStackTrace();
		}
		imageLabel.setSize(this.getSize());
		imageLabel.repaint();
		imageLabel.revalidate();
	}
}
