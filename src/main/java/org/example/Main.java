package org.example;

import com.amazonaws.services.schemaregistry.serializers.GlueSchemaRegistryKafkaSerializer;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
<<<<<<< HEAD
=======
import com.amazonaws.services.schemaregistry.utils.AvroRecordType;
>>>>>>> 63902f1 (Initial Commit)
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.ReflectionAvroSerializer;
import javafx.scene.input.DataFormat;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private static final String TOPIC = "transactions";
    private static final Properties props = new Properties();
    public static void main(String[] args)throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(TripDataVO.class).withHeader();
<<<<<<< HEAD
        FileInputStream fileInputStream = new FileInputStream("train.csv");
=======
        FileInputStream fileInputStream = new FileInputStream("/home/ec2-user/train.csv");
>>>>>>> 63902f1 (Initial Commit)
        MappingIterator<TripDataVO> mappingIterator = mapper.readerFor(TripDataVO.class).with(schema).readValues(fileInputStream);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GlueSchemaRegistryKafkaSerializer.class.getName());
        props.put(AWSSchemaRegistryConstants.AWS_REGION, "us-east-1");
<<<<<<< HEAD
        props.put(AWSSchemaRegistryConstants.REGISTRY_NAME, "my-registry");
        props.put(AWSSchemaRegistryConstants.SCHEMA_NAME, "my-schema");

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ReflectionAvroSerializer.class);
=======
        props.put(AWSSchemaRegistryConstants.REGISTRY_NAME, "pbregistry");
        props.put(AWSSchemaRegistryConstants.AVRO_RECORD_TYPE, AvroRecordType.GENERIC_RECORD.getName());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
>>>>>>> 63902f1 (Initial Commit)
        try (KafkaProducer<String, TripDataVO> producer = new KafkaProducer<>(props)) {
            while(mappingIterator.hasNext()){
                TripDataVO tripDataElement= mappingIterator.next();
                final ProducerRecord<String,TripDataVO> record = new ProducerRecord<>(TOPIC,tripDataElement.getId().toString(),tripDataElement);
                producer.send(record);
                System.out.println("sent Record with id: "+tripDataElement.id);
            }
            producer.flush();
        }catch (final SerializationException e) {
            e.printStackTrace();
        }
    }
}