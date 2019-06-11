package com.location.dto;

public class LocationFilterDTO {
    String name;
    String categoryName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "LocationFilterDTO{" +
                "name='" + name + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
