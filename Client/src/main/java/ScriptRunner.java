import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class ScriptRunner {
    private final static Set<Path> recursionDefense = new HashSet<>();

    public void runScript(String filename) throws AccessDeniedException, FileNotFoundException {
        var path = Path.of(filename).toAbsolutePath();
        if (!Files.isReadable(path))
            throw new AccessDeniedException("Такого файла не существует или у программы нет прав, чтоб его прочитать");
        if (ScriptRunner.recursionDefense.contains(path))
            throw new IllegalArgumentException("в ваших скриптах обнаружена рекурсия, а рекурсия вредна для здоровья!");
        ScriptRunner.recursionDefense.add(path);
        var fileIn = new FileReader(path.toFile());
        var fileConsole = new ClientConsole();
        var sender = new Sender("localhost", 1488, 3000, 3);
        fileConsole.runApp(fileIn, sender, true);
        System.out.println("достигнут конец файла " + filename);
        ScriptRunner.recursionDefense.clear();
    }
}
