package com.horrorgame;

import java.util.ArrayList;
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

public class Game implements ApplicationListener {

	// screen width and height
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 400;

	// Constants for game
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

	Player player;
	List<Monster> monsters;

	int gameStatus;
	float timeHiding;
	
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
		
		monsters = new ArrayList<Monster>();
		monsters.add(new Monster(atlas, Monster.AI.PACER, 500, 75, false));
		
		// find images from pack
		mainBackground = backgroundAtlas.findRegion("Proto_Background");
		lockers = backgroundAtlas.findRegion("Proto_Lockers");
		door = backgroundAtlas.findRegion("Proto_OpenDoor");
		closet_open = atlas.findRegion("open_closet");
		closet_close = atlas.findRegion("close_closet");		

		// initialize speed variables
		timeHiding = 0;
		gameStatus = 1; // NOTE: change this once start screen is made
		
		font = new BitmapFont();
		font.setColor(0,0,0,1);
		
		shapeRenderer = new ShapeRenderer();
		
		batch = new SpriteBatch();

//		setScreen( (Screen)getStartScreen());
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
		
		
		// keeps track of player and monster walking time
		timeHiding += Gdx.graphics.getDeltaTime();
		player.update(Gdx.graphics.getDeltaTime());
		for (int i = 0; i < monsters.size(); i++) {
			monsters.get(i).update(Gdx.graphics.getDeltaTime(), player);
		}
		
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
			player.push(monsters);
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
		
		for (int i = 0; i < monsters.size(); i++) {
			monsters.get(i).draw(batch);
		}
		
		player.draw(batch);
		
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
