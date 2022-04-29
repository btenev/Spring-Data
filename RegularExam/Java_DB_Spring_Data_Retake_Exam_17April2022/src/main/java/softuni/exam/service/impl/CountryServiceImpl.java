package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCountryDTO;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {
    private static final Path COUNTRIES_FILE_PATH
            = Path.of("src", "main", "resources", "files", "json", "countries.json");

    private final CountryRepository countryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, Gson gson, ValidationUtil validationUtil,
                              ModelMapper mapper) {
        this.countryRepository = countryRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(COUNTRIES_FILE_PATH);
    }

    @Override
    public String importCountries() throws IOException {
        ImportCountryDTO[] importCountryDTOS = this.gson.fromJson(readCountriesFromFile(), ImportCountryDTO[].class);

        return Arrays.stream(importCountryDTOS)
                .map(this::importValidCountry)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidCountry(ImportCountryDTO importCountryDTO) {
        boolean isValid = this.validationUtil.isValid(importCountryDTO);
        boolean isAvailable = this.countryRepository.existsCountryByCountryName(importCountryDTO.getCountryName());

        if(isValid && !isAvailable) {
            Country country = this.mapper.map(importCountryDTO, Country.class);

            this.countryRepository.save(country);

            return String.format("Successfully imported country %s - %s",
                    country.getCountryName(), country.getCurrency());
        }

        return "Invalid country";
    }
}
