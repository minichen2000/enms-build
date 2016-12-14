package com.nsb.enms.adapter.server.sdh.action.method.eq;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.ExternalScriptType;
import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.common.conf.ConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ParseUtil;
import com.nsb.enms.adapter.server.sdh.action.entity.EquipmentEntity;
import com.nsb.enms.adapter.server.sdh.action.entity.TptCoordinatorEntity;
import com.nsb.enms.adapter.server.sdh.action.method.ExecExternalScript;
import com.nsb.enms.common.ErrorCode;

public class CreatePort {
	private static final Logger log = LogManager.getLogger(CreatePort.class);

	private static final String CREATE_ISA_PORT_VC12 = ConfLoader.getInstance().getConf(ConfigKey.CREATE_ISA_PORT_VC12,
			ConfigKey.DEFAULT_CREATE_ISA_PORT_VC12);

	private static final String CREATE_ISA_PORT_VC12X = ConfLoader.getInstance()
			.getConf(ConfigKey.CREATE_ISA_PORT_VC12X, ConfigKey.DEFAULT_CREATE_ISA_PORT_VC12X);

	private static final String CREATE_ISA_PORT_VCX = ConfLoader.getInstance().getConf(ConfigKey.CREATE_ISA_PORT_VCX,
			ConfigKey.DEFAULT_CREATE_ISA_PORT_VCX);

