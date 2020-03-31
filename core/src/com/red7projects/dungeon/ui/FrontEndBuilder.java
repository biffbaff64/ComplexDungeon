/*
 *  Copyright 10/01/2019 Red7Projects.
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
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.maths.SimpleVec2;

public class FrontEndBuilder
{
    private       Texture    frontEndScroll;
    private       SimpleVec2 sourcePos;
    private final App        app;

    public FrontEndBuilder(App _app)
    {
        this.app = _app;
    }

    public void initialise(String fileName)
    {
        frontEndScroll  = app.assets.loadSingleAsset(fileName, Texture.class);
        sourcePos       = new SimpleVec2();
    }

    public void update()
    {
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw
            (
                frontEndScroll,
                0,
                0,
                sourcePos.x,
                sourcePos.y,
                Gfx._VIEW_WIDTH,
                Gfx._VIEW_HEIGHT
            );
    }
}
