<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const id = route.params.id;
const BASE_URL = "http://localhost:8080/san-pham-chi-tiet";

const SPCT = ref({
      maSPCT: "",
      tenSanPham: "",
      tenMau: "",
      tenKhuyenMai: "",
      tenSize: "",
      giaBan: 0,
      soLuong: 0,
      trangThai: true,
});

const sanPhamList = ref([]);
const mauSacList = ref([]);
const khuyenMaiList = ref([]);
const sizeList = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const successMessage = ref("");
const errors = ref({});

const fetchDropdownData = async () => {
      try {
            const [spRes, msRes, kmRes, sizeRes] = await Promise.all([
                  axios.get(`${BASE_URL}/san-pham`),
                  axios.get(`${BASE_URL}/mau-sac`),
                  axios.get(`${BASE_URL}/khuyen-mai`),
                  axios.get(`${BASE_URL}/size`),
            ]);
            sanPhamList.value = spRes.data;
            mauSacList.value = msRes.data;
            khuyenMaiList.value = kmRes.data;
            sizeList.value = sizeRes.data;
      } catch (error) {
            errorMessage.value = "Không thể tải dữ liệu từ máy chủ.";
      }
};

const fetchSanPhamChiTiet = async () => {
      if (!id) {
            errorMessage.value = "ID sản phẩm chi tiết không hợp lệ!";
            return;
      }
      loading.value = true;
      try {
            const response = await axios.get(`${BASE_URL}/${id}`);
            if (response.data) {
                  SPCT.value = { ...response.data };
            } else {
                  errorMessage.value = "Không tìm thấy sản phẩm chi tiết.";
            }
      } catch (error) {
            errorMessage.value = "Lỗi khi tải dữ liệu sản phẩm.";
      } finally {
            loading.value = false;
      }
};

const validateForm = () => {
      errors.value = {};
      if (!SPCT.value.maSPCT.trim()) errors.value.maSPCT = "Mã SPCT không được để trống!";
      if (!SPCT.value.tenSanPham) errors.value.tenSanPham = "Vui lòng chọn sản phẩm!";
      if (!SPCT.value.tenMau) errors.value.tenMau = "Vui lòng chọn màu sắc!";
      if (!SPCT.value.tenKhuyenMai) errors.value.tenKhuyenMai = "Vui lòng chọn khuyến mãi!";
      if (!SPCT.value.tenSize) errors.value.tenSize = "Vui lòng chọn size!";
      if (!SPCT.value.giaBan) errors.value.giaBan = "Giá bán không được để trống!";
      if (!SPCT.value.soLuong) errors.value.soLuong = "Số lượng không được để trống!";
      return Object.keys(errors.value).length === 0;
};

const updateSPCT = async () => {
      if (!validateForm()) return;
      loading.value = true;
      errorMessage.value = "";
      successMessage.value = "";
      try {
            await axios.put(`${BASE_URL}/${id}`, SPCT.value);
            successMessage.value = "Cập nhật sản phẩm chi tiết thành công!";
            setTimeout(() => router.push("/admin/products/details"), 1500);
      } catch (error) {
            errorMessage.value = "Lỗi khi cập nhật sản phẩm chi tiết.";
      } finally {
            loading.value = false;
      }
};

onMounted(async () => {
      await fetchDropdownData();
      await fetchSanPhamChiTiet();
});
</script>


