package com.example.world.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list/All")
    public String allList(Model model, @RequestParam(value = "page",defaultValue = "0")int page){
        Page<Product> paging = this.productService.AllTheme(page);
        model.addAttribute("paging",paging);
        return "product_list";
    }


    @GetMapping(value = "/list/{thema}")
    public String productList(Model model, @PathVariable("thema") String key, @RequestParam(value = "page",defaultValue = "0")int page){
        String themekey;
        if(key.equals("rpg")){
            themekey = "RPG";
            Page<Product> paging = this.productService.getTheme(page, themekey);
            model.addAttribute("paging",paging);
        }else if(key.equals("fps")){
            themekey="FPS";
            Page<Product> paging = this.productService.getTheme(page, themekey);
            model.addAttribute("paging",paging);
        }else if(key.equals("thriller")){
            themekey="Thriller";
            Page<Product> paging = this.productService.getTheme(page, themekey);
            model.addAttribute("paging",paging);
        }else if(key.equals("arcade")){
            themekey="Arcade";
            Page<Product> paging = this.productService.getTheme(page, themekey);
            model.addAttribute("paging", paging);
        }else if(key.equals("sports")){
            themekey="Sports";
            Page<Product> paging = this.productService.getTheme(page, themekey);
            model.addAttribute("paging",paging);
        }else if(key.equals("simulation")){
            themekey="Simulation";
            Page<Product> paging = this.productService.getTheme(page, themekey);
            model.addAttribute("paging",paging);
        }else if(key.equals("etc")){
            themekey="ETC";
            Page<Product> paging = this.productService.getTheme(page, themekey);
            model.addAttribute("paging",paging);
        }

        return "product_list";
    }


// all priduct/list/

}
