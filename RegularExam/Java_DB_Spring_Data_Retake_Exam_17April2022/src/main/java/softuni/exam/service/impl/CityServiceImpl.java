package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCityDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {
    private static final Path CITIES_FILE_PATH
            = Path.of("src", "main", "resources", "files", "json", "cities.json");

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(CITIES_FILE_PATH);
    }

    @Override
    public String importCities() throws IOException {
        ImportCityDTO[] importCityDTOS = this.gson.fromJson(readCitiesFileContent(), ImportCityDTO[].class);

        return Arrays.stream(importCityDTOS)
                .map(this::importValidCity)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidCity(ImportCityDTO importCityDTO) {
        boolean isValid = this.validationUtil.isValid(importCityDTO);

        if(isValid) {
            Optional<City> optionalCity = this.cityRepository.findByCityName(importCityDTO.getCityName());

            if (optionalCity.isEmpty()) {
                City city = this.mapper.map(importCityDTO, City.class);

                Optional<Country> optionalCountry = this.countryRepository.findById(importCityDTO.getCountry());
                city.setCountry(optionalCountry.get());

                this.cityRepository.save(city);

                return String.format("Successfully imported city %s - %d", city.getCityName(), city.getPopulation());
            }

        }

        return "Invalid city";
    }
}
