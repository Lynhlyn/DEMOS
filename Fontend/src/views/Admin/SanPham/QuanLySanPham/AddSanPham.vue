<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

// URL API
const BASE_URL = "http://localhost:8080";
const urlSanPham = `${BASE_URL}/san-pham/addSP`;
const urlDanhMuc = `${BASE_URL}/danh-muc`;
const urlThuongHieu = `${BASE_URL}/thuong-hieu`;
const urlChatLieu = `${BASE_URL}/chat-lieu`;
const urlDeGiay = `${BASE_URL}/de-giay`;

// Dữ liệu form
const tenSanPham = ref("");
const moTa = ref("");
const danhMuc = ref("");
const thuongHieu = ref("");
const chatLieu = ref("");
const deGiay = ref("");
const trangThai = ref(true); // Mặc định là hoạt động

// Danh sách combobox
const danhMucList = ref([]);
const thuongHieuList = ref([]);
const chatLieuList = ref([]);
const deGiayList = ref([]);

// Trạng thái xử lý form
const isLoading = ref(false);
const errors = ref({});

// Fetch dữ liệu combobox
const fetchDropdownData = async () => {
    try {
        const [resDanhMuc, resThuongHieu, resChatLieu, resDeGiay] = await Promise.all([
            axios.get(urlDanhMuc),
            axios.get(urlThuongHieu),
            axios.get(urlChatLieu),
            axios.get(urlDeGiay),
        ]);

        danhMucList.value = resDanhMuc.data.length ? resDanhMuc.data : [];
        thuongHieuList.value = resThuongHieu.data.length ? resThuongHieu.data : [];
        chatLieuList.value = resChatLieu.data.length ? resChatLieu.data : [];
        deGiayList.value = resDeGiay.data.length ? resDeGiay.data : [];

        if (!danhMucList.value.length || !thuongHieuList.value.length || !chatLieuList.value.length || !deGiayList.value.length) {
            alert("Một số danh sách trống! Vui lòng kiểm tra API.");
        }
    } catch (error) {
        console.error("Lỗi khi lấy dữ liệu combobox:", error);
        alert("Không thể tải dữ liệu danh mục, thương hiệu, chất liệu, đế giày!");
    }
};

// Xử lý lỗi validation
const validateForm = () => {
    errors.value = {};
    if (!tenSanPham.value) errors.value.tenSanPham = "Tên sản phẩm không được để trống";
    if (!danhMuc.value) errors.value.danhMuc = "Vui lòng chọn danh mục";
    if (!thuongHieu.value) errors.value.thuongHieu = "Vui lòng chọn thương hiệu";
    if (!chatLieu.value) errors.value.chatLieu = "Vui lòng chọn chất liệu";
    if (!deGiay.value) errors.value.deGiay = "Vui lòng chọn đế giày";
    return Object.keys(errors.value).length === 0;
};

// Hàm xử lý thêm sản phẩm
const handleAddSanPham = async () => {
    if (!validateForm()) return; // Dừng nếu form không hợp lệ

    const newSanPham = {
        tenSanPham: tenSanPham.value,
        moTa: moTa.value,
        danhMuc: { id: danhMuc.value },
        thuongHieu: { id: thuongHieu.value },
        chatLieu: { id: chatLieu.value },
        deGiay: { id: deGiay.value },
        trangThai: trangThai.value,
    };

    isLoading.value = true;

    try {
        const response = await axios.post(urlSanPham, newSanPham);
        console.log("Thêm sản phẩm thành công:", response.data);
        alert("Thêm sản phẩm thành công!");
        router.push('/admin/products/manage');
    } catch (error) {
        console.error("Lỗi khi thêm sản phẩm:", error.response ? error.response.data : error.message);
        errors.value.apiError = error.response?.data || "Có lỗi xảy ra, vui lòng thử lại!";
    } finally {
        isLoading.value = false;
    }
};

const goBack = () => {
    router.go(-1);
};

onMounted(fetchDropdownData);
</script>


<template>
    <div class="p-4" style="min-height: 450px;">
        <h1 class="mb-4 text-center">Thêm sản phẩm mới</h1>

        <div class="card p-4 shadow-sm">
            <form @submit.prevent="handleAddSanPham">
                <!-- Tên sản phẩm -->
                <div class="mb-3">
                    <label class="form-label">Tên sản phẩm</label>
                    <input v-model="tenSanPham" type="text" class="form-control" placeholder="Nhập tên sản phẩm">
                    <small v-if="errors.tenSanPham" class="text-danger">{{ errors.tenSanPham }}</small>
                </div>

                <!-- Mô tả -->
                <div class="mb-3">
                    <label class="form-label">Mô tả</label>
                    <textarea v-model="moTa" class="form-control" placeholder="Nhập mô tả sản phẩm"></textarea>
                </div>

                <!-- Danh mục -->
                <div class="mb-3">
                    <label class="form-label">Danh mục</label>
                    <select v-model="danhMuc" class="form-select">
                        <option value="" disabled>Chọn danh mục</option>
                        <option v-for="dm in danhMucList" :key="dm.id" :value="dm.id">{{ dm.tenDanhMuc }}</option>
                    </select>
                    <small v-if="errors.danhMuc" class="text-danger">{{ errors.danhMuc }}</small>
                </div>

                <!-- Thương hiệu -->
                <div class="mb-3">
                    <label class="form-label">Thương hiệu</label>
                    <select v-model="thuongHieu" class="form-select">
                        <option value="" disabled>Chọn thương hiệu</option>
                        <option v-for="th in thuongHieuList" :key="th.id" :value="th.id">{{ th.tenThuongHieu }}</option>
                    </select>
                    <small v-if="errors.thuongHieu" class="text-danger">{{ errors.thuongHieu }}</small>
                </div>

                <!-- Chất liệu -->
                <div class="mb-3">
                    <label class="form-label">Chất liệu</label>
                    <select v-model="chatLieu" class="form-select">
                        <option value="" disabled>Chọn chất liệu</option>
                        <option v-for="cl in chatLieuList" :key="cl.id" :value="cl.id">{{ cl.tenChatLieu }}</option>
                    </select>
                    <small v-if="errors.chatLieu" class="text-danger">{{ errors.chatLieu }}</small>
                </div>

                <!-- Đế giày -->
                <div class="mb-3">
                    <label class="form-label">Đế giày</label>
                    <select v-model="deGiay" class="form-select">
                        <option value="" disabled>Chọn đế giày</option>
                        <option v-for="dg in deGiayList" :key="dg.id" :value="dg.id">{{ dg.tenDeGiay }}</option>
                    </select>
                    <small v-if="errors.deGiay" class="text-danger">{{ errors.deGiay }}</small>
                </div>

                <!-- Trạng thái -->
                <div class="mb-3">
                    <label class="form-label">Trạng thái</label>
                    <select v-model="trangThai" class="form-select">
                        <option :value="true">Hoạt động</option>
                        <option :value="false">Ngừng hoạt động</option>
                    </select>
                </div>

                <!-- Thông báo lỗi API -->
                <div v-if="errors.apiError" class="alert alert-danger">{{ errors.apiError }}</div>

                <div class="d-flex gap-3">
                    <button type="submit" class="btn btn-primary" :disabled="isLoading">
                        {{ isLoading ? "Đang xử lý..." : "Thêm sản phẩm" }}
                    </button>
                    <button type="button" class="btn btn-secondary" @click="router.go(-1)">
                        Quay lại
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>