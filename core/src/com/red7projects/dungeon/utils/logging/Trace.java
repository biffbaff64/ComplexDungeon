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

package com.red7projects.dungeon.utils.logging;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class Trace
{
    /*
     * NB: Do not make these final, as they can be modified
     */
    private static final String _LOGFILE_PATH_ = "C:/Development/LogFiles/";
    private static final String debugTag       = "DEBUG";
    private static final String errorTag       = "ERROR";

    private static File logFile;

    /**
     * Write a debug string to logcat or console.
     * The string can contain format options.
     *
     * @param formatString  The string, or format string, to display.
     * @param args          Arguments for use in format strings.
     */
    public static void dbg(String formatString, Object... args)
    {
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
        {
            StringBuilder debugString = new StringBuilder();

            debugString.append("     [ ");
            debugString.append(formatString);

            if (args.length > 0)
            {
                for (Object arg : args)
                {
                    debugString.append(" ");
                    debugString.append(arg);
                }
            }

            debugString.append(" ]");

            Gdx.app.debug(debugTag, debugString.toString());

            writeString(debugString.toString());
            writeString("\n");
        }
    }

    /**
     * Write an error string to logcat or console.
     *
     * @param string    The string to write.
     */
    public static void err(String string)
    {
        if (Gdx.app.getLogLevel() != Application.LOG_ERROR)
        {
            StringBuilder sb = new StringBuilder();

            sb.append("     [ ");
            sb.append(string);
            sb.append(" ]");

            Gdx.app.debug(errorTag, sb.toString());
        }
    }

    public static void __LINE__(Object ... args)
    {
        StringBuilder sb = new StringBuilder("Line: ");
        sb.append(new Throwable().getStackTrace()[1].getLineNumber());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the filename and line number of the Trace call
     * to logcat or console.
     */
    public static void __FILE_LINE__()
    {
        dbg
            (
                "Func: %s%s%d",
                new Throwable().getStackTrace()[1].getFileName(),
                "::",
                new Throwable().getStackTrace()[1].getLineNumber()
            );
    }

    /**
     * Write the current filename to logcat or console.
     */
    public static void __FILE__()
    {
        dbg("File: %s", new Throwable().getStackTrace()[1].getFileName());
    }

    /**
     * Write the current method name to logcat or console.
     */
    public static void __FUNC__(Object ... args)
    {
        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the current method name to logcat or console,
     * with a dividing line beforehand.
     */
    public static void __FUNC_WithDivider(Object ... args)
    {
        dbg("--------------------------------------------------");

        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the current filename and method name to logcat or console.
     */
    public static void __FILE_FUNC(Object ... args)
    {
        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the current filename and method name to logcat or console,
     * with a dividing line beforehand.
     */
    public static void __FILE_FUNC_WithDivider(Object ... args)
    {
        dbg("--------------------------------------------------");

        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Writes the current file name to logcat or console.
     * Also writes the current method name plus the current line number.
     */
    public static void __FILE_FUNC_LINE(Object ... args)
    {
        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getLineNumber());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Writes a divider line to logcat or console.
     */
    public static void divider()
    {
        divider(50);
    }

    /**
     * Writes a divider line to logcat or console.
     *
     * @param _length The length of the divider.
     */
    public static void divider(int _length)
    {
        divider('-', _length);
    }

    /**
     * Writes a divider line to logcat or console.
     *
     * @param _length The length of the divider.
     * @param _char The character to use for the divider.
     */
    public static void divider(char _char, int _length)
    {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<_length; i++)
        {
            sb.append(_char);
        }

        dbg(sb.toString());
    }

    /**
     * Writes two divider lines, with a message between them, to logcat or console.
     *
     * @param string    The message to write.
     */
    public static void megaDivider(String string)
    {
        divider(50);
        divider(50);
        dbg(string);
        divider(50);
        divider(50);
    }

    public static void finishedMessage()
    {
        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());
        sb.append("::...Finished...");

        dbg(sb.toString());
    }

    public static void openLogfile(String logfileName)
    {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            logFile = new File(_LOGFILE_PATH_ + logfileName);

            if (logFile.exists())
            {
                if (logFile.delete())
                {
                    Gdx.app.debug(debugTag, "LOGGER: Logfile deleted: " + logFile.getName());
                }
            }

            try
            {
                if (logFile.createNewFile())
                {
                    Gdx.app.debug(debugTag, "LOGGER: Logfile created: " + logFile.getName());

                    //noinspection UseOfObsoleteDateTimeApi
                    GregorianCalendar c = new GregorianCalendar();

                    writeString("-----------------------------------------------------------\n");
                    writeString("Filename: " + logFile.toString() + "\n");
                    writeString("Created: " + c.getTime().toString() + "\n");
                    writeString("-----------------------------------------------------------\n\n");
                }
            }
            catch (IOException ex)
            {
                Stats.incMeter(Meters._IO_EXCEPTION.get());
            }
        }
    }

    private static void writeString(String text)
    {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
        {
            if ((logFile != null) && logFile.exists())
            {
                try
                {
                    if (logFile.exists())
                    {
                        /*
                         * BufferedWriter for performance, true to set append to file flag
                         */
                        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                        buf.append(text);
                        buf.write('\r');
                        buf.newLine();
                        buf.close();
                    }
                }
                catch (IOException e)
                {
                    Stats.incMeter(Meters._IO_EXCEPTION.get());
                }
            }
        }
    }
}
