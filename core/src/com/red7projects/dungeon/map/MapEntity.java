package com.red7projects.dungeon.map;

import com.red7projects.dungeon.graphics.GraphicID;

public class MapEntity
{
    public final String    identifier;
    public final GraphicID graphicID;
    public final MarkerID  markerID;
    public final String    asset;

    public MapEntity(String _id, GraphicID _gid, MarkerID _mid, String _asset)
    {
        this.identifier = _id;
        this.graphicID  = _gid;
        this.markerID   = _mid;
        this.asset      = _asset;
    }
}
