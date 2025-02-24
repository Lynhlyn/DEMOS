package com.example.baskend.SanPham.QuanLySanPham.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.ThuongHieu;
import com.example.baskend.SanPham.QuanLySanPham.repository.ThuongHieuRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("thuong-hieu")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThuongHieuController {
    @Autowired
    ThuongHieuRepo thuongHieuRepo;

    @GetMapping
    public List<ThuongHieu> getAllThuongHieu() {
        return thuongHieuRepo.findAll();
    }
}
