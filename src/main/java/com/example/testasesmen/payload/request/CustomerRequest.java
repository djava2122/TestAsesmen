package com.example.testasesmen.payload.request;

public class CustomerRequest {

    private Long idUser;

    private Long idCustomer;
    private String namaCustomer;
    private String alamat;
    private String kota;
    private String noTelepon;

    public void setIdCustomer(Long idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Long getIdCustomer() {
        return idCustomer;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdUser() {
        return idUser;
    }

    public CustomerRequest(String namaCustomer, String alamat, String kota, String noTelepon) {
        this.namaCustomer = namaCustomer;
        this.alamat = alamat;
        this.kota = kota;
        this.noTelepon = noTelepon;
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
