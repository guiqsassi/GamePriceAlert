package guiqsassi.gamescraper.Mapper;
import guiqsassi.gamescraper.Dto.GamePriceOutputDto;
import guiqsassi.gamescraper.Entity.GamePrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GamePriceMapper {

    GamePriceOutputDto toDto(GamePrice gamePrice);

}
