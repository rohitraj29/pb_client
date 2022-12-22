package org.example;

import com.amazonaws.services.schemaregistry.serializers.GlueSchemaRegistryKafkaSerializer;
import com.amazonaws.services.schemaregistry.utils.AWSSchemaRegistryConstants;
import com.amazonaws.services.schemaregistry.utils.AvroRecordType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;
import software.amazon.awssdk.services.glue.model.DataFormat;

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
        props.put(AWSSchemaRegistryConstants.DATA_FORMAT, DataFormat.AVRO.name());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "b-1.gommtsource.s5acw7.c18.kafka.us-east-1.amazonaws.com:9092,b-2.gommtsource.s5acw7.c18.kafka.us-east-1.amazonaws.com:9092");
        props.put(AWSSchemaRegistryConstants.SCHEMA_AUTO_REGISTRATION_SETTING, true);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        Schema tripSchema = ReflectData.get().getSchema(TripDataVO.class);

        try (KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(props)) {
            while(mappingIterator.hasNext()){
                GenericRecord tripRecordGeneric = new GenericData.Record(tripSchema);
                TripDataVO tripDataElement= mappingIterator.next();
                tripRecordGeneric.put("id",tripDataElement.getId());
                tripRecordGeneric.put("vendor_id",tripDataElement.getVendor_id());
                tripRecordGeneric.put("pickup_datetime",tripDataElement.getPickup_datetime());
                tripRecordGeneric.put("dropoff_datetime",tripDataElement.getDropoff_datetime());
                tripRecordGeneric.put("passenger_count",tripDataElement.getPassenger_count());
                tripRecordGeneric.put("pickup_longitude",tripDataElement.getPickup_longitude());
                tripRecordGeneric.put("pickup_latitude",tripDataElement.getPickup_latitude());
                tripRecordGeneric.put("dropoff_longitude",tripDataElement.getDropoff_longitude());
                tripRecordGeneric.put("dropoff_latitude",tripDataElement.getDropoff_latitude());
                tripRecordGeneric.put("store_and_fwd_flag",tripDataElement.getStore_and_fwd_flag());
                tripRecordGeneric.put("trip_duration",tripDataElement.getTrip_duration());
                final ProducerRecord<String,GenericRecord> record = new ProducerRecord<>(TOPIC,tripRecordGeneric.get("id").toString(),tripRecordGeneric);
                producer.send(record);
                System.out.println("sent Record with id: "+tripDataElement.id);
            }
            producer.flush();
        }catch (final SerializationException e) {
            e.printStackTrace();
        }
    }
}