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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.FontUtils;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.utils.logging.Meters;
import com.red7projects.dungeon.utils.logging.Stats;
import com.red7projects.dungeon.utils.logging.Trace;

import java.io.BufferedReader;
import java.io.IOException;

@SuppressWarnings("FieldCanBeLocal")
public class PrivacyPolicyPanel extends BasicPanel
{
    private final String _FILE_NAME     = "documents/privacy_policy.txt";
//    private final String _BACKGROUND    = "data/empty_screen.png";

    private       Image title;
    private final App   app;

    public PrivacyPolicyPanel(App _app)
    {
        super();

        this.app = _app;
    }

    @Override
    public void populateTable()
    {
        try
        {
            FileHandle file = Gdx.files.internal(_FILE_NAME);
            BufferedReader reader = new BufferedReader(file.reader());
            String         string;
            Label          label;
            BitmapFont     bitmapFont;

            FontUtils fontUtils = new FontUtils();
            bitmapFont = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 18);

            while ((string = reader.readLine()) != null)
            {
                label = new Label(string, skin);
                label.setAlignment(Align.left);
                label.setWrap(true);

                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = bitmapFont;
                labelStyle.font.setColor(Color.WHITE);
                label.setStyle(labelStyle);

                buffer.add(label).align(Align.left);
                buffer.row();
            }

            buffer.setVisible(true);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC_LINE();
            Stats.incMeter(Meters._NULL_POINTER_EXCEPTION.get());
        }
        catch (IOException ioe)
        {
            Trace.__FILE_FUNC_LINE();
            Stats.incMeter(Meters._IO_EXCEPTION.get());
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        buffer.clear();
        scrollPane.clear();

//        app.assets.unloadAsset(_BACKGROUND);

        buffer.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());
        title.addAction(Actions.removeActor());

        title = null;
        skin = null;
//        texture = null;
    }

    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        buffer = new Table();
        buffer.top().left();
        buffer.pad(30, 30, 30, 30);
        buffer.setDebug(false);

        Texture sky = app.assets.loadSingleAsset("data/night_sky.png", Texture.class);
        Image image = new Image(new TextureRegion(sky));
        buffer.setBackground(image.getDrawable());

//        texture = app.assets.loadSingleAsset(_BACKGROUND, Texture.class);

        createTitle();

        populateTable();

        scrollPane = new ScrollPane(buffer, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._VIEW_WIDTH - 80);
        scrollPane.setHeight((float) (Gfx._VIEW_HEIGHT / 8) * 6);
        scrollPane.setPosition(xOffset + 40, yOffset + 40);

        app.stage.addActor(scrollPane);
        app.stage.addActor(title);
    }

    private void createTitle()
    {
        TextureRegion         region   = app.assets.getTextAtlas().findRegion("title_small");
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        title = new Image(drawable);
        title.setPosition(xOffset + 351, yOffset + (Gfx._VIEW_HEIGHT - 159));
    }
}
