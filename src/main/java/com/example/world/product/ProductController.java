package com.example.world.product;

import com.example.world.product.specification.macMin.MacMinForm;
import com.example.world.product.specification.macMin.MacMinService;
import com.example.world.product.specification.macRecommended.MacRecommendedForm;
import com.example.world.product.specification.macRecommended.MacRecommendedService;
import com.example.world.product.specification.windowMin.WindowMinForm;
import com.example.world.product.specification.windowMin.WindowMinService;
import com.example.world.product.specification.windowRecommended.WindowRecommendedForm;
import com.example.world.product.specification.windowRecommended.WindowRecommendedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final MacMinService macMinService;
    private final MacRecommendedService macRecommendedService;
    private final WindowMinService windowMinService;
    private final WindowRecommendedService windowRecommendedService;

    @GetMapping("/list/All")
    public String allList(Model model, @RequestParam(value = "page",defaultValue = "0")int page){
        Page<Product> paging = this.productService.AllTheme(page);
        model.addAttribute("paging",paging);
        return "product_list";
    }


    @GetMapping(value = "/list/{theme}")
    public String productList(Model model, @PathVariable("theme") String key, @RequestParam(value = "page",defaultValue = "0")int page){
        String themeKey;
        if(key.equals("rpg")){
            themeKey = "RPG";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging",paging);
        }else if(key.equals("fps")){
            themeKey="FPS";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging",paging);
        }else if(key.equals("thriller")){
            themeKey="Thriller";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging",paging);
        }else if(key.equals("arcade")){
            themeKey="Arcade";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        }else if(key.equals("sports")){
            themeKey="Sports";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging",paging);
        }else if(key.equals("simulation")){
            themeKey="Simulation";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging",paging);
        }else if(key.equals("etc")){
            themeKey="ETC";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging",paging);
        }

        return "product_list";
    }


// all priduct/list/


    @GetMapping("/create")
    public String create(Model model, ProductForm productForm) {

        java.util.List<WindowMinForm> windowMinList = new ArrayList<>();
        java.util.List<WindowRecommendedForm> windowRecommendedList = new ArrayList<>();
        java.util.List<MacMinForm> macMinList = new ArrayList<>();
        List<MacRecommendedForm> macRecommendedList = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            windowMinList.add(new WindowMinForm());
            windowRecommendedList.add(new WindowRecommendedForm());
            macMinList.add(new MacMinForm());
            macRecommendedList.add(new MacRecommendedForm());
        }
        model.addAttribute("windowMinList", windowMinList);
        model.addAttribute("windowRecommendedList", windowRecommendedList);
        model.addAttribute("macMinList", macMinList);
        model.addAttribute("macRecommendedList", macRecommendedList);
        return "product_form";
    }

//    @PostMapping("/create")
//    public String create(Model model,
//                         @Valid ProductForm productForm,
//                         BindingResult bindingResult) {
//
////                if (bindingResult.hasErrors()) {
////                    List<Field> fieldList = this.fieldService.getList();
////                    model.addAttribute("fieldList", fieldList);
////                    System.out.println("error");
////                    return "trainer_form";
////                }
////                System.out.println("들어옴");
////                System.out.println("name = " + trainerForm.getName());
////                System.out.println("center = " + trainerForm.getCenter());
////                System.out.println("gender = " + trainerForm.getGender());
////                System.out.println("introAbstract = " + trainerForm.getIntroAbstract());
////                System.out.println("introDetail = " + trainerForm.getIntroDetail());
////                System.out.println("zoneCodeAddress = " + trainerForm.getAddress().getZoneCode());
////                System.out.println("MainAddress = " + trainerForm.getAddress().getMainAddress());
////                System.out.println("subAddress = " + trainerForm.getAddress().getSubAddress());
////                System.out.println("latitude = " + trainerForm.getAddress().getLatitude());
////                System.out.println("longitude = " + trainerForm.getAddress().getLongitude());
//
//        int index = 0;
//        for (WindowMinForm windowMinForm : productForm.getWindowMinList()) {
//            if (productForm.getTime() != null && productForm.getPrice() != null) {
//                System.out.printf("Lesson #%d: time = %s, price = %s\n", ++index, lessonForm.getTime(), lessonForm.getPrice());
//            }
//        }
//
//        index = 0;
//        for (ContactForm contactForm : trainerForm.getContactList()) {
//            if (!contactForm.getType().equals("") && !contactForm.getContent().equals("")) {
//                System.out.printf("contact #%d : type = %s, content = %s\n", ++index, contactForm.getType(), contactForm.getContent());
//            }
//        }
//
//        index = 0;
//        for (CertificateForm certificateForm : trainerForm.getCertificateList()) {
//            if (!certificateForm.getName().equals("") && !certificateForm.getImgUrl().equals("")) {
//                System.out.printf("name #%d : name = %s, imgUrl = %s\n", ++index, certificateForm.getName(), certificateForm.getImgUrl());
//            }
//        }
//
//        // Form 데이터를 실제 객체로 변환
//        List<Field> fieldList = new ArrayList<>();
//        for (String field : trainerForm.getFieldList()) {
//            fieldList.add(this.fieldService.getField(field));
//        }
//
//        // 트레이너 기본 정보 저장
//        Trainer trainer = this.trainerService.create(trainerForm.getName(), trainerForm.getCenter(),
//                trainerForm.getGender(), trainerForm.getIntroAbstract(),
//                trainerForm.getIntroDetail(), fieldList);
//
//        // 레슨 정보 저장
//        for (LessonForm lessonForm : trainerForm.getLessonList()) {
//            this.lessonService.create(lessonForm.getTime(), lessonForm.getPrice(), trainer);
//        }
//
//        // 연락처 정보 저장
//        for(ContactForm contactForm : trainerForm.getContactList()){
//            this.contactService.create(contactForm.getType(), contactForm.getContent(), trainer);
//        }
//
//        // 자격증 정보 저장
//        for(CertificateForm certificateForm : trainerForm.getCertificateList()){
//            this.certificateService.create(certificateForm.getName(), certificateForm.getImgUrl(), trainer);
//        }
//
//
//        return "redirect:/trainer";
//    }
//




}
