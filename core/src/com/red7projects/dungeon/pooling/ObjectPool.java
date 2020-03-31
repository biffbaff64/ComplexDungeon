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

package com.red7projects.dungeon.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.red7projects.dungeon.graphics.GraphicID;

public class ObjectPool<T>
{
    public interface ObjectPoolFactory<T>
    {
        T createObject();

        T createObject(Rectangle rectangle);

        T createObject(int x, int y, int width, int height, GraphicID type);

//        T createObject(App _app);
//
//        T createObject(GraphicID gid, App _app);
    }

    private final Array<T>             freeObjects;
    private final ObjectPoolFactory<T> factory;
    private final int                  maxSize;

    public ObjectPool(ObjectPoolFactory<T> factory, int maxSize)
    {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new Array<>(maxSize);
    }

    public T newObject()
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject();
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(Rectangle rectangle)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(rectangle);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(int x, int y, int width, int height, GraphicID graphicID)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(x, y, width, height, graphicID);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

//    public T newObject(App _app)
//    {
//        T object;
//
//        if (freeObjects.size == 0)
//        {
//            object = factory.createObject(_app);
//        }
//        else
//        {
//            object = freeObjects.removeIndex(freeObjects.size - 1);
//        }
//
//        return object;
//    }

//    public T newObject(GraphicID gid, App _app)
//    {
//        T object;
//
//        if (freeObjects.size == 0)
//        {
//            object = factory.createObject(gid, _app);
//        }
//        else
//        {
//            object = freeObjects.removeIndex(freeObjects.size - 1);
//        }
//
//        return object;
//    }

    public void free(T object)
    {
        if (object != null)
        {
            if (freeObjects.size < maxSize)
            {
                freeObjects.add(object);
            }
        }
    }
}
