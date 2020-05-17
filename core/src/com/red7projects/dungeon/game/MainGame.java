package com.red7projects.dungeon.game;

import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.utils.development.Developer;
import com.red7projects.dungeon.utils.logging.Trace;
import com.red7projects.dungeon.utils.logging.google.DummyAdsController;
import com.red7projects.dungeon.utils.logging.google.PlayServices;

// @formatter:off
public class MainGame extends App
{
	/**
	 * Instantiates a new Main game.
	 */
	public MainGame(PlayServices _services)
	{
		super();

		this.googleServices	= _services;
        this.adsController	= new DummyAdsController();

        AppConfig.isShowingSplashScreen	= false;
	}

	@Override
	public void create()
	{
		//
		// Initialise all essential objects required before
        // the main screen is initialised.
        //
		Startup startup = new Startup(this);
		startup.startApp();
		startup.close();

		Trace.divider();
	}

	/**
	 * Handles window resizing
	 * @param width     The new window width
	 * @param height    The new window height
	 */
	@Override
	public void resize(int width, int height)
	{
		super.resize(width, height);
	}

	/**
	 * Pause the app
	 */
	@Override
	public void pause()
	{
		if (!Developer.isDevMode()
			&& (appState != null)
			&& (appState.equalTo(StateID._STATE_GAME)))
		{
			hud.buttonPause.press();
		}
	}

	/**
	 * Actions to perform on leaving Pause
	 */
	@Override
	public void resume()
	{
		if (!Developer.isDevMode()
			&& (appState != null)
			&& (appState.equalTo(StateID._STATE_GAME)))
		{
			hud.buttonPause.release();
		}
	}
}
