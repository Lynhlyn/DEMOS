package com.example.baskend.SanPham.SanPhamChiTiet.repository;

import com.example.baskend.SanPham.SanPhamChiTiet.entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamChiTietRepo extends JpaRepository<SanPhamChiTiet, Integer> {
    List<SanPhamChiTiet> findBySanPham_TenSanPhamContainingIgnoreCase(String tenSanPham);
}
