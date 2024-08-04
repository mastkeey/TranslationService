package ru.mastkey.translater.mapper;

import jakarta.annotation.Nullable;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.core.convert.converter.Converter;
import ru.mastkey.translater.client.dto.YandexTranslateRequest;
import ru.mastkey.translater.config.MapperConfiguration;
import ru.mastkey.translater.controller.dto.TranslationRequest;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public interface TranslationRequestToYandexTranslateRequestMapper extends Converter<TranslationRequest, YandexTranslateRequest>{
    @BeforeMapping
    default void beforeMapping(@MappingTarget YandexTranslateRequest target,  TranslationRequest source) {
        target.setTexts(List.of(source.getText()));
        target.setTargetLanguageCode(source.getTargetLanguageCode());
        target.setSourceLanguageCode(source.getSourceLanguageCode());
    }

    @Override
    @Nullable
    YandexTranslateRequest convert(TranslationRequest source);
}
