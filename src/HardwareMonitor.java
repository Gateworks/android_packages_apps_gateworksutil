/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gateworks.gateworksutil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import android.os.SystemProperties;

public class HardwareMonitor {
    /**
     * Gets the Android System property of the given HWMON name.
     * Equivalent to a <code>getprop hw.hwmon.name</code> command.
     *
     * @param name    The name assigned to the HWMON of interest
     * @return        A String containing the path to the sysfs value node for the HWMON
     */
    public static String getHwProp(String name) {
        return SystemProperties.get("hw.hwmon." + name);
    }

    /**
     * Gets the current value read by the HWMON.
     *
     * @param name      The name assigned to the HWMON of interest
     * @return          The value of the HWMON in millivolts or degrees Celcius
     */
    public static int getHwmonValue(String name) {
        return Integer.parseInt(readStringFromFile(getHwProp(name)));
    }

    private static String readStringFromFile(String path) {
        String line = null;

        try {
            FileReader file = new FileReader(path);
            BufferedReader reader = new BufferedReader(file);
            line = reader.readLine().trim();
            reader.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }
}
