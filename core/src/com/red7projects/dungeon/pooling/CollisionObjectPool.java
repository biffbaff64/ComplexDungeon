/*
 *  Copyright 02/07/2018 Red7Projects.
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

package com.red7projects.dungeon.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.graphics.GraphicID;

public class CollisionObjectPool<T>
{
    public interface ObjectPoolFactory<T>
    {
        T createObject(App _app);

        T createObject(Rectangle _rectangle, App _app);

        T createObject(int x, int y, int width, int height, GraphicID type, App _app);

        void finaliseObject();
    }

    private final Array<T>             freeObjects;
    private final ObjectPoolFactory<T> factory;
    private final int                  maxSize;

    public CollisionObjectPool(ObjectPoolFactory<T> _factory, int _maxSize)
    {
        this.factory = _factory;
        this.maxSize = _maxSize;
        this.freeObjects = new Array<>(_maxSize);
    }

    public T newObject(App _app)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(_app);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(Rectangle _rectangle, App _app)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(_rectangle, _app);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(int x, int y, int width, int height, GraphicID type, App _app)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(x, y, width, height, type, _app);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public void free(T object)
    {
        if (object != null)
        {
            factory.finaliseObject();

            if (freeObjects.size < maxSize)
            {
                freeObjects.add(object);
            }
        }
    }
}
