import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/Admin/HomePage.vue'),
      children: [
        {
          path: 'products/manage',
          name: 'admin-products',
          component: () => import('../views/Admin/SanPham/QuanLySanPham/SanPham.vue'),
        },
        {
          path: 'products/manage/add-sanpham',
          name: 'admin-add-product',
          component: () => import('../views/Admin/SanPham/QuanLySanPham/AddSanPham.vue'),
        },
        {
          path: 'products/manage/update-sanpham/:id',
          name: 'admin-update-product',
          component: () => import('../views/Admin/SanPham/QuanLySanPham/UpdateSanPham.vue'),
          props: true, // Truyền ID sản phẩm qua props
        },
        {
          path: 'products/details',
          name: 'products-details',
          component: () => import('../views/Admin/SanPham/SanPhamChiTiet/SanPhamChiTiet.vue'),
        },
        {
          path: 'products/details/add-spct',
          name: 'add-products-details',
          component: () => import('../views/Admin/SanPham/SanPhamChiTiet/AddSPCT.vue'),
        },
        {
          path: 'products/details/update-spct/:id',
          name: 'update-products-details',
          component: () => import('../views/Admin/SanPham/SanPhamChiTiet/UpdateSPCT.vue'),
        },

        //  Khach hang

        {
          path: "/admin/customers",
          name: "customers",
          component: () => import("../views/Admin/KhachHang/QuanLyKhachHang/KhachHang.vue")
        },
        {
          path: 'customers/manage/add-khachhang',
          name: 'admin-add-customer',
          component: () => import('../views/Admin/KhachHang/QuanLyKhachHang/AddKhachHang.vue'),
        },
      ],
    },
    {
      path: '/',
      name: 'user-home',
      component: () => import('../views/User/HomePage.vue'),
    },
  ],
});

export default router;
