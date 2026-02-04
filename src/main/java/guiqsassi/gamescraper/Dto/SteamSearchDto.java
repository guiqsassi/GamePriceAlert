package guiqsassi.gamescraper.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class SteamSearchDto {

    private Integer total;
    private List<ItemDto> items;
}
