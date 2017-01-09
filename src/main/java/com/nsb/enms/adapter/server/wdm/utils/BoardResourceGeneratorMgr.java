package com.nsb.enms.adapter.server.wdm.utils;



import com.nsb.enms.adapter.server.wdm.action.entity.SnmpTpEntity;
import com.nsb.enms.adapter.server.wdm.business.boardResourceGenerator.BasicOAResourceGenerator;
import com.nsb.enms.adapter.server.wdm.business.boardResourceGenerator.BasicOTResourceGenerator;
import com.nsb.enms.adapter.server.wdm.business.boardResourceGenerator.BasicSFDResourceGenerator;
import com.nsb.enms.adapter.server.wdm.business.boardResourceGenerator.OTResourceGenerator_260SCX2;
import com.nsb.enms.adapter.server.wdm.business.itf.BoardResourceGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.CTPGenerator;
import com.nsb.enms.adapter.server.wdm.business.itf.PortConverter;
import com.nsb.enms.adapter.server.wdm.business.itf.XCGenerator;
import com.nsb.enms.restful.model.adapter.AdpTp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by minichen on 2017/1/5.
 */
public class BoardResourceGeneratorMgr {
    private static BoardResourceGeneratorMgr instance = new BoardResourceGeneratorMgr();

    public static BoardResourceGeneratorMgr getInstance() {
        return instance;
    }

    private Map<String, BoardResourceGenerator> generator=new HashMap<String, BoardResourceGenerator>();
    private BoardResourceGeneratorMgr() {
        init();
    }
    private void init(){

        BoardResourceGenerator ot_Client10Line1_100G=new BasicOTResourceGenerator(2,4,10);
        BoardResourceGenerator ot_260SCX2= new OTResourceGenerator_260SCX2();
        BoardResourceGenerator basicOAResourceGenerator=new BasicOAResourceGenerator();
        BoardResourceGenerator basicSFDResourceGenerator=new BasicSFDResourceGenerator();

        //TODO: 根据需要对不同盘放入相应的BoardResourceGenerator
        generator.put("130SNX10", ot_Client10Line1_100G);
        generator.put("130SCX10", ot_Client10Line1_100G);
        generator.put("260SCX2", ot_260SCX2);
        generator.put("SFD44", basicSFDResourceGenerator);
        generator.put("SFD44B", basicSFDResourceGenerator);
        generator.put("SFD40", basicSFDResourceGenerator);
        generator.put("SFD40B", basicSFDResourceGenerator);
        generator.put("AHPHG", basicOAResourceGenerator);

        //...
    }

    public BoardResourceGenerator getGenerator(String boardType){
        return generator.get(boardType);
    }

    static public class Main {
        public static void main(String[] argv){

            //TODO:假设我们已经从下层snmp得到了所有Port的SnmpPortEntity列表
            //...
            List<SnmpTpEntity> entities=new ArrayList<SnmpTpEntity>();
            generateAllPortsAndCTPs(entities);
            generateAllXCs();


        }
        private static void generateAllPortsAndCTPs(List<SnmpTpEntity> entities){
            AdpTp port;
            BoardResourceGenerator g=null;
            PortConverter portConverter;
            CTPGenerator ctpGenerator;
            XCGenerator xcGenerator;
            for(SnmpTpEntity entity : entities){
                //TODO:
                //g= BoardResourceGeneratorMgr.getInstance().getGenerator(entity.getBoardType());
                if(null!=g){
                    port=new AdpTp();
                    boolean rlt=g.getPortConverter().convert(port, entity);
                    if(rlt) {
                        //TODO: port写入DB
                        ctpGenerator=g.getCTPGenerator();
                        if(null!=ctpGenerator){
                            List<AdpTp> ctps=ctpGenerator.generate(port, entity);
                            //TODO: ctps写入DB
                        }
                    }else{
                        //TODO:记录日志
                    }
                }else{
                    //TODO:记录日志
                }
            }

        }
        private static void generateAllXCs(){
            //TODO: 为所有盘按盘生成xc
            //0. 从DB取出所有盘
            //1. 从DB取出该盘的所有TP
            //2. BoardResourceGeneratorMgr.getInstance().getGenerator(entity.getBoardType()).getXCGenerator().generate();
        }
    }
}
