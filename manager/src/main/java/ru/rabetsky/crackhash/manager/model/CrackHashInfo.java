package ru.rabetsky.crackhash.manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.rabetsky.crackhash.model.CrackHashWorkerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrackHashInfo {

    private Status status;
    private Long timeout;
    private Integer totalWorkerCount;
    private Integer completedWorkerCount;
    private ScheduledExecutorService scheduler;
    @Builder.Default
    private List<String> data = new ArrayList<>();

    public void startCountdown() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            status = Status.ERROR;
        }, timeout, TimeUnit.MINUTES);
    }

    public void workerFinished(CrackHashWorkerResponse.Answers answers) {
        completedWorkerCount++;

        if (answers != null) {
            data.addAll(answers.getWords());
        }

        if (completedWorkerCount.equals(totalWorkerCount) && status != Status.ERROR) {
            status = Status.READY;
            scheduler.shutdown();
        }
    }
}
