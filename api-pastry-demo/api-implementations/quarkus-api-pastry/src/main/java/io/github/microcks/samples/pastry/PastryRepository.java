/*
MIT License

Copyright (c) 2020 Laurent BROUDOUX

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.microcks.samples.pastry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author laurent
 */
public class PastryRepository {

    private static Map<String, Pastry> pastries = new HashMap<>();

    static {
        pastries.put("Baba Rhum", new Pastry("Baba Rhum", "Delicieux Baba au Rhum pas calorique du tout", 3.2, "L", "available"));
        pastries.put("Divorces", new Pastry("Divorces", "Delicieux Divorces pas calorique du tout", 2.8, "M", "available"));
        pastries.put("Tartelette Fraise", new Pastry("Tartelette Fraise", "Delicieuse Tartelette aux Fraises fraiches", 2, "S", "available"));
        pastries.put("Eclair Cafe", new Pastry("Eclair Cafe", "Delicieux Eclair au Cafe pas calorique du tout", 2.5, "M", "available"));
        pastries.put("Millefeuille", new Pastry("Millefeuille", "Delicieux Millefeuille pas calorique du tout", 4.4, "L", "available"));
    }

    public static List<Pastry> getPastries() {
        return new ArrayList<Pastry>(pastries.values());
    }
  
    public static Pastry findByName(String name) {
        if (pastries.containsKey(name)) {
            return pastries.get(name);
        }
        return null;
    }

    public static void save(Pastry pastry) {
        pastries.put(pastry.getName(), pastry);
    }
}