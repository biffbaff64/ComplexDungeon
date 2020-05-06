/*
 *
 *  * *****************************************************************************
 *  *  Copyright 27/03/2017 See AUTHORS file.
 *  *  <p>
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *  <p>
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  * ***************************************************************************
 *
 *
 */

package com.red7projects.dungeon.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.entities.Entities;
import com.red7projects.dungeon.entities.components.EntityManagerComponent;
import com.red7projects.dungeon.entities.objects.EntityDef;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;
import com.red7projects.dungeon.map.tiled.TiledMapTileLayer;
import com.red7projects.dungeon.map.tiled.objects.TiledMapTileMapObject;
import com.red7projects.dungeon.maths.Box;
import com.red7projects.dungeon.maths.SimpleVec2F;
import com.red7projects.dungeon.physics.Direction;
import com.red7projects.dungeon.physics.Movement;
import com.red7projects.dungeon.physics.Speed;
import com.red7projects.dungeon.physics.aabb.AABBData;
import com.red7projects.dungeon.physics.aabb.CollisionObject;
import com.red7projects.dungeon.physics.aabb.PolygonCollisionObject;
import com.red7projects.dungeon.utils.logging.Trace;

//@formatter:off
public class MapCreator
{
    public Array<MarkerTile> placementTiles;
    public Array<Rectangle> autoFloors;
    private App app;

    public MapCreator(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;
        placementTiles = new Array<>();
        autoFloors = new Array<>();
    }

    /**
     * Create the map data for the current level
     * <p>
     * Load the TileMap data, then create the
     * game map from that data.
     */
    public void createMap()
    {
        Trace.__FILE_FUNC();

        for (EntityManagerComponent component : app.entityData.managerList)
        {
            component.setPlaceable(false);
        }

        placementTiles.clear();
        autoFloors.clear();

        parseMarkerTiles();
        parseObjectTiles();

        if (AABBData.boxes().size == 0)
        {
            addDummyCollisionObject();
            app.entityManager.initialisePlayer();
        }

        createCollisionBoxes();
        createCollisonPolygons();
    }

    private void addDummyCollisionObject()
    {
        CollisionObject collisionObject;

        collisionObject              = app.collisionUtils.newObject();
        collisionObject.gid          = GraphicID.G_NO_ID;
        collisionObject.type         = GraphicID.G_NO_ID;
        collisionObject.bodyCategory = Gfx.CAT_NOTHING;
        collisionObject.collidesWith = Gfx.CAT_NOTHING;
        collisionObject.addObjectToList();
    }

