package lt.shgg.parsing;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lt.shgg.data.Ticket;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

/**
 * <h1>Класс предоставляющий методы для парсинга</h1>
 * под парсингом подразумевается чтение и запись объектов коллекции в файлы формата XML
 */
public class Parser {
    /**
     * Метод для сохранения коллекции в файл
     * @param file файл для записи
     * @param data коллекция экземпляров класса {@link lt.shgg.data.Ticket}
     * @throws IOException если файл не найден или недоступен
     * @throws AccessDeniedException если программа не может писать в файл
     */
    public static void saveToFile(File file, Collection<Ticket> data) throws IOException{
        if (!Files.isWritable(file.toPath())) throw new
                AccessDeniedException("У программы нет прав, чтоб писать в этот файл");
        XmlMapper mpr = new XmlMapper();
        mpr.enable(SerializationFeature.INDENT_OUTPUT);
        var out = new BufferedWriter(new FileWriter(file));
        mpr.writeValue(out, data);
    }

    /**
     * Метод для чтения коллекции из файла
     * @param file файл для чтения
     * @return список объектов Ticket
     * @throws IOException если файл не найден или недоступен
     * @throws AccessDeniedException если программа не может читать из файла
     */
    public static List<Ticket> getFromFile(File file) throws IOException {
        if (!Files.isReadable(file.toPath())) throw new
                AccessDeniedException("У программы нет прав, чтоб прочитать этот файл");
        XmlMapper mpr = new XmlMapper();
        mpr.enable(SerializationFeature.INDENT_OUTPUT);
        var in = new BufferedReader(new FileReader(file));
        return List.of(mpr.readValue(in, Ticket[].class));
    }
}
