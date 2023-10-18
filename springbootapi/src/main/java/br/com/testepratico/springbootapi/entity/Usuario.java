package br.com.testepratico.springbootapi.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private BigInteger id;

	@NotBlank(message = "Nome não deve estar em branco")
    @Size(min = 3,max = 50,message = "O nome precisa ter entre 3 a 50 caracteres")
    @Column(length = 50)
    private String nome;

	@CPF(message = "Número do registro de contribuinte individual brasileiro (CPF) inválido")
	@NotBlank(message = "CPF não deve estar em branco")
    private String cpf;

	@Email(message = "Deve ser um endereço de e-mail bem formado")
	@NotBlank(message = "E-mail não deve estar em branco")
    private String email;

    @NotNull(message = "Idade não deve estar em branco")
    @Min(value = 18, message = "Idade não pode ser menor que 18")
    @Max(value = 150, message = "Idade não pode ser maior que 150")
    private int idade;

}
