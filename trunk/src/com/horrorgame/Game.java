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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.InputProcessor;

public class Game implements ApplicationListener {

	// screen width and height
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 400;

	// Constants for game
	public static final int MONSTER_WALK = 2; // speed of monster walk
	public static final int GAME_PAUSED = 0;
	public static final int GAME_START = 1;
	
	// things to draw
	SpriteBatch batch;
	Texture texture;
	TextureRegion closet_open;
	TextureRegion closet_close;
	TextureRegion mainBackground;
	TextureRegion lockers;
	TextureRegion door;
	
	List<AtlasRegion> monsterWalk;

	InputProcessor processor;

	Player player;
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
	boolean monsterGoingLeft;
	
	BitmapFont font;
	ShapeRenderer shapeRenderer;
	
	TextureAtlas atlas;
	TextureAtlas playerAtlas;
	TextureAtlas backgroundAtlas;
	
	public StartScreen getStartScreen()
	{
		return new StartScreen();
	}
	
	@Override
	public void create() {
		
		playerAtlas = new TextureAtlas(Gdx.files.internal("assets/player_images.pack"));
		atlas = new TextureAtlas(Gdx.files.internal("assets/images.pack"));
		backgroundAtlas = new TextureAtlas(Gdx.files.internal("assets/bkgnd.pack"));
		
		player = new Player(playerAtlas);
		
		// find images from pack
		mainBackground = backgroundAtlas.findRegion("Proto_Background");
		lockers = backgroundAtlas.findRegion("Proto_Lockers");
		door = backgroundAtlas.findRegion("Proto_OpenDoor");
		closet_open = atlas.findRegion("open_closet");
		closet_close = atlas.findRegion("close_closet");		
		
		monsterWalk = atlas.findRegions("monsta");

		// initialize speed variables
		monsterWalkTime = 0;
		currentFrame = 0;
		monsterCurrentFrame = 0;
		currentMonsterFrameTime = 0;
		monsterGoingLeft = true;
		timeHiding = 0;
		gameStatus = 1; // NOTE: change this once start screen is made
		
		font = new BitmapFont();
		font.setColor(0,0,0,1);
		
		shapeRenderer = new ShapeRenderer();
		
		batch = new SpriteBatch();

//		setScreen( (Screen)getStartScreen());
		
		// monster
		monster = new Rectangle();
		monster.width = 275;
		monster.height = 200;
		monster.x = 20;
		monster.y = 75;
	
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
		monsterWalkTime += Gdx.graphics.getDeltaTime();
		currentMonsterFrameTime += Gdx.graphics.getDeltaTime();
		timeHiding += Gdx.graphics.getDeltaTime();
		player.update(Gdx.graphics.getDeltaTime());
		
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			if (player.getX() > 390 && player.getX() < 450) {
				if (timeHiding > .25)
				{
					if (player.isHiding()) {
						player.stand();
					} else {
						player.hide();
					}
					timeHiding = 0;
				}
			}
		} else if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			player.push();
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			player.goLeft(Gdx.input.isKeyPressed(Keys.TAB));
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			player.goRight(Gdx.input.isKeyPressed(Keys.TAB));
		} else {
			if (!player.isHiding()) {
				player.stand();
			}
		}
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		// background and objects
		batch.draw(mainBackground, 0, 0, 800, 400);
		batch.draw(lockers, 100, 100, 125, 150);
		batch.draw(door, 700, 98, 85, 165);
		
		// closet
		if (!player.isHiding())
			batch.draw(closet_open, 400, 100, 70, 120);
		else
		{
			batch.draw(closet_close,  410,  100,  50,  120);
			font.draw(batch, "Narnia Discovered!", 370, 100);
		}
		
		// monster
		batch.draw(monsterWalk.get(monsterCurrentFrame), monster.x, monster.y, monster.width, monster.height);
		
		player.draw(batch);
		
		font.draw(batch, Float.toString(player.getStamina()), 100, 50);
		
// batch.draw(monsterStand, 25, 100, 550, 400);
		
		batch.end();
		
		// Draw player Stamina bar
		shapeRenderer.begin(ShapeType.FilledRectangle);
		shapeRenderer.setColor(0, 1, 0, 1);
		shapeRenderer.filledRect(50, 375, player.getStamina(), 20);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Rectangle);
		shapeRenderer.setColor(0, 0, 0, 1);
		shapeRenderer.rect(50, 375, player.STA_MAX, 20);
		shapeRenderer.end();

	}

	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
		
	}

}
