/*
 *  Copyright 28/11/2018 Red7Projects.
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

package com.red7projects.dungeon.utils.development;

import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.game.App;

public abstract class Developer
{
    private static boolean _DEVMODE = false;
    private static boolean _LAPTOP = false;
    private static App app;

    /**
     * Set _DEVMODE from the _DEV_MODE Environment variable.
     * Set _LAPTOP from the _MACHINE Environment variable.
     */
    public static void setMode(App _app)
    {
        app = _app;

        if (AppConfig.isDesktopApp())
        {
            _DEVMODE = "TRUE".equals(System.getenv("_DEV_MODE").toUpperCase());
            _LAPTOP  = "TRUE".equals(System.getenv("_USING_LAPTOP"));
        }

        if (AppConfig.isAndroidApp())
        {
            _DEVMODE = false;
            _LAPTOP = false;
        }
    }

    public static boolean isDevMode()
    {
        return _DEVMODE;
    }

    public static boolean isGodMode()
    {
        return app.settings.isEnabled(Settings._GOD_MODE);
    }

    public static boolean isLaptop()
    {
        return _LAPTOP;
    }
}
