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

package com.red7projects.dungeon.utils.development;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.config.AppConfig;
import com.red7projects.dungeon.config.Settings;
import com.red7projects.dungeon.entities.objects.GdxSprite;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.utils.FontUtils;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.physics.aabb.CollisionObject;
import com.red7projects.dungeon.physics.aabb.CollisionRect;
import com.red7projects.dungeon.utils.logging.Meters;
import com.red7projects.dungeon.utils.logging.Stats;
import com.red7projects.dungeon.utils.logging.Trace;

import java.util.Locale;

public class DebugRenderer implements Disposable
{
    private static TextureRegion debugTextureRegion;
    private static App           app;
    private static BitmapFont    font;

    public static void setup(App _app)
    {
        app = _app;

        debugTextureRegion = new TextureRegion();

        FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 15, Color.WHITE);
    }

    public static void drawText(String _message, float _x, float _y)
    {
        font.draw(app.spriteBatch, _message, _x, _y);
    }

    public static void drawBoxes()
    {
        if (app.settings.isEnabled(Settings._TILE_BOXES))
        {
            drawTileLayerBoxes();
        }

        if (app.settings.isEnabled(Settings._SPRITE_BOXES))
        {
            drawSpriteCollisionBoxes();
        }
    }

    private static void drawSpriteActions()
    {
        if (AppConfig.entitiesExist)
        {
            for (GdxSprite sprite : app.entityData.entityMap)
            {
                if (sprite.getSpriteAction() != null)
                {
                    font.draw
                        (
                            app.spriteBatch,
                            String.format(Locale.getDefault(), "%s", sprite.getSpriteAction().name()),
                            sprite.sprite.getX(),
                            sprite.sprite.getY() + sprite.frameHeight
                        );
                }
            }
        }
    }

    private static void drawTileLayerBoxes()
    {
        TextureRegion debugTextureRegion;
        Rectangle     debugRectangle = new Rectangle();

        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            CollisionObject collisionObject = AABBData.boxes().get(i);

            switch (collisionObject.type)
            {
                case _WALL:
                case _SIGN:
                case _OBSTACLE:
                case _INTERACTIVE:
                default:
                {
                    debugRectangle.set(collisionObject.rectangle);

                    if (collisionObject.action == Actions._COLLIDABLE)
                    {
                        debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_red32x32");
                    }
                    else if (collisionObject.action == Actions._COLLIDING)
                    {
                        debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_blue32x32");
                    }
                    else
                    {
                        debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_white32x32");
                    }

                    drawRect
                        (
                            debugTextureRegion,
                            (int) debugRectangle.x,
                            (int) debugRectangle.y,
                            (int) debugRectangle.width,
                            (int) debugRectangle.height,
                            4
                        );
                }
                break;

//                default:
//                    break;
            }
        }
    }

    private static void drawSpriteCollisionBoxes()
    {
        TextureRegion debugTextureRegion;
        CollisionRect debugRectangle = new CollisionRect(GraphicID.G_NO_ID);

        for (int i = 0; i < AABBData.boxes().size; i++)
        {
            CollisionObject collisionObject = AABBData.boxes().get(i);

            if (collisionObject.type == GraphicID._ENTITY)
            {
                debugRectangle.set(AABBData.boxes().get(i).rectangle);

                if (collisionObject.rectangle.colour == Color.BLUE)
                {
                    debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_blue32x32");
                }
                else if ((collisionObject.rectangle.colour == Color.RED) || (collisionObject.action == Actions._COLLIDING))
                {
                    debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_red32x32");
                }
                else if (collisionObject.rectangle.colour == Color.YELLOW)
                {
                    debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_yellow32x32");
                }
                else if (collisionObject.rectangle.colour == Color.GREEN)
                {
                    debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_green32x32");
                }
                else
                {
                    debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_white32x32");
                }

                drawRect
                    (
                        debugTextureRegion,
                        (int) debugRectangle.x,
                        (int) debugRectangle.y,
                        (int) debugRectangle.width,
                        (int) debugRectangle.height,
                        4
                    );
            }
        }
    }

    public static void drawRect(int x, int y, int width, int height, int thickness)
    {
        debugTextureRegion = app.assets.getObjectsAtlas().findRegion("solid_red32x32");

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    public static void drawRect(int x, int y, int width, int height, int thickness, Color color)
    {
        String asset;

        if (color == Color.BLUE)
        {
            asset = "solid_blue32x32";
        }
        else if (color == Color.RED)
        {
            asset = "solid_red32x32";
        }
        else if (color == Color.YELLOW)
        {
            asset = "solid_yellow32x32";
        }
        else if (color == Color.GREEN)
        {
            asset = "solid_green32x32";
        }
        else
        {
            asset = "solid_white32x32";
        }

        debugTextureRegion = app.assets.getObjectsAtlas().findRegion(asset);

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    private static void drawRect(TextureRegion textureRegion, int x, int y, int width, int height, int thickness)
    {
        try
        {
            app.spriteBatch.draw(textureRegion, x, y, width, thickness);
            app.spriteBatch.draw(textureRegion, x, y, thickness, height);
            app.spriteBatch.draw(textureRegion, x, (y + height) - thickness, width, thickness);
            app.spriteBatch.draw(textureRegion, (x + width) - thickness, y, thickness, height);
        }
        catch (NullPointerException exception)
        {
            Trace.__FILE_FUNC("NullPointerException !");
            Trace.dbg("textureRegion: " + textureRegion);
            Trace.dbg("x: " + x);
            Trace.dbg("y: " + y);
            Trace.dbg("width: " + width);
            Trace.dbg("height: " + height);
            Trace.dbg("thickness: " + thickness);
            Trace.dbg("game: " + app);
            Trace.dbg("From: " + new Exception().getStackTrace()[1].getClassName());

            Stats.incMeter(Meters._NULL_POINTER_EXCEPTION.get());
        }
    }

    @Override
    public void dispose()
    {
        debugTextureRegion.getTexture().dispose();
        debugTextureRegion = null;
    }
}
