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

package com.red7projects.dungeon.graphics.parallax;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.utils.logging.Trace;

public class ParallaxBackground implements Disposable
{
    public final Array<ParallaxLayer> layers;

    private final App app;

    public ParallaxBackground(App _app)
    {
        this.layers = new Array<>();
        this.app = _app;
    }

    public void setupLayers(LayerImage[] layerImage)
    {
        layers.clear();
        addLayers(layerImage);
    }

    public void render()
    {
        for (int layer=0; layer<layers.size; layer++)
        {
            layers.get(layer).draw();
        }
    }

    public void addLayers(LayerImage[] layerImage)
    {
        for (int layer = 0; layer < layerImage.length; layer++)
        {
            layers.add(new ParallaxLayer(layerImage[layer].layerName, app));

            layers.get(layer).xSpeed = layerImage[layer].horizontalSpeed;
            layers.get(layer).ySpeed = layerImage[layer].verticalSpeed;
        }
    }

    public void addLayerGroup(String[] layerGroup, App _app)
    {
        for(int layer = 0, arraySize = layerGroup.length; layer < arraySize; layer++)
        {
            layers.add(new ParallaxLayer(layerGroup[layer], app));
            layers.get(layer).setTextureRegion();
        }
    }

    public void addLayerGroupSpeeds(float[][] speeds)
    {
        if((speeds[0].length != layers.size) || (speeds[0].length != speeds[1].length))
        {
            Trace.__FILE_FUNC_WithDivider();
            Trace.dbg("ERROR: SPEED and LAYERS arrays size mismatch.");
        }

        int arraySize = speeds[0].length;

        for(int layer = 0; layer < arraySize; layer++)
        {
            layers.get(layer).xSpeed = speeds[0][layer];
            layers.get(layer).ySpeed = speeds[1][layer];
        }
    }

    public void scrollLayersUp()
    {
        for(int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_STILL, Movement._DIRECTION_UP);
        }
    }

    public void scrollLayersDown()
    {
        for(int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN);
        }
    }

    public void scrollLayersLeft()
    {
        for(int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
        }
    }

    public void scrollLayersRight()
    {
        for(int layerNum = 0, arraySize = layers.size; layerNum < arraySize; layerNum++)
        {
            layers.get(layerNum).scrollLayer(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        }
    }

    @Override
    public void dispose()
    {
        for (int i=0; i<layers.size; i++)
        {
            layers.get(i).dispose();
            layers.removeIndex(i);
        }
    }
}
