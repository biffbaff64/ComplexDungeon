package com.red7projects.dungeon.graphics;

public enum GraphicID
{
    // ----------------------------
    // The Player
    G_PLAYER,
    G_PLAYER_FIGHT,
    G_PLAYER_CAST,

    // ----------------------------
    // Other main, non-enemy, characters
    G_PRISONER,
    G_VILLAGER,

    // ----------------------------
    // Pickup Items
    G_COIN,
    G_SPECIAL_COIN,
    G_HIDDEN_COIN,
    G_ARROW,
    G_GEM,
    G_SHIELD,
    G_KEY,
    G_HUD_KEY,
    G_APPLE,
    G_BOOK,
    G_CAKE,
    G_CHERRIES,
    G_GRAPES,
    G_SILVER_ARMOUR,
    G_GOLD_ARMOUR,

    // ----------------------------
    // Decorations
    G_POT,
    G_CRATE,
    G_BARREL,
    G_TORCH,
    G_ALCOVE_TORCH,
    G_SACKS,

    // ----------------------------
    // Interactive items
    G_DOOR,
    G_OPEN_DOOR,
    G_LOCKED_DOOR,
    G_FLOOR_BUTTON,
    G_LEVER_SWITCH,
    G_TREASURE_CHEST,
    G_MYSTERY_CHEST,
    G_TELEPORTER,
    G_PRIZE_BALLOON,
    G_MESSAGE_BUBBLE,
    G_HELP_BUBBLE,
    G_DOCUMENT,
    G_QUESTION_MARK,
    G_EXCLAMATION_MARK,
    G_TALK_BOX,
    G_FLOATING_PLATFORM,
    G_SELECTION_RING,
    G_CROSSHAIRS,

    // ----------------------------
    G_EXPLOSION12,
    G_EXPLOSION32,
    G_EXPLOSION64,
    G_EXPLOSION128,
    G_EXPLOSION256,

    // ----------------------------
    // Enemies
    G_STORM_DEMON,
    G_BOUNCER,
    G_MINI_FIRE_BALL,
    G_SPIKE_BALL,
    G_SPIKE_BLOCK_HORIZONTAL,
    G_SPIKE_BLOCK_VERTICAL,
    G_DOUBLE_SPIKE_BLOCK,
    G_LOOP_BLOCK_HORIZONTAL,
    G_LOOP_BLOCK_VERTICAL,
    G_SPIKES,
    G_SCORPION,
    G_ENEMY_BULLET,
    G_ENEMY_FIREBALL,
    G_BEETLE,
    G_SOLDIER,
    G_SOLDIER_FIGHT,
    G_JELLY_MONSTER,

    // ----------------------------
    G_LASER,
    G_LASER_BEAM,
    G_LASER_BEAM_VERTICAL,
    G_LASER_BEAM_HORIZONTAL,
    G_FLAME_THROWER,
    G_FLAME_THROWER_VERTICAL,
    G_TURRET,

    // ----------------------------
    // Generic
    _MONSTER,
    _BLOCKS,

    // ----------------------------
    // object IDs for non-sprites
    _GROUND,
    _CEILING,
    _WALL,
    _LETHAL_OBJECT,
    _ENEMY,
    _MAIN,
    _SIGN,
    _SPEECH,
    _HUD_PANEL,
    _ENTITY_BARRIER,
    _EXIT_BOX,
    _AUTO_FLOOR,

    // ----------------------------
    // Encapsulating type, covering any collision IDs that can be stood on.
    // This will be checked against the collision object TYPE, not the NAME.
    _OBSTACLE,

    // ----------------------------
    // As above but for objects that can't be stood on and are not entities
    _DECORATION,

    // As above, but for entities
    _ENTITY,

    // ----------------------------
    // Interactive objects
    _PICKUP,
    _WEAPON,
    _INTERACTIVE,
    _PRISONER,
    _PLATFORM,

    // ----------------------------
    // Messages
    _STORM_DEMON_WARNING,
    _PRESS_FOR_TREASURE,
    _PRESS_FOR_PRISONER,
    _PRESS_FOR_GUIDE,
    _KEY_NEEDED,

    // ----------------------------

    G_DUMMY,
    G_NO_ID
}
