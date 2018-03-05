package game2017.Model;

import java.io.Serializable;

public class Player implements Serializable {
	private String name;
	private int xpos;
	private int ypos;
	private int point;
	private String direction;
	private boolean alive = true;

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
		return name+":   "+point;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
