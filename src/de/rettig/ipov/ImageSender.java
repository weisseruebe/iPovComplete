package de.rettig.ipov;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import javax.imageio.ImageIO;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public class ImageSender {
	
	private SerialPort port;
	private OutputStream outputStream;

	public ImageSender(String portname) throws PortInUseException, NoSuchPortException, UnsupportedCommOperationException, TooManyListenersException, IOException{
	    port = (SerialPort)CommPortIdentifier.getPortIdentifier(portname).open("iPov",2000);
		port.setSerialPortParams(115200, 8, 1, 0);
		port.notifyOnDataAvailable(true);
		outputStream = port.getOutputStream();
		
		port.addEventListener(new SerialPortEventListener(){

			@Override
			public void serialEvent(SerialPortEvent arg0) {
				if(arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE){
					byte[] in = read();
					System.out.print(new String(in));
				}
			}
			
		});
	}
	
	public byte[] read() {
		byte[] buffer = new byte[1024];
		try {
			int len = port.getInputStream().read(buffer);
			byte[] tmp = new byte[len];
			System.arraycopy(buffer,0,tmp,0,len);
			port.getInputStream().close();
			return tmp;
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return new byte[0]; 
	}

	protected void writeToPort(byte[] data) throws IOException {
		outputStream.write(data);
	}
	
	public void close(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				port.close();
			}
			
		}).start();
	}
	
	public void sendImage(String fileName) throws IOException{
		writeToPort("d".getBytes());
		writeToPort(ImageConverter.convert(ImageIO.read(new File(fileName))));
	}
	
	public void sendImage(BufferedImage bufferedImage) throws IOException{
		writeToPort("d".getBytes());
		writeToPort(ImageConverter.convert(bufferedImage));
	}
	
	public void sendOffset(byte offset) throws IOException{
		writeToPort("o".getBytes());
		writeToPort(new byte[]{offset});
	}
	
	public static void main(String[] args) throws PortInUseException, NoSuchPortException, UnsupportedCommOperationException, IOException, TooManyListenersException, InterruptedException {
		ImageSender imageSender = new ImageSender("/dev/tty.WiiCopter-DevB");
		Thread.sleep(1000);
		imageSender.sendImage("./images/smile.bmp");
	}

	public void sendTimelineIndex(byte index) throws IOException {
		writeToPort("p".getBytes());
		writeToPort(new byte[]{index});
	}
}
