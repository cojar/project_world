package com.example.world;


import com.example.world.product.Product;
import com.example.world.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class MainController {

    final private ProductService productService;
    @GetMapping("/hi")
    public String hi(){
        return "navbar";
    }

    @GetMapping("/")
    public String Main(Model model){
        Page<Product> paging = this.productService.allThemeMain(0);
        model.addAttribute("paging",paging);
        return "MainPage";
    }



}

