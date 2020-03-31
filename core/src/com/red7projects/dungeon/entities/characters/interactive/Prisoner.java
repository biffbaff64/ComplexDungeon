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

package com.red7projects.dungeon.entities.characters.interactive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.Preferences;
import com.red7projects.dungeon.entities.objects.EntityDescriptor;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.graphics.SimpleDrawable;
import com.red7projects.dungeon.logging.Trace;

public class Prisoner extends GdxSprite
{
    private SimpleDrawable helpMessage;
    private final App app;

    public Prisoner(final GraphicID _gid, final App _app)
    {
        super(_gid, _app);

        this.app = _app;
    }

    @Override
    public void initialise(final EntityDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        collisionObject.bodyCategory = Gfx.CAT_INTERACTIVE;
        collisionObject.collidesWith = Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_FIXED_ENEMY;

        setAction(Actions._STANDING);

        addHelpMeMessage();

        sprite.setScale
            (
                app.getPlayer().sprite.getScaleX(),
                app.getPlayer().sprite.getScaleY()
            );

        canFlip = false;
    }

    @Override
    public void update(final int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
            }
            break;

            case _RUNNING:
            {
                // TODO: 13/01/2020 - Add code here to handle the prisoner escaping.
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        if (isAnimating)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);

        if (helpMessage != null)
        {
            helpMessage.draw(spriteBatch);
        }
    }

    private void addHelpMeMessage()
    {
        if (app.preferences.isEnabled(Preferences._SHOW_HINTS))
        {
            TextureRegion region = app.assets.getTextAtlas().findRegion(GameAssets._HELP_ME_ASSET);

            helpMessage = new SimpleDrawable
                (
                    region,
                    this.sprite.getX() - region.getRegionWidth(),
                    this.sprite.getY() + this.frameHeight + 8
                );
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();

        helpMessage = null;
    }
}
