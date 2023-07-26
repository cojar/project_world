package com.example.world.order;

import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import jakarta.persistence.Lob;
import jakarta.persistence.criteria.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/order")
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final ProductService productService;
    private final UserService userService;
    private final SiteUser siteUser;
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{id}")
    public String showOrderForm(OrderForm orderForm, @PathVariable("id") Long id, Model model, Principal principal) {
        Product product = this.productService.getProduct(id);
        SiteUser user = this.userService.getUser(principal.getName());
        orderForm.setProduct(product);
        orderForm.setUser(user);
        model.addAttribute("product", product);
        model.addAttribute("user", user);
        return "Order_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}") // order/create/product.id
    public String submitOrder(@Valid OrderForm orderForm,@PathVariable("id") Long id, Model model, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Order_form";
        }
        Product product = this.productService.getProduct(id);
        SiteUser user = this.userService.getUser(principal.getName());
        this.orderService.create(orderForm,product,user);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Long id, Principal principal, Model model) {
        SiteUser user = userService.getUser(principal.getName());
        ProductOrder productOrder = orderService.getOrder(id);
        model.addAttribute("orderProduct", productOrder);
        model.addAttribute("username", user);
        model.addAttribute("product", productOrder.getProduct());

        return "Order_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cancel/{id}")
    public String cancleOrder(@PathVariable Long id) {
        this.orderService.cancleOrder(id);
        return "redirect:/order/detail/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        ProductOrder order = this.orderService.getOrder(id);
        this.orderService.delete(order);
        return "redirect:/";
    }

}
