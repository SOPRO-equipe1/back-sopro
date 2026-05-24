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

    // Construtor manual para Injeção de Dependência
    public PerfilService(UsuarioRepository usuarioRepository, PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public PerfilResponseDTO obterPerfilPorEmail(String email) {
        // 1. Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Busca o último pedido realizado por este usuário
        Pedido ultimoPedido = pedidoRepository.findFirstByUsuarioEmailOrderByIdDesc(email)
                .orElse(null);

        // Estrutura o DTO interno do pedido caso ele exista
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


        String nomePlano = "Plano Free";
        if (usuario.getAssinatura() != null && usuario.getAssinatura().getStatus() != null) {

            if ("ATIVO".equalsIgnoreCase(usuario.getAssinatura().getStatus().toString())) {
                nomePlano = "Plano Premium";
            }
        }


        String addressText = "Endereço não preenchido";
        if (usuario.getEndereco() != null) {
            String logradouro = usuario.getEndereco().getLogradouro() != null ? usuario.getEndereco().getLogradouro() : "";
            String complemento = usuario.getEndereco().getComplemento() != null ? usuario.getEndereco().getComplemento() : "";

            if (!logradouro.isEmpty()) {
                addressText = logradouro + (complemento.isEmpty() ? "" : " - " + complemento);
            }
        }

        // Retorna o DTO do Perfil montado para o Frontend React
        return new PerfilResponseDTO(
                usuario.getNomeCompleto(),
                nomePlano,
                usuario.getCidadeEstado(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getTelefoneCelular(),
                usuario.getDataNascimento(),
                addressText,
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
        usuarioRepository.save(usuario);
    }
}