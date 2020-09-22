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

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import io.github.microcks.samples.registration.UserSignedUpEvent;
import io.quarkus.kafka.client.serialization.JsonbSerializer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import org.eclipse.microprofile.config.inject.ConfigProperty;
/**
 * @author laurent
 */
@ApplicationScoped
public class KafkaUserRegistrationProducerManager {
    
    private Producer<String, UserSignedUpEvent> producer;

    @ConfigProperty(name = "kafka.bootstrap-service")
    String bootstrapService;

    @PostConstruct
    public void create() {
        producer = createProducer();
    }

    protected Producer<String, UserSignedUpEvent> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "quarkus-user-registration");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonbSerializer.class.getName());
        return new KafkaProducer<String, UserSignedUpEvent>(props);
    }

    public void publish(ApprovedUserRegistration registration) {
        // Build a new event that is matching the AsyncAPI contract.
        UserSignedUpEvent event = new UserSignedUpEvent();
        event.setId(registration.getId());
        event.setFullName(registration.getFullName());
        event.setEmail(registration.getEmail());
        event.setAge(registration.getAge());
        event.setSendAt(String.valueOf(System.currentTimeMillis()));
        // Publish it !
        producer.send(new ProducerRecord<>("user-signed-up", event.getSendAt(), event));
        producer.flush();
    }
}
