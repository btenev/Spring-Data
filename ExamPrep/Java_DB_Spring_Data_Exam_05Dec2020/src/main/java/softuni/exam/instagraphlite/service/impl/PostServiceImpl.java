package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dtos.ImportRootPostDTO;
import softuni.exam.instagraphlite.models.dtos.SummaryPostDTO;
import softuni.exam.instagraphlite.models.entities.Picture;
import softuni.exam.instagraphlite.models.entities.Post;
import softuni.exam.instagraphlite.models.entities.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    public static final Path POSTS_FILE_PATH
            = Path.of("src", "main", "resources", "files", "posts.xml");

    private final PostRepository postRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final XmlParser xmlParser;
    private final ModelMapper mapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PictureRepository pictureRepository, UserRepository userRepository, XmlParser xmlParser, ModelMapper mapper, ValidationUtil validationUtil) {
        this.postRepository = postRepository;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.xmlParser = xmlParser;
        this.mapper = mapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean areImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(POSTS_FILE_PATH);
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        ImportRootPostDTO importRootPostDTOS = this.xmlParser.fromFile(POSTS_FILE_PATH, ImportRootPostDTO.class);

        return importRootPostDTOS
                .getPosts()
                .stream()
                .map(this::importValidPost)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String importValidPost(SummaryPostDTO dto) {
        boolean isValid = this.validationUtil.isValid(dto);

        if(isValid) {
            Optional<User> optionalUser = this.userRepository.findByUsername(dto.getUser().getUsername());
            Optional<Picture> optionalPicture = this.pictureRepository.findPictureByPath(dto.getPicture().getPath());

            if(optionalPicture.isPresent() && optionalUser.isPresent()) {
                Post post = this.mapper.map(dto, Post.class);
                post.setPicture(optionalPicture.get());
                post.setUser(optionalUser.get());

                this.postRepository.save(post);
                return String.format("Successfully imported Post, made by %s", post.getUser().getUsername());
            }
        }

        return "Invalid Post";
    }
}
