package com.example.mangotech2.a123ngo.Model;

/**
 * Created by Sameer on 11/22/2017.
 */

public class VehicleSO {
    public int vehicle_id ;
    public int vehicle_type_id ;
    public int driver_details_id ;
    public int vehicle_owner_id;
    public String vehicle_name;
    public String registration_number;
    public String vehicle_model;
    public String vehicle_color;
    //public HttpPostedFileBase img_vehicle_tax_paper { get; set; }
    public String vehicle_tax_paper;
    public int vehicle_status;
    public int is_active;
    public int created_by;
    public String created_date;
    public int updated_by;

    public int getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public int getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(int vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public int getDriver_details_id() {
        return driver_details_id;
    }

    public void setDriver_details_id(int driver_details_id) {
        this.driver_details_id = driver_details_id;
    }

    public int getVehicle_owner_id() {
        return vehicle_owner_id;
    }

    public void setVehicle_owner_id(int vehicle_owner_id) {
        this.vehicle_owner_id = vehicle_owner_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_color() {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getVehicle_tax_paper() {
        return vehicle_tax_paper;
    }

    public void setVehicle_tax_paper(String vehicle_tax_paper) {
        this.vehicle_tax_paper = vehicle_tax_paper;
    }

    public int getVehicle_status() {
        return vehicle_status;
    }

    public void setVehicle_status(int vehicle_status) {
        this.vehicle_status = vehicle_status;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(int updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String updated_date;
}
