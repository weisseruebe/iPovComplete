package de.rettig.ipov;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

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
public class IPovGui extends org.eclipse.swt.widgets.Composite {
	private Button btnSend;
	private Label label1;
	private Button btnNext;
	private Button btnBack;
	private Button btnOpen;
	private ImageSender imageSender;
	private String fileName;
	private int index;
	StringRenderer stringRenderer = new StringRenderer();
	//private String port = "/dev/tty.usbserial-A8004Zfe";
	private String port = "/dev/tty.WiiCopter-DevB";
	private Button btnTime;

	private Scale scale1;
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
		IPovGui inst = new IPovGui(shell, SWT.NULL);
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

	public IPovGui(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();

		try {
			imageSender = new ImageSender(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void initGUI() {
		try {
			this.setLayout(new FormLayout());
			this.setSize(479, 165);
			{
				btnTime = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btnTimeLData = new FormData();
				btnTimeLData.width = 67;
				btnTimeLData.height = 32;
				btnTimeLData.left =  new FormAttachment(0, 1000, 254);
				btnTimeLData.top =  new FormAttachment(0, 1000, 121);
				btnTime.setLayoutData(btnTimeLData);
				btnTime.setText("Time");
				btnTime.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						btnTimeWidgetSelected(evt);
					}
				});
			}
			{
				FormData scale1LData = new FormData();
				scale1LData.width = 431;
				scale1LData.height = 22;
				scale1LData.left =  new FormAttachment(0, 1000, 24);
				scale1LData.top =  new FormAttachment(0, 1000, 99);
				scale1 = new Scale(this, SWT.NONE);
				scale1.setLayoutData(scale1LData);
				scale1.setMaximum(63);
				scale1.setPageIncrement(1);
				scale1.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						scale1WidgetSelected(evt);
					}
				});
			}
			{
				btnNext = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btnNextLData = new FormData();
				btnNextLData.width = 49;
				btnNextLData.height = 32;
				btnNextLData.left =  new FormAttachment(0, 1000, 174);
				btnNextLData.top =  new FormAttachment(0, 1000, 121);
				btnNext.setLayoutData(btnNextLData);
				btnNext.setText(">");
				btnNext.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						next(1);
					}
				});
			}
			{
				btnBack = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btnBackLData = new FormData();
				btnBackLData.width = 49;
				btnBackLData.height = 32;
				btnBackLData.left =  new FormAttachment(0, 1000, 119);
				btnBackLData.top =  new FormAttachment(0, 1000, 121);
				btnBack.setLayoutData(btnBackLData);
				btnBack.setText("<");
				btnBack.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent evt) {
						next(-1);
					}
				});
			}
			{
				FormData label1LData = new FormData();
				label1LData.width = 449;
				label1LData.height = 88;
				label1LData.left =  new FormAttachment(0, 1000, 12);
				label1LData.top =  new FormAttachment(0, 1000, 12);
				label1 = new Label(this, SWT.NONE);
				label1.setLayoutData(label1LData);
				
			}
			{
				btnOpen = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btnOpenLData = new FormData();
				btnOpenLData.width = 95;
				btnOpenLData.height = 32;
				btnOpenLData.left =  new FormAttachment(0, 1000, 12);
				btnOpenLData.top =  new FormAttachment(0, 1000, 121);
				btnOpen.setLayoutData(btnOpenLData);
				btnOpen.setText("Open");
				btnOpen.addSelectionListener(new SelectionListener(){

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {

					}

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						FileDialog fileDialog = new FileDialog(getParent().getShell(),SWT.OPEN);

						String fileName = fileDialog.open();
						if (fileName!=null){
							setFile(fileName);
						}

					}

				});
			}
			{
				btnSend = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData btnSendLData = new FormData();
				btnSendLData.width = 128;
				btnSendLData.height = 32;
				btnSendLData.left =  new FormAttachment(0, 1000, 333);
				btnSendLData.top =  new FormAttachment(0, 1000, 121);
				btnSend.setLayoutData(btnSendLData);
				btnSend.setText("Send");
				btnSend.addSelectionListener(new SelectionListener(){

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						if (fileName!=null){
							try {
								imageSender.sendImage(fileName);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

				});
			}
			this.layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void next(int n) {
		index += n;
		if (index>0 & index <5){
			try {
				imageSender.sendImage("./images/smile"+index+".bmp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void setFile(String fileName) {
		this.fileName = fileName;
		label1.setImage(new Image(null,fileName));
	}
	
	private void scale1WidgetSelected(SelectionEvent evt) {
		try {
			imageSender.sendOffset((byte) scale1.getSelection());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void btnTimeWidgetSelected(SelectionEvent evt) {
			startTimeDisplay();
	}

	Timer timer = new Timer();
	private void startTimeDisplay() {
		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
				String s = formatter.format(date);
				try {
					imageSender.sendImage(stringRenderer.getBitmap(s));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		};
		timer.schedule(timerTask, 0,1000);
		
	}

}
