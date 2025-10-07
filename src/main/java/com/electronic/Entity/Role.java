package com.electronic.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    private String roleID;
    private String name;

//    @ManyToMany(mappedBy ="roles" ,fetch = FetchType.LAZY)
//    private List<User> users= new ArrayList<>();
}
