package com.example.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Models.MembershipTear;
import com.example.Repositorys.MembershipTearRepository;

@RestController
@RequestMapping("/tear")
@CrossOrigin("*")
public class MembershipTearController {

    @Autowired
    private MembershipTearRepository membershipTearRepository;

    @PostMapping("/tear")
    public String newMembershipTear(@RequestBody MembershipTear membershipTear) {
        membershipTearRepository.save(membershipTear);
        return "MembershipTear created successfully";
    }

    @GetMapping("/allTears")
    public Iterable<MembershipTear> allTears() {
        return membershipTearRepository.findAll();
    }

}
