package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.entity.Favorite;
import com.ngocnguyen.jewelry_ecommerce.entity.FavoriteItems;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.entity.User;
import com.ngocnguyen.jewelry_ecommerce.repository.FavoriteItemRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.FavoriteRepository;
import com.ngocnguyen.jewelry_ecommerce.service.FavoriteService;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private FavoriteItemRepository favoriteItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @Override
    public void addFavorite(Long productId) throws Exception {
        User currentUser = userService.getCurrentUser();
        Optional<Product> product = productService.findById(productId);
        if(currentUser != null && product.isPresent()){
            Optional<Favorite> favoriteList = favoriteRepository.findByUser_Id(currentUser.getId());
            if(favoriteList.isEmpty()){
                favoriteList = Optional.of(new Favorite());
                favoriteList.get().setUser(currentUser);
                favoriteRepository.save(favoriteList.get());
            }
            FavoriteItems item = new FavoriteItems();
            item.setFavorite(favoriteList.get());
            item.setProduct(product.get());
            favoriteItemRepository.save(item);
        } else {
            throw new Exception("Sản phẩm không tồn tại");
        }
    }

    @Override
    public void remove(Long productId) throws Exception {
        User currentUser = userService.getCurrentUser();
        if(currentUser != null){
            Optional<Favorite> favorite = favoriteRepository.findByUser_Id(currentUser.getId());
            if(favorite.isPresent()){
                Optional<FavoriteItems> item = favoriteItemRepository.findByFavorite_IdAndProduct_Id(favorite.get().getId(), productId);
                item.ifPresent(favoriteItems -> favoriteItemRepository.deleteById(favoriteItems.getId()));
                item.orElseThrow();
            }
            favorite.orElseThrow();
        } else {
            throw new Exception("Chưa đăng nhập");
        }
    }
    @Override
    public List<Product> getAllFavorite(){
        Favorite favorite = userService.getCurrentUser().getFavorite();
        if(favorite == null){
            favorite = new Favorite();
            favorite.setUser(userService.getCurrentUser());
            favoriteRepository.save(favorite);
        }
        List<FavoriteItems> items = favorite.getFavoriteItems();
        if(items != null){
            List<Product> products = new ArrayList<Product>();
            for (FavoriteItems item:items) {
                products.add(item.getProduct());
            }
            return products;
        }
        return null;
    }
    @Override
    public boolean isInFavorite(Long productId){
        if(userService.getCurrentUser() == null){
            return false;
        } else {
            Favorite favorite = userService.getCurrentUser().getFavorite();
            return favoriteItemRepository.existsByFavorite_IdAndProduct_Id(favorite.getId(), productId);
        }

    }
    @Override
    public int count(){
        if(userService.getCurrentUser() == null){
            return 0;
        } else {
            Favorite favorite = userService.getCurrentUser().getFavorite();
            return favorite.getFavoriteItems().size();
        }
    }
}
