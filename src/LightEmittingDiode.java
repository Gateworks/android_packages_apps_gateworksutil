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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import android.os.SystemProperties;

public class LightEmittingDiode {
    /**
     * Gets the Android System property of the given LED name.
     * Equivalent to a <code>getprop hw.LED.name</code> command.
     *
     * @param name    The name assigned to the LED of interest
     * @return        A String containing the path to the sysfs directory for the LED
     */
    public static String getHwProp(String name) {
        return SystemProperties.get("hw.led." + name);
    }

    /**
     * Gets the available trigger modes offered by the driver of the LED.
     *
     * @param name      The name assigned to the LED of interest
     * @return          The triggers of the LED including the currently selected
     */
    public static List<String> getAllLedTriggers(String name) {
        String triggers = readStringFromFile(getHwProp(name) + "trigger");

        return Arrays.asList(triggers.replace("[", "").replace("]", "").split(" "));
    }

    /**
     * Sets the new on/off value of the LED. Note that setting the LED to off will clear
     * any set trigger.
     *
     * @param name      The name assigned to the LED of interest
     * @param on        The new setting of the LED (true for on, false for off)
     */
    public static void setLedValue(String name, boolean on) {
        writeStringToFile(getHwProp(name) + "brightness", on ? "1" : "0");
    }

    /**
     * Gets the current on/off value read by the LED.
     *
     * @param name      The name assigned to the LED of interest
     * @return          The current setting of the LED (true for on, false for off)
     */
    public static boolean getLedValue(String name) {
        return !readStringFromFile(getHwProp(name) + "brightness").equals("0");
    }

    /**
     * Gets the current trigger mode being used by the LED.
     *
     * @param name      The name assigned to the LED of interest
     * @return          The name of the LED trigger
     */
    public static String getLedTrigger(String name) {
        String triggerString = readStringFromFile(getHwProp(name) + "trigger");

        return  triggerString.substring(triggerString.indexOf('[') + 1, triggerString.indexOf(']'));
    }

    /**
     * Sets the new trigger to be used by the LED. Setting a trigger that is not
     * contained in the list of triggers returned by getAllLedTriggers will throw
     * a system warning and the trigger will not change.
     *
     * @param name      The name assigned to the LED of interest
     * @param trigger   The name of the new trigger to be assigned
     * @see GateworksUtil.LightEmittingDiode#getAllLedTriggers(String)
     */
    public static void setLedTrigger(String name, String trigger) {
        writeStringToFile(getHwProp(name) + "trigger", trigger);
    }


    private static void writeStringToFile (String path, String input) {
        try {
            FileWriter file = new FileWriter(path);
            file.write(input);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
