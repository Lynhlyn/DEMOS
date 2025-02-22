<script setup>
</script>

<template>
      <footer class="footer mt-4 pt-4 border-top bg-light">
            <div class="container">
                  <div class="row text-center text-md-start">
                        <!-- Cột 1: Thông tin công ty -->
                        <div class="col-md-3 mb-3">
                              <h5 class="fw-bold text-primary">Thông tin công ty</h5>
                              <p class="mb-1"><i class="fas fa-building me-2 text-secondary"></i> Công ty TNHH
                                    SportShoes Việt Nam</p>
                              <p class="mb-1"><i class="fas fa-map-marker-alt me-2 text-danger"></i> Tầng 6, Tòa
                                    Friendship, Số 3, Đường Lê Duẩn, Hồ Chí Minh</p>
                              <p class="mb-1"><i class="fas fa-envelope me-2 text-warning"></i> linhcanthimy@gmail.com
                              </p>
                              <p><i class="fas fa-phone me-2 text-success"></i> 03456245672</p>
                        </div>

                        <!-- Cột 2: Chính sách -->
                        <div class="col-md-3 mb-3">
                              <h5 class="fw-bold text-primary">Chính sách</h5>
                              <ul class="list-unstyled">
                                    <li><a href="#" class="policy-link"> Chính sách vận chuyển</a></li>
                                    <li><a href="#" class="policy-link"> Chính sách đổi trả</a></li>
                                    <li><a href="#" class="policy-link"> Chính sách bảo mật</a></li>
                                    <li><a href="#" class="policy-link"> Chính sách kiểm hàng</a></li>
                                    <li><a href="#" class="policy-link"> Nghĩa vụ của người bán và khách hàng</a></li>
                              </ul>
                        </div>

                        <!-- Cột 3: Hỗ trợ khách hàng & Mạng xã hội -->
                        <div class="col-md-3">
                              <h5 class="fw-bold text-primary">Hỗ trợ khách hàng</h5>
                              <div class="d-flex flex-column">
                                    <a href="#" class="support-link"> Giới thiệu</a>
                                    <a href="#" class="support-link"> Quy định chung</a>
                                    <a href="#" class="support-link"> Hệ thống cửa hàng</a>
                                    <a href="#" class="support-link"> Kiểm tra đơn hàng</a>
                              </div>
                        </div>

                        <!-- Logo Bộ Công Thương -->
                        <div class="col-md-3">
                              <img src="@/assets/footer/bocongthuong.webp" alt="Bộ Công Thương"
                                    class="logo mb-2 text-center" width="120" />
                              <div class="d-flex flex-column gap-1">
                                    <span class="fw-medium fs-5">Công ty TNHH SportShoes Việt Nam</span>
                                    <span class="fw-light">Tầng 6, Tòa Friendship, Số 3, Đường Lê Duẩn, Hồ Chí
                                          Minh</span>
                              </div>
                        </div>
                  </div>
            </div>
      </footer>
</template>

<style scoped>
/* Hiệu ứng chung cho liên kết */
.footer a {
      color: #555;
      transition: color 0.3s, transform 0.2s;
}

/* Hiệu ứng hover cho Chính sách */
.policy-link:hover {
      color: #007bff;
      transform: translateX(5px);
}

/* Hiệu ứng hover cho Hỗ trợ khách hàng */
.support-link:hover {
      color: #28a745;
      transform: translateY(-2px);
}
</style>
