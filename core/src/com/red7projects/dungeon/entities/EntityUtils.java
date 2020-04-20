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

package com.red7projects.dungeon.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.Trace;
import org.jetbrains.annotations.NotNull;

public class EntityUtils
{
    private final App app;

    public EntityUtils(App _app)
    {
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

    public TextureRegion getKeyFrame(final Animation animation, final float elapsedTime, final boolean looping)
    {
        return (TextureRegion) animation.getKeyFrame(elapsedTime, looping);
    }

    public GdxSprite getRandomSprite(GdxSprite oneToAvoid)
    {
        GdxSprite randomSprite;

        do
        {
            randomSprite = app.entityData.entityMap.get(MathUtils.random(app.entityData.entityMap.size - 1));
        }
        while ((randomSprite.gid == oneToAvoid.gid)
                || (randomSprite.sprite == null)
                || (randomSprite.spriteNumber == oneToAvoid.spriteNumber));

        return randomSprite;
    }

    public GdxSprite getDistantSprite(GdxSprite gdxSprite)
    {
        GdxSprite distantSprite = app.getPlayer();
        float distance = gdxSprite.getPosition().dst(distantSprite.getPosition());

        for (GdxSprite sprite : app.entityData.entityMap)
        {
            float tempDistance = gdxSprite.getPosition().dst(sprite.getPosition());

            if (Math.abs(tempDistance) > Math.abs(distance))
            {
                distance = tempDistance;
                distantSprite = sprite;
            }
        }

        return distantSprite;
    }

    /**
     * Resets positions for all entities back to
     * their initialisation positions.
     */
    public void resetAllPositions()
    {
        if (app.entityData.entityMap != null)
        {
            GdxSprite entity;

            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                entity = app.entityData.entityMap.get(i);

                entity.sprite.setPosition(entity.initXY.getX(), entity.initXY.getY());
            }
        }
    }

    /**
     * @param   graphicID   - {@link GraphicID} of entity to be checked
     * @return  boolean     - TRUE if the entity is a main character
     */
    public boolean isMainCharacter(final GraphicID graphicID)
    {
        return graphicID == GraphicID.G_PLAYER;
    }

    public boolean canUpdate(final GraphicID graphicID)
    {
        boolean isUpdateable;

        switch (graphicID)
        {
            case G_PLAYER:
            case G_LASER:
            case G_PRISONER:
            case G_VILLAGER:

            // ----------
            case G_ARROW:
            case G_GEM:
            case G_COIN:
            case G_KEY:
            case G_SHIELD:
            case G_DOCUMENT:
            case G_LITTER:
            case G_RUNE:
            case G_BOOK:

            // ----------
            case G_ALCOVE_TORCH:
            case G_POT:
            case G_CRATE:
            case G_BARREL:
            case G_GLOW_EYES:
            case G_SACKS:

            // ----------
            case G_DOOR:
            case G_LOCKED_DOOR:
            case G_OPEN_DOOR:

            // ----------
            case G_FLOOR_BUTTON:
            case G_LEVER_SWITCH:
            case G_SPIKES:
            case G_TREASURE_CHEST:
            case G_MYSTERY_CHEST:
            case G_TELEPORTER:
            case G_FLOATING_PLATFORM:
            case G_TURRET:
            case G_ESCALATOR:
            case G_ESCALATOR_LEFT:
            case G_ESCALATOR_RIGHT:
            case G_ESCALATOR_UP:
            case G_ESCALATOR_DOWN:

            // ----------
            case G_LASER_BEAM:
            case G_LASER_BEAM_VERTICAL:
            case G_LASER_BEAM_HORIZONTAL:
            case G_FLAME_THROWER:

            // ----------
            case G_STORM_DEMON:
            case G_SPIKE_BALL:
            case G_SCORPION:
            case G_BOUNCER:
            case G_SOLDIER:
            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_ENEMY_BULLET:

            // ----------
            case G_EXPLOSION12:
            case G_EXPLOSION32:
            case G_EXPLOSION64:
            case G_EXPLOSION128:
            case G_EXPLOSION256:

            // ----------
            case G_SELECTION_RING:
            case G_MESSAGE_BUBBLE:
            case G_PRIZE_BALLOON:
            case G_HELP_BUBBLE:
            {
                isUpdateable = true;
            }
            break;

            default:
            {
                isUpdateable = false;
            }
            break;
        }

        return isUpdateable;
    }

    public boolean canFlashOnContact(final GraphicID gid)
    {
        boolean canFlash;

        switch (gid)
        {
            case G_STORM_DEMON:
            case G_BOUNCER:
            case G_SCORPION:
            case G_SOLDIER:
            // ----------------
            case G_PLAYER:
            case G_VILLAGER:
            {
                canFlash = true;
            }
            break;

            default:
            {
                canFlash = false;
            }
            break;
        }

        return canFlash;
    }

