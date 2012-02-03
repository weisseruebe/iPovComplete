package de.rettig.ipov.pong;

import java.awt.Point;

import javax.vecmath.Vector2f;

public class PongModel {
	int fieldHeight = 16;
	int fieldWidth  = 64;
	int pWidth 	    = 20;
	int wallPos = 0;
	int p1Pos = 0;
	int p2Pos = 0;
	
	Point ballPos = new Point(2,2);
	Vector2f ballDir = new Vector2f(1f,1f);
	
	float speed = 2; //pixel pro runde
	
	void restart(){
		ballPos = new Point(0,2);
		ballDir = new Vector2f(1,1);
		System.out.println("restart");
	}
	
	void moveP1(int dif){
		setP1(p1Pos+dif);
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
	
	void setP1(int p){
		p1Pos = p;
		if (p1Pos < 0){
			p1Pos = fieldWidth;
		}
		if (p1Pos > 64){
			p1Pos = 0;
		}
	}
	
	void loop(){
		ballDir.normalize();
		
		if (ballPos.x == wallPos){
			ballDir.x = -ballDir.x;		
		}
		
		/* Oben oder unten */
		if (ballPos.y <= 0 ){
			/* Wenn getroffen */
			if ((ballPos.x > p1Pos) & (ballPos.x  < (p1Pos + pWidth))){
				ballDir.y = -ballDir.y;		
			} else {
				restart();
			}		
		}
		if (ballPos.y >= fieldHeight-1){
			//if ((ballPos.x >= p2Pos) & (ballPos.x  <= p2Pos + pWidth)){
				ballDir.y = -ballDir.y;		
			//} else {
			//	restart();
			//}
		}
		
		/* Move */
		ballPos.x += (int) (speed*ballDir.x);
		ballPos.y += (int) (speed*ballDir.y);
		
		ballPos.x = (ballPos.x) % fieldWidth;
		if (ballPos.x < 0){
			ballPos.x = fieldWidth-1;
		}
		
	}
	
}
