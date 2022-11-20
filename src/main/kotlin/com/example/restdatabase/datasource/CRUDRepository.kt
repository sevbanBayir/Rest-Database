package com.example.restdatabase.datasource

import com.example.restdatabase.model.Bank
import org.springframework.data.jpa.repository.JpaRepository

interface CRUDRepository : JpaRepository<Bank, Int>