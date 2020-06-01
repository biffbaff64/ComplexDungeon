
package com.red7projects.dungeon.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.game.StateID;
import com.red7projects.dungeon.graphics.camera.OrthoGameCamera;
import com.red7projects.dungeon.utils.development.DebugRenderer;

public class WorldRenderer implements GameScreenRenderer
{
    private final App app;

    public WorldRenderer(App _app)
    {
        this.app = _app;
    }

    @Override
    public void render(SpriteBatch spriteBatch, OrthoGameCamera gameCamera)
    {
        switch (app.appState.peek())
        {
            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_PAUSED:
            case _STATE_LEVEL_RETRY:
            case _STATE_LEVEL_FINISHED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            case _STATE_ANNOUNCE_MISSILE:
            case _STATE_SETTINGS_PANEL:
            case _STATE_TELEPORTING:
            case _STATE_DEBUG_HANG:
            {
                if (app.appState.peek() == StateID._STATE_GAME)
                {
//                    if (UIButtons.controllerLBPressed)
//                    {
//                        app.baseRenderer.tiledGameCamera.camera.rotate(1.0f);
//                        app.baseRenderer.spriteGameCamera.camera.rotate(1.0f);
//                    }
//                    else if (UIButtons.controllerRBPressed)
//                    {
//                        app.baseRenderer.tiledGameCamera.camera.rotate(-1.0f);
//                        app.baseRenderer.spriteGameCamera.camera.rotate(-1.0f);
//                    }
                }

                app.baseRenderer.gameZoom.stop();
                app.mainGameScreen.draw();

                DebugRenderer.drawBoxes();
            }
            break;

            case _STATE_CLOSING:
            case _STATE_GAME_OVER:
            case _STATE_END_GAME:
            {
            }
            break;

            case _STATE_TITLE_SCREEN:
            default:
                break;
        }
    }
}
