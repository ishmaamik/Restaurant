package Menu.mappers;

import Menu.DTOs.MenuDTO;
import Menu.domain.Menu;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    MenuDTO menuMapper(Menu menu);

    List<MenuDTO> menuListsMapper(List<Menu> menu);
}
