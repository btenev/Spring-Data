package softuni.exam.instagraphlite.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ImportUserDTO {

    @NotBlank
    @Size(min = 2, max = 18)
    private String username;

    @NotBlank
    @Size(min = 4)
    private String password;

    @NotBlank
    private String profilePicture;

    public ImportUserDTO() {
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
