<script setup>
import { onMounted, ref, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const khachHangList = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const searchQuery = ref("");
const router = useRouter();
const urlKhachHang = "http://localhost:8080/khach-hang";

const currentPage = ref(1);
const pageSize = 5; // Số khách hàng trên mỗi trang

const fetchKhachHang = async () => {
    loading.value = true;
    errorMessage.value = "";
    try {
        const response = await axios.get(urlKhachHang);
        if (response.data && Array.isArray(response.data)) {
            khachHangList.value = response.data.map(kh => ({
                ...kh,
                ngayTao: kh.ngayTao ? new Date(kh.ngayTao).toLocaleDateString() : "Không có",
                ngaySua: kh.ngaySua ? new Date(kh.ngaySua).toLocaleDateString() : "Không có"
            }));
        } else {
            throw new Error("API trả về dữ liệu không hợp lệ");
        }
    } catch (error) {
        errorMessage.value = "Lỗi khi tải dữ liệu khách hàng. Vui lòng thử lại!";
        console.error("API Error: ", error);
    } finally {
        loading.value = false;
    }
};

const filteredKhachHangList = computed(() => {
    return khachHangList.value.filter(kh =>
        kh.hoTen.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        kh.email.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
});

const paginatedKhachHangList = computed(() => {
    const startIndex = (currentPage.value - 1) * pageSize;
    return filteredKhachHangList.value.slice(startIndex, startIndex + pageSize);
});

const totalPages = computed(() => Math.ceil(filteredKhachHangList.value.length / pageSize));

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

const handleDeleteKhachHang = async (id) => {
    if (confirm("Bạn có chắc chắn muốn xoá khách hàng này?")) {
        try {
            await axios.delete(`${urlKhachHang}/${id}`);
            alert("Xoá khách hàng thành công!");
            await fetchKhachHang();
        } catch (error) {
            console.error("Lỗi khi xoá khách hàng:", error);
            alert("Lỗi khi xoá khách hàng. Vui lòng thử lại!");
        }
    }
};

const handleEditKhachHang = (id) => {
    router.push(`/admin/customers/manage/update-khachhang/${id}`);
};

const handleAddKhachHang = () => {
    router.push('/admin/customers/manage/add-khachhang');
};

onMounted(fetchKhachHang);
</script>

<template>
    <div class="p-4" style="min-height: 450px;">
        <h1 class="mb-4 text-center">Quản lý khách hàng</h1>

        <div class="mb-3 d-flex justify-content-between align-items-center">
            <div class="d-flex w-50">
                <input v-model="searchQuery" class="form-control me-2" type="text" placeholder="Tìm kiếm khách hàng..." />
                <button class="btn btn-secondary">Tìm kiếm</button>
            </div>
            <button class="btn btn-success" @click="handleAddKhachHang">Thêm mới</button>
        </div>

        <div v-if="loading" class="alert alert-info text-center">Đang tải dữ liệu...</div>
        <div v-if="errorMessage" class="alert alert-danger text-center">{{ errorMessage }}</div>

        <table v-if="!loading && paginatedKhachHangList.length > 0" class="table table-striped table-hover">
            <thead class="table-dark">
                <tr class="text-center">
                    <th>STT</th>
                    <th>Mã khách hàng</th>
                    <th>Tên khách hàng</th>
                    <th>Giới tính</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Ngày tạo</th>
                    <th>Ngày sửa</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(khachHang, index) in paginatedKhachHangList" :key="khachHang.id" class="align-middle">
                    <td class="text-center">{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                    <td>{{ khachHang.maKhachHang }}</td>
                    <td>{{ khachHang.hoTen }}</td>
                    <td class="text-center">
                        {{ khachHang.gioiTinh ? 'Nam' : 'Nữ' }}
                    </td>
                    <td>{{ khachHang.email }}</td>
                    <td>{{ khachHang.soDienThoai || 'Không có' }}</td>
                    <td>{{ khachHang.ngayTao }}</td>
                    <td>{{ khachHang.ngaySua }}</td>
                    <td class="text-center">
                        <span class="badge" :class="khachHang.trangThai ? 'bg-success' : 'bg-danger'">
                            {{ khachHang.trangThai ? 'Hoạt động' : 'Đã khoá' }}
                        </span>
                    </td>
                    <td class="text-center">
                        <div class="d-flex justify-content-center gap-2">
                            <button class="btn btn-warning btn-sm" @click="handleEditKhachHang(khachHang.id)">Sửa</button>
                            <button class="btn btn-danger btn-sm" @click="handleDeleteKhachHang(khachHang.id)">Xoá</button>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>

        <div v-else-if="!loading && paginatedKhachHangList.length === 0" class="alert alert-warning text-center">
            Không có khách hàng nào!
        </div>

        <div v-if="totalPages > 1" class="mt-3 d-flex justify-content-center">
            <button class="btn btn-primary me-2" @click="prevPage" :disabled="currentPage === 1">← Trang trước</button>
            <span class="align-self-center">Trang {{ currentPage }} / {{ totalPages }}</span>
            <button class="btn btn-primary ms-2" @click="nextPage" :disabled="currentPage === totalPages">Trang sau →</button>
        </div>
    </div>
</template>
