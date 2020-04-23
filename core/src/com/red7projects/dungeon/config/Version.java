/*
 *  Copyright 24/04/2018 Red7Projects.
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

package com.red7projects.dungeon.config;

import com.red7projects.dungeon.game.App;
import com.red7projects.dungeon.utils.logging.Trace;

/**
 * Major Version - 0 == Development Version
 *               - 1 == Alpha release
 *               - 2 == Beta release
 *               - 3 == Master release
 *
 * Minor Version - 0 ==
 *               - 1 ==
 *               - 2 ==
 *               - etc...
 *
 * App Version details
 * ------------------------------------------------------------------
 * @version 0.0.1 Internal       initial issue
 */
@SuppressWarnings({"unused", "SameReturnValue", "WeakerAccess"})
public final class Version
{
    public static final int majorVersion    = 0;
    public static final int minorVersion    = 0;
    public static final int issueNumber     = 1;

    static final String appVersion  = "" + majorVersion + "." + minorVersion + "." + issueNumber;
    static final String projectID   = "Dungeon Quest";
    static final String googleAppID = "833586899901";

    //
    // Release Version
    static final String clientID    = "833586899901-2agnae2v5rduhnmthdp2gupe4b959hcp.apps.googleusercontent.com";
    static final String sha1        = "D9:D1:DB:DA:6B:2E:13:F2:DB:92:65:72:FA:A4:2F:53:E9:78:DB:5C";

    //
    // Debug Version
    static final String clientID_debug = "833586899901-l6m9mn1ss8ferq1g7cogv8ir9uf4a56h.apps.googleusercontent.com";
    static final String sha1_debug     = "B8:34:70:6F:59:3A:5F:49:DA:C1:58:75:D1:AC:E8:FA:2B:99:E9:68";

    //
    // Google Base64-encoded RSA public key
    static final String googleRsaPublicKey =
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlilVuIVfxXYWaMTZzPC5yyFs42lcO" +
                    "m4ZlKSfe69XtJNcQLUo+mw1jubmBrpuLkSAYUb8FncNO1ujWhtZu/z0ofy5Lu8Z3ztnme91T9" +
                    "o0ODjL/+3F/tBwB6+qi2XQkWxd0ZN/jRSq4ZecFURU29nKDkIEXdqTgpie+W917MeNAjuWDIp" +
                    "wd1Z2lHYqLAODYKK2ZtSB5AU9k28Q4hxIjq1ZgUdZf7Dw1kwDq2LmD+g2PmT3Z7tzIvmSCDPU" +
                    "grk7kh+73E0o2a3AZGHTNKx+3HEbfRutz1NF07sviQM+H4bIX01G34xD9E59JiUG54f/cZmMUg" +
                    "W4OG8DYnY2pWK/fuDxdwIDAQAB";

    // ------------------------------------------------------

    public static void appDetails(App app)
    {
        Trace.divider('*', 80);
        Trace.divider(80);

        Trace.dbg(getDisplayVersion());
        Trace.dbg("Signed in to Google?: " + app.googleServices.isSignedIn());

        Trace.divider(80);
        Trace.divider('*', 80);
    }

    /**
     * Gets the app Version string for displaying on the settings screen
     *
     * @return  String holding the version details.
     */
    public static String getDisplayVersion()
    {
        return "Version  " + googleAppID + " : " + appVersion + " : " + projectID;
    }

    /**
     * Gets the app Version string
     *
     * @return  String holding the version details.
     */
    public static String getAppVersion()
    {
        return "V." + appVersion;
    }
}
