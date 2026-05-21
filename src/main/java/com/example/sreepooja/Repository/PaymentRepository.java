package com.example.sreepooja.Repository;


import com.example.sreepooja.Entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payments, Long> {

    Optional<Payments> findByRazorpayOrderId(String orderId);

}
