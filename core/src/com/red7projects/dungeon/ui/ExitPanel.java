/*
 *  Copyright 08/06/2018 Red7Projects.
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

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.utils.logging.StopWatch;

import java.util.concurrent.TimeUnit;

@SuppressWarnings({"WeakerAccess"})
public class ExitPanel implements Disposable
{
    public static final int _NONE_PRESSED = 0;
    public static final int _NO_PRESSED   = 1;
    public static final int _YES_PRESSED  = 2;

    public ImageButton   buttonYes;
    public ImageButton   buttonNo;

    private Texture     panel;
    private final App   app;
    private Texture     darkLayer;
    private int         action;
    private StopWatch   stopWatch;
    private boolean     showYes;
    private boolean     firstTime;

    private static final int _YES = 0;
    private static final int _NO  = 1;

    private static final int[][] displayPos =
        {
            {480, (Gfx._VIEW_HEIGHT - 520), 142, 66},   // Yes
            {668, (Gfx._VIEW_HEIGHT - 520), 142, 66},   // No
        };

    public ExitPanel(App _app)
    {
        this.app = _app;
    }

    public void open()
    {
        setup();
    }

    public void close()
    {
        dispose();
    }

    public int update()
    {
        if ((stopWatch.time(TimeUnit.MILLISECONDS) > 750) || firstTime)
        {
            if (showYes)
            {
                buttonYes.setVisible(true);
                buttonNo.setVisible(false);

                buttonYes.setDisabled(false);
                buttonNo.setDisabled(true);
            }
            else
            {
                buttonYes.setVisible(false);
                buttonNo.setVisible(true);

                buttonYes.setDisabled(true);
                buttonNo.setDisabled(false);
            }

            showYes = !showYes;
            firstTime = false;

            stopWatch.reset();
        }

        return action;
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(darkLayer, 0, 0);
        spriteBatch.draw(panel, 0, 0);
    }

    private void setup()
    {
        panel       = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);
        darkLayer   = app.assets.loadSingleAsset("data/dark_screen.png", Texture.class);

        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        buttonYes = scene2DUtils.addButton
        (
            "button_yes",
            "button_yes_pressed",
            displayPos[_YES][0],
            displayPos[_YES][1]
        );

        buttonNo = scene2DUtils.addButton
        (
            "button_no",
            "button_no_pressed",
            displayPos[_NO][0],
            displayPos[_NO][1]
        );

        buttonYes.setZIndex(1);
        buttonNo.setZIndex(1);

        action      = _NONE_PRESSED;
        showYes     = true;
        firstTime   = true;
        stopWatch   = StopWatch.start();

        buttonYes.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                action = _YES_PRESSED;
            }
        });

        buttonNo.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                action = _NO_PRESSED;
            }
        });
    }

    @Override
    public void dispose()
    {
        buttonYes.addAction(Actions.removeActor());
        buttonNo.addAction(Actions.removeActor());

        app.assets.unloadAsset("data/exit_screen.png");
        panel = null;

        app.assets.unloadAsset("data/dark_screen.png");
        darkLayer = null;

        buttonYes = null;
        buttonNo = null;
    }
}
