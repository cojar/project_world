package com.example.world.product;


import com.example.world.file.UploadedFile;
//import com.example.world.product.productImage.ProductImage;
import com.example.world.product.productImage.ProductImage;
import com.example.world.product.specification.macMin.MacMin;
import com.example.world.product.specification.macRecommended.MacRecommended;
import com.example.world.product.specification.windowMin.WindowMin;
import com.example.world.product.specification.windowRecommended.WindowRecommended;
import com.example.world.review.Review;
import com.example.world.qna.Question;
import com.example.world.user.SiteUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    private SiteUser username;

    @Size(max = 30)
    private String productName;
    // 게임 이름

    private String developer;
    // 게임 개발사

    private String theme;
    //게임 테마 (장르)

    @NotNull
    private int price;
    // 상품 가격

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<MacMin> macMinList;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<MacRecommended> macRecommendedList;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<WindowMin> windowMinList;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<WindowRecommended> windowRecommendedList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductImage> productImageList;
//    @OneToOne
//    private UploadedFile panelImage;

    @Column(columnDefinition = "text")
    private String content;
    // 상품 설명

    @CreatedDate
    private LocalDate createDate;
    // 상품 등록일

    private Integer viewCount;
    //조회수

    @ManyToMany
    Set<SiteUser> wish;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Question> questionList;
    // 상품과 연결된 질문들


    /////////////아래부터 파일관련

//    @ElementCollection
//    @CollectionTable(name = "product_filenames", joinColumns = @JoinColumn(name = "product_id"))
//    @Column(name = "filename")
//    private List<String> filenames;
//
//    @ElementCollection
//    @CollectionTable(name = "product_filepaths", joinColumns = @JoinColumn(name = "product_id"))
//    @Column(name = "filepath")
//    private List<String> filepaths;

}
