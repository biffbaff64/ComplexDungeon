/*
 *  Copyright 04/06/2018 Red7Projects.
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.Arrays;
import java.util.Comparator;

public class HighScoreUtils
{
    private static final String filePath = "hiscore_data.json";

    /**
     * The HighScore Table.
     * The table is 10 entrys in size PLUS one extra.
     * This extra entry is where a new hiscore is placed so that
     * array sorting can move it into its correct place.
     *
     * Array index 0 is the highest value.
     *
     * I thought about using Array but it just seemed
     * to be a bit much for such a small number of entries.
     */
    private HighScore[] highScores;
    private final Json json;
    private final App app;

    public HighScoreUtils(App _app)
    {
        this.app        = _app;
        this.json       = new Json();
        this.highScores = new HighScore[Constants._MAX_HISCORES + 1];

        for(int i = 0; i < (Constants._MAX_HISCORES + 1); i++)
        {
            highScores[i] = new HighScore();
        }
    }

    /**
     * Adds a value to the extra position at the end of the table,
     * then sorts the table
     */
    public void addHighScore(HighScore highScore)
    {
        if (highScore.score >= highScores[Constants._MAX_HISCORES - 1].score)
        {
            highScores[Constants._MAX_HISCORES].level = highScore.level;
            highScores[Constants._MAX_HISCORES].score = highScore.score;
            highScores[Constants._MAX_HISCORES].name = highScore.name;

            sortTable();

            writeTable();
        }
    }

    public int findInsertLevel(HighScore highScore)
    {
        int index = Constants._MAX_HISCORES;

        while (highScores[index].score > highScore.score)
        {
            index--;
        }

        return index;
    }

    /**
     * Loads the hiscore data from the json file into the
     * hiscore table array.
     *
     * NOTE: If the Json file does not exist then the table is set up
     * with default data and then written out as a Json file
     * for future use.
     */
    public void loadTableData()
    {
        if (!Gdx.files.local(filePath).exists())
        {
            setDefaultHiscoreTable();
        }

        highScores = json.fromJson(HighScore[].class, Gdx.files.local(filePath));
    }

    /**
     * Writes the hiscore table back to the json file
     */
    private void writeTable()
    {
        FileHandle handle = Gdx.files.local(filePath);

        handle.writeString(json.prettyPrint(json.toJson(highScores)), false);
    }

    /**
     * Sorts the table
     */
    private void sortTable()
    {
        HighScore[] temp = new HighScore[Constants._MAX_HISCORES + 1];

        for (int i = 0; i < (Constants._MAX_HISCORES + 1); i++)
        {
            temp[i] = new HighScore();

            temp[i].level = highScores[i].level;
            temp[i].score = highScores[i].score;
            temp[i].name  = highScores[i].name;
        }

        Arrays.sort(temp, new SortByScore());

        int j = Constants._MAX_HISCORES;

        for (int i = 0; i < (Constants._MAX_HISCORES + 1); i++)
        {
            highScores[i].rank  = i + 1;
            highScores[i].level = temp[j].level;
            highScores[i].score = temp[j].score;
            highScores[i].name  = temp[j--].name;
        }
    }

    /**
     * Checks if a new hiscore entry
     * can be added.
     *
     * @return boolean - TRUE if can be added.
     */
    public boolean canAddNewEntry()
    {
        boolean available = false;

        if (app.gameProgress.score != null)
        {
            if (app.gameProgress.score.getTotal() >= highScores[Constants._MAX_HISCORES - 1].score)
            {
                available = true;
            }
        }

        return available;
    }

    /**
     * Gets the hiscore table
     *
     * @return a reference to the table
     */
    public HighScore[] getHighScoreTable()
    {
        return highScores;
    }

    /**
     * Reset the hiscore table to default values.
     */
    public void resetTable()
    {
        setDefaultHiscoreTable();
        app.gameProgress.newHiScoreAvailable = false;
    }

    /**
     * Writes default values to the hiscore table if the
     * json file does not exist, ie because of a
     * fresh installation
     */
    private void setDefaultHiscoreTable()
    {
        HighScore[] defaultTable =
            {
                new HighScore(11,1, 0,      "TABLE END"),
                new HighScore(10,1, 1000,   "CHUCKY"),
                new HighScore(9, 1, 2000,   "THING"),
                new HighScore(8, 1, 3000,   "LUIGI"),
                new HighScore(7, 1, 4000,   "PACMAN"),
                new HighScore(6, 1, 5000,   "KONG"),
                new HighScore(5, 1, 6000,   "HORACE"),
                new HighScore(4, 1, 7000,   "MARIO"),
                new HighScore(3, 1, 8000,   "JS WILLY"),
                new HighScore(2, 1, 9000,   "MONTY"),
                new HighScore(1, 1, 10000,  "JETMAN"),
            };

        for (int i=0; i<Constants._MAX_HISCORES + 1; i++)
        {
            highScores[i].rank  = defaultTable[i].rank;
            highScores[i].level = defaultTable[i].level;
            highScores[i].score = defaultTable[i].score;
            highScores[i].name  = defaultTable[i].name;
        }

        sortTable();
        writeTable();
    }

    static class SortByScore implements Comparator<HighScore>
    {
        public int compare(HighScore a, HighScore b)
        {
            return Integer.compare(a.score, b.score);
        }
    }
}
