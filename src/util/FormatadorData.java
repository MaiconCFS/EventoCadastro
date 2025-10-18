package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormatadorData {
    
    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public static final DateTimeFormatter FORMATO_ARQUIVO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public static LocalDate parseData(String dataStr) {
        try {
            return LocalDate.parse(dataStr, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data invalida. Use o formato DD/MM/AAAA");
        }
    }
    
    public static LocalDateTime parseDataHora(String dataHoraStr) {
        try {
            return LocalDateTime.parse(dataHoraStr, FORMATO_DATA_HORA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data/hora invalida. Use o formato DD/MM/AAAA HH:MM");
        }
    }
    
    public static String formatarData(LocalDate data) {
        return data.format(FORMATO_DATA);
    }
    
    public static String formatarDataHora(LocalDateTime dataHora) {
        return dataHora.format(FORMATO_DATA_HORA);
    }
    
    public static String formatarParaArquivo(LocalDateTime dataHora) {
        return dataHora.format(FORMATO_ARQUIVO);
    }
}