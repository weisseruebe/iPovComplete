package de.rettig.ipov.pong;


public class PongRenderer {
	int w = 64;
	private PongModel pongModel;

	public PongRenderer(PongModel pongModel){
		this.pongModel = pongModel;
	}
	
	public byte[] render(){
		byte[] buffer = new byte[2*w];
		
		// Panel 1
		for (int x = 0;x<pongModel.pWidth;x++){
			int pos = pongModel.p1Pos*2 + 2*x;
			pos = pos % (w*2);
			buffer[pos+1] |= 1;
		}
		
		//Panel 2
		for (int x = 0;x<pongModel.pWidth;x++){
			int pos = pongModel.p2Pos*2 + 2*x;
			pos = pos % (w*2);
			buffer[pos] |= 1<<7;
		}
		
		//Ball
		int ballIndex = pongModel.ballPos.x * 2 + 1-(pongModel.ballPos.y / 8);
		buffer[ballIndex] |= 1 << pongModel.ballPos.y % 8;
		return buffer;
	}
	
}
