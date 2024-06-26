package com.hisham.HomeCentre.repositories;

import com.hisham.HomeCentre.models.Order;
import com.hisham.HomeCentre.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUser(User user, Pageable pageable);
}
