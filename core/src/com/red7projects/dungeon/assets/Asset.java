package com.red7projects.dungeon.assets;

import com.red7projects.dungeon.graphics.GraphicID;

public class Asset
{
    public final String name;
    public final int frames;
    public final AssetSize size;

    public Asset(GraphicID _gid, String _name, int _frames, int _width, int _height)
    {
        this.name = _name;
        this.frames = _frames;
        this.size = new AssetSize(_gid, _width, _height);
    }
}
