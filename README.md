# location-utility

## Description:
The utility will allow you to search for locations and also list places for the same. It also allows to search for location based on category, type and other location related attributes.

Default Port: 8081

## Installation:

**Pre-requisites:**
Install the following on your machine
- Java 1.8
- Git
- Maven

Clone the repository on your local machine and run the following commands to start the application:

##### Build the application without test-cases
``` mvn clean install -DskipTests```
##### Run the application
``` mvn spring-boot:run```
##### Run tests
``` mvn test```


## API endpoints

#### Get place details for location
Returns a list of places for a given location with attribute like category(e.g.Building,Aquarium,Art Gallery etc).
> POST: http://localhost:8081/api/location/getPlaces

**Parameters:**
- name: (Required) name of location
- categoryName: (Optional) Search for place by category name.

## Location Provider Service
Using FOURSQUARE place and Google geocode APIs for fetching location details.
1. Link for Foursquare API reference: https://developer.foursquare.com/places-api
2. Link for Google geocode API reference: https://developers.google.com/maps/documentation/geocoding/intro

## Implementation

Bean qualifier is used for different implementations of Service Providers.
And Provider API is provided in application property file (application.yml) along with their auth credentials.
The service will return a merged response from both the APIs or return either of them based on data returned from the response.

> Error codes thrown by FourSquare which are handled in the Application:

- Bad Request (400): Caused when location searched for is incorrect.
- Unauthorized (401): Provided Auth token of FourSquare is invalid.
- Internal Server error (500 & 409): Request was not processed by FourSquare.

> Error Status codes thrown by Google which are handled in the Application:

- ZERO_RESULTS: Caused when location searched for is incorrect.
- OVER_DAILY_LIMIT: Provided Auth token of Google is invalid or account billing is not setup.
- INVALID_REQUEST: Address is missing
- UNKNOWN_ERROR: Request could not be processed by Google.


