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

import com.red7projects.dungeon.game.App;

public interface PlayServices
{
    void setup(App _app);

    void createApiClient();

    void signIn();

    void signInSilently();

    void signOut();

    boolean isSignedIn();

    boolean isEnabled();

    void submitScore(int score, int level);

    void unlockAchievement(String achievementId);

    void showAchievementScreen();

    void showLeaderboard();
}
