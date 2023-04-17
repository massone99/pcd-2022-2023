package pcd.lab06.executors.forkjoin;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Long> {
    private final Folder folder;
    private final String searchedWord;
    private final WordCounter wc;
    
    public FolderSearchTask(WordCounter wc, Folder folder, String searchedWord) {
        super();
        this.wc = wc;
        this.folder = folder;
        this.searchedWord = searchedWord;
    }
    
    @Override
    protected Long compute() {
        long count = 0L;
        List<RecursiveTask<Long>> forks = new LinkedList<RecursiveTask<Long>>();
        for (Folder subFolder : folder.getSubFolders()) {
            // genero ricorsivamente un task per ogni subfolder
            FolderSearchTask task = new FolderSearchTask(wc, subFolder, searchedWord);
            forks.add(task);
            // Tramite .fork() il task viene dato in pasto alla stessa pool di thread che sta eseguendo il task corrente
            task.fork();
        }
        
        for (Document document : folder.getDocuments()) {
            // per ogni documento genero un task adatto ad analizzare il documento
            DocumentSearchTask task = new DocumentSearchTask(wc, document, searchedWord);
            forks.add(task);
            // Tramite .fork() il task viene dato in pasto alla stessa pool di thread che sta eseguendo il task corrente
            task.fork();
        }

        // Dopo averli generati tutti devo fare una reduce
        for (RecursiveTask<Long> task : forks) {
            // La join è bloccante, aspetta che il task sia terminato e poi ritorna il risultato
            // Se il Thread è bloccato in una join, gli altri Thread continuano a svolgere i Task senza causare Deadlock
            count = count + task.join();
        }
        return count;
    }
}
    