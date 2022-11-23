package com.binno.dominio.module.usuarioacesso.service;

import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.usuarioacesso.api.dto.CriarPedidoAmizadeDto;
import com.binno.dominio.module.usuarioacesso.model.ConviteAmizade;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.ConviteAmizadeRepository;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AmizadesService {

    private final ConviteAmizadeRepository repository;

    private final UsuarioAcessoRepository usuarioAcessoRepository;

    private final RegistrarNotificacao registrarNotificacao;

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

    private boolean verificarSeJaFoiFeitoPedidoAmizadeAnteriormente(UsuarioAcesso solicitante, UsuarioAcesso requisitado) {
        return repository.findByUsuarioRequisitadoIdAndUsuarioSolicitanteId(requisitado.getId(), solicitante.getId()).isPresent();
    }
}
