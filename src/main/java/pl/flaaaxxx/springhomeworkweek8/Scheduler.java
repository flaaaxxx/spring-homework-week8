package pl.flaaaxxx.springhomeworkweek8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.flaaaxxx.springhomeworkweek8.aop.Aop;
import pl.flaaaxxx.springhomeworkweek8.model.Weather;
import pl.flaaaxxx.springhomeworkweek8.model.WeatherDetails;
import pl.flaaaxxx.springhomeworkweek8.model.WeatherInfo;
import pl.flaaaxxx.springhomeworkweek8.repository.WeatherRepo;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Component
public class Scheduler {

    private WeatherRepo weatherRepo;
    @Value("${weather-info.city}")
    private String city;

    @Autowired
    public Scheduler(WeatherRepo weatherRepo) {
        this.weatherRepo = weatherRepo;
    }

    @Aop
    @Scheduled(fixedDelay = 360000)
    public void saveTime() {

        WeatherDetails weatherDetails = getWeatherDetails(city);

        if (weatherDetails != null) {
            weatherRepo.save(new Weather(weatherDetails.getTitle(), weatherDetails.getConsolidatedWeather().get(0).getTheTemp(), LocalDateTime.now()));
            System.out.println("Zapisano w bazie: " + LocalDateTime.now());
        } else {
            System.out.println("Brak zapisu.");
        }
    }

    //    return details about city weather
    private WeatherDetails getWeatherDetails(String city) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherDetails weatherDetails = null;

        WeatherInfo[] weatherInfo = restTemplate.getForObject("https://www.metaweather.com/api/location/search/?query=" + city, WeatherInfo[].class);

        Optional<WeatherInfo> weatherCity = Arrays.asList(weatherInfo).stream().filter(w -> w.getTitle().equalsIgnoreCase(city)).findFirst();
        if (weatherCity.isPresent()) {
//            find weather details
            weatherDetails = restTemplate.getForObject("https://www.metaweather.com/api/location/" + weatherCity.get().getWoeid(), WeatherDetails.class);
        }

        return weatherDetails;
    }

}