    /**
     * NB: Does NOT create entities. This just extracts markers from
     * the Tile map and creates the necessary information from them.
     */
    private void parseMarkerTiles()
    {
        Trace.__FILE_FUNC();

        int xOffset = 0;
        int yOffset = 0;

        TileID    tileID;
        GraphicID graphicID = GraphicID.G_NO_ID;

        for (int y = 0; y < app.mapData.markerTilesLayer.getHeight(); y++)
        {
            for (int x = 0; x < app.mapData.markerTilesLayer.getWidth(); x++)
            {
                // getCell() returns null if the raw data at x,y is zero.
                // This is Ok because, here, that means there is no
                // marker tile to process.
                TiledMapTileLayer.Cell cell = app.mapData.markerTilesLayer.getCell(x, y);

                if (cell != null)
                {
                    tileID = TileID.fromValue(cell.getTile().getId());

                    boolean isSpawnPoint = false;
                    boolean isIgnoreTile = false;

                    for (EntityDef entityDef : Entities.entityList)
                    {
                        if (entityDef.tileID.equals(tileID))
                        {
                            graphicID       = entityDef.graphicID;
                            isSpawnPoint    = true;
                        }
                    }

                    if (!isSpawnPoint)
                    {
                        switch (tileID)
                        {
                            case _NORTH_TILE:
                            case _EAST_TILE:
                            case _SOUTH_TILE:
                            case _WEST_TILE:
                            {
                                //
                                // These aren't spawn point tiles, but they also
                                // should not write error messages.
                                isIgnoreTile = true;
                            }
                            break;

                            default:
                                break;
                        }
                    }

                    if (isSpawnPoint)
                    {
                        switch (tileID)
                        {
                            case _ARROW_TILE:
                            case _GEM_TILE:
                            case _KEY_TILE:
                            case _COIN_TILE:
                            case _SHIELD_TILE:
                            case _CHEST_TILE:
                            case _MYSTERY_COIN:
                            case _MYSTERY_CHEST_TILE:
                            case _HIDDEN_COIN_TILE:
                            {
                                setEntityPlaceable(GraphicID._PICKUP, true);
                            }
                            break;

                            case _TORCH2_TILE:
                            case _CRATE_TILE:
                            case _BARREL_TILE:
                            case _POT_TILE:
                            case _SACKS_TILE:
                            case _GLOW_EYES_TILE:
                            {
                                setEntityPlaceable(GraphicID._DECORATION, true);
                            }
                            break;

                            case _VILLAGER_TILE:
                            {
                                setEntityPlaceable(GraphicID.G_VILLAGER, true);
                            }
                            break;

                            case _SOLDIER_TILE:
                            {
                                setEntityPlaceable(GraphicID.G_SOLDIER, true);
                            }
                            break;

                            case _TURRET_TILE:
                            {
                                setEntityPlaceable(GraphicID.G_TURRET, true);
                            }
                            break;

                            case _STORM_DEMON_TILE:
                            case _SCORPION_TILE:
                            case _BOUNCER_TILE:
                            {
                                setEntityPlaceable(GraphicID._MONSTER, true);
                            }
                            break;

                            case _LEVER_TILE:
                            case _DOOR_TILE:
                            case _ESCALATOR_UP_TILE:
                            case _ESCALATOR_DOWN_TILE:
                            case _ESCALATOR_LEFT_TILE:
                            case _ESCALATOR_RIGHT_TILE:
                            {
                                setEntityPlaceable(GraphicID._INTERACTIVE, true);
                            }
                            break;

                            default:
                                break;
                        }

                        if (app.entityUtils.canUpdate(graphicID))
                        {
                            MarkerTile markerTile = new MarkerTile
                                (
                                    xOffset,
                                    yOffset,
                                    tileID,
                                    graphicID,
                                    placementTiles.size,
                                    ""
                                );

                            placementTiles.add(markerTile);
                        }
                    }
                    else
                    {
                        if (!isIgnoreTile)
                        {
                            Trace.dbg
                                (
                                    " - Unknown tile: "
                                    + tileID
                                    + "(" + tileID.get() + ")"
                                    + " at " + x + ", " + y
                                );
                        }
                    }
                }

                xOffset++;
            }

            xOffset = 0;
            yOffset++;
        }

        Trace.divider();
    }

    @SuppressWarnings("SameParameterValue")
    private void setEntityPlaceable(GraphicID _gid, boolean _placeable)
    {
        for (EntityManagerComponent component : app.entityData.managerList)
        {
            if (component.getGID() == _gid)
            {
                component.setPlaceable(_placeable);
            }
        }
    }

