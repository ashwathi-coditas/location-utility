package com.location.dto;

import java.util.Objects;

public class PlaceDTO {
    String name;
    String category;
    String city;
    String state;
    String country;
    String countryCode;
    String postalCode;
    String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PlaceDTO{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaceDTO placeDTO = (PlaceDTO) o;
        return category.contains(placeDTO.category) &&
                city.equalsIgnoreCase(placeDTO.city) &&
                state.equalsIgnoreCase(placeDTO.state) &&
                country.equalsIgnoreCase(placeDTO.country) &&
                countryCode.equalsIgnoreCase(placeDTO.countryCode) &&
                postalCode.equalsIgnoreCase(placeDTO.postalCode) &&
                address.equalsIgnoreCase(placeDTO.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, state, country, countryCode, postalCode, address);
    }
}
