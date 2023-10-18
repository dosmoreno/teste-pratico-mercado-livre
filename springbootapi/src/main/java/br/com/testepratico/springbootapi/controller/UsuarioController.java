package br.com.testepratico.springbootapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.testepratico.springbootapi.entity.Usuario;
import br.com.testepratico.springbootapi.repository.UsuarioRepository;

import static java.util.Objects.nonNull;

import java.lang.reflect.Field;
import java.math.BigInteger;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    private final String erroCPF = "O CPF já existe no sistema\n"; 
    private final String erroEmail = "O e-mail já existe no sistema\n";

    @GetMapping("/todos")
    public ResponseEntity<?> buscarPorTodos() {
            try {
                return new ResponseEntity<List<Usuario>>(usuarioRepository.findAll(),
                    HttpStatus.OK);
            }catch (Exception ee) {
                return ResponseEntity.badRequest().body(ee.getMessage());
            }
        }

    @GetMapping()
    public ResponseEntity<?> buscar(@RequestParam (required=false) BigInteger id, 
        @RequestParam(required=false) String nome) {
        
        try {

            if (id != null) {
                if (nome != null) {
                    return ResponseEntity.badRequest().body("Parâmetros errados");
                } else {
                    Optional<Usuario> usuario = usuarioRepository.findById(id);
                    if(usuario.isPresent())
                        return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
                    else
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);   
                }
            }else{
                if (nome != null) {
                    List<Usuario> listaUsuarios = usuarioRepository.findByNome(nome);
                    if(listaUsuarios.isEmpty())
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    else
                        return new ResponseEntity<List<Usuario>>(
                            listaUsuarios, HttpStatus.OK);
                } else 
                    return ResponseEntity.badRequest().body("Parâmetros errados");
            }

        }catch (Exception ee) {
            return ResponseEntity.badRequest().body(ee.getMessage());
        }
        
    }

    @PostMapping()
    public ResponseEntity<?> criar(@Valid @RequestBody Usuario usuario) {
        try {

            String erros = "";

            if ( nonNull( usuarioRepository.findTop1ByCpf(usuario.getCpf()))) {
                erros += erroCPF;
            }
            if ( nonNull( usuarioRepository.findTop1ByEmail( usuario.getEmail()))) {
                erros += erroEmail;
            }

            if (erros.equals("")) {
                usuarioRepository.save(usuario);    
                return new ResponseEntity<Usuario>(usuario, HttpStatus.CREATED);
            } else {
                return ResponseEntity.badRequest().body(erros);
            }
        }catch (Exception ee) {
            return ResponseEntity.badRequest().body(ee.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable(value = "id") BigInteger id, @RequestBody Map<Object, Object> campos)
    {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if(usuario.isPresent()){

            String erros = "";

            campos.forEach((key, value) -> {
                if (((String)key).equals("cpf") && nonNull( usuarioRepository.findTop1ByCpf((String)value))) {
                    value = "CPF com erro";
                }
                if (((String)key).equals("email") && nonNull( usuarioRepository.findTop1ByEmail((String)value))) {
                    value = "E-mail com erro";
                }
                Field field = ReflectionUtils.findField(Usuario.class, (String)key);
                if(field != null)
                    field.setAccessible(true);
                ReflectionUtils.setField(field, usuario.get(), value);
            });

            if (usuario.get().getCpf().equals("CPF com erro"))
                erros += erroCPF;

            if (usuario.get().getEmail().equals("E-mail com erro"))
                erros += erroEmail;            

            if (erros.equals("")) {
                usuarioRepository.save(usuario.get());
                return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body(erros);
            }

        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
