package com.easystock.config;

import com.easystock.model.StatusRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Este componente Spring implementa a interface {@link Converter}
 * para permitir a conversão de um valor do tipo {@link String}
 * para o tipo {@link StatusRequest}. Isso é útil para converter
 * os valores enviados de um formulário HTML (que são sempre Strings)
 * para o enum {@link StatusRequest} que é usado na nossa entidade {@code Order}.
 */
@Component
public class StringToStatusRequestConverter implements Converter<String, StatusRequest> {

    /**
     * Este método é responsável por realizar a conversão da String de origem
     * para o enum {@link StatusRequest}.
     *
     * @param source A String a ser convertida. Geralmente, este será o valor
     * de um parâmetro de requisição HTTP (por exemplo, o valor
     * selecionado em um `<select>`).
     * @return O valor correspondente do enum {@link StatusRequest}. Retorna {@code null}
     * se a String de origem for nula ou vazia.
     * @throws IllegalArgumentException Se a String de origem não corresponder a
     * nenhum dos valores definidos no enum
     * {@link StatusRequest} (após ser convertida
     * para maiúsculas).
     */
    @Override
    public StatusRequest convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        // Convertemos a String de origem para maiúsculas para corresponder
        // aos nomes das constantes do enum {@link StatusRequest}. O método
        // {@code StatusRequest.valueOf()} lançará um {@code IllegalArgumentException}
        // se a String não corresponder a nenhum valor do enum.
        return StatusRequest.valueOf(source.toUpperCase());
    }
}