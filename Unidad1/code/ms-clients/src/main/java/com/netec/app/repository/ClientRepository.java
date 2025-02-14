package com.netec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.netec.app.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
