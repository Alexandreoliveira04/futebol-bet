package br.com.futebolbet.service;

import br.com.futebolbet.enums.TipoResultado;
import br.com.futebolbet.models.Aposta;
import br.com.futebolbet.models.Partida;
import br.com.futebolbet.models.Participante;
import br.com.futebolbet.models.Resultado;
import br.com.futebolbet.repository.ApostaRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApostaService {

    private ApostaRepository apostaRepository;

    public ApostaService() {
        this.apostaRepository = ApostaRepository.getInstance();
    }

    public static void validarPlacarCoerenteComResultado(
            TipoResultado tipo, int golsCasa, int golsFora) throws Exception {
        if (tipo == null) {
            return;
        }
        switch (tipo) {
            case VITORIA_CASA:
                if (golsCasa <= golsFora) {
                    throw new Exception(
                            "Para 'Vitória do mandante', o placar deve favorecer o time da casa (mais gols que o visitante).");
                }
                break;
            case VITORIA_FORA:
                if (golsFora <= golsCasa) {
                    throw new Exception(
                            "Para 'Vitória do visitante', o placar deve favorecer o time de fora (mais gols que o mandante).");
                }
                break;
            case EMPATE:
                if (golsCasa != golsFora) {
                    throw new Exception("Para 'Empate', os gols do mandante e do visitante devem ser iguais.");
                }
                break;
            default:
                break;
        }
    }

    public void registrarAposta(Aposta aposta) throws Exception {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime horaPartida = aposta.getPartida().getDataHora();

        long minutosAtePartida = ChronoUnit.MINUTES.between(agora, horaPartida);

        if (minutosAtePartida < 20) {
            throw new Exception("Apostas só podem ser realizadas até 20 minutos antes da partida.");
        }

        validarPlacarCoerenteComResultado(
                aposta.getResultadoEsperado(),
                aposta.getGolsCasaEsperado().intValue(),
                aposta.getGolsForaEsperado().intValue());

        apostaRepository.adicionar(aposta);
    }

    public void processarResultadosPartida(Partida partida) {
        Resultado resultadoReal = partida.getResultado();
        if (resultadoReal == null) {
            return;
        }

        List<Aposta> apostasDaPartida = apostaRepository.obterPorPartida(partida);

        for (Aposta aposta : apostasDaPartida) {
            calcularPontuacao(aposta, resultadoReal);
        }

        Set<Participante> participantesAfetados = new HashSet<>();
        for (Aposta aposta : apostasDaPartida) {
            participantesAfetados.add(aposta.getParticipante());
        }
        for (Participante p : participantesAfetados) {
            sincronizarPontosDoParticipante(p);
        }
    }

    public void sincronizarPontosDoParticipante(Participante participante) {
        int total = 0;
        for (Aposta a : apostaRepository.obterPorParticipante(participante)) {
            total += a.getPontos() != null ? a.getPontos() : 0;
        }
        participante.definirPontos(total);
    }

    private void calcularPontuacao(Aposta aposta, Resultado resultadoReal) {
        Integer golsCasaReal = resultadoReal.getGolsCasa();
        Integer golsForaReal = resultadoReal.getGolsFora();
        TipoResultado tipoResultadoReal = determinarTipoResultado(golsCasaReal, golsForaReal);

        if (golsCasaReal.equals(aposta.getGolsCasaEsperado()) && golsForaReal.equals(aposta.getGolsForaEsperado())) {
            aposta.setPontos(10);
        } else if (tipoResultadoReal == aposta.getResultadoEsperado()) {
            aposta.setPontos(5);
        } else {
            aposta.setPontos(0);
        }
    }

    private TipoResultado determinarTipoResultado(int golsCasa, int golsFora) {
        if (golsCasa > golsFora) return TipoResultado.VITORIA_CASA;
        if (golsCasa < golsFora) return TipoResultado.VITORIA_FORA;
        return TipoResultado.EMPATE;
    }
}



