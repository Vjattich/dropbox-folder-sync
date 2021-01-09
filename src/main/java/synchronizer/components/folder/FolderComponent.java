package synchronizer.components.folder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

@Slf4j
public class FolderComponent {

    private final String folderPath;
    private final Map<WatchEvent.Kind<Path>, Function<Path, Path>> eventsMap;
    private volatile boolean stopped = false;

    public FolderComponent(String folderPath, Map<WatchEvent.Kind<Path>, Function<Path, Path>> eventsMap) {
        isFolder(folderPath);
        this.folderPath = folderPath;
        this.eventsMap = eventsMap;
    }

    @SneakyThrows
    public void save(String fileName, Date clientModifiedDate, ByteArrayOutputStream byteArrayOutputStream) {
        String filePath = folderPath + File.separator + fileName;
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            byteArrayOutputStream.writeTo(outputStream);
            new File(filePath).setLastModified(clientModifiedDate.getTime());
        }
    }

    public void stopWatch() {
        this.stopped = true;
    }

    public void startWatch() {
        this.stopped = false;
        this.watch();
    }

    public List<File> getFiles() {
        return new ArrayList<>(Arrays.asList(Paths.get(folderPath).toFile().listFiles()));
    }

    //todo test watch method
    private void watch() {

        log.info("Watching path: {}", folderPath);

        Path path = Paths.get(folderPath);

        try (WatchService service = FileSystems.getDefault().newWatchService()) {

            path.register(service, eventsMap.keySet().toArray(WatchEvent.Kind<?>[]::new));

            WatchKey key;
            while (Objects.nonNull(key = service.take())) {

                for (WatchEvent<?> event : key.pollEvents()) {

                    WatchEvent.Kind<?> kind = event.kind();

                    if (StandardWatchEventKinds.OVERFLOW == kind) continue; // loop

                    Function<Path, Path> watchEventPathFunction = eventsMap.get(kind);

                    Path context = (Path) event.context();

                    watchEventPathFunction.apply(
                            Paths.get(folderPath + File.separator + context.toString())
                    );

                }
                key.reset();

                if (stopped) {
                    break;
                }

            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }

    private void isFolder(String folderPath) {
        try {

            Boolean isFolder = (Boolean) Files.getAttribute(Paths.get(folderPath), "basic:isDirectory", NOFOLLOW_LINKS);

            if (!isFolder) {
                throw new IllegalArgumentException("Path: " + folderPath + " is not a folder");
            }

        } catch (IOException ioe) {
            throw new IllegalArgumentException("Something wrong with folder argument", ioe);
        }
    }

}
