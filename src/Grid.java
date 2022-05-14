import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Grid extends JPanel{
	static final Color[] colors = new Color[]
	        {Color.BLACK, Color.GRAY, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.MAGENTA, Color.RED};
	static final String[] colorTexts = new String[]
	        {"BLACK", "GRAY", "BLUE", "#00FFFF", "GREEN", "YELLOW", "#FFA500", "#FFC0CB", "#FF00FF", "RED"};
	static final Color BACKGROUND = Color.WHITE;
	static final String Slash = "BLACK";
	
	int ROWS;
	int COLS;
	
	public<T extends Number> Grid(int rows, int cols, String[] corner, String[] horizontal, String[] vertical, T[][] hotSpotMatrix, T Max)
	{
		this.ROWS = rows + 1;
		this.COLS = cols + 1;
		
		double cooking = Max.doubleValue()/10;
		double hotLevel;
		
		/*if(cooking < 10)
		{
			hotLevel = 1;
		}
		else
		{
			hotLevel = cooking/10;
		}*/
		
		hotLevel = cooking/10;
		cooking -= hotLevel;
		
		JLabel[][] label = new JLabel[ROWS][COLS];
		
		setLayout(new GridLayout(ROWS,COLS));
		
		GridBagConstraints gc = new GridBagConstraints();
	    gc.weightx = 1d;
	    gc.weighty = 1d;
	    gc.insets = new Insets(0, 0, 1, 1);
	    gc.fill = GridBagConstraints.BOTH;

	    // fill the whole panel with labels
	    for( int r=0 ; r<ROWS ; r++) {
	        for( int c=0 ; c<COLS ; c++) {
	            JLabel l = new JLabel();
	            l.setOpaque(true);
	            //l.setForeground(BACKGROUND);
	            l.setBackground(BACKGROUND);
	            gc.gridx = r;
	            gc.gridy = c;
	            add(l, gc);
	            label[r][c] = l;
	        }
	    }
	    
	    BufferedImage bi = new BufferedImage(77, 77,  
                BufferedImage.TYPE_INT_RGB); 
        Graphics2D g = bi.createGraphics();
        g.setBackground(BACKGROUND);
        g.clearRect(0, 0, 77, 77);
        
        g.setColor(Color.BLACK);
        BasicStroke bs = new BasicStroke(1); 
        g.setStroke(bs);    
        g.drawLine(18, 25, 66, 59);
        g.drawString(corner[1], 40, 40);
        g.drawString(corner[0], 16, 58);
        ImageIcon icon = new ImageIcon(bi); 
        label[0][0].setIcon(icon);
        
        //g.dispose();
        //bi.flush();
	    
	    //label[0][0].setText("<html>" + "\\" + corner[1] + 
	    //		"<br />" + 
	    //		corner[0] + "</html>");
        for(int i = 1;i < ROWS;i++)
	    {
	    	label[i][0].setText(horizontal[i-1]);
	    	label[i][0].setForeground(Color.DARK_GRAY);
	    	label[i][0].setVerticalTextPosition(SwingConstants.CENTER);
    		label[i][0].setHorizontalTextPosition(SwingConstants.CENTER);
    		label[i][0].setHorizontalAlignment(SwingConstants.CENTER);
    		label[i][0].setVerticalAlignment(SwingConstants.CENTER);
	    }
	    for(int i = 1;i < COLS;i++)
	    {
	    	label[0][i].setText(vertical[i-1]);
	    	label[0][i].setForeground(Color.DARK_GRAY);
	    	label[0][i].setVerticalTextPosition(SwingConstants.CENTER);
    		label[0][i].setHorizontalTextPosition(SwingConstants.CENTER);
    		label[0][i].setHorizontalAlignment(SwingConstants.CENTER);
    		label[0][i].setVerticalAlignment(SwingConstants.CENTER);
	    }
	    
	    //set colors
	    for(int i = 1;i < ROWS;i++)
	    {
	    	for(int j = 1;j < COLS;j++)
	    	{
	    		if(hotSpotMatrix[i-1][j-1].doubleValue() > cooking)
	    		{
	    			label[i][j].setForeground(colors[colors.length - 1]);
	    		}
	    		else
	    		{
	    			label[i][j].setForeground(colors[(int) (hotSpotMatrix[i-1][j-1].doubleValue()/hotLevel)]);
	    		}
	    		
	    		if(hotSpotMatrix[i-1][j-1].doubleValue() >= hotLevel * 7)
	    		{
	    			label[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
	    		}
	    		
	    		if(hotSpotMatrix[i-1][j-1] instanceof Integer)
	    		{
	    			label[i][j].setText(Integer.toString(hotSpotMatrix[i-1][j-1].intValue()));
	    		}
	    		else
	    		{
	    			DecimalFormat twoDForm = new DecimalFormat("#.##");
	    			label[i][j].setText(twoDForm.format(hotSpotMatrix[i-1][j-1]));
	    		}
	    		
	    		label[i][j].setVerticalTextPosition(SwingConstants.CENTER);
	    		label[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
	    		label[i][j].setHorizontalAlignment(SwingConstants.CENTER);
	    		label[i][j].setVerticalAlignment(SwingConstants.CENTER);
	    	}
	    }
	}
	
	public<T extends Number> Grid(int rows, int cols, String[] corner, String[] horizontal, String[] vertical, T[][] hotSpotMatrix, T[][] comparisonMatrix, T Max)
	{
		this.ROWS = rows + 1;
		this.COLS = cols + 1;
		
		double cooking = Max.doubleValue()/10;
		double hotLevel;
		
		/*if(cooking < 10)
		{
			hotLevel = 1;
		}
		else
		{
			hotLevel = cooking/10;
		}*/
		
		hotLevel = cooking/10;
		cooking -= hotLevel;
		
		JLabel[][] label = new JLabel[ROWS][COLS];
		
		setLayout(new GridLayout(ROWS,COLS));
		
		GridBagConstraints gc = new GridBagConstraints();
	    gc.weightx = 1d;
	    gc.weighty = 1d;
	    gc.insets = new Insets(0, 0, 1, 1);
	    gc.fill = GridBagConstraints.BOTH;
		
		// fill the whole panel with labels
	    for( int r=0 ; r<ROWS ; r++) {
	        for( int c=0 ; c<COLS ; c++) {
	        	JLabel l = new JLabel();
	            l.setOpaque(true);
	            //l.setForeground(BACKGROUND);
	            l.setBackground(BACKGROUND);
	            gc.gridx = r;
	            gc.gridy = c;
	            add(l, gc);
	            label[r][c] = l;
	        }
	    }
		
	    BufferedImage bi = new BufferedImage(77, 77,  
                BufferedImage.TYPE_INT_RGB); 
        Graphics2D g = bi.createGraphics();
        g.setBackground(BACKGROUND);
        g.clearRect(0, 0, 77, 77);
        
        g.setColor(Color.BLACK);
        BasicStroke bs = new BasicStroke(1); 
        g.setStroke(bs);    
        g.drawLine(18, 25, 66, 59);
        g.drawString(corner[1], 40, 40);
        g.drawString(corner[0], 16, 58);
        ImageIcon icon = new ImageIcon(bi); 
        label[0][0].setIcon(icon);
        
        //g.dispose();
        //bi.flush();
	    
	    //label[0][0].setText("<html>" + "\\" + corner[1] + 
	    //		"<br />" + 
	    //		corner[0] + "</html>");
	    for(int i = 1;i < ROWS;i++)
	    {
	    	label[i][0].setText(horizontal[i-1]);
	    	label[i][0].setForeground(Color.DARK_GRAY);
	    	label[i][0].setVerticalTextPosition(SwingConstants.CENTER);
    		label[i][0].setHorizontalTextPosition(SwingConstants.CENTER);
    		label[i][0].setHorizontalAlignment(SwingConstants.CENTER);
    		label[i][0].setVerticalAlignment(SwingConstants.CENTER);
	    }
	    for(int i = 1;i < COLS;i++)
	    {
	    	label[0][i].setText(vertical[i-1]);
	    	label[0][i].setForeground(Color.DARK_GRAY);
	    	label[0][i].setVerticalTextPosition(SwingConstants.CENTER);
    		label[0][i].setHorizontalTextPosition(SwingConstants.CENTER);
    		label[0][i].setHorizontalAlignment(SwingConstants.CENTER);
    		label[0][i].setVerticalAlignment(SwingConstants.CENTER);
	    }

	    // fill the whole panel with labels
	    for( int i=1 ; i<ROWS ; i++) {
	        for( int j=1 ; j<COLS ; j++) {
	        	
	        	if(hotSpotMatrix[i-1][j-1].doubleValue() >= hotLevel * 7 || 
	        			comparisonMatrix[i-1][j-1].doubleValue() >= hotLevel * 7)
	    		{
	    			label[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
	    		}
	        	
	        	int hotSpotColor = (int) (hotSpotMatrix[i-1][j-1].doubleValue()/hotLevel);
	    		int comparisonColor = (int) (comparisonMatrix[i-1][j-1].doubleValue()/hotLevel);
	        	
	    		if(hotSpotColor > colors.length - 1)
	    		{
	    			hotSpotColor = colors.length - 1;
	    		}
	    		
	    		if(comparisonColor > colors.length - 1)
	    		{
	    			comparisonColor = colors.length - 1;
	    		}
	    		
	    		String labelText;
	    		
	    		if(hotSpotMatrix[i-1][j-1] instanceof Integer)
	    		{
	    			labelText = "<html><font color=" + colorTexts[hotSpotColor] + ">" + hotSpotMatrix[i-1][j-1] + "</font> " +
	    					"<font color=" + Slash + ">/</font> <br />" +
	    					"<font color=" + colorTexts[comparisonColor] + ">" + comparisonMatrix[i-1][j-1] + "</font></html>";
	    		}
	    		else
	    		{
	    			DecimalFormat twoDForm = new DecimalFormat("#.##");
	    			labelText = "<html><font color=" + colorTexts[hotSpotColor] + ">" + twoDForm.format(hotSpotMatrix[i-1][j-1]) + "</font> " +
	    					"<font color=" + Slash + ">/</font> <br />" +
	    					"<font color=" + colorTexts[comparisonColor] + ">" + twoDForm.format(comparisonMatrix[i-1][j-1]) + "</font></html>";
	    		}
	    		
	    		label[i][j].setText(labelText);
	    		label[i][j].setVerticalTextPosition(SwingConstants.CENTER);
	    		label[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
	    		label[i][j].setHorizontalAlignment(SwingConstants.CENTER);
	    		label[i][j].setVerticalAlignment(SwingConstants.CENTER);
	        }
	    }
	}
	
	public void saveImage(String file) 
	{
        BufferedImage image = new BufferedImage(Grid.this.getWidth(), Grid.this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = image.createGraphics();
        this.paint(graph);
        //graph.dispose();
        try
        {
            ImageIO.write(image, "png", new File(file));
        }
        catch(IOException ioe)
        {
            System.out.println("Image Writing Problem: " + ioe.getMessage());
        }
    }
}
