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

    public PerfilService(UsuarioRepository usuarioRepository, PedidoRepository pedidoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public PerfilResponseDTO obterPerfilPorEmail(String email) {
        //  Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        //  Busca o último pedido realizado por este usuário
        Pedido ultimoPedido = pedidoRepository.findFirstByUsuarioEmailOrderByIdDesc(email)
                .orElse(null);


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
            if ("ATIVO".equalsIgnoreCase(usuario.getAssinatura().getStatus())) {
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

        // Retorna o DTO do Perfil com todos os tipos perfeitamente alinhados
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

    // BOTÃO 1: Salva estritamente as Informações Pessoais do Usuário
    @Transactional
    public void atualizarDadosPessoais(String email, SalvarPerfilDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNomeCompleto(dto.nomeCompleto());
        usuario.setCpf(dto.cpf());
        usuario.setTelefoneCelular(dto.telefoneCelular());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setCidadeEstado(dto.cidadeEstado());

        usuarioRepository.save(usuario);
    }


    @Transactional
    public void atualizarEndereco(String email, SalvarPerfilDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Endereco endereco = usuario.getEndereco();
        if (endereco == null) {
            endereco = new Endereco();
        }


        endereco.setCep(dto.cep() != null ? dto.cep() : "00000-000");
        endereco.setNumero(dto.numero() != null ? dto.numero() : "S/N");
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro() != null ? dto.bairro() : "Bairro");
        endereco.setCidade(dto.cidade() != null ? dto.cidade() : "Cidade");
        endereco.setEstado(dto.estado() != null ? dto.estado() : "SP");

        usuario.setEndereco(endereco);
        usuarioRepository.save(usuario);
    }
}