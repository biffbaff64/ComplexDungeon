/*
 *
 *  * *****************************************************************************
 *  *  Copyright 27/03/2017 See AUTHORS file.
 *  *  <p>
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *  <p>
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  * ***************************************************************************
 *
 */

package com.red7projects.dungeon.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
public class Sfx
{
   	public static final int _SILENT                 = 0;
    public static final int _MIN_VOLUME             = 0;
   	public static final int _MAX_VOLUME             = 10;
   	public static final int _VOLUME_INCREMENT       = 1;
   	public static final int _VOLUME_MULTIPLIER      = 10;
   	public static final int _DEFAULT_MUSIC_VOLUME   = 4;
   	public static final int _DEFAULT_FX_VOLUME      = 6;

   	public static final int SFX_LASER             = 0;
	public static final int SFX_PLAZMA            = 1;
	public static final int SFX_EXPLOSION_1       = 2;
	public static final int SFX_EXPLOSION_2       = 3;
	public static final int SFX_EXPLOSION_3       = 4;
	public static final int SFX_EXPLOSION_4       = 5;
	public static final int SFX_EXPLOSION_5       = 6;
	public static final int SFX_THRUST            = 7;
	public static final int SFX_PICKUP            = 8;
	public static final int SFX_TELEPORT          = 9;
	public static final int SFX_EXTRA_LIFE        = 10;
	public static final int SFX_LAUNCH_WARNING    = 11;
    public static final int SFX_TEST_SOUND        = 12;
    public static final int SFX_BEEP              = 13;
	public static final int MAX_SOUND             = 14;

	public static final int MUSIC_TITLE   = 0;
	public static final int MUSIC_HISCORE = 1;
	public static final int MUSIC_GAME    = 2;
	public static final int MAX_TUNES     = 3;

    public Sound[] sounds;
    public Music[] music;

    private int currentTune;
    private int musicVolumeSave;
    private int fxVolumeSave;
    private App     app;
    private boolean soundsLoaded;
    private boolean isTunePaused;

    private static final Sfx instance = new Sfx();

    public static Sfx inst()
    {
        return instance;
    }

    public void setup(App _app)
    {
        app = _app;
        soundsLoaded = false;
        isTunePaused = false;

        sounds = new Sound[MAX_SOUND];
        music = new Music[MAX_TUNES];

        loadSounds();

        if (musicVolumeSave == 0)
        {
            musicVolumeSave = _DEFAULT_MUSIC_VOLUME;
        }

        if (fxVolumeSave == 0)
        {
            fxVolumeSave = _DEFAULT_FX_VOLUME;
        }
    }

    public void update()
    {
        if (soundsLoaded)
        {
            if (AppConfig.gamePaused)
            {
                if ((music[currentTune] != null) && music[currentTune].isPlaying())
                {
                    music[currentTune].pause();
                    isTunePaused = true;
                }
            }
            else
            {
                if ((music[currentTune] != null) && !music[currentTune].isPlaying() && isTunePaused)
                {
                    music[currentTune].play();
                    isTunePaused = false;
                }
            }
        }
    }

    private void loadSounds()
    {
        Trace.__FILE_FUNC();

        sounds[SFX_LASER]           = app.assets.loadSingleAsset("data/sounds/laser.mp3", Sound.class);
        sounds[SFX_PLAZMA]          = app.assets.loadSingleAsset("data/sounds/plazma.mp3", Sound.class);
        sounds[SFX_EXPLOSION_1]     = app.assets.loadSingleAsset("data/sounds/explosion_1.mp3", Sound.class);
        sounds[SFX_EXPLOSION_2]     = app.assets.loadSingleAsset("data/sounds/explosion_2.mp3", Sound.class);
        sounds[SFX_EXPLOSION_3]     = app.assets.loadSingleAsset("data/sounds/explosion_3.mp3", Sound.class);
        sounds[SFX_EXPLOSION_4]     = app.assets.loadSingleAsset("data/sounds/explosion_4.mp3", Sound.class);
        sounds[SFX_EXPLOSION_5]     = app.assets.loadSingleAsset("data/sounds/explosion_5.mp3", Sound.class);
        sounds[SFX_THRUST]          = app.assets.loadSingleAsset("data/sounds/thrust3.mp3", Sound.class);
        sounds[SFX_PICKUP]          = app.assets.loadSingleAsset("data/sounds/pickup.mp3", Sound.class);
        sounds[SFX_TELEPORT]        = app.assets.loadSingleAsset("data/sounds/teleport.mp3", Sound.class);
        sounds[SFX_EXTRA_LIFE]      = app.assets.loadSingleAsset("data/sounds/extra_life.mp3", Sound.class);
        sounds[SFX_LAUNCH_WARNING]  = app.assets.loadSingleAsset("data/sounds/teleport.mp3", Sound.class);
        sounds[SFX_TEST_SOUND]      = app.assets.loadSingleAsset("data/sounds/teleport.mp3", Sound.class);
        sounds[SFX_BEEP]            = app.assets.loadSingleAsset("data/sounds/pickup.mp3", Sound.class);

        music[MUSIC_TITLE]   = app.assets.loadSingleAsset("data/sounds/loseme2.mp3", Music.class);
        music[MUSIC_HISCORE] = app.assets.loadSingleAsset("data/sounds/breath.mp3", Music.class);
        music[MUSIC_GAME]    = app.assets.loadSingleAsset("data/sounds/fear_mon.mp3", Music.class);

        soundsLoaded = true;
    }

