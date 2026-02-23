package br.com.klsys.dscommerce.dto;

import br.com.klsys.dscommerce.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;

    private List<String> roles = new ArrayList<>();

    public UserDTO(User entity) {
         this.id = entity.getId();
         this.name = entity.getName();
         this.email = entity.getEmail();
         this.phone = entity.getPhone();
         this.birthDate = entity.getBirthDate();
         entity.getRoles().forEach(role -> this.roles.add(role.getAuthority()));
    }

}
