package guiqsassi.gamescraper.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceDto {
    private String currency;
    private String initial;
    @JsonProperty("final")
    private String finalPrice;



    public BigDecimal priceToBigDecimal(){
        Double price = Double.parseDouble(finalPrice)/100;

        return BigDecimal.valueOf(price);
    }
}
