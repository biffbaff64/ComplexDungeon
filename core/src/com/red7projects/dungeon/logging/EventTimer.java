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

import java.util.Arrays;
import java.util.Locale;

@SuppressWarnings("WeakerAccess")
public class EventTimer
{
    private final int MAX_EVENTS = 20;

    private long beginTime;
    private long endTime;
    private long events;

    private       double   averageDuration;
    private final double[] eventsStore; // TODO: 28/02/2020 - Refactor for Array<>()
    private       String   averageDurationString;

    private boolean hasStarted;
    private boolean isFrozen;

    public EventTimer()
    {
        this.beginTime = 0;
        this.endTime = 0;
        this.events = 0;

        this.averageDuration = 0.0;

        this.hasStarted = false;
        this.isFrozen = false;

        eventsStore = new double[MAX_EVENTS];
        Arrays.fill(eventsStore, 0.0f);
    }

    public void start()
    {
        if(!hasStarted)
        {
            this.beginTime = System.currentTimeMillis();
            this.hasStarted = true;
            this.isFrozen = false;
        }
        else
        {
            Trace.__FILE_FUNC(":Cannot start EventTimer before finishing previous.");
            Trace.dbg("**EventTimer is FROZEN**");

            this.isFrozen = true;
        }
    }

    public void end()
    {
        if(!isFrozen)
        {
            this.endTime = System.currentTimeMillis();
            this.hasStarted = false;
            this.events++;

            eventsStore[(int) this.events % MAX_EVENTS] = (this.endTime - this.beginTime);

            double totalTime = 0;

            for (int i = 0; i < MAX_EVENTS; i++)
            {
                totalTime += eventsStore[i];
            }

            this.averageDuration = totalTime / MAX_EVENTS;
            this.averageDurationString = String.format(Locale.UK, "%.2f", this.averageDuration);
        }
    }

    public void report()
    {
        Trace.dbg(averageDurationString);
    }

    public double getAverageDuration()
    {
        return this.averageDuration;
    }

    public String getAverageDurationString()
    {
        return averageDurationString;
    }
}
