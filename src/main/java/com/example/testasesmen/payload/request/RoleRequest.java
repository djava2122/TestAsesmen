package com.example.testasesmen.payload.request;

import com.example.testasesmen.models.ERole;

public class RoleRequest {

    private ERole namaRole;

    public void setNamaRole(ERole namaRole) {
        this.namaRole = namaRole;
    }

    public ERole getNamaRole() {
        return namaRole;
    }
}
