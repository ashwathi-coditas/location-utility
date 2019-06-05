package com.location.dto;

public class LocationFilterDTO {
    String name;
    String categoryId;
    Long radius;
    String searchQuery;
    Long limit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return "LocationFilterDTO{" +
                "name='" + name + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", radius=" + radius +
                ", searchQuery='" + searchQuery + '\'' +
                ", limit=" + limit +
                '}';
    }
}
