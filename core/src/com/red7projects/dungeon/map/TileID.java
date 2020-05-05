/*
 * *****************************************************************************
 *    Copyright 27/03/2017 See AUTHORS file.
 *    <p>
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    <p>
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *   ***************************************************************************
 *
 */

package com.red7projects.dungeon.map;

public enum TileID
{
    _DEFAULT_TILE(0),

    // Row 1
    _PLAYER_TILE(1),
    _POT_TILE(2),
    _CRATE_TILE(3),
    _BARREL_TILE(4),
    _SACKS_TILE(5),
    _LASER_TILE(6),
    _DOOR_TILE(7),
    _VILLAGER_TILE(8),
    _KEY_TILE(9),
    _CHEST_TILE(10),

    // Row 2
    _COIN_TILE(11),
    _FLAME_THROWER_TILE(12),
    _SHIELD_TILE(13),
    _SPIKE_BLOCK_DOWN_TILE(14),
    _SPIKE_BLOCK_LEFT_TILE(15),
    _SPIKE_BALL_TILE(16),
    _SPIKE_BLOCK_RIGHT_TILE(17),
    _SPIKE_BLOCK_UP_TILE(18),
    _FLOOR_BUTTON_TILE(19),
    _LEVER_TILE(20),

    // Row 3
    _GEM_TILE(21),
    _TORCH2_TILE(22),
    _LOOP_BLOCK_HORIZONTAL_TILE(23),
    _LOOP_BLOCK_VERTICAL_TILE(24),
    _PRISONER_TILE(25),
    _SPECIAL_COIN_TILE(26),
    _HIDDEN_COIN_TILE(27),
    _MYSTERY_CHEST_TILE(28),
    _FLOATING_PLATFORM_TILE(29),
    _SOLDIER_TILE(30),

    // Row 4
    _STORM_DEMON_TILE(31),
    _BOUNCER_TILE(32),
    _SCORPION_TILE(33),
    _PLANT_POT_TILE(34),
    _FIRE_BALL_TILE(35),
    _BIG_BLOCK_TILE(36),
    _U37_TILE(37),
    _U38_TILE(38),
    _U39_TILE(39),
    _TURRET_TILE(40),

    // Row 5
    _U41_TILE(41),
    _U42_TILE(42),
    _U43_TILE(43),
    _U44_TILE(44),
    _U45_TILE(45),
    _U46_TILE(46),
    _U47_TILE(47),
    _U48_TILE(48),
    _GLOW_EYES_TILE(49),
    _ARROW_TILE(50),

    // Row 6
    _FLAME_THROW_UP_TILE(51),
    _FLAME_THROW_DOWN_TILE(52),
    _FLAME_THROW_LEFT_TILE(53),
    _FLAME_THROW_RIGHT_TILE(54),
    _ESCALATOR_LEFT_TILE(55),
    _ESCALATOR_RIGHT_TILE(56),
    _ESCALATOR_DOWN_TILE(57),
    _ESCALATOR_UP_TILE(58),
    _U59_TILE(59),
    _U60_TILE(60),

    // Row 7
    _PURPLE_BOOK_TILE(61),
    _RED_BOOK_TILE(62),
    _BROWN_BOOK_TILE(63),
    _GREY_BOOK_TILE(64),
    _YELLOW_BOOK_TILE(65),
    _GREEN_BOOK_TILE(66),
    _PINK_BOOK_TILE(67),
    _BLUE_BOOK_TILE(68),
    _U69_TILE(69),
    _U70_BOOK_TILE(70),

    // Row 8
    _LIGHTNING_RUNE_TILE(71),
    _FIRE_RUNE_TILE(72),
    _WIND_RUNE_TILE(73),
    _SUN_RUNE_TILE(74),
    _ICE_RUNE_TILE(75),
    _NATURE_RUNE_TILE(76),
    _WATER_RUNE_TILE(77),
    _EARTH_RUNE_TILE(78),
    _U79_TILE(79),
    _U80_TILE(80),

    // Row 9
    _U81_TILE(81),
    _U82_TILE(82),
    _U83_TILE(83),
    _U84_TILE(84),
    _U85_TILE(85),
    _U86_TILE(86),
    _U87_TILE(87),
    _U88_TILE(88),
    _U89_TILE(89),
    _U90_TILE(90),

    // Row 10
    _U91_TILE(91),
    _U92_TILE(92),
    _U93_TILE(93),
    _NORTH_TILE(94),
    _EAST_TILE(95),
    _SOUTH_TILE(96),
    _WEST_TILE(97),
    _WAY_POINT_TILE(98),
    _BLANK_TILE(99),
    _NO_ACTION_TILE(100),

    // Tile IDs that are used in path finding
    _GROUND(150),
    _HOLE(151),
    _WATER(152),
    _GRASS(153),
    _WALL_EDGE(154),

    // Tile IDs that aren't indexes into the tileset
    _EXPLOSION_TILE(200),
    _LASER_HORIZONTAL_TILE(201),
    _LASER_VERTICAL_TILE(202),

    _MYSTERY_KEY(210),
    _MYSTERY_COIN(211),
    _MYSTERY_MAP(212),
    _MYSTERY_AXE(213),

    _UNKNOWN(255);

    private final int tileNumber;

    TileID(int value)
    {
        this.tileNumber = value;
    }

    public int get()
    {
        return tileNumber;
    }

    public static TileID fromValue(int value)
    {
        TileID returnValue = _UNKNOWN;

        for (TileID tileID : values())
        {
            if (tileID.get() == value)
            {
                returnValue = tileID;
            }
        }

        return returnValue;
    }
}
