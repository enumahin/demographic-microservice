package com.alienworkspace.cdr.demographic.service.client;

import static com.alienworkspace.cdr.demographic.helpers.Constants.METADATA_BASE_URL;

import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client for interacting with the metadata service.
 */
@FeignClient(name = "metadata", fallback = MetadataFallback.class)
public interface MetadataFeignClient {

    /**
     * Gets all countries.
     *
     * @return The list of countries.
     */
    @GetMapping(METADATA_BASE_URL + "/countries")
    ResponseEntity<List<CountryDto>> getAllCountries();

    /**
     * Gets a country by id.
     *
     * @param id The id of the country.
     * @return The country.
     */
    @GetMapping(METADATA_BASE_URL + "/countries/{id}")
    ResponseEntity<CountryDto> getCountry(@RequestHeader("X-cdr-correlation-id") String correlationId,
                                          @PathVariable int id);

    /**
     * Gets a person's location.
     *
     * @param countryId The id of the country.
     * @param stateId The id of the state.
     * @param countyId The id of the county.
     * @param cityId The id of the city.
     * @param communityId The id of the community.
     * @return The location.
     */
    @GetMapping(METADATA_BASE_URL
            + "/person-location/{countryId}/{stateId}/{countyId}/{cityId}/{communityId}")
    ResponseEntity<CountryDto> getPersonLocation(@RequestHeader("X-cdr-correlation-id") String correlationId,
                                                 @PathVariable int countryId, @PathVariable Integer stateId,
                                                 @PathVariable Integer countyId, @PathVariable Integer cityId,
                                                 @PathVariable Integer communityId);

    /**
     * Gets a person's location.
     *
     * @param countryId The id of the country.
     * @param stateId The id of the state.
     * @param countyId The id of the county.
     * @param cityId The id of the city.
     * @param communityId The id of the community.
     * @param locationId The id of the location.
     * @return The location.
     */
    @GetMapping(METADATA_BASE_URL
            + "/person-location/{countryId}/{stateId}/{countyId}/{cityId}/{communityId}/{locationId}")
    ResponseEntity<CountryDto> getPersonLocation(@PathVariable int countryId, @PathVariable Integer stateId,
                                                 @PathVariable Integer countyId, @PathVariable Integer cityId,
                                                 @PathVariable Integer communityId, @PathVariable Integer locationId);

    /**
     * Gets all states.
     *
     * @return The list of states.
     */
    @GetMapping(METADATA_BASE_URL + "/states")
    ResponseEntity<List<StateDto>> getAllStates();

    /**
     * Gets a state by id.
     *
     * @param id The id of the state.
     * @return The state.
     */
    @GetMapping(METADATA_BASE_URL + "/states/{id}")
    ResponseEntity<StateDto> getState(@PathVariable int id);

    /**
     * Gets all counties.
     *
     * @return The list of counties.
     */
    @GetMapping(METADATA_BASE_URL + "/counties")
    ResponseEntity<List<CountyDto>> getAllCounties();

    /**
     * Gets a county by id.
     *
     * @param id The id of the county.
     * @return The county.
     */
    @GetMapping(METADATA_BASE_URL + "/counties/{id}")
    ResponseEntity<CountyDto> getCounty(@PathVariable int id);

    /**
     * Gets all cities.
     *
     * @return The list of cities.
     */
    @GetMapping(METADATA_BASE_URL + "/cities")
    ResponseEntity<List<CityDto>> getAllCities();

    /**
     * Gets a city by id.
     *
     * @param id The id of the city.
     * @return The city.
     */
    @GetMapping(METADATA_BASE_URL + "/cities/{id}")
    ResponseEntity<CityDto> getCity(@PathVariable int id);

    /**
     * Gets all communities.
     *
     * @return The list of communities.
     */
    @GetMapping(METADATA_BASE_URL + "/communities")
    ResponseEntity<List<CommunityDto>> getAllCommunities();

    /**
     * Gets a community by id.
     *
     * @param id The id of the community.
     * @return The community.
     */
    @GetMapping(METADATA_BASE_URL + "/communities/{id}")
    ResponseEntity<CommunityDto> getCommunity(@PathVariable int id);

    /**
     * Gets all locations.
     *
     * @return The list of locations.
     */
    @GetMapping(METADATA_BASE_URL + "/locations")
    ResponseEntity<List<LocationDto>> getAllLocations();

    /**
     * Gets a location by id.
     *
     * @param id The id of the location.
     * @return The location.
     */
    @GetMapping(METADATA_BASE_URL + "/locations/{id}")
    ResponseEntity<LocationDto> getLocation(@PathVariable int id);
}
