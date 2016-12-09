package com.nsb.enms.adapter.server.action.method.tp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.action.entity.TpEntity;
import com.nsb.enms.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ParseUtil;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.ManagedObjectType;
import com.nsb.enms.common.utils.Pair;

public class GetCtp {
	private static final Logger log = LogManager.getLogger(GetCtp.class);

	private static final String SCENARIO = ConfLoader.getInstance().getConf(ConfigKey.GET_CONNECTION_PORT_REQ,
			ConfigKey.DEFAULT_GET_CONNECTION_PORT_REQ);

	public static List<TpEntity> getSdhCtps(String groupId, String neId, String tpId) throws AdapterException {
		log.debug("tpId = " + tpId);

		String rsTTPId = getRsCtp(groupId, neId, tpId);
		log.debug("rsTTPId = " + rsTTPId);

		if (StringUtils.isEmpty(rsTTPId)) {
			return null;
		}
		String msTTPId = getMsCtp(groupId, neId, rsTTPId);
		log.debug("msTTPId = " + msTTPId);

		if (StringUtils.isEmpty(msTTPId)) {
			return null;
		}
		String protectedTTPId = getUnProtectedCtp(groupId, neId, msTTPId);
		log.debug("protectedTTPId = " + protectedTTPId);

		if (StringUtils.isEmpty(protectedTTPId)) {
			return null;
		}
		List<TpEntity> ctpList = getAu4Ctps(groupId, neId, protectedTTPId);
		return ctpList;
	}

