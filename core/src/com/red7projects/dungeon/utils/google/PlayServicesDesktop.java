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

package com.red7projects.dungeon.utils.google;

import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.utils.logging.Trace;

public class PlayServicesDesktop implements PlayServices
{
    private App app;

    @Override
    public void setup(App _app)
    {
        this.app = _app;
    }

    @Override
    public void createApiClient()
    {
        Trace.__FILE_FUNC();
    }

    @Override
    public void signIn()
    {
        if (isEnabled())
        {
            if (!app.settings.isEnabled(Settings._SIGN_IN_STATUS))
            {
                Trace.__FILE_FUNC();

                app.settings.enable(Settings._SIGN_IN_STATUS);
            }
        }
    }

    @Override
    public void signInSilently()
    {
        if (isEnabled())
        {
            Trace.__FILE_FUNC();

            signIn();
        }
    }

    @Override
    public void signOut()
    {
        if (isEnabled())
        {
            Trace.__FILE_FUNC();

            app.settings.disable(Settings._SIGN_IN_STATUS);
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return app.settings.isEnabled(Settings._SIGN_IN_STATUS);
    }

    @Override
    public boolean isEnabled()
    {
        return app.settings.isEnabled(Settings._PLAY_SERVICES);
    }

    @Override
    public void submitScore(int score, int level)
    {
        if (isEnabled())
        {
            Trace.__FILE_FUNC("" + score + ", " + level);
        }
    }

    @Override
    public void unlockAchievement(String achievementId)
    {
    }

    @Override
    public void showAchievementScreen()
    {
        Trace.__FILE_FUNC();
    }

    @Override
    public void showLeaderboard()
    {
        Trace.__FILE_FUNC();
    }
}
