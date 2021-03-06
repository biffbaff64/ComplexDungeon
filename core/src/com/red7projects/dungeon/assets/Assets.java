package com.red7projects.dungeon.assets;
/*
 *  Copyright 27/11/2018 Red7Projects.
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

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Assets
{
    <T> T loadSingleAsset(String asset, Class<?> type);

    void loadAtlas(String atlasName);

    void unloadAsset(String asset);

    TextureAtlas getButtonsAtlas();

    TextureAtlas getAnimationsAtlas();

    TextureAtlas getObjectsAtlas();

    TextureAtlas getTextAtlas();

    TextureAtlas getAchievementsAtlas();

    TextureRegion buttonRegion(String _name);

    TextureRegion animationRegion(String _name);

    TextureRegion objectRegion(String _name);

    TextureRegion textRegion(String _name);

    TextureRegion achievementsRegion(final String _name);
}
