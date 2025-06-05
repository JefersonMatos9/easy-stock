package com.easystock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Esta classe de configuração Spring é responsável por personalizar
 * a configuração do Spring MVC na nossa aplicação. Ao implementar
 * a interface {@link WebMvcConfigurer}, podemos sobrescrever métodos
 * para adicionar funcionalidades como conversores de tipo e formatadores.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Este método é sobrescrito da interface {@link WebMvcConfigurer}
     * para permitir o registro de formatadores e conversores personalizados.
     * O {@link FormatterRegistry} é uma interface que fornece métodos para
     * adicionar formatadores e conversores.
     *
     * @param registry O registro de formatadores e conversores do Spring MVC.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Aqui, adicionamos o nosso conversor personalizado {@link StringToStatusRequestConverter}
        // ao registro. Isso fará com que o Spring utilize este conversor sempre que
        // precisar converter uma String para o tipo {@link StatusRequest}.
        registry.addConverter(new StringToStatusRequestConverter());
    }
}