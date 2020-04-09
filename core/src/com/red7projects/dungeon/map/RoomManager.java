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

package com.red7projects.dungeon.map;

import com.red7projects.dungeon.map.tiled.TiledMap;
import com.red7projects.dungeon.map.tiled.TiledMapTileLayer;
import com.red7projects.dungeon.map.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.logging.Trace;
import com.red7projects.dungeon.maths.SimpleVec2;
import com.red7projects.dungeon.maths.SimpleVec2F;
import org.jetbrains.annotations.NotNull;

//@formatter:off
public class RoomManager
{
    private static final String _TEST_ROOM      = "aaa_test_room.tmx";        //

    private static final String _N              = "room_n.tmx";           // Done
    private static final String _N2             = "room_n2.tmx";          // Done
    private static final String _E              = "room_e.tmx";           // Done
    private static final String _S              = "room_s.tmx";           //
    private static final String _NS             = "room_ns.tmx";          //
    private static final String _SN             = "room_sn.tmx";          //
    private static final String _W              = "room_w.tmx";           //
    private static final String _E2             = "room_e2.tmx";          // Done
    private static final String _EW             = "room_ew.tmx";          // Done
    private static final String _NES            = "room_nes.tmx";         // Done
    private static final String _NEW            = "room_new.tmx";         //
    private static final String _NW             = "room_nw2.tmx";         //
    private static final String _NWS            = "room_nws.tmx";         //
    private static final String _WS             = "room_ws.tmx";          //
    private static final String _SEW            = "room_sew.tmx";         //
    private static final String _NESW           = "room_nesw.tmx";        //
    private static final String _NESW2          = "room_nesw2.tmx";       //

    private static final String _PASSAGE_ES     = "passage_es.tmx";       // Done
    private static final String _PASSAGE_ESW    = "room_horizontal.tmx";  // Done
    private static final String _PASSAGE_NS     = "passage_ns.tmx";       // Done
    private static final String _PASSAGE_NES    = "passage_nes.tmx";      // Done
    private static final String _PASSAGE_NE     = "passage_ne.tmx";       // Done

    private static final String _VERTICAL       = "room_vertical.tmx";    //
    private static final String _HORIZONTAL     = "room_horizontal.tmx";  // Done

    private static final String _SECRET1        = "room_secret1.tmx";     //

    private static final String _PRISON_N       = "prison_room_n.tmx";    //
    private static final String _PRISON_S       = "prison_room_s.tmx";    //

    private static final String _ROOM1_NS       = "room1_ns.tmx";         // Done
    private static final String _ROOM2_NS       = "room2_ns.tmx";         //
    private static final String _ROOM2_NES      = "room2_nes.tmx";        //
    private static final String _ROOM3_NEW      = "room3_new.tmx";        //
    private static final String _ROOM4_ES       = "room4_es.tmx";         //
    private static final String _ROOM5_NW       = "room5_ne.tmx";         //
    private static final String _ROOM6_ESW      = "room6_esw.tmx";        //
    private static final String _ROOM7_NS       = "room7_ns.tmx";         //
    private static final String _ROOM8_NW       = "room8_nw.tmx";         //
    private static final String _ROOM9_NESW     = "room9_nesw.tmx";       //
    private static final String _ROOM10_NSW     = "room10_nsw.tmx";       //
    private static final String _ROOM11_NESW    = "room11_nesw.tmx";      //
    private static final String _ROOM12_NS      = "room12_ns.tmx";        //
    private static final String _ROOM13_ESW     = "room13_esw.tmx";       //
    private static final String _ROOM14_NS      = "room14_ns.tmx";        //
    private static final String _ROOM15_NE      = "room15_ne.tmx";        //
    private static final String _ROOM16_NW      = "room16_nw.tmx";        //
    private static final String _ROOM17_N       = "room17_n.tmx";         // Done
    private static final String _ROOM18_N       = "room18_n.tmx";         //

    private static final String _AREA_1         = "area1.tmx";            //

    private static final String _MAPS_PATH      = "data/maps/";

    private static final int    _DEFAULT_START_ROW      = 1;
    private static final int    _DEFAULT_START_COLUMN   = 1;
    private static final String _START_ROOM             = "ROOM13.TMX";
    private static final int    _START_POSITION         = Room._START;

    private int worldWidth;
    private int worldHeight;

