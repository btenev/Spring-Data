package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.ImportPlaneDTO;
import softuni.exam.models.dtos.PlaneSummaryDTO;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
public class PlaneServiceImpl implements PlaneService {
    private static final Path PLANES_FILE_PATH
            = Path.of("src", "main", "resources", "files", "xml", "planes.xml");

    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public PlaneServiceImpl(PlaneRepository planeRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper mapper) {
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(PLANES_FILE_PATH);
    }

    @Override
    public String importPlanes() throws JAXBException, IOException {
        ImportPlaneDTO importPlaneDTO = this.xmlParser.fromFile(PLANES_FILE_PATH, ImportPlaneDTO.class);

        return importPlaneDTO
                .getPlanes()
                .stream()
                .map(this::importValidPlane)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidPlane(PlaneSummaryDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if(isValid) {
            Plane plane = this.mapper.map(dto, Plane.class);

            this.planeRepository.save(plane);

            return String.format("Successfully imported Plane %s", plane.getRegisterNumber());
        }

        return "Invalid Plane";
    }
}
