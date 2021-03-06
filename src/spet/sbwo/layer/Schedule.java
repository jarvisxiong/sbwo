package spet.sbwo.layer;

import java.io.File;

import org.picocontainer.MutablePicoContainer;

import spet.sbwo.control.config.Configuration;
import spet.sbwo.control.controller.user.CachedSessionManager;
import spet.sbwo.control.scheduler.ScheduleBuilder;
import spet.sbwo.control.scheduler.SchedulerType;
import spet.sbwo.data.access.DatabaseFacade;

public class Schedule {

	public Schedule(MutablePicoContainer container, Configuration configuration) {
		ScheduleBuilder scheduleBuilder = new ScheduleBuilder().threads(configuration.getSchedulerThreads());
		scheduleBuilder.backup().backuper(container.getComponent(DatabaseFacade.class))
				.directory(configuration.getDatabaseBackupLocation())
				.intervalDays(configuration.getDatabaseBackupInterval())
				.delayMillis(configuration.getDatabaseBackupStart());
		scheduleBuilder.cleanup().delayMillis(configuration.getCleanupStart())
				.maxAgeDays(configuration.getCleanupThreshold())
				.addBackupBased(configuration.getDatabaseBackupLocation())
				.addPatternBased(new File("logs"), "yyyyMMdd", "log_(\\d{8})_\\d+");
		scheduleBuilder.simple().intervalMins(configuration.getSessionFlushInterval()).type(SchedulerType.SESSION_CACHE)
				.runnable(container.getComponent(CachedSessionManager.class)::flush);
		container.addComponent(scheduleBuilder.build());
	}

}