    private final Room[][] roomMap =
        {
            // DO NOT CHANGE THIS LINE
            // -----------------------------------------
            {null, null, null, null, null, null, null, null},
            // -----------------------------------------

            {null, null,                    null,                   null,                   null,                   null,                   new Room(_PRISON_S),    null},
            {null, new Room(_ROOM1_NS),     new Room(_SECRET1),     new Room(_ROOM4_ES),    new Room(_W),           new Room(_S),           new Room(_SN),          null},
            {null, new Room(_PASSAGE_NES),  new Room(_NESW2),       new Room(_NESW),        new Room(_ROOM6_ESW),   new Room(_ROOM11_NESW), new Room(_ROOM10_NSW),  null},
            {null, new Room(_ROOM2_NES),    new Room(_ROOM3_NEW),   new Room(_ROOM9_NESW),  new Room(_ROOM8_NW),    new Room(_ROOM7_NS),    new Room(_PASSAGE_NS),  null},
            {null, new Room(_N),            new Room(_E),           new Room(_ROOM5_NW),    null,                   new Room(_NES),         new Room(_NW),          null},
            {null, new Room(_PASSAGE_ES),   new Room(_WS),          null,                   null,                   new Room(_ROOM12_NS),   null,                   null},
            {null, new Room(_ROOM14_NS),    new Room(_PASSAGE_NE),  new Room(_PASSAGE_ESW), new Room(_ROOM13_ESW),  new Room(_NWS),         new Room(_VERTICAL),    null},
            {null, new Room(_ROOM17_N),     new Room(_N2),          new Room(_ROOM18_N),    new Room(_ROOM2_NS),    new Room(_ROOM15_NE),   new Room(_ROOM16_NW),   null},
            {null, null,                    null,                   null,                   new Room(_PRISON_N),    null,                   null,                   null},

            // -----------------------------------------
            // DO NOT CHANGE THIS LINE
            // -----------------------------------------
            {null, null, null, null, null, null, null, null},
            // -----------------------------------------
        };

    public int              roomIndex;
    public Array<String>    roomList;
    public int              playerStart;
    public Room             activeRoom;

    public RoomManager()
    {
        Trace.__FILE_FUNC();

        worldWidth  = roomMap[0].length;
        worldHeight = roomMap.length;

        Trace.dbg("_WORLD_WIDTH : " + worldWidth);
        Trace.dbg("_WORLD_HEIGHT: " + worldHeight);

        createRoomList();
        storeEntryPoints();
    }

    public void initialise()
    {
        Trace.__FILE_FUNC();

        activeRoom = new Room();

        int startRow    = _DEFAULT_START_ROW;
        int startColumn = _DEFAULT_START_COLUMN;

        roomIndex = roomList.indexOf(_START_ROOM, false);

        if (roomIndex > 0)
        {
            SimpleVec2 roomPos = findRoom(roomList.get(roomIndex).toLowerCase());

            startRow    = roomPos.getX();
            startColumn = roomPos.getY();
        }

        setRoom(startRow, startColumn, _START_POSITION);

        activeRoom.mysteryChestsAvailable = countRoomMarkers(TileID._MYSTERY_CHEST_TILE, activeRoom);
    }

    private void setRoom(int row, int column, int position)
    {
        if (roomMap[row][column] != null)
        {
            activeRoom.set(roomMap[row][column]);

            activeRoom.row = row;
            activeRoom.column = column;
        }

        playerStart = position;
    }

    @NotNull
    private SimpleVec2 findRoom(String roomName)
    {
        SimpleVec2 roomPosition = new SimpleVec2(1, 1);
        boolean isFound = false;

        for (int row = 0; row < worldHeight && !isFound; row++)
        {
            for (int column = 0; column < worldWidth && !isFound; column++)
            {
                if (roomMap[row][column] != null)
                {
                    if (roomName.equals(roomMap[row][column].roomName))
                    {
                        roomPosition.set(row, column);
                        isFound = true;
                    }
                }
            }
        }

        return roomPosition;
    }

    public Room[][] getRoomMap()
    {
        return roomMap;
    }

    public int getWorldWidth()
    {
        return worldWidth;
    }

    public int getWorldHeight()
    {
        return worldHeight;
    }

    public String getActiveRoomName()
    {
        String name = "null";

        if (roomMap[activeRoom.row][activeRoom.column] != null)
        {
            name = roomMap[activeRoom.row][activeRoom.column].roomName;
        }

        return name;
    }

    public SimpleVec2F getStartPosition()
    {
        String      currentMap = roomMap[activeRoom.row][activeRoom.column].roomName;
        SimpleVec2F positions  = new SimpleVec2F();

        for (int row = 0; row < worldHeight; row++)
        {
            for (int column = 0; column < worldWidth; column++)
            {
                if (roomMap[row][column] != null)
                {
                    if (currentMap.equals(roomMap[row][column].roomName))
                    {
                        positions.set(roomMap[row][column].compassPoints[playerStart]);
                    }
                }
            }
        }

        return positions;
    }

    public String getMapNameWithPath()
    {
        return _MAPS_PATH + roomMap[activeRoom.row][activeRoom.column].roomName;
    }

