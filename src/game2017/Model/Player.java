package game2017.Model;

import java.io.Serializable;

public class Player implements Serializable {
	String name;
	int xpos;
	int ypos;
	int point;
	String direction;
	boolean alive = true;

	private int prev_xpos;
	private int prev_ypos;

	public Player(String name, int xpos, int ypos, String direction) {
		this.name = name;
		this.xpos = xpos;
		this.ypos = ypos;
		this.direction = direction;
		this.point = 0;
	}

	public int getXpos() {
		return xpos;
	}
	public void setXpos(int xpos) {
		this.xpos = xpos;
	}
	public int getYpos() {
		return ypos;
	}
	public void setYpos(int ypos) {
		this.ypos = ypos;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public void addPoints(int p) {
		point+=p;
	}
	public String toString() {
		return name+":   "+point + ", x: " + xpos + ", y: " + ypos;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getPrev_xpos() {
		return prev_xpos;
	}

	public void setPrev_xpos(int prev_xpos) {
		this.prev_xpos = prev_xpos;
	}

	public int getPrev_ypos() {
		return prev_ypos;
	}

	public void setPrev_ypos(int prev_ypos) {
		this.prev_ypos = prev_ypos;
	}
}
