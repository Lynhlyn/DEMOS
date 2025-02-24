package com.example.baskend.SanPham.QuanLySanPham.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.DeGiay;
import com.example.baskend.SanPham.QuanLySanPham.repository.DeGiayRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("de-giay")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DeGiayController {
    @Autowired
    DeGiayRepo deGiayRepo;

    @GetMapping("")
    public List<DeGiay> getAllDeGiay() {
        return deGiayRepo.findAll();
    }
}
