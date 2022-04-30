package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportSellerRootDTO;
import softuni.exam.models.dto.SellerSummaryDTO;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerService {
    public static final Path SELLERS_FILE_PATH
            = Path.of("src","main", "resources", "files", "xml", "sellers.xml");

    private final SellerRepository sellerRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(SELLERS_FILE_PATH);
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        ImportSellerRootDTO importSellerRootDTO = xmlParser.fromFile(SELLERS_FILE_PATH, ImportSellerRootDTO.class);

        return importSellerRootDTO
                .getSellers()
                .stream()
                .map(this::importValidSeller)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidSeller(SellerSummaryDTO dto) {
        boolean isValid = validationUtil.isValid(dto);

        if (isValid) {
            Seller seller = this.modelMapper.map(dto, Seller.class);
            this.sellerRepository.save(seller);

            return String.format("Successfully import seller %s - %s", seller.getLastName(), seller.getEmail());
        }

        return "Invalid seller";
    }
}
