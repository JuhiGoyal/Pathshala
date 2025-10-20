package com.pathshala.repository;

import com.pathshala.entities.ContactUs;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactUsRepository extends JpaRepository<ContactUs, Integer> {
}