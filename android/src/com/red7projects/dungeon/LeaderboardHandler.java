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
import com.google.android.gms.tasks.OnSuccessListener;
import com.red7projects.dungeon.utils.google.RCConstants;

class LeaderboardHandler implements OnSuccessListener<Intent>
{
    private final AndroidLauncher androidLauncher;

    LeaderboardHandler(AndroidLauncher _androidLauncher)
    {
        this.androidLauncher = _androidLauncher;
    }

    @Override
    public void onSuccess(final Intent intent)
    {
        androidLauncher.startActivityForResult(intent, RCConstants.RC_LEADERBOARD_UI.value);
    }

    /**
     * Submit score and level achieved.
     *
     * @param score - int - The score.
     * @param level - int - The level.
     */
    void submitScore(int score, int level)
    {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);
//
//        if (account != null)
//        {
//            Trace.__FILE_FUNC(androidLauncher.getString(R.string.leaderboard_leaderboard_tester) + ": " + score);
//
//            Games.getLeaderboardsClient(androidLauncher, account)
//            .submitScore(androidLauncher.getString(R.string.leaderboard_leaderboard_tester), score);
//        }
    }

    /**
     * Shows the global Leaderboard.
     */
    void showLeaderBoard()
    {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);
//
//        if (account != null)
//        {
//            Games.getLeaderboardsClient(androidLauncher, account)
//            .getLeaderboardIntent(androidLauncher.getString(R.string.leaderboard_leaderboard))
//            .addOnSuccessListener(this);
//        }
    }
}