	private static String getRsCtp(String groupId, String neId, String tpId) throws AdapterException {
		Process process = null;
	    try {
			// labelledOpticalSPITTPBidirectional = 0.0.7.774.127.7.0.3.4
			String objectClass = "0.0.7.774.127.7.0.3.4";
			String opticalSPITTPId = "0.0.7.774.0.7.18";
			String paramKey = opticalSPITTPId;
			String paramValue = tpId;
			String scope = "firstLevelOnly";

			String rsCTPId = "0.0.7.774.0.7.22";
			String filterParam = rsCTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String rsTTPId = handleInputStream(br, "downstreamConnectivityPointer", "rsTTPId=");
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get rsCtp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return rsTTPId;
		} catch (Exception e) {
			log.error("getRsCtp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally
	    {
		    if (process != null)
		        ExecExternalScript.destroyProcess( process );
	    }
	}

	private static String handleInputStream(BufferedReader br, String filter1, String filter2) throws IOException {
		String line;
		String tpId = StringUtils.EMPTY;
		boolean isOk = false;
		while ((line = br.readLine()) != null) {
			if (line.contains("GetReply received")) {
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (line.startsWith(filter1)) {
						tpId = ParseUtil.parseAttrWithMultiValue(line);
						tpId = tpId.replaceAll(filter2, StringUtils.EMPTY);
						isOk = true;
						break;
					}
				}

				if (isOk) {
					break;
				}
			}
		}
		return tpId;
	}

	private static String getMsCtp(String groupId, String neId, String rsTtpId) throws AdapterException {
		Process process = null;
	    try {
			// rsTTPBidirectional = 0.0.7.774.0.3.40
			String objectClass = "0.0.7.774.0.3.40";
			// rsTTPId = 0.0.7.774.0.7.25
			String paramKey = "0.0.7.774.0.7.25";
			String paramValue = rsTtpId;
			String scope = "firstLevelOnly";

			String msCTPId = "0.0.7.774.0.7.13";
			String filterParam = msCTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String msTTPId = handleInputStream(br, "downstreamConnectivityPointer", "msTTPId=");
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get msCtp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return msTTPId;
		} catch (Exception e) {
			log.error("getMsCtp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally
	    {
		    if (process != null)
		        ExecExternalScript.destroyProcess( process );
	    }
	}

	private static String getUnProtectedCtp(String groupId, String neId, String msTtpId) throws AdapterException {
		Process process = null;
	    try {
			// msTTPBidirectional = 0.0.7.774.0.3.25
			String objectClass = "0.0.7.774.0.3.25";
			// msTTPId = 0.0.7.774.0.7.16
			String paramKey = "0.0.7.774.0.7.16";
			String paramValue = msTtpId;
			String scope = "firstLevelOnly";

			String unprotectedCTPId = "0.0.7.774.127.3.0.7.16";
			String filterParam = unprotectedCTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String protectedTTPId = handleInputStream(br, "downstreamConnectivityPointer=single", "protectedTTPId=");
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get unProtectedCtp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return protectedTTPId;
		} catch (Exception e) {
			log.error("getUnProtectedCtp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
	}

	private static List<TpEntity> getAu4Ctps(String groupId, String neId, String protectedTtpId)
			throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();
		Process process = null;
		try {
			// protectedTTPBidirectional = 0.0.7.774.127.3.0.3.2
			String objectClass = "0.0.7.774.127.3.0.3.2";

			// protectedTTPId = 0.0.7.774.127.3.0.7.4
			String paramKey = "0.0.7.774.127.3.0.7.4";
			String paramValue = protectedTtpId;
			String scope = "wholeSubtree";

			String au4CTPId = "0.0.7.774.0.7.2";
			String filterParam = au4CTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			BufferedReader br = handleInputStream(tpList, process);
			br.close();

			if (process.waitFor() != 0) {
				log.error("");
				log.error("Get au4Ctp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return tpList;
		} catch (Exception e) {
			log.error("getAu4Ctp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally
		{
		    if (process != null)
		        ExecExternalScript.destroyProcess( process );
		}
	}

	public static Pair<List<String>, List<TpEntity>> getPdhCtp(String groupId, String neId, String tpId)
			throws AdapterException {
		log.debug("tpId = " + tpId);

		Pair<String, String> pair = getEPdhCtp(groupId, neId, tpId);
		String moc = pair.getFirst();
		String ttpId = pair.getSecond();
		log.debug("ttpId = " + ttpId);

		if (StringUtils.isEmpty(ttpId)) {
			log.error("ttpId is null or empty");
			return null;
		}
		List<TpEntity> tpList = null;
		Pair<List<String>, List<TpEntity>> pair2 = new Pair<List<String>, List<TpEntity>>();
		if (moc.startsWith("e1")) {
			tpList = getVc12Ttps(groupId, neId, ttpId);
			pair2.setFirst(ManagedObjectType.E1_PHYSICAL.getLayerRates());
			pair2.setSecond(tpList);
			return pair2;
		}

		if (moc.startsWith("e3")) {
			tpList = getVc3Ttps(groupId, neId, ttpId);
			pair2.setFirst(ManagedObjectType.E3_PHYSICAL.getLayerRates());
			pair2.setSecond(tpList);
			return pair2;
		}
		return pair2;
	}

	private static Pair<String, String> getEPdhCtp(String groupId, String neId, String tpId) throws AdapterException {
		Process process = null;
	    try {
			// pPITTPBidirectionalR1 = 1.3.12.2.1006.54.0.0.3.210
			String objectClass = "1.3.12.2.1006.54.0.0.3.210";
			String pPITTPId = "0.4.0.371.0.7.1";
			String paramKey = pPITTPId;
			String paramValue = tpId;
			String scope = "firstLevelOnly";

			String ePDHCTPId = "0.4.0.371.0.7.2";
			String filterParam = ePDHCTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			String ttpId = "";
			boolean isOk = false;
			Pair<String, String> pair = new Pair<String, String>();
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtil.parseAttrWithSingleValue(line);
							log.debug("moc = " + moc);
							/*
							 * if
							 * (!"e1MonitoringCTPBidirectional".equalsIgnoreCase
							 * (moc)) { break; }
							 */
							pair.setFirst(moc);
						}

						if (line.startsWith("downstreamConnectivityPointer")) {
							ttpId = ParseUtil.parseAttrWithMultiValue(line);
							log.debug("ttpId = " + ttpId);
							ttpId = ttpId.split("/")[0];
							ttpId = ttpId.split("=")[1];
							pair.setSecond(ttpId);
							isOk = true;
							break;
						}
					}

					if (isOk) {
						break;
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get ePdhCtp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return pair;
		} catch (Exception e) {
			log.error("getEpdhCtp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally
	    {
		    if (process != null)
		        ExecExternalScript.destroyProcess( process );
	    }
	}

	private static List<TpEntity> getVc12Ttps(String groupId, String neId, String tpId) throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();
		Process process = null;
		try {
			// vc12PathTraceTTPBidirectional = 0.0.7.774.127.7.0.3.10
			String objectClass = "0.0.7.774.127.7.0.3.10";
			String vc12TTPId = "0.0.7.774.0.7.39";
			String paramKey = vc12TTPId;
			String paramValue = tpId;
			String scope = "baseObject";

			String filterParam = vc12TTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			BufferedReader br = handleInputStream(tpList, process);
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get vc12Ttp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return tpList;
		} catch (Exception e) {
			log.error("getVc12Ttp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
	}

	private static List<TpEntity> getVc3Ttps(String groupId, String neId, String tpId) throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();
		try {
			String objectClass = "0.0.7.774.0.3.101";
			String vc3TTPId = "0.0.7.774.0.7.41";
			String paramKey = vc3TTPId;
			String paramValue = tpId;
			String scope = "baseObject";

			String filterParam = vc3TTPId;

			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			BufferedReader br = handleInputStream(tpList, process);
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get vc3Ttp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return tpList;
		} catch (Exception e) {
			log.error("getVc3Ttp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		}
	}

	public static List<TpEntity> getTu12Ctps(String groupId, String neId, String vc4TtpId) throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();
		Process process = null;
		try {
			// modifiableVC4TTPBidirectionalR1 = 0.0.7.774.127.2.0.3.25
			String objectClass = "0.0.7.774.127.2.0.3.25";

			// vc4TTPId = 0.0.7.774.0.7.42
			String paramKey = "0.0.7.774.0.7.42";
			String paramValue = vc4TtpId;
			String scope = "wholeSubtree";

			String tu12CTPId = "0.0.7.774.0.7.30";
			String filterParam = tu12CTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			BufferedReader br = handleInputStream(tpList, process);
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get tu12Ctp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return tpList;
		} catch (Exception e) {
			log.error("getTu12Ctp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
	}

	public static List<TpEntity> getTu3Ctps(String groupId, String neId, String vc4TtpId) throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();
		Process process = null;
		try {
			// modifiableVC4TTPBidirectionalR1 = 0.0.7.774.127.2.0.3.25
			String objectClass = "0.0.7.774.127.2.0.3.25";

			// vc4TTPId = 0.0.7.774.0.7.42
			String paramKey = "0.0.7.774.0.7.42";
			String paramValue = vc4TtpId;
			String scope = "wholeSubtree";

			String tu3CTPId = "0.0.7.774.0.7.32";
			String filterParam = tu3CTPId;

			process = ExecExternalScript.run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId, neId,
					paramKey, paramValue, scope, filterParam);
			BufferedReader br = handleInputStream(tpList, process);
			br.close();

			if (process.waitFor() != 0) {
				log.error("Get tu3Ctp failed!!!");
				throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
			}
			return tpList;
		} catch (Exception e) {
			log.error("getTu3Ctp", e);
			throw new AdapterException(ErrorCode.FAIL_GET_TP_BY_EMLIM);
		} finally {
            if (process != null)
                ExecExternalScript.destroyProcess( process );
        }
	}

	private static BufferedReader handleInputStream(List<TpEntity> tpList, Process process) throws IOException {
		InputStream inputStream = process.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.contains("GetReply received")) {
				TpEntity portEntity = new TpEntity();
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if (line.startsWith("managedObjectClass")) {
						String moc = ParseUtil.parseAttrWithSingleValue(line);
						portEntity.setMoc(moc);
						continue;
					}
					if (line.startsWith("managedObjectInstance")) {
						String moi = ParseUtil.parseAttrWithMultiValue(line);
						portEntity.setMoi(moi);
						continue;
					}

					// if (line.startsWith("alarmStatus")) {
					// portEntity.setAlarmStatus(ParseUtil.parseAttr(line));
					// continue;
					// }

					if (line.startsWith("supportedByObjectList")) {
						portEntity.setSupportedByObjectList(ParseUtil.parseList(line));
						continue;
					}

					if (line.startsWith("operationalState")) {
						portEntity.setOperationalState(ParseUtil.parseAttr(line));
						continue;
					}

					if (line.startsWith("-----------------")) {
						tpList.add(portEntity);
						break;
					}
				}
			}
		}
		return br;
	}
}
