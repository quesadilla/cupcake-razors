package com.horrorgame;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Monster {

	public static final int MONSTER_WALK = 1; // speed of monster walk
	public static final int MONSTER_RUN = 4;
	public static final int MONSTER_VIEW = 250;
	public static final int PACE_RANGE = 150;
	public static final float MONSTER_STUMBLE = 2;
	
	enum STATE {WALK, CHASE, STAND, STUMBLE_LEFT, STUMBLE_RIGHT};
	enum AI {PACER, GUARD, WALKER, IDIOT};
	
	List<AtlasRegion> monsterWalk;
	
	private int currentFrame;
	private boolean goingLeft;
	private float walkTime;
	private float frameTime;
	private float stumbleTime;
	private int x, y, width, height, startPace;
	private STATE monsterState;
	private AI ai;
	
	private int lastPlayerX;
	
	public Monster(TextureAtlas atlas, AI type, int x, int y, boolean facingLeft) {
		monsterWalk = atlas.findRegions("monsta");
		
		ai = type;
		
		currentFrame = 0;
		goingLeft = true;
		
		if (!facingLeft) {
			goRight();
		}
		
		// start out stationary
		monsterState = STATE.STAND;
		
		width = 275;
		height = 200;
		this.x = x;
		this.y = y;
		
		startPace = x;
	}
	
	public void goLeft() {
		if (!goingLeft) {
			flipImages();
			goingLeft = true;
		}
	}
	
	public void goRight() {
		if (goingLeft) {
			flipImages();
			goingLeft = false;
		}
	}
	
	public void stumbleLeft() {
		stumbleTime = 0;
		monsterState = STATE.STUMBLE_LEFT;
	}
	
	public void stumbleRight() {
		stumbleTime = 0;
		monsterState = STATE.STUMBLE_RIGHT;
	}
	
	public void update(float timeDelta, Player player) {
		walkTime += timeDelta;
		frameTime += timeDelta;
		stumbleTime += timeDelta;
		
		makeDecision(player);
		
		if (monsterState == STATE.STUMBLE_LEFT || monsterState == STATE.STUMBLE_RIGHT) {
			if (monsterState == STATE.STUMBLE_LEFT) {
				x -= MONSTER_WALK;
			} else if (monsterState == STATE.STUMBLE_RIGHT){
				x += MONSTER_WALK;
			}
			if (stumbleTime > MONSTER_STUMBLE) {
				monsterState = STATE.STAND;
			}
			return;
		} 
		if (walkTime > .02) {
			walkTime = 0;
			if (goingLeft) {
				if (monsterState == STATE.WALK) {
					x -= MONSTER_WALK;
				} else if (monsterState == STATE.CHASE) {
					x -= MONSTER_RUN;
				}
			} else {
				if (monsterState == STATE.WALK) { 
					x += MONSTER_WALK;
				} else if (monsterState == STATE.CHASE) {
					x += MONSTER_RUN;
				}
			}
		}
		
		if (frameTime > .06) {
			frameTime = 0;
			if (monsterState == STATE.WALK || monsterState == STATE.CHASE) {
				currentFrame++;
				if (currentFrame >= monsterWalk.size()) {
					currentFrame = 0;
				}
			}
		}
	}
	
	private void makeDecision(Player player) {
		if (monsterState == STATE.STUMBLE_LEFT || monsterState == STATE.STUMBLE_RIGHT) {
			return;
		}
		switch (ai) {
		case IDIOT:
			monsterState = STATE.STAND;
			break;
		case WALKER:
			monsterState = STATE.WALK;
			break;
		case PACER:
			if (inView(player)) {
				monsterState = STATE.CHASE;
			} else {
				if (monsterState == STATE.CHASE) {
					if (player.isHiding()) {
						if (goingLeft && x < lastPlayerX) {
							monsterState = STATE.WALK;
						} else if (!goingLeft && x > lastPlayerX) {
							monsterState = STATE.WALK;
						}
					} else {
						monsterState = STATE.WALK;
					}
				} else {
					monsterState = STATE.WALK;
					if (goingLeft && startPace - x > PACE_RANGE) {
						goRight();
					} else if (!goingLeft && x - startPace > PACE_RANGE) {
						goLeft();
					}
				}
			}
			break;
		case GUARD:
			if (inView(player)) {
				monsterState = STATE.CHASE;
			} else {
				monsterState = STATE.STAND;
			}
			break;
		default:
			break;
		}
	}
	
	private boolean inView(Player player) {
		if (!player.isHiding()) {
			lastPlayerX = player.getCenterX();
			if (goingLeft) {
				return (getCenterX() - player.getCenterX() > 0 && getCenterX() - player.getCenterX() < MONSTER_VIEW);
			} else {
				return (player.getCenterX() - getCenterX() > 0 && player.getCenterX() - getCenterX() < MONSTER_VIEW);
			}
		} else {
			return false;
		}
	}
	
	private void flipImages() {
		for ( int i = 0; i < monsterWalk.size(); i++) {
			monsterWalk.get(i).flip(true, false);
		}
	}
	
	public void draw(SpriteBatch batch) {
		Random rnd = new Random();
		if (rnd.nextInt(10) < 6)
			batch.draw(monsterWalk.get(currentFrame), x, y, width, height);
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
}
