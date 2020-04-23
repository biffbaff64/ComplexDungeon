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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;

@SuppressWarnings("FieldCanBeLocal")
public class InstructionsPanel extends BasicPanel
{
    private final String _FILE_NAME = "data/howto.png";
    private final App app;

    public InstructionsPanel(App _app)
    {
        super();

        this.app = _app;
    }

    @Override
    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        buffer = new Table();
        buffer.top().left();
        buffer.pad(10, 10, 10, 10);
        buffer.setDebug(false);

        Texture sky = app.assets.loadSingleAsset(_FILE_NAME, Texture.class);
        Image image = new Image(new TextureRegion(sky));
        buffer.setBackground(image.getDrawable());

        populateTable();

        scrollPane = new ScrollPane(buffer, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._VIEW_WIDTH - 80);
        scrollPane.setHeight((Gfx._VIEW_HEIGHT / 8) * 6);
        scrollPane.setPosition(xOffset + 40, yOffset + 40);

        app.stage.addActor(scrollPane);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        buffer.clear();
        scrollPane.clear();

        buffer.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());

        skin = null;
    }
}
