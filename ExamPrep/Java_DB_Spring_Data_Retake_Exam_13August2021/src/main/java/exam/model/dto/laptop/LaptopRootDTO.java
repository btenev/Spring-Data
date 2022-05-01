package exam.model.dto.laptop;

import exam.model.entities.WarrantyType;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class LaptopRootDTO {
    @Size(min = 9)
    private String macAddress;

    @Positive
    private double cpuSpeed;

    @Min(8)
    @Max(128)
    private int ram;

    @Min(128)
    @Max(1024)
    private int storage;

    @Size(min = 10)
    private String description;

    @Positive
    private BigDecimal price;

    @NotNull
    private WarrantyType warrantyType;

    private ShopNameDTO shop;

    public LaptopRootDTO() {}

    public String getMacAddress() {
        return macAddress;
    }

    public double getCpuSpeed() {
        return cpuSpeed;
    }

    public int getRam() {
        return ram;
    }

    public int getStorage() {
        return storage;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public WarrantyType getWarrantyType() {
        return warrantyType;
    }

    public ShopNameDTO getShop() {
        return shop;
    }
}
