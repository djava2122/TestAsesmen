package com.example.testasesmen.controller;


import com.example.testasesmen.models.Customer;
import com.example.testasesmen.models.Role;
import com.example.testasesmen.payload.request.CustomerRequest;
import com.example.testasesmen.payload.request.RoleRequest;
import com.example.testasesmen.payload.response.MessageResponse;
import com.example.testasesmen.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/role")
public class RoleController {


    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/role")
    public ResponseEntity<?> saveRole(@Valid @RequestBody RoleRequest roleRequest) {

        Role role = new Role();
        role.setName(roleRequest.getNamaRole());
        roleRepository.save(role);

        return ResponseEntity.ok(new MessageResponse("Role registered successfully!"));
    }

    @PostMapping("/getAll")
    public ResponseEntity<?> getAll() {

        List<Role> roleList = roleRepository.findAll();

        return ResponseEntity.ok(roleList);
    }



}
