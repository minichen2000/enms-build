package com.nsb.enms.adapter.server.wdm.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nsb.enms.adapter.server.common.business.itf.ObjectIdGenerator;
import com.nsb.enms.adapter.server.common.db.mgr.AdpTpsDbMgr;
import com.nsb.enms.adapter.server.common.exception.AdapterException;
import com.nsb.enms.adapter.server.wdm.business.objectIdGenerator.WdmObjectIdGenerator;
import com.nsb.enms.common.Direction;
import com.nsb.enms.common.ErrorCode;
import com.nsb.enms.common.LayerRate;
import com.nsb.enms.common.TpType;
import com.nsb.enms.restful.model.adapter.AdpKVPair;
import com.nsb.enms.restful.model.adapter.AdpTp;

public class AdpTpWrapperUtil {
	private final static Logger log = LogManager.getLogger(AdpTpWrapperUtil.class);
	private static ObjectIdGenerator objectIdGenerator = new WdmObjectIdGenerator();
	private static AdpTpsDbMgr tpDbMgr = new AdpTpsDbMgr();

	public static AdpTp constructCTP(String neId, String nativeName, List<String> layerRates, AdpTp ptp,
			String primaryLayerRate, List<AdpKVPair> params) throws AdapterException {
		AdpTp tp = new AdpTp();
		tp.setNeId(neId);
		tp.setNativeName(nativeName);
		tp.setUserLabel(nativeName);
		tp.setLayerRates(layerRates);
		String keyOnNe = nativeName + "_" + ptp.getKeyOnNe();
		tp.setKeyOnNe(keyOnNe);
		tp.setDirection(Direction.BI.name());
		tp.setFreeResources(null);
		tp.setTpType(TpType.CTP.name());
		tp.setPrimaryLayerRate(primaryLayerRate);
		tp.setId(objectIdGenerator.generateCTPId(ptp, tp));
		String ptpID = ptp.getId();
		tp.setPtpID(ptpID);
		tp.setParentTpID(ptpID);
		tp.setParams(null);
		tp.setAlarmState(null);
		return addCTP2DB(tp);
	}

	private static AdpTp addCTP2DB(AdpTp ctp) throws AdapterException {
		AdpTp ctpFromDb = isCTPExisted(ctp.getNeId(), ctp.getKeyOnNe());
		if (null != ctpFromDb) {
			// TODO 替换已有值
			return ctpFromDb;
		}
		try {
			ctpFromDb = tpDbMgr.addTp(ctp);
		} catch (Exception e) {
			log.error("addTps", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return ctpFromDb;
	}

	public static AdpTp isCTPExisted(String neId, String keyOnNe) throws AdapterException {
		try {
			AdpTp tpFromDb = tpDbMgr.getTpByKeyOnNe(neId, keyOnNe);
			if (null != tpFromDb && StringUtils.isNotEmpty(tpFromDb.getId())) {
				return tpFromDb;
			}
		} catch (Exception e) {
			log.error("getTpByKeyOnNe", e);
			throw new AdapterException(ErrorCode.FAIL_DB_OPERATION);
		}
		return null;
	}

	public static AdpTp getOCHCTP(String neId, AdpTp ptp) throws AdapterException {
		return getOCHCTP(neId, ptp, "/och=1");
	}

	public static AdpTp getOCHCTP(String neId, AdpTp ptp, String nativeName) throws AdapterException {
		String primaryLayerRate = LayerRate.OCH.name();
		List<String> layerRates = new ArrayList<String>();
		layerRates.add(primaryLayerRate);
		List<AdpKVPair> params = new ArrayList<AdpKVPair>();
		AdpKVPair kv = new AdpKVPair();
		kv.setKey("frequency");
		String frequency = SnmpOCHFrequencyUtil.getFrequency(neId, ptp.getKeyOnNe());
		kv.setValue(frequency);
		params.add(kv);
		return constructCTP(neId, nativeName, layerRates, ptp, primaryLayerRate, params);
	}
}
