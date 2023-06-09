package uz.BTService.btservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import uz.BTService.btservice.constants.TableNames;
import uz.BTService.btservice.controller.dto.UserDto;
import uz.BTService.btservice.entity.base.BaseServerModifierEntity;
import uz.BTService.btservice.entity.base.BaseUserEntity;
import uz.BTService.btservice.entity.role.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = TableNames.DEPARTMENT_USER)
public class UserEntity extends BaseUserEntity implements UserDetails {

    private String firstname;
    private String lastname;
    private String middleName;
    private Date birtDate;

    @OneToOne
    private AttachEntity attach;

    @Column(unique = true, nullable = false)
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String username;
    private String password;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private RegionEntity region;

    @Enumerated(EnumType.STRING)
    private List<RoleEnum> roleEnumList;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roleEnumList.forEach((rol) -> {
            roles.add(new SimpleGrantedAuthority("ROLE_" + rol.name()));
        });

        return roles;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    /************************************************************
     * ******************** CONVERT TO DTO ***********************
     * ***********************************************************/
    @JsonIgnore
    public UserDto toDto(String... ignoreProperties) {
        return toDto(this, new UserDto(), ignoreProperties);
    }

}
