package com.ngocnguyen.jewelry_ecommerce.service.implement;

import com.ngocnguyen.jewelry_ecommerce.component.CustomUserDetails;
import com.ngocnguyen.jewelry_ecommerce.dto.ProductDTO;
import com.ngocnguyen.jewelry_ecommerce.entity.Product;
import com.ngocnguyen.jewelry_ecommerce.repository.ProductRepository;
import com.ngocnguyen.jewelry_ecommerce.repository.ProductTableRepository;
import com.ngocnguyen.jewelry_ecommerce.service.ProductService;
import com.ngocnguyen.jewelry_ecommerce.utils.CommonConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductTableRepository tableRepository;
    @Autowired
    private final ModelMapper mapper;
    public ProductServiceImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product create(Product product) throws Exception {
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
        productRepository.save(product);
        if(!product.getFile().isEmpty()){
            String avatar = ImageUpload(product.getId(), product.getFile());
            product.setAvatar(avatar);
        }
        if(!product.getFiles()[0].isEmpty()){
            StringBuilder builder = new StringBuilder();
            for (MultipartFile file : product.getFiles()) {
                String image = ImageUpload(product.getId(), file);
                builder.append(image).append(",");
            }

            if(builder.toString().endsWith(",")){
                builder = new StringBuilder(builder.substring(0, builder.length() - 1));
            }
            product.setImages(builder.toString());
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
    @Override
    public int countProducts(){
        return productRepository.findAll().size();
    }
    @Override
    public void updateQuantity(Long id, int quantity) throws Exception {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            product.get().setQuantity(product.get().getQuantity() + quantity);
            productRepository.save(product.get());
        } else {
            throw new Exception("Sản phẩm không tồn tại");
        }
    }

    @Override
    public void updateSales(Long id, int quantity) throws Exception {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            product.get().setSales(product.get().getSales() + quantity);
            productRepository.save(product.get());
        } else {
            throw new Exception("Sản phẩm không tồn tại");
        }
    }

    public int[] productSales(){
        List<Product> products = findAll();
        int[] count = new int[products.size()];
        for(int i = 0; i < count.length; i++){
            count[i] = products.get(i).getSales();
        }
        return count;
    }
    @Override
    public int getAllSales(){
        return Arrays.stream(productSales()).sum();
    }

    @Override
    public List<Product> sortBySales(){
        List<Product> products = productRepository.findAllByProductStatus(CommonConstants.UNLOCK_STATUS);
        products.sort(Comparator.comparingInt(Product::getSales).reversed());
        return products;
    }
    @Override
    public List<Product> topSaleProduct(int limit){
        List<Product> products = sortBySales();
        if(limit < products.size()){
            return products.subList(0, limit);
        }
        return products;
    }

    @Override
    public int[] countInTopSales(int maxTop){
        int size = maxTop;
        List<Product> products = sortBySales();
        if(size > products.size()){
            size = products.size();
        }
        int[] top = new int[size];
        for(int i = 0; i < top.length; i++){
            top[i] = products.get(i).getSales();
        }
        return top;
    }

    @Override
    public String[] nameOfTopSales(int maxTop){
        int size = maxTop;
        List<Product> products = sortBySales();
        if(size > products.size()){
            size = products.size();
        }
        String[] top = new String[size];
        for(int i = 0; i < top.length; i++){
            top[i] = products.get(i).getProductName();
        }
        return top;
    }
    @Override
    public List<Product> searchResult(String keyword){
        if(keyword != null){
            return productRepository.search(keyword);
        }
        return findAll();
    }

    @Override
    public List<Product> sortByNewest(){
        List<Product> products = productRepository.findAllByProductStatus(CommonConstants.UNLOCK_STATUS);
        List<Product> sortedProducts = products.stream()
                .sorted(Comparator.comparing(Product::getUpdateAt, Comparator.nullsLast(Comparator.reverseOrder()))).toList();
        return sortedProducts;
    }

    @Override
    public List<Product> newestProduct(int limit){
        List<Product> sortedProducts = sortByNewest();
        return sortedProducts.subList(0,Math.min(sortedProducts.size(),limit));
    }
    @Override
    public List<Product> getRelativeProduct(Long id) throws Exception {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return productRepository.findAllByCategory_IdAndIdNot(product.get().getCategory().getId(), id);
        } else {
            throw new Exception("Sản phẩm không tồn tại");
        }
    }
    @Override
    public Page<Product> getAllProducts(Pageable pageable){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> list;
        List<Product> products = productRepository.findAllByProductStatus(CommonConstants.UNLOCK_STATUS);
        if(products.size() < startItem){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }
        Page<Product> productPage = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), products.size());
        return productPage;
    }
    @Override
    public Page<Product> getPageProducts(Pageable pageable, List<Product> products){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> list;
        if(products.size() < startItem){
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }
        Page<Product> productPage = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), products.size());
        return productPage;
    }
    @Override
    public List<Product> getProductByCate(Long cateId){
        return productRepository.findAllByCategory_Id(cateId);
    }


    public List<ProductDTO> convertToDTO(List<Product> products){
        List<ProductDTO> dto = new ArrayList<>();
        for(Product product:products){
            ProductDTO item = mapper.map(product, ProductDTO.class);
            dto.add(item);
        }
        return dto;
    }
    @Override
    public DataTablesOutput<ProductDTO> getAllProductDTO(DataTablesInput input){
        DataTablesOutput<Product> productOutput = tableRepository.findAll(input);
        List<ProductDTO> productDTOS = convertToDTO(productOutput.getData());
        DataTablesOutput<ProductDTO> output = new DataTablesOutput<>();
        output.setData(productDTOS);
        output.setDraw(productOutput.getDraw());
        output.setError(productOutput.getError());
        output.setRecordsFiltered(productOutput.getRecordsFiltered());
        output.setRecordsTotal(productOutput.getRecordsTotal());
        return output;
    }
}
