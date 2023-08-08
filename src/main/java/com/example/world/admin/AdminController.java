package com.example.world.admin;

import com.example.world.notice.Notice;
import com.example.world.notice.NoticeService;
import com.example.world.order.OrderRepository;
import com.example.world.product.Product;
import com.example.world.product.ProductForm;
import com.example.world.product.specification.macMin.MacMin;
import com.example.world.product.specification.macMin.MacMinForm;
import com.example.world.product.specification.macMin.MacMinService;
import com.example.world.product.specification.macRecommended.MacRecommended;
import com.example.world.product.specification.macRecommended.MacRecommendedForm;
import com.example.world.product.specification.windowMin.WindowMin;
import com.example.world.product.specification.windowMin.WindowMinForm;
import com.example.world.product.specification.windowRecommended.WindowRecommended;
import com.example.world.product.specification.windowRecommended.WindowRecommendedForm;
import com.example.world.review.Review;
import com.example.world.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.ProductService;
import com.example.world.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final NoticeService noticeService;
    private final AdminService adminService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ProductService productService;


    @GetMapping("/admin")
    public String adminMain() {
        return "admin/admin_main";
    }


    @GetMapping("/admin/order")
    public String adminOrder(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ProductOrder> paging = this.adminService.getList(page, size);
        List<ProductOrder> orderProductList = this.orderService.getOrderList();
        model.addAttribute("paging", paging);
        model.addAttribute("orderProductList", orderProductList);

        return "admin/admin_order";
    }

    @PostMapping("/admin/confirm/{id}")
    public ResponseEntity<String> adminConfirmOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "결제완료");
        return ResponseEntity.ok("주문 상태가 성공적으로 변경되었습니다.");
    }

    @GetMapping("/admin/code/{id}")
    public String adminSendCode(@PathVariable("id") Long id, @RequestParam(value = "sendCode", defaultValue = "") String sendCode) {
        ProductOrder productOrder = orderService.getOrder(id);
        if (productOrder != null) {
            productOrder.setCode(sendCode);
            productOrder.setOrderStatus("주문완료");
            orderRepository.save(productOrder);
            return "redirect:/ad/order";
        } else {
            return "redirect:/ad/order";
        }
    }

    @PostMapping("/admin/completeCancel/{id}")
    public ResponseEntity<String> adminCompleteCancelOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "취소완료");
        return ResponseEntity.ok("주문 상태가 성공적으로 변경되었습니다.");
    }

    @GetMapping("/admin/product")
    public String adminProduct(Long id, Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Product> paging = this.productService.getList(page, size);
        List<Product> productList = this.productService.getProductList();
        long completedOrderCount = orderService.getCompletedOrderCount();
        model.addAttribute("paging", paging);
        model.addAttribute("productList", productList);
        model.addAttribute("completedOrderCount", completedOrderCount);

        return "admin/admin_product";
    }

