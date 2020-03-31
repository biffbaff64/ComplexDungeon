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

package com.red7projects.dungeon.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

@SuppressWarnings({"WeakerAccess"})
public class AssetLoader implements Assets, Disposable
{
    private static final String _BUTTONS_ATLAS      = "data/packedimages/output/buttons.atlas";
    private static final String _ANIMATIONS_ATLAS   = "data/packedimages/output/animations.atlas";
    private static final String _OBJECTS_ATLAS      = "data/packedimages/output/objects.atlas";
    private static final String _TEXT_ATLAS         = "data/packedimages/output/text.atlas";
    private static final String _ACHIEVEMENTS_ATLAS = "data/packedimages/output/achievements.atlas";

    public AssetManager assetManager;

    public AssetLoader()
    {
        this.assetManager = new AssetManager();

        loadAtlas(_BUTTONS_ATLAS);
        loadAtlas(_ANIMATIONS_ATLAS);
        loadAtlas(_TEXT_ATLAS);
        loadAtlas(_OBJECTS_ATLAS);
        loadAtlas(_ACHIEVEMENTS_ATLAS);
    }

    @Override
    public TextureAtlas getButtonsAtlas()
    {
        return assetManager.get(_BUTTONS_ATLAS, TextureAtlas.class);
    }

    @Override
    public TextureAtlas getAnimationsAtlas()
    {
        return assetManager.get(_ANIMATIONS_ATLAS, TextureAtlas.class);
    }

    @Override
    public TextureAtlas getObjectsAtlas()
    {
        return assetManager.get(_OBJECTS_ATLAS, TextureAtlas.class);
    }

    @Override
    public TextureAtlas getTextAtlas()
    {
        return assetManager.get(_TEXT_ATLAS, TextureAtlas.class);
    }

    @Override
    public TextureAtlas getAchievementsAtlas()
    {
        return assetManager.get(_ACHIEVEMENTS_ATLAS, TextureAtlas.class);
    }

    /**
     * Load single asset, and ensures that it is loaded.
     * It then returns an object of the specified type.
     *
     * @param <T>   the type parameter
     * @param asset the asset to load
     * @param type  the class type of the asset to load
     * @return an object of the specified type
     */
    @Override
    public <T> T loadSingleAsset(String asset, Class<?> type)
    {
        if (!assetManager.isLoaded(asset, type))
        {
            assetManager.load(asset, type);
            assetManager.finishLoadingAsset(asset);
        }

        return assetManager.get(asset);
    }

    /**
     * Load TextureAtlas asset.
     *
     * @param atlasName the full name of the specified atlas.
     */
    @Override
    public void loadAtlas(String atlasName)
    {
        loadSingleAsset(atlasName, TextureAtlas.class);
    }

    /**
     * Unload the specified object
     *
     * @param asset the filename of the object to unload
     */
    @Override
    public void unloadAsset(String asset)
    {
        if (assetManager.isLoaded(asset))
        {
            assetManager.unload(asset);
        }
    }

    @Override
    public void dispose()
    {
        assetManager.dispose();
        assetManager = null;
    }
}
