package com.horrorgame;

import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Input.Keys;

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
	
	// things to draw
	SpriteBatch batch;
	Texture texture;
	TextureRegion closet_open;
	TextureRegion closet_close;
	TextureRegion playerStand;
	TextureRegion playerPush;
	List<AtlasRegion> playerWalk;
	InputProcessor processor;
	Rectangle player;
	int currentFrame;
	int currSpeed;
	float frameTime;
	float timeHiding;
	boolean goingLeft;
	boolean hiding;
	BitmapFont font;
	
	
	TextureAtlas atlas;
	
	@Override
	public void create() {
//		Gdx.app.log("HorrorGameLibGDXDesktop", "Game.create()");
		
		atlas = new TextureAtlas(Gdx.files.internal("assets/assets_horror.pack"));
		
		closet_open = atlas.findRegion("open_closet");
		closet_close = atlas.findRegion("close_closet");
		playerPush = atlas.findRegion("push");
		playerStand = atlas.findRegion("stand");
		playerWalk = atlas.findRegions("walk");
		frameTime = 0;
		currentFrame = 0;
		goingLeft = false;
		timeHiding = 0;
		hiding = false;
		currSpeed = 1;
		
		font = new BitmapFont();
		font.setColor(0,0,0,1);
		
		//texture = new Texture(Gdx.files.internal("assets/limbo.png"));
		batch = new SpriteBatch();

		player = new Rectangle();
		player.width = 30;
		player.height = 120;
		player.x = 100;
		player.y = 100;
		
//		processor = new InputProcessor();
	/*	
		batcher = new SpriteBatch();
		pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
		texture = new Texture(pixmap);
		
		smiley_pos = new Vector2(SCREEN_WIDTH/2 - 256/2, SCREEN_HEIGHT/2 - 256/2);
		
		DrawSmiley();
		*/
		
	}
/*
	private void DrawSmiley()
	{
		Gdx.app.log("MyLibGDXGame", "Game.DrawSmiley()");
		
		pixmap.setColor(1, 1, 0, 1);
		pixmap.fillCircle(256/2, 256/2, 256/2);
		
		pixmap.setColor(0, 0, 0, 1);
		pixmap.fillCircle(256/2, 140, 80);
		
		pixmap.setColor(1, 1, 0, 1);
		pixmap.fillCircle(256/2, 100, 100);
		
		pixmap.setColor(0, 0, 0, 1);
		pixmap.fillCircle(256/3, 100, 30);
		pixmap.fillCircle(256-256/3, 100, 30);
		
		texture.draw(pixmap, 0, 0);
		texture.bind();
		
	}
	*/
	
	@Override
	public void dispose() {
//		Gdx.app.log("HorrorGameLibGDXDesktop", "Game.dispose()");
		
/*		texture.dispose();
		pixmap.dispose();
		batcher.dispose();
*/
	}

	@Override
	public void pause() {
//		Gdx.app.log("HorrorGameLibGDXDesktop", "Game.pause()");		
	}

	@Override
	public void render() {
		
		if(Gdx.input.isKeyPressed(Keys.LEFT) && !hiding)
		{
			if (!goingLeft) {
				playerPush.flip(true, false);
				currSpeed = 1;
			}
			player.x -= currSpeed;
			goingLeft = true;
		}
			
		if(Gdx.input.isKeyPressed(Keys.RIGHT) && !hiding)
		{
			if (goingLeft) {
				playerPush.flip(true, false);
				currSpeed = 1;
			}
			player.x += currSpeed;
			goingLeft = false;
		}
		
		timeHiding += Gdx.graphics.getDeltaTime();
		
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
		
		if (!hiding) {
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			batch.draw(playerPush, player.x, player.y, player.width, player.height);
		} else if (!Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)) {
			currentFrame = 0;
			batch.draw(playerStand, player.x, player.y, player.width, player.height);
		} else {
			frameTime += Gdx.graphics.getDeltaTime();
			if (frameTime > .25) {
				frameTime = 0;
				currentFrame++;
				if (currentFrame > 3) {
					currentFrame = 0;
					if (currSpeed < 3) {
						currSpeed++;
					}
				}
			}
			batch.draw(playerWalk.get(currentFrame), player.x, player.y, player.width, player.height);
		}
		}
		batch.end();

		
		
		
		/*		float deltaTime=Gdx.graphics.getDeltaTime();
		
		GLCommon gl = Gdx.gl;
		gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT | GL10.GL_STENCIL_BUFFER_BIT);
	
		batcher.begin();
		batcher.draw(texture, smiley_pos.x, smiley_pos.y);
		batcher.end();
		
		if(200*deltaTime<4)
			smiley_pos.x+=200*deltaTime;
		
		else
			smiley_pos.x+=4;
		
		if(smiley_pos.x>SCREEN_WIDTH)
			smiley_pos.x=-256;
	*/	
	}

	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
//		Gdx.app.log("HorrorGameLibGDXDesktop", "Game.resume()");
		
	}

}
