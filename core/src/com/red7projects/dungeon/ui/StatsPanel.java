/*
 *  Copyright 28/05/2018 Red7Projects.
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.red7projects.dungeon.logging.Meters;
import com.red7projects.dungeon.logging.Stats;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.graphics.FontUtils;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.graphics.Gfx;

@SuppressWarnings("FieldCanBeLocal")
public class StatsPanel extends BasicPanel
{
    private class StatsInfo
    {
        final String name;
        final Meters meter;

        StatsInfo(String _name, Meters _meter)
        {
            this.name = _name;
            this.meter = _meter;
        }
    }

    private final String _SCROLL_PANE_NAME  = "policyScrollPane";
    private final String _BUFFER_NAME       = "policyBuffer";

    private       Image title;
    private final App   app;

    private final StatsInfo[] meterNames =
        {
            new StatsInfo("  Illegal Game Mode            ",   Meters._ILLEGAL_GAME_MODE),
            new StatsInfo("  Sound Load Failure           ",   Meters._SOUND_LOAD_FAIL),
            new StatsInfo("  Bad Player Action            ",   Meters._BAD_PLAYER_ACTION),
            new StatsInfo("  Font Load Failure            ",   Meters._FONT_LOAD_FAILURE),
            new StatsInfo("  Bordered Font Load Failure   ",   Meters._BORDERED_FONT_LOAD_FAILURE),

            new StatsInfo("divider", null),

            new StatsInfo("  I/O Exception                ",  Meters._IO_EXCEPTION),
            new StatsInfo("  Index Out Of Bounds Exception",  Meters._INDEX_OUT_OF_BOUNDS_EXCEPTION),
            new StatsInfo("  Array Index O.O.B Exception  ",  Meters._ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION),
            new StatsInfo("  SAX Exception                ",  Meters._SAX_EXCEPTION),
            new StatsInfo("  Interrupted Exception        ",  Meters._INTERRUPTED_EXCEPTION),
            new StatsInfo("  Null Pointer Exception       ",  Meters._NULL_POINTER_EXCEPTION),
            new StatsInfo("  Illegal State Exception      ",  Meters._ILLEGAL_STATE_EXCEPTION),
            new StatsInfo("  GDX Runtime Exception        ",  Meters._GDX_RUNTIME_EXCEPTION),
            new StatsInfo("  Unknown Exception            ",  Meters._UNKNOWN_EXCEPTION),
            new StatsInfo("  Should Always be ZERO >>>>   ",  Meters._DUMMY_METER),
            new StatsInfo("  Entity Data Exception        ",  Meters._ENTITY_DATA_EXCEPTION),

            new StatsInfo("divider", null),
        };

    public StatsPanel(App _app)
    {
        super();

        this.app = _app;
    }

    @Override
    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        buffer = new Table();
        buffer.setName(_BUFFER_NAME);
        buffer.top().left();
        buffer.pad(30, 30, 30, 30);
        buffer.setDebug(false);

        Texture sky = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);
        Image image = new Image(new TextureRegion(sky));
        buffer.setBackground(image.getDrawable());

        title = UIUtils.createImage("title_small", app.assets.getTextAtlas());
        title.setPosition(xOffset + 350, yOffset + (Gfx._VIEW_HEIGHT - 160));

        populateTable(buffer, skin);

        // Wrap the buffer in a scrollpane.
        scrollPane = UIUtils.createScrollPane(buffer, skin, _SCROLL_PANE_NAME);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._VIEW_WIDTH - 80);
        scrollPane.setHeight((float) (Gfx._VIEW_HEIGHT / 8) * 6);
        scrollPane.setPosition((xOffset + 40), (yOffset + 40));

        app.stage.addActor(scrollPane);
        app.stage.addActor(title);

        createButtonListeners();
    }

    /**
     * Populate table.
     */
    @Override
    public void populateTable()
    {
        populateTable(buffer, skin);
    }

    /**
     * Populate table.
     *
     * @param table the table
     * @param skin  the skin
     */
    private void populateTable(Table table, Skin skin)
    {
        try
        {
            TextField.TextFieldStyle textFieldStyle;

            for (StatsInfo meterName : meterNames)
            {
                if ("divider".equals(meterName.name))
                {
                    TextureRegion         region   = app.assets.getObjectsAtlas().findRegion("divider");
                    TextureRegionDrawable drawable = new TextureRegionDrawable(region);
                    Image                 image    = new Image(drawable);

                    table.add(image).padLeft(200);
                    table.row();
                }
                else
                {
                    FontUtils fontUtils = new FontUtils();

                    TextField label = new TextField(meterName.name, skin);
                    textFieldStyle = new TextField.TextFieldStyle(label.getStyle());
                    textFieldStyle.font = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 32, Color.WHITE);
                    textFieldStyle.fontColor = Color.WHITE;

                    label.setStyle(textFieldStyle);
                    label.setAlignment(Align.left);
                    label.setDisabled(true);

                    String meterString = Integer.toString(Stats.getMeter(meterName.meter.get()));

//                    if (meterName.isOutOf)
//                    {
//                        meterString = meterString.concat(" / ");
//
//                        int outOf;
//
//                        switch (meterName.special)
//                        {
//                            default:
//                            {
//                                outOf = 0;
//                            }
//                            break;
//                        }
//
//                        meterString = meterString.concat(Integer.toString(outOf));
//                    }

                    TextField meterLabel = new TextField(meterString, skin);
                    textFieldStyle = new TextField.TextFieldStyle(meterLabel.getStyle());
                    textFieldStyle.font = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 24, Color.YELLOW);
                    textFieldStyle.fontColor = Color.YELLOW;

                    meterLabel.setStyle(textFieldStyle);
                    meterLabel.setAlignment(Align.center);
                    meterLabel.setDisabled(true);

                    float prefHeight = label.getPrefHeight() * 1.5f;

                    table.add(label).padLeft(40).padBottom(10).prefWidth((float) Gfx._VIEW_WIDTH / 3).prefHeight(prefHeight);
                    table.add(meterLabel).padLeft(40).padBottom(10).prefWidth((float) Gfx._VIEW_WIDTH / 4).prefHeight(prefHeight);
                }

                table.row();
            }

            table.setVisible(true);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC_LINE();
            Stats.incMeter(Meters._NULL_POINTER_EXCEPTION.get());
        }
    }

    /**
     * Create a listener for the Reset Stats button.
     */
    @Override
    public void createButtonListeners()
    {
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        buffer.clear();
        scrollPane.clear();

        app.assets.unloadAsset("data/stats_screen.png");

        title.addAction(Actions.removeActor());
        buffer.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());

//        texture = null;
        title = null;
        skin = null;
    }
}