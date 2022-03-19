package de.cofinpro.webquizengine.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO object representing a RegisteredUser creation request body.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestBody {

    @Pattern(regexp="\\w+@\\w+\\.\\w+")
    private String email;
    @Size(min=5)
    private String password;

}
