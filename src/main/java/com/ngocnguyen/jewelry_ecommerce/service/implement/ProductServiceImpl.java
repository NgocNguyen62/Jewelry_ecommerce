package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.repository.ProductRepository;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public Product create(Product product) {
        productRepository.save(product);
        String avatar = ImageUpload(product.getId(), product.getFile());
        product.setAvatar(avatar);
        return productRepository.save(product);
    }

    public String ImageUpload(Long productId, MultipartFile uploadFile){
        String folder = "src/main/resources/static/upload";
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
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
