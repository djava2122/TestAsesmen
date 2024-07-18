package com.example.testasesmen.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "m_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 100)
    private String namaCustomer;

    @NotBlank
    @Size(max = 100)
    private String alamat;

    @NotBlank
    @Size(max = 100)
    private String kota;

    @NotBlank
    @Size(max = 100)
    private String noTelepon;

    private Integer idUser;

    public Customer() {
    }

    public Customer(String namaCustomer, String alamat, String kota, String noTelepon, Integer idUser) {
        this.namaCustomer = namaCustomer;
        this.alamat = alamat;
        this.kota = kota;
        this.noTelepon = noTelepon;
        this.idUser = idUser;
    }

    public Customer(Integer id, String namaCustomer, String alamat, String kota, String noTelepon, Integer idUser) {
        this.id = id;
        this.namaCustomer = namaCustomer;
        this.alamat = alamat;
        this.kota = kota;
        this.noTelepon = noTelepon;
        this.idUser = idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }
}
