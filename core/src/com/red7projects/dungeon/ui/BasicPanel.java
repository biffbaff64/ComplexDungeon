/*
 * *****************************************************************************
 *    Copyright 27/03/2017 See AUTHORS file.
 *    <p>
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    <p>
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *   ***************************************************************************
 *
 */

package com.red7projects.dungeon.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.game.StateManager;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.types.XYSetF;

public class BasicPanel implements BasicUIPanel, Disposable
{
    public int xOffset = 0;
    public int yOffset = 0;

    public Table      buffer;
    public Skin       skin;
    public ScrollPane scrollPane;
    public Texture    texture;

    public boolean isActive;
    public int     panelWidth;
    public int     panelHeight;

    public Speed       speed;
    public Direction   direction;
    public XYSetF      distance;
    public XYSetF      distanceReset;
    public SimpleVec2F position;

    public StateManager state;

    SimpleVec2 size;
    TextureRegion textureRegion;
    String        nameID;

    public BasicPanel()
    {
        this.speed         = new Speed();
        this.direction     = new Direction();
        this.distance      = new XYSetF();
        this.distanceReset = new XYSetF();
        this.position      = new SimpleVec2F();

        this.state = new StateManager();

        this.panelWidth  = 50;
        this.panelHeight = 50;
        this.nameID      = "unnamed";
    }

    @Override
    public boolean nameExists(String _nameID)
    {
        return _nameID.equals(this.nameID);
    }

    @Override
    public String getNameID()
    {
        return nameID;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (isActive && (texture != null))
        {
            spriteBatch.draw(texture, xOffset, yOffset);
        }
    }

    @Override
    public void open()
    {
        setup();
    }

    @Override
    public void close()
    {
        dispose();
    }

    @Override
    public void activate()
    {
        isActive = true;
    }

    @Override
    public void deactivate()
    {
        isActive = false;
    }

    @Override
    public void setXOffset(int _offset)
    {
        xOffset = _offset;
    }

    @Override
    public void setYOffset(int _offset)
    {
        yOffset = _offset;
    }

    @Override
    public StateID getState()
    {
        return state.get();
    }

    @Override
    public void setState(StateID _state)
    {
        state.set(_state);
    }

    @Override
    public void dispose()
    {
        speed         = null;
        direction     = null;
        distance      = null;
        position      = null;
        textureRegion = null;
    }

    @Override
    public void setup()
    {
    }

    @Override
    public void updatePanel()
    {
    }

    @Override
    public void populateTable()
    {
    }

    @Override
    public void createButtonListeners()
    {
    }
}
