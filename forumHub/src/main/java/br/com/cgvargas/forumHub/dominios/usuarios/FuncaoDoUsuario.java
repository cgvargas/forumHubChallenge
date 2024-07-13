package br.com.cgvargas.forumHub.dominios.usuarios;

public enum FuncaoDoUsuario {
    ADMIN("admin"),
    USER("user");

    private String role;

    FuncaoDoUsuario(String role){
        this.role=role;
    }

    public String getRole(){
        return role;
    }
}
