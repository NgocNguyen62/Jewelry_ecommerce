package com.ngocnguyen.jewelry_ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.datatables.qrepository.QDataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.ngocnguyen.jewelry_ecommerce.repository", repositoryFactoryBeanClass = QDataTablesRepositoryFactoryBean.class)
public class DataTablesConfiguration {}
