package org.example;

import com.amazonaws.services.schemaregistry.serializers.GlueSchemaRegistryKafkaSerializer;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import com.amazonaws.services.schemaregistry.utils.AvroRecordType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
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
        FileInputStream fileInputStream = new FileInputStream("/home/ec2-user/train.csv");
        MappingIterator<TripDataVO> mappingIterator = mapper.readerFor(TripDataVO.class).with(schema).readValues(fileInputStream);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GlueSchemaRegistryKafkaSerializer.class.getName());
        props.put(AWSSchemaRegistryConstants.AWS_REGION, "us-east-1");
        props.put(AWSSchemaRegistryConstants.REGISTRY_NAME, "pbregistry");
        props.put(AWSSchemaRegistryConstants.AVRO_RECORD_TYPE, AvroRecordType.GENERIC_RECORD.getName());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "\tb-1.gommtsource.s5acw7.c18.kafka.us-east-1.amazonaws.com:9092,b-2.gommtsource.s5acw7.c18.kafka.us-east-1.amazonaws.com:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
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