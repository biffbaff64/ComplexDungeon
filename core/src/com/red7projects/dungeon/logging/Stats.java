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

package com.red7projects.dungeon.logging;

import com.badlogic.gdx.Gdx;

@SuppressWarnings("unused")
public class Stats
{
    private static com.badlogic.gdx.Preferences prefs;

    public static void setup()
    {
        Trace.__FILE_FUNC();

        prefs = Gdx.app.getPreferences("com.red7projects.gdx7lib.preferences");
    }

    public static void setMeter(int meter, int amount)
    {
        if (prefs != null)
        {
            prefs.putInteger(Meters.fromValue(meter).name(), amount);
            prefs.flush();
        }
    }

    public static void addToMeter(int meter, int amount)
    {
        if (prefs != null)
        {
            prefs.putInteger
                    (
                            Meters.fromValue(meter).name(),
                            (prefs.getInteger(Meters.fromValue(meter).name(), 0) + amount)
                    );

            prefs.flush();
        }
    }

    public static void decMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger
                    (
                            Meters.fromValue(meter).name(),
                            (prefs.getInteger(Meters.fromValue(meter).name(), 0) - 1)
                    );

            prefs.flush();
        }
    }

    public static void incMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger
                    (
                            Meters.fromValue(meter).name(),
                            (prefs.getInteger(Meters.fromValue(meter).name(), 0) + 1)
                    );

            prefs.flush();
        }
    }

    public static void clearMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger(Meters.fromValue(meter).name(), 0);
            prefs.flush();
        }
    }

    public static int getMeter(int meter)
    {
        return (prefs == null) ? 0 : prefs.getInteger(Meters.fromValue(meter).name(), 0);
    }

    public static void resetAllMeters()
    {
        if (prefs != null)
        {
            int end = Meters._MAX_STATS_METERS.get();

            for (int i = 0; i < end; i++)
            {
                prefs.putInteger(Meters.fromValue(i).name(), 0);
            }

            prefs.flush();
        }
    }
}
