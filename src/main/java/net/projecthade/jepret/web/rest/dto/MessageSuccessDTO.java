package net.projecthade.jepret.web.rest.dto;

public class MessageSuccessDTO {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MessageSuccessDTO() {
    }

    public MessageSuccessDTO(String name) {
        this.name = name;
    }
}
