/*
 *  Copyright 11/11/2018 Red7Projects.
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

public class SavedGamesHandler implements OnSuccessListener<Intent>
{
    private final AndroidLauncher androidLauncher;

    SavedGamesHandler(AndroidLauncher _androidLauncher)
    {
        this.androidLauncher = _androidLauncher;
    }

    @Override
    public void onSuccess(final Intent intent)
    {
        androidLauncher.startActivityForResult(intent, RCConstants.RC_SAVED_GAMES_UI.value);
    }

    void showSavedGamesUI()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);

        if (account != null)
        {
            int maxNumberOfSavedGamesToShow = 5;

            Games.getSnapshotsClient(androidLauncher, account).getSelectSnapshotIntent
                (
                    "Saved Games List",
                    true,
                    true,
                    maxNumberOfSavedGamesToShow
                );
        }
    }
}
