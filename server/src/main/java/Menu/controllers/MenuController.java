package Menu.controllers;

import Menu.DTOs.MenuDTO;
import Menu.domain.Menu;
import Menu.mappers.MenuMapper;
import Menu.services.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/menus")
public class MenuController {

    private MenuMapper menuMapper;
    private MenuService menuService;

    @GetMapping("/all")
    public ResponseEntity<List<MenuDTO>> getAllMenus(){
        List<Menu> menuList= menuService.getAllMenus();
        return ResponseEntity.status(200).body(menuMapper.menuListsMapper(menuList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> getMenu(@PathVariable("id") UUID id){
        Menu menu= menuService.getMenu(id);
        return ResponseEntity.status(201).body(menuMapper.menuMapper(menu));
    }
}
