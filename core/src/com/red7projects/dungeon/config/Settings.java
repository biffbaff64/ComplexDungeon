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
import com.red7projects.dungeon.logging.Trace;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class Settings
{
    private static final String[][] preferencesList =
        {
            // Configuration Options
            {"music enabled",       "true", "true"},
            {"sound enabled",       "true", "true"},
            {"fx volume",           "true", "true"},
            {"music volume",        "true", "true"},
            {"play services",       "true", "true"},
            {"achievements",        "true", "true"},
            {"challenges",          "true", "true"},
            {"sign in status",      "true", "true"},
            {"using box2d",         "true", "true"},
            {"ashley ecs",          "true", "true"},
            {"shader program",      "true", "true"},
            {"b2d renderer",        "true", "true"},
            {"gl profiler",         "true", "true"},
            {"installed",           "true", "true"},
            {"show hints",          "true", "true"},
            {"vibrations",          "true", "true"},
            {"joystick state",      "true", "true"},
            {"game controllers",    "true", "true"},
            {"controller pos",      "true", "true"},
            {"cull sprites",        "true", "true"},
            {"progress bars",       "true", "true"},
            {"animating tiles",     "true", "true"},
            {"show game buttons",   "true", "true"},

            // Development Configuration Options
            {"dev mode",            "false", "false"},
            {"god mode",            "false", "false"},
            {"force prefs reset",   "false", "false"},
            {"android on desktop",  "false", "false"},
            {"disable enemies",     "false", "false"},
            {"scroll demo",         "false", "false"},
            {"test level",          "false", "false"},
            {"sprite boxes",        "false", "false"},
            {"tile boxes",          "false", "false"},
            {"button boxes",        "false", "false"},
            {"show fps",            "false", "false"},
            {"show debug",          "false", "false"},
            {"game trace",          "false", "false"},
            {"spawn points",        "false", "false"},
            {"map window",          "false", "false"},
            {"menu heaps",          "false", "false"},
            {"disable menu",        "false", "false"},
            {"view window",         "false", "false"},

            // Main characters
            {"player",              "true", "true"},
            {"prisoner",            "true", "true"},
            {"villager",            "true", "true"},

            // Interactive Items and Decorations
            {"pickups",             "true", "true"},
            {"floating platform",   "true", "true"},
            {"doors",               "true", "true"},
            {"mystery chest",       "true", "true"},
            {"teleporter",          "true", "true"},

            // Mobile enemies
            {"soldier",             "true", "true"},
            {"blue mine",           "true", "true"},
            {"bouncer",             "true", "true"},
            {"lava ball",           "true", "true"},
            {"fire ball",           "true", "true"},
            {"red mine",            "true", "true"},
            {"spike ball",          "true", "true"},
            {"spike block",         "true", "true"},
            {"scorpion",            "true", "true"},
            {"beetle",              "true", "true"},

            // Static enemies
            {"laser door",          "true", "true"},
            {"flame thrower",       "true", "true"},
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

    public static void prepareSettingsJsonFile()
    {
        for (int i=0; i<preferencesList.length; i++)
        {
            options[i]              = new GameOption();
            options[i].prefName     = preferencesList[i][0];
            options[i].value        = preferencesList[i][1];
            options[i].defaultValue = preferencesList[i][2];
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
