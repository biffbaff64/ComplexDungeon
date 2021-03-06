/*
 *  Copyright 10/11/2018 Red7Projects.
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

package com.red7projects.dungeon;

import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;
import com.red7projects.dungeon.utils.google.RCConstants;
import com.red7projects.dungeon.utils.logging.Trace;

class AchievementsHandler implements OnSuccessListener<Intent>
{
    private final AndroidLauncher androidLauncher;

    AchievementsHandler(AndroidLauncher _androidLauncher)
    {
        this.androidLauncher = _androidLauncher;
    }

    @Override
    public void onSuccess(final Intent intent)
    {
        androidLauncher.startActivityForResult(intent, RCConstants.RC_ACHIEVEMENT_UI.value);
    }

    /**
     * Unlock the specified achievement.
     *
     * @param achievementId - String - The achievement ID.
     */
    void unlockAchievement(final String achievementId)
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);

        if (account != null)
        {
            Trace.__FILE_FUNC(achievementId);

            Games.getAchievementsClient(androidLauncher, account).unlock(achievementId);
        }
    }

    /**
     * Shows the achievements screen.
     */
    void showAchievementScreen()
    {
        Trace.__FILE_FUNC();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);

        if (account != null)
        {
            Games.getAchievementsClient(androidLauncher, account)
                .getAchievementsIntent()
                .addOnSuccessListener(this);
        }
    }
}
