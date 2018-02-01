package spirit.fitness.scanner.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class LoadingFrameHelper extends JFrame{
	
	public JProgressBar loadingSample(String title) 
	{
			this.setTitle("Loading");
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setSize(200, 100);
		    Container content = this.getContentPane();
		    JProgressBar loading = new JProgressBar();
		    loading.setValue(25);
		    loading.setStringPainted(true);
		    Border border = BorderFactory.createTitledBorder(title);
		    loading.setBorder(border);
		    content.add(loading, BorderLayout.NORTH);
			this.setLocationRelativeTo(null);
		    this.setVisible(true);
		    this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
					setVisible(false);
				}
			});

		    return loading;
	}
}
