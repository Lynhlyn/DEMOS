<script setup>
import { onMounted, ref, computed, watch } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const sanPhamList = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const searchQuery = ref("");
const router = useRouter();
const urlSanPhamChiTiet = "http://localhost:8080/san-pham-chi-tiet";

const currentPage = ref(1);
const pageSize = 5; // Số sản phẩm trên mỗi trang

const fetchSanPhamChiTiet = async (keyword = "") => {
      loading.value = true;
      errorMessage.value = "";

      try {
            const response = await axios.get(urlSanPhamChiTiet, {
                  params: { keyword: keyword.trim() }, 
            });

            if (Array.isArray(response.data)) {
                  const today = new Date();
                  sanPhamList.value = response.data.map(sp => {
                        const ngayBatDau = sp.ngayBatDau ? new Date(sp.ngayBatDau) : null;
                        const ngayKetThuc = sp.ngayKetThuc ? new Date(sp.ngayKetThuc) : null;

                        let trangThai = sp.trangThai;

                        if ((ngayKetThuc && ngayKetThuc < today) || (ngayBatDau && ngayBatDau > today)) {
                              trangThai = false; 
                        }

                        return {
                              ...sp,
                              tenKhuyenMai: sp.tenKhuyenMai || "Không có",
                              phanTramGiamGia: sp.phanTramGiamGia ? `${sp.phanTramGiamGia}` : "0%",
                              ngayBatDau: ngayBatDau ? ngayBatDau.toLocaleDateString("vi-VN") : "Không có",
                              ngayKetThuc: ngayKetThuc ? ngayKetThuc.toLocaleDateString("vi-VN") : "Không có",
                              ngayTao: sp.ngayTao ? new Date(sp.ngayTao).toLocaleDateString("vi-VN") : "Không có",
                              ngaySua: sp.ngaySua ? new Date(sp.ngaySua).toLocaleDateString("vi-VN") : "Không có",
                              trangThai,
                        };
                  });
            } else {
                  throw new Error("API trả về dữ liệu không hợp lệ");
            }
      } catch (error) {
            errorMessage.value = "Lỗi khi tải dữ liệu sản phẩm. Vui lòng thử lại!";
            console.error("API Error: ", error);
      } finally {
            loading.value = false;
      }
};


// Gọi API tìm kiếm khi người dùng nhập vào ô tìm kiếm
watch(searchQuery, (newVal) => {
      fetchSanPhamChiTiet(newVal);
});

// Tính toán danh sách sản phẩm theo phân trang
const paginatedSanPhamList = computed(() => {
      const startIndex = (currentPage.value - 1) * pageSize;
      return sanPhamList.value.slice(startIndex, startIndex + pageSize);
});

// Tổng số trang
const totalPages = computed(() => {
      return Math.ceil(sanPhamList.value.length / pageSize);
});

// Chuyển trang
const nextPage = () => {
      if (currentPage.value < totalPages.value) {
            currentPage.value++;
      }
};

const prevPage = () => {
      if (currentPage.value > 1) {
            currentPage.value--;
      }
};

const handleDeleteSanPhamChiTiet = async (id) => {
      if (confirm("Bạn có chắc chắn muốn xoá sản phẩm này?")) {
            try {
                  await axios.delete(`${urlSanPhamChiTiet}/${id}`);
                  alert("Xoá sản phẩm thành công!");
                  await fetchSanPhamChiTiet();
            } catch (error) {
                  console.error("Lỗi khi xoá sản phẩm:", error);
                  alert("Lỗi khi xoá sản phẩm. Vui lòng thử lại!");
            }
      }
};

const handleEditSanPhamChiTiet = (id) => {
      router.push(`/admin/products/details/update-spct/${id}`);
};

const handleAddSanPhamChiTiet = () => {
      router.push("/admin/products/details/add-spct");
};



onMounted(fetchSanPhamChiTiet);
</script>

