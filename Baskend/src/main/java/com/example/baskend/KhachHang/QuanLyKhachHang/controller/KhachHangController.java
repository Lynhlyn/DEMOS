package com.example.baskend.KhachHang.QuanLyKhachHang.controller;

import com.example.baskend.KhachHang.QuanLyKhachHang.entity.KhachHang;
import com.example.baskend.KhachHang.QuanLyKhachHang.repository.KhachHangRepo;
import com.example.baskend.KhachHang.QuanLyKhachHang.response.KhachHangResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/khach-hang")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KhachHangController {
    private final KhachHangRepo khachHangRepo;
    @GetMapping("/search")
    public List<KhachHangResponse> searchKhachHang(@RequestParam String keyword) {
        return khachHangRepo.findByHoTenContainingIgnoreCaseOrSoDienThoaiContaining(keyword, keyword).stream()
                .map(this::mapKhachHangToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("")
    public List<KhachHangResponse> getAllKhachHang() {
        return khachHangRepo.findAll().stream()
                .map(this::mapKhachHangToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/addKH")
    public ResponseEntity<?> createKhachHang(@Valid @RequestBody KhachHang khachHang, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        if (khachHang.getMatKhau() == null || khachHang.getMatKhau().isBlank()) {
            return ResponseEntity.badRequest().body("Mật khẩu không được để trống");
        }

        if (khachHangRepo.existsByEmail(khachHang.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }

        if (khachHangRepo.existsBySoDienThoai(khachHang.getSoDienThoai())) {
            return ResponseEntity.badRequest().body("Số điện thoại đã tồn tại");
        }

        if (khachHangRepo.existsByMaKhachHang(khachHang.getMaKhachHang())) {
            return ResponseEntity.badRequest().body("Mã khách hàng đã tồn tại");
        }

        khachHangRepo.save(khachHang);
        return ResponseEntity.ok("Thêm khách hàng thành công!");
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getKhachHangById(@PathVariable Integer id) {
        Optional<KhachHang> khachHang = khachHangRepo.findById(id);
        if (khachHang.isPresent()) {
            return ResponseEntity.ok(khachHang.get());
        } else {
            return ResponseEntity.status(404).body("Khách hàng không tồn tại!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKhachHang(@PathVariable Integer id) {
        if (!khachHangRepo.existsById(id)) {
            return ResponseEntity.badRequest().body("Khách hàng không tồn tại!");
        }
        khachHangRepo.deleteById(id);
        return ResponseEntity.ok("Xóa khách hàng thành công!");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKhachHang(@PathVariable Integer id, @Valid @RequestBody KhachHang khachHang) {
        Optional<KhachHang> existingKhachHangOpt = khachHangRepo.findById(id);
        if (existingKhachHangOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Khách hàng không tồn tại!");
        }

        KhachHang existingKhachHang = existingKhachHangOpt.get();
        existingKhachHang.setMaKhachHang(khachHang.getMaKhachHang());
        existingKhachHang.setHoTen(khachHang.getHoTen());
        existingKhachHang.setTenDangNhap(khachHang.getTenDangNhap());
        existingKhachHang.setGioiTinh(khachHang.getGioiTinh());
        existingKhachHang.setEmail(khachHang.getEmail());
        existingKhachHang.setSoDienThoai(khachHang.getSoDienThoai());
        existingKhachHang.setTrangThai(khachHang.getTrangThai());

        khachHangRepo.save(existingKhachHang);
        return ResponseEntity.ok("Cập nhật khách hàng thành công!");
    }
    private KhachHangResponse mapKhachHangToResponse(KhachHang khachHang) {
        return new KhachHangResponse(
                khachHang.getId(),
                khachHang.getMaKhachHang(),
                khachHang.getTenDangNhap(),
                khachHang.getHoTen(),
                khachHang.getGioiTinh(),
                khachHang.getEmail(),
                khachHang.getMatKhau(),
                khachHang.getSoDienThoai(),
                khachHang.getTrangThai(),
                khachHang.getNgayTao(),
                khachHang.getNgaySua()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleValidationException(Exception ex) {
        return ResponseEntity.badRequest().body("Lỗi khi xử lý request: " + ex.getMessage());
    }
}