    private float getUsableVolume(int volume)
    {
//        volume /= _VOLUME_MULTIPLIER;
//
//        if (volume >= _MAX_VOLUME)
//        {
//            volume = _MAX_VOLUME;
//        }
//
//        return volumes[volume];

        return volume;
    }

    /**
     * Play or Stop the Main Game tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    public void playGameTune(boolean playTune)
    {
        if (playTune)
        {
            startTune(MUSIC_GAME, getMusicVolume(), true);
        }
        else
        {
            tuneStop();
        }
    }

    /**
     * Play or Stop the Main Title tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    public void playTitleTune(boolean playTune)
    {
        if (playTune)
        {
            startTune(MUSIC_TITLE, getMusicVolume(), true);
        }
        else
        {
            tuneStop();
        }
    }

    /**
     * Play or Stop the HiScore name entry tune.
     * This tune is played on the nname entry screen only,
     * NOT when the hiscore table is displayed in
     * the titles screen sequence.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    public void playHiScoreTune(boolean playTune)
    {
        if (playTune)
        {
            startTune(MUSIC_HISCORE, getMusicVolume(), true);
        }
        else
        {
            tuneStop();
        }
    }

    public void startTune(int musicNumber, int volume, boolean looping)
    {
        if (soundsLoaded)
        {
            if (getMusicVolume() > 0)
            {
                if (app.settings.isEnabled(Settings._MUSIC_ENABLED) && (music != null) && !music[musicNumber].isPlaying())
                {
                    music[musicNumber].setLooping(looping);
                    music[musicNumber].setVolume(getUsableVolume(volume));
                    music[musicNumber].play();

                    currentTune = musicNumber;
                }
            }
        }
    }

    public long startSound(int soundNumber)
    {
        long id = 0;

        if (app.settings.isEnabled(Settings._SOUNDS_ENABLED) && soundsLoaded)
        {
            if (getFXVolume() > 0)
            {
                if (sounds[soundNumber] != null)
                {
                    id = sounds[soundNumber].play(getUsableVolume(getFXVolume()));
                }
            }
        }

        return id;
    }

    public void tuneStop()
    {
        if (soundsLoaded)
        {
            if ((music[currentTune] != null) && music[currentTune].isPlaying())
            {
                music[currentTune].stop();
            }
        }
    }

    public void setMusicVolume(int volume)
    {
        if (music[currentTune] != null)
        {
            music[currentTune].setVolume(getUsableVolume(volume));
        }

        app.settings.prefs.putInteger(Settings._MUSIC_VOLUME, volume);
        app.settings.prefs.flush();
    }

    public void setFXVolume(int volume)
    {
        app.settings.prefs.putInteger(Settings._FX_VOLUME, volume);
        app.settings.prefs.flush();
    }

    public int getMusicVolume()
    {
        return app.settings.prefs.getInteger(Settings._MUSIC_VOLUME);
    }

    public int getFXVolume()
    {
        return app.settings.prefs.getInteger(Settings._FX_VOLUME);
    }

    public float getUsableFxVolume()
    {
        return getFXVolume();
    }

    public void saveMusicVolume()
    {
        musicVolumeSave = getMusicVolume();
    }

    public void saveFXVolume()
    {
        fxVolumeSave = getFXVolume();
    }

    public int getMusicVolumeSave()
    {
        return musicVolumeSave;
    }

    public int getFXVolumeSave()
    {
        return fxVolumeSave;
    }

    public Sound[] getSoundsTable()
    {
        return sounds;
    }

    public Music[] getMusicTable()
    {
        return music;
    }

    public boolean isTunePlaying()
    {
        return soundsLoaded && music[currentTune].isPlaying();
    }

    public boolean isTunePlaying(int _tune)
    {
        return soundsLoaded && music[_tune].isPlaying();
    }

    public void clearUp()
    {
        app = null;
        sounds = null;
        music = null;
    }
}
