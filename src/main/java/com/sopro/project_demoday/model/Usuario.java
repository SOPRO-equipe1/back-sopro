package com.sopro.project_demoday.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.sopro.project_demoday.model.enums.RoleUsuario;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String nome;
    
    @Column(name = "nome_completo")
    private String nomeCompleto;
    
    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank
    @Column(nullable = false)
    private String senha;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleUsuario role;
    
    @Column(length = 14, unique = true)
    private String cpf;
    
    @Column(name = "telefone_celular", length = 15)
    private String telefoneCelular;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "cidade_estado")
    private String cidadeEstado;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
    
    public Usuario() {
    }
    
    public Usuario(String nome, String email, String senha, RoleUsuario role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role != null ? role : RoleUsuario.USUARIO;
        this.ativo = true;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public RoleUsuario getRole() {
        return role;
    }
    
    public void setRole(RoleUsuario role) {
        this.role = role;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public String getNomeCompleto() {
        return nomeCompleto;
    }
    
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getTelefoneCelular() {
        return telefoneCelular;
    }
    
    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public String getCidadeEstado() {
        return cidadeEstado;
    }
    
    public void setCidadeEstado(String cidadeEstado) {
        this.cidadeEstado = cidadeEstado;
    }
    
    public Endereco getEndereco() {
        return endereco;
    }
    
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
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
        return ativo;
    }
}
