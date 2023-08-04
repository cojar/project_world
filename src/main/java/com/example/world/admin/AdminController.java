package com.example.world.admin;

import com.example.world.notice.Notice;
import com.example.world.notice.NoticeForm;
import com.example.world.notice.NoticeService;
import com.example.world.product.Product;
import com.example.world.user.SiteUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.ProductService;
import com.example.world.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final NoticeService noticeService;

    private final AdminService adminService;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;


    @GetMapping("/ad")
    public String adminMain(Model model,AdminSearchForm adminSearchForm) {
        int num=0;
        int priceM1 = this.adminService.requestMonthPrice(YearMonth.now());
        int priceM2 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(1));
        int priceM3 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(2));
        int priceM4 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(3));
        int priceM5 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(4));
        YearMonth month1 = YearMonth.now();
        YearMonth month2 = this.adminService.MonthMinus(1);
        YearMonth month3 = this.adminService.MonthMinus(2);
        YearMonth month4 = this.adminService.MonthMinus(3);
        YearMonth month5 = this.adminService.MonthMinus(4);

        model.addAttribute("month1",month1);
        model.addAttribute("month2",month2);
        model.addAttribute("month3",month3);
        model.addAttribute("month4",month4);
        model.addAttribute("month5",month5);
        model.addAttribute("priceM1",priceM1);
        model.addAttribute("priceM2",priceM2);
        model.addAttribute("priceM3",priceM3);
        model.addAttribute("priceM4",priceM4);
        model.addAttribute("priceM5",priceM5);
        model.addAttribute("listSize",num);
        model.addAttribute("allPrice",num);
        return "admin/admin_main";
    }


    @PostMapping("/ad")
    public String adminMainSearsh(Model model, @Valid AdminSearchForm adminSearchForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/ad/order";
        }
        LocalDateTime start = adminSearchForm.getStart();
        LocalDateTime end = adminSearchForm.getEnd();
        List<ProductOrder> list = this.orderService.getOrdersBetweenDates(start,end);

        int num=list.size();
        int totalPrice=1;
        for (ProductOrder order : list) {
            Product product = order.getProduct();
            if (product != null) {
                totalPrice += product.getPrice();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedAllPrice = decimalFormat.format(totalPrice);

        LocalDate todayLocalDate = LocalDate.now();

        int priceM1 = this.adminService.requestMonthPrice(YearMonth.now());
        int priceM2 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(1));
        int priceM3 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(2));
        int priceM4 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(3));
        int priceM5 = this.adminService.requestMonthPrice(this.adminService.MonthMinus(4));
        String month1 = YearMonth.now().toString();
        String month2 = this.adminService.MonthMinus(1).toString();
        String month3 = this.adminService.MonthMinus(2).toString();
        String month4 = this.adminService.MonthMinus(3).toString();
        String month5 = this.adminService.MonthMinus(4).toString();

        model.addAttribute("month1",month1);
        model.addAttribute("month2",month2);
        model.addAttribute("month3",month3);
        model.addAttribute("month4",month4);
        model.addAttribute("month5",month5);
        model.addAttribute("priceM1",priceM1);
        model.addAttribute("priceM2",priceM2);
        model.addAttribute("priceM3",priceM3);
        model.addAttribute("priceM4",priceM4);
        model.addAttribute("priceM5",priceM5);
        model.addAttribute("listSize",num);
        model.addAttribute("allPrice",formattedAllPrice);
        return "admin/admin_main";
    }



    @GetMapping("/ad/order")
    public String adminOrder(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ProductOrder> paging = this.adminService.getList(page, size);
        List<ProductOrder> orderProductList = this.orderService.getOrderList();
        model.addAttribute("paging", paging);
        model.addAttribute("orderProductList", orderProductList);

        return "admin/admin_order";
    }


    @PostMapping("/ad/code/{id}")
    public String adminSendCode(@PathVariable("id") Long id,
                                @RequestParam(value = "sendCode", required = false) String sendCode) {
        ProductOrder productOrder = this.orderService.getOrder(id);

        if (id != null && sendCode != null && !sendCode.isEmpty()) {
            orderService.updateOrderSendCode(productOrder);
            return "redirect:/ad/order";
        } else {
            return "redirect:/ad/order";
        }
    }

    @PostMapping("/ad/confirm/{id}")
    public String adminConfirmOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "결제완료");
        return "redirect:/ad/order";
    }


    @PostMapping("/ad/cancel/{id}")
    public String adminCancelOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "취소완료");
        return "redirect:/ad/order";
    }


    @GetMapping("/ad/product")
    public String adminProduct() {
        return "admin/admin_product";
    }


    @GetMapping("/ad/user")
    public String adminUser(Model model , @RequestParam(value = "page",defaultValue = "0")int page) {
        Page<SiteUser> userList = this.adminService.getUserList(page);
        model.addAttribute("paging",userList);
        return "admin/admin_user";
    }

    @PostMapping("/ad/user/adminPlus{id}")
    public ResponseEntity<String> adminPlus(@PathVariable Long id) {
        userService.adminPlus(id, "ROLE_ADMIN");
        return ResponseEntity.ok("관리자가 성공적으로 추가되었습니다.");
    }

    @GetMapping("/ad/review")
    public String adminReview() {
        return "admin/admin_review";
    }


    @GetMapping("/ad/qna")
    public String adminQna() {
        return "admin/admin_qna";
    }

    @GetMapping("ad/notice")
    public String adminNotice(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Notice> paging = this.noticeService.allNotice(page);
        model.addAttribute("paging",paging);
        return "admin/admin_notice";
    }
}