<template>
      <div class="p-4" style="min-height: 450px">
            <h1 class="mb-4 text-center">Quản lý sản phẩm chi tiết</h1>

            <!-- Thanh tìm kiếm và nút thêm mới -->
            <div class="mb-3 d-flex justify-content-between align-items-center">
                  <div class="d-flex w-50">
                        <input v-model="searchQuery" class="form-control me-2" type="text"
                              placeholder="Tìm kiếm sản phẩm theo tên..." />
                        <button class="btn btn-secondary" @click="fetchSanPhamChiTiet">Tìm kiếm</button>
                  </div>
                  <button class="btn btn-success" @click="handleAddSanPhamChiTiet">Thêm mới</button>
            </div>

            <div v-if="loading" class="alert alert-info text-center">Đang tải dữ liệu...</div>
            <div v-if="errorMessage" class="alert alert-danger text-center">{{ errorMessage }}</div>

            <table v-if="!loading && paginatedSanPhamList.length > 0" class="table table-striped table-hover">
                  <thead class="table-dark">
                        <tr class="text-center">
                              <th>STT</th>
                              <th>Mã SPCT</th>
                              <th>Tên sản phẩm</th>
                              <th>Màu sắc</th>
                              <th>Khuyến mãi</th>
                              <th>Phần trăm giảm giá</th>
                              <th>Ngày bắt đầu</th>
                              <th>Ngày kết thúc</th>
                              <th>Size</th>
                              <th>Giá bán</th>
                              <th>Số lượng</th>
                              <th>Ngày tạo</th>
                              <th>Ngày sửa</th>
                              <th>Trạng thái</th>
                              <th>Hành động</th>
                        </tr>
                  </thead>
                  <tbody>
                        <tr v-for="(spct, index) in paginatedSanPhamList" :key="spct.id" class="align-middle">
                              <td class="text-center">{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                              <td>{{ spct.maSPCT }}</td>
                              <td>{{ spct.tenSanPham }}</td>
                              <td>{{ spct.tenMau || "Không có" }}</td>
                              <td>{{ spct.tenKhuyenMai || "Không có" }}</td>
                              <td>{{ spct.phanTramGiamGia ? spct.phanTramGiamGia + "%" : "0%" }}</td>
                              <td class="text-center">{{ spct.ngayBatDau }}</td>
                              <td class="text-center">{{ spct.ngayKetThuc }}</td>
                              <td class="text-center">{{ spct.tenSize }}</td>
                              <td class="text-center">{{ spct.giaBan.toLocaleString() }} đ</td>
                              <td class="text-center">{{ spct.soLuong }}</td>
                              <td class="text-center">{{ spct.ngayTao }}</td>
                              <td class="text-center">{{ spct.ngaySua }}</td>
                              <td class="text-center">
                                    <span class="badge" :class="spct.trangThai ? 'bg-success' : 'bg-danger'">
                                          {{ spct.trangThai ? "Hoạt động" : "Ngừng bán" }}
                                    </span>
                              </td>

                              <td class="text-center">
                                    <div class="d-flex justify-content-center gap-2">
                                          <button class="btn btn-warning btn-sm"
                                                @click="handleEditSanPhamChiTiet(spct.id)">Sửa</button>
                                          <button class="btn btn-danger btn-sm"
                                                @click="handleDeleteSanPhamChiTiet(spct.id)">Xoá</button>
                                    </div>
                              </td>
                        </tr>
                  </tbody>
            </table>

            <div v-else-if="!loading && paginatedSanPhamList.length === 0" class="alert alert-warning text-center">
                  Không có sản phẩm nào!
            </div>

            <!-- Phân trang -->
            <div v-if="totalPages > 1" class="mt-3 d-flex justify-content-center">
                  <button class="btn btn-primary me-2" @click="prevPage" :disabled="currentPage === 1">← Trang
                        trước</button>
                  <span class="align-self-center">Trang {{ currentPage }} / {{ totalPages }}</span>
                  <button class="btn btn-primary ms-2" @click="nextPage" :disabled="currentPage === totalPages">Trang
                        sau →</button>
            </div>
      </div>
</template>
