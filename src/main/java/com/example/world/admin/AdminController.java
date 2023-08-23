package com.example.world.admin;

import com.example.world.notice.Notice;
import com.example.world.notice.NoticeForm;
import com.example.world.notice.NoticeService;
import com.example.world.order.OrderRepository;
import com.example.world.order.OrderService;
import com.example.world.order.ProductOrder;
import com.example.world.product.Product;
import com.example.world.product.ProductForm;
import com.example.world.product.ProductService;
import com.example.world.product.productImage.ProductImageForm;
import com.example.world.product.productImage.ProductImageService;
import com.example.world.product.specification.macMin.MacMin;
import com.example.world.product.specification.macMin.MacMinForm;
import com.example.world.product.specification.macMin.MacMinService;
import com.example.world.product.specification.macRecommended.MacRecommended;
import com.example.world.product.specification.macRecommended.MacRecommendedForm;
import com.example.world.product.specification.macRecommended.MacRecommendedService;
import com.example.world.product.specification.windowMin.WindowMin;
import com.example.world.product.specification.windowMin.WindowMinForm;
import com.example.world.product.specification.windowMin.WindowMinService;
import com.example.world.product.specification.windowRecommended.WindowRecommended;
import com.example.world.product.specification.windowRecommended.WindowRecommendedForm;
import com.example.world.product.specification.windowRecommended.WindowRecommendedService;
import com.example.world.qna.Question;
import com.example.world.qna.QuestionService;
import com.example.world.qnaAnswer.AnswerForm;
import com.example.world.qnaAnswer.AnswerService;
import com.example.world.review.Review;
import com.example.world.review.ReviewService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final NoticeService noticeService;
    private final AdminService adminService;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;
    private final JavaMailSender mailSender;

    private final MacMinService macMinService;
    private final MacRecommendedService macRecommendedService;
    private final WindowMinService windowMinService;
    private final WindowRecommendedService windowRecommendedService;
    private final ProductImageService productImageService;

    @GetMapping("/")
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


    @PostMapping("/")
    public String adminMainSearsh(Model model, @Valid AdminSearchForm adminSearchForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/";
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


    @GetMapping("/order")
    public String adminOrder(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<ProductOrder> paging = this.adminService.getList(page, size);
        List<ProductOrder> orderProductList = this.orderService.getOrderList();
        model.addAttribute("paging", paging);
        model.addAttribute("orderProductList", orderProductList);

        return "admin/admin_order";
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> adminConfirmOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "결제완료");
        return ResponseEntity.ok("주문 상태가 성공적으로 변경되었습니다.");
    }

    @GetMapping("/code/{id}")
    public String adminSendCode(@PathVariable("id") Long id, @RequestParam(value = "sendCode", defaultValue = "") String sendCode) {
        ProductOrder productOrder = orderService.getOrder(id);
        if (productOrder != null) {
            productOrder.setCode(sendCode);
            productOrder.setOrderStatus("발송완료");
            orderRepository.save(productOrder);

            String userEmail = productOrder.getEmail();

            String emailSubject = "발송 코드 정보";
            String emailContent = "반갑습니다 WORLD 에서 주문하신 상품의 코드입니다 : " + sendCode;

            try {
                MimeMessage mail = mailSender.createMimeMessage();
                MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

                mailHelper.setTo(userEmail);
                mailHelper.setSubject(emailSubject);
                mailHelper.setText(emailContent, true);

                mailSender.send(mail);

                return "redirect:/admin/order";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/admin/order";
            }
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/completeCancel/{id}")
    public ResponseEntity<String> adminCompleteCancelOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, "취소완료");
        return ResponseEntity.ok("주문 상태가 성공적으로 변경되었습니다.");
    }

    @GetMapping("/product")
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

    @GetMapping("/product/modify/{id}")
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

    @GetMapping("/user")
    public String adminUser(Model model , @RequestParam(value = "page",defaultValue = "0")int page) {
        Page<SiteUser> userList = this.adminService.getUserList(page);
        model.addAttribute("paging", userList);
        return "admin/admin_user";
    }

    @PostMapping("/product/modify/{id}")
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

    @PostMapping("/user/adminPlus/{id}")
    public ResponseEntity<String> adminPlus(@PathVariable("id") Long id) throws Exception {
        userService.adminPlus(id);
        return ResponseEntity.ok("관리자가 성공적으로 추가되었습니다.");
    }
    @PostMapping("/user/adminMinus/{id}")
    public ResponseEntity<String> adminMinus(@PathVariable("id") Long id) throws Exception {
        userService.adminMinus(id);
        return ResponseEntity.ok("관리자 권한이 성공적으로 회수 되었습니다.");
    }

    @GetMapping("/review")
    public String adminReview(Long id, Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Review> paging = this.reviewService.getList(page, size);
        List<Review> reviewList = this.reviewService.getReviewList();
        model.addAttribute("paging", paging);
        model.addAttribute("reviewList", reviewList);


        return "admin/admin_review";
    }

    @PostMapping("/review/delete")
    public ResponseEntity<String> deleteReview(@RequestParam(value="ids[]") List<Long> ids) {
        for (Long id : ids) {
            reviewService.deleteReview(id);
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/qna")
    public String adminQna(Long id, Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Question> paging = this.questionService.getList(page, size);
        List<Question> questionList = this.questionService.getQuestionList();
        model.addAttribute("paging", paging);
        model.addAttribute("questionList", questionList);
        return "admin/admin_qna";
    }

    @GetMapping("/qna/answer/{id}")
    public String adminAnswer(@PathVariable("id") Long id, Model model, AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);
        answerForm.setContent(answerForm.getContent());
        model.addAttribute("question", question);
        return "admin/answer_form";
    }

    @PostMapping("/qna/answer/{id}")
    public String adminAnswer(@PathVariable("id") Long id, @RequestParam String content, Principal principal) {
        Question question = this.questionService.getQuestion(id);
        SiteUser adminUser = this.userService.getUser(principal.getName());
        this.answerService.create(question, content, adminUser);

        return "redirect:/admin/qna";
    }

    @PostMapping("/qna/delete")
    public ResponseEntity<String> deleteQuestions(@RequestParam(value="ids[]") List<Long> ids) {
        for (Long id : ids) {
            questionService.deleteQuestionWithAnswers(id);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notice")
    public String adminNotice(Model model, @RequestParam(value = "page", defaultValue = "0") int page){
        Page<Notice> paging = this.noticeService.allNotice(page);
        model.addAttribute("paging",paging);
        return "admin/admin_notice";
    }

    //////공지사항작성///////
    @GetMapping("/notice/create")
    public String noticeCreate(){
        return "notice_form";
    }


    @PostMapping("/notice/create")
    public String articleCreate(@Valid NoticeForm noticeForm, BindingResult bindingResult, MultipartFile thumbnail) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        this.noticeService.create(noticeForm.getSubject(),noticeForm.getContent(),thumbnail);
        return String.format("redirect:/admin/notice");
    }

    ////////상품작성////////
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/product/create")
    public String create(Model model, ProductForm productForm) {
//    public String create(Model model, ProductForm productForm, Principal principal) {
//        SiteUser username = this.userService.getUserByUsername(principal.getName());
//        productForm.setUsername(username.getUsername());

        List<WindowMinForm> windowMinList = new ArrayList<>();
        List<WindowRecommendedForm> windowRecommendedList = new ArrayList<>();
        List<MacMinForm> macMinList = new ArrayList<>();
        List<MacRecommendedForm> macRecommendedList = new ArrayList<>();
        List<ProductImageForm> productImageList = new ArrayList<>();

        windowMinList.add(new WindowMinForm());
        windowRecommendedList.add(new WindowRecommendedForm());
        macMinList.add(new MacMinForm());
        macRecommendedList.add(new MacRecommendedForm());
        productImageList.add(new ProductImageForm());


        model.addAttribute("windowMinList", windowMinList);
        model.addAttribute("windowRecommendedList", windowRecommendedList);
        model.addAttribute("macMinList", macMinList);
        model.addAttribute("macRecommendedList", macRecommendedList);
        model.addAttribute("productImageList", productImageList);

        return "product_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/product/create")
    public String create(Model model,
                         @Valid ProductForm productForm,
                         BindingResult bindingResult, Principal principal) throws IOException {

//        SiteUser username = this.userService.getUserByUsername(principal.getName());

        Product product = this.productService.create(productForm.getProductName(), productForm.getDeveloper(),
                productForm.getTheme(), productForm.getPrice(), productForm.getContent());

//        Product product = this.productService.create(productForm.getProductName(), productForm.getDeveloper(),
//                productForm.getTheme(), productForm.getPanelImage(), productForm.getPrice(), productForm.getContent());

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

        for (ProductImageForm productImageForm : productForm.getProductImageList()) {
            if (!productImageForm.getName().equals("") && productImageForm.getImage() != null) {
                this.productImageService.create(productImageForm.getName(), productImageForm.getImage(), product);
            }
        }

        return String.format("redirect:/product/%s", product.getId());
    }
}
