package softuni.exam.instagraphlite.models.entities;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(optional = false)
    private Picture profilePicture;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Picture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Picture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        String postList = this.posts.stream()
                .sorted(Comparator.comparingDouble(p -> p.getPicture().getSize()))
                .map(Post::toString)
                .collect(Collectors.joining(System.lineSeparator()));

        return String.format("User: %s" + System.lineSeparator() +
                        "Post count: %d" + System.lineSeparator() +
                        "%s"
                , this.username
                , this.posts.size()
                , postList);
    }
}
