/*
 *  Copyright 30/08/2018 Red7Projects.
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

package com.red7projects.dungeon.game;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.types.Item;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
//@formatter:off
public class GameProgress implements Disposable
{
    public static final int _LIGHTNING  = 0;
    public static final int _FIRE       = 1;
    public static final int _WIND       = 2;
    public static final int _SUN        = 3;
    public static final int _ICE        = 4;
    public static final int _NATURE     = 5;
    public static final int _WATER      = 6;
    public static final int _EARTH      = 7;

    public boolean      isRestarting;           // Player has lost a life. Switch player, or start next life
    public boolean      levelCompleted;         // True if level finished and next level needed.
    public boolean      gameCompleted;          // True if all levels finished.
    public boolean      gameSetupDone;          //
    public boolean      newHiScoreAvailable;    //
    public boolean      playerLifeOver;         //
    public boolean      cloudDemonInView;       //

    public int          playerLevel;            //

    public boolean[] runes;
    public boolean[] books;
    public boolean[] potions;

    public Item score;
    public Item lives;
    public Item gemCount;
    public Item coinCount;
    public Item keyCount;
    public Item rescuedVillagers;

    private final App app;

    public GameProgress(App _app)
    {
        this.app = _app;

        resetProgress();
    }

    public void resetProgress()
    {
        Trace.__FILE_FUNC();

        isRestarting        = false;
        levelCompleted      = false;
        gameCompleted       = false;
        gameSetupDone       = false;
        newHiScoreAvailable = false;
        cloudDemonInView    = false;

        createData();
        toMinimum();
    }

    /**
     * Create player data for the Heads Up Display,
     * data for anything that is displayed on the HUD.
     * (Scores, Lives, Level etc.)
     */
    private void createData()
    {
        Trace.__FILE_FUNC();

        score            = new Item(0, Constants._MAX_SCORE, 0);
        lives            = new Item(0, Constants._MAX_LIVES, Constants._MAX_LIVES);
        gemCount         = new Item(0, Constants._MAX_GEMS);
        coinCount        = new Item(0, Constants._MAX_COINS);
        keyCount         = new Item(0, Constants._MAX_KEYS);
        rescuedVillagers = new Item(0, 0, Constants._MAX_PRISONERS);

        score.setRefillAmount(0);
        lives.setRefillAmount(Constants._MAX_LIVES);

        playerLifeOver = false;
        playerLevel = 1;

        runes   = new boolean[GameAssets._RUNES_FRAMES];
        books   = new boolean[GameAssets._BOOKS_FRAMES];
        potions = new boolean[GameAssets._POTIONS_FRAMES];

        Arrays.fill(runes, false);
        Arrays.fill(books, false);
        Arrays.fill(potions, false);
    }

    public void closeLastGame()
    {
        app.googleServices.submitScore(score.getTotal(), playerLevel);
    }

    public void toMinimum()
    {
        score.setToMinimum();
        lives.setToMinimum();
        gemCount.setToMinimum();
        coinCount.setToMinimum();
        keyCount.setToMinimum();
        rescuedVillagers.setToMinimum();

        Arrays.fill(runes, false);
        Arrays.fill(books, false);
        Arrays.fill(potions, false);

        playerLifeOver = false;
        playerLevel = 1;
    }

    public void addScore(int amount)
    {
        score.add(amount);
    }

    public void addCoins(int amount)
    {
        coinCount.add(amount);
    }

    public void addGems(int amount)
    {
        gemCount.add(amount);
    }

    public void addKeys(int amount)
    {
        keyCount.add(amount);
    }

    public void addVillager(int amount)
    {
        rescuedVillagers.add(amount);
    }

    public Item getScoreOne()
    {
        return score;
    }

    public boolean isGameCompleted()
    {
        return gameCompleted;
    }

    public boolean isLevelCompleted()
    {
        return levelCompleted;
    }

    @Override
    public void dispose()
    {
        score            = null;
        lives            = null;
        gemCount         = null;
        coinCount        = null;
        keyCount         = null;
        rescuedVillagers = null;
   }
}
