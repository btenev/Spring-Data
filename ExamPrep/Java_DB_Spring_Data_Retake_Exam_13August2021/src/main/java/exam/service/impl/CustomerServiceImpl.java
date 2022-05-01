package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.customer.CustomerRootDTO;
import exam.model.entities.Customer;
import exam.model.entities.Town;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Path CUSTOMER_SERVICE_PATH_FILE
            = Path.of("src", "main", "resources", "files", "json", "customers.json");

    private final CustomerRepository customerRepository;
    private final Gson gson;
    private final ModelMapper mapper;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, Gson gson, ModelMapper mapper,
                               ValidationUtil validationUtil, TownRepository townRepository) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;

        this.mapper = mapper;
        this.gson = gson;
        this.validationUtil = validationUtil;

    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(CUSTOMER_SERVICE_PATH_FILE);
    }

    @Override
    public String importCustomers() throws IOException {
        BufferedReader reader = Files.newBufferedReader(CUSTOMER_SERVICE_PATH_FILE);
        CustomerRootDTO[] customerRootDTO = this.gson.fromJson(reader, CustomerRootDTO[].class);

        return  Arrays.stream(customerRootDTO)
                .map(this::importCustomer)
                .collect(Collectors.joining(System.lineSeparator()));

    }

    private String importCustomer(CustomerRootDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if (isValid) {
            Optional<Customer> optCustomer = this.customerRepository.findByEmail(dto.getEmail());

            if (optCustomer.isEmpty()) {
                Customer customer = this.mapper.map(dto, Customer.class);
                customer.setRegisteredOn(LocalDate.parse(dto.getRegisteredOn(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                Town town = this.townRepository.findByName(dto.getTown().getName());
                customer.setTown(town);

                this.customerRepository.save(customer);

                return "Successfully imported Customer "
                        + customer.getFirstName() + " " + customer.getLastName() + " - " + customer.getEmail();
            }
        }

        return "Invalid Customer";
    }
}
