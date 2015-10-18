package com.mygdx.game.objs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Item {
	Rectangle dims;
	Texture img;
	int points;
	public Rectangle getDims() {
		return dims;
	}
	public void setDims(Rectangle dims) {
		this.dims = dims;
	}
	
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public Texture getImg() {
		return img;
	}
	public void setImg(Texture img) {
		this.img = img;
	}
	
}
