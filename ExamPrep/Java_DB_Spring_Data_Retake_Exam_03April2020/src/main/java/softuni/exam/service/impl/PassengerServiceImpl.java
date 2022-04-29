package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.ImportPassengerDTO;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {
    private static final Path PASSENGERS_FILE_PATH
            = Path.of("src", "main", "resources", "files", "json", "passengers.json");

    private final PassengerRepository passengerRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.passengerRepository = passengerRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(PASSENGERS_FILE_PATH);
    }

    @Override
    public String importPassengers() throws IOException {
        ImportPassengerDTO[] importPassengerDTOS
                = this.gson.fromJson(readPassengersFileContent(), ImportPassengerDTO[].class);

        return Arrays.stream(importPassengerDTOS)
                .map(this::importValidPassenger)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidPassenger(ImportPassengerDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if(isValid) {
            Passenger passenger = this.mapper.map(dto, Passenger.class);

            Optional<Town> optionalTown = this.townRepository.findByName(dto.getTown());
            passenger.setTown(optionalTown.get());

            this.passengerRepository.save(passenger);

            return String.format("Successfully imported Passenger %s - %s",
                    passenger.getLastName(), passenger.getEmail());
        }

        return "Invalid Passenger";
    }

    @Override
    @Transactional
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        List<Passenger> passengerList = this.passengerRepository.findByOrderByTicketsCountDescEmailAsc();
        return passengerList
                .stream()
                .map(Passenger::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
