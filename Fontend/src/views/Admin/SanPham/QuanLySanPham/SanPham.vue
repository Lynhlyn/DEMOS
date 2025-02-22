<script setup>
import { onMounted, ref, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const sanPhamList = ref([]);
const loading = ref(false);
const errorMessage = ref("");
const searchQuery = ref("");
const router = useRouter();
const urlSanPham = "http://localhost:8080/san-pham";

const currentPage = ref(1);
const pageSize = 5; // Số sản phẩm trên mỗi trang

const fetchSanPham = async () => {
    loading.value = true;
    errorMessage.value = "";
    try {
        const response = await axios.get(urlSanPham);
        if (response.data && Array.isArray(response.data)) {
            sanPhamList.value = response.data.map(sp => ({
                ...sp,
                ngayTao: sp.ngayTao ? new Date(sp.ngayTao).toLocaleDateString() : "Không có",
                ngaySua: sp.ngaySua ? new Date(sp.ngaySua).toLocaleDateString() : "Không có"
            }));
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

// Lọc danh sách theo tìm kiếm
const filteredSanPhamList = computed(() => {
    return sanPhamList.value.filter(sp => 
        sp.tenSanPham.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
});

// Tính toán danh sách sản phẩm theo phân trang
const paginatedSanPhamList = computed(() => {
    const startIndex = (currentPage.value - 1) * pageSize;
    return filteredSanPhamList.value.slice(startIndex, startIndex + pageSize);
});

// Tổng số trang
const totalPages = computed(() => {
    return Math.ceil(filteredSanPhamList.value.length / pageSize);
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

const handleDeleteSanPham = async (id) => {
    if (confirm("Bạn có chắc chắn muốn xoá sản phẩm này?")) {
        try {
            await axios.delete(`${urlSanPham}/${id}`);
            alert("Xoá sản phẩm thành công!");
            await fetchSanPham();
        } catch (error) {
            console.error("Lỗi khi xoá sản phẩm:", error);
            alert("Lỗi khi xoá sản phẩm. Vui lòng thử lại!");
        }
    }
};

const handleEditSanPham = (id) => {
    router.push(`/admin/products/manage/update-sanpham/${id}`);
};

const handleAddSanPham = () => {
    router.push('/admin/products/manage/add-sanpham');
};

onMounted(fetchSanPham);
</script>

<template>
    <div class="p-4" style="min-height: 450px;">
        <h1 class="mb-4 text-center">Quản lý sản phẩm</h1>

        <!-- Thanh tìm kiếm và nút thêm mới -->
        <div class="mb-3 d-flex justify-content-between align-items-center">
            <div class="d-flex w-50">
                <input v-model="searchQuery" class="form-control me-2" type="text" placeholder="Tìm kiếm sản phẩm theo tên..." />
                <button class="btn btn-secondary">Tìm kiếm</button>
            </div>
            <button class="btn btn-success" @click="handleAddSanPham">Thêm mới</button>
        </div>

        <div v-if="loading" class="alert alert-info text-center">Đang tải dữ liệu...</div>
        <div v-if="errorMessage" class="alert alert-danger text-center">{{ errorMessage }}</div>

        <table v-if="!loading && paginatedSanPhamList.length > 0" class="table table-striped table-hover">
            <thead class="table-dark">
                <tr class="text-center">
                    <th>STT</th>
                    <th>Tên sản phẩm</th>
                    <th>Mô tả</th>
                    <th>Danh Mục</th>
                    <th>Thương Hiệu</th>
                    <th>Chất liệu</th>
                    <th>Đế giày</th>
                    <th>Ngày tạo</th>
                    <th>Ngày sửa</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(sanPham, index) in paginatedSanPhamList" :key="sanPham.id" class="align-middle">
                    <td class="text-center">{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                    <td>{{ sanPham.tenSanPham }}</td>
                    <td>{{ sanPham.moTa }}</td>
                    <td>{{ sanPham.danhMuc || 'Không có' }}</td>
                    <td>{{ sanPham.thuongHieu || 'Không có' }}</td>
                    <td>{{ sanPham.chatLieu || 'Không có' }}</td>
                    <td>{{ sanPham.deGiay || 'Không có' }}</td>
                    <td class="text-center">{{ sanPham.ngayTao }}</td>
                    <td class="text-center">{{ sanPham.ngaySua }}</td>
                    <td class="text-center">
                        <span class="badge" :class="sanPham.trangThai ? 'bg-success' : 'bg-danger'">
                            {{ sanPham.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
                        </span>
                    </td>
                    <td class="text-center">
                        <div class="d-flex justify-content-center gap-2">
                            <button class="btn btn-warning btn-sm" @click="handleEditSanPham(sanPham.id)">Sửa</button>
                            <button class="btn btn-danger btn-sm" @click="handleDeleteSanPham(sanPham.id)">Xoá</button>
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
            <button class="btn btn-primary me-2" @click="prevPage" :disabled="currentPage === 1">← Trang trước</button>
            <span class="align-self-center">Trang {{ currentPage }} / {{ totalPages }}</span>
            <button class="btn btn-primary ms-2" @click="nextPage" :disabled="currentPage === totalPages">Trang sau →</button>
        </div>
    </div>
</template>
