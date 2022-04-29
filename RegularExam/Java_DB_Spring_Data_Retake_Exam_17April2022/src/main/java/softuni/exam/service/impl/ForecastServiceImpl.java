package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportForecastRootDTO;
import softuni.exam.models.dto.SummaryForecastDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.WeekDays;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {
    private static final Path FORECASTS_FILE_PATH
            = Path.of("src", "main", "resources", "files", "xml", "forecasts.xml");

    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;

    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, CityRepository cityRepository, XmlParser xmlParser,
                               ValidationUtil validationUtil, ModelMapper mapper) {
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(FORECASTS_FILE_PATH);
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ImportForecastRootDTO importForecastRootDTO
                = this.xmlParser.fromFile(FORECASTS_FILE_PATH, ImportForecastRootDTO.class);

        return importForecastRootDTO
                .getForecasts()
                .stream()
                .map(this::importValidForecast)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidForecast(SummaryForecastDTO summaryForecastDTO) {
        boolean isValid = this.validationUtil.isValid(summaryForecastDTO);

        if(isValid) {
            Optional<Forecast> optionalForecast = this.forecastRepository
                    .findByDaysOfWeekEqualsAndCity_Id(summaryForecastDTO.getDaysOfWeek(), summaryForecastDTO.getCity());

            if (optionalForecast.isEmpty()) {
                Forecast forecast = this.mapper.map(summaryForecastDTO, Forecast.class);

                Optional<City> optionalCity = this.cityRepository.findById(summaryForecastDTO.getCity());

                forecast.setCity(optionalCity.get());

                this.forecastRepository.save(forecast);

                return String.format("Successfully import forecast %s - %.2f",
                        forecast.getDaysOfWeek().name(), forecast.getMaxTemperature());
            }

        }

        return "Invalid forecast";
    }

    @Override
    public String exportForecasts() {
        WeekDays weekDays = WeekDays.SUNDAY;
        List<Forecast> forecasts = this.forecastRepository.
                findByDaysOfWeekEqualsAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(weekDays, 150000);

        return forecasts
                .stream()
                .map(Forecast::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
