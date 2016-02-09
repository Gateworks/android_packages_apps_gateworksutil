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

    /**
     * Gets the Android System property of the given PWM name.
     * Equivalent to a <code>getprop hw.pwm.name</code> command.
     *
     * @param name    The name assigned to the PWM of interest
     * @return        A String containing the path to the sysfs directory for the PWM
     */
    public static String getHwProp(String name) {
        return SystemProperties.get("hw.pwm." + name);
    }

    /**
     * Checks if the PWM is currently enabled.
     *
     * @param name      The name assigned to the PWM of interest
     * @return          True if the PWM is enabled
     */
    public static boolean getEnabled(String name) {
        return readStringFromFile(getHwProp(name) + "enable").equals("1");
    }

    /**
     * Sets a new enabled setting for the PWM.
     *
     * @param name      The name assigned to the PWM of interest
     * @param enabled   The new on/off setting of the PWM (true/false respectively)
     */
    public static void setEnabled(String name, boolean enabled) {
        writeStringToFile(getHwProp(name) + "enable", enabled ? "1" : "0");
    }

    /**
     * Gets the current polarity value of the PWM.
     *
     * @param name      The name assigned to the PWM of interest
     * @return          An enum representing the polarity of the PWM
     */
    public static Polarity getPolarity(String name) {
        return readStringFromFile(getHwProp(name) + "polarity").equals("normal") ?
                Polarity.NORMAL : Polarity.INVERSED;
    }

    /**
     * Sets the new polarity value of the PWM. A polarity of inversed will cause
     * the pwm to act as a simple inverter. For example, a 50% duty cycle signal
     * will simple be phase shifted 180 degrees.
     *
     * @param name      The name assigned to the PWM of interest
     * @param polarity  A Polarity enum representing the new polarity of the PWM
     */
    public static void setPolarity(String name, Polarity polarity) {
        writeStringToFile(getHwProp(name) + "polarity", polarity.toString().toLowerCase());
    }

    /**
     * Gets the current duty cycle value of the PWM.
     *
     * @param name      The name assigned to the PWM of interest
     * @return          The current duty cycle value in nanoseconds
     */
    public static int getDutyCycle(String name) {
        return Integer.parseInt(readStringFromFile(getHwProp(name) + "duty_cycle"));
    }

    /**
     * Sets the duty cycle to be used by the PWM. Setting a duty cycle that is
     * greater than or equal to the current period of the PWM will cause a system
     * warning and the new duty cycle will not be set.
     *
     * @param name      The name assigned to the PWM of interest
     * @param value     The new duty cycle value in nanoseconds
     */
    public static void setDutyCycle(String name, int value) {
        writeStringToFile(getHwProp(name) + "duty_cycle", Integer.toString(value));
    }

    /**
     * Gets the current period being used by the PWM.
     *
     * @param name      The name assigned to the PWM of interest
     * @return          The period of the PWM in nanoseconds
     */
    public static int getPeriod(String name) {
        return Integer.parseInt(readStringFromFile(getHwProp(name) + "period"));
    }

    /**
     * Sets the new period to be used by the PWM. Setting a period that is less
     * than or equal to the current duty cycle of the PWM will cause a system
     * warning and the new period will not be set. The same behavior occurs if
     * a value that is negative or greater than 11 digits (in nanoseconds) is set.
     *
     * @param name      The name assigned to the PWM of interest
     * @param value     The new period value in nanoseconds
     */
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
