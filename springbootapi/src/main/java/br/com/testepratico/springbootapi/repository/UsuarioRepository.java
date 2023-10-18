package br.com.testepratico.springbootapi.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.testepratico.springbootapi.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, BigInteger> {

    public Usuario findTop1ByCpf(String cpf);

    public Usuario findTop1ByEmail(String email);
    
    public List<Usuario> findByNome(String nome); 

}