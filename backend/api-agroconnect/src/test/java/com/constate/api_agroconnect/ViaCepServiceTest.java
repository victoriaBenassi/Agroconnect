//package com.constate.api_agroconnect;
//
//import com.constate.api_agroconnect.model.Endereco;
//import com.constate.api_agroconnect.service.ViaCepService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.*;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class ViaCepServiceTest {
//
//    @InjectMocks
//    private EnderecoService enderecoService;
//
//    @Mock
//    private ViaCepService viaCepService;
//
//    @Mock
//    private EnderecoRepository enderecoRepository;
//
//    @Mock
//    private UsuarioRepository usuarioRepository;
//
//    private Usuario usuario;
//    private Endereco endereco;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        usuario = new Usuario();
//        usuario.setId_usuario(1);
//        usuario.setNome("Teste");
//        usuario.setSobrenome("Usuario");
//        usuario.setEmail("teste@usuario.com");
//        usuario.setSenha("senha");
//
//        endereco = new Endereco();
//        endereco.setCep("06341420");
//        endereco.setLogradouro("Rua Teste");
//        endereco.setBairro("Bairro Teste");
//        endereco.setLocalidade("Cidade Teste");
//    }
////
////
////    @Test
////    public void testAdicionarEnderecoPorCep() {
////
////        // Simular o comportamento do serviço ViaCep
////        when(viaCepService.buscarEnderecoPorCep("06341420")).thenReturn(endereco);
////        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
////
////        // Chamar o método a ser testado
////        ResponseEntity<Endereco> response = enderecoService.adicionarEnderecoPorCep("06341420", 1);
////
////        // Verificações
////        assertEquals(200, response.getStatusCodeValue());
////        assertNotNull(response.getBody());
////        assertEquals("06341420", response.getBody().getCep());
////        verify(enderecoRepository).save(endereco); // Verifica se o endereço foi salvo
////        verify(usuarioRepository).save(usuario); // Verifica se o usuário foi atualizado
////    }
//}
