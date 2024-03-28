package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.dto.CartDTO;
import com.ngocnguyen.jewelry_ecommerce.dto.CartItemDTO;
import com.ngocnguyen.jewelry_ecommerce.entity.Cart;
import com.ngocnguyen.jewelry_ecommerce.entity.CartItems;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.repository.CartItemsRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.CartRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.ProductRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.UserRepository;
import com.ngocnguyen.jewelry_ecommerce.service.CartService;
import com.ngocnguyen.jewelry_ecommerce.service.UserService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final ModelMapper mapper;

    public CartServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public CartItems addItem(Long productId) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            Optional<Cart> cart = cartRepository.findByUserId(currentUser.getUserId());
            if(!cart.isPresent()){
                Cart newCart = new Cart();
                newCart.setUser(userRepository.findById(currentUser.getUserId()).get());
                cartRepository.save(newCart);
            }
            Optional<CartItems> cartItems = cartItemsRepository.findCartItemsByCart_idAndProduct_id(cart.get().getId(), productId);
            Optional<Product> product = productRepository.findById(productId);
            if(product.isPresent()){
                if(cartItems.isPresent()){
                    updateQuantity(productId, CommonConstants.INTIALIZE_VALUE);
                } else {
                    CartItems newItem = new CartItems();
                    newItem.setCart(cart.get());
                    newItem.setProduct(product.get());
                    newItem.setQuantity(CommonConstants.INTIALIZE_VALUE);
                    return cartItemsRepository.save(newItem);
                }
            } else {
                throw new Exception("Sản phẩm không tồn tại");
            }
        } else {
            throw new Exception("Chưa đăng nhập");
        }
        return null;
    }

    @Override
    public void deleteItem(Long productId) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            Optional<Cart> cart = cartRepository.findByUserId(currentUser.getUserId());
            if(cart.isPresent()){
                Optional<CartItems> item = cartItemsRepository.findCartItemsByCart_idAndProduct_id(cart.get().getId(), productId);
                if(item.isPresent()){
                    cartItemsRepository.deleteById(item.get().getId());
                }
                item.orElseThrow();
            }
            cart.orElseThrow();

        } else {
            throw new Exception("Chưa đăng nhập");
        }
    }

    @Override
    @Transactional
    public void deleteAll() throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            Optional<Cart> cart = cartRepository.findByUserId(currentUser.getUserId());
            if(cart.isPresent()){
                cartItemsRepository.deleteCartItemsByCart_id(cart.get().getId());
            }
            cart.orElseThrow();
        } else {
            throw new Exception("Chưa đăng nhập");
        }
    }

    @Override
    public CartItems updateQuantity(Long id, int quantity) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            Optional<Cart> cart = cartRepository.findByUserId(currentUser.getUserId());
            if(cart.isPresent()){
                Optional<CartItems> item = cartItemsRepository.findCartItemsByCart_idAndProduct_id(cart.get().getId(), id);
                if(item.isPresent()){
                    CartItems cartItems = item.get();
                    int newQuantity = cartItems.getQuantity() + quantity;
                    if(newQuantity > 0){
                        cartItems.setQuantity(newQuantity);
                        if(item.get().getProduct().getQuantity() > cartItems.getQuantity()){
                            return cartItemsRepository.save(cartItems);
                        } else {
                            throw new Exception("Không đủ sản phẩm");
                        }

                    } else {
                        deleteItem(id);
                    }
                }
            }
            cart.orElseThrow();
        } else {
            throw new Exception("Chưa đăng nhập");
        }
        return null;
    }

    @Override
    public List<CartItemDTO> getAllCartItems() throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            Optional<Cart> cart = cartRepository.findByUserId(currentUser.getUserId());
            List<CartItems> items = cartItemsRepository.findAllByCart_id(cart.get().getId());
            return items.stream()
                    .map(cartItem -> mapper.map(cartItem, CartItemDTO.class))
                    .collect(Collectors.toList());
        } else {
            throw new Exception("Chưa đăng nhập");
        }

    }

    @Override
    public CartDTO getCart() throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            Optional<Cart> cart = cartRepository.findByUserId(currentUser.getUserId());
            CartDTO dto = mapper.map(cart.get(), CartDTO.class);
            dto.setCartItemDTOS(getAllCartItems());
            return dto;
        } else {
            throw new Exception("Chưa đăng nhập");
        }
    }
}
