package exam.model.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "laptops")
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "mac_address", nullable = false, unique = true)
    private String macAddress;

    @Column(name = "cpu_speed")
    private double cpuSpeed;

    private int ram;

    private int storage;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "warranty_type", nullable = false)
    private WarrantyType warrantyType;

    @ManyToOne(optional = false)
    private Shop shop;

    public Laptop() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public double getCpuSpeed() {
        return cpuSpeed;
    }

    public void setCpuSpeed(double cpuSpeed) {
        this.cpuSpeed = cpuSpeed;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public WarrantyType getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(WarrantyType warrantyType) {
        this.warrantyType = warrantyType;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return  "Laptop - " + this.macAddress + System.lineSeparator() +
                "*Cpu Speed - " + this.cpuSpeed + System.lineSeparator() +
                "**Ram - " + this.ram + System.lineSeparator() +
                "***Storage - " + this.storage + System.lineSeparator() +
                "****Price - " + this.price + System.lineSeparator() +
                "#Shop name - " + this.shop.getName() + System.lineSeparator() +
                "##Town - " + this.shop.getTown().getName() + System.lineSeparator();
    }
}
//"Laptop - {mac address}
//*Cpu speed - {cpu speed}
//**Ram - {ram}
//***Storage - {storage}
//****Price - {price}
//#Shop name - {name of the shop}
//##Town - {the name of the town of shop}
//. . . "