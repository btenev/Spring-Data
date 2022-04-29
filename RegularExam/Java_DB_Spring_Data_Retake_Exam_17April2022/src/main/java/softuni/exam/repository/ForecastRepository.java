package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Forecast;
import softuni.exam.models.entity.WeekDays;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByDaysOfWeekEqualsAndCity_Id(WeekDays weekDays, long id);

    List<Forecast> findByDaysOfWeekEqualsAndCityPopulationLessThanOrderByMaxTemperatureDescIdAsc(WeekDays weekDays, int population);

}
