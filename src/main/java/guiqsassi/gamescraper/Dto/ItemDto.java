package guiqsassi.gamescraper.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ItemDto {
    private String type;
    private String name;
    private String id;
    private PriceDto price;
}