//    @GetMapping("/ad/product/delete/{id}")
//    public String adminProductDelete(@PathVariable("id") Long id) {
//        Product product = this.productService.getProduct(id);
//        ProductOrder order = this.orderService.getOrder(id);
//        this.orderService.delete(order);
//        this.productService.delete(product);
//        System.out.println("가져온 상품 :" + productService.getProduct(id));
//        return "redirect:/ad/product";
//    }

    @GetMapping("/admin/product/modify/{id}")
    public String adminProductModify(ProductForm productForm, @PathVariable("id") Long id, Model model) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return "admin/admin_product";
        }

        productForm.setProductName(product.getProductName());
        productForm.setDeveloper(product.getDeveloper());
        productForm.setTheme(product.getTheme());
        productForm.setPrice(product.getPrice());
        productForm.setContent(product.getContent());

        List<WindowMinForm> windowMinList = new ArrayList<>();
        for (WindowMin windowMin : product.getWindowMinList()) {
            WindowMinForm windowMinForm = new WindowMinForm();
            windowMinForm.setOperatingSystem(windowMin.getOperatingSystem());
            windowMinForm.setProcessor(windowMin.getProcessor());
            windowMinForm.setMemory(windowMin.getMemory());
            windowMinForm.setGraphics(windowMin.getGraphics());
            windowMinForm.setStorage(windowMin.getStorage());
            windowMinForm.setDirectAccess(windowMin.getDirectAccess());
            windowMinForm.setNetwork(windowMin.getNetwork());
            windowMinList.add(windowMinForm);
        }
        productForm.setWindowMinList(windowMinList);

        List<WindowRecommendedForm> windowRecommendedList = new ArrayList<>();
        for (WindowRecommended windowRecommended : product.getWindowRecommendedList()) {
            WindowRecommendedForm windowRecommendedForm = new WindowRecommendedForm();
            windowRecommendedForm.setOperatingSystem(windowRecommended.getOperatingSystem());
            windowRecommendedForm.setProcessor(windowRecommended.getProcessor());
            windowRecommendedForm.setMemory(windowRecommended.getMemory());
            windowRecommendedForm.setGraphics(windowRecommended.getGraphics());
            windowRecommendedForm.setStorage(windowRecommended.getStorage());
            windowRecommendedForm.setDirectAccess(windowRecommended.getDirectAccess());
            windowRecommendedForm.setNetwork(windowRecommended.getNetwork());
            windowRecommendedList.add(windowRecommendedForm);
        }
        productForm.setWindowRecommendedList(windowRecommendedList);

        List<MacMinForm> macMinList = new ArrayList<>();
        for (MacMin macMin : product.getMacMinList()) {
            MacMinForm macMinForm = new MacMinForm();
            macMinForm.setOperatingSystem(macMin.getOperatingSystem());
            macMinForm.setProcessor(macMin.getProcessor());
            macMinForm.setMemory(macMin.getMemory());
            macMinForm.setGraphics(macMin.getGraphics());
            macMinForm.setStorage(macMin.getStorage());
            macMinForm.setDirectAccess(macMin.getDirectAccess());
            macMinForm.setNetwork(macMin.getNetwork());
            macMinList.add(macMinForm);
        }
        productForm.setMacMinList(macMinList);

        List<MacRecommendedForm> macRecommendedList = new ArrayList<>();
        for (MacRecommended macRecommended : product.getMacRecommendedList()) {
            MacRecommendedForm macRecommendedForm = new MacRecommendedForm();
            macRecommendedForm.setOperatingSystem(macRecommended.getOperatingSystem());
            macRecommendedForm.setProcessor(macRecommended.getProcessor());
            macRecommendedForm.setMemory(macRecommended.getMemory());
            macRecommendedForm.setGraphics(macRecommended.getGraphics());
            macRecommendedForm.setStorage(macRecommended.getStorage());
            macRecommendedForm.setDirectAccess(macRecommended.getDirectAccess());
            macRecommendedForm.setNetwork(macRecommended.getNetwork());
            macRecommendedList.add(macRecommendedForm);
        }
        productForm.setMacRecommendedList(macRecommendedList);

        model.addAttribute("productForm", productForm);
        model.addAttribute("windowMinList", windowMinList);
        model.addAttribute("windowRecommendedList", windowRecommendedList);
        model.addAttribute("macMinList", macMinList);
        model.addAttribute("macRecommendedList", macRecommendedList);

        return "product_form";
    }

    @PostMapping("/admin/product/modify/{id}")
    public String adminProductModify(@ModelAttribute("productForm") @Valid ProductForm productForm,
                                     BindingResult bindingResult,
                                     @PathVariable("id") Long id,
                                     RedirectAttributes redirectAttributes) throws Exception {
        Product product = productService.getProduct(id);
        System.out.println("수정된 제품 정보 확인:");
        System.out.println("수정된 이름: " + productForm.getProductName());
        System.out.println("수정된 개발사: " + productForm.getDeveloper());
        System.out.println("수정된 테마: " + productForm.getTheme());
        System.out.println("수정된 가격: " + productForm.getPrice());
        System.out.println("수정된 설명: " + productForm.getContent());
        System.out.println("수정된 윈도우최소: " + productForm.getWindowMinList());
        System.out.println("수정된 윈도우권장: " + productForm.getWindowRecommendedList());
        System.out.println("수정된 맥최소: " + productForm.getMacMinList());
        System.out.println("수정된 맥권장:" + productForm.getMacRecommendedList());

        if (bindingResult.hasErrors()) {
            System.out.println("유효성 검사 오류 발생");
            return "product_form";
        }

        this.productService.modifyProduct(id, productForm);
        System.out.println("제품 수정 완료");
        redirectAttributes.addFlashAttribute("successMessage", "제품이 수정되었습니다.");

        return String.format("redirect:/product/%s", product.getId());
    }

    @GetMapping("/admin/user")
    public String adminUser() {
        return "admin/admin_user";
    }

    @GetMapping("/admin/review")
    public String adminReview() {
        return "admin/admin_review";
    }


    @GetMapping("/admin/qna")
    public String adminQna() {
        return "admin/admin_qna";
    }

    @GetMapping("admin/notice")
    public String adminNotice(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Notice> paging = this.noticeService.allNotice(page);
        model.addAttribute("paging",paging);
        return "admin/admin_notice";
    }

}
