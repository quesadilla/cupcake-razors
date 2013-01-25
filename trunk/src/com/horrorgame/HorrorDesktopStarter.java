package com.horrorgame;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class HorrorDesktopStarter {
	public static void main(String[] args)
	{
		new JoglApplication(new Game(), 
							"Team Cupcake Razors", 800, 400, true);
	}
}
