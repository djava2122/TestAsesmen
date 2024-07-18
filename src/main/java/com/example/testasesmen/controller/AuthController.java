package com.example.testasesmen.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.example.testasesmen.models.ERole;
import com.example.testasesmen.models.Role;
import com.example.testasesmen.models.User;
import com.example.testasesmen.payload.request.LoginRequest;
import com.example.testasesmen.payload.request.SignupRequest;
import com.example.testasesmen.payload.response.JwtResponse;
import com.example.testasesmen.payload.response.MessageResponse;
import com.example.testasesmen.repository.RoleRepository;
import com.example.testasesmen.repository.UserRepository;
import com.example.testasesmen.security.jwt.JwtUtils;
import com.example.testasesmen.security.services.EmailService;
import com.example.testasesmen.security.services.UserDetailsImpl;
import com.example.testasesmen.util.Mail;
import com.example.testasesmen.util.SendEmail;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private EmailService sendEmailTask;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        }catch (Exception e){
            e.printStackTrace();
        }

        if(authentication == null){
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username Tidak Valid"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody SignupRequest signUpRequest) {
        if (!userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username tidak valid!"));
        }
        User user = userRepository.findByUsername(signUpRequest.getUsername()).get();
        System.out.println("User "+user.getId());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Perubahan Password berhasil successfully!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        Context context = new Context();
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username Sudah Terdaftar!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email Sudah di gunakan"));
        }


        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role tidak boleh kosong."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {

                    case "admin":

                        Role adminRole = null;
                        Optional<Role> adminRoleList = roleRepository.findByName(ERole.ROLE_ADMIN);
                        if(adminRoleList.isEmpty()){
                            adminRole = new Role();
                            adminRole.setName(ERole.ROLE_ADMIN);
                            roleRepository.save(adminRole);
                            roles.add(adminRole);
                        }else{
                            adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);
                        }

                        break;

                    case "user":

                        Role userRole = null;
                        Optional<Role> userRoleList = roleRepository.findByName(ERole.ROLE_USER);
                        if(userRoleList.isEmpty()){
                            userRole = new Role();
                            userRole.setName(ERole.ROLE_USER);
                            roleRepository.save(userRole);
                            roles.add(userRole);
                        }else{
                            userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                        }

                        break;

                    default:

                        new RuntimeException("User Role Tidak tersedia");

                }
            });
        }

        System.out.println("roles "+roles);
        if(roles.size() == 0){
            return ResponseEntity.ok(new MessageResponse("User Role Tidak tersedia!"));
        }else {
            user.setRoles(roles);
            User userSave = userRepository.save(user);

            Mail mail = new Mail();
            mail.setRecipient(signUpRequest.getEmail());
            mail.setSubject("Info Registration");
            mail.setBody("/user/registration.html");

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("username", signUpRequest.getUsername());
            hashMap.put("password", signUpRequest.getPassword());
            mail.setModel(hashMap);
            sendEmailTask.sendEmailWithHtmlTemplate(mail.getRecipient(), mail.getSubject(), mail.getBody(), context);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }
    }
}
