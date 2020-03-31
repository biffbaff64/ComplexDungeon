/*
 *  Copyright 22/01/2019 Red7Projects.
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

package com.red7projects.dungeon.entities.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Speed;

@SuppressWarnings("NonConstantFieldWithUpperCaseName")
//@formatter:off
public class EntityDescriptor
{
    public int                _X;                 // X Coordinate.
    public int                _Y;                 // Y Coordinate.
    public int                _Z;                 // Z Coordinate.
    public int                _INDEX;             // This entities position in the entity map.
    public TextureRegion      _ASSET;             // The initial image asset.
    public int                _FRAMES;            // Number of frames in the asset above.
    public Animation.PlayMode _PLAYMODE;          // Animation playmode for the asset frames above.
    public float              _ANIM_RATE;         // Animation speed
    public boolean            _ENEMY;             // TRUE if this entity is an enemy entity.
    public boolean            _MAIN_CHARACTER;    // TRUE if this entity is one of the MAIN characters.
    public boolean            _UPDATEABLE;        // Initial setting for isUpdateable flag.
    public GdxSprite          _PARENT;            // Parent or Linked GDXSprite (if applicable).
    public SimpleVec2         _SIZE;              // Width and Height.
    public int                _LINK;              // Linked GDXSprite (if applicable).
    public Direction          _DIR;
    public SimpleVec2F        _DIST;
    public Speed              _SPEED;

    // TODO: 10/12/2019 - Remove _PARENT and make use of _LINK instead.

    public EntityDescriptor()
    {
        _X              = 0;
        _Y              = 0;
        _Z              = 0;
        _INDEX          = 0;
        _ASSET          = null;
        _FRAMES         = 0;
        _PLAYMODE       = Animation.PlayMode.NORMAL;
        _ANIM_RATE      = 1.0f / 6f;
        _ENEMY          = false;
        _UPDATEABLE     = false;
        _MAIN_CHARACTER = false;
        _PARENT         = null;
        _SIZE           = null;
        _LINK           = 0;
        _DIR            = new Direction();
        _DIST           = new SimpleVec2F();
        _SPEED          = new Speed();
    }

    public EntityDescriptor(EntityDescriptor descriptor)
    {
        _X              = descriptor._X;
        _Y              = descriptor._Y;
        _Z              = descriptor._Z;
        _INDEX          = descriptor._INDEX;
        _ASSET          = descriptor._ASSET;
        _FRAMES         = descriptor._FRAMES;
        _PLAYMODE       = descriptor._PLAYMODE;
        _ANIM_RATE      = descriptor._ANIM_RATE;
        _ENEMY          = descriptor._ENEMY;
        _UPDATEABLE     = descriptor._UPDATEABLE;
        _MAIN_CHARACTER = descriptor._MAIN_CHARACTER;
        _PARENT         = descriptor._PARENT;
        _SIZE           = descriptor._SIZE;
        _LINK           = descriptor._LINK;
        _DIR            = descriptor._DIR;
        _DIST           = descriptor._DIST;
        _SPEED          = descriptor._SPEED;
    }

    public EntityDescriptor(int x, int y, int z, TextureRegion asset, int frames, Animation.PlayMode mode)
    {
        this();

        _X        = x;
        _Y        = y;
        _Z        = z;
        _ASSET    = asset;
        _FRAMES   = frames;
        _PLAYMODE = mode;
    }

    public EntityDescriptor(TextureRegion asset, int frames, Animation.PlayMode mode)
    {
        this();

        _ASSET    = asset;
        _FRAMES   = frames;
        _PLAYMODE = mode;
    }

    public EntityDescriptor(TextureRegion asset, int frames, Animation.PlayMode mode, SimpleVec2 size)
    {
        this();

        _ASSET    = asset;
        _FRAMES   = frames;
        _PLAYMODE = mode;
        _SIZE     = size;
    }

    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("_X              : " + _X);
        Trace.dbg("_Y              : " + _Y);
        Trace.dbg("_Z              : " + _Z);
        Trace.dbg("_INDEX          : " + _INDEX);
        Trace.dbg("_ASSET          : " + _ASSET);
        Trace.dbg("_FRAMES         : " + _FRAMES);
        Trace.dbg("_PLAYMODE       : " + _PLAYMODE);
        Trace.dbg("_ANIM_RATE      : " + _ANIM_RATE);
        Trace.dbg("_ENEMY          : " + _ENEMY);
        Trace.dbg("_UPDATEABLE     : " + _UPDATEABLE);
        Trace.dbg("_MAIN_CHARACTER : " + _MAIN_CHARACTER);
        Trace.dbg("_PARENT         : " + _PARENT);
        Trace.dbg("_SIZE           : " + _SIZE);
        Trace.dbg("_LINK           : " + _LINK);
        Trace.dbg("_DIR            : " + _DIR.toString());
        Trace.dbg("_DIST           : " + _DIST.toString());
        Trace.dbg("_SPEED          : " + _SPEED.toString());
    }
}