    /**
     * NB: Does NOT create entities. This just extracts markers from
     * the Tile map (Object Layer) and creates the necessary information from them.
     */
    // TODO: 06/12/2019 - This method has become too big. Work on reducing it.
    private void parseObjectTiles()
    {
        Trace.__FILE_FUNC();

        GraphicID graphicID;
        TileID    tileID;
        String    asset;

        for (MapObject object : app.mapData.mapObjects)
        {
            if (object instanceof TiledMapTileMapObject)
            {
                boolean isSizeBoxNeeded = false;
                boolean hasDirection    = false;
                boolean hasDistance     = false;
                boolean hasSpeed        = false;
                boolean isLockedDoor    = false;
                boolean isLinked        = false;

                graphicID = GraphicID.G_NO_ID;
                tileID    = TileID._DEFAULT_TILE;
                asset     = "";

                //
                // Find this objects details ready for parsing
                boolean isFound = false;

                if (object.getName() != null)
                {
                    for (EntityDef entityDef : Entities.entityList)
                    {
                        if (object.getName().equals(entityDef.objectName))
                        {
                            graphicID = entityDef.graphicID;
                            tileID    = entityDef.tileID;
                            asset     = entityDef.asset;
                            isFound   = true;
                        }
                    }
                }

                if (isFound)
                {
                    switch (object.getName())
                    {
                        case "Laser":
                        {
                            if (object.getProperties().get("direction").equals("horizontal"))
                            {
                                graphicID = GraphicID.G_LASER_BEAM_HORIZONTAL;
                                asset     = GameAssets._LASER_BEAM_HORIZONTAL_ASSET;
                            }
                            else
                            {
                                graphicID = GraphicID.G_LASER_BEAM_VERTICAL;
                                asset     = GameAssets._LASER_BEAM_VERTICAL_ASSET;
                            }

                            isSizeBoxNeeded = true;
                            isLinked        = true;

                            setEntityPlaceable(GraphicID.G_LASER_BEAM, true);
                        }
                        break;

                        case "Teleporter":
                        case "Lever Switch":
                        {
                            // No properties for these tiles....yet.
                            setEntityPlaceable(GraphicID._INTERACTIVE, true);
                        }
                        break;

                        case "Floor Button":
                        {
                            isLinked = true;
                            setEntityPlaceable(GraphicID._INTERACTIVE, true);
                        }
                        break;

                        case "Big Block Horizontal":
                        case "Big Block Vertical":
                        case "Floating Platform":
                        {
                            hasDirection = true;
                            hasDistance  = true;

                            setEntityPlaceable(GraphicID._BLOCKS, true);
                        }
                        break;

                        case "Spike Ball":
                        case "Spike Block":
                        case "Spike Block Vertical":
                        {
                            hasDirection = true;
                            hasDistance  = true;
                            hasSpeed     = true;

                            setEntityPlaceable(GraphicID._BLOCKS, true);
                        }
                        break;

                        case "Loop Block":
                        case "Loop Block Vertical":
                        {
                            hasDirection = true;

                            setEntityPlaceable(GraphicID._BLOCKS, true);
                        }
                        break;


                        default:
                        {
                            Trace.__FILE_FUNC("Unknown Object name: " + object.getName());
                        }
                        break;
                    }

                    if ((graphicID != GraphicID.G_NO_ID) && app.entityUtils.canUpdate(graphicID))
                    {
                        MarkerTile markerTile = new MarkerTile();

                        markerTile._X     = (int) (((TiledMapTileMapObject) object).getX() / Gfx.getTileWidth());
                        markerTile._Y     = (int) (((TiledMapTileMapObject) object).getY() / Gfx.getTileHeight());
                        markerTile._GID   = graphicID;
                        markerTile._TILE  = tileID;
                        markerTile._ASSET = asset;
                        markerTile._INDEX = placementTiles.size;
                        markerTile._DIST  = new SimpleVec2F();
                        markerTile._DIR   = new Direction();
                        markerTile._SPEED = new Speed();

                        if (isSizeBoxNeeded)
                        {
                            int width  = ((int) object.getProperties().get("width"));
                            int height = ((int) object.getProperties().get("height"));

                            markerTile._BOX = new Box(0, 0, width, height);
                        }

                        if (hasDistance)
                        {
                            markerTile._DIST.set
                                (
                                    ((int) object.getProperties().get("xdistance")),
                                    ((int) object.getProperties().get("ydistance"))
                                );
                        }

                        if (hasDirection)
                        {
                            markerTile._DIR.set
                                (
                                    object.getProperties().get("xdirection")
                                        .equals("right") ? Movement._DIRECTION_RIGHT :
                                        object.getProperties().get("xdirection")
                                            .equals("left") ? Movement._DIRECTION_LEFT : Movement._DIRECTION_STILL,

                                    object.getProperties().get("ydirection")
                                        .equals("up") ? Movement._DIRECTION_UP :
                                        object.getProperties().get("ydirection")
                                            .equals("down") ? Movement._DIRECTION_DOWN : Movement._DIRECTION_STILL
                                );
                        }

                        if (hasSpeed)
                        {
                            markerTile._SPEED.set
                                (
                                    ((float) object.getProperties().get("xspeed")),
                                    ((float) object.getProperties().get("yspeed"))
                                );
                        }

                        if (isLinked)
                        {
                            //
                            // Fetch the link ID of the attached entity
                            if (object.getProperties().get("connection") != null)
                            {
                                markerTile._LINK = (int) object.getProperties().get("connection");
                            }
                        }

                        placementTiles.add(markerTile);
                    }
                }
            }
        }

        Trace.finishedMessage();
    }

