package com.example.world.email;

import com.example.world.DataNotFoundException;
import com.example.world.user.SiteUser;
import com.example.world.user.UserRepository;
import com.example.world.user.UserService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MailController {

    private final JavaMailSender mailSender;

    private final UserService userService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/mailCheck")
    @ResponseBody
    public int processMailCheck(@RequestParam("email") String email) throws Exception {
        int mailKey = (int) ((Math.random() * (99999 - 10000 + 1)) + 10000);

        String to = email;
        String title = "환영합니다! WORLD 입니다. 회원가입시 필요한 인증번호 입니다.";
        String content = "[인증번호] " + mailKey + " 입니다. <br/> 인증번호 확인란에 기입해주세요.";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

        } catch (Exception e) {
            throw new DataNotFoundException("error");
        }
        return mailKey;
    }


    @PostMapping("/user/findPw/sendEmail")
    @ResponseBody
    public void sendEmailForPw(@RequestParam("email") String userEmail, String userName) {

        String tempPw = userService.generateTempPassword();
        String from = "thdcodud010128@gmail.com";//보내는 이 메일주소
        String to = userEmail;
        String title = "임시 비밀번호입니다.";
        String content = userName + "님의" + "[임시 비밀번호] " + tempPw + " 입니다. <br/> 접속한 후 비밀번호를 변경해주세요";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

            SiteUser user = userService.getUserByUsername(userName);
            user.setPassword(passwordEncoder.encode(tempPw));
            userRepository.save(user);

        } catch (Exception e) {
            throw new DataNotFoundException("error");
        }
    }


    @PostMapping("/user/findId/sendEmail")
    @ResponseBody
    public void sendEmailForId(@RequestParam("userEmail") String userEmail, String userName) {
        String from = "hdcodud010128@gmail.com";//보내는 이 메일주소
        String to = userEmail;
        String title = "아이디 찾기 결과입니다.";
        String content = "[아이디] " + userName + " 입니다. <br/>";
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(title);
            mailHelper.setText(content, true);

            mailSender.send(mail);

        } catch (Exception e) {
            throw new DataNotFoundException("error");
        }
    }
}