<template>
      <div class="container mt-4">
            <h2 class="text-center">Cập nhật sản phẩm chi tiết</h2>

            <div v-if="loading" class="alert alert-info text-center">Đang tải dữ liệu...</div>
            <div v-if="errorMessage" class="alert alert-danger text-center">{{ errorMessage }}</div>
            <div v-if="successMessage" class="alert alert-success text-center">{{ successMessage }}</div>

            <form v-if="!loading" @submit.prevent="updateSPCT">
                  <!-- Mã SPCT -->
                  <div class="mb-3">
                        <label class="form-label">Mã SPCT</label>
                        <input v-model="SPCT.maSPCT" type="text" class="form-control"
                              placeholder="Nhập mã sản phẩm chi tiết">
                        <small v-if="errors.maSPCT" class="text-danger">{{ errors.maSPCT }}</small>
                  </div>

                  <!-- Sản phẩm -->
                  <div class="mb-3">
                        <label class="form-label">Sản phẩm</label>
                        <select v-model="SPCT.tenSanPham" class="form-select" :disabled="loading || !sanPhamList.length">
                              <option value="" disabled>Chọn sản phẩm</option>
                              <option v-for="sp in sanPhamList" :key="sp.id" :value="sp.tenSanPham">
                                    {{ sp.tenSanPham }}
                              </option>
                        </select>
                        <small v-if="errors.tenSanPham" class="text-danger">{{ errors.tenSanPham }}</small>
                  </div>

                  <!-- Màu sắc -->
                  <div class="mb-3">
                        <label class="form-label">Màu sắc</label>
                        <select v-model="SPCT.tenMau" class="form-select" :disabled="loading || !mauSacList.length">
                              <option value="" disabled>Chọn màu sắc</option>
                              <option v-for="ms in mauSacList" :key="ms.id" :value="ms.tenMau">
                                    {{ ms.tenMau }}
                              </option>
                        </select>
                        <small v-if="errors.tenMau" class="text-danger">{{ errors.tenMau }}</small>
                  </div>

                  <!-- Khuyến mãi -->
                  <div class="mb-3">
                        <label class="form-label">Khuyến mãi</label>
                        <select v-model="SPCT.tenKhuyenMai" class="form-select" :disabled="loading || !khuyenMaiList.length">
                              <option value="" disabled>Chọn khuyến mãi</option>
                              <option v-for="km in khuyenMaiList" :key="km.id" :value="km.tenKhuyenMai">
                                    {{ km.tenKhuyenMai }} - {{ km.phanTramGiamGia }}%
                              </option>
                        </select>
                        <small v-if="errors.tenKhuyenMai" class="text-danger">{{ errors.tenKhuyenMai }}</small>
                  </div>

                  <!-- Size -->
                  <div class="mb-3">
                        <label class="form-label">Size</label>
                        <select v-model="SPCT.tenSize" class="form-select" :disabled="loading || !sizeList.length">
                              <option value="" disabled>Chọn size</option>
                              <option v-for="s in sizeList" :key="s.id" :value="s.tenSize">{{ s.tenSize }}</option>
                        </select>
                        <small v-if="errors.tenSize" class="text-danger">{{ errors.tenSize }}</small>
                  </div>

                  <!-- Giá bán -->
                  <div class="mb-3">
                        <label class="form-label">Giá bán</label>
                        <input v-model="SPCT.giaBan" type="number" class="form-control" placeholder="Nhập giá bán sản phẩm">
                        <small v-if="errors.giaBan" class="text-danger">{{ errors.giaBan }}</small>
                  </div>

                  <!-- Số lượng -->
                  <div class="mb-3">
                        <label class="form-label">Số lượng</label>
                        <input v-model="SPCT.soLuong" type="number" class="form-control" placeholder="Nhập số lượng sản phẩm">
                        <small v-if="errors.soLuong" class="text-danger">{{ errors.soLuong }}</small>
                  </div>

                  <!-- Trạng thái -->
                  <div class="mb-3">
                        <label class="form-label">Trạng thái</label>
                        <select v-model="SPCT.trangThai" class="form-select">
                              <option :value="true">Hoạt động</option>
                              <option :value="false">Ngừng hoạt động</option>
                        </select>
                  </div>

                  <button type="submit" class="btn btn-primary" :disabled="loading">Cập nhật</button>
                  <router-link to="/admin/products/details" class="btn btn-secondary ms-2">Hủy</router-link>
            </form>
      </div>
</template>