    private void createCollisionBoxes()
    {
        Trace.__FILE_FUNC();

        CollisionObject collisionObject;
        GraphicID graphicID;

        for (MapObject object : app.mapData.mapObjects)
        {
            if (object instanceof RectangleMapObject)
            {
                collisionObject = app.collisionUtils.newObject(new Rectangle(((RectangleMapObject) object).getRectangle()));

                if (object.getName() != null)
                {
                    short bodyCategory = 0;
                    short collidesWith = 0;

                    graphicID = GraphicID.G_NO_ID;

                    switch (object.getName())
                    {
                        case "Wall":
                        {
                            graphicID    = GraphicID._WALL;
                            bodyCategory = Gfx.CAT_WALL;
                            collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WEAPON | Gfx.CAT_DECORATION;
                        }
                        break;

                        case "Entity Barrier":
                        {
                            graphicID    = GraphicID._ENTITY_BARRIER;
                            bodyCategory = Gfx.CAT_ENTITY_BARRIER;
                            collidesWith = Gfx.CAT_DECORATION | Gfx.CAT_VILLAGER | Gfx.CAT_INTERACTIVE;
                        }
                        break;

                        case "Auto Floor":
                        {
                            graphicID    = GraphicID._AUTO_FLOOR;
                            bodyCategory = Gfx.CAT_INTERACTIVE;
                            collidesWith = Gfx.CAT_PLAYER;

                            autoFloors.add(collisionObject.rectangle);
                        }
                        break;

                        case "Spawn Free Zone":
                        {
                        }
                        break;

                        default:
                        {
                            Trace.__FILE_FUNC_WithDivider();
                            Trace.dbg("Unknown CollisionObject type: " + object.getName());
                        }
                        break;
                    }

                    if (graphicID != GraphicID.G_NO_ID)
                    {
                        collisionObject.gid          = graphicID;
                        collisionObject.type         = GraphicID._OBSTACLE;
                        collisionObject.bodyCategory = bodyCategory;
                        collisionObject.collidesWith = collidesWith;

                        collisionObject.addObjectToList();
                    }
                }
            }
        }
    }

    private void createCollisonPolygons()
    {
        Trace.__FILE_FUNC();

        PolygonCollisionObject collisionObject;

        for (MapObject object : app.mapData.mapObjects)
        {
            if (object instanceof PolygonMapObject)
            {
                collisionObject = new PolygonCollisionObject();

                if (object.getName() != null)
                {
                    if ("collision area".equals(object.getName()))
                    {
                        collisionObject.gid = GraphicID._WALL;
                        collisionObject.type = GraphicID._OBSTACLE;
                        collisionObject.bodyCategory = Gfx.CAT_WALL;
                        collisionObject.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY;
                        collisionObject.polygon = ((PolygonMapObject) object).getPolygon();

                        collisionObject.addObjectToList();
                    }
                }
            }
        }
    }
}
