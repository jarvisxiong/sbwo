package spet.sbwo.control.action.bo.base;

import java.sql.Timestamp;

import spet.sbwo.control.ControlError;
import spet.sbwo.control.ControlException;
import spet.sbwo.control.action.base.BaseUserDatabaseAction;
import spet.sbwo.data.DatabaseException;
import spet.sbwo.data.access.IDatabaseExecutor;
import spet.sbwo.data.base.JournalizedBaseEntity;
import spet.sbwo.data.table.User;

public abstract class BaseUserBoAction<T extends JournalizedBaseEntity, I, O> extends BaseUserDatabaseAction<I, O> {
	protected final Class<T> entity;

	protected BaseUserBoAction(Class<T> entity, Class<?> channel, boolean mandatory) {
		super(channel, mandatory);
		this.entity = entity;
	}

	@Override
	public O doRun(I input, IDatabaseExecutor executor, User user) throws ControlException, DatabaseException {
		T t = executor.find(entity, keyFromInput(input));
		if (t != null) {
			return doRun(input, t, executor, user);
		} else {
			throw new ControlException(ControlError.ENTITY_NOT_FOUND, channel);
		}
	}

	protected abstract Integer keyFromInput(I input);

	protected abstract O doRun(I input, T t, IDatabaseExecutor executor, User user)
			throws ControlException, DatabaseException;

	protected void changed(User user, T t) {
		t.setChangedBy(user);
		t.setChangedOn(new Timestamp(System.currentTimeMillis()));
	}
}
