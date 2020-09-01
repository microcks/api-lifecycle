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

import javax.xml.bind.annotation.XmlRootElement;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author laurent
 */
@RegisterForReflection
@XmlRootElement
public class Pastry {

    private String name;
    private String description;
    private double price;
    private String size;
    private String status;

    public Pastry() {
    }

    public Pastry(String name, String description, double price, String size, String status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.size = size;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String geDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return this.size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}