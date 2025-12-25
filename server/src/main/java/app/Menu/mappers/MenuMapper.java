package app.Menu.mappers;

import app.Menu.DTOs.MenuDTO;
import app.Menu.domain.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    @Mapping(source = "imageURL", target = "imageURL")
    MenuDTO toMenuDTO(Menu menu);

    List<MenuDTO> menuListsMapper(List<Menu> menu);

    Menu toMenu(MenuDTO menuDTO);
}
