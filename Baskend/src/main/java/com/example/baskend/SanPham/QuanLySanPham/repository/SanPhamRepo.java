package com.example.baskend.SanPham.QuanLySanPham.repository;

import com.example.baskend.SanPham.QuanLySanPham.entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepo extends JpaRepository<SanPham, Integer> {
    List<SanPham> findByTenSanPhamContainingIgnoreCase(String keyword);

}

