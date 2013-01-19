package com.horrorgame;

import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Input.Keys;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.Game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.InputProcessor;

public class Game implements ApplicationListener {

	// screen width and height
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 400;

	// Constants for game	
	public static final int PLAYER_WALK = 1; // speed of player walk
	public static final int PLAYER_RUN = 3; // speed of player run
	public static final int MONSTER_WALK = 2; // speed of monster walk
	public static final float PLAYER_RUN_DELAY = (float) 2.3;
	public static final int GAME_PAUSED = 0;
	public static final int GAME_START = 1;
	
	// things to draw
	SpriteBatch batch;
	Texture texture;
	TextureRegion closet_open;
	TextureRegion closet_close;
	TextureRegion playerStand;
	TextureRegion playerPush;
	List<AtlasRegion> monsterWalk;
	List<AtlasRegion> playerWalk;
	List<AtlasRegion> playerRun;
	InputProcessor processor;
	Rectangle player;
	Rectangle monster;
	int currentFrame;
	int monsterCurrentFrame;
	int currSpeed;
	int gameStatus;
	float frameTime;
	float walkTime;
	float monsterWalkTime;
	float currentMonsterFrameTime;
	float timeHiding;
	float totalWalkTime;
	boolean goingLeft;
	boolean monsterGoingLeft;
	boolean hiding;
	BitmapFont font;
	
	TextureAtlas atlas;
	
	public StartScreen getStartScreen()
	{
		return new StartScreen();
	}
	
	@Override
	public void create() {
		
		atlas = new TextureAtlas(Gdx.files.internal("assets/images.pack"));
		
		// find images from pack
		closet_open = atlas.findRegion("open_closet");
		closet_close = atlas.findRegion("close_closet");
		
		playerPush = atlas.findRegion("push");
		playerStand = atlas.findRegion("stand");
		playerWalk = atlas.findRegions("mainw");
		playerRun = atlas.findRegions("mainr");
		
		monsterWalk = atlas.findRegions("monsta");

		// initialize speed variables
		frameTime = 0;
		walkTime = 0;
		monsterWalkTime = 0;
		currentFrame = 0;
		monsterCurrentFrame = 0;
		currentMonsterFrameTime = 0;
		goingLeft = false;
		monsterGoingLeft = true;
		timeHiding = 0;
		hiding = false;
		totalWalkTime = 0;
		currSpeed = PLAYER_WALK;
		gameStatus = 1; // NOTE: change this once start screen is made
		
		font = new BitmapFont();
		font.setColor(0,0,0,1);
		
		batch = new SpriteBatch();

//		setScreen( (Screen)getStartScreen());
		
		// player
		player = new Rectangle();
		player.width = 100;
		player.height = 150;
		player.x = 100;
		player.y = 100;
		
		// monster
		monster = new Rectangle();
		monster.width = 275;
		monster.height = 200;
		monster.x = 20;
		monster.y = 100;
	
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {

		// pause the game
		if(Gdx.input.isKeyPressed(Keys.P) && gameStatus != GAME_PAUSED)
		{
			gameStatus = GAME_PAUSED;
		}
		
		// continue the game
		else
		{
			gameStatus = GAME_START;
		}
		
		// monster walking
		if(monsterGoingLeft)
		{
			for( int i = 0; i < monsterWalk.size(); i++)
			{
				monsterWalk.get(i).flip(true, false);
			}
			monsterGoingLeft = false;
		}
		
		// controls rate of monster's speed
		if(monsterWalkTime > .02)
		{
			monster.x += MONSTER_WALK;
			monsterWalkTime = 0;
		}

		if(currentMonsterFrameTime > .06)
		{
			monsterCurrentFrame++;
			currentMonsterFrameTime = 0;
		}
		if(monsterCurrentFrame >= monsterWalk.size())
		{
			monsterCurrentFrame = 0;
		}
		
		
		// keeps track of player and monster walking time
		walkTime += Gdx.graphics.getDeltaTime();
		monsterWalkTime += Gdx.graphics.getDeltaTime();
		currentMonsterFrameTime += Gdx.graphics.getDeltaTime();
		
		// move the player left
		if(Gdx.input.isKeyPressed(Keys.LEFT) && !hiding)
		{
			if (!goingLeft) {
				playerPush.flip(true, false);
				for (int i = 0; i < playerWalk.size(); i++) {
					playerWalk.get(i).flip(true,  false);
				}
				for (int i = 0; i < playerRun.size(); i++) {
					playerRun.get(i).flip(true,  false);
				}
				currSpeed = PLAYER_WALK;
				totalWalkTime = 0;
			}
			
			// controls rate of player's speed
			if(walkTime > .01)
			{
				player.x -= currSpeed;
				walkTime = 0;
			}
			goingLeft = true;
		}
			
		// move the player right
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && !hiding)
		{
			if (goingLeft) {
				playerPush.flip(true, false);
				for (int i = 0; i < playerWalk.size(); i++) {
					playerWalk.get(i).flip(true,  false);
				}
				for (int i = 0; i < playerRun.size(); i++) {
					playerRun.get(i).flip(true,  false);
				}
				currSpeed = PLAYER_WALK;
				totalWalkTime = 0;
			}
			if(walkTime > .01 && player.x < SCREEN_WIDTH)
			{
				player.x += currSpeed;
				walkTime = 0;
			}
			else if (player.x >= SCREEN_WIDTH || player.x < 0)
			{
				currSpeed = 0;
			}
			goingLeft = false;
		}
		
		timeHiding += Gdx.graphics.getDeltaTime();
		
		// make the player hide
		if(Gdx.input.isKeyPressed(Keys.UP) && player.x > 390 && player.x < 450) {
			if (timeHiding > .25)
			{
				hiding = !hiding;
				timeHiding = 0;
			}
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		if (!hiding)
			batch.draw(closet_open, 400, 100, 70, 120);
		else
		{
			batch.draw(closet_close,  410,  100,  50,  120);
			font.draw(batch, "Narnia Discovered!", 370, 100);
		}
		
		// monster
		batch.draw(monsterWalk.get(monsterCurrentFrame), monster.x, monster.y, monster.width, monster.height);
		
		if (!hiding) {
			if (Gdx.input.isKeyPressed(Keys.SPACE)) {
				batch.draw(playerPush, player.x, player.y, player.width, player.height);
			} 
			else if (!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)) {
				currentFrame = 0;
				batch.draw(playerWalk.get(currentFrame), player.x, player.y, player.width, player.height);
			} 
			else {
				frameTime += Gdx.graphics.getDeltaTime();
				if (frameTime > .1) {
					totalWalkTime += frameTime;
					frameTime = 0;
					currentFrame++;
					if (totalWalkTime > PLAYER_RUN_DELAY) {
						currSpeed = PLAYER_RUN;
					}
					if (currentFrame >= playerWalk.size()) {
						currentFrame = 0;
						//if (currSpeed < 3) {
						//	currSpeed++;
						//}
					}
				}
				if (currSpeed == PLAYER_WALK)
					batch.draw(playerWalk.get(currentFrame), player.x, player.y, player.width, player.height);
				else
					batch.draw(playerRun.get(currentFrame), player.x, player.y, player.width, player.height);
			}
		}
		
// batch.draw(monsterStand, 25, 100, 550, 400);
		
		batch.end();

	}

	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
		
	}

}
