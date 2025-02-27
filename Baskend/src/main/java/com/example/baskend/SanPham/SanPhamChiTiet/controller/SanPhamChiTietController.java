package com.example.baskend.SanPham.SanPhamChiTiet.controller;

import com.example.baskend.SanPham.QuanLySanPham.entity.SanPham;
import com.example.baskend.SanPham.QuanLySanPham.repository.SanPhamRepo;
import com.example.baskend.SanPham.KhuyenMai.entity.KhuyenMai;
import com.example.baskend.SanPham.SanPhamChiTiet.entity.MauSac;
import com.example.baskend.SanPham.SanPhamChiTiet.entity.SanPhamChiTiet;
import com.example.baskend.SanPham.SanPhamChiTiet.entity.Size;
import com.example.baskend.SanPham.KhuyenMai.repository.KhuyenMaiRepo;
import com.example.baskend.SanPham.SanPhamChiTiet.repository.MauSacRepo;
import com.example.baskend.SanPham.SanPhamChiTiet.repository.SanPhamChiTietRepo;
import com.example.baskend.SanPham.SanPhamChiTiet.repository.SizeRepo;
import com.example.baskend.SanPham.SanPhamChiTiet.response.SanPhamChiTietResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/san-pham-chi-tiet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SanPhamChiTietController {
    private final SanPhamChiTietRepo sanPhamChiTietRepo;
    private final SanPhamRepo sanPhamRepo;
    private final MauSacRepo mauSacRepo;
    private final KhuyenMaiRepo khuyenMaiRepo;
    private final SizeRepo sizeRepo;

    @GetMapping("")
    public ResponseEntity<List<SanPhamChiTietResponse>> getAllSanPhamChiTiet(
            @RequestParam(required = false, defaultValue = "") String keyword) {

        List<SanPhamChiTiet> sanPhamChiTietList = keyword.trim().isEmpty()
                ? sanPhamChiTietRepo.findAll()
                : sanPhamChiTietRepo.searchByTenSanPham(keyword.trim());

        List<SanPhamChiTietResponse> responseList = sanPhamChiTietList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @PostMapping("/add-spct")
    public ResponseEntity<?> createSanPhamChiTiet(@Valid @RequestBody SanPhamChiTiet sanPhamChiTiet) {
        if (sanPhamChiTiet.getSanPham() == null || sanPhamChiTiet.getMauSac() == null ||
                sanPhamChiTiet.getSize() == null || sanPhamChiTiet.getMaSPCT() == null || sanPhamChiTiet.getMaSPCT().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Thiếu thông tin sản phẩm, màu sắc, size hoặc mã sản phẩm!");
        }

        Optional<SanPham> sanPhamOpt = sanPhamRepo.findById(sanPhamChiTiet.getSanPham().getId());
        Optional<MauSac> mauSacOpt = mauSacRepo.findById(sanPhamChiTiet.getMauSac().getId());
        Optional<Size> sizeOpt = sizeRepo.findById(sanPhamChiTiet.getSize().getId());
        Optional<KhuyenMai> khuyenMaiOpt = (sanPhamChiTiet.getKhuyenMai() != null && sanPhamChiTiet.getKhuyenMai().getId() != null)
                ? khuyenMaiRepo.findById(sanPhamChiTiet.getKhuyenMai().getId()) : Optional.empty();

        if (sanPhamOpt.isEmpty() || mauSacOpt.isEmpty() || sizeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Sản phẩm, màu sắc hoặc size không hợp lệ!");
        }

        if (sanPhamChiTiet.getGiaBan() <= 0 || sanPhamChiTiet.getSoLuong() < 0) {
            return ResponseEntity.badRequest().body("Giá bán phải lớn hơn 0, số lượng không được âm!");
        }

        sanPhamChiTiet.setSanPham(sanPhamOpt.get());
        sanPhamChiTiet.setMauSac(mauSacOpt.get());
        sanPhamChiTiet.setSize(sizeOpt.get());
        sanPhamChiTiet.setKhuyenMai(khuyenMaiOpt.orElse(null));

        sanPhamChiTietRepo.save(sanPhamChiTiet);
        return ResponseEntity.ok("Thêm sản phẩm chi tiết thành công!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSanPhamChiTiet(@PathVariable Integer id, @Valid @RequestBody SanPhamChiTiet sanPhamChiTiet) {
        Optional<SanPhamChiTiet> existingSPCTOpt = sanPhamChiTietRepo.findById(id);
        if (existingSPCTOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Sản phẩm chi tiết không tồn tại!");
        }

        SanPhamChiTiet existingSPCT = existingSPCTOpt.get();

        if (sanPhamChiTiet.getGiaBan() <= 0 || sanPhamChiTiet.getSoLuong() < 0) {
            return ResponseEntity.badRequest().body("Giá bán phải lớn hơn 0, số lượng không được âm!");
        }

        existingSPCT.setMauSac(sanPhamChiTiet.getMauSac());
        existingSPCT.setKhuyenMai(sanPhamChiTiet.getKhuyenMai());
        existingSPCT.setSize(sanPhamChiTiet.getSize());
        existingSPCT.setGiaBan(sanPhamChiTiet.getGiaBan());
        existingSPCT.setSoLuong(sanPhamChiTiet.getSoLuong());
        existingSPCT.setTrangThai(sanPhamChiTiet.getTrangThai());

        sanPhamChiTietRepo.save(existingSPCT);
        return ResponseEntity.ok("Cập nhật sản phẩm chi tiết thành công!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSanPhamChiTiet(@PathVariable Integer id) {
        if (!sanPhamChiTietRepo.existsById(id)) {
            return ResponseEntity.badRequest().body("Sản phẩm chi tiết không tồn tại!");
        }
        sanPhamChiTietRepo.deleteById(id);
        return ResponseEntity.ok("Xóa sản phẩm chi tiết thành công!");
    }

    @GetMapping("/san-pham")
    public List<SanPham> getAllSanPham() {
        return sanPhamRepo.findAll();
    }

    @GetMapping("/mau-sac")
    public List<MauSac> getAllMauSac() {
        return mauSacRepo.findAll();
    }

    @GetMapping("/khuyen-mai")
    public List<KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiRepo.findAll();
    }

    @GetMapping("/size")
    public List<Size> getAllSize() {
        return sizeRepo.findAll();
    }

    private SanPhamChiTietResponse mapToResponse(SanPhamChiTiet spct) {
        KhuyenMai khuyenMai = spct.getKhuyenMai();
        return new SanPhamChiTietResponse(
                spct.getId(),
                spct.getMaSPCT(),
                spct.getSanPham().getTenSanPham(),
                spct.getMauSac().getTenMau(),
                khuyenMai != null ? khuyenMai.getTenKhuyenMai() : "Không có khuyến mãi",
                khuyenMai != null ? khuyenMai.getPhanTramGiamGia() : 0F,
                khuyenMai != null ? khuyenMai.getNgayBatDau() : null,
                khuyenMai != null ? khuyenMai.getNgayKetThuc() : null,
                spct.getSize().getTenSize(),
                spct.getGiaBan(),
                spct.getSoLuong(),
                spct.getNgayTao(),
                spct.getNgaySua(),
                spct.getTrangThai()
        );
    }
}
