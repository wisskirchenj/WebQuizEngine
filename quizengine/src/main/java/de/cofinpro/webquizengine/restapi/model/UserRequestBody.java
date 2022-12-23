package de.cofinpro.webquizengine.restapi.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO object representing a RegisteredUser creation request body.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestBody {

    @Pattern(regexp="\\w+@\\w+\\.\\w+")
    @NotNull
    private String email;
    @NotNull
    @Size(min=5)
    private String password;

}
