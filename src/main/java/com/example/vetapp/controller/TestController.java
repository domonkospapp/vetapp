package com.example.vetapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class TestController {
  
  @GetMapping("/api/test/user")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public String userAccess() {
    return ">>> User Contents!";
  }
  
  @GetMapping("/api/test/doctor")
  @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
  public String projectManagementAccess() {
    return ">>> Board Management Project";
  }
  
  @GetMapping("/api/test/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return ">>> Admin Contents";
  }
}
