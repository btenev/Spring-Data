package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.ImportTicketRootDTO;
import softuni.exam.models.dtos.TicketSummaryDTO;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    private static final Path TICKETS_FILE_PATH
            = Path.of("src", "main", "resources", "files", "xml", "tickets.xml");

    private final TicketRepository ticketRepository;
    private final TownRepository townRepository;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;

    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TownRepository townRepository,
                             PassengerRepository passengerRepository, PlaneRepository planeRepository, XmlParser xmlParser,
                             ValidationUtil validationUtil, ModelMapper mapper) {
        this.ticketRepository = ticketRepository;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(TICKETS_FILE_PATH);
    }

    @Override
    public String importTickets() throws JAXBException, IOException {
        ImportTicketRootDTO importTicketRootDTO = this.xmlParser.fromFile(TICKETS_FILE_PATH, ImportTicketRootDTO.class);

        return importTicketRootDTO
                .getTickets()
                .stream()
                .map(this::importValidTicket)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidTicket(TicketSummaryDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if(isValid) {
            Ticket ticket = this.mapper.map(dto, Ticket.class);

            Optional<Town> optionalFromTown = this.townRepository.findByName(dto.getFromTown().getName());
            ticket.setFromTown(optionalFromTown.get());

            Optional<Town> optionalToTown = this.townRepository.findByName(dto.getToTown().getName());
            ticket.setToTown(optionalToTown.get());

            Optional<Passenger> optionalPassenger = this.passengerRepository.findByEmail(dto.getPassenger().getEmail());
            ticket.setPassenger(optionalPassenger.get());

            Optional<Plane> optionalPlane = this.planeRepository.findByRegisterNumber(dto.getPlane().getRegisterNumber());
            ticket.setPlane(optionalPlane.get());

            this.ticketRepository.save(ticket);

            return String.format("Successfully imported Ticket %s - %s",
                    ticket.getFromTown().getName(), ticket.getToTown().getName());
        }

        return "Invalid Ticket";
    }
}
