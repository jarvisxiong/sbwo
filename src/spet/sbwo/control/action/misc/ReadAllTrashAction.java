package spet.sbwo.control.action.misc;

import java.util.List;
import java.util.stream.Collectors;

import spet.sbwo.control.ControlException;
import spet.sbwo.control.action.base.BaseDatabaseAction;
import spet.sbwo.control.channel.TrashChannel;
import spet.sbwo.data.DatabaseException;
import spet.sbwo.data.access.IDatabaseExecutor;
import spet.sbwo.data.view.DeletedEntity;

public class ReadAllTrashAction extends BaseDatabaseAction<Void, List<TrashChannel>> {

	public ReadAllTrashAction() {
		super(TrashChannel.class);
	}

	@Override
	public List<TrashChannel> doRun(Void input, IDatabaseExecutor executor) throws ControlException, DatabaseException {
		List<DeletedEntity> entities = executor.select(DeletedEntity.class).execute();
		return entities.stream().map(entity -> {
			TrashChannel channel = new TrashChannel();
			channel.setId(entity.getKey().getId());
			channel.setType(entity.getKey().getType().ordinal());
			return channel;
		}).collect(Collectors.toList());
	}

}