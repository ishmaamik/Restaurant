package Menu.services;

import Menu.domain.Menu;
import Menu.repository.MenuRepo;
import Users.domain.User;
import Users.enums.UserRole;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor    //auto generates constructor for final fields
@Transactional  //Allows rollback, commit etc. transactions (database)
public class MenuService {

    private final MenuRepo menuRepo;

    public List<Menu> getAllMenus(){
        return menuRepo.findAll();
    }

    public Menu createMenu(Menu menu){
        return menuRepo.save(menu);
    }

    public Menu getMenu(UUID menuId){
        return menuRepo.findById(menuId)
                .orElseThrow(()-> new EntityNotFoundException("Menu does not exist"));
    }

    public Menu updatePrice(UUID id, BigDecimal price, User user){
        if(!user.getRoles().contains(UserRole.ADMIN)){
            throw new AccessDeniedException("You're not an Admin");
        }
        Menu menu= getMenu(id);
        menu.changePrice(price);
        return menuRepo.save(menu);
    }

    public Menu activateMenu(UUID id, User user){
        if(!user.getRoles().contains(UserRole.ADMIN)){
            throw new AccessDeniedException("You're not an Admin");
        }

        Menu menu= getMenu(id);
        menu.activate();
        return menuRepo.save(menu);
    }

    public Menu deactivateMenu(UUID id, User user){
        if(!user.getRoles().contains(UserRole.ADMIN)){
            throw new AccessDeniedException("You're not an Admin!");
        }

        Menu menu= getMenu(id);
        menu.deactivate();
        return menuRepo.save(menu);
    }
}
