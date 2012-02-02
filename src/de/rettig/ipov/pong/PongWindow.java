package de.rettig.ipov.pong;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.rettig.ipov.ImageSender;


/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PongWindow extends org.eclipse.swt.widgets.Composite {
	private String port = "/dev/tty.WiiCopter-DevB-1";
	ImageSender imageSender;
	
	private Composite compField;
	private PongModel pongModel = new PongModel();
	private PongRenderer povRenderer = new PongRenderer(pongModel);
	private Timer timer = new Timer();

	private PaintListener paintField = new PaintListener(){

		@Override
		public void paintControl(PaintEvent arg0) {
			GC gc = arg0.gc;
			gc.setBackground(new org.eclipse.swt.graphics.Color(getDisplay(), 255,255,255));
			gc.fillRectangle(0, 0, pongModel.fieldWidth, pongModel.fieldHeight);
			byte[] data = povRenderer.render();
			for (int x = 0;x < pongModel.fieldWidth;x++){
				for (int y = 0;y < pongModel.fieldHeight;y++){
					int bit = y % 8;
					int pos = (x*16+y)/8;
					if ((1 << bit & data[pos]) != 0){
						gc.drawPoint(x, y);
					}
				}	
			}
		}

	};
	protected char keyDown;

	private KeyListener keyListener = new KeyListener(){

		@Override
		public void keyPressed(KeyEvent arg0) {
			keyDown = arg0.character;

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			keyDown = ' ';
		}

	};
	
	private MouseMoveListener mouseMove = new MouseMoveListener(){

		@Override
		public void mouseMove(MouseEvent arg0) {
			pongModel.setP2(arg0.x);
		}
		
	};

	/**
	 * Auto-generated main method to display this 
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void main(String[] args) {
		showGUI();
	}

	/**
	 * Auto-generated method to display this 
	 * org.eclipse.swt.widgets.Composite inside a new Shell.
	 */
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		PongWindow inst = new PongWindow(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public PongWindow(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
		try {
			imageSender = new ImageSender(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		startTimer();
		
	}

	private void startTimer() {
		TimerTask task = new TimerTask(){

			@Override
			public void run() {
				getDisplay().syncExec(new Runnable(){

					@Override
					public void run() {
						pongModel.loop();
						switch (keyDown) {
						case 'a':
							pongModel.moveP2(-2);
							break;
						case 's':
							pongModel.moveP2(2);
							break;
						case 'y':
							pongModel.moveP1(-2);
							break;
						case 'x':
							pongModel.moveP1(2);
							break;
						default:
							break;
						}
						compField.redraw();
						sendToPov();
					}

				});
			}
		};
		timer.schedule(task, 10, 50);
	}

	private void sendToPov(){
		try {
			imageSender.writeToPort("d".getBytes());
			imageSender.writeToPort(povRenderer.render());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
		try {
			this.setLayout(new FormLayout());
			this.setSize(388, 175);
			{
				FormData compFieldLData = new FormData();
				compFieldLData.width = 338;
				compFieldLData.height = 151;
				compFieldLData.left =  new FormAttachment(0, 1000, 24);
				compFieldLData.top =  new FormAttachment(0, 1000, 12);
				compField = new Composite(this, SWT.NONE);
				GridLayout compFieldLayout = new GridLayout();
				compFieldLayout.makeColumnsEqualWidth = true;
				compField.setLayout(compFieldLayout);
				compField.setLayoutData(compFieldLData);
				compField.addPaintListener(paintField);
				compField.addKeyListener(keyListener);
				compField.addMouseMoveListener(mouseMove );
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
