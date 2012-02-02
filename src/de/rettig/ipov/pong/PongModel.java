package de.rettig.ipov.pong;

import java.awt.Point;

import javax.vecmath.Vector2f;

public class PongModel {
	int fieldHeight = 16;
	int fieldWidth  = 64;
	int pWidth 	    = 20;
	
	int p1Pos = 0;
	int p2Pos = 0;
	
	Point ballPos = new Point(0,2);
	Vector2f ballDir = new Vector2f(1,1);
	
	float speed = 2; //pixel pro runde
	
	void moveP1(int dif){
		p1Pos += dif;
		if (p1Pos < 0){
			p1Pos = fieldWidth;
		}
		if (p1Pos > 64){
			p1Pos = 0;
		}
	}
	
	void moveP2(int dif){
		setP2(p2Pos+dif);
	}
	
	void setP2(int p){
		p2Pos = p;
		if (p2Pos < 0){
			p2Pos = fieldWidth;
		}
		if (p2Pos > 64){
			p2Pos = 0;
		}
	}
	
	void loop(){
		ballDir.normalize();
		
		/* Oben oder unten */
		if (ballPos.y <= 0 | ballPos.y >= fieldHeight-1){
			ballDir.y = -ballDir.y;			
		} 
		
		/* Move */
		ballPos.x += (int) (speed*ballDir.x);
		ballPos.y += (int) (speed*ballDir.y);
		
		ballPos.x = (ballPos.x) % fieldWidth;
		
	}
	
}
