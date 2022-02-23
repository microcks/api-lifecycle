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
package io.github.microcks.samples.registration;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author laurent
 */
@RegisterForReflection
public class ApprovedUserRegistration extends UserRegistration {
    
    private String id;
    private String registrationDate;

    public ApprovedUserRegistration(String id, UserRegistration source) {
        this.id = id;
        this.setFullName(source.getFullName());
        this.setEmail(source.getEmail());
        this.setAge(source.getAge());
        this.registrationDate = String.valueOf(System.currentTimeMillis());
    }

    public ApprovedUserRegistration(String id, String fullName, String email, int age) {
        this.id = id;
        this.setFullName(fullName);
        this.setEmail(email);
        this.setAge(age);
        this.registrationDate = String.valueOf(System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}
