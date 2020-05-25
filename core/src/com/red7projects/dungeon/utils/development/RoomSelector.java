package com.red7projects.dungeon.utils.development;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.game.App;

public class RoomSelector
{
    private SelectBox<String> roomlist;
    private App app;

    public RoomSelector(App _app)
    {
        this.app = _app;
    }

    public void setRoomlist()
    {
        if (Developer.isDevMode())
        {
            Array<String> temp = app.roomManager.roomList;

            roomlist = new SelectBox<>(new Skin(Gdx.files.internal("data/uiskin.json")));
            roomlist.setItems(app.roomManager.roomList);

            app.stage.addActor(roomlist);
        }
    }

    public void update(int x, int y)
    {
        roomlist.setPosition(x, y);
    }
}
