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

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.logging.Trace;

public class AnimationUtils
{
    private final App app;

    public AnimationUtils(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
    }

    public Animation<TextureRegion> createAnimation(String filename, TextureRegion[] destinationFrames, int frameCount, Animation.PlayMode playmode)
    {
        Animation<TextureRegion> animation;

        try
        {
            TextureRegion textureRegion = app.assets.getAnimationsAtlas().findRegion(filename);

            TextureRegion[] tmpFrames = textureRegion.split
                (
                    (textureRegion.getRegionWidth() / frameCount),
                    textureRegion.getRegionHeight()
                )[0];

            System.arraycopy(tmpFrames, 0, destinationFrames, 0, frameCount);

            animation = new Animation<>(0.75f / 6f, tmpFrames);
            animation.setPlayMode(playmode);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC(filename);

            animation = null;
        }

        return animation;
    }

    public TextureRegion getKeyFrame(Animation animation, float elapsedTime, boolean looping)
    {
        return (TextureRegion) animation.getKeyFrame(elapsedTime, looping);
    }
}
