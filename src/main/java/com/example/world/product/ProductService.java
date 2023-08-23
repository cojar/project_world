package com.example.world.product;


import com.example.world.DataNotFoundException;
import com.example.world.file.FileService;
import com.example.world.order.OrderRepository;
import com.example.world.product.specification.macMin.MacMin;
import com.example.world.product.specification.macMin.MacMinForm;
import com.example.world.product.specification.macRecommended.MacRecommended;
import com.example.world.product.specification.macRecommended.MacRecommendedForm;
import com.example.world.product.specification.windowMin.WindowMin;
import com.example.world.product.specification.windowMin.WindowMinForm;
import com.example.world.product.specification.windowRecommended.WindowRecommended;
import com.example.world.product.specification.windowRecommended.WindowRecommendedForm;
import com.example.world.user.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final FileService fileService;

    //    public Product create(String productName, String developer, String theme, MultipartFile file, int price, String content) throws IOException {
    public Product create(String productName, String developer, String theme, int price, String content) throws IOException {
        Product product = new Product();

//        UploadedFile panelImage = this.fileService.upload(file, "product", "productImage", "productNumber");

//        product.setUsername(username);
        product.setProductName(productName);
        product.setDeveloper(developer);
        product.setTheme(theme);
//        product.setPanelImage(panelImage);
        product.setPrice(price);
        product.setContent(content);
        product.setCreateDate(LocalDate.now());
        this.productRepository.save(product);
        return product;
    }

    public Page<Product> getTheme(int page, String key) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("id"));
        Pageable pageable = PageRequest.of(page, 16, Sort.by(sorts));
        Specification<Product> spec = searchTheme(key);
        return this.productRepository.findAll(spec, pageable);
    }

    public Page<Product>sortVote(int page, String key){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("wish"));
        Pageable pageable = PageRequest.of(page,16,Sort.by(sorts));
        Specification<Product> spec = searchTheme(key);
        return this.productRepository.findAll(spec, pageable);
    }

    public Page<Product> sortVoteAll(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("wish"));
        Pageable pageable = PageRequest.of(page,16,Sort.by(sorts));
        return this.productRepository.findAll(pageable);
    }

    public Page<Product>sortHigh(int page, String key){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("price"));
        Pageable pageable = PageRequest.of(page,16,Sort.by(sorts));
        Specification<Product> spec = searchTheme(key);
        return this.productRepository.findAll(spec, pageable);
    }

    public Page<Product> sortHighAll(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("price"));
        Pageable pageable = PageRequest.of(page,16,Sort.by(sorts));
        return this.productRepository.findAll(pageable);
    }

    public Page<Product> sortLowAll(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("price"));
        Pageable pageable = PageRequest.of(page,16,Sort.by(sorts));
        return this.productRepository.findAll(pageable);
    }


    public Page<Product> sortLow(int page, String key) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("price"));
        Pageable pageable = PageRequest.of(page,16,Sort.by(sorts));
        Specification<Product> spec = searchTheme(key);
        return this.productRepository.findAll(spec, pageable);
    }

    public Specification<Product> searchTheme(String sortkey) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Season 컬럼을 기준으로 검색 조건 생성
            if (sortkey != null) {
                Path<String> seasonPath = root.get("theme");
                Predicate seasonPredicate = criteriaBuilder.equal(seasonPath, sortkey);
                predicates.add(seasonPredicate);
            }

            // 다른 조건들을 추가하고 싶다면 여기에 추가

            // 검색 조건들을 조합하여 최종 검색 조건 생성
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Page<Product> allTheme(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 16,Sort.by(sorts));
        return this.productRepository.findAll(pageable);
    }
    public Product getProduct(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new DataNotFoundException("product not found");
        }
    }

    public Product getProduct(String orderId) {
        Optional<Product> product = this.productRepository.findById(Long.valueOf(orderId));
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new DataNotFoundException("product not found");
        }
    }

    public Page<Product> getList(int page, int size) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        return this.productRepository.findAll(pageable);
    }

    public List<Product> getProductList() {
        return this.productRepository.findAll();
    }

    public void modifyProduct(Long id,
                              ProductForm productForm) throws IOException {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            System.out.println("존재하지 않는 제품입니다.");
            return;
        }

        product.setProductName(productForm.getProductName());
        product.setDeveloper(productForm.getDeveloper());
        product.setTheme(productForm.getTheme());
        product.setPrice(productForm.getPrice());
        product.setContent(productForm.getContent());

        List<WindowMin> windowMinList = new ArrayList<>();
        for (WindowMinForm windowMinForm : productForm.getWindowMinList()) {
            WindowMin windowMin = new WindowMin();
            windowMin.setOperatingSystem(windowMinForm.getOperatingSystem());
            windowMin.setProcessor(windowMinForm.getProcessor());
            windowMin.setMemory(windowMinForm.getMemory());
            windowMin.setGraphics(windowMinForm.getGraphics());
            windowMin.setStorage(windowMinForm.getStorage());
            windowMin.setDirectAccess(windowMinForm.getDirectAccess());
            windowMin.setNetwork(windowMinForm.getNetwork());
            windowMinList.add(windowMin);
        }
        product.setWindowMinList(windowMinList);

        List<WindowRecommended> windowRecommendedList = new ArrayList<>();
        for (WindowRecommendedForm windowRecommendedForm : productForm.getWindowRecommendedList()) {
            WindowRecommended windowRecommended = new WindowRecommended();
            windowRecommended.setOperatingSystem(windowRecommendedForm.getOperatingSystem());
            windowRecommended.setProcessor(windowRecommendedForm.getProcessor());
            windowRecommended.setMemory(windowRecommendedForm.getMemory());
            windowRecommended.setGraphics(windowRecommendedForm.getGraphics());
            windowRecommended.setStorage(windowRecommendedForm.getStorage());
            windowRecommended.setDirectAccess(windowRecommendedForm.getDirectAccess());
            windowRecommended.setNetwork(windowRecommendedForm.getNetwork());
            windowRecommendedList.add(windowRecommended);
        }
        product.setWindowRecommendedList(windowRecommendedList);

        List<MacMin> macMinList = new ArrayList<>();
        for (MacMinForm macMinForm : productForm.getMacMinList()) {
            MacMin macMin = new MacMin();
            macMin.setOperatingSystem(macMinForm.getOperatingSystem());
            macMin.setProcessor(macMinForm.getProcessor());
            macMin.setMemory(macMinForm.getMemory());
            macMin.setGraphics(macMinForm.getGraphics());
            macMin.setStorage(macMinForm.getStorage());
            macMin.setDirectAccess(macMinForm.getDirectAccess());
            macMin.setNetwork(macMinForm.getNetwork());
            macMinList.add(macMin);
        }
        product.setMacMinList(macMinList);

        List<MacRecommended> macRecommendedList = new ArrayList<>();
        for (MacRecommendedForm macRecommendedForm : productForm.getMacRecommendedList()) {
            MacRecommended macRecommended = new MacRecommended();
            macRecommended.setOperatingSystem(macRecommendedForm.getOperatingSystem());
            macRecommended.setProcessor(macRecommendedForm.getProcessor());
            macRecommended.setMemory(macRecommendedForm.getMemory());
            macRecommended.setGraphics(macRecommendedForm.getGraphics());
            macRecommended.setStorage(macRecommendedForm.getStorage());
            macRecommended.setDirectAccess(macRecommendedForm.getDirectAccess());
            macRecommended.setNetwork(macRecommendedForm.getNetwork());
            macRecommendedList.add(macRecommended);
        }
        product.setMacRecommendedList(macRecommendedList);

