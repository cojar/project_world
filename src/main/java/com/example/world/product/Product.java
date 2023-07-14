package com.example.world.product;


import com.example.world.productReview.ProductReview;
import com.example.world.qna.Qna;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //상품번호

    private String thema;
    //게임 테마 (장르)

    @Size(max = 30)
    private String name;
    // 게임 이름

    @Column(columnDefinition = "text")
    private String content;
    // 상품 내용

    private int price;
    // 상품 가격

    private String developer;
    // 게임 개발사

    private LocalDate createDate;
    // 상품 등록일

    private String device;
    // 상품 권장 장치 (콘솔 , pc )

    private int viewCount;
    //조화수


    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<ProductReview> reviewList;
    // 상품과 연결된 리뷰들

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Qna> qnaList;
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
