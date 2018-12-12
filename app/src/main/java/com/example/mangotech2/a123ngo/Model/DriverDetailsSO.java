package com.example.mangotech2.a123ngo.Model;

/**
 * Created by Sameer on 11/22/2017.
 */

public class DriverDetailsSO {
    //public HttpPostedFileBase img_nic_front { get; set; }
    //public HttpPostedFileBase img_letterof_concert_from_owner { get; set; }
    //public HttpPostedFileBase img_nic_back { get; set; }
    //public HttpPostedFileBase img_driving_lisence_front { get; set; }
    //public HttpPostedFileBase img_license { get; set; }
    //public HttpPostedFileBase img_driving_lisence_back { get; set; }
    //--image names
    public String nic_front;
    public String letterof_concert_from_owner;
    public String nic_back;
    public String driving_lisence_front;
    public String license;
    public String driving_lisence_back;
    //
    public int driver_details_id;
    public int driver_status;
    public String password;
    public String nic_number;
    public int user_id;
    public int is_active;
    public int created_by;
    public String created_date;
    public int updated_by;
    public String updated_date;
    public VehicleSO VehicleSO;
    public String license_number;

    public String getNic_front() {
        return nic_front;
    }

    public void setNic_front(String nic_front) {
        this.nic_front = nic_front;
    }

    public String getLetterof_concert_from_owner() {
        return letterof_concert_from_owner;
    }

    public void setLetterof_concert_from_owner(String letterof_concert_from_owner) {
        this.letterof_concert_from_owner = letterof_concert_from_owner;
    }

    public String getNic_back() {
        return nic_back;
    }

    public void setNic_back(String nic_back) {
        this.nic_back = nic_back;
    }

    public String getDriving_lisence_front() {
        return driving_lisence_front;
    }

    public void setDriving_lisence_front(String driving_lisence_front) {
        this.driving_lisence_front = driving_lisence_front;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDriving_lisence_back() {
        return driving_lisence_back;
    }

    public void setDriving_lisence_back(String driving_lisence_back) {
        this.driving_lisence_back = driving_lisence_back;
    }

    public int getDriver_details_id() {
        return driver_details_id;
    }

    public void setDriver_details_id(int driver_details_id) {
        this.driver_details_id = driver_details_id;
    }

    public int getDriver_status() {
        return driver_status;
    }

    public void setDriver_status(int driver_status) {
        this.driver_status = driver_status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNic_number() {
        return nic_number;
    }

    public void setNic_number(String nic_number) {
        this.nic_number = nic_number;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public com.example.mangotech2.a123ngo.Model.VehicleSO getVehicleSO() {
        return VehicleSO;
    }

    public void setVehicleSO(com.example.mangotech2.a123ngo.Model.VehicleSO vehicleSO) {
        VehicleSO = vehicleSO;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }
}
