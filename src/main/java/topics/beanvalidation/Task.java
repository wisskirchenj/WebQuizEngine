package topics.beanvalidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

class Task {
    @Size(min = 1, max = 50)
    @NotBlank
    String name;

    @Size(min = 1, max = 200)
    @NotBlank
    String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
