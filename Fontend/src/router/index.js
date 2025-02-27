import { createRouter, createWebHistory } from 'vue-router';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../views/Admin/HomePage.vue'),
      children: [
        // Bán hàng
        // {
        //   path: 'sales/offline',
        //   name: 'sales-offline',
        //   component: () => import('../views/Admin/Sales/OfflineSales.vue'),
        // },
        // {
        //   path: 'sales/online',
        //   name: 'sales-online',
        //   component: () => import('../views/Admin/Sales/OnlineSales.vue'),
        // },
        // Thống kê
        // {
        //   path: 'stats',
        //   name: 'stats',
        //   component: () => import('../views/Admin/Stats/StatsPage.vue'),
        // },
        // Sản phẩm
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
          props: true,
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
        // Voucher
        // {
        //   path: 'vouchers',
        //   name: 'vouchers',
        //   component: () => import('../views/Admin/Vouchers/VoucherManage.vue'),
        // },

        // // Nhân viên
        // {
        //   path: 'staff',
        //   name: 'staff',
        //   component: () => import('../views/Admin/Staff/StaffManage.vue'),
        // },

        // // Khách hàng
        // {
        //   path: 'customers',
        //   name: 'customers',
        //   component: () => import('../views/Admin/Customers/CustomerManage.vue'),
        // },

        // // Đơn hàng
        // {
        //   path: 'orders',
        //   name: 'orders',
        //   component: () => import('../views/Admin/Orders/OrderDetails.vue'),
        // },
        // {
        //   path: 'order-details',
        //   name: 'order-details',
        //   component: () => import('../views/Admin/Orders/OrderDetails.vue'),
        // },

        // // Chat hỗ trợ
        // {
        //   path: 'chat',
        //   name: 'chat-support',
        //   component: () => import('../views/Admin/Chat/ChatSupport.vue'),
        // },
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
