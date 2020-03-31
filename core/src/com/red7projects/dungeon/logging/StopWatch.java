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

package com.red7projects.dungeon.logging;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class StopWatch
{
    private long starts;

    private StopWatch()
    {
        reset();
    }

    public static StopWatch start()
    {
        return new StopWatch();
    }

    public void reset()
    {
        starts = System.currentTimeMillis();
    }

    public long time()
    {
        long ends = System.currentTimeMillis();

        return ends - starts;
    }

    public long time(TimeUnit unit)
    {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}
