package com.example.baskend.SanPham.QuanLySanPham.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.SanPham;
import com.example.baskend.SanPham.QuanLySanPham.repository.SanPhamRepo;
import com.example.baskend.SanPham.QuanLySanPham.response.SanPhamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/san-pham")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SanPhamController {
    private final SanPhamRepo sanPhamRepo;

    @GetMapping("")
    public List<SanPhamResponse> getAllSanPham() {
        return sanPhamRepo.findAll().stream()
                .map(this::mapSanPhamToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("addSP")
    public ResponseEntity<?> createSanPham(@Valid @RequestBody SanPham sanPham, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        sanPhamRepo.save(sanPham);
        return ResponseEntity.ok("Thêm sản phẩm thành công!");
    }

    private SanPhamResponse mapSanPhamToResponse(SanPham sanPham) {
        return new SanPhamResponse(
                sanPham.getId(),
                sanPham.getTenSanPham(),
                sanPham.getMoTa(),
                sanPham.getDanhMuc() != null ? sanPham.getDanhMuc().getTenDanhMuc() : "Không có",
                sanPham.getThuongHieu() != null ? sanPham.getThuongHieu().getTenThuongHieu() : "Không có",
                sanPham.getChatLieu() != null ? sanPham.getChatLieu().getTenChatLieu() : "Không có",
                sanPham.getDeGiay() != null ? sanPham.getDeGiay().getTenDeGiay() : "Không có",
                sanPham.getNgayTao(),
                sanPham.getNgaySua(),
                sanPham.getTrangThai()
        );
    }

    // Xử lý lỗi validation
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleValidationException(Exception ex) {
        return ResponseEntity.badRequest().body("Lỗi khi xử lý request: " + ex.getMessage());
    }
}
