package com.location.dto;

public class LocationFilterDTO {
    String name;
    Long radius;
    String searchQuery;
    Long limit;
    String categoryName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRadius() {
        return radius;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
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
                ", radius=" + radius +
                ", searchQuery='" + searchQuery + '\'' +
                ", limit=" + limit +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
