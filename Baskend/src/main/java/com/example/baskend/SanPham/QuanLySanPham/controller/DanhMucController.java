package com.example.baskend.SanPham.QuanLySanPham.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.DanhMuc;
import com.example.baskend.SanPham.QuanLySanPham.repository.DanhMucRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("danh-muc")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DanhMucController {
    @Autowired
    DanhMucRepo danhMucRepo;

    @GetMapping
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepo.findAll();
    }
}
