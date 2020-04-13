/*
 *  Copyright 24/04/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.red7projects.dungeon.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.utils.logging.Trace;

// TODO: 05/01/2019 - Investigate storing preferences & settings on the players Google Drive

@SuppressWarnings({"WeakerAccess"})
public class Preferences implements Disposable
{
    //
    // Standard defaults that all projects will use
    public static final boolean _PREF_FALSE_DEFAULT = false;
    public static final boolean _PREF_TRUE_DEFAULT  = true;

    //
    // Development options
    public static final String _DEV_MODE            = "dev mode";           // Enables/Disables DEV Mode
    public static final String _GOD_MODE            = "god mode";           //
    public static final String _FORCE_PREFS_RESET   = "force prefs reset";  // Forces reset of ALL app.preferences.on game start
    public static final String _ANDROID_ON_DESKTOP  = "android on desktop"; // Enables emulation of Android build on desktop
    public static final String _USING_ASHLEY_ECS    = "ashley ecs";         // Enables use of Ashley Entity Component System

    public static final String _DISABLE_ENEMIES     = "disable enemies";    // Disables all enemy entities
    public static final String _PROGRESS_BARS       = "progress bars";      // Enables/Disables progress bars on HUD
    public static final String _ANIM_TILES          = "animating tiles";    // Enables/Disables animating game tiles
    public static final String _SCROLL_DEMO         = "scroll demo";        // Enables Game Scroll Demo mode
    public static final String _TEST_LEVEL          = "test level";         //

    public static final String _SPRITE_BOXES        = "sprite boxes";       // Shows sprite AABB Boxes
    public static final String _TILE_BOXES          = "tile boxes";         // Shows game tile AABB Boxes
    public static final String _BUTTON_BOXES        = "button boxes";       // Shows GameButton bounding boxes
    public static final String _SHOW_FPS            = "show fps";           // Shows current FPS on-screen
    public static final String _SHOW_DEBUG          = "show debug";         // Enables on-screen debug printing
    public static final String _SHOW_TRACE          = "game trace";         // Enables Trace output to Logcat
    public static final String _SPAWNPOINTS         = "spawn points";       // Shows spawn point tiles from game map
    public static final String _MAP_WINDOW          = "map window";         // Shows the map window outline
    public static final String _MENU_HEAPS          = "menu heaps";         // Show Heap Sizes on Menu Page if true
    public static final String _DISABLE_MENU_SCREEN = "disable menu";       //

    public static final String _CULL_SPRITES        = "cull sprites";       // Enables Sprite Culling when off screen
    public static final String _SHADER_PROGRAM      = "shader program";     // Enables/Disables global shader program
    public static final String _USING_BOX2D         = "using box2d";        // Enables Box2D Physics
    public static final String _B2D_RENDERER        = "b2d renderer";       // Enables/Disables the Box2D Debug Renderer
    public static final String _GL_PROFILER         = "gl profiler";        // Enables/Disables the LibGdx OpenGL Profiler

    //
    // Game settings
    public static final String _INSTALLED           = "installed";          //
    public static final String _SHOW_HINTS          = "show hints";         // Enables/Disables In-Game Hints
    public static final String _VIBRATIONS          = "vibrations";         // Enables/Disables device vibrations
    public static final String _MUSIC_ENABLED       = "music enabled";      // Enables/Disables Music
    public static final String _SOUNDS_ENABLED      = "sound enabled";      // Enables/Disables Sound FX
    public static final String _FX_VOLUME           = "fx volume";          // Current Sound FX Volume
    public static final String _MUSIC_VOLUME        = "music volume";       // Current Music Volume
    public static final String _PLAY_SERVICES       = "play services";      // Enables Google Play Services
    public static final String _ACHIEVEMENTS        = "achievements";       // Enables In-Game Achievements
    public static final String _CHALLENGES          = "challenges";         // Enables In-Game challenges
    public static final String _SIGN_IN_STATUS      = "sign in status";     // Google Services sign in status
    public static final String _BOX2D_PHYSICS       = "using box2d";        //

    //
    // Default controller is On-Screen Virtual Joystick. This can be turned off
    // and a wireless/connected Android compatible controller used instead
    public static final String _ON_SCREEN_CONTROLLER    = "joystick state";    // On Screen Joystick and button controls
    public static final String _EXTERNAL_CONTROLLER     = "game controllers";  // External Game Controller devices
    public static final String _KEYBOARD_CONTROL        = "keyboard control";  // Keyboard control only
    public static final String _MOUSE_CONTROL           = "mouse control";     // Keyboard control only
    public static final String _SHOW_GAME_BUTTONS       = "show game buttons"; // Enables/Disables showing of on-screen buttons and joystick

    //
    // The Hero...
    public static final String _PLAYER                  = "player";
    public static final String _LJM_VIEW_WINDOW         = "view window";

    //
    // Other Main characters
    public static final String _PRISONER                = "prisoner";
    public static final String _VILLAGER                = "villager";

    //
    // Interactives
    public static final String _TELEPORTER              = "teleporter";
    public static final String _PICKUPS                 = "pickups";
    public static final String _FLOATING_PLATFORM       = "floating platform";
    public static final String _DOORS                   = "doors";
    public static final String _MYSTERY_CHEST           = "mystery chest";

    //
    // Moving Entities
    public static final String _STORM_DEMON             = "storm demon";
    public static final String _BOUNCER                 = "bouncer";
    public static final String _JELLY_MONSTER           = "jelly monster";
    public static final String _SPIKE_BALL              = "spike ball";
    public static final String _SPIKE_BLOCK             = "spike block";
    public static final String _SCORPION                = "scorpion";
    public static final String _BEETLE                  = "beetle";
    public static final String _SOLDIER                 = "soldier";

    //
    // Stationary Entities
    public static final String _LASER_DOOR              = "laser door";
    public static final String _FLAME_THROWER           = "flame thrower";
    public static final String _TURRETS                 = "turrets";
    public static final String _DECORATIONS             = "decorations";

    public com.badlogic.gdx.Preferences prefs;

    public Preferences()
    {
        Trace.__FILE_FUNC();
    }

    public void setup(final String fileName)
    {
        Trace.__FILE_FUNC();

        try
        {
            prefs = Gdx.app.getPreferences(fileName);
        }
        catch (Exception e)
        {
            Trace.__FILE_FUNC();
            Trace.dbg(e.getMessage());
        }

        //
        // Usually used in development, but may be
        // used in release mode possibly...
        if (prefs.getBoolean(_FORCE_PREFS_RESET))
        {
            setPrefsToDefault();
        }
    }

    public void setPrefsToDefault()
    {
        Trace.__FILE_FUNC();

        prefs.putBoolean(_DEV_MODE,             _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_GOD_MODE,             _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_FORCE_PREFS_RESET,    _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_ANDROID_ON_DESKTOP,   _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_USING_ASHLEY_ECS,     _PREF_FALSE_DEFAULT);

        prefs.putBoolean(_DISABLE_ENEMIES,      _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_PROGRESS_BARS,        _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_ANIM_TILES,           _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_SCROLL_DEMO,          _PREF_FALSE_DEFAULT);

        prefs.putBoolean(_SPRITE_BOXES,         _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_TILE_BOXES,           _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_BUTTON_BOXES,         _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_SHOW_FPS,             _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_SHOW_DEBUG,           _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_SHOW_TRACE,           _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_SPAWNPOINTS,          _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_MAP_WINDOW,           _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_MENU_HEAPS,           _PREF_FALSE_DEFAULT);

        prefs.putBoolean(_CULL_SPRITES,         _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_SHADER_PROGRAM,       _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_USING_BOX2D,          _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_B2D_RENDERER,         _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_GL_PROFILER,          _PREF_FALSE_DEFAULT);

        prefs.putBoolean(_INSTALLED,            _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_SHOW_HINTS,           _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_VIBRATIONS,           _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_MUSIC_ENABLED,        _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_SOUNDS_ENABLED,       _PREF_TRUE_DEFAULT);
        prefs.putInteger(_FX_VOLUME,            Sfx.inst()._DEFAULT_FX_VOLUME);
        prefs.putInteger(_MUSIC_VOLUME,         Sfx.inst()._DEFAULT_MUSIC_VOLUME);
        prefs.putBoolean(_PLAY_SERVICES,        _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_ACHIEVEMENTS,         _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_CHALLENGES,           _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_SIGN_IN_STATUS,       _PREF_FALSE_DEFAULT);

        prefs.putBoolean(_KEYBOARD_CONTROL,     _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_ON_SCREEN_CONTROLLER, _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_EXTERNAL_CONTROLLER,  _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_MOUSE_CONTROL,        _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_SHOW_GAME_BUTTONS,    _PREF_FALSE_DEFAULT);

        //----------- Achievements -----------

        // ---------- Development Flags ----------
        prefs.putBoolean(_TEST_LEVEL,           _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_LJM_VIEW_WINDOW,      _PREF_FALSE_DEFAULT);
        prefs.putBoolean(_DISABLE_MENU_SCREEN,  _PREF_FALSE_DEFAULT);

        // ---------- Entities (including player) ----------
        prefs.putBoolean(_PLAYER,               _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_PRISONER,             _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_VILLAGER,             _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_LASER_DOOR,           _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_FLAME_THROWER,        _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_STORM_DEMON,          _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_JELLY_MONSTER,        _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_BOUNCER,              _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_SPIKE_BALL,           _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_SPIKE_BLOCK,          _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_SCORPION,             _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_BEETLE,               _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_SOLDIER,              _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_TURRETS,              _PREF_TRUE_DEFAULT);

        // ----------
        prefs.putBoolean(_MYSTERY_CHEST,        _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_TELEPORTER,           _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_PICKUPS,              _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_FLOATING_PLATFORM,    _PREF_TRUE_DEFAULT);
        prefs.putBoolean(_DOORS,                _PREF_TRUE_DEFAULT);

        prefs.flush();
    }

    public boolean isEnabled(final String preference)
    {
        return prefs.getBoolean(preference);
    }

    public void enable(final String preference)
    {
        prefs.putBoolean(preference, true);
        prefs.flush();
    }

    public void disable(final String preference)
    {
        prefs.putBoolean(preference, false);
        prefs.flush();
    }

    @Override
    public void dispose()
    {
        prefs = null;
    }
}
