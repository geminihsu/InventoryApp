package spirit.fitness.scanner.util;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class LoadingFrameHelper extends JFrame{
	
	public JProgressBar loadingSample(String title) 
	{
			this.setTitle("Loading");
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		    Container content = this.getContentPane();
		    JProgressBar loading = new JProgressBar();
		    loading.setValue(25);
		    loading.setStringPainted(true);
		    Border border = BorderFactory.createTitledBorder(title);
		    loading.setBorder(border);
		    content.add(loading, BorderLayout.NORTH);
		    this.setSize(500, 100);
		    this.setVisible(true);
		    
		    return loading;
	}
}
