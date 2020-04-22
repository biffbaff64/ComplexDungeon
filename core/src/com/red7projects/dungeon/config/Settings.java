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
import com.red7projects.dungeon.utils.logging.Trace;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class Settings
{
    private static final GameOption[] preferencesList =
        {
            // Development Configuration Options
            new GameOption("dev mode",            false, false),
            new GameOption("god mode",            false, false),
            new GameOption("ashley ecs",          true,  true),
            new GameOption("disable enemies",     false, false),
            new GameOption("scroll demo",         false, false),
            new GameOption("sprite boxes",        false, false),
            new GameOption("tile boxes",          false, false),
            new GameOption("button boxes",        false, false),
            new GameOption("show fps",            false, false),
            new GameOption("show debug",          false, false),
            new GameOption("game trace",          false, false),
            new GameOption("spawn points",        false, false),
            new GameOption("menu heaps",          false, false),
            new GameOption("disable menu",        false, false),
            new GameOption("cull sprites",        true,  true),
            new GameOption("shader program",      true,  true),
            new GameOption("using box2d",         true,  true),
            new GameOption("b2d renderer",        true,  true),
            new GameOption("gl profiler",         true,  true),

            // Configuration Options
            new GameOption("installed",           true,  true),
            new GameOption("show hints",          true,  true),
            new GameOption("vibrations",          true,  true),
            new GameOption("music enabled",       true,  true),
            new GameOption("sound enabled",       true,  true),
            new GameOption("fx volume",           true,  true),
            new GameOption("music volume",        true,  true),
            new GameOption("play services",       true,  true),
            new GameOption("achievements",        true,  true),
            new GameOption("challenges",          true,  true),
            new GameOption("sign in status",      true,  true),

            // Main characters
            new GameOption("player",              true,  true),
            new GameOption("prisoner",            true,  true),
            new GameOption("villager",            true,  true),

            // Interactive Items and Decorations
            new GameOption("teleporter",          true,  true),
            new GameOption("pickups",             true,  true),
            new GameOption("mystery chest",       true,  true),

            // Mobile enemies
            new GameOption("storm demon",         true,  true),
            new GameOption("bouncer",             true,  true),
            new GameOption("spike ball",          true,  true),
            new GameOption("spike block",         true,  true),
            new GameOption("scorpion",            true,  true),
            new GameOption("soldier",             true,  true),

            // Static enemies
            new GameOption("flame thrower",       true,  true),
            new GameOption("turrets",             true,  true),
        };

    private static final String _SETTINGS_FILENAME = "game_settings.json";

    private static GameOption[] options;
    private static Json         json;

    public static void initialise()
    {
        options = new GameOption[preferencesList.length];
        json    = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        load();
    }

    public static void setOption(String _prefname, boolean _state, boolean _write)
    {
        for (final GameOption option : options)
        {
            if (_prefname.equals(option.prefName))
            {
                option.state = _state;
            }
        }

        if (_write)
        {
            write();
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
        for (int i=0; i<preferencesList.length; i++)
        {
            options[i].state = options[i].defaultState;
        }
    }

    public static void prepareSettingsJsonFile()
    {
        Trace.__FILE_FUNC();

        for (int i=0; i<preferencesList.length; i++)
        {
            options[i]              = new GameOption();
            options[i].prefName     = preferencesList[i].prefName;
            options[i].state        = preferencesList[i].state;
            options[i].defaultState = preferencesList[i].defaultState;
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
