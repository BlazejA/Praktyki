package com.hacktyki.car.BaseClasses;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Cars implements Serializable {

    private String carMakes;
    private String carModel;
    private String carImageUrl;
    private String carRegistrationNumber;


    public String getCarRegistrationNumber() {
        return carRegistrationNumber;
    }

    public void setCarRegistrationNumber(String carRegistrationNumber) {
        this.carRegistrationNumber = carRegistrationNumber;
    }

    public String getCarImageUrl() {
        return carImageUrl;
    }

    public void setCarImageUrl(String carImageUrl) {
        this.carImageUrl = carImageUrl;
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
