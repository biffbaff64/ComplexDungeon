/*
 *  Copyright 31/01/2019 Red7Projects.
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

package com.red7projects.dungeon.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.maths.SimpleVec2F;

public class SimpleDrawable
{
    public final TextureRegion image;
    public final SimpleVec2F   position;
    public final int           width;
    public final int           height;

    public SimpleDrawable()
    {
        this.width      = 16;
        this.height     = 16;
        this.image      = new TextureRegion();
        this.position   = new SimpleVec2F();
    }

    public SimpleDrawable(TextureRegion _image, float _x, float _y)
    {
        this.image      = _image;
        this.position   = new SimpleVec2F(_x, _y);
        this.width      = _image.getRegionWidth();
        this.height     = image.getRegionHeight();
    }

    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(image, position.x, position.y, width, height);
    }
}
