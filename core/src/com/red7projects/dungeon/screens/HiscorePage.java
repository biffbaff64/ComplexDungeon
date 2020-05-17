/*
 *  Copyright 01/06/2018 Red7Projects.
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

package com.red7projects.dungeon.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.*;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.ui.Scene2DUtils;
import com.red7projects.dungeon.ui.UIPage;
import com.red7projects.dungeon.utils.logging.StopWatch;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class HiscorePage implements UIPage, Disposable
{
    private final Color[] colours =
        {
            Color.CYAN,
            Color.SKY,
            Color.SKY,
            Color.SLATE,
            Color.SLATE,
            Color.ROYAL,
            Color.ROYAL,
            Color.BLUE,
            Color.BLUE,
            Color.NAVY,
            Color.NAVY,
            Color.BLUE,
            Color.BLUE,
            Color.ROYAL,
            Color.ROYAL,
            Color.SLATE,
            Color.SLATE,
            Color.SKY,
            Color.SKY,
            Color.CYAN,
        };

    private final int _RANK_X             = 400;
    private final int _SCORE_X            = 550;
    private final int _NAME_X             = 1400;
    private final int _TABLE_Y            = 1080;
    private final int _SPACING            = 100;
    private final int _FONT_SIZE          = 96;
    private final int _DISPLAYED_HISCORES = 10;

    private Label        titleLabel;
    private Label[]      rankLabels;
    private Label[]      scoreLabels;
    private Label[]      nameLabels;
    private int[]        colorIndex;
    private int          loopCount;
    private StopWatch    stopWatch;
    private StateManager state;
    private Texture      foreground;
    private App          app;

    public HiscorePage(App _app)
    {
        this.app = _app;

        addItems();
        addClickListeners();

        state = new StateManager();
        stopWatch = StopWatch.start();

        showItems(false);
    }

    @Override
    public boolean update()
    {
        boolean isFinished = false;

        if (state.get() != StateID._STATE_NEW_HISCORE)
        {
            if (stopWatch.time(TimeUnit.MILLISECONDS) >= 75)
            {
                for (int i = 0; i < _DISPLAYED_HISCORES; i++)
                {
                    rankLabels[i].setStyle(new Label.LabelStyle(rankLabels[i].getStyle().font, colours[colorIndex[i]]));
                    scoreLabels[i].setStyle(new Label.LabelStyle(scoreLabels[i].getStyle().font, colours[colorIndex[i]]));
                    nameLabels[i].setStyle(new Label.LabelStyle(nameLabels[i].getStyle().font, colours[colorIndex[i]]));

                    if (--colorIndex[i] < 0)
                    {
                        colorIndex[i] = (colours.length - 1);

                        if (i == 0)
                        {
                            if (++loopCount >= 10)
                            {
                                isFinished = true;
                            }
                        }
                    }
                }

                stopWatch.reset();
            }
        }

        return isFinished;
    }

    @Override
    public void reset()
    {
        loopCount = 0;
    }

    @Override
    public void show()
    {
        showItems(true);

        loopCount = 0;

        if (app.gameProgress.newHiScoreAvailable)
        {
            state.set(StateID._STATE_NEW_HISCORE);
        }
        else
        {
            state.set(StateID._STATE_PANEL_UPDATE);
        }

        stopWatch.reset();
    }

    @Override
    public void hide()
    {
        showItems(false);

        app.gameProgress.newHiScoreAvailable = false;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, OrthoGameCamera camera, float originX, float originY)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, 0, 0);
        }
    }

    /**
     * Adds the Menu items to the stage
     */
    private void addItems()
    {
        foreground = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);

        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        rankLabels  = new Label[Constants._MAX_HISCORES];
        scoreLabels = new Label[Constants._MAX_HISCORES];
        nameLabels  = new Label[Constants._MAX_HISCORES];

        app.highScoreUtils.loadTableData();

        HighScore highScore = new HighScore();

        if (app.gameProgress.newHiScoreAvailable)
        {
            highScore.score = app.gameProgress.getScoreOne().getTotal();
            highScore.rank  = app.highScoreUtils.findInsertLevel(highScore);

            app.highScoreUtils.addHighScore(highScore);
        }

        titleLabel = scene2DUtils.addLabel
                (
                        "HIGH SCORES",
                        800,
                        Gfx._VIEW_HEIGHT - 160,
                        120,
                        Color.WHITE,
                        GameAssets._CENTURY_FONT
                );

        app.stage.addActor(titleLabel);

        colorIndex = new int[Constants._MAX_HISCORES];
        loopCount  = 0;

        for (int i = 0; i < Constants._MAX_HISCORES; i++)
        {
            // Hiscore table rank
            rankLabels[i] = scene2DUtils.addLabel
                (
                    String.format(Locale.UK, "%2d", (i + 1)),
                    _RANK_X, _TABLE_Y - (_SPACING * i),
                    _FONT_SIZE,
                    Color.WHITE,
                    GameAssets._CENTURY_FONT
                );

            // The player score
            scoreLabels[i] = scene2DUtils.addLabel
                (
                    String.format(Locale.UK, "%12d", app.highScoreUtils.getHighScoreTable()[i].score),
                    _SCORE_X, _TABLE_Y - (_SPACING * i),
                    _FONT_SIZE,
                    Color.WHITE,
                    GameAssets._CENTURY_FONT
                );

            nameLabels[i] = scene2DUtils.addLabel
                (
                    String.format(Locale.UK, "%s", app.highScoreUtils.getHighScoreTable()[i].name),
                    _NAME_X, _TABLE_Y - (_SPACING * i),
                    _FONT_SIZE,
                    Color.WHITE,
                    GameAssets._CENTURY_FONT
                );

            colorIndex[i] = i;

            app.stage.addActor(rankLabels[i]);
            app.stage.addActor(scoreLabels[i]);
            app.stage.addActor(nameLabels[i]);
        }
    }

    /**
     * Show or Hide all menu items
     *
     * @param _visible visibility flag/
     */
    private void showItems(boolean _visible)
    {
        if (titleLabel != null)
        {
            titleLabel.setVisible(_visible);
        }

        if (rankLabels != null)
        {
            for (Label label : rankLabels)
            {
                label.setVisible(_visible);
            }
        }

        if (scoreLabels != null)
        {
            for (Label label : scoreLabels)
            {
                label.setVisible(_visible);
            }
        }

        if (nameLabels != null)
        {
            for (Label label : nameLabels)
            {
                label.setVisible(_visible);
            }
        }
    }

    @Override
    public void dispose()
    {
        if (titleLabel != null)
        {
            titleLabel.addAction(Actions.removeActor());
        }

        if (rankLabels != null)
        {
            for (Label label : rankLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        if (scoreLabels != null)
        {
            for (Label label : scoreLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        if (nameLabels != null)
        {
            for (Label label : nameLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        titleLabel  = null;
        colorIndex  = null;
        rankLabels  = null;
        scoreLabels = null;
        nameLabels  = null;
        state       = null;
    }

    private void addClickListeners()
    {
    }
}