//        List<ProductImage> productImageList = new ArrayList<>();
//        for (ProductImageForm productImageForm : productForm.getProductImageList()) {
//            ProductImage productImage = new ProductImage();
//
//            productImage.setName(productImageForm.getName());
//            productImage.setImage(productImage.getImage());
//            productImageList.add(productImage);
//        }
//        product.setProductImageList(productImageList);


        //product = productRepository.save(product);
        this.productRepository.save(product);
    }

    public void wish(Product product, SiteUser siteUser) {
        product.getWish().add(siteUser);
        this.productRepository.save(product);
    }
    public void cancelWish(Product product, SiteUser siteUser) {
        product.getWish().remove(siteUser);
        this.productRepository.save(product);
    }

    public List<Product> getProductsByWish(SiteUser wish) {
        return productRepository.findByWish(wish);
    }

    public void delete(Product product) {
        this.productRepository.delete(product);
    }


    //////////메인페이지 검색 , 페이징 구현 //////////////
    public Page<Product> allThemeMain(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 8,Sort.by(sorts));
        return this.productRepository.findAll(pageable);
    }

    private Specification<Product> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Product> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                return cb.or(cb.like(q.get("productName"), "%" + kw + "%"), // 제목
                        cb.like(q.get("developer"), "%" + kw + "%"));     // 내용

            }
        };
    }

    public Page<Product> getSearch(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 16, Sort.by(sorts));
        Specification<Product> spec = search(kw);
        return this.productRepository.findAll(spec, pageable);
    }


    //////////////////여기까지 메인페이지 구현 ///

    public Page<Product> sortHighMain(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("price"));
        Pageable pageable = PageRequest.of(page, 16);
        return this.productRepository.findAll(pageable);
    }

    public Page<Product> sortLowMain(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("price"));
        Pageable pageable = PageRequest.of(page, 16);
        return this.productRepository.findAll(pageable);
    }
}
