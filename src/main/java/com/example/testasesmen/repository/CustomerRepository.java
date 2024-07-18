package com.example.testasesmen.repository;

import com.example.testasesmen.models.Customer;
import com.example.testasesmen.models.ERole;
import com.example.testasesmen.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByNamaCustomer(String namaCustomer);

    @Modifying
    @Query(value = "delete from m_customer where id = ?1 and id_user = ?2", nativeQuery = true)
    void deleteCustomerByIdAndUserId(Long idCustomer, Long idUser);

//    void deleteByIdAndIdUser(Long idCustomer, Long idUser)

    @Query("Select c from Customer c where c.id = ?1 and c.idUser = ?2")
    List<Customer> findCustomerByIdAndIdUser(Long id, Long idUser);

}
