package com.gsmc.png.service.interfaces;

import java.util.concurrent.Future;

public interface PtDataService {

    void execute(Thread ptBetUpdateThread);

    Future<?> executeOtherTask(Runnable runnable);
}
