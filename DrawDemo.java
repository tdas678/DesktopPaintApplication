import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.color.*;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Stack;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;

public class DrawDemo{
	
	//variable declaration
	private int last_x;
	private int last_y;
	private int x;
	private int y;
	private int tool=1;   // default free-hand sketch
	private String imagepath;
	
	public Color c=Color.blue;  //default paint color=blue

	private BufferedImage bufferedImage;
//	variable for copy and paste
	private Rectangle copied;
	private boolean cutvalue=false;
	private ArrayList<Rectangle> shape=new ArrayList<Rectangle>();
	
// variable for undo and redo
	private Stack<BufferedImage> undoStack=new Stack<BufferedImage>();
	private Stack<BufferedImage> redoStack=new Stack<BufferedImage>();

	
	
	

	
	//Main Component declaration
	JFrame paint=new JFrame("Sketch Pad");
	JPanel shapePanel=new JPanel();
	JPanel colorPanel=new JPanel();
	drawPanel draw=new drawPanel();
	JPanel menuPanel=new JPanel();
	//Shape button declaration
	JButton zigline=new JButton("Zig-line");
	JButton straightline=new JButton("Straight-line");
	JButton rectangle=new JButton("Rectangle");
	JButton ellipse=new JButton("Ellipse");
	JButton circle=new JButton("Circle");
	JButton square=new JButton("Square");
	JButton rhombus=new JButton("Rhombus");
	JButton polygon=new JButton("Polygon");
	//Controls button declaration
	JButton clear=new JButton("Clear Screen");
	JButton delete=new JButton("Eraser");
	JButton copy=new JButton("Copy");
	JButton cut=new JButton("Cut");
	JButton paste=new JButton("Paste");
	JButton open=new JButton("Open File");
	JButton save=new JButton("Save");
	JButton undo=new JButton("Undo");
	JButton redo=new JButton("Redo");
	//Color button declaration
	JButton black=new JButton("Black");
	JButton red=new JButton("Red");
	JButton blue=new JButton("Blue");
	JButton cyan=new JButton("Cyan");
	JButton pink=new JButton("Pink");
	JButton orange=new JButton("Orange");
	JButton yellow=new JButton("Yellow");
	JButton green=new JButton("Green");
	JLabel shapeLabel=new JLabel("Shape Panel");
	//FileChooser
	JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "JPG,PNG & GIF Images", "jpg", "gif","png");
	
	DrawDemo(){
		
		prepGUI();
	}
	//Component Layout
	public void prepGUI(){
	  paint.setSize(1600, 1000);
	// paint.setLayout(new GridLayout(2,0));
	  paint.setLayout(new FlowLayout());
	 shapePanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
	//  shapePanel.setLayout(new GridLayout(0,5));
	  shapePanel.setLayout(new FlowLayout());
	  shapePanel.setPreferredSize(new Dimension(1500,50));
	  colorPanel.setLayout(new FlowLayout());
	  colorPanel.setPreferredSize(new Dimension(700,50));
	  menuPanel.setPreferredSize(new Dimension(750,50));
	  menuPanel.setBorder(BorderFactory.createLineBorder(Color.magenta));
	  colorPanel.setBorder(BorderFactory.createLineBorder(Color.magenta));
	
	  paint.addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent e){
				 System.exit(0);
			 }
		 });

	  //Adding ActionListener to components
	 zigline.addActionListener(new listenerPaint());
	 straightline.addActionListener(new listenerPaint());
	 rectangle.addActionListener(new listenerPaint());
	 ellipse.addActionListener(new listenerPaint());
	 rhombus.addActionListener(new listenerPaint());
	 polygon.addActionListener(new listenerPaint());
	 square.addActionListener(new listenerPaint());
	 circle.addActionListener(new listenerPaint());
	 open.addActionListener(new listenerPaint());
	 save.addActionListener(new listenerPaint());
	 clear.addActionListener(new listenerPaint());
	 delete.addActionListener(new listenerPaint());
	 copy.addActionListener(new listenerPaint());
	 cut.addActionListener(new listenerPaint());
	 undo.addActionListener(new listenerPaint());
	 redo.addActionListener(new listenerPaint());
	 paste.addActionListener(new colorPaint());
	 red.addActionListener(new colorPaint());
	 blue.addActionListener(new colorPaint());
	 cyan.addActionListener(new colorPaint());
	 pink.addActionListener(new colorPaint());
	 orange.addActionListener(new colorPaint());
	 yellow.addActionListener(new colorPaint());
	 green.addActionListener(new colorPaint());
	 black.setBackground(Color.black);
	 red.setBackground(Color.red);
	 blue.setBackground(Color.blue);
	 cyan.setBackground(Color.cyan);
	 pink.setBackground(Color.pink);
	 orange.setBackground(Color.orange);
	 yellow.setBackground(Color.yellow);
	 green.setBackground(Color.green);
	 shapePanel.add(shapeLabel);
	 shapePanel.add(zigline);
	 shapePanel.add(straightline);
	 shapePanel.add(rectangle);
	 shapePanel.add(ellipse);
	 shapePanel.add(square);
	 shapePanel.add(circle);
	 shapePanel.add(polygon);
	 shapePanel.add(rhombus);
	 colorPanel.add(black);
	 colorPanel.add(red);
	 colorPanel.add(blue);
	 colorPanel.add(cyan);
	 colorPanel.add(pink);
	 colorPanel.add(orange);
	 colorPanel.add(green);
	 menuPanel.add(clear);
	 menuPanel.add(open);
	 menuPanel.add(save);
	 menuPanel.add(delete);
	 menuPanel.add(copy);
	 menuPanel.add(cut);
	 menuPanel.add(undo);
	 menuPanel.add(redo);
	 menuPanel.add(paste);
	 paint.add(shapePanel);
	 paint.add(colorPanel);
	 paint.add(menuPanel);
	paint.add(draw,BorderLayout.SOUTH);
	 
	 paint.setVisible(true);

	 
	}
	//ActionListener Class
	public class listenerPaint implements ActionListener{

	
		public void actionPerformed(ActionEvent e) {
			String key=e.getActionCommand();
	//		drawPanel.addMouseListener(new mousePaint() );
	//		drawPanel.addMouseMotionListener(new mousePaint());
			if(key.equalsIgnoreCase("Zig-line")) tool=1;
		 	else if(key.equalsIgnoreCase("Straight-line"))tool=2;
			else if(key.equalsIgnoreCase("Rectangle"))tool=3;
			else if(key.equalsIgnoreCase("Ellipse"))tool=4;
			else if(key.equalsIgnoreCase("Circle"))tool=5;
			else if(key.equalsIgnoreCase("Square")) tool=6;
			else if(key.equalsIgnoreCase("Polygon")) tool=7;
			else if(key.equalsIgnoreCase("Rhombus")) tool=8;
			else if(key.equalsIgnoreCase("Eraser")) tool=10;
			else if(key.equalsIgnoreCase("Clear Screen")){
				Graphics p=bufferedImage.getGraphics();
				p.setColor(Color.white);
				p.fillRect(0, 0, 1500, 800);
				p.drawImage(bufferedImage, 0, 0, draw);
				draw.update(draw.getGraphics());
			}
			else if(key.equalsIgnoreCase("Copy")) tool=11;
			else if(key.equalsIgnoreCase("Cut")) tool=12;
			else if(key.equalsIgnoreCase("Open File")) 
			{
				chooser.setFileFilter(filter);
				int val=chooser.showOpenDialog(draw);
				if(val==JFileChooser.APPROVE_OPTION){
					System.out.println(chooser.getSelectedFile().getPath());
					imagepath=chooser.getSelectedFile().getPath();
					
						
					 }
				
				try {
					bufferedImage = ImageIO.read(new File(imagepath));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					Graphics p=draw.getGraphics();
					p.drawImage(bufferedImage, 0, 0, null);
				
				
				
			}else if(key.equalsIgnoreCase("Save")){
				
				BufferedImage bi = new BufferedImage(draw.getSize().width, draw.getSize().height,   BufferedImage.TYPE_INT_ARGB); 
				Graphics g = bi.createGraphics();
				draw.paint(g);  //this == JComponent
				g.dispose();
				
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("Save the Image");
				int val=chooser.showSaveDialog(draw);
				if(val==JFileChooser.APPROVE_OPTION){
					 try {
			                ImageIO.write(bi, "png", chooser.getSelectedFile());
			            } catch (IOException oe) {
			                oe.printStackTrace();
			            }
				}
			}
			else if(key.equalsIgnoreCase("Undo")){
			  draw.saveImage(redoStack);
				if(!undoStack.isEmpty()){
				bufferedImage=undoStack.pop();
				redoStack.push(bufferedImage);
				Graphics m=draw.getGraphics();
				m.drawImage(bufferedImage, 0, 0, null);}
			}else if(key.equalsIgnoreCase("Redo")){
				if(!redoStack.isEmpty()){
				bufferedImage=redoStack.pop();
				Graphics m=draw.getGraphics();
				m.drawImage(bufferedImage, 0, 0, null);}
			}
			}
		
	}
	
	
	
	public class colorPaint implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			
			String key=e.getActionCommand();
			
			
			 if(key.equalsIgnoreCase("Black")) c=Color.black;
			else if(key.equalsIgnoreCase("Red")) c=Color.red;
			else if(key.equalsIgnoreCase("Blue")) c=Color.blue;
			else if(key.equalsIgnoreCase("Cyan")) c=Color.cyan;
			else if(key.equalsIgnoreCase("Pink")) c=Color.pink; 
			else if(key.equalsIgnoreCase("Red")) c=Color.red;
			else if(key.equalsIgnoreCase("Orange")) c=Color.orange; 
			else if(key.equalsIgnoreCase("Yellow")) c=Color.yellow;
			else if(key.equalsIgnoreCase("Green")) c=Color.green; 
			else if(key.equalsIgnoreCase("Paste")) tool=13;
			} 
		
	}
	//Mouse Events
	public class drawPanel extends JPanel implements MouseListener,MouseMotionListener {
		
public drawPanel(){
	addMouseListener( this );
    addMouseMotionListener( this );
    setPreferredSize(new Dimension(1500,800));
    setBorder(BorderFactory.createLineBorder(Color.BLUE));
    paint.revalidate();
    bufferedImage =new BufferedImage(
            1500, 800, BufferedImage.TYPE_INT_RGB);
    Graphics2D ig2 = bufferedImage.createGraphics();

    ig2.setBackground(Color.WHITE);
    ig2.clearRect(0, 0, 1500, 800);
	
}

		@Override
		public void mouseDragged(MouseEvent e) {
			
			 if(tool==1){
				 x=e.getX();
				   y=e.getY();
			        
				   saveImage(undoStack);
				   Graphics g=bufferedImage.createGraphics();
				   g.setColor(c);
				    g.drawLine(last_x, last_y, x, y);
				    
				    last_x=x;
				    last_y=y;
				    repaint();
				 
				}else if(tool==10){
					 x=e.getX();
					   y=e.getY();

					   Graphics g=bufferedImage.createGraphics();
					   g.setColor(Color.white);
			         g.fillRect(x, y, 10, 10);
			         repaint();
				}else{
					x=e.getX();
					y=e.getY();
				}
			 
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			    
				last_x=e.getX();
				last_y=e.getY();
 
				}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			if(tool==2){
				 
				saveImage(undoStack);
				   Graphics g=bufferedImage.createGraphics();
				   g.setColor(c);
			g.drawLine(last_x, last_y, x, y);
			repaint(); 
			last_x=x;
			 last_y=y;
			 
			 }else if(tool==3){
				
				 saveImage(undoStack);
				 int w=x-last_x;
				 int h=y-last_y;
				 Graphics n=bufferedImage.createGraphics();
				   n.setColor(c);
				 n.fillRect(last_x, last_y, w, h);				
				 repaint();
			 }else if(tool==4){
				 saveImage(undoStack);
				 int w=x-last_x;
				 int h=y-last_y;
				  Graphics g=bufferedImage.createGraphics();
				   g.setColor(c);
				 g.fillOval(last_x, last_y, w, h);	
				 repaint();
			 }else if(tool==5){
				 saveImage(undoStack);
				 int w=x-last_x;
				 Graphics g=bufferedImage.createGraphics();
				   g.setColor(c);
				 g.fillOval(last_x, last_y, w, w);
				  
				 repaint();
			 }else if(tool==6){
				 saveImage(undoStack);
				 int w=x-last_x;
				  Graphics g=bufferedImage.createGraphics();
				   g.setColor(c);
				 g.fillRect(last_x, last_y, w, w);	
				 repaint();
			 }else if(tool==7){
				 saveImage(undoStack);
				 double d=(((x-last_x)*(x-last_x))+((y-last_y)*(y-last_y)));
				 double r=(Math.sqrt(d))/2;
				 int a=(int) ((int) 2*r*(Math.sin((Math.PI)/2)));
				 int []xPoints={last_x,last_x+(a/2),last_x+(a/4),last_x-(a/4),last_x-(a/2)};
				 int []yPoints={last_y,last_y+(a/2),y,y,last_y+(a/2)};
				 Graphics g=bufferedImage.createGraphics();
				   g.setColor(c);
				  g.fillPolygon(xPoints, yPoints,5);
				  repaint();
			 }else if(tool==8){
				 saveImage(undoStack);
				 double d=(((x-last_x)*(x-last_x))+((y-last_y)*(y-last_y)));
				 double r=(Math.sqrt(d))/2;
				 int a=(int) ((int) 2*r*(Math.sin((Math.PI)/2)));
				 int []xPoints={last_x,last_x+(a/2),last_x,last_x,last_x-(a/2)};
				 int []yPoints={last_y,last_y+(a/2),y,y,last_y+(a/2)};
				 Graphics g=bufferedImage.createGraphics();
				   g.setColor(c);
				g.fillPolygon(xPoints, yPoints, 5);
				 repaint();
				 
			 }else if(tool==11){
				 int w=x-last_x;
				 int h=y-last_y;	
				 saveImage(undoStack);
			copied=new Rectangle(last_x,last_y,w,h);
			//g2d.draw(copied);
			shape.add(copied);
				 
			 }else if(tool==12){
				 saveImage(undoStack);
				 int w=x-last_x;
				 int h=y-last_y;	
			
			copied=new Rectangle(last_x,last_y,w,h);
			shape.add(copied);
			cutvalue=true;
				 
			 }
				 else if(tool==13){
					 saveImage(undoStack);
				 int nx=e.getX();
				 int ny=e.getY();
				 int w,h,x,y=0;
				 for(Rectangle p:shape){
					w=p.width;
				    h=p.height;
				    x=p.x;
				    y=p.y;
				  int  dx=nx-x;
				    int dy=ny-y;
					 System.out.println(w);
					 Graphics g=bufferedImage.createGraphics();
					   g.setColor(c);
					   Graphics2D g2d=(Graphics2D)g;
					  g2d.copyArea(x, y, w, h, dx, dy);
					  repaint();
					  if(cutvalue){ 
						  g2d.setColor(Color.white);
						  g2d.fillRect(x, y, w, h);
						  }
				 }
				 shape.clear();
				 
			     
			 }
				
				last_x=0;
				last_y=0;
				x=0;
				y=0;
				
				
		}
		public void paintComponent( Graphics g ) {
			super.paintComponent(g);
			
		 g.drawImage(bufferedImage, 0, 0, null);
		 g.dispose();
		
		}
		
		public void saveImage(Stack s){
			BufferedImage bi = new BufferedImage(draw.getSize().width, draw.getSize().height, BufferedImage.TYPE_INT_ARGB); 
			Graphics g = bi.createGraphics();
			draw.paint(g);  //this == JComponent
			g.dispose();
			s.push(bi);
		}
			
	}
	
	
	
		
	public static void main(String args[]){
		DrawDemo n=new DrawDemo();
		
	}
}