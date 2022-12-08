package com.binno.dominio.module.usuarioacesso.service;

import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.usuarioacesso.api.dto.CriarPedidoAmizadeDto;
import com.binno.dominio.module.usuarioacesso.model.ConviteAmizade;
import com.binno.dominio.module.usuarioacesso.model.RelacionamentoAmizade;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.ConviteAmizadeRepository;
import com.binno.dominio.module.usuarioacesso.repository.RelacionamentoAmizadeRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AmizadesService {

    private final ConviteAmizadeRepository repository;

    private final UsuarioAcessoRepository usuarioAcessoRepository;

    private final RegistrarNotificacao registrarNotificacao;
    private final RelacionamentoAmizadeRepository relacionamentoAmizadeRepository;

    public void criarPedidoAmizade(CriarPedidoAmizadeDto dto) {
        UsuarioAcesso solicitante = usuarioAcessoRepository.findById(dto.getIdUsuarioSolicitante()).orElseThrow();
        UsuarioAcesso requisitado = usuarioAcessoRepository.findById(dto.getIdUsuarioRequistado()).orElseThrow();

        if (solicitante.getId().equals(requisitado.getId()))
            throw new ValidationException("Não é permitido solicitar amizade para voce mesmo");

        if (verificarSeJaFoiFeitoPedidoAmizadeAnteriormente(solicitante, requisitado)) {
            return;
        }

        repository.save(ConviteAmizade.builder()
                .dataSolicitacao(LocalDate.now())
                .mensagem(dto.getMensagem())
                .usuarioRequisitado(UsuarioAcesso.builder().id(dto.getIdUsuarioRequistado()).build())
                .usuarioSolicitante(UsuarioAcesso.builder().id(dto.getIdUsuarioSolicitante()).build())
                .build());

        registrarNotificacao.executar("Novo pedido de amizade recebido de " + solicitante.getLogin(), requisitado.getTenant().getId());
    }

    public List<UsuarioAcesso> meusAmigos() {
        UsuarioAcesso usuarioAcesso = usuarioAcessoRepository.findById(2).orElseThrow();
        return usuarioAcesso.getAmigos()
                .stream()
                .map(RelacionamentoAmizade::getFriend)
                .map(UsuarioAcesso::getId)
                .map(id -> usuarioAcessoRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<ConviteAmizade> meusConvites(Integer idUsuario) {
        return repository.findAllByUsuarioSolicitanteId(idUsuario)
                .stream()
                .filter(conviteAmizade -> !conviteAmizade.getAceito())
                .collect(Collectors.toList());
    }

    public void aceitarPedidoAmizade(Integer idPedido) {
        ConviteAmizade amizade = repository.findById(idPedido).orElseThrow();
        amizade.setAceito(true);
        repository.save(amizade);

        relacionamentoAmizadeRepository.save(RelacionamentoAmizade.builder()
                .dataCriacao(LocalDate.now())
                .owner(amizade.getUsuarioSolicitante())
                .friend(amizade.getUsuarioRequisitado())
                .build());
    }

    private boolean verificarSeJaFoiFeitoPedidoAmizadeAnteriormente(UsuarioAcesso solicitante, UsuarioAcesso requisitado) {
        return repository.findByUsuarioRequisitadoIdAndUsuarioSolicitanteId(requisitado.getId(), solicitante.getId()).isPresent();
    }
}
