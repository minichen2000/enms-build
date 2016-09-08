package com.nsb.enms.restful.adapter.server.action.method.tp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.restful.adapter.server.action.entity.TpEntity;
import com.nsb.enms.restful.adapter.server.action.method.ExecExternalScript;
import com.nsb.enms.restful.adapter.server.common.ExternalScriptType;
import com.nsb.enms.restful.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.restful.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterException;
import com.nsb.enms.restful.adapter.server.common.exception.AdapterExceptionType;
import com.nsb.enms.restful.adapter.server.common.util.ParseUtils;

public class GetCtp {
	private static final Logger log = LogManager.getLogger(GetCtp.class);

	private static final String SCENARIO = ConfLoader.getInstance().getConf(ConfigKey.GET_CONNECTION_PORT_REQ,
			ConfigKey.DEFAULT_GET_CONNECTION_PORT_REQ);

	public static List<TpEntity> getSdhCtp(int groupId, int neId, String tpId) throws AdapterException {
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
		List<TpEntity> ctpList = getAu4Ctp(groupId, neId, protectedTTPId);
		return ctpList;
	}

	private static String getRsCtp(int groupId, int neId, String tpId) throws AdapterException {
		try {
			// labelledOpticalSPITTPBidirectional = 0.0.7.774.127.7.0.3.4
			String objectClass = "0.0.7.774.127.7.0.3.4";
			String opticalSPITTPId = "0.0.7.774.0.7.18";
			String paramKey = opticalSPITTPId;
			String paramValue = tpId;
			String scope = "firstLevelOnly";

			String rsCTPId = "0.0.7.774.0.7.22";
			String filterParam = rsCTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, objectClass,
					String.valueOf(groupId), String.valueOf(neId), paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			String rsTTPId = "";
			boolean isOk = false;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("downstreamConnectivityPointer")) {
							rsTTPId = ParseUtils.parseAttrWithMultiValue(line);
							rsTTPId = rsTTPId.replaceAll("rsTTPId=", StringUtils.EMPTY);
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
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get rsCtp failed!!!");
			}
			return rsTTPId;
		} catch (Exception e) {
			log.error("getRsCtp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	private static String getMsCtp(int groupId, int neId, String rsTtpId) throws AdapterException {
		try {
			// rsTTPBidirectional = 0.0.7.774.0.3.40
			String objectClass = "0.0.7.774.0.3.40";
			// rsTTPId = 0.0.7.774.0.7.25
			String paramKey = "0.0.7.774.0.7.25";
			String paramValue = rsTtpId;
			String scope = "firstLevelOnly";

			String msCTPId = "0.0.7.774.0.7.13";
			String filterParam = msCTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, objectClass,
					String.valueOf(groupId), String.valueOf(neId), paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			String msTTPId = "";
			boolean isOk = false;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("downstreamConnectivityPointer")) {
							msTTPId = ParseUtils.parseAttrWithMultiValue(line);
							msTTPId = msTTPId.replaceAll("msTTPId=", StringUtils.EMPTY);
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
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get rsCtp failed!!!");
			}
			return msTTPId;
		} catch (Exception e) {
			log.error("getMsCtp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	private static String getUnProtectedCtp(int groupId, int neId, String msTtpId) throws AdapterException {
		try {
			// msTTPBidirectional = 0.0.7.774.0.3.25
			String objectClass = "0.0.7.774.0.3.25";
			// msTTPId = 0.0.7.774.0.7.16
			String paramKey = "0.0.7.774.0.7.16";
			String paramValue = msTtpId;
			String scope = "firstLevelOnly";

			String unprotectedCTPId = "0.0.7.774.127.3.0.7.16";
			String filterParam = unprotectedCTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, objectClass,
					String.valueOf(groupId), String.valueOf(neId), paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			String protectedTTPId = "";
			boolean isOk = false;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("downstreamConnectivityPointer=single")) {
							protectedTTPId = ParseUtils.parseAttrWithMultiValue(line);
							protectedTTPId = protectedTTPId.replaceAll("protectedTTPId=", StringUtils.EMPTY);
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
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get rsCtp failed!!!");
			}
			return protectedTTPId;
		} catch (Exception e) {
			log.error("getUnProtectedCtp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	private static List<TpEntity> getAu4Ctp(int groupId, int neId, String protectedTtpId) throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();

		try {
			// protectedTTPBidirectional = 0.0.7.774.127.3.0.3.2
			String objectClass = "0.0.7.774.127.3.0.3.2";

			// protectedTTPId = 0.0.7.774.127.3.0.7.4
			String paramKey = "0.0.7.774.127.3.0.7.4";
			String paramValue = protectedTtpId;
			String scope = "wholeSubtree";

			String au4CTPId = "0.0.7.774.0.7.2";
			String filterParam = au4CTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, objectClass,
					String.valueOf(groupId), String.valueOf(neId), paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					TpEntity portEntity = new TpEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtils.parseAttrWithSingleValue(line);
							portEntity.setMoc(moc);
							continue;
						}
						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtils.parseAttrWithMultiValue(line);
							portEntity.setMoi(moi);
							continue;
						}

						if (line.startsWith("alarmStatus")) {
							portEntity.setAlarmStatus(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("supportedByObjectList")) {
							portEntity.setSupportedByObjectList(ParseUtils.parseList(line));
							continue;
						}

						if (line.startsWith("operationalState")) {
							portEntity.setOperationalState(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("-----------------")) {
							tpList.add(portEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get rsCtp failed!!!");
			}
			return tpList;
		} catch (Exception e) {
			log.error("getAu4Ctp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	public static List<TpEntity> getPdhCtp(int groupId, int neId, String tpId) throws AdapterException {
		log.debug("tpId = " + tpId);

		String vc12TTPId = getE1Ctp(groupId, neId, tpId);
		log.debug("vc12TTPId = " + vc12TTPId);

		if (StringUtils.isEmpty(vc12TTPId)) {
			return null;
		}

		List<TpEntity> tpList = getVc12Ttp(groupId, neId, vc12TTPId);
		return tpList;
	}

	private static String getE1Ctp(int groupId, int neId, String tpId) throws AdapterException {
		try {
			// pPITTPBidirectionalR1 = 1.3.12.2.1006.54.0.0.3.210
			String objectClass = "1.3.12.2.1006.54.0.0.3.210";
			String pPITTPId = "0.4.0.371.0.7.1";
			String paramKey = pPITTPId;
			String paramValue = tpId;
			String scope = "firstLevelOnly";

			String ePDHCTPId = "0.4.0.371.0.7.2";
			String filterParam = ePDHCTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, objectClass,
					String.valueOf(groupId), String.valueOf(neId), paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			String vc12TTPId = "";
			boolean isOk = false;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtils.parseAttrWithSingleValue(line);
							log.debug("moc = " + moc);
							if (!"e1MonitoringCTPBidirectional".equalsIgnoreCase(moc)) {
								break;
							}
						}

						if (line.startsWith("downstreamConnectivityPointer")) {
							vc12TTPId = ParseUtils.parseAttrWithMultiValue(line);
							log.debug("vc12TTPId = " + vc12TTPId);
							vc12TTPId = vc12TTPId.split("/")[0];
							vc12TTPId = vc12TTPId.replaceAll("vc12TTPId=", StringUtils.EMPTY);
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
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get rsCtp failed!!!");
			}
			return vc12TTPId;
		} catch (Exception e) {
			log.error("getRsCtp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	private static List<TpEntity> getVc12Ttp(int groupId, int neId, String tpId) throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();
		try {
			// vc12PathTraceTTPBidirectional = 0.0.7.774.127.7.0.3.10
			String objectClass = "0.0.7.774.127.7.0.3.10";
			String vc12TTPId = "0.0.7.774.0.7.39";
			String paramKey = vc12TTPId;
			String paramValue = tpId;
			String scope = "baseObject";

			String filterParam = vc12TTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, objectClass,
					String.valueOf(groupId), String.valueOf(neId), paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					TpEntity portEntity = new TpEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtils.parseAttrWithSingleValue(line);
							portEntity.setMoc(moc);
							continue;
						}
						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtils.parseAttrWithMultiValue(line);
							portEntity.setMoi(moi);
							continue;
						}

						if (line.startsWith("alarmStatus")) {
							portEntity.setAlarmStatus(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("supportedByObjectList")) {
							portEntity.setSupportedByObjectList(ParseUtils.parseList(line));
							continue;
						}

						if (line.startsWith("operationalState")) {
							portEntity.setOperationalState(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("-----------------")) {
							tpList.add(portEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get rsCtp failed!!!");
			}
			return tpList;
		} catch (Exception e) {
			log.error("getAu4Ctp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}

	public static List<TpEntity> getTu12Ctp(String groupId, String neId, String vc4TtpId) throws AdapterException {
		List<TpEntity> tpList = new LinkedList<TpEntity>();

		try {
			// modifiableVC4TTPBidirectionalR1 = 0.0.7.774.127.2.0.3.25
			String objectClass = "0.0.7.774.127.2.0.3.25";

			// vc4TTPId = 0.0.7.774.0.7.42
			String paramKey = "0.0.7.774.0.7.42";
			String paramValue = vc4TtpId;
			String scope = "wholeSubtree";

			String tu12CTPId = "0.0.7.774.0.7.30";
			String filterParam = tu12CTPId;

			Process process = new ExecExternalScript().run(ExternalScriptType.TSTMGR, SCENARIO, objectClass, groupId,
					neId, paramKey, paramValue, scope, filterParam);
			InputStream inputStream = process.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("GetReply received")) {
					TpEntity portEntity = new TpEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.startsWith("managedObjectClass")) {
							String moc = ParseUtils.parseAttrWithSingleValue(line);
							portEntity.setMoc(moc);
							continue;
						}
						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtils.parseAttrWithMultiValue(line);
							portEntity.setMoi(moi);
							continue;
						}

						if (line.startsWith("alarmStatus")) {
							portEntity.setAlarmStatus(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("supportedByObjectList")) {
							portEntity.setSupportedByObjectList(ParseUtils.parseList(line));
							continue;
						}

						if (line.startsWith("operationalState")) {
							portEntity.setOperationalState(ParseUtils.parseAttr(line));
							continue;
						}

						if (line.startsWith("-----------------")) {
							tpList.add(portEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, "Get rsCtp failed!!!");
			}
			return tpList;
		} catch (Exception e) {
			log.error("getTu12Ctp", e);
			throw new AdapterException(AdapterExceptionType.EXCPT_INTERNAL_ERROR, e.getMessage());
		}
	}
}