    @NotNull
    public String getMapNameWithPath(String roomName)
    {
        return _MAPS_PATH + roomName;
    }

    private void createRoomList()
    {
        roomList = new Array<>();

        roomList.add("--- SELECT ---");

        for (int roomRow = 0; roomRow < worldHeight; roomRow++)
        {
            for (int roomColumn = 0; roomColumn < worldWidth; roomColumn++)
            {
                Room room = roomMap[roomRow][roomColumn];

                if (room != null)
                {
                    roomList.add(room.roomName.toUpperCase());
                }
            }
        }

        roomList.sort();
    }

    private int countRoomMarkers(TileID _marker, Room _room)
    {
        TmxMapLoader      tmxMapLoader = new TmxMapLoader();
        TiledMap          map          = tmxMapLoader.load(getMapNameWithPath(_room.roomName));
        TiledMapTileLayer layer        = (TiledMapTileLayer) map.getLayers().get(MapData._MARKER_TILES);

        int markerCount = 0;

        for (int row = 0; row < layer.getHeight(); row++)
        {
            for (int column = 0; column < layer.getWidth(); column++)
            {
                TiledMapTileLayer.Cell cell = layer.getCell(column, row);

                if (cell != null)
                {
                    if (_marker.equals(TileID.fromValue(cell.getTile().getId())))
                    {
                        markerCount++;
                    }
                }
            }
        }

        return markerCount;
    }

    /**
     * Check all rooms for entry points, and store the
     * coordinates in the roomList array
     */
    private void storeEntryPoints()
    {
        for (int roomRow = 0; roomRow < worldHeight; roomRow++)
        {
            for (int roomColumn = 0; roomColumn < worldWidth; roomColumn++)
            {
                Room room = roomMap[roomRow][roomColumn];

                if (room != null)
                {
                    TmxMapLoader      tmxMapLoader = new TmxMapLoader();
                    TiledMap          map          = tmxMapLoader.load(getMapNameWithPath(room.roomName));
                    TiledMapTileLayer layer        = (TiledMapTileLayer) map.getLayers().get(MapData._MARKER_TILES);

                    for (int row = 0; row < layer.getHeight(); row++)
                    {
                        for (int column = 0; column < layer.getWidth(); column++)
                        {
                            TiledMapTileLayer.Cell cell = layer.getCell(column, row);

                            if (cell != null)
                            {
                                TileID tileID = TileID.fromValue(cell.getTile().getId());

                                switch (tileID)
                                {
                                    case _NORTH_TILE:
                                    {
                                        room.compassPoints[Room._N].set(column, row);
                                    }
                                    break;

                                    case _EAST_TILE:
                                    {
                                        room.compassPoints[Room._E].set(column, row);
                                    }
                                    break;

                                    case _SOUTH_TILE:
                                    {
                                        room.compassPoints[Room._S].set(column, row);
                                    }
                                    break;

                                    case _WEST_TILE:
                                    {
                                        room.compassPoints[Room._W].set(column, row);
                                    }
                                    break;

                                    case _PLAYER_TILE:
                                    {
                                        room.compassPoints[Room._START].set(column, row);
                                    }
                                    break;

                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void moveUp()
    {
        if (activeRoom.row > 0)
        {
            if (roomMap[activeRoom.row - 1][activeRoom.column] != null)
            {
                activeRoom.row--;

                setRoom(activeRoom.row, activeRoom.column, Room._S);
            }
        }
    }

    public void moveDown()
    {
        if (activeRoom.row < roomMap.length)
        {
            if (roomMap[activeRoom.row + 1][activeRoom.column] != null)
            {
                activeRoom.row++;

                setRoom(activeRoom.row, activeRoom.column, Room._N);
            }
        }
    }

    public void moveLeft()
    {
        if (activeRoom.column > 0)
        {
            if (roomMap[activeRoom.row][activeRoom.column - 1] != null)
            {
                activeRoom.column--;

                setRoom(activeRoom.row, activeRoom.column, Room._E);
            }
        }
    }

    public void moveRight()
    {
        if (activeRoom.column < roomMap[activeRoom.column].length)
        {
            if (roomMap[activeRoom.row][activeRoom.column + 1] != null)
            {
                activeRoom.column++;

                setRoom(activeRoom.row, activeRoom.column, Room._W);
            }
        }
    }

    private void debugRoomMap()
    {
        for (int _row = 0; _row < worldHeight; _row++)
        {
            Trace.divider();

            for (int _column = 0; _column < worldWidth; _column++)
            {
                if (roomMap[_row][_column] == null)
                {
                    Trace.dbg("null room");
                }
                else
                {
                    Trace.dbg(roomMap[_row][_column].roomName);
                }
            }
        }
    }
}
