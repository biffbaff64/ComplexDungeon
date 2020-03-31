/*
 *  Copyright 24/04/2018 Red7Projects.
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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.red7projects.dungeon.game.App;

public class GfxUtils
{
    public static void splitRegion(TextureRegion textureRegion, int frameCount, TextureRegion[] destinationFrames, App app)
    {
        TextureRegion[] tmpFrames = textureRegion.split
            (
                (textureRegion.getRegionWidth() / frameCount),
                textureRegion.getRegionHeight()
            )[0];

        System.arraycopy(tmpFrames, 0, destinationFrames, 0, frameCount);
    }

    public static void drawRect(int x, int y, int width, int height, int thickness, Color color)
    {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(color);
        shapeRenderer.box(x, y, 0, width, height, thickness);
        shapeRenderer.end();
    }
}
