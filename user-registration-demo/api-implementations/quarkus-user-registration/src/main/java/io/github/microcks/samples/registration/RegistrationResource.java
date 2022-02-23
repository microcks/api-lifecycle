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

import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/**
 * @author laurent
 */
@Path("/registration")
public class RegistrationResource {
    
    private final Logger logger = Logger.getLogger(getClass());

    private List<ApprovedUserRegistration> registrations = Arrays.asList(
        new ApprovedUserRegistration(UUID.randomUUID().toString(), "Jonathan Vila", "jvilalop@redhat.com", 23),
        new ApprovedUserRegistration(UUID.randomUUID().toString(), "Carles Arnal", "carnalp@redhat.com", 23),
        new ApprovedUserRegistration(UUID.randomUUID().toString(), "Laurent Broudoux", "lbroudou@redhat.com", 42)
        );

    @Inject
    KafkaUserRegistrationProducerManager registrationProducer;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApprovedUserRegistration> getRegistrations() {
        logger.info("Fetching approved registrations");
        return registrations;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRegistration(UserRegistration registrationRequest) {
        logger.info("Receiving registration request for " + registrationRequest.getFullName());
        String id = UUID.randomUUID().toString();
        ApprovedUserRegistration registration = new ApprovedUserRegistration(id, registrationRequest);

        try {
            registrationProducer.publish(registration);
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error("Exception while publishing event on Kafka topic: " + t.getMessage());
        }

        return Response.status(Response.Status.CREATED).entity(registration).build();
    }
}
