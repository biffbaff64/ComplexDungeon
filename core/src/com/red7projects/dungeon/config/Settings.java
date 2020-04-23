/*
 *  Copyright 06/02/2019 Red7Projects.
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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.red7projects.dungeon.game.Sfx;
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings({"WeakerAccess", "BooleanMethodIsAlwaysInverted"})
//@formatter:off
public class Settings
{
    //
    // Defaults
    public static final String _DEFAULT_ON  = "default on";
    public static final String _DEFAULT_OFF = "default off";

    //
    // Development options
    public static final String _DEV_MODE            = "dev mode";           // Enables/Disables DEV Mode
    public static final String _GOD_MODE            = "god mode";           //
    public static final String _USING_ASHLEY_ECS    = "ashley ecs";         // Enables use of Ashley Entity Component System
    public static final String _DISABLE_ENEMIES     = "disable enemies";    // Disables all enemy entities
    public static final String _SCROLL_DEMO         = "scroll demo";        // Enables Game Scroll Demo mode
    public static final String _SPRITE_BOXES        = "sprite boxes";       // Shows sprite AABB Boxes
    public static final String _TILE_BOXES          = "tile boxes";         // Shows game tile AABB Boxes
    public static final String _BUTTON_BOXES        = "button boxes";       // Shows GameButton bounding boxes
    public static final String _SHOW_FPS            = "show fps";           // Shows current FPS on-screen
    public static final String _SHOW_DEBUG          = "show debug";         // Enables on-screen debug printing
    public static final String _SPAWNPOINTS         = "spawn points";       // Shows spawn point tiles from game map
    public static final String _MENU_HEAPS          = "menu heaps";         // Show Heap Sizes on Menu Page if true
    public static final String _DISABLE_MENU_SCREEN = "disable menu";       //
    public static final String _CULL_SPRITES        = "cull sprites";       // Enables Sprite Culling when off screen
    public static final String _SHADER_PROGRAM      = "shader program";     // Enables/Disables global shader program
    public static final String _BOX2D_PHYSICS       = "using box2d";        // Enables Box2D Physics
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
    public static final String _SIGN_IN_STATUS      = "sign in status";     // Google Services sign in status (Android)

    //
    // Main characters
    public static final String _PLAYER              = "player";
    public static final String _PRISONER            = "prisoner";
    public static final String _VILLAGER            = "villager";

    //
    // Interactives
    public static final String _TELEPORTER          = "teleporter";
    public static final String _PICKUPS             = "pickups";
    public static final String _MYSTERY_CHEST       = "mystery chest";

    //
    // Moving Entities
    public static final String _STORM_DEMON         = "storm demon";
    public static final String _BOUNCER             = "bouncer";
    public static final String _SPIKE_BALL          = "spike ball";
    public static final String _SPIKE_BLOCK         = "spike block";
    public static final String _SCORPION            = "scorpion";
    public static final String _SOLDIER             = "soldier";

    //
    // Stationary Entities
    public static final String _FLAME_THROWER       = "flame thrower";
    public static final String _TURRETS             = "turrets";

    private static final GameOption[] optionsList =
        {
            // Defaults
            new GameOption(_DEFAULT_ON,             true, true),
            new GameOption(_DEFAULT_OFF,            true, true),

            // Development Configuration Options
            new GameOption(_DEV_MODE,              false, false),
            new GameOption(_GOD_MODE,              false, false),
            new GameOption(_USING_ASHLEY_ECS,      true,  true),
            new GameOption(_DISABLE_ENEMIES,       false, false),
            new GameOption(_SCROLL_DEMO,           false, false),
            new GameOption(_SPRITE_BOXES,          false, false),
            new GameOption(_TILE_BOXES,            false, false),
            new GameOption(_BUTTON_BOXES,          false, false),
            new GameOption(_SHOW_FPS,              false, false),
            new GameOption(_SHOW_DEBUG,            false, false),
            new GameOption(_SPAWNPOINTS,           false, false),
            new GameOption(_MENU_HEAPS,            false, false),
            new GameOption(_DISABLE_MENU_SCREEN,   false, false),
            new GameOption(_CULL_SPRITES,          true,  true),
            new GameOption(_SHADER_PROGRAM,        true,  true),
            new GameOption(_BOX2D_PHYSICS,         true,  true),
            new GameOption(_B2D_RENDERER,          true,  true),
            new GameOption(_GL_PROFILER,           true,  true),

            // Configuration Options
            new GameOption(_INSTALLED,             true,  true),
            new GameOption(_SHOW_HINTS,            true,  true),
            new GameOption(_VIBRATIONS,            true,  true),
            new GameOption(_MUSIC_ENABLED,         true,  true),
            new GameOption(_SOUNDS_ENABLED,        true,  true),
            new GameOption(_FX_VOLUME,             0,  Sfx.inst()._DEFAULT_FX_VOLUME),
            new GameOption(_MUSIC_VOLUME,          0,  Sfx.inst()._DEFAULT_MUSIC_VOLUME),
            new GameOption(_PLAY_SERVICES,         true,  true),
            new GameOption(_ACHIEVEMENTS,          true,  true),
            new GameOption(_CHALLENGES,            true,  true),
            new GameOption(_SIGN_IN_STATUS,        true,  true),

            // Main characters
            new GameOption(_PLAYER,              true,  true),
            new GameOption(_PRISONER,            true,  true),
            new GameOption(_VILLAGER,            true,  true),

            // Interactive Items and Decorations
            new GameOption(_TELEPORTER,          true,  true),
            new GameOption(_PICKUPS,             true,  true),
            new GameOption(_MYSTERY_CHEST,       true,  true),

            // Mobile enemies
            new GameOption(_STORM_DEMON,         true,  true),
            new GameOption(_BOUNCER,             true,  true),
            new GameOption(_SPIKE_BALL,          true,  true),
            new GameOption(_SPIKE_BLOCK,         true,  true),
            new GameOption(_SCORPION,            true,  true),
            new GameOption(_SOLDIER,             true,  true),

            // Static enemies
            new GameOption(_FLAME_THROWER,       true,  true),
            new GameOption(_TURRETS,             true,  true),
        };

    private static final String _SETTINGS_FILENAME = "game_settings.json";

    private static GameOption[] options;
    private static Json         json;

    public static void initialise()
    {
        options = new GameOption[optionsList.length];
        json    = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        load();
    }

    public static boolean isEnabled(String _prefName)
    {
        return (boolean) getOption(_prefName);
    }

    public static boolean getBoolean(String _prefName)
    {
        return (boolean) getOption(_prefName);
    }

    public static int getInt(String _prefName)
    {
        return (int) getOption(_prefName);
    }

    public static float getFloat(String _prefName)
    {
        return (float) getOption(_prefName);
    }

    public static void putBoolean(String _prefName, boolean _state)
    {
        setOption(_prefName, _state);
    }

    public static void putInt(String _prefName, int _value)
    {
        setOption(_prefName, _value);
    }

    public static void putFloat(String _prefName, float _value)
    {
        setOption(_prefName, _value);
    }

    private static Object getOption(String _prefName)
    {
        Object state = 0;

        for (final GameOption option : options)
        {
            if (_prefName.equals(option.prefName))
            {
                state = option.state;
            }
        }

        return state;
    }

    private static void setOption(String _prefName, Object _state)
    {
        for (final GameOption option : options)
        {
            if (_prefName.equals(option.prefName))
            {
                option.state = _state;
            }
        }
    }

    public static void load()
    {
        if (!Gdx.files.local(_SETTINGS_FILENAME).exists())
        {
            prepareSettingsJsonFile();
        }

        options = json.fromJson(GameOption[].class, Gdx.files.local(_SETTINGS_FILENAME));
    }

    public static void write()
    {
        FileHandle handle = Gdx.files.local(_SETTINGS_FILENAME);

        handle.writeString(json.prettyPrint(json.toJson(options)), false);
    }

    public static void resetToDefaults()
    {
        for (int i = 0; i< optionsList.length; i++)
        {
            options[i].state = options[i].defaultState;
        }
    }

    public static void prepareSettingsJsonFile()
    {
        Trace.__FILE_FUNC();

        for (int i = 0; i< optionsList.length; i++)
        {
            options[i]              = new GameOption();
            options[i].prefName     = optionsList[i].prefName;
            options[i].state        = optionsList[i].state;
            options[i].defaultState = optionsList[i].defaultState;
        }

        write();
    }

    private static void debug()
    {
        for (GameOption option : options)
        {
            Trace.dbg(option.toString());
        }
    }
}
