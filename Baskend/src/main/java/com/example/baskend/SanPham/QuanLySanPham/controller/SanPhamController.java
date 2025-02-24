package com.example.baskend.SanPham.QuanLySanPham.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.SanPham;
import com.example.baskend.SanPham.QuanLySanPham.repository.SanPhamRepo;
import com.example.baskend.SanPham.QuanLySanPham.response.SanPhamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public List<SanPhamResponse> getAllSanPham(@RequestParam(required = false) String keyword) {
        List<SanPham> sanPhams;
        if (keyword != null && !keyword.trim().isEmpty()) {
            sanPhams = sanPhamRepo.findByTenSanPhamContainingIgnoreCase(keyword);
        } else {
            sanPhams = sanPhamRepo.findAll();
        }
        return sanPhams.stream()
                .map(this::mapSanPhamToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<SanPhamResponse> searchSanPham(@RequestParam String keyword) {
        return sanPhamRepo.findByTenSanPhamContainingIgnoreCase(keyword).stream()
                .map(this::mapSanPhamToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("addSP")
    public ResponseEntity<?> createSanPham(@Valid @RequestBody SanPham sanPham) {
        sanPhamRepo.save(sanPham);
        return ResponseEntity.ok("Thêm sản phẩm thành công!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSanPham(@PathVariable Integer id) {
        if (!sanPhamRepo.existsById(id)) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }
        sanPhamRepo.deleteById(id);
        return ResponseEntity.ok("Xoá sản phẩm thành công!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSanPham(@PathVariable Integer id, @Valid @RequestBody SanPham sanPham) {
        if (!sanPhamRepo.existsById(id)) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }
        sanPham.setId(id);
        sanPhamRepo.save(sanPham);
        return ResponseEntity.ok("Sửa sản phẩm thành công!");
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
}
