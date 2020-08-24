package com.hacktyki.car;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Cars implements Serializable {

    private String carMakes;
    private String carModel;
    private String imageUrl;
    public String registrationNumber;


    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCarMakes() {
        return carMakes;
    }

    public void setCarMakes(String carMakes) {
        this.carMakes = carMakes;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}
