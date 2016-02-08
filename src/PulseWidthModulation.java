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
import android.os.SystemProperties;

public class PulseWidthModulation {

    public enum Polarity {NORMAL, INVERSED}

    public static String getHwProp(String name) {
        return SystemProperties.get("hw.pwm." + name);
    }

    public static boolean getEnabled(String name) {
        return readStringFromFile(getHwProp(name) + "enable").equals("1");
    }

    public static void setEnabled(String name, boolean enabled) {
        writeStringToFile(getHwProp(name) + "enable", enabled ? "1" : "0");
    }

    public static Polarity getPolarity(String name) {
        return readStringFromFile(getHwProp(name) + "polarity").equals("normal") ?
                Polarity.NORMAL : Polarity.INVERSED;
    }

    public static void setPolarity(String name, Polarity polarity) {
        writeStringToFile(getHwProp(name) + "polarity", polarity.toString().toLowerCase());
    }

    public static int getDutyCycle(String name) {
        return Integer.parseInt(readStringFromFile(getHwProp(name) + "duty_cycle"));
    }

    public static void setDutyCycle(String name, int value) {
        writeStringToFile(getHwProp(name) + "duty_cycle", Integer.toString(value));
    }

    public static int getPeriod(String name) {
        return Integer.parseInt(readStringFromFile(getHwProp(name) + "period"));
    }

    public static void setPeriod(String name, int value) {
        writeStringToFile(getHwProp(name) + "period", Integer.toString(value));
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
