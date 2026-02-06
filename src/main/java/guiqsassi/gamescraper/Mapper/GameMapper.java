package guiqsassi.gamescraper.Mapper;

import guiqsassi.gamescraper.Dto.GameOutputDto;
import guiqsassi.gamescraper.Entity.Game;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameOutputDto toDto(Game game);
}
