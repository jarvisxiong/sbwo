package spet.sbwo.control.action.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spet.sbwo.control.action.base.BaseDatabaseAction;
import spet.sbwo.control.channel.LocationImportChannel;
import spet.sbwo.data.DatabaseException;
import spet.sbwo.data.access.IDatabaseExecutor;
import spet.sbwo.data.base.ICodifiedEntity;
import spet.sbwo.data.query.WhereOperator;
import spet.sbwo.data.table.LocationAdministrativeUnit;
import spet.sbwo.data.table.LocationCountry;
import spet.sbwo.data.table.LocationRegion;

public class ImportLocationsAction extends BaseDatabaseAction<List<LocationImportChannel>, Void> {

	public ImportLocationsAction() {
		super(LocationImportChannel.class);
	}

	@Override
	public Void doRun(List<LocationImportChannel> input, IDatabaseExecutor executor) throws DatabaseException {
		if (!input.isEmpty()) {
			importLocationData(input, executor);
		}
		return null;
	}

	protected void importLocationData(List<LocationImportChannel> data, IDatabaseExecutor executor)
			throws DatabaseException {
		Collections.sort(data);
		LocationCountry currentCountry = null;
		LocationRegion currentRegion = null;
		Map<String, LocationRegion> currentRegions = null;
		Map<String, LocationAdministrativeUnit> currentUnits = null;

		for (LocationImportChannel line : data) {
			if (currentCountry == null || !line.getCountryCode().equals(currentCountry.getCode())) {
				currentCountry = this.getCountry(executor, line.getCountryCode(), line.getCountryName());
				currentRegions = this.toMap(currentCountry.getRegions());
				currentRegion = null;
			}
			if (currentRegion == null || !line.getRegionCode().equals(currentRegion.getCode())) {
				currentRegion = this.getRegion(executor, currentRegions, currentCountry, line.getRegionCode(),
						line.getRegionName());
				currentUnits = this.toMap(currentRegion.getAdministrativeUnits());
			}
			this.getUnit(executor, currentUnits, currentRegion, line.getAdmUnitCode(), line.getAdmUnitName());
		}
	}

	protected <T, K extends ICodifiedEntity<T>> Map<T, K> toMap(List<K> data) {
		Map<T, K> result = new HashMap<>();
		for (K entity : data) {
			result.put(entity.getCode(), entity);
		}
		return result;
	}

	protected LocationCountry getCountry(IDatabaseExecutor executor, String code, String name)
			throws DatabaseException {
		LocationCountry result;
		result = executor.selectSingle(LocationCountry.class).where("code", WhereOperator.EQ, code).execute();
		if (result == null) {
			result = new LocationCountry();
			result.setCode(code);
			result.setName(name);
			result.setRegions(new ArrayList<>());
			executor.create(result);
		} else {
			result.setName(name);
		}
		return result;
	}

	protected LocationRegion getRegion(IDatabaseExecutor executor, Map<String, LocationRegion> regions,
			LocationCountry country, String code, String name) throws DatabaseException {
		LocationRegion result = regions.get(code);
		if (result == null) {
			result = new LocationRegion();
			result.setCode(code);
			result.setName(name);
			result.setCountry(country);
			result.setAdministrativeUnits(new ArrayList<>());
			country.getRegions().add(result);
			executor.create(result);
			regions.put(code, result);
		} else {
			result.setName(name);
		}
		return result;
	}

	protected LocationAdministrativeUnit getUnit(IDatabaseExecutor executor,
			Map<String, LocationAdministrativeUnit> units, LocationRegion region, String code, String name)
					throws DatabaseException {
		LocationAdministrativeUnit result = units.get(code);
		if (result == null) {
			result = new LocationAdministrativeUnit();
			result.setCode(code);
			result.setName(name);
			result.setRegion(region);
			region.getAdministrativeUnits().add(result);
			executor.create(result);
			units.put(code, result);
		} else {
			result.setName(name);
		}
		return result;
	}

}