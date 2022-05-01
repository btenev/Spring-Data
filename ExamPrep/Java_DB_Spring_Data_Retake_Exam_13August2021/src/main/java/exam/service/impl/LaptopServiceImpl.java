package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.laptop.LaptopRootDTO;
import exam.model.entities.Laptop;
import exam.model.entities.Shop;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService {
    private static final Path LAPTOP_PATH_FILE
            = Path.of("src", "main", "resources", "files", "json", "laptops.json");

    private final LaptopRepository laptopRepository;
    private final ShopRepository shopRepository;

    private final Gson json;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository, Gson json, ValidationUtil validationUtil, ModelMapper mapper,
                             ShopRepository shopRepository) {
        this.laptopRepository = laptopRepository;
        this.shopRepository = shopRepository;

        this.json = json;
        this.validationUtil = validationUtil;
        this.mapper = mapper;

    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(LAPTOP_PATH_FILE);
    }

    @Override
    public String importLaptops() throws IOException {
        BufferedReader reader = Files.newBufferedReader(LAPTOP_PATH_FILE);

        LaptopRootDTO[] laptopRootDTOS = this.json.fromJson(reader, LaptopRootDTO[].class);

        return Arrays.stream(laptopRootDTOS)
                .map(this::importLaptop)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importLaptop(LaptopRootDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if(isValid) {
            Optional<Laptop> optLaptop = this.laptopRepository.findByMacAddress(dto.getMacAddress());

            if(optLaptop.isEmpty()) {
                Laptop laptop = this.mapper.map(dto, Laptop.class);
                Optional<Shop> shop = this.shopRepository.findByName(dto.getShop().getName());
                laptop.setShop(shop.get());

                this.laptopRepository.save(laptop);

                return String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                      laptop.getMacAddress(),laptop.getCpuSpeed(), laptop.getRam(), laptop.getStorage());
            }
        }

        return "Invalid Laptop";
    }

    @Override
    public String exportBestLaptops() {
        List<Laptop> laptops = this.laptopRepository.findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddress();

        return laptops
                .stream()
                .map(Laptop::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
