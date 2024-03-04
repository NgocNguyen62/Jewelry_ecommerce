package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.repository.ProductRepository;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product create(Product product) throws Exception {
        productRepository.save(product);
        String avatar = ImageUpload(product.getId(), product.getFile());
        product.setAvatar(avatar);
        StringBuilder builder = new StringBuilder();
        for (MultipartFile file : product.getFiles()) {
           String image = ImageUpload(product.getId(), file);
           builder.append(image).append(",");
        }

        if(builder.toString().endsWith(",")){
            builder = new StringBuilder(builder.substring(0, builder.length() - 1));
        }
        product.setImages(builder.toString());
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
            if (product.getId() == null) {
                product.setCreateAt(LocalDateTime.now());
                product.setCreateBy(currentUser.getUserId());
            } else {
                Optional<Product> temp = productRepository.findById(product.getId());
                if(temp.isPresent()){
                    product.setCreateAt(temp.get().getCreateAt());
                    product.setCreateBy(temp.get().getCreateBy());
                    product.setUpdateAt(LocalDateTime.now());
                    product.setUpdateBy(currentUser.getUserId());
                }
                temp.orElseThrow();
            }
        } else {
            throw new Exception("Chưa đăng nhập");
        }
        return productRepository.save(product);
    }

    public String ImageUpload(Long productId, MultipartFile uploadFile){
        String folder = CommonConstants.FOLDER_UPLOAD;
        String fileName = "";
        try{
            byte[] bytes = uploadFile.getBytes();
            File file = new File(folder+"/"+productId);
            if(!file.exists()){
                file.mkdir();
            }
            fileName = folder + "/" + productId + "/" + uploadFile.getOriginalFilename();
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(
                            new File(fileName))
            );

            stream.write(bytes);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "/upload" + "/" + productId + "/" + uploadFile.getOriginalFilename();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void delete(Long id) throws Exception {
        File file = new File(CommonConstants.FOLDER_UPLOAD+"/"+id);
        try {
            FileSystemUtils.deleteRecursively(file);
        } catch (Exception e){
            throw new Exception(e);
        }
        productRepository.deleteById(id);
    }
}
