package softuni.exam.instagraphlite.models.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class ImportPictureDTO {

    @NotBlank
    private String path;

    @Min(500)
    @Max(60000)
    private double size;

    public ImportPictureDTO() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
