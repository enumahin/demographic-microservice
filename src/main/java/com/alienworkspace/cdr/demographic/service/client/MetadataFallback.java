package com.alienworkspace.cdr.demographic.service.client;

import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.dto.metadata.LocationDto;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback class for the metadata service.
 */
@Component
public class MetadataFallback implements MetadataFeignClient {

    /**
     * Gets all countries.
     *
     * @return The list of countries.
     */
    @Override
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a country by id.
     *
     * @param id The id of the country.
     * @return The country.
     */
    @Override
    public ResponseEntity<CountryDto> getCountry(String correlationId, int id) {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a location by id.
     *
     * @param countryId The id of the country.
     * @param stateId The id of the state.
     * @param countyId The id of the county.
     * @param cityId The id of the city.
     * @param communityId The id of the community.
     * @return The location.
     */
    @Override
    public ResponseEntity<CountryDto> getPersonLocation(String correlationId, int countryId, Integer stateId,
                                                        Integer countyId, Integer cityId, Integer communityId) {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a location by id.
     *
     * @param countryId The id of the country.
     * @param stateId The id of the state.
     * @param countyId The id of the county.
     * @param cityId The id of the city.
     * @param communityId The id of the community.
     * @param locationId The id of the location.
     * @return The location.
     */
    @Override
    public ResponseEntity<CountryDto> getPersonLocation(int countryId, Integer stateId, Integer countyId,
                                                        Integer cityId, Integer communityId, Integer locationId) {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets all states.
     *
     * @return The list of states.
     */
    @Override
    public ResponseEntity<List<StateDto>> getAllStates() {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a state by id.
     *
     * @param id The id of the state.
     * @return The state.
     */
    @Override
    public ResponseEntity<StateDto> getState(int id) {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets all counties.
     *
     * @return The list of counties.
     */
    @Override
    public ResponseEntity<List<CountyDto>> getAllCounties() {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a county by id.
     *
     * @param id The id of the county.
     * @return The county.
     */
    @Override
    public ResponseEntity<CountyDto> getCounty(int id) {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets all cities.
     *
     * @return The list of cities.
     */
    @Override
    public ResponseEntity<List<CityDto>> getAllCities() {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a city by id.
     *
     * @param id The id of the city.
     * @return The city.
     */
    @Override
    public ResponseEntity<CityDto> getCity(int id) {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets all communities.
     *
     * @return The list of communities.
     */
    @Override
    public ResponseEntity<List<CommunityDto>> getAllCommunities() {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a community by id.
     *
     * @param id The id of the community.
     * @return The community.
     */
    @Override
    public ResponseEntity<CommunityDto> getCommunity(int id) {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets all locations.
     *
     * @return The list of locations.
     */
    @Override
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        return ResponseEntity.ok(null);
    }

    /**
     * Gets a location by id.
     *
     * @param id The id of the location.
     * @return The location.
     */
    @Override
    public ResponseEntity<LocationDto> getLocation(int id) {
        return ResponseEntity.ok(null);
    }
}
