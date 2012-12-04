package com.horrorgame;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class HorrorDesktopStarter {
	public static void main(String[] args)
	{
		
		new JoglApplication(new Game(), 
							"title", 800, 400, false);
	}
}
