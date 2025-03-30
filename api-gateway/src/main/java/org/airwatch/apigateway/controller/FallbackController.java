package org.airwatch.apigateway.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

  @GetMapping(value = "/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
  public String apiDocsFallback() {
    return "{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"Service Unavailable\",\"version\":\"1.0.0\"},\"paths\":{}}";
  }
}