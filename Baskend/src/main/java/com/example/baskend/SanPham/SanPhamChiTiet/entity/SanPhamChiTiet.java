package com.example.baskend.SanPham.SanPhamChiTiet.entity;

import com.example.baskend.SanPham.QuanLySanPham.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Mã sản phẩm chi tiết không được để trống")
    @Column(name = "ma_spct")
    private String maSPCT;

    @NotNull(message = "Sản phẩm không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_san_pham", referencedColumnName = "id")
    private SanPham sanPham;

    @NotNull(message = "Màu sắc không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_mau_sac", referencedColumnName = "id")
    private MauSac mauSac;

    @NotNull(message = "Khuyến mại không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_khuyen_mai", referencedColumnName = "id")
    private KhuyenMai khuyenMai;

    @NotNull(message = "Size không được để trống")
    @ManyToOne
    @JoinColumn(name = "id_size", referencedColumnName = "id")
    private Size size;

    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 0, message = "Giá bán không được nhỏ hơn 0")
    @Column(name = "gia_ban")
    private Float giaBan;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không được nhỏ hơn 0")
    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "ngay_tao")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_sua")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngaySua;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @PrePersist
    protected void onCreate() {
        this.ngayTao = LocalDateTime.now();
        this.ngaySua = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.ngaySua = LocalDateTime.now();
    }
}
