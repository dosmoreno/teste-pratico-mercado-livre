package br.com.testepratico.springbootapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.testepratico.springbootapi.entity.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testeCriarSucesso(){
        Usuario usuario = new Usuario();
        usuario.setCpf("287.418.090-41");
        usuario.setEmail("luiz@gmail.com");
        usuario.setIdade(20);
        usuario.setNome("Luiz");

    
        ResponseEntity<String> responseEntity = 
            restTemplate.postForEntity("/api/usuario", usuario, String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void testeCriarBadRequest(){
        Usuario usuario = new Usuario();
        usuario.setCpf("787.907.760-03");
        usuario.setEmail("marcelo@gmail.com");
        usuario.setIdade(20);
        usuario.setNome("Marcelo");

    
        ResponseEntity<String> responseEntity = 
            restTemplate.postForEntity("/api/usuario", usuario, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    public void testeBuscarPorTodos(){
    
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/usuario/todos", String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testeBuscarPorIdSucesso(){
    
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/usuario?id={id}", String.class, Map.of("id", "1"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testeBuscarPorIdErro(){
    
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/usuario?id={id}", String.class, Map.of("id", "4"));

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testeBuscarPorNomeSucesso(){
    
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/usuario?nome={nome}", String.class, Map.of("nome", "Daniel"));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testeBuscarPorNomeErro(){
    
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/usuario?nome={nome}", String.class, Map.of("nome", "João"));

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testeBuscarParametrosErrados(){
    
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("/api/usuario", String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testeAtualizarSucesso() throws Exception{

        String requestJson = "{\"email\": \"danielmoreno@gmail.com\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/usuario/1",HttpMethod.PATCH,new HttpEntity<String>(requestJson, headers), String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testeAtualizarBadRequest(){
    
        String requestJson = "{\"email\": \"renata@gmail.com\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/usuario/1",HttpMethod.PATCH,new HttpEntity<String>(requestJson, headers), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testeAtualizarNotFound(){
    
        String requestJson = "{\"email\": \"renata@gmail.com\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        ResponseEntity<String> responseEntity = restTemplate.exchange("/api/usuario/4",HttpMethod.PATCH,new HttpEntity<String>(requestJson, headers), String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
