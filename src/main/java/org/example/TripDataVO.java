package org.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.reflect.ReflectData;

import java.util.Date;

@JsonPropertyOrder({"id","vendor_id","pickup_datetime","dropoff_datetime"
        ,"passenger_count","pickup_longitude","pickup_latitude","dropoff_longitude","dropoff_latitude","store_and_fwd_flag"
,"trip_duration"})
public class TripDataVO{
    String id;
    Integer vendor_id;
    String pickup_datetime;
    String dropoff_datetime;
    Integer passenger_count;
    Double pickup_longitude;
    Double pickup_latitude;
    Double dropoff_longitude;
    Double dropoff_latitude;
    String store_and_fwd_flag;
    Long trip_duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(Integer vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getPickup_datetime() {
        return pickup_datetime;
    }

    public void setPickup_datetime(String pickup_datetime) {
        this.pickup_datetime = pickup_datetime;
    }

    public String getDropoff_datetime() {
        return dropoff_datetime;
    }

    public void setDropoff_datetime(String dropoff_datetime) {
        this.dropoff_datetime = dropoff_datetime;
    }

    public Integer getPassenger_count() {
        return passenger_count;
    }

    public void setPassenger_count(Integer passenger_count) {
        this.passenger_count = passenger_count;
    }

    public Double getPickup_longitude() {
        return pickup_longitude;
    }

    public void setPickup_longitude(Double pickup_longitude) {
        this.pickup_longitude = pickup_longitude;
    }

    public Double getPickup_latitude() {
        return pickup_latitude;
    }

    public void setPickup_latitude(Double pickup_latitude) {
        this.pickup_latitude = pickup_latitude;
    }

    public Double getDropoff_longitude() {
        return dropoff_longitude;
    }

    public void setDropoff_longitude(Double dropoff_longitude) {
        this.dropoff_longitude = dropoff_longitude;
    }

    public Double getDropoff_latitude() {
        return dropoff_latitude;
    }

    public void setDropoff_latitude(Double dropoff_latitude) {
        this.dropoff_latitude = dropoff_latitude;
    }

    public String getStore_and_fwd_flag() {
        return store_and_fwd_flag;
    }

    public void setStore_and_fwd_flag(String store_and_fwd_flag) {
        this.store_and_fwd_flag = store_and_fwd_flag;
    }

    public Long getTrip_duration() {
        return trip_duration;
    }

    public void setTrip_duration(Long trip_duration) {
        this.trip_duration = trip_duration;
    }

    @Override
    public String toString() {
        return "TripDataVO{" +
                "id='" + id + '\'' +
                ", vendor_id=" + vendor_id +
                ", pickup_datetime=" + pickup_datetime +
                ", dropoff_datetime=" + dropoff_datetime +
                ", passenger_count=" + passenger_count +
                ", pickup_longitude=" + pickup_longitude +
                ", pickup_latitude=" + pickup_latitude +
                ", dropoff_longitude=" + dropoff_longitude +
                ", dropoff_latitude=" + dropoff_latitude +
                ", store_and_fwd_flag='" + store_and_fwd_flag + '\'' +
                ", trip_duration=" + trip_duration +
                '}';
    }


}
