package com.red7projects.dungeon.graphics;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class AnimatingTileManager
{
    private final Array<TiledMapTileLayer.Cell> mapAnimTiles;

    public AnimatingTileManager()
    {
        mapAnimTiles = new Array<>();
    }

    public void animateTiles(String tileName, int maxFrame, HashMap<String, TiledMapTile> tiles)
    {
        for (TiledMapTileLayer.Cell cell : mapAnimTiles)
        {
            if (cell != null)
            {
                String property = (String) cell.getTile().getProperties().get(tileName);

                if (property != null)
                {
                    Integer currentAnimationFrame = Integer.parseInt(property);

                    currentAnimationFrame++;

                    if (currentAnimationFrame > maxFrame)
                    {
                        currentAnimationFrame = 1;
                    }

                    cell.setTile(tiles.get(currentAnimationFrame.toString()));
                }
            }
        }
    }
}
