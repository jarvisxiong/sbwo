package spet.sbwo.control.controller.bo;

import spet.sbwo.control.ControlException;
import spet.sbwo.control.action.base.BaseActionExecutor;
import spet.sbwo.control.action.bo.base.CreateEntity;
import spet.sbwo.control.action.bo.base.DeleteEntity;
import spet.sbwo.control.action.bo.base.ReadEntity;
import spet.sbwo.control.action.bo.base.RestoreEntity;
import spet.sbwo.control.action.bo.base.UpdateEntity;
import spet.sbwo.control.channel.JournalChannel;
import spet.sbwo.data.access.IDatabaseExecutorCreator;
import spet.sbwo.data.base.JournalizedBaseEntity;

abstract class BaseBoController<E extends JournalizedBaseEntity, C extends JournalChannel>
		extends BaseActionExecutor implements IBoController<C> {
	protected final int directDeleteInterval;

	public BaseBoController(IDatabaseExecutorCreator database, int directDeleteInterval) {
		super(database);
		this.directDeleteInterval = directDeleteInterval;
	}

	@Override
	public int create(C data, String username) throws ControlException {
		return executeAndCommit(username, createAction(), data);
	}

	@Override
	public C read(int id) throws ControlException {
		return execute(readAction(), id);
	}

	@Override
	public void update(int id, C data, String username) throws ControlException {
		data.setId(id);
		executeAndCommit(username, updateAction(), data);
	}

	@Override
	public void delete(int id, String username) throws ControlException {
		executeAndCommit(username, deleteAction(directDeleteInterval), id);
	}

	@Override
	public void restore(int id, String username) throws ControlException {
		executeAndCommit(username, restoreAction(), id);
	}

	protected abstract CreateEntity<E, C> createAction();

	protected abstract ReadEntity<E, C> readAction();

	protected abstract UpdateEntity<E, C> updateAction();

	protected abstract DeleteEntity<E> deleteAction(int directDeleteInterval);

	protected abstract RestoreEntity<E> restoreAction();

}
