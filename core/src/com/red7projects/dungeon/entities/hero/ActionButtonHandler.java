/*
 *  Copyright 21/05/2018 Red7Projects.
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

package com.red7projects.dungeon.entities.hero;

import com.badlogic.gdx.utils.Disposable;
import com.red7projects.dungeon.assets.GameAssets;
import com.red7projects.dungeon.game.Actions;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.Gfx;
import com.red7projects.dungeon.graphics.GraphicID;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class ActionButtonHandler implements Disposable
{
    private       Actions actionMode;
    private       Actions previousActionMode;
    private       Actions futureActionMode;
    private final App     app;

    public ActionButtonHandler(App _app)
    {
        this.app = _app;

        this.actionMode         = Actions._NO_ACTION;
        this.previousActionMode = Actions._NO_ACTION;
    }

    public void setAction()
    {
    }

    public void update()
    {
        if (app.getPlayer().isOnFloorButton)
        {
            if (!app.gameProgress.keyCount.isEmpty())
            {
                if (getActionMode() == Actions._NO_ACTION)
                {
                    setActionMode(Actions._OFFER_ABXY_A);
                    setFutureActionMode(Actions._PRESS_FLOOR_SWITCH);
                    app.getPlayer().setLink(app.getPlayer().collisionObject.contactSprite.spriteNumber);

                    app.getHud().messageManager.addZoomMessage
                        (
                            GameAssets._PRESS_FOR_PRISONER_ASSET,
                            5000,
                            (Gfx._VIEW_WIDTH - GameAssets.getAssetSize(GraphicID._PRESS_FOR_PRISONER).getX()) / 2,
                            100
                        );
                }
            }
        }
        else if (app.getPlayer().collision.isNextTo(GraphicID.G_MYSTERY_CHEST) > 0)
        {
            if (getActionMode() == Actions._NO_ACTION)
            {
                setActionMode(Actions._OFFER_ABXY_A);
                setFutureActionMode(Actions._OPEN_MYSTERY_BOX);

                app.getHud().messageManager.addZoomMessage
                    (
                        GameAssets._PRESS_FOR_TREASURE_ASSET,
                        5000,
                        (Gfx._VIEW_WIDTH - GameAssets.getAssetSize(GraphicID._PRESS_FOR_TREASURE).getX()) / 2,
                        100
                    );
            }
        }
        else if ((app.getPlayer().collision.isNextTo(GraphicID.G_TREASURE_CHEST) > 0)
                && (app.getPlayer().collisionObject.contactSprite != null)
                && (app.getPlayer().collisionObject.contactSprite.getSpriteAction() == Actions._STANDING))
        {
            if (getActionMode() == Actions._NO_ACTION)
            {
                setActionMode(Actions._OFFER_ABXY_A);
                setFutureActionMode(Actions._OPEN_TREASURE_CHEST);

                app.getHud().messageManager.addZoomMessage
                    (
                        GameAssets._PRESS_FOR_TREASURE_ASSET,
                        5000,
                        (Gfx._VIEW_WIDTH - GameAssets.getAssetSize(GraphicID._PRESS_FOR_TREASURE).getX()) / 2,
                        100
                    );
            }
        }
        else
        {
            removeAction();
            app.getPlayer().setLink(0);
        }
    }

    public Actions getActionMode()
    {
        return actionMode;
    }

    public void setActionMode(Actions mode)
    {
        previousActionMode = actionMode;
        actionMode         = mode;
    }

    public void setFutureActionMode(Actions mode)
    {
        futureActionMode = mode;
    }

    public Actions getPreviousActionMode()
    {
        return previousActionMode;
    }

    public Actions getFutureActionMode()
    {
        return futureActionMode;
    }

    public void removeAction()
    {
        if (previousActionMode != Actions._NO_ACTION)
        {
            setActionMode(previousActionMode);

            previousActionMode = Actions._NO_ACTION;
        }
        else
        {
            actionMode = Actions._NO_ACTION;
        }
    }

    @Override
    public void dispose()
    {
        actionMode         = null;
        previousActionMode = null;
    }
}
