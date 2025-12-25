package app.Menu.services;

import app.Menu.repository.MenuRepo;
import app.Menu.domain.Menu;
import app.SupabaseStorage.StorageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor    //auto generates constructor for final fields
@Transactional  //Allows rollback, commit etc. transactions (database)
public class MenuService {

    @Autowired
    private MenuRepo menuRepo;
    @Autowired
    private StorageService storageService;
    public List<Menu> getAllMenus(){
        return menuRepo.findAll();
    }

    public Menu createMenu(Menu menu){
        return menuRepo.save(menu);
    }

    public String uploadImage(MultipartFile file){
       return storageService.uploadFile(file);
    }

    public Menu getMenu(UUID menuId){
        return menuRepo.findById(menuId)
                .orElseThrow(()-> new EntityNotFoundException("Menu does not exist"));
    }

    public Menu updatePrice(UUID id, BigDecimal price){
        Menu menu= getMenu(id);
        menu.changePrice(price);
        return menuRepo.save(menu);
    }

    public Menu activateMenu(UUID id){
        Menu menu= getMenu(id);
        menu.activate();
        return menuRepo.save(menu);
    }

    public Menu deactivateMenu(UUID id){
        Menu menu= getMenu(id);
        menu.deactivate();
        return menuRepo.save(menu);
    }
}
