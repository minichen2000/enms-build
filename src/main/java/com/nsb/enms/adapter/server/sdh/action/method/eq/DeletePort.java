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

import com.nsb.enms.adapter.server.common.conf.ConfLoader;
import com.nsb.enms.adapter.server.sdh.constants.SdhConfigKey;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.common.utils.ParseUtil;
import com.nsb.enms.adapter.server.sdh.action.entity.TptCoordinatorEntity;
import com.nsb.enms.adapter.server.sdh.action.method.ExecExternalScript;
import com.nsb.enms.adapter.server.sdh.constants.ExternalScriptType;
import com.nsb.enms.common.ErrorCode;

public class DeletePort {
	private static final Logger log = LogManager.getLogger(DeletePort.class);

	private static final String DELETE_ISA_PORT = ConfLoader.getInstance().getConf(SdhConfigKey.DELETE_ISA_PORT,
			SdhConfigKey.DEFAULT_DELETE_ISA_PORT);

	public static List<TptCoordinatorEntity> deleteISAPort(String groupId, String neId, String tptCoordinatorId,
			String position) throws AdapterException {
		log.debug("--------------Start deleteISAPort--------------");
		try {
			Process process = ExecExternalScript.run(ExternalScriptType.TSTMGR, DELETE_ISA_PORT, groupId, neId,
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

			log.debug("--------------End deleteISAPort--------------");
			return tptCorrdinatorList;
		} catch (Exception e) {
			log.error("deleteISAPort", e);
			throw new AdapterException(ErrorCode.FAIL_GET_EQUIPMENT_BY_EMLIM);
		}
	}
}