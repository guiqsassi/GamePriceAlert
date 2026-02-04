package guiqsassi.gamescraper.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PriceDto {
    private String currency;
    private String initial;
    @JsonProperty("final")
    private String finalPrice;
}
