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

import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;

import org.jboss.resteasy.annotations.providers.jaxb.Wrapped;
/**
 * @author laurent
 */
@Path("/pastry")
public class PastryResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    @Wrapped(element="pastries")
    public List<Pastry> getPastries() {
        return PastryRepository.getPastries();
    }

    @GET
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public Pastry getPastry(@PathParam("name") String name) {
        return PastryRepository.findByName(name);
    }

    @PATCH
    @Path("/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
    public Pastry patchPastry(@PathParam("name") String name, Pastry pastry) {
        Pastry original = PastryRepository.findByName(name);
        if (original != null) {
            if (pastry.geDescription() != null) {
                original.setDescription(pastry.geDescription());
            }
            if (pastry.getPrice() != 0d) {
                original.setPrice(pastry.getPrice());
            }
            if (pastry.getSize() != null) {
                original.setSize(pastry.getSize());
            }
            if (pastry.getStatus() != null) {
                original.setStatus(pastry.getStatus());
            }
            PastryRepository.save(pastry);
        }
        return original;
    }
}