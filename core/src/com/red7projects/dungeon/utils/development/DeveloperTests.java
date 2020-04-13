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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.ui.BasicPanel;
import com.red7projects.dungeon.ui.Scene2DUtils;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.Constants;
import com.red7projects.dungeon.input.UIButtons;

public class DeveloperTests extends BasicPanel
{
    private Image       title;
    private TextField   tf1;
    private TextField   tf2;
    private TextField   tf3;
    private TextField   tf4;
    private TextField   tfLevelSelect;
    private Label       controllerName;
    private Label       buttonPress;
    private Label       axisCode;
    private Label       axisValue;
    private Label       povPress;
    private Label       levelValue;
    private ImageButton buttonPlus;
    private ImageButton buttonMinus;
    private int         testLevel;

    private final App app;

    public DeveloperTests(App _app)
    {
        super();

        this.app = _app;
        this.testLevel = 1;
    }

    @Override
    public void open()
    {
    }

    @Override
    public void close()
    {
    }

    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        title = createTitle();

        populateTable();

        createButtonListeners();
    }

    @Override
    public void updatePanel()
    {
        if (AppConfig.controllersFitted)
        {
            try
            {
                controllerName.setText(app.inputManager.gameController.getController().getName());
                buttonPress.setText("" + UIButtons.controllerButtonCode);
                axisCode.setText("" + UIButtons.controllerAxisCode);
                axisValue.setText("    /    " + UIButtons.controllerAxisValue);
                povPress.setText("" + UIButtons.controllerPovDirection);
            }
            catch (NullPointerException npe)
            {
                controllerName.setText("  Name Error, possible corruption...");
            }
        }
        else
        {
            controllerName.setText("  None Fitted");
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
    }

    @Override
    public void createButtonListeners()
    {
        buttonMinus.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (testLevel > 1)
                {
                    testLevel--;
                    levelValue.setText(testLevel);
                }
            }
        });

        buttonPlus.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                if (testLevel < Constants._MAX_LEVEL)
                {
                    testLevel++;
                    levelValue.setText(testLevel);
                }
            }
        });
    }

    @Override
    public void populateTable()
    {
        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        tf1 = scene2DUtils.addTextField("  Controller Name  ", xOffset+20, yOffset+(Gfx._VIEW_HEIGHT - 190), Color.WHITE, true, skin);
        controllerName = scene2DUtils.addLabel
            (
                "--------",
                (int) (xOffset + 20 + tf1.getWidth() + 100),
                (int) tf1.getY(),
                Color.WHITE,
                skin
            );
        controllerName.setAlignment(Align.left);

        tf2 = scene2DUtils.addTextField("  Button Code  ", xOffset+20, yOffset+(720-230), Color.WHITE, true, skin);
        buttonPress = scene2DUtils.addLabel
            (
                "--------",
                (int) (xOffset + 20 + tf2.getWidth() + 100),
                (int) tf2.getY(),
                Color.WHITE,
                skin
            );

        tf3 = scene2DUtils.addTextField("  Axis Code/Value  ", xOffset+20, yOffset+(720-270), Color.WHITE, true, skin);
        axisCode = scene2DUtils.addLabel
            (
                "--------",
                (int) (tf3.getX() + tf3.getWidth() + 100),
                (int) tf3.getY(),
                Color.WHITE,
                skin
            );
        axisValue = scene2DUtils.addLabel
            (
                "--------",
                (int) (axisCode.getX() + axisCode.getWidth() + 20),
                (int) tf3.getY(),
                Color.WHITE,
                skin
            );

        tf4 = scene2DUtils.addTextField("  POV Code  ", xOffset+20, yOffset+(720-310), Color.WHITE, true, skin);
        povPress = scene2DUtils.addLabel
            (
                "--------",
                (int) (xOffset + 20 + tf4.getWidth() + 100),
                (int) tf4.getY(),
                Color.WHITE,
                skin
            );

        tfLevelSelect = scene2DUtils.addTextField("Level Select", xOffset + 800, yOffset + (720-190), Color.WHITE, true, skin);
        levelValue = scene2DUtils.addLabel
            (
                "00",
                (int) (xOffset + 800 + tfLevelSelect.getWidth() + 80),
                (int) tfLevelSelect.getY(),
                Color.WHITE,
                skin
            );
        levelValue.setText(testLevel);
        levelValue.setFontScale(1.5f);
        buttonMinus = scene2DUtils.addButton
            (
                "arrow_left_blue",
                "arrow_left_blue_pressed",
                (int) (xOffset + 800 + tfLevelSelect.getWidth() + 20),
                (int) tfLevelSelect.getY()
            );
        buttonPlus = scene2DUtils.addButton
            (
                "arrow_right_blue",
                "arrow_right_blue_pressed",
                (int) (xOffset + 800 + tfLevelSelect.getWidth() + 140),
                (int) tfLevelSelect.getY()
            );
        buttonMinus.setSize(32, 32);
        buttonPlus.setSize(32, 32);
    }

    public void clearUp()
    {
        super.dispose();

        title.addAction(Actions.removeActor());
        tf1.addAction(Actions.removeActor());
        tf2.addAction(Actions.removeActor());
        tf3.addAction(Actions.removeActor());
        tf4.addAction(Actions.removeActor());
        controllerName.addAction(Actions.removeActor());
        buttonPress.addAction(Actions.removeActor());
        axisCode.addAction(Actions.removeActor());
        axisValue.addAction(Actions.removeActor());
        povPress.addAction(Actions.removeActor());
        tfLevelSelect.addAction(Actions.removeActor());
        levelValue.addAction(Actions.removeActor());

        skin = null;
        title = null;
        tf1 = null;
        tf2 = null;
        tf3 = null;
        tf4 = null;
        controllerName = null;
        buttonPress = null;
        axisCode = null;
        axisValue = null;
        povPress = null;
        tfLevelSelect = null;
        levelValue = null;
    }

    /**
     * Create the Title image
     *
     * @return Image holding the created title
     */
    private Image createTitle()
    {
        TextureRegion         region   = app.assets.getTextAtlas().findRegion("test_title");
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        Image title = new Image(drawable);
        title.setPosition(xOffset + (float) ((Gfx._VIEW_WIDTH - region.getRegionWidth()) / 2), yOffset + (Gfx._VIEW_HEIGHT - 80));

        app.stage.addActor(title);

        return title;
    }
}
