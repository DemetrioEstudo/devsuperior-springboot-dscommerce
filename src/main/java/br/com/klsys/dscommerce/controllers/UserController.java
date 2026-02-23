package br.com.klsys.dscommerce.controllers;

import br.com.klsys.dscommerce.dto.ProductDTO;

import br.com.klsys.dscommerce.dto.UserDTO;
import br.com.klsys.dscommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value="/users")
public class UserController {

    @Autowired
    private UserService service;


    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO>  getMe(){
        UserDTO dto =  service.getMe();
        return ResponseEntity.ok(dto);
    }

}
