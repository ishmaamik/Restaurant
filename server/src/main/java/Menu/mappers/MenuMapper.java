package Menu.mappers;

import Menu.DTOs.MenuDTO;
import Menu.domain.Menu;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    MenuDTO menuMapper(Menu menu);
}
