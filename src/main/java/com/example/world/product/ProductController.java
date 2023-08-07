package com.example.world.product;

import com.example.world.order.ProductOrder;
import com.example.world.product.specification.macMin.MacMinForm;
import com.example.world.product.specification.macMin.MacMinService;
import com.example.world.product.specification.macRecommended.MacRecommendedForm;
import com.example.world.product.specification.macRecommended.MacRecommendedService;
import com.example.world.product.specification.windowMin.WindowMinForm;
import com.example.world.product.specification.windowMin.WindowMinService;
import com.example.world.product.specification.windowRecommended.WindowRecommendedForm;
import com.example.world.product.specification.windowRecommended.WindowRecommendedService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    private final UserService userService;

//    @GetMapping("/list")
//    public String allList(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
//        String themeName = "All";
//        Page<Product> paging = this.productService.allTheme(page);
//        model.addAttribute("paging", paging);
//        model.addAttribute("themeKey",themeName);
//        return "product_list";
//
//    }

    @GetMapping("/list")
    public String root(){
        return "redirect:/product/list/all";
    }

    @GetMapping(value = "/list/{theme}")
    public String productList(Model model, @PathVariable("theme") String key, @RequestParam(value = "page", defaultValue = "0") int page) {
        String themeKey;
        if (key.equals("RPG")) {
            themeKey = "RPG";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
            model.addAttribute("themeKey",themeKey);
        } else if (key.equals("FPS")) {
            themeKey = "FPS";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
            model.addAttribute("themeKey",themeKey);
        } else if (key.equals("Thriller")) {
            themeKey = "스릴러";
            String themeName = "Thriller";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
            model.addAttribute("themeKey",themeName);
        } else if (key.equals("Arcade")) {
            themeKey = "아케이드";
            String themeName="Arcade";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
            model.addAttribute("themeKey",themeName);
        } else if (key.equals("Sports")) {
            themeKey = "스포츠";
            String themeName= "Sports";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
            model.addAttribute("themeKey",themeName);
        } else if (key.equals("Simulation")) {
            themeKey = "시뮬레이션";
            String themeName = "Simulation";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
            model.addAttribute("themeKey",themeName);
        } else if (key.equals("ETC")) {
            themeKey = "기타";
            String themeName = "ETC";
            Page<Product> paging = this.productService.getTheme(page, themeKey);
            model.addAttribute("paging", paging);
            model.addAttribute("themeKey",themeName);
        } else if (key.equals("all")){
            String themeName = "all";
            Page<Product> paging = this.productService.allTheme(page);
            model.addAttribute("paging",paging);
            model.addAttribute("themeKey",themeName);
        }

        return "product_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model, ProductForm productForm, Principal principal) {

        SiteUser username = this.userService.getUserByUsername(principal.getName());
        //productForm.setUsername(username.getUsername());

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(Model model,
                         @Valid ProductForm productForm,
                         BindingResult bindingResult, Principal principal) {

        SiteUser username = this.userService.getUserByUsername(principal.getName());

        Product product = this.productService.create(username ,productForm.getProductName(), productForm.getDeveloper(),
                productForm.getTheme(), productForm.getPrice(), productForm.getContent());

        // windowMin 저장
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

        // windowRecommended 저장
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

        // MacMin 저장
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

        // MacRecommended 저장
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

        return String.format("redirect:/product/%s", product.getId());
    }

    @GetMapping("/{id}")
    public String detail(Model model,
                         @PathVariable("id") Long id) {

        Product product = this.productService.getProduct(id);
        model.addAttribute("product", product);
//
//        model.addAttribute("tKw", "");
//        model.addAttribute("theme", "");

        return "product_detail";
    }



    @GetMapping(value = "/list/{theme}/sort/high")
    public String sortHigh(Model model, @PathVariable("theme") String key, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Product> paging = this.productService.sortHigh(page, key);
        model.addAttribute("paging", paging);
        model.addAttribute("themeKey", key);

        return "product_list";
    }

    @GetMapping(value = "/list/{theme}/sort/low")
    public String sortLow(Model model, @PathVariable("theme") String key, @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Product> paging = this.productService.sortLow(page, key);
        model.addAttribute("paging", paging);
        model.addAttribute("themeKey", key);
        return "product_list";
    }

}
