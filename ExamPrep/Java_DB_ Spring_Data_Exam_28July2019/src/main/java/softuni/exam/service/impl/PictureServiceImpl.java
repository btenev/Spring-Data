package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.dto.ImportPicturesRootDTO;
import softuni.exam.domain.dto.PictureDTO;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.ValidatorUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    private final Path PICTURES_FILE_PATH
            = Path.of("src","main", "resources", "files", "xml", "pictures.xml");

    private final PictureRepository pictureRepository;

    private final XmlParser xmlParser;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, XmlParser xmlParser, ValidatorUtil validatorUtil, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;

        this.xmlParser = xmlParser;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public String importPictures() throws IOException, JAXBException {
        ImportPicturesRootDTO importPictureRootDTO
                = this.xmlParser.fileFrom(PICTURES_FILE_PATH, ImportPicturesRootDTO.class);

        return importPictureRootDTO
                .getPictures()
                .stream()
                .map(this::importValidPicture)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidPicture(PictureDTO pictureDTO) {
        boolean isValid = this.validatorUtil.isValid(pictureDTO);

        if (isValid) {
            Picture picture = this.modelMapper.map(pictureDTO, Picture.class);

            this.pictureRepository.save(picture);

            return String.format("Successfully imported picture - %s", picture.getUrl());
        }

        return "Invalid picture";
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return Files.readString(PICTURES_FILE_PATH);
    }

}
