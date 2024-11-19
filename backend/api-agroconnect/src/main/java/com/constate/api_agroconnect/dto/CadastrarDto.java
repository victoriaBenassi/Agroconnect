package com.constate.api_agroconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastrarDto(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Sobrenome é obrigatório") String sobrenome,
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail deve ser válido") String email,
        @NotBlank(message = "Senha é obrigatória") String senha) { }
