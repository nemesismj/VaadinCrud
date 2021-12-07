package com.tutorial.vaadin.repository;

import com.tutorial.vaadin.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    @Query("from Employee e " +
            "where concat(lower(e.lastName), ' ' , lower(e.firstName), ' ' ,lower(e.patronymic))" +
            "like concat ('%', lower(:name), '%')")
    List<Employee> findByName(@Param("name") String name);
}
