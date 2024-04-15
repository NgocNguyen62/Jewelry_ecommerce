package com.ngocnguyen.jewelry_ecommerce.repository;

import com.ngocnguyen.jewelry_ecommerce.entity.User;
import org.springframework.data.jpa.datatables.qrepository.QDataTablesRepository;

public interface UserTableRepository extends QDataTablesRepository<User, Long> {
}
