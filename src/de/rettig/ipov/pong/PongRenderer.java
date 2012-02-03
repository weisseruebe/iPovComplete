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
			buffer[pos+1] |= 3;
		}

		//Panel 2
		for (int x = 0;x<pongModel.pWidth;x++){
			int pos = pongModel.p2Pos*2 + 2*x;
			pos = pos % (w*2);
			buffer[pos] |= 3<<6;
		}

		//Ball
		int ballIndex = pongModel.ballPos.x * 2 + 1-(pongModel.ballPos.y / 8);
		if (ballIndex >= 0 & ballIndex < buffer.length){
			buffer[ballIndex % 128]     |= 3 << pongModel.ballPos.y % 8;
			buffer[(ballIndex+2) % 128] |= 3 << pongModel.ballPos.y % 8;
		}
		buffer[pongModel.wallPos*2]   = (byte) 0xFF;
		buffer[pongModel.wallPos*2+1] = (byte) 0xFF;
		
		return buffer;
	}

}
