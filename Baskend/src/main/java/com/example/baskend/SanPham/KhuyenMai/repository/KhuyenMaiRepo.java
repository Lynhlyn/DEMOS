package com.example.baskend.SanPham.KhuyenMai.repository;

import com.example.baskend.SanPham.KhuyenMai.entity.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhuyenMaiRepo extends JpaRepository<KhuyenMai, Integer> {
}
