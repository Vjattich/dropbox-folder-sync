package components;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public class FolderComponent {

    private final String folderPath;
    private final Map<WatchEvent.Kind<Path>, Function<Path, Path>> eventsMap;
    private volatile boolean stopped = false;

    public FolderComponent(String folderPath, Map<WatchEvent.Kind<Path>, Function<Path, Path>> eventsMap) {
        isFolder(folderPath);
        this.folderPath = folderPath;
        this.eventsMap = eventsMap;
    }

    public void watch() {

        System.out.println("Watching path: " + folderPath);

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

    public List<File> getFiles() {
        return new ArrayList<>(Arrays.asList(Paths.get(folderPath).toFile().listFiles()));
    }

    public void stopWatch() {
        this.stopped = true;
    }

    public void resumeWatch() {
        this.stopped = false;
        this.watch();
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
