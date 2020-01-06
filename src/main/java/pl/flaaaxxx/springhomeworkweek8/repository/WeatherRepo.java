package pl.flaaaxxx.springhomeworkweek8.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.flaaaxxx.springhomeworkweek8.model.Weather;

@Repository
public interface WeatherRepo extends JpaRepository<Weather, Long> {
}
