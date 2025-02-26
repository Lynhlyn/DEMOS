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
        }
      ],
    },
    {
      path: '/',
      name: 'user-home',
      component: () => import('../views/User/HomePage.vue'),
    },
    ,
    {
      path: '/chinh-sach-van-chuyen',
      name: 'shipping-policy',
      component: () => import('../views/User/Policies/ShippingPolicy.vue'),
    },
    {
      path: '/chinh-sach-doi-tra',
      name: 'return-policy',
      component: () => import('../views/User/Policies/ReturnPolicy.vue'),
    },
    {
      path: '/chinh-sach-bao-mat',
      name: 'privacy-policy',
      component: () => import('../views/User/Policies/PrivacyPolicy.vue'),
    },
    {
      path: '/chinh-sach-kiem-hang',
      name: 'inspection-policy',
      component: () => import('../views/User/Policies/InspectionPolicy.vue'),
    },
    {
      path: '/nghia-vu-nguoi-ban-va-khach-hang',
      name: 'obligation-policy',
      component: () => import('../views/User/Policies/ObligationPolicy.vue'),
    },
    {
      path: '/ho-tro-khach-hang',
      name: 'customer-support',
      component: () => import('../views/User/Support/CustomerSupport.vue'),
    },
    {
      path: '/gioi-thieu',
      name: 'about',
      component: () => import('../views/User/Support/About.vue'),
    },
    {
      path: '/he-thong-cua-hang',
      name: 'store-locations',
      component: () => import('../views/User/Support/StoreLocations.vue'),
    },
    {
      path: '/kiem-tra-don-hang',
      name: 'order-tracking',
      component: () => import('../views/User/Support/OrderTracking.vue'),
    }
  ],
});

export default router;
