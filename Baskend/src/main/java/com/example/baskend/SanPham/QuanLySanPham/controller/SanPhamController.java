package com.example.baskend.SanPham.QuanLySanPham.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.*;
import com.example.baskend.SanPham.QuanLySanPham.repository.*;
import com.example.baskend.SanPham.QuanLySanPham.response.SanPhamResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/san-pham")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SanPhamController {
    private final SanPhamRepo sanPhamRepo;
    private final DanhMucRepo danhMucRepo;
    private final ThuongHieuRepo thuongHieuRepo;
    private final ChatLieuRepo chatLieuRepo;
    private final DeGiayRepo deGiayRepo;

    @GetMapping("/{id}")
    public ResponseEntity<SanPham> getSanPhamById(@PathVariable Integer id) {
        Optional<SanPham> sanPham = sanPhamRepo.findById(id);
        return sanPham.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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

    @PostMapping("/addSP")
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
        Optional<SanPham> existingSanPhamOpt = sanPhamRepo.findById(id);
        if (existingSanPhamOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }

        SanPham existingSanPham = existingSanPhamOpt.get();
        existingSanPham.setTenSanPham(sanPham.getTenSanPham());
        existingSanPham.setMoTa(sanPham.getMoTa());
        existingSanPham.setDanhMuc(sanPham.getDanhMuc());
        existingSanPham.setThuongHieu(sanPham.getThuongHieu());
        existingSanPham.setChatLieu(sanPham.getChatLieu());
        existingSanPham.setDeGiay(sanPham.getDeGiay());
        existingSanPham.setTrangThai(sanPham.getTrangThai());
        existingSanPham.setNgaySua(LocalDateTime.now());

        sanPhamRepo.save(existingSanPham);
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

    @GetMapping("/danh-muc")
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepo.findAll();
    }

    @GetMapping("/thuong-hieu")
    public List<ThuongHieu> getAllThuongHieu() {
        return thuongHieuRepo.findAll();
    }

    @GetMapping("/chat-lieu")
    public List<ChatLieu> getAllChatLieu() {
        return chatLieuRepo.findAll();
    }

    @GetMapping("/de-giay")
    public List<DeGiay> getAllDeGiay() {
        return deGiayRepo.findAll();
    }

}
