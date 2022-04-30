package softuni.exam.models.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String make;

    private String model;

    private int kilometers;

    @Column(name = "registered_on")
    private LocalDate registeredOn;

    @OneToMany(mappedBy = "car")
    private List<Picture> pictures;

    public Car() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    @Override
    public String toString() {
        return String.format("Car make - %s, model - %s" + System.lineSeparator() +
                "    Kilometers - %d" + System.lineSeparator() +
                "    Registered on - %s" + System.lineSeparator() +
                "    Number of pictures - %d",
                this.make, this.model,
                this.kilometers,
                this.registeredOn.toString(),
                this.pictures.size());

    }

    //The combination of make, model and kilometers makes a car unique.
}
