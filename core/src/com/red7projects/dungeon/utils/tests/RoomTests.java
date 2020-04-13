package com.red7projects.dungeon.utils.tests;

import com.red7projects.dungeon.map.tiled.TiledMap;
import com.red7projects.dungeon.map.tiled.TiledMapTileLayer;
import com.red7projects.dungeon.map.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.StringBuilder;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.utils.logging.Trace;
import com.red7projects.dungeon.map.MapData;
import com.red7projects.dungeon.map.TileID;
import com.red7projects.dungeon.map.Room;

public abstract class RoomTests
{
    private static App app;

    public static void validateRooms(App _app)
    {
        app = _app;

        markerIDCheck(TileID._PLAYER_TILE, true);
        markerIDCheck(TileID._SHIELD_TILE, false);

        objectIDCheck();

        roomSizeCheck();
    }

    private static void roomSizeCheck()
    {
        for (int roomRow = 0; roomRow < app.getRoomSystem().getWorldHeight(); roomRow++)
        {
            for (int roomColumn = 0; roomColumn < app.getRoomSystem().getWorldWidth(); roomColumn++)
            {
                Room room = app.getRoomSystem().getRoomMap()[roomRow][roomColumn];

                if (room != null)
                {
                    TmxMapLoader    tmxMapLoader = new TmxMapLoader();
                    TiledMap        map          = tmxMapLoader.load(app.getRoomSystem().getMapNameWithPath(room.roomName));

                    int width = (int) map.getProperties().get("width");
                    int height = (int) map.getProperties().get("height");

                    Trace.__FILE_FUNC(room.roomName + " : " + width + ", " + height);
                }
            }
        }
    }

    private static void markerIDCheck(TileID targetTileID, boolean oneOnly)
    {
        for (int roomRow = 0; roomRow < app.getRoomSystem().getWorldHeight(); roomRow++)
        {
            for (int roomColumn = 0; roomColumn < app.getRoomSystem().getWorldWidth(); roomColumn++)
            {
                Room room = app.getRoomSystem().getRoomMap()[roomRow][roomColumn];

                if (room != null)
                {
                    TmxMapLoader      tmxMapLoader = new TmxMapLoader();
                    TiledMap          map          = tmxMapLoader.load(app.getRoomSystem().getMapNameWithPath(room.roomName));
                    TiledMapTileLayer layer        = (TiledMapTileLayer) map.getLayers().get(MapData._MARKER_TILES);

                    int tileCount = 0;
                    boolean roomPassed = false;

                    for (int row = 0; row < layer.getHeight(); row++)
                    {
                        for (int column = 0; column < layer.getWidth(); column++)
                        {
                            TiledMapTileLayer.Cell cell = layer.getCell(column, row);

                            if (cell != null)
                            {
                                if (TileID.fromValue(cell.getTile().getId()) == targetTileID)
                                {
                                    roomPassed = true;
                                    tileCount++;
                                }
                            }
                        }
                    }

                    StringBuilder sb = new StringBuilder();

                    if (oneOnly && (tileCount > 1))
                    {
                        sb.append("WARNING:  Room ");
                        sb.append(app.getRoomSystem().getRoomMap()[roomRow][roomColumn].roomName);
                        sb.append(" contains TOO MANY instances of ");
                        sb.append(targetTileID.name());
                        sb.append(" (").append(tileCount).append(")");

                        Trace.__FILE_FUNC(sb.toString());
                    }
                    else
                    {
                        if (!roomPassed)
                        {
                            sb.append("WARNING:  Room ");
                            sb.append(app.getRoomSystem().getRoomMap()[roomRow][roomColumn].roomName);
                            sb.append(" does not contain ");
                            sb.append(targetTileID.name());

                            Trace.__FILE_FUNC(sb.toString());
                        }
                    }
                }
            }
        }
    }

    private static void objectIDCheck()
    {
    }
}
