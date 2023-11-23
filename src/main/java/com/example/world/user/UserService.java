package com.example.world.user;

import com.example.world.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.example.world.user.UserRole.ADMIN;
import static com.example.world.user.UserRole.USER;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final EmailVerificationTokenRepository tokenRepository;
//    private final EmailService emailService;

    public SiteUser create(String username, String password, String nickname, LocalDate birthDate, Integer mailKey, UserRole role, boolean mailAuth) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname);
        user.setBirthDate(birthDate);
        user.setRole(role);
        user.setMailKey(mailKey);
        user.setMailAuth(mailAuth);
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUserByUsername1 (String username) {
        Optional<SiteUser> siteUserOptional = this.userRepository.findByUsername(username);
        return siteUserOptional.orElse(null);
    }


    public SiteUser getUserByUsername(String username) {
        Optional<SiteUser> siteUserOptional = this.userRepository.findByUsername(username);
        if (siteUserOptional.isPresent()) {
            return siteUserOptional.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public String generateTempPassword() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public boolean confirmPassword(String password, SiteUser user) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public SiteUser modifyPassword(String password, SiteUser user) {
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    public List<SiteUser> getList () {
        return this.userRepository.findAll();
    }

    public SiteUser getUser(Long id) {
        Optional<SiteUser> siteUser = this.userRepository.findById(id);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteUser not found");
        }
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteUser not found");
        }
    }
    public SiteUser getUserId(Long id) {
        Optional<SiteUser> siteUser = this.userRepository.findById(id);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteUserId not found"); // 예외처리로 에러(DataNotFoundException)를 표시
        }
    }

    public boolean isNicknameDuplicate(String nickname) {
        Optional<SiteUser> existingUser = userRepository.findByNickname(nickname);
        return existingUser.isPresent();
    }

    public void deleteUser(SiteUser user) {
        this.userRepository.delete(user);
    }

    public Optional<SiteUser> getUserByusername(String username) {
        return userRepository.findByUsername(username);
    }

    public SiteUser verifyEmailConfirmation(String username, int mailKey) throws Exception {
        SiteUser user = this.getUserByUsername1(username);
        if (user == null) {
            throw new Exception("유효하지 않은 이메일입니다.");
        }
        if (user.isMailAuth()) {
            throw new Exception("이미 인증된 이메일입니다.");
        }
        if (user.getMailKey() != mailKey) {
            throw new Exception("인증코드가 일치하지 않습니다.");
        }
        user.setMailAuth(true);
        userRepository.save(user);
        return user;
    }

    public void updateMailAuth(String email, int mailKey) {
        int updatedRows = userRepository.updateMailAuth(email, mailKey);
        if (updatedRows > 0) {
            System.out.println("Mail auth updated successfully.");
        } else {
            System.out.println("Failed to update mail auth.");
        }
    }

    public void emailConfirm(String username, int mailKey) throws Exception {
        SiteUser user = this.getUserByUsername(username);

        if (user != null && user.getMailKey() == mailKey) {
            updateMailAuth(username, mailKey);

        } else {
            throw new Exception("유효하지 않은 이메일 또는 메일 키입니다.");
        }
    }

    public void adminPlus(Long id ) throws Exception {
        SiteUser user = this.userRepository.getReferenceById(id);
        user.setRole(ADMIN);
        this.userRepository.save(user);

        Optional<SiteUser> _siteUser = this.userRepository.findById(id);
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));

    }

    public void adminMinus(Long id ) throws Exception {
        SiteUser user = this.userRepository.getReferenceById(id);
        user.setRole(USER);
        this.userRepository.save(user);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));

    }

    public void modifyUser(SiteUser siteUser,String nickname, String password) {
        siteUser.setNickname(nickname);
        siteUser.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(siteUser);
    }


    //    public CurrentUser updateUser(String newUsername, String newPassword) {
//        CurrentUser currentUser = new CurrentUser();
//        currentUser.setUsername(newUsername);
//        currentUser.setPassword(newPassword);
//        return currentUser;
//    }


//    public void registerUser(SiteUser user) {
//        // 비밀번호 암호화
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // 사용자 저장
//        userRepository.save(user);
//
//        // 이메일 인증 토큰 생성
//        EmailVerificationToken token = new EmailVerificationToken();
//        token.setToken(generateToken());
//        token.setUser(user);
//        token.setExpiryDate(LocalDateTime.now().plusHours(24));
//        tokenRepository.save(token);
//
//        // 이메일 발송
//        String subject = "이메일 인증을 완료해주세요.";
//        String body = "인증을 완료하려면 다음 링크를 클릭하세요: http://example.com/verify?token=" + token.getToken();
//        emailService.sendEmail(user.getUsername(), subject, body);
//    }
//
//    private String generateToken() {
//        // 토큰 생성 로직 구현
//        // 예시: 랜덤한 문자열 생성 또는 UUID 사용
//        return "generated_token";
//    }


    //    public SiteUser updateProfile(SiteUser user, File file) throws IOException {
//        String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";
//
//        UUID uuid = UUID.randomUUID();
//        String fileName = uuid + "_" + file.getName();
//        String filePath = "/files/" + fileName;
//
//        File saveFile = new File(projectPath, fileName);
//        FileUtils.copyFile(file, saveFile); // 임시 파일을 실제 저장 경로로 복사
//
//        user.setProfileFilename(fileName);
//        user.setProfileFilepath(filePath);
//        this.userRepository.save(user);
//
//        return user;
//    }

}
