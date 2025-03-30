package org.airwatch.airquality.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AirQualityContoller {

  @GetMapping("/api/air-quality")
  public String getAirQuality() {
    return "Air Quality Data";
  }


}
