package guiqsassi.gamescraper.Mapper;

import guiqsassi.gamescraper.Dto.GameImageOutputDto;
import guiqsassi.gamescraper.Entity.GameImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameImageMapper {

    GameImageOutputDto toDto(GameImage gameImage);

}
