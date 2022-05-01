package exam.model.dto.laptop;

import javax.validation.constraints.Size;

public class ShopNameDTO {

    @Size(min = 4)
    private String name;

    public ShopNameDTO(){}

    public String getName() {
        return name;
    }
}
