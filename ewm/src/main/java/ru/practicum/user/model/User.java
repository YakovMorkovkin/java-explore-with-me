package ru.practicum.user.model;

import lombok.*;
import ru.practicum.user.dto.UserDto;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
@SqlResultSetMapping(name = "userEntityMapping",
        classes = {
                @ConstructorResult(
                        targetClass = UserDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "name", type = String.class),
                                @ColumnResult(name = "email", type = String.class)
                        }
                )
        }
)
@NamedNativeQuery(
        name = "User.findUsersFrom",
        query = "SELECT id, name, email " +
                "FROM users " +
                "ORDER BY id " +
                "OFFSET :first " +
                "LIMIT :size",
        resultSetMapping = "userEntityMapping"
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
}
