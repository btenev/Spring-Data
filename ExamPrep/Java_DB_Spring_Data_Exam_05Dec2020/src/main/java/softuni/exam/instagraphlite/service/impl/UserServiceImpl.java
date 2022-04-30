package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.ImportUserDTO;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Path USERS_FILE_PATH = Path.of("src", "main", "resources", "files", "users.json");

    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PictureRepository pictureRepository, Gson gson, ValidationUtil validationUtil, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.mapper = mapper;
    }

    @Override
    public boolean areImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(USERS_FILE_PATH);
    }

    @Override
    public String importUsers() throws IOException {
        ImportUserDTO[] importUserDTOS = this.gson.fromJson(readFromFileContent(), ImportUserDTO[].class);

        return Arrays.stream(importUserDTOS)
                .map(this::importValidUser)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidUser(ImportUserDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if(isValid) {
            Optional<Picture> optionalPicture = this.pictureRepository.findPictureByPath(dto.getProfilePicture());

            if(optionalPicture.isPresent()) {
                User user = this.mapper.map(dto, User.class);
                user.setProfilePicture(optionalPicture.get());

                this.userRepository.save(user);

                return String.format("Successfully imported User: %s", user.getUsername());
            }

        }

        return "Invalid User";
    }

    @Override
    @Transactional
    public String exportUsersWithTheirPosts() {
        List<User> users = this.userRepository.findByOrderByUserCountDescUserId();

        return users
                .stream()
                .map(User::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
