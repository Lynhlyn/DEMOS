<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const id = route.params.id;

const urlSanPham = "http://localhost:8080/san-pham";

const sanPham = ref({
      tenSanPham: "",
      moTa: "",
      danhMuc: null,
      thuongHieu: null,
      chatLieu: null,
      deGiay: null,
      trangThai: true,
});

const danhMucList = ref([]);
const thuongHieuList = ref([]);
const chatLieuList = ref([]);
const deGiayList = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const successMessage = ref("");

// Load danh sách danh mục, thương hiệu, chất liệu, đế giày
const fetchDropdownData = async () => {
      try {
            const [danhMucRes, thuongHieuRes, chatLieuRes, deGiayRes] = await Promise.all([
                  axios.get("http://localhost:8080/danh-muc"),
                  axios.get("http://localhost:8080/thuong-hieu"),
                  axios.get("http://localhost:8080/chat-lieu"),
                  axios.get("http://localhost:8080/de-giay"),
            ]);

            danhMucList.value = danhMucRes.data;
            thuongHieuList.value = thuongHieuRes.data;
            chatLieuList.value = chatLieuRes.data;
            deGiayList.value = deGiayRes.data;
      } catch (error) {
            console.error("Lỗi khi tải dữ liệu danh mục:", error);
      }
};

// Load dữ liệu sản phẩm theo ID
const fetchSanPham = async () => {
      if (!id) {
            errorMessage.value = "ID sản phẩm không hợp lệ!";
            return;
      }
      loading.value = true;
      try {
            const response = await axios.get(`${urlSanPham}/${id}`);
            if (response.data) {
                  sanPham.value = {
                        tenSanPham: response.data.tenSanPham,
                        moTa: response.data.moTa,
                        danhMuc: response.data.danhMuc ? response.data.danhMuc.id : null,
                        thuongHieu: response.data.thuongHieu ? response.data.thuongHieu.id : null,
                        chatLieu: response.data.chatLieu ? response.data.chatLieu.id : null,
                        deGiay: response.data.deGiay ? response.data.deGiay.id : null,
                        trangThai: response.data.trangThai,
                  };
            } else {
                  throw new Error("Không tìm thấy sản phẩm!");
            }
      } catch (error) {
            errorMessage.value = "Lỗi khi tải dữ liệu sản phẩm.";
            console.error("API Error: ", error);
      } finally {
            loading.value = false;
      }
};

// Gửi yêu cầu cập nhật sản phẩm
const updateSanPham = async () => {
      if (!sanPham.value.tenSanPham.trim()) {
            errorMessage.value = "Tên sản phẩm không được để trống!";
            return;
      }

      loading.value = true;
      errorMessage.value = "";
      successMessage.value = "";

      try {
            const payload = {
                  ...sanPham.value,
                  danhMuc: sanPham.value.danhMuc ? { id: sanPham.value.danhMuc } : null,
                  thuongHieu: sanPham.value.thuongHieu ? { id: sanPham.value.thuongHieu } : null,
                  chatLieu: sanPham.value.chatLieu ? { id: sanPham.value.chatLieu } : null,
                  deGiay: sanPham.value.deGiay ? { id: sanPham.value.deGiay } : null,
            };

            await axios.put(`${urlSanPham}/${id}`, payload);
            successMessage.value = "Cập nhật sản phẩm thành công!";
            setTimeout(() => router.push("/admin/products/manage"), 1500);
      } catch (error) {
            errorMessage.value = "Lỗi khi cập nhật sản phẩm. Vui lòng thử lại!";
            console.error("Lỗi khi cập nhật sản phẩm:", error);
      } finally {
            loading.value = false;
      }
};

onMounted(async () => {
      await fetchDropdownData();
      await fetchSanPham();
});
</script>

<template>
      <div class="container mt-4">
            <h2 class="text-center">Cập nhật sản phẩm</h2>,

            <div v-if="loading" class="alert alert-info text-center">Đang tải dữ liệu...</div>
            <div v-if="errorMessage" class="alert alert-danger text-center">{{ errorMessage }}</div>
            <div v-if="successMessage" class="alert alert-success text-center">{{ successMessage }}</div>

            <form v-if="!loading" @submit.prevent="updateSanPham">
                  <div class="mb-3">
                        <label class="form-label">Tên sản phẩm</label>
                        <input v-model="sanPham.tenSanPham" class="form-control" required />
                  </div>

                  <div class="mb-3">
                        <label class="form-label">Mô tả</label>
                        <textarea v-model="sanPham.moTa" class="form-control"></textarea>
                  </div>

                  <div class="mb-3">
                        <label class="form-label">Danh Mục</label>
                        <select v-model="sanPham.danhMuc" class="form-select">
                              <option v-for="dm in danhMucList" :key="dm.id" :value="dm.id">
                                    {{ dm.tenDanhMuc }}
                              </option>
                        </select>
                  </div>

                  <div class="mb-3">
                        <label class="form-label">Thương Hiệu</label>
                        <select v-model="sanPham.thuongHieu" class="form-select">
                              <option v-for="th in thuongHieuList" :key="th.id" :value="th.id">
                                    {{ th.tenThuongHieu }}
                              </option>
                        </select>
                  </div>

                  <div class="mb-3">
                        <label class="form-label">Chất Liệu</label>
                        <select v-model="sanPham.chatLieu" class="form-select">
                              <option v-for="cl in chatLieuList" :key="cl.id" :value="cl.id">
                                    {{ cl.tenChatLieu }}
                              </option>
                        </select>
                  </div>

                  <div class="mb-3">
                        <label class="form-label">Đế giày</label>
                        <select v-model="sanPham.deGiay" class="form-select">
                              <option v-for="dg in deGiayList" :key="dg.id" :value="dg.id">
                                    {{ dg.tenDeGiay }}
                              </option>
                        </select>
                  </div>

                  <div class="mb-3">
                        <label class="form-label">Trạng thái</label>
                        <select v-model="sanPham.trangThai" class="form-select">
                              <option :value="true">Hoạt động</option>
                              <option :value="false">Ngừng hoạt động</option>
                        </select>
                  </div>

                  <button type="submit" class="btn btn-primary" :disabled="loading">Cập nhật</button>
                  <router-link to="/admin/products/manage" class="btn btn-secondary ms-2">Hủy</router-link>
            </form>
      </div>
</template>
