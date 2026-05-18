package com.sopro.project_demoday.service;

import com.sopro.project_demoday.dto.PerfilResponseDTO;
import com.sopro.project_demoday.dto.SalvarPerfilDTO;
import com.sopro.project_demoday.model.Endereco;
import com.sopro.project_demoday.model.Pedido;
import com.sopro.project_demoday.model.Usuario;
import com.sopro.project_demoday.repository.PedidoRepository;
import com.sopro.project_demoday.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    // Construtor manual para Injeção de Dependência (substituindo o @RequiredArgsConstructor)
    public PerfilService(UsuarioRepository usuarioRepository, PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }


    public PerfilResponseDTO obterPerfilPorEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        Pedido ultimoPedido = pedidoRepository.findFirstByUsuarioEmailOrderByIdDesc(email)
                .orElse(null);

        // DTO interno do pedido caso ele exista
        PerfilResponseDTO.UltimoPedidoDTO pedidoDTO = null;
        if (ultimoPedido != null) {
            pedidoDTO = new PerfilResponseDTO.UltimoPedidoDTO(
                    ultimoPedido.getCodigoPedido(),
                    ultimoPedido.getProdutoDescricao(),
                    ultimoPedido.getStatusStatus(),
                    ultimoPedido.getCodigoRastreio(),
                    ultimoPedido.getDataEntregaPrevista(),
                    ultimoPedido.getValorTotal()
            );
        }

        //  para definir textualmente o Plano com base na assinatura
        String nomePlano = "Plano Free";
        if (usuario.getAssinatura() != null && usuario.getAssinatura().getStatus() != null) {

            nomePlano = usuario.getAssinatura().getStatus().name();
        }


        String enderecoCompleto = "Endereço não preenchido";
        if (usuario.getEndereco() != null) {
            enderecoCompleto = usuario.getEndereco().getLogradouro() + " - " + usuario.getEndereco().getComplemento();
        }


        return new PerfilResponseDTO(
                usuario.getNomeCompleto(),
                nomePlano,
                usuario.getCidadeEstado(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getTelefoneCelular(),
                usuario.getDataNascimento(),
                enderecoCompleto,
                pedidoDTO
        );
    }


    @Transactional
    public void atualizarDadosPerfil(String email, SalvarPerfilDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNomeCompleto(dto.nomeCompleto());
        usuario.setCpf(dto.cpf());
        usuario.setTelefoneCelular(dto.telefoneCelular());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setCidadeEstado(dto.cidadeEstado());


        Endereco endereco = usuario.getEndereco();
        if (endereco == null) {
            endereco = new Endereco();
        }

        endereco.setLogradouro(dto.logradouro());
        endereco.setComplemento(dto.complemento());


        usuario.setEndereco(endereco);

        // Salva o usuário no banco. o CascadeType.ALL configurado na Entidade Usuario salvará o endereço junto
        usuarioRepository.save(usuario);
    }
}