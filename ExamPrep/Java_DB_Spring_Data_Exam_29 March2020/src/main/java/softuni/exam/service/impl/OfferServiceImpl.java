package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportOffersDTO;
import softuni.exam.models.dto.OfferSummary;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Offer;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    public static final Path OFFERS_FILE_PATH
            = Path.of("src","main", "resources", "files", "xml", "offers.xml");

    private final OfferRepository offerRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;
    private final SellerRepository sellerRepository;
    private final CarRepository carRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper mapper, SellerRepository sellerRepository, CarRepository carRepository) {
        this.offerRepository = offerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
        this.sellerRepository = sellerRepository;
        this.carRepository = carRepository;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(OFFERS_FILE_PATH);
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        ImportOffersDTO importOffersDTO = this.xmlParser.fromFile(OFFERS_FILE_PATH, ImportOffersDTO.class);

        return importOffersDTO
                .getOffers()
                .stream()
                .map(this::importValidOffer)
                .collect(Collectors.joining(System.lineSeparator()));


    }

    private String importValidOffer(OfferSummary dto) {
        boolean isValid = validationUtil.isValid(dto);

        if (isValid) {
            Offer offer = this.mapper.map(dto, Offer.class);

            Optional<Car> optionalCar = this.carRepository.findById(dto.getCar().getId());
            offer.setCar(optionalCar.get());

            Optional<Seller> sellerOptional = this.sellerRepository.findById(dto.getSeller().getId());
            offer.setSeller(sellerOptional.get());

            this.offerRepository.save(offer);

            return String.format("Successfully import offer %s - %s",
                    offer.getAddedOn().toString(), offer.isHasGoldStatus());
        }

        return "Invalid offer";
    }
}
