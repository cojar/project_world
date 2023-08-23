package com.example.world.order;

import com.example.world.product.Product;
import com.example.world.product.ProductService;
import com.example.world.user.SiteUser;
import com.example.world.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@RequestMapping("/order")
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final ProductService productService;
    private final UserService userService;
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
    @PostMapping("/create/{id}")
    public String submitOrder(@Valid OrderForm orderForm, @PathVariable("id") Long id, Model model, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "Order_form";
        }
        Product product = this.productService.getProduct(id);
        SiteUser user = this.userService.getUser(principal.getName());
        this.orderService.create(orderForm,product,user);

        return String.format("redirect:/order/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Long id, Principal principal, Model model) {
        SiteUser user = userService.getUser(principal.getName());
        ProductOrder productOrder = orderService.getOrder(id);

//        ReviewForm reviewForm = new ReviewForm();
//        reviewForm.setProductOrderId(id);
//        model.addAttribute("reviewForm", reviewForm);

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

    @Value("${custom.paymentSecretKey}")
    private String paymentSecretKey;
    String secretKey = "test_sk_ZORzdMaqN3wBKjyXWOB85AkYXQGw";

    @GetMapping("{id}/success")
    public String paymentResult(
            Model model,
            @PathVariable("id") String id,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "paymentKey") String paymentKey,
            @RequestParam(value = "amount") String amount,
            OrderForm orderForm,
            Principal principal) throws Exception {

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(secretKey.getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("productId", id);
        obj.put("amount", amount);
//        obj.put("customerName", customerName);
//        obj.put("email", email);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;
        model.addAttribute("isSuccess", isSuccess);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        model.addAttribute("responseStr", jsonObject.toJSONString());
        System.out.println(jsonObject.toJSONString());

        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));

        if (((String) jsonObject.get("method")) != null) {
            if (((String) jsonObject.get("method")).equals("카드")) {
                model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
            } else if (((String) jsonObject.get("method")).equals("가상계좌")) {
                model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
            } else if (((String) jsonObject.get("method")).equals("계좌이체")) {
                model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
            } else if (((String) jsonObject.get("method")).equals("휴대폰")) {
                model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
            }
        } else {
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        Product product = this.productService.getProduct(id);
        SiteUser user = userService.getUser(principal.getName());

        Long productOrderId = this.orderService.tossOrderCreate(product, user);

        model.addAttribute("productOrderId", productOrderId);

        return "order/success";
    }

    @GetMapping("/fail")
    public String paymentResult(
            Model model,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "code") Integer code
    ) throws Exception {

        model.addAttribute("code", code);
        model.addAttribute("message", message);

        return "order/fail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        ProductOrder order = this.orderService.getOrder(id);
        this.orderService.delete(order);
        return "redirect:/";
    }

}
