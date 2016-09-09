package com.nsb.enms.restful.adapter.server.manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.action.method.ne.DeleteNe;
import com.nsb.enms.restful.adapter.server.common.ExternalScriptType;
import com.nsb.enms.restful.adapter.server.common.Pair;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.restful.db.client.ApiException;
import com.nsb.enms.restful.db.client.api.NesApi;

public class Q3EmlImMgr {
	private static final Logger log = LogManager.getLogger(Q3EmlImMgr.class);

	private static Map<Integer, List<Integer>> groupToNeId = new LinkedHashMap<Integer, List<Integer>>();

	private static Q3EmlImMgr q3EmlImMgr = new Q3EmlImMgr();

	private static final int MAX_NE_COUNT = 200;

	private static final int EMLIM_COUNT = 4;

	private ReadWriteLock rwLock = new ReentrantReadWriteLock();

	private List<Integer> diedEmlImGroupIdList = new ArrayList<Integer>();

	private NesApi nesApi = new NesApi();

	private Q3EmlImMgr() {

	}

	public static Q3EmlImMgr getInstance() {
		return Q3EmlImMgr.q3EmlImMgr;
	}

	public void init(int groupId) throws AdapterException {
		for (int i = 0; i < EMLIM_COUNT; i++) {
			if (!groupToNeId.containsKey(groupId)) {
				startEmlIm(groupId);
				groupId++;
			}
		}

		Timer timer = new Timer();
		long period = ConfLoader.getInstance().getInt(ConfigKey.EMLIM_MONITOR_INTERVAL,
				ConfigKey.DEFAULT_EMLIM_MONITOR_INTERVAL);
		timer.scheduleAtFixedRate(new Q3EmlImMonitorTask(), period, period);
	}

	public void startEmlIm(int groupId) throws AdapterException {
		try {
			Process process = new ExecExternalScript().run(ExternalScriptType.EMLIM, groupId + "");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {

			}
			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Start emlim failed!!!");
			}

			br.close();
			groupToNeId.put(groupId, new ArrayList<Integer>());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	public Set<Integer> getGroupIdList() {
		return groupToNeId.keySet();
	}

	public synchronized Pair<Integer, Integer> getGroupNeId() throws AdapterException {
		int neId = 1;

		// remove later
		if (groupToNeId.keySet().size() == 0) {
			groupToNeId.put(100, new ArrayList<Integer>());
		}
		for (int groupId : groupToNeId.keySet()) {
			List<Integer> neIds = groupToNeId.get(groupId);
			if (neIds.size() < MAX_NE_COUNT) {
				if (!neIds.isEmpty()) {
					Collections.sort(neIds);
					neId = neIds.get(neIds.size() - 1) + 1;
				} else {
					neId = getMaxNeIdFromDb() + 1;
				}
				updateMaxNeId2Db(neId);
				neIds.add(neId);
				return new Pair<Integer, Integer>(groupId, neId);
			}
		}

		if (groupToNeId.keySet().size() < EMLIM_COUNT) {
			for (int groupId : diedEmlImGroupIdList) {
				if (!groupToNeId.keySet().contains(groupId)) {
					startEmlIm(groupId);
					diedEmlImGroupIdList.remove(groupId);
					List<Integer> neIds = groupToNeId.get(groupId);
					neIds.add(neId);
					return new Pair<Integer, Integer>(groupId, neId);
				}
			}
		}
		throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR,
				"There isn't any EMLIM has capacity to manager NE!!!");
	}

	private void updateMaxNeId2Db(int neId) {
		try {
			nesApi.updateMaxNeId(String.valueOf(neId));
		} catch (ApiException e) {
			log.error("updateMaxNeId2Db", e);
		}
	}

	private int getMaxNeIdFromDb() {
		String maxNeId = StringUtils.EMPTY;
		try {
			maxNeId = nesApi.getMaxNeId();
		} catch (ApiException e) {
			log.error("getMaxNeIdFromDb", e);
		}
		if (StringUtils.isEmpty(maxNeId)) {
			return 1;
		}
		return Integer.valueOf(maxNeId);
	}

	public void removeGroup(int groupId) {
		rwLock.writeLock().lock();
		groupToNeId.remove(new Integer(groupId));
		diedEmlImGroupIdList.add(groupId);
		rwLock.writeLock().unlock();
	}

	public void removeNe(int groupId, int neId) {
		rwLock.writeLock().lock();
		groupToNeId.get(groupId).remove(neId);
		rwLock.writeLock().unlock();
	}

	public void deleteAllNes() {
		for (int groupId : groupToNeId.keySet()) {
			List<Integer> neIdList = groupToNeId.get(new Integer(groupId));
			if (neIdList != null) {
				for (int neId : neIdList) {
					try {
						DeleteNe.deleteNe(groupId, neId);
					} catch (AdapterException e) {
						log.error("deleteAllNes", e);
					}
				}
			}
			removeGroup(groupId);
		}
	}

	public void destory() throws AdapterException {
		for (int groupId : groupToNeId.keySet()) {
			List<Integer> neIdList = groupToNeId.get(new Integer(groupId));
			if (neIdList != null) {
				for (int neId : neIdList) {
					try {
						DeleteNe.deleteNe(groupId, neId);
					} catch (AdapterException e) {
						log.error("destory", e);
					}
				}
			}
			killEmlImProcess(groupId);
		}
	}

	public void killEmlImProcess(int groupId) throws AdapterException {
		try {
			Process process = new ExecExternalScript().run(ExternalScriptType.KILL_EMLIM, groupId + "");
			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR,
						"Kill emlim process with groupId " + groupId + " failed!!!");
			}
		} catch (Exception e) {
			log.error("killEmlImProcess", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}
}
