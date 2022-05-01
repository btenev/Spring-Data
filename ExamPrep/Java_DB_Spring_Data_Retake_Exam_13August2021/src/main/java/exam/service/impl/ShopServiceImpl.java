package exam.service.impl;

import exam.model.dto.shop.ShopRootDTO;
import exam.model.dto.shop.ShopSummaryDTO;
import exam.model.entities.Shop;
import exam.model.entities.Town;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import exam.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private static final Path SHOP_PATH_FILE
            = Path.of("src", "main", "resources", "files", "xml", "shops.xml");

    private final ShopRepository shopRepository;
    private final TownRepository townRepository;

    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, XmlParser xmlParser, ValidationUtil validationUtil,
                           ModelMapper mapper, TownRepository townRepository) throws JAXBException {
        this.shopRepository = shopRepository;
        this.townRepository = townRepository;

        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.mapper = mapper;

    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(SHOP_PATH_FILE);
    }

    @Override
    public String importShops() throws JAXBException, IOException {
        ShopRootDTO shopRootDTO = this.xmlParser.fromFile(SHOP_PATH_FILE, ShopRootDTO.class);

        return shopRootDTO
                .getShops()
                .stream()
                .map(this::importShop)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importShop(ShopSummaryDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if (isValid) {
            Optional<Shop> optionalShop = this.shopRepository.findByName(dto.getName());

            if (optionalShop.isEmpty()) {
                Shop shop = this.mapper.map(dto, Shop.class);
                Town town = this.townRepository.findByName(dto.getTown().getName());
                shop.setTown(town);

                this.shopRepository.save(shop);

                return "Successfully imported Shop " + shop.getName() + " - " + shop.getIncome();
            }
        }

        return "Invalid shop";
    }
}
