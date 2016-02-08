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

public class GeneralPurposeIO {
    public enum Direction {OUT, IN}

    public static String getHwProp(String name) {
        return SystemProperties.get("hw.gpio." + name);
    }

    public static int getGpioValue(int number) {
        return Integer.parseInt(readStringFromFile("/sys/class/gpio/gpio" + number + "/value"));
    }

    public static int getGpioValue(String name) {
        String sysfsPath = getHwProp(name);
        int resolvedNumber = Integer.parseInt(sysfsPath
                .substring(sysfsPath.lastIndexOf("gpio") + 4, sysfsPath.lastIndexOf('/')));

        return getGpioValue(resolvedNumber);
    }

    public static void setGpioValue(int number, int value) {
        writeStringToFile("/sys/class/gpio/gpio" + number + "/value", value == 0 ? "0" : "1");
    }

    public static void setGpioValue(String name, int value) {
        String sysfsPath = getHwProp(name);
        int resolvedNumber = Integer.parseInt(sysfsPath
                .substring(sysfsPath.lastIndexOf("gpio") + 4, sysfsPath.lastIndexOf('/')));

        setGpioValue(resolvedNumber, value);
    }

    public static Direction getGpioDirection(int number) {
        return readStringFromFile("/sys/class/gpio/gpio" + number + "/direction")
                .equals("in") ? Direction.IN : Direction.OUT;
    }
    public static Direction getGpioDirection(String name) {
        String sysfsPath = getHwProp(name);
        int resolvedNumber = Integer.parseInt(sysfsPath
                .substring(sysfsPath.lastIndexOf("gpio") + 4, sysfsPath.lastIndexOf('/')));

        return getGpioDirection(resolvedNumber);
    }

    public static void setGpioDirection(int number, Direction direction) {
        writeStringToFile("/sys/class/gpio/gpio" + number + "/direction",
                direction.toString().toLowerCase());
    }

    public static void setGpioDirection(String name, Direction direction) {
        String sysfsPath = getHwProp(name);
        int resolvedNumber = Integer.parseInt(sysfsPath
                .substring(sysfsPath.lastIndexOf("gpio") + 4, sysfsPath.lastIndexOf('/')));

        setGpioDirection(resolvedNumber, direction);
    }

    public static void setGpio(String name, Direction direction, int value) {
        String sysfsPath = getHwProp(name);
        int resolvedNumber = Integer.parseInt(sysfsPath
                .substring(sysfsPath.lastIndexOf("gpio") + 4, sysfsPath.lastIndexOf('/')));

        setGpio(resolvedNumber, direction, value);
    }

    public static void setGpio(int number, Direction direction, int value) {
        setGpioDirection(number, direction);
        setGpioValue(number, value);
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