    public boolean setEnemyStatus(final GraphicID gid)
    {
        boolean isEnemy;

        switch (gid)
        {
            case G_STORM_DEMON:
            case G_BOUNCER:
            case G_SPIKE_BALL:
            case G_SCORPION:
            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            case G_LASER_BEAM:
            case G_LASER_BEAM_VERTICAL:
            case G_LASER_BEAM_HORIZONTAL:
            case G_ENEMY_BULLET:
            case G_FLAME_THROWER:
            case G_TURRET:
            {
                isEnemy = true;
            }
            break;

            default:
            {
                isEnemy = false;
            }
            break;
        }

        return isEnemy;
    }

    public int getInitialZPosition(final GraphicID graphicID)
    {
        int zed;

        switch (graphicID)
        {
            case G_SELECTION_RING:
            {
                zed = Gfx._MAXIMUM_Z_DEPTH;
            }
            break;

            case G_FLOOR_BUTTON:
            case G_LEVER_SWITCH:
            case G_TELEPORTER:
            case G_ESCALATOR:
            case G_ESCALATOR_LEFT:
            case G_ESCALATOR_RIGHT:
            case G_ESCALATOR_UP:
            case G_ESCALATOR_DOWN:
            {
                zed = 10;
            }
            break;

            case G_DOOR:
            case G_LOCKED_DOOR:
            case G_OPEN_DOOR:
            case G_FLOATING_PLATFORM:
            {
                zed = 9;
            }
            break;

            case G_ALCOVE_TORCH:
            case G_POT:
            case G_CRATE:
            case G_BARREL:
            case G_GLOW_EYES:
            case G_SPIKES:
            case G_TREASURE_CHEST:
            case G_MYSTERY_CHEST:
            case G_TURRET:
            case G_SACKS:
            {
                zed = 8;
            }
            break;

            case G_ARROW:
            case G_GEM:
            case G_COIN:
            case G_SPECIAL_COIN:
            case G_HIDDEN_COIN:
            case G_KEY:
            case G_SHIELD:
            case G_DOCUMENT:
            case G_LITTER:
            case G_RUNE:
            case G_BOOK:
            {
                zed = 7;
            }
            break;

            case G_QUESTION_MARK:
            {
                zed = 6;
            }
            break;

            case G_ENEMY_BULLET:
            {
                zed = 5;
            }
            break;

            case G_SOLDIER:
            case G_SPIKE_BALL:
            case G_SCORPION:
            {
                zed = 4;
            }
            break;

            case G_SPIKE_BLOCK_HORIZONTAL:
            case G_SPIKE_BLOCK_VERTICAL:
            case G_LOOP_BLOCK_HORIZONTAL:
            case G_LOOP_BLOCK_VERTICAL:
            {
                zed = 3;
            }
            break;

            case G_LASER:
            case G_PLAYER:
            case G_VILLAGER:
            case G_PRISONER:
            {
                zed = 2;
            }
            break;

            case G_LASER_BEAM:
            case G_LASER_BEAM_VERTICAL:
            case G_LASER_BEAM_HORIZONTAL:
            case G_FLAME_THROWER:
            {
                zed = 1;
            }
            break;

            case G_STORM_DEMON:
            case G_BOUNCER:
            case G_PRIZE_BALLOON:
            case G_MESSAGE_BUBBLE:
            case G_HELP_BUBBLE:
            case G_EXPLOSION12:
            case G_EXPLOSION32:
            case G_EXPLOSION64:
            case G_EXPLOSION128:
            case G_EXPLOSION256:
            {
                zed = 0;
            }
            break;

            default:
            {
                zed = Gfx._MAXIMUM_Z_DEPTH + 1;
            }
            break;
        }

        return zed;
    }

    public boolean isOnScreen(@NotNull GdxSprite spriteObject)
    {
        return app.mapData.viewportBox.overlaps(spriteObject.sprite.getBoundingRectangle());
    }

    public GdxSprite findFirstOf(final GraphicID _gid)
    {
        GdxSprite gdxSprite = null;

        for (GdxSprite sprite : app.entityData.entityMap)
        {
            if ((sprite.gid == _gid) && (gdxSprite == null))
            {
                gdxSprite = sprite;
            }
        }

        return gdxSprite;
    }

    public GdxSprite findLastOf(final GraphicID _gid)
    {
        GdxSprite gdxSprite = null;

        for (GdxSprite sprite : app.entityData.entityMap)
        {
            if (sprite.gid == _gid)
            {
                gdxSprite = sprite;
            }
        }

        return gdxSprite;
    }
}
