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

package com.red7projects.dungeon.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.utils.logging.Trace;

/**
 * Creates an object describing essential asset data for entities.
 * Matches the following:-
 * 1. The GraphicID of the entity.
 * 2. A String holding the asset name.
 * 3. A String holding the enabled/disabled Preference flag.
 * 4. An int holding the number of animation frames.
 * 5. The default Animation Playmode.
 */
public class GfxAsset
{
    public final GraphicID          graphicID;
    public       String             asset;
    public final String             preference;
    public final int                frames;
    public final Animation.PlayMode playMode;

    public GfxAsset(final GraphicID _gid, final String _preference, final String _asset)
    {
        this.graphicID  = _gid;
        this.preference = _preference;
        this.asset      = _asset;
        this.frames     = 1;
        this.playMode   = Animation.PlayMode.NORMAL;
    }

    public GfxAsset(final GraphicID _gid, final String _preference, final String _asset, int _frames)
    {
        this.graphicID  = _gid;
        this.preference = _preference;
        this.asset      = _asset;
        this.frames     = _frames;
        this.playMode   = Animation.PlayMode.NORMAL;
    }

    public GfxAsset(final GraphicID _gid, final String _preference, final String _asset, int _frames, Animation.PlayMode _playmode)
    {
        this.graphicID  = _gid;
        this.preference = _preference;
        this.asset      = _asset;
        this.frames     = _frames;
        this.playMode   = _playmode;
    }

    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("graphicID  : " + graphicID);
        Trace.dbg("preference : " + preference);
        Trace.dbg("asset      : " + asset);
        Trace.dbg("frames     : " + frames);
        Trace.dbg("playMode   : " + playMode);
    }
}
