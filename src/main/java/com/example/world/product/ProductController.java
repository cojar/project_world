package com.example.world.product;

import com.example.world.product.specification.macMin.MacMinForm;
import com.example.world.product.specification.macMin.MacMinService;
import com.example.world.product.specification.macRecommended.MacRecommendedForm;
import com.example.world.product.specification.macRecommended.MacRecommendedService;
import com.example.world.product.specification.windowMin.WindowMinForm;
import com.example.world.product.specification.windowMin.WindowMinService;
import com.example.world.product.specification.windowRecommended.WindowRecommendedForm;
import com.example.world.product.specification.windowRecommended.WindowRecommendedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String allList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Product> paging = this.productService.AllTheme(page);
        model.addAttribute("paging", paging);
        return "product_list";
    }


    @GetMapping(value = "/list/{theme}")
    public String productList(Model model, @PathVariable("theme") String key, @RequestParam(value = "page", defaultValue = "0") int page) {
        String themeKey;
        if (key.equals("rpg")) {
            themeKey = "RPG";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        } else if (key.equals("fps")) {
            themeKey = "FPS";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        } else if (key.equals("thriller")) {
            themeKey = "Thriller";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        } else if (key.equals("arcade")) {
            themeKey = "Arcade";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        } else if (key.equals("sports")) {
            themeKey = "Sports";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        } else if (key.equals("simulation")) {
            themeKey = "Simulation";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        } else if (key.equals("etc")) {
            themeKey = "ETC";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
        }

        return "product_list";
    }


// all priduct/list/


    @GetMapping("/create")
    public String create(Model model, ProductForm productForm) {

        List<WindowMinForm> windowMinList = new ArrayList<>();
        List<WindowRecommendedForm> windowRecommendedList = new ArrayList<>();
        List<MacMinForm> macMinList = new ArrayList<>();
        List<MacRecommendedForm> macRecommendedList = new ArrayList<>();

        windowMinList.add(new WindowMinForm());
        windowRecommendedList.add(new WindowRecommendedForm());
        macMinList.add(new MacMinForm());
        macRecommendedList.add(new MacRecommendedForm());
        model.addAttribute("windowMinList", windowMinList);
        model.addAttribute("windowRecommendedList", windowRecommendedList);
        model.addAttribute("macMinList", macMinList);
        model.addAttribute("macRecommendedList", macRecommendedList);
        return "product_form";
    }

    @PostMapping("/create")
    public String create(Model model,
                         @Valid ProductForm productForm,
                         BindingResult bindingResult) {


        // 상품 기본정보 저장
        Product product = this.productService.create(productForm.getProductName(), productForm.getDeveloper(),
                productForm.getTheme(), productForm.getPrice(), productForm.getContent());

        // windowMin
        for (WindowMinForm windowMinForm : productForm.getWindowMinList()) {
            if (!windowMinForm.getOperatingSystem().isEmpty()
                    || !windowMinForm.getProcessor().isEmpty()
                    || !windowMinForm.getMemory().isEmpty()
                    || !windowMinForm.getGraphics().isEmpty()
                    || !windowMinForm.getStorage().isEmpty()) {
                this.windowMinService.create(windowMinForm.getOperatingSystem(), windowMinForm.getProcessor(), windowMinForm.getMemory(),
                        windowMinForm.getGraphics(), windowMinForm.getStorage(), windowMinForm.getDirectAccess(), windowMinForm.getNetwork(), product);
            }
        }

        // windowRecommended
        for (WindowRecommendedForm windowRecommendedForm : productForm.getWindowRecommendedList()) {
            if (!windowRecommendedForm.getOperatingSystem().isEmpty()
                    || !windowRecommendedForm.getProcessor().isEmpty()
                    || !windowRecommendedForm.getMemory().isEmpty()
                    || !windowRecommendedForm.getGraphics().isEmpty()
                    || !windowRecommendedForm.getStorage().isEmpty()) {
                this.windowRecommendedService.create(windowRecommendedForm.getOperatingSystem(), windowRecommendedForm.getProcessor(), windowRecommendedForm.getMemory(),
                        windowRecommendedForm.getGraphics(), windowRecommendedForm.getStorage(), windowRecommendedForm.getDirectAccess(), windowRecommendedForm.getNetwork(), product);
            }
        }

        // MacMin
        for (MacMinForm macMinForm : productForm.getMacMinList()) {
            if (!macMinForm.getOperatingSystem().isEmpty()
                    || !macMinForm.getProcessor().isEmpty()
                    || !macMinForm.getMemory().isEmpty()
                    || !macMinForm.getGraphics().isEmpty()
                    || !macMinForm.getStorage().isEmpty()) {
                this.macMinService.create(macMinForm.getOperatingSystem(), macMinForm.getProcessor(), macMinForm.getMemory(),
                        macMinForm.getGraphics(), macMinForm.getStorage(), macMinForm.getDirectAccess(), macMinForm.getNetwork(), product);
            }
        }

        // MacRecommended
        for (MacRecommendedForm macRecommendedForm : productForm.getMacRecommendedList()) {
            if (!macRecommendedForm.getOperatingSystem().isEmpty()
                    || !macRecommendedForm.getProcessor().isEmpty()
                    || !macRecommendedForm.getMemory().isEmpty()
                    || !macRecommendedForm.getGraphics().isEmpty()
                    || !macRecommendedForm.getStorage().isEmpty()) {
                this.macRecommendedService.create(macRecommendedForm.getOperatingSystem(), macRecommendedForm.getProcessor(), macRecommendedForm.getMemory(),
                        macRecommendedForm.getGraphics(), macRecommendedForm.getStorage(), macRecommendedForm.getDirectAccess(), macRecommendedForm.getNetwork(), product);
            }
        }


        return "redirect:/product/list/All";
    }


}
