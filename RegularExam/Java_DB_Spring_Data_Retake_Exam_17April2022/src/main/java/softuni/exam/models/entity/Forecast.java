package softuni.exam.models.entity;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "forecasts")
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private WeekDays daysOfWeek;

    @Column(name = "max_temperature")
    private double maxTemperature;

    @Column(name = "min_temperature")
    private double minTemperature;

    @Column(nullable = false)
    private LocalTime sunrise;

    @Column(nullable = false)
    private LocalTime sunset;

    @ManyToOne
    private City city;

    public Forecast() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WeekDays getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(WeekDays daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public LocalTime getSunrise() {
        return sunrise;
    }

    public void setSunrise(LocalTime sunrise) {
        this.sunrise = sunrise;
    }

    public LocalTime getSunset() {
        return sunset;
    }

    public void setSunset(LocalTime sunset) {
        this.sunset = sunset;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return String.format("City: %s" + System.lineSeparator()
                            + "-min temperature:  %.2f" + System.lineSeparator()
                            + "--max temperature: %.2f" + System.lineSeparator()
                            + "---sunrise: %s" + System.lineSeparator()
                            + "----sunset: %s",
                            this.city.getCityName(),
                            this.minTemperature,
                            this.maxTemperature,
                            this.sunrise.toString(),
                            this.sunset.toString());

    }
}