	public static List<TptCoordinatorEntity> createISAPortVC12(String groupId, String neId, String tptCoordinatorId,
			String position) throws AdapterException {
		try {
			log.debug("--------------Start createISAPortVC12--------------");
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, CREATE_ISA_PORT_VC12, groupId, neId,
					tptCoordinatorId, position);

			InputStream inputStream = process.getInputStream();
			List<TptCoordinatorEntity> tptCorrdinatorList = new LinkedList<TptCoordinatorEntity>();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;

			while ((line = br.readLine()) != null) {
				if (line.indexOf("GetReply received") >= 0) {
					TptCoordinatorEntity tptCoordinatorEntity = new TptCoordinatorEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.indexOf("managedObjectClass") >= 0) {
							String moc = ParseUtil.parseAttrWithSingleValue(line);
							tptCoordinatorEntity.setMoc(moc);
							continue;
						}

						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtil.parseAttrWithMultiValue(line);
							String[] elements = moi.split("/");
							String tptCoordinatorId1 = elements[2].split("=")[1];
							String rack = "/equipmentId=" + tptCoordinatorId1.substring(0, 1);
							String shelf = "/equipmentId=" + tptCoordinatorId1.substring(1, 2);
							String slotId = tptCoordinatorId1.substring(2, 4);
							if (slotId.startsWith("0")) {
								slotId = slotId.substring(1, 2);
							}
							String slot = "/equipmentId=" + slotId;
							String equMoi = elements[0] + "/" + elements[1] + rack + shelf + slot;
							tptCoordinatorEntity.setMoi(moi);
							tptCoordinatorEntity.setEquMoi(equMoi);
							continue;
						}

						if (line.startsWith("cardIpAddress")) {
							String cardIpAddress = ParseUtil.parseAttrWithSingleValue(line);
							Pattern pattern = Pattern.compile("ipAddress\\s*=(.*),\\s*ipMask\\s*=(.*)");
							Matcher matcher = pattern.matcher(cardIpAddress);
							if (matcher.find()) {
								tptCoordinatorEntity.setIpAddress(matcher.group(1));
								tptCoordinatorEntity.setIpMask(matcher.group(2));
							}
						}

						if (line.startsWith("-----------------")) {
							tptCorrdinatorList.add(tptCoordinatorEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM);
			}
			log.debug("--------------End createISAPortVC12--------------");
			return tptCorrdinatorList;
		} catch (Exception e) {
			log.error("createISAPortVC12", e);
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM);
		}
	}

	public static List<EquipmentEntity> createISAPortVC12x(String groupId, String neId, String tptCoordinatorId,
			String position) throws AdapterException {
		try {
			log.debug("------------Start createISAPortVC12x-------------------");
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, CREATE_ISA_PORT_VC12X, groupId, neId);

			InputStream inputStream = process.getInputStream();
			List<EquipmentEntity> eqList = new LinkedList<EquipmentEntity>();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;

			while ((line = br.readLine()) != null) {
				if (line.indexOf("GetReply received") >= 0) {
					EquipmentEntity equipmentEntity = new EquipmentEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.indexOf("managedObjectClass") >= 0) {
							String moc = ParseUtil.parseAttrWithSingleValue(line);
							equipmentEntity.setMoc(moc);
							continue;
						}

						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtil.parseAttrWithMultiValue(line);
							equipmentEntity.setMoi(moi);
							continue;
						}

						if (line.startsWith("-----------------")) {
							eqList.add(equipmentEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM);
			}
			log.debug("------------End createISAPortVC12x-------------------");
			return eqList;
		} catch (Exception e) {
			log.error("createISAPortVC12x", e);
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM);
		}
	}

	public static List<TptCoordinatorEntity> createISAPortVCx(String groupId, String neId, String tptCoordinatorId,
			String position, String objClass, String vcxNum) throws AdapterException {
		log.debug("--------------Start createISAPortVCx--------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, CREATE_ISA_PORT_VCX, groupId, neId,
					tptCoordinatorId, position, objClass, vcxNum);

			InputStream inputStream = process.getInputStream();
			List<TptCoordinatorEntity> tptCorrdinatorList = new LinkedList<TptCoordinatorEntity>();

			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;

			while ((line = br.readLine()) != null) {
				if (line.indexOf("GetReply received") >= 0) {
					TptCoordinatorEntity tptCoordinatorEntity = new TptCoordinatorEntity();
					while ((line = br.readLine()) != null) {
						line = line.trim();
						if (line.indexOf("managedObjectClass") >= 0) {
							String moc = ParseUtil.parseAttrWithSingleValue(line);
							tptCoordinatorEntity.setMoc(moc);
							continue;
						}

						if (line.startsWith("managedObjectInstance")) {
							String moi = ParseUtil.parseAttrWithMultiValue(line);
							String[] elements = moi.split("/");
							String tptCoordinatorId1 = elements[2].split("=")[1];
							String rack = "/equipmentId=" + tptCoordinatorId1.substring(0, 1);
							String shelf = "/equipmentId=" + tptCoordinatorId1.substring(1, 2);
							String slotId = tptCoordinatorId1.substring(2, 4);
							if (slotId.startsWith("0")) {
								slotId = slotId.substring(1, 2);
							}
							String slot = "/equipmentId=" + slotId;
							String equMoi = elements[0] + "/" + elements[1] + rack + shelf + slot;
							tptCoordinatorEntity.setMoi(moi);
							tptCoordinatorEntity.setEquMoi(equMoi);
							continue;
						}

						if (line.startsWith("cardIpAddress")) {
							String cardIpAddress = ParseUtil.parseAttrWithSingleValue(line);
							Pattern pattern = Pattern.compile("ipAddress\\s*=(.*),\\s*ipMask\\s*=(.*)");
							Matcher matcher = pattern.matcher(cardIpAddress);
							if (matcher.find()) {
								tptCoordinatorEntity.setIpAddress(matcher.group(1));
								tptCoordinatorEntity.setIpMask(matcher.group(2));
							}
						}

						if (line.startsWith("-----------------")) {
							tptCorrdinatorList.add(tptCoordinatorEntity);
							break;
						}
					}
				}
			}
			br.close();

			if (process.waitFor() != 0) {
				throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM);
			}

			log.debug("--------------End createISAPortVCx--------------");
			return tptCorrdinatorList;
		} catch (Exception e) {
			log.error("createISAPortVCx", e);
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM);
		}
	}
}