package de.cofinpro.webquizengine.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Persistence layer entity object representing a registered user.
 * The solution is not displayed to clients who attempt to solve this quiz,
 * therefore the getter is marked @JsonIgnore.
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class RegisteredUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    private String username;

    @Column(name="password")
    private String encryptedPassword;
}
