# location-utility

## Description:
The utility will allow you to search for locations and also list places for the same. It also allows to search for location based on category, type and other location related attributes.

Default Port: 8080

## Installation:

**Pre-requisites:**
Install the following on your machine
- Java 1.8
- Git
- Maven

Clone the repository on your local machine and run the following commands to start the application:
```
1. mvn clean install
2. mvn spring-boot:run
```

## API endpoints

#### Search for location by name
Returns location details like city,state,country etc.
> GET : http://localhost:8080/api/location/getLocationByName/

**Parameters:**
- name: (Required) name of location passed in path
e.g. http://localhost:8080/api/location/getLocationByName/chicago

#### Get categories list
Returns a list of categories with id and name.
> GET : http://localhost:8080/api/location/getCategories

#### Get place details
Returns a list of places for a given location with specified attributes like category,radius,searchQuery.
> POST: http://localhost:8080/api/location/getPlaces

**Parameters:**
- name: (Required) name of location
- categoryId: (Optional) Category  id for a category obtained from category API.
- radius: (Optional) Radius in meters for bring places in and around that radius of given location.
- limit: (Optional) Limit the number of records to fetch.
- searchQuery:(Optional) Search text will be applied on the list of places obtained.

