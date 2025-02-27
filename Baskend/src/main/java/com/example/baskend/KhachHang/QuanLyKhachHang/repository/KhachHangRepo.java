package com.example.baskend.KhachHang.QuanLyKhachHang.repository;

import com.example.baskend.KhachHang.QuanLyKhachHang.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangRepo extends JpaRepository<KhachHang, Integer> {
    //    List<KhachHang> findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(String hoTen, String email);
    List<KhachHang> findByHoTenContainingIgnoreCaseOrSoDienThoaiContaining(String hoTen, String soDienThoai);
    boolean existsByEmail(String email);
    boolean existsByMaKhachHang(String maKhachHang);
    boolean existsBySoDienThoai(String soDienThoai);

}
