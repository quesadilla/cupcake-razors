package com.horrorgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartScreen implements Screen{
	
	private Texture mainScreen;
	private TextureRegion mainScreenRegion;
	SpriteBatch batch;
	
	// create constructor here?
	
	@Override
	public void show()
	{	
		// load the image and create the texture region
		mainScreen = new Texture("horrorgameatlas.png");
		
		// image begins at (0,0) and has dimensions 800x400
		mainScreenRegion = new TextureRegion(mainScreen, 0, 0, 800, 400);
	}
	
	@Override
	public void render(float delta)
	{
	
		// NOTE: current issue, tutorial says their batch is started elsewhere
		// so should I make a new SpriteBatch here to draw this, or am I missing
		// something?
		
		batch.begin();
		
		// batch draws region from (0,0) with size of screen
		batch.draw( mainScreenRegion, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		batch.end();
	}
	
	@Override
	public void dispose()
	{
		mainScreen.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
}
