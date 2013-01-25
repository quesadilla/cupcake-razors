package com.horrorgame;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Player {
	enum STATE {RUN, WALK, PUSH, STAND, HIDE};
	
	public static final int PLAYER_WALK = 1; // speed of player walk
	public static final int PLAYER_RUN = 3; // speed of player run
	public static final float STA_MAX = (float) 100;
	public static final float STA_REGEN = (float) 0.5;
	public static final float STA_RUN_COST = (float) 0.5;
	public static final float STA_PUSH_COST = (float) 50;
	
	private TextureRegion playerStand;
	private List<AtlasRegion> playerPush;
	private List<AtlasRegion> playerWalk;
	private List<AtlasRegion> playerRun;
	
	private boolean facingLeft;
	private float stamina;
	private STATE playerState;
	
	private int currentFrame;
	private float frameTime;
	private float moveTime;
	
	private int x, y, height, width;
	
	public Player(TextureAtlas atlas) {
		playerStand = atlas.findRegion("idle");
		playerPush = atlas.findRegions("push");
		playerWalk = atlas.findRegions("walk/mainw");
		playerRun = atlas.findRegions("run/mainr");
		
		playerState = STATE.STAND;
		facingLeft = false;
		currentFrame = 0;
		frameTime = 0;
		moveTime = 0;
		width = 100;
		height = 150;
		x = 100;
		y = 80;
		stamina = STA_MAX;
	}
	
	public void update(float timeDelta)
	{
		frameTime += timeDelta;
		moveTime += timeDelta;
		
		if (frameTime > .1) {
			frameTime = 0;
			if (playerState == STATE.WALK) {
				currentFrame++;
				if (currentFrame >= playerWalk.size()) {
					currentFrame = 0;
				}
			} else if (playerState == STATE.RUN) {
				currentFrame++;
				if (currentFrame >= playerWalk.size()) {
					currentFrame = 0;
				}
			}
		}
		
		if (moveTime > .01) {
			moveTime = 0;
			if (playerState != STATE.RUN) {
				if (stamina < 100) {
					stamina += STA_REGEN;
				} 
			}
			
			if (playerState == STATE.WALK) {
				if (facingLeft) {
					x -= PLAYER_WALK;
				} else {
					x += PLAYER_WALK;
				}
			} else if (playerState == STATE.RUN) {
				if (facingLeft) {
					if (stamina > 0)
						x -= PLAYER_RUN;
					else
						x -= PLAYER_WALK;
				} else {
					if (stamina > 0)
						x += PLAYER_RUN;
					else
						x += PLAYER_WALK;
				}
				
				if (stamina > 0) {
					stamina -= STA_RUN_COST;
				}
			}
		}
	}
	
	public void goLeft(boolean run) {
		if (playerState != STATE.HIDE) {
			if (!facingLeft) {
				flipImages();
				facingLeft = true;
			}
			
			if (run) {
				if (playerState != STATE.RUN) {
					currentFrame = 0;
					frameTime = 0;
					moveTime = 0;
				}
				
				playerState = STATE.RUN;
			} else {
				if (playerState != STATE.WALK) {
					currentFrame = 0;
					frameTime = 0;
					moveTime = 0;
				}
				playerState = STATE.WALK;
			}
		}
	}
	
	public void goRight(boolean run) {
		if (playerState != STATE.HIDE) {
			if (facingLeft) {
				flipImages();
				facingLeft = false;
			}
			
			if (run) {
				if (playerState != STATE.RUN) {
					currentFrame = 0;
					frameTime = 0;
					moveTime = 0;
				}
				playerState = STATE.RUN;
			} else {
				if (playerState != STATE.WALK) {
					currentFrame = 0;
					frameTime = 0;
					moveTime = 0;
				}
				playerState = STATE.WALK;
			}
		}
	}
	
	public void push(List<Monster> monsters) {
		if (playerState != STATE.HIDE && stamina >= STA_PUSH_COST) {
			if (playerState != STATE.PUSH) {
				stamina -= STA_PUSH_COST;
				playerState = STATE.PUSH;
				
				for (int i = 0; i < monsters.size(); i++) {
					// see if monster is in range
				}
			}
		}
	}
	
	public void stand() {
		playerState = STATE.STAND;
	}
	
	public void hide() {
		playerState = STATE.HIDE;
	}
	
	private void flipImages() {
		playerStand.flip(true, false);
		for (int i = 0; i < playerPush.size(); i++) {
			playerPush.get(i).flip(true,  false);
		}
		for (int i = 0; i < playerWalk.size(); i++) {
			playerWalk.get(i).flip(true,  false);
		}
		for (int i = 0; i < playerRun.size(); i++) {
			playerRun.get(i).flip(true,  false);
		}
	}
	
	public void draw(SpriteBatch batch) {
		switch (playerState) {
		case WALK:
			batch.draw(playerWalk.get(currentFrame), x, y, width, height);
			break;
		case RUN:
			batch.draw(playerRun.get(currentFrame), x, y, width, height);
			break;
		case STAND:
			batch.draw(playerStand, x, y, width, height);
			break;
		case PUSH:
			batch.draw(playerPush.get(0), x, y, width, height);
			break;
		case HIDE:
			break;
		default:
			break;
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getCenterX() {
		return x + width/2;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getStamina() {
		return stamina;
	}
	
	public boolean facingLeft() {
		return facingLeft;
	}
	
	public boolean isHiding() {
		return playerState == STATE.HIDE;
	}
}
