package app.Menu.controllers;

import app.Menu.DTOs.MenuDTO;
import app.Menu.domain.Menu;
import app.Menu.mappers.MenuMapper;
import app.Menu.services.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/menus")
public class MenuController {

    private MenuMapper menuMapper;
    private MenuService menuService;

    public MenuController(MenuMapper menuMapper, MenuService menuService) {
        this.menuMapper = menuMapper;
        this.menuService = menuService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MenuDTO>> getAllMenus(){
        List<Menu> menuList= menuService.getAllMenus();
        return ResponseEntity.status(200).body(menuMapper.menuListsMapper(menuList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> getMenu(@PathVariable("id") UUID id){
        Menu menu= menuService.getMenu(id);
        return ResponseEntity.status(201).body(menuMapper.toMenuDTO(menu));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuDTO> create(@RequestBody MenuDTO menuDTO){
        Menu created= menuService.createMenu(menuMapper.toMenu(menuDTO));
        return ResponseEntity.ok(menuMapper.toMenuDTO(created));
    }

    @PostMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuDTO> updatePrice(@PathVariable UUID id, @RequestBody Map<String, Object> body){
        Object object= body.get("price");
        BigDecimal price= new BigDecimal(object.toString());
        Menu menu= menuService.updatePrice(id, price);
        return ResponseEntity.ok(menuMapper.toMenuDTO(menu));
        //convert to big decimal to ensure type safety otherwise
        //throws exception
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuDTO> activate(@PathVariable UUID id){
        Menu menu= menuService.activateMenu(id);
        return ResponseEntity.ok(menuMapper.toMenuDTO(menu));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuDTO> deactivate(@PathVariable UUID id){
        Menu menu= menuService.deactivateMenu(id);
        return ResponseEntity.ok(menuMapper.toMenuDTO(menu));
    }

}
