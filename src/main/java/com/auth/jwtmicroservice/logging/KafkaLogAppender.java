package com.auth.jwtmicroservice.logging;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.util.Properties;

public class KafkaLogAppender extends AbstractAppender {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    protected KafkaLogAppender(String name, Layout<?> layout, String bootstrapServers, String topic) {
        super(name, null, layout, true);
        this.topic = topic;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

        this.producer = new KafkaProducer<>(properties);
    }

    @Override
    public void append(LogEvent event) {
        String message = new String(getLayout().toByteArray(event));
        producer.send(new ProducerRecord<>(topic, message));
    }
}