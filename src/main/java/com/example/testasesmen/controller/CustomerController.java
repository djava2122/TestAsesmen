package com.example.testasesmen.controller;


import com.example.testasesmen.models.Customer;
import com.example.testasesmen.models.ERole;
import com.example.testasesmen.models.Role;
import com.example.testasesmen.models.User;
import com.example.testasesmen.payload.request.CustomerRequest;
import com.example.testasesmen.payload.request.SignupRequest;
import com.example.testasesmen.payload.response.MessageResponse;
import com.example.testasesmen.repository.CustomerRepository;
import com.example.testasesmen.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/getAll")
    public ResponseEntity<?> getAll(@Valid @RequestBody CustomerRequest customerRequest) {

        Optional<User> user = userRepository.findById(customerRequest.getIdUser());
        if(user.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("user tidak terdaftar"));
        }

        List<Customer> customerList = customerRepository.findAll();
        if(customerList.size() == 0){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Data tidak ditemukan"));
        }else {
            return ResponseEntity.ok(customerList);
        }
    }

    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<?> deleteCustomer(@Valid @RequestBody CustomerRequest customerRequest) {

        Optional<User> user = userRepository.findById(customerRequest.getIdUser());
        if(user.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("user tidak terdaftar"));
        }

        User user1 = user.get();
        List<Role> role = user1.getRoles().stream().findAny().stream().toList();

        Boolean isAdmin = false;
        for (Role ro : role){
            System.out.println("hohohoho "+ro.getName());
            if(ro.getName().equals(ERole.ROLE_ADMIN)){
                isAdmin = true;
            }
        }

        if(customerRequest.getIdCustomer() != null) {


            if(!isAdmin){

                List<Customer> customerList = customerRepository.findCustomerByIdAndIdUser(customerRequest.getIdCustomer(), customerRequest.getIdUser());
                if(customerList.size() == 0){
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Customer tidak ditemukan"));
                }else{

                    customerRepository.deleteCustomerByIdAndUserId(customerRequest.getIdCustomer(), customerRequest.getIdUser());
                    return ResponseEntity.ok(new MessageResponse("Customer Deleted successfully!"));
                }

            }else {
                Optional<Customer> cus = customerRepository.findById(customerRequest.getIdCustomer());
                if (cus.isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Customer tidak ditemukan"));
                }

                else{
                    customerRepository.deleteById(customerRequest.getIdCustomer());
                    return ResponseEntity.ok(new MessageResponse("Customer Deleted successfully!"));
                }
            }

        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Message Invalid"));
        }



    }

    @PostMapping("/customer")
    public ResponseEntity<?> saveCustomer(@Valid @RequestBody CustomerRequest customerRequest) {

        Optional<User> user = userRepository.findById(customerRequest.getIdUser());
        if(user.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("user tidak terdaftar"));
        }

        User user1 = user.get();
        List<Role> role = user1.getRoles().stream().findAny().stream().toList();

        Boolean isAdmin = false;
        for (Role ro : role){
            System.out.println("hohohoho "+ro.getName());
            if(ro.getName().equals(ERole.ROLE_ADMIN)){
                isAdmin = true;
            }
        }

        if(customerRequest.getIdCustomer() != null) {
            if(!isAdmin){

                List<Customer> customerList = customerRepository.findCustomerByIdAndIdUser(customerRequest.getIdCustomer(), customerRequest.getIdUser());
                if(customerList.size() == 0){
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Customer tidak ditemukan"));
                }

            }else {
                Optional<Customer> cus = customerRepository.findById(customerRequest.getIdCustomer());
                if (cus.isEmpty()) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Customer tidak ditemukan"));
                }
            }

            Optional<Customer> cus = customerRepository.findById(customerRequest.getIdCustomer());
            Customer customer = new Customer(customerRequest.getIdCustomer().intValue(), customerRequest.getNamaCustomer(), customerRequest.getAlamat(),
                        customerRequest.getKota(), customerRequest.getNoTelepon(), cus.get().getIdUser());
                customerRepository.save(customer);
                return ResponseEntity.ok(new MessageResponse("Customer Updated successfully!"));


        }else {

                Customer customer = new Customer(customerRequest.getNamaCustomer(), customerRequest.getAlamat(),
                        customerRequest.getKota(), customerRequest.getNoTelepon(), customerRequest.getIdUser().intValue());
                customerRepository.save(customer);
                return ResponseEntity.ok(new MessageResponse("Customer registered successfully!"));

        }
    }


}
