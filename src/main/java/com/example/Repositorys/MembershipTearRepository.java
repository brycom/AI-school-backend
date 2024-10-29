package com.example.Repositorys;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Models.MembershipTear;

@Repository
public interface MembershipTearRepository extends CrudRepository<MembershipTear, Integer> {

    public MembershipTear findByPrice(int produktPrice);

}
