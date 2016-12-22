package com.nsb.enms.adapter.server.common.utils;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class PSSBoardUtil {

	private static Logger log = Logger.getLogger(PSSBoardUtil.class);
/*   39 */   private static final HashMap<String, HashMap<String, String>> EqptToPortMapTable = new HashMap();
/*   40 */   private static final HashMap<String, String> DCMPortMap = new HashMap();
/*   41 */   private static final HashMap<String, String> ITLBPortMap = new HashMap();
/*   42 */   private static final HashMap<String, String> ALPHGPortMap = new HashMap();
/*   43 */   private static final HashMap<String, String> AHPHGPortMap = new HashMap();
/*   44 */   private static final HashMap<String, String> AHPLGPortMap = new HashMap();
/*   45 */   private static final HashMap<String, String> CWR8PortMap = new HashMap();
/*   46 */   private static final HashMap<String, String> CWR8_88PortMap = new HashMap();
/*   47 */   private static final HashMap<String, String> SVACPortMap = new HashMap();
/*   48 */   private static final HashMap<String, String> OPSAPortMap = new HashMap();
/*      */ 
/*   50 */   private static final HashMap<String, String> SFD5APortMap = new HashMap();
/*   51 */   private static final HashMap<String, String> SFD5BPortMap = new HashMap();
/*   52 */   private static final HashMap<String, String> SFD5CPortMap = new HashMap();
/*   53 */   private static final HashMap<String, String> SFD5DPortMap = new HashMap();
/*   54 */   private static final HashMap<String, String> SFD5EPortMap = new HashMap();
/*   55 */   private static final HashMap<String, String> SFD5FPortMap = new HashMap();
/*   56 */   private static final HashMap<String, String> SFD5GPortMap = new HashMap();
/*   57 */   private static final HashMap<String, String> SFD5HPortMap = new HashMap();
/*   58 */   private static final HashMap<String, String> SFD10APortMap = new HashMap();
/*   59 */   private static final HashMap<String, String> SFD10BPortMap = new HashMap();
/*   60 */   private static final HashMap<String, String> SFD10CPortMap = new HashMap();
/*   61 */   private static final HashMap<String, String> SFD10DPortMap = new HashMap();
/*   62 */   private static final HashMap<String, String> SFD44PortMap = new HashMap();
/*   63 */   private static final HashMap<String, String> SFD44BPortMap = new HashMap();
/*      */ 
/*   65 */   private static final HashMap<String, String> SFC8PortMap = new HashMap();
/*   66 */   private static final HashMap<String, String> SFC4APortMap = new HashMap();
/*   67 */   private static final HashMap<String, String> SFC4BPortMap = new HashMap();
/*   68 */   private static final HashMap<String, String> SFC2APortMap = new HashMap();
/*   69 */   private static final HashMap<String, String> SFC2BPortMap = new HashMap();
/*   70 */   private static final HashMap<String, String> SFC2CPortMap = new HashMap();
/*   71 */   private static final HashMap<String, String> SFC2DPortMap = new HashMap();
/*      */ 
/*   73 */   private static final HashMap<String, String> _11STAR1PortMap = new HashMap();
/*   74 */   private static final HashMap<String, String> _11STMM10PortMap = new HashMap();
/*   75 */   private static final HashMap<String, String> _11STGE12PortMap = new HashMap();
/*   76 */   private static final HashMap<String, String> _11DPGE12PortMap = new HashMap();
/*   77 */   private static final HashMap<String, String> _43STX4PortMap = new HashMap();
/*   78 */   private static final HashMap<String, String> _4DPA4PortMap = new HashMap();
/*   79 */   private static final HashMap<String, String> ALPFGTPortMap = new HashMap();
/*   80 */   private static final HashMap<String, String> A2325APortMap = new HashMap();
/*   81 */   private static final HashMap<String, String> OSCTPortMap = new HashMap();
/*   82 */   private static final HashMap<String, String> _11QPA4PortMap = new HashMap();
/*   83 */   private static final HashMap<String, String> _11DPE12PortMap = new HashMap();
/*   84 */   private static final HashMap<String, String> _43STA1PPortMap = new HashMap();
/*   85 */   private static final HashMap<String, String> _4DPA2PortMap = new HashMap();
/*   86 */   private static final HashMap<String, String> _43STX4PPortMap = new HashMap();
/*   87 */   private static final HashMap<String, String> SFD40PortMap = new HashMap();
/*   88 */   private static final HashMap<String, String> SFD40BPortMap = new HashMap();
/*   89 */   private static final HashMap<String, String> SFD8APortMap = new HashMap();
/*   90 */   private static final HashMap<String, String> SFD8BPortMap = new HashMap();
/*   91 */   private static final HashMap<String, String> SFD8CPortMap = new HashMap();
/*   92 */   private static final HashMap<String, String> SFD8DPortMap = new HashMap();
/*   93 */   private static final HashMap<String, String> PMDCLPortMap = new HashMap();
/*   94 */   private static final HashMap<String, String> SFD44LPortMap = new HashMap();
/*   95 */   private static final HashMap<String, String> SFD44BLPortMap = new HashMap();
/*      */ 
/*   97 */   private static final HashMap<String, String> SFC1APortMap = new HashMap();
/*   98 */   private static final HashMap<String, String> SFC1BPortMap = new HashMap();
/*   99 */   private static final HashMap<String, String> SFC1CPortMap = new HashMap();
/*  100 */   private static final HashMap<String, String> SFC1DPortMap = new HashMap();
/*  101 */   private static final HashMap<String, String> SFC1EPortMap = new HashMap();
/*  102 */   private static final HashMap<String, String> SFC1FPortMap = new HashMap();
/*  103 */   private static final HashMap<String, String> SFC1GPortMap = new HashMap();
/*  104 */   private static final HashMap<String, String> SFC1HPortMap = new HashMap();
/*  105 */   private static final HashMap<String, String> PSS1GBEPortMap = new HashMap();
/*  106 */   private static final HashMap<String, String> PSS1MD4PortMap = new HashMap();
/*      */ 
/*  109 */   private static final HashMap<String, String> _112SCA1PortMap = new HashMap();
/*  110 */   private static final HashMap<String, String> _112SCX10PortMap = new HashMap();
/*  111 */   private static final HashMap<String, String> _130SNX10PortMap = new HashMap();
/*      */ 
/*  113 */   private static final HashMap<String, String> _130SCA1PortMap = new HashMap();
/*      */ 
/*  116 */   private static final HashMap<String, String> AM2017BPortMap = new HashMap();
/*  117 */   private static final HashMap<String, String> AM2325BPortMap = new HashMap();
/*  118 */   private static final HashMap<String, String> WTOCMPortMap = new HashMap();
/*  119 */   private static final HashMap<String, String> _43SCT1PortMap = new HashMap();
/*      */ 
/*  121 */   private static final HashMap<String, String> SFD4APortMap = new HashMap();
/*  122 */   private static final HashMap<String, String> SFD4BPortMap = new HashMap();
/*  123 */   private static final HashMap<String, String> SFD4CPortMap = new HashMap();
/*  124 */   private static final HashMap<String, String> SFD4DPortMap = new HashMap();
/*  125 */   private static final HashMap<String, String> SFD4EPortMap = new HashMap();
/*  126 */   private static final HashMap<String, String> SFD4FPortMap = new HashMap();
/*  127 */   private static final HashMap<String, String> SFD4GPortMap = new HashMap();
/*  128 */   private static final HashMap<String, String> SFD4HPortMap = new HashMap();
/*      */ 
/*  130 */   private static final HashMap<String, String> PSS1P21PortMap = new HashMap();
/*      */ 
/*  133 */   private static final HashMap<String, String> AM2125APortMap = new HashMap();
/*  134 */   private static final HashMap<String, String> AM2625APortMap = new HashMap();
/*  135 */   private static final HashMap<String, String> AM2032APortMap = new HashMap();
/*  136 */   private static final HashMap<String, String> RA2PPortMap = new HashMap();
/*      */ 
/*  138 */   private static final HashMap<String, String> _11DPM12PortMap = new HashMap();
/*      */ 
/*  142 */   private static final HashMap<String, String> MVACPortMap = new HashMap();
/*      */ 
/*  145 */   private static final HashMap<String, String> AM2318APortMap = new HashMap();
/*  146 */   private static final HashMap<String, String> MESH4PortMap = new HashMap();
/*  147 */   private static final HashMap<String, String> ITLUPortMap = new HashMap();
/*  148 */   private static final HashMap<String, String> WR8_88APortMap = new HashMap();
/*      */ 
/*  151 */   private static final HashMap<String, String> MVAC8BPortMap = new HashMap();
/*      */ 
/*  154 */   private static final HashMap<String, String> WR2_88PortMap = new HashMap();
/*      */ 
/*  156 */   private static final HashMap<String, String> _11QPE24PortMap = new HashMap();
/*      */ 
/*  159 */   private static final HashMap<String, String> A2P2125PortMap = new HashMap();
/*      */ 
/*  162 */   private static final HashMap<String, String> _4QPA8PortMap = new HashMap();
/*  163 */   private static final HashMap<String, String> PTPCTLPortMap = new HashMap();
/*  164 */   private static final HashMap<String, String> PTPIOPortMap = new HashMap();
/*  165 */   private static final HashMap<String, String> _112PDM11PortMap = new HashMap();
/*      */ 
/*  168 */   private static final HashMap<String, String> _112SDX11PortMap = new HashMap();
/*  169 */   private static final HashMap<String, String> _11OPE8PortMap = new HashMap();
/*  170 */   private static final HashMap<String, String> _11QCE12XPortMap = new HashMap();
/*  171 */   private static final HashMap<String, String> _260SCX2PortMap = new HashMap();
/*  172 */   private static final HashMap<String, String> _AA2DONWPortMap = new HashMap();
/*  173 */   private static final HashMap<String, String> MSH8PortMap = new HashMap();
/*  174 */   private static final HashMap<String, String> _PSC1PortMap = new HashMap();
/*  175 */   private static final HashMap<String, String> _11DPM8PortMap = new HashMap();
/*      */ 
/*  180 */   private static final HashMap<String, String> _A4PSWGPortMap = new HashMap();
/*  181 */   private static final HashMap<String, String> _ASWGPortMap = new HashMap();
/*  182 */   private static final HashMap<String, String> AAR_8APortMap = new HashMap();
/*  183 */   private static final HashMap<String, String> _MCS8X16PortMap = new HashMap();
/*  184 */   private static final HashMap<String, String> _WR20TFMPortMap = new HashMap();
/*  185 */   private static final HashMap<String, String> _WR20TFPortMap = new HashMap();
/*      */ 
/*  188 */   private static final HashMap<String, String> _10AN10GPortMap = new HashMap();
/*  189 */   private static final HashMap<String, String> _24ANMPortMap = new HashMap();
/*  190 */   private static final HashMap<String, String> _24ANMBPortMap = new HashMap();
/*  191 */   private static final HashMap<String, String> _11QCUPCPortMap = new HashMap();
/*  192 */   private static final HashMap<String, String> _24ET1GBPortMap = new HashMap();
/*  193 */   private static final HashMap<String, String> _8ET1GBPortMap = new HashMap();
/*  194 */   private static final HashMap<String, String> _10ET10GPortMap = new HashMap();
/*  195 */   private static final HashMap<String, String> _4AN10GPortMap = new HashMap();
/*  196 */   private static final HashMap<String, String> _130SCUPPortMap = new HashMap();
/*  197 */   private static final HashMap<String, String> _130SCUPBPortMap = new HashMap();
/*  198 */   private static final HashMap<String, String> _MTC1T9PortMap = new HashMap();
/*  199 */   private static final HashMap<String, String> _OTDRPortMap = new HashMap();
/*  200 */   public static final HashMap<String, String> D5X500OTUMap = new HashMap();

			public static String getPSSPortDesc(String boardType, int port) {
				
				String portNum = String.valueOf(port);
				String retVal = null;
				log.debug("search PSS port description. board type " + boardType + ", port number " + port);
/* 3912 */      if (boardType.equalsIgnoreCase("11DPM4M")) {
/* 3913 */       
/* 3914 */       	if ((port > 6) && (port < 11))
/* 3915 */         		boardType = "11DPM4E";
/*      */      }
/* 3917 */     	if ((boardType.equals("260SCX2")) && port == 3) {
/* 3918 */      	 retVal = "C2";
/*      */     	}
/* 3920 */     	else if ((boardType.equals("D5X500")) && (portNum != null) && (D5X500OTUMap.containsKey(portNum))) {
/* 3921 */       	retVal = (String)D5X500OTUMap.get(portNum);
/*      */     	} else {
/* 3923 */      	HashMap portMap = (HashMap)EqptToPortMapTable.get(boardType);
/* 3924 */    		if (portMap != null)
/*      */      	{
/* 3926 */        		retVal = (String)portMap.get(portNum);
/*      */      	}
					else	
						log.error("portMap is null. board type " + boardType + ", port number " + port + " is not supported");
/*      */     	}
/* 3933 */     	if (retVal == null)
/*      */     	{
/* 3935 */       	log.error("retVal is null. board type " + boardType + " is not supported");
/*      */     	}
				log.debug("port description is " + retVal);
				return retVal;
			}
/*      */  static
/*      */  {
/*  372 */     DCMPortMap.put("1", "DCM");
/*      */ 
/*  375 */     ITLBPortMap.put("1", "SIG");
/*  376 */     ITLBPortMap.put("2", "E");
/*  377 */     ITLBPortMap.put("3", "O");
/*      */ 
/*  379 */     ALPHGPortMap.put("1", "SIG");
/*      */ 
/*  381 */     ALPHGPortMap.put("3", "DCM");
/*  382 */     ALPHGPortMap.put("4", "LINE");
/*  383 */     ALPHGPortMap.put("5", "OSC");
/*      */ 
/*  385 */     AHPHGPortMap.put("1", "SIG");
/*      */ 
/*  387 */     AHPHGPortMap.put("3", "DCM");
/*  388 */     AHPHGPortMap.put("4", "LINE");
/*  389 */     AHPHGPortMap.put("5", "OSC");
/*      */ 
/*  392 */     AHPLGPortMap.put("1", "SIG");
/*      */ 
/*  394 */     AHPLGPortMap.put("3", "DCM");
/*  395 */     AHPLGPortMap.put("4", "LINE");
/*  396 */     AHPLGPortMap.put("5", "OSC");
/*      */ 
/*  398 */     CWR8PortMap.put("1", "SIG");
/*  399 */     CWR8PortMap.put("2", "THRU");
/*  400 */     CWR8PortMap.put("3", "OMD");
/*  401 */     CWR8PortMap.put("4", "CLS1");
/*  402 */     CWR8PortMap.put("5", "CLS2");
/*  403 */     CWR8PortMap.put("6", "CLS3");
/*  404 */     CWR8PortMap.put("7", "CLS4");
/*  405 */     CWR8PortMap.put("8", "CLS5");
/*  406 */     CWR8PortMap.put("9", "CLS6");
/*  407 */     CWR8PortMap.put("10", "CLS7");
/*  408 */     CWR8PortMap.put("11", "CLS8");
/*      */ 
/*  413 */     CWR8_88PortMap.put("1", "SIG");
/*  414 */     CWR8_88PortMap.put("2", "THRU");
/*  415 */     CWR8_88PortMap.put("3", "OMD");
/*  416 */     CWR8_88PortMap.put("4", "CLS1");
/*  417 */     CWR8_88PortMap.put("5", "CLS2");
/*  418 */     CWR8_88PortMap.put("6", "CLS3");
/*  419 */     CWR8_88PortMap.put("7", "CLS4");
/*  420 */     CWR8_88PortMap.put("8", "CLS5");
/*  421 */     CWR8_88PortMap.put("9", "CLS6");
/*  422 */     CWR8_88PortMap.put("10", "CLS7");
/*  423 */     CWR8_88PortMap.put("11", "CLS8");
/*      */ 
/*  428 */     ALPFGTPortMap.put("1", "SIG");
/*      */ 
/*  430 */     ALPFGTPortMap.put("3", "OSC");
/*  431 */     ALPFGTPortMap.put("4", "LINE");
/*  432 */     ALPFGTPortMap.put("5", "OSCSFP");
/*      */ 
/*  435 */     A2325APortMap.put("1", "SIG");
/*      */ 
/*  437 */     A2325APortMap.put("3", "DCM");
/*  438 */     A2325APortMap.put("4", "LINE");
/*  439 */     A2325APortMap.put("5", "OSC");
/*      */ 
/*  442 */     OSCTPortMap.put("1", "SIG");
/*      */ 
/*  444 */     OSCTPortMap.put("3", "OSC");
/*  445 */     OSCTPortMap.put("4", "LINE");
/*  446 */     OSCTPortMap.put("5", "OSCSFP");
/*      */ 
/*  448 */     OPSAPortMap.put("1", "SIG");
/*  449 */     OPSAPortMap.put("2", "A");
/*  450 */     OPSAPortMap.put("3", "B");
/*      */ 
/*  452 */     SVACPortMap.put("1", "L1");
/*  453 */     SVACPortMap.put("2", "C1");
/*      */ 
/*  455 */     SFD5APortMap.put("1", "OMD");
/*  456 */     SFD5APortMap.put("2", "EXP");
/*  457 */     SFD5APortMap.put("3", "9190");
/*  458 */     SFD5APortMap.put("4", "9200");
/*  459 */     SFD5APortMap.put("5", "9210");
/*  460 */     SFD5APortMap.put("6", "9220");
/*  461 */     SFD5APortMap.put("7", "9230");
/*      */ 
/*  463 */     SFD5BPortMap.put("1", "OMD");
/*  464 */     SFD5BPortMap.put("2", "EXP");
/*  465 */     SFD5BPortMap.put("3", "9240");
/*  466 */     SFD5BPortMap.put("4", "9250");
/*  467 */     SFD5BPortMap.put("5", "9260");
/*  468 */     SFD5BPortMap.put("6", "9270");
/*  469 */     SFD5BPortMap.put("7", "9280");
/*      */ 
/*  471 */     SFD5CPortMap.put("1", "OMD");
/*  472 */     SFD5CPortMap.put("2", "EXP");
/*  473 */     SFD5CPortMap.put("3", "9290");
/*  474 */     SFD5CPortMap.put("4", "9300");
/*  475 */     SFD5CPortMap.put("5", "9310");
/*  476 */     SFD5CPortMap.put("6", "9320");
/*  477 */     SFD5CPortMap.put("7", "9330");
/*      */ 
/*  479 */     SFD5DPortMap.put("1", "OMD");
/*  480 */     SFD5DPortMap.put("2", "EXP");
/*  481 */     SFD5DPortMap.put("3", "9340");
/*  482 */     SFD5DPortMap.put("4", "9350");
/*  483 */     SFD5DPortMap.put("5", "9360");
/*  484 */     SFD5DPortMap.put("6", "9370");
/*  485 */     SFD5DPortMap.put("7", "9380");
/*      */ 
/*  487 */     SFD5EPortMap.put("1", "OMD");
/*  488 */     SFD5EPortMap.put("2", "EXP");
/*  489 */     SFD5EPortMap.put("3", "9410");
/*  490 */     SFD5EPortMap.put("4", "9420");
/*  491 */     SFD5EPortMap.put("5", "9430");
/*  492 */     SFD5EPortMap.put("6", "9440");
/*  493 */     SFD5EPortMap.put("7", "9450");
/*      */ 
/*  495 */     SFD5FPortMap.put("1", "OMD");
/*  496 */     SFD5FPortMap.put("2", "EXP");
/*  497 */     SFD5FPortMap.put("3", "9460");
/*  498 */     SFD5FPortMap.put("4", "9470");
/*  499 */     SFD5FPortMap.put("5", "9480");
/*  500 */     SFD5FPortMap.put("6", "9490");
/*  501 */     SFD5FPortMap.put("7", "9500");
/*      */ 
/*  503 */     SFD5GPortMap.put("1", "OMD");
/*  504 */     SFD5GPortMap.put("2", "EXP");
/*  505 */     SFD5GPortMap.put("3", "9510");
/*  506 */     SFD5GPortMap.put("4", "9520");
/*  507 */     SFD5GPortMap.put("5", "9530");
/*  508 */     SFD5GPortMap.put("6", "9540");
/*  509 */     SFD5GPortMap.put("7", "9550");
/*      */ 
/*  511 */     SFD5HPortMap.put("1", "OMD");
/*  512 */     SFD5HPortMap.put("2", "EXP");
/*  513 */     SFD5HPortMap.put("3", "9560");
/*  514 */     SFD5HPortMap.put("4", "9570");
/*  515 */     SFD5HPortMap.put("5", "9580");
/*  516 */     SFD5HPortMap.put("6", "9590");
/*  517 */     SFD5HPortMap.put("7", "9600");
/*      */ 
/*  519 */     SFD10APortMap.put("1", "OMD");
/*  520 */     SFD10APortMap.put("2", "EXP");
/*  521 */     SFD10APortMap.put("3", "9190");
/*  522 */     SFD10APortMap.put("4", "9200");
/*  523 */     SFD10APortMap.put("5", "9210");
/*  524 */     SFD10APortMap.put("6", "9220");
/*  525 */     SFD10APortMap.put("7", "9230");
/*  526 */     SFD10APortMap.put("8", "9240");
/*  527 */     SFD10APortMap.put("9", "9250");
/*  528 */     SFD10APortMap.put("10", "9260");
/*  529 */     SFD10APortMap.put("11", "9270");
/*  530 */     SFD10APortMap.put("12", "9280");
/*      */ 
/*  532 */     SFD10BPortMap.put("1", "OMD");
/*  533 */     SFD10BPortMap.put("2", "EXP");
/*  534 */     SFD10BPortMap.put("3", "9290");
/*  535 */     SFD10BPortMap.put("4", "9300");
/*  536 */     SFD10BPortMap.put("5", "9310");
/*  537 */     SFD10BPortMap.put("6", "9320");
/*  538 */     SFD10BPortMap.put("7", "9330");
/*  539 */     SFD10BPortMap.put("8", "9340");
/*  540 */     SFD10BPortMap.put("9", "9350");
/*  541 */     SFD10BPortMap.put("10", "9360");
/*  542 */     SFD10BPortMap.put("11", "9370");
/*  543 */     SFD10BPortMap.put("12", "9380");
/*      */ 
/*  545 */     SFD10CPortMap.put("1", "OMD");
/*  546 */     SFD10CPortMap.put("2", "EXP");
/*  547 */     SFD10CPortMap.put("3", "9410");
/*  548 */     SFD10CPortMap.put("4", "9420");
/*  549 */     SFD10CPortMap.put("5", "9430");
/*  550 */     SFD10CPortMap.put("6", "9440");
/*  551 */     SFD10CPortMap.put("7", "9450");
/*  552 */     SFD10CPortMap.put("8", "9460");
/*  553 */     SFD10CPortMap.put("9", "9470");
/*  554 */     SFD10CPortMap.put("10", "9480");
/*  555 */     SFD10CPortMap.put("11", "9490");
/*  556 */     SFD10CPortMap.put("12", "9500");
/*      */ 
/*  558 */     SFD10DPortMap.put("1", "OMD");
/*  559 */     SFD10DPortMap.put("2", "EXP");
/*  560 */     SFD10DPortMap.put("3", "9510");
/*  561 */     SFD10DPortMap.put("4", "9520");
/*  562 */     SFD10DPortMap.put("5", "9530");
/*  563 */     SFD10DPortMap.put("6", "9540");
/*  564 */     SFD10DPortMap.put("7", "9550");
/*  565 */     SFD10DPortMap.put("8", "9560");
/*  566 */     SFD10DPortMap.put("9", "9570");
/*  567 */     SFD10DPortMap.put("10", "9580");
/*  568 */     SFD10DPortMap.put("11", "9590");
/*  569 */     SFD10DPortMap.put("12", "9600");
/*      */ 
/*  571 */     SFD44PortMap.put("1", "9600");
/*  572 */     SFD44PortMap.put("2", "9590");
/*  573 */     SFD44PortMap.put("3", "9580");
/*  574 */     SFD44PortMap.put("4", "9570");
/*  575 */     SFD44PortMap.put("5", "9560");
/*  576 */     SFD44PortMap.put("6", "9550");
/*  577 */     SFD44PortMap.put("7", "9540");
/*  578 */     SFD44PortMap.put("8", "9530");
/*  579 */     SFD44PortMap.put("9", "9520");
/*  580 */     SFD44PortMap.put("10", "9510");
/*  581 */     SFD44PortMap.put("11", "9500");
/*  582 */     SFD44PortMap.put("12", "9490");
/*  583 */     SFD44PortMap.put("13", "9480");
/*  584 */     SFD44PortMap.put("14", "9470");
/*  585 */     SFD44PortMap.put("15", "9460");
/*  586 */     SFD44PortMap.put("16", "9450");
/*  587 */     SFD44PortMap.put("17", "9440");
/*  588 */     SFD44PortMap.put("18", "9430");
/*  589 */     SFD44PortMap.put("19", "9420");
/*  590 */     SFD44PortMap.put("20", "9410");
/*  591 */     SFD44PortMap.put("21", "9400");
/*  592 */     SFD44PortMap.put("22", "9390");
/*  593 */     SFD44PortMap.put("23", "9380");
/*  594 */     SFD44PortMap.put("24", "9370");
/*  595 */     SFD44PortMap.put("25", "9360");
/*  596 */     SFD44PortMap.put("26", "9350");
/*  597 */     SFD44PortMap.put("27", "9340");
/*  598 */     SFD44PortMap.put("28", "9330");
/*  599 */     SFD44PortMap.put("29", "9320");
/*  600 */     SFD44PortMap.put("30", "9310");
/*  601 */     SFD44PortMap.put("31", "9300");
/*  602 */     SFD44PortMap.put("32", "9290");
/*  603 */     SFD44PortMap.put("33", "9280");
/*  604 */     SFD44PortMap.put("34", "9270");
/*  605 */     SFD44PortMap.put("35", "9260");
/*  606 */     SFD44PortMap.put("36", "9250");
/*  607 */     SFD44PortMap.put("37", "9240");
/*  608 */     SFD44PortMap.put("38", "9230");
/*  609 */     SFD44PortMap.put("39", "9220");
/*  610 */     SFD44PortMap.put("40", "9210");
/*  611 */     SFD44PortMap.put("41", "9200");
/*  612 */     SFD44PortMap.put("42", "9190");
/*  613 */     SFD44PortMap.put("43", "9180");
/*  614 */     SFD44PortMap.put("44", "9170");
/*  615 */     SFD44PortMap.put("45", "OMD");
/*      */ 
/*  618 */     SFD44BPortMap.put("1", "9605");
/*  619 */     SFD44BPortMap.put("2", "9595");
/*  620 */     SFD44BPortMap.put("3", "9585");
/*  621 */     SFD44BPortMap.put("4", "9575");
/*  622 */     SFD44BPortMap.put("5", "9565");
/*  623 */     SFD44BPortMap.put("6", "9555");
/*  624 */     SFD44BPortMap.put("7", "9545");
/*  625 */     SFD44BPortMap.put("8", "9535");
/*  626 */     SFD44BPortMap.put("9", "9525");
/*  627 */     SFD44BPortMap.put("10", "9515");
/*  628 */     SFD44BPortMap.put("11", "9505");
/*  629 */     SFD44BPortMap.put("12", "9495");
/*  630 */     SFD44BPortMap.put("13", "9485");
/*  631 */     SFD44BPortMap.put("14", "9475");
/*  632 */     SFD44BPortMap.put("15", "9465");
/*  633 */     SFD44BPortMap.put("16", "9455");
/*  634 */     SFD44BPortMap.put("17", "9445");
/*  635 */     SFD44BPortMap.put("18", "9435");
/*  636 */     SFD44BPortMap.put("19", "9425");
/*  637 */     SFD44BPortMap.put("20", "9415");
/*  638 */     SFD44BPortMap.put("21", "9405");
/*  639 */     SFD44BPortMap.put("22", "9395");
/*  640 */     SFD44BPortMap.put("23", "9385");
/*  641 */     SFD44BPortMap.put("24", "9375");
/*  642 */     SFD44BPortMap.put("25", "9365");
/*  643 */     SFD44BPortMap.put("26", "9355");
/*  644 */     SFD44BPortMap.put("27", "9345");
/*  645 */     SFD44BPortMap.put("28", "9335");
/*  646 */     SFD44BPortMap.put("29", "9325");
/*  647 */     SFD44BPortMap.put("30", "9315");
/*  648 */     SFD44BPortMap.put("31", "9305");
/*  649 */     SFD44BPortMap.put("32", "9295");
/*  650 */     SFD44BPortMap.put("33", "9285");
/*  651 */     SFD44BPortMap.put("34", "9275");
/*  652 */     SFD44BPortMap.put("35", "9265");
/*  653 */     SFD44BPortMap.put("36", "9255");
/*  654 */     SFD44BPortMap.put("37", "9245");
/*  655 */     SFD44BPortMap.put("38", "9235");
/*  656 */     SFD44BPortMap.put("39", "9225");
/*  657 */     SFD44BPortMap.put("40", "9215");
/*  658 */     SFD44BPortMap.put("41", "9205");
/*  659 */     SFD44BPortMap.put("42", "9195");
/*  660 */     SFD44BPortMap.put("43", "9185");
/*  661 */     SFD44BPortMap.put("44", "9175");
/*  662 */     SFD44BPortMap.put("45", "OMD");
/*      */ 
/*  665 */     SFD40PortMap.put("1", "9600");
/*  666 */     SFD40PortMap.put("2", "9590");
/*  667 */     SFD40PortMap.put("3", "9580");
/*  668 */     SFD40PortMap.put("4", "9570");
/*  669 */     SFD40PortMap.put("5", "9560");
/*  670 */     SFD40PortMap.put("6", "9550");
/*  671 */     SFD40PortMap.put("7", "9540");
/*  672 */     SFD40PortMap.put("8", "9530");
/*  673 */     SFD40PortMap.put("9", "9520");
/*  674 */     SFD40PortMap.put("10", "9510");
/*  675 */     SFD40PortMap.put("11", "9500");
/*  676 */     SFD40PortMap.put("12", "9490");
/*  677 */     SFD40PortMap.put("13", "9480");
/*  678 */     SFD40PortMap.put("14", "9470");
/*  679 */     SFD40PortMap.put("15", "9460");
/*  680 */     SFD40PortMap.put("16", "9450");
/*  681 */     SFD40PortMap.put("17", "9440");
/*  682 */     SFD40PortMap.put("18", "9430");
/*  683 */     SFD40PortMap.put("19", "9420");
/*  684 */     SFD40PortMap.put("20", "9410");
/*  685 */     SFD40PortMap.put("21", "9400");
/*  686 */     SFD40PortMap.put("22", "9390");
/*  687 */     SFD40PortMap.put("23", "9380");
/*  688 */     SFD40PortMap.put("24", "9370");
/*  689 */     SFD40PortMap.put("25", "9360");
/*  690 */     SFD40PortMap.put("26", "9350");
/*  691 */     SFD40PortMap.put("27", "9340");
/*  692 */     SFD40PortMap.put("28", "9330");
/*  693 */     SFD40PortMap.put("29", "9320");
/*  694 */     SFD40PortMap.put("30", "9310");
/*  695 */     SFD40PortMap.put("31", "9300");
/*  696 */     SFD40PortMap.put("32", "9290");
/*  697 */     SFD40PortMap.put("33", "9280");
/*  698 */     SFD40PortMap.put("34", "9270");
/*  699 */     SFD40PortMap.put("35", "9260");
/*  700 */     SFD40PortMap.put("36", "9250");
/*  701 */     SFD40PortMap.put("37", "9240");
/*  702 */     SFD40PortMap.put("38", "9230");
/*  703 */     SFD40PortMap.put("39", "9220");
/*  704 */     SFD40PortMap.put("40", "9210");
/*  705 */     SFD40PortMap.put("41", "OMD");
/*      */ 
/*  708 */     SFD40BPortMap.put("1", "9605");
/*  709 */     SFD40BPortMap.put("2", "9595");
/*  710 */     SFD40BPortMap.put("3", "9585");
/*  711 */     SFD40BPortMap.put("4", "9575");
/*  712 */     SFD40BPortMap.put("5", "9565");
/*  713 */     SFD40BPortMap.put("6", "9555");
/*  714 */     SFD40BPortMap.put("7", "9545");
/*  715 */     SFD40BPortMap.put("8", "9535");
/*  716 */     SFD40BPortMap.put("9", "9525");
/*  717 */     SFD40BPortMap.put("10", "9515");
/*  718 */     SFD40BPortMap.put("11", "9505");
/*  719 */     SFD40BPortMap.put("12", "9495");
/*  720 */     SFD40BPortMap.put("13", "9485");
/*  721 */     SFD40BPortMap.put("14", "9475");
/*  722 */     SFD40BPortMap.put("15", "9465");
/*  723 */     SFD40BPortMap.put("16", "9455");
/*  724 */     SFD40BPortMap.put("17", "9445");
/*  725 */     SFD40BPortMap.put("18", "9435");
/*  726 */     SFD40BPortMap.put("19", "9425");
/*  727 */     SFD40BPortMap.put("20", "9415");
/*  728 */     SFD40BPortMap.put("21", "9405");
/*  729 */     SFD40BPortMap.put("22", "9395");
/*  730 */     SFD40BPortMap.put("23", "9385");
/*  731 */     SFD40BPortMap.put("24", "9375");
/*  732 */     SFD40BPortMap.put("25", "9365");
/*  733 */     SFD40BPortMap.put("26", "9355");
/*  734 */     SFD40BPortMap.put("27", "9345");
/*  735 */     SFD40BPortMap.put("28", "9335");
/*  736 */     SFD40BPortMap.put("29", "9325");
/*  737 */     SFD40BPortMap.put("30", "9315");
/*  738 */     SFD40BPortMap.put("31", "9305");
/*  739 */     SFD40BPortMap.put("32", "9295");
/*  740 */     SFD40BPortMap.put("33", "9285");
/*  741 */     SFD40BPortMap.put("34", "9275");
/*  742 */     SFD40BPortMap.put("35", "9265");
/*  743 */     SFD40BPortMap.put("36", "9255");
/*  744 */     SFD40BPortMap.put("37", "9245");
/*  745 */     SFD40BPortMap.put("38", "9235");
/*  746 */     SFD40BPortMap.put("39", "9225");
/*  747 */     SFD40BPortMap.put("40", "9215");
/*  748 */     SFD40BPortMap.put("41", "OMD");
/*      */ 
/*  750 */     SFC8PortMap.put("1", "OMD");
/*  751 */     SFC8PortMap.put("2", "1471");
/*  752 */     SFC8PortMap.put("3", "1491");
/*  753 */     SFC8PortMap.put("4", "1511");
/*  754 */     SFC8PortMap.put("5", "1531");
/*  755 */     SFC8PortMap.put("6", "1551");
/*  756 */     SFC8PortMap.put("7", "1571");
/*  757 */     SFC8PortMap.put("8", "1591");
/*  758 */     SFC8PortMap.put("9", "1611");
/*      */ 
/*  760 */     SFC4APortMap.put("1", "OMD");
/*  761 */     SFC4APortMap.put("2", "EXP");
/*  762 */     SFC4APortMap.put("3", "1471");
/*  763 */     SFC4APortMap.put("4", "1491");
/*  764 */     SFC4APortMap.put("5", "1511");
/*  765 */     SFC4APortMap.put("6", "1531");
/*  766 */     SFC4BPortMap.put("1", "OMD");
/*  767 */     SFC4BPortMap.put("2", "EXP");
/*  768 */     SFC4BPortMap.put("3", "1551");
/*  769 */     SFC4BPortMap.put("4", "1571");
/*  770 */     SFC4BPortMap.put("5", "1591");
/*  771 */     SFC4BPortMap.put("6", "1611");
/*      */ 
/*  773 */     SFC2APortMap.put("1", "OMD");
/*  774 */     SFC2APortMap.put("2", "EXP");
/*  775 */     SFC2APortMap.put("3", "1471");
/*  776 */     SFC2APortMap.put("4", "1491");
/*      */ 
/*  778 */     SFC2BPortMap.put("1", "OMD");
/*  779 */     SFC2BPortMap.put("2", "EXP");
/*  780 */     SFC2BPortMap.put("3", "1511");
/*  781 */     SFC2BPortMap.put("4", "1531");
/*      */ 
/*  783 */     SFC2CPortMap.put("1", "OMD");
/*  784 */     SFC2CPortMap.put("2", "EXP");
/*  785 */     SFC2CPortMap.put("3", "1551");
/*  786 */     SFC2CPortMap.put("4", "1571");
/*      */ 
/*  788 */     SFC2DPortMap.put("1", "OMD");
/*  789 */     SFC2DPortMap.put("2", "EXP");
/*  790 */     SFC2DPortMap.put("3", "1591");
/*  791 */     SFC2DPortMap.put("4", "1611");
/*      */ 
/*  793 */     SFC1APortMap.put("1", "OMD");
/*  794 */     SFC1APortMap.put("2", "EXP");
/*  795 */     SFC1APortMap.put("3", "1471");
/*      */ 
/*  797 */     SFC1BPortMap.put("1", "OMD");
/*  798 */     SFC1BPortMap.put("2", "EXP");
/*  799 */     SFC1BPortMap.put("3", "1491");
/*      */ 
/*  801 */     SFC1CPortMap.put("1", "OMD");
/*  802 */     SFC1CPortMap.put("2", "EXP");
/*  803 */     SFC1CPortMap.put("3", "1511");
/*      */ 
/*  805 */     SFC1DPortMap.put("1", "OMD");
/*  806 */     SFC1DPortMap.put("2", "EXP");
/*  807 */     SFC1DPortMap.put("3", "1531");
/*      */ 
/*  809 */     SFC1EPortMap.put("1", "OMD");
/*  810 */     SFC1EPortMap.put("2", "EXP");
/*  811 */     SFC1EPortMap.put("3", "1551");
/*      */ 
/*  813 */     SFC1FPortMap.put("1", "OMD");
/*  814 */     SFC1FPortMap.put("2", "EXP");
/*  815 */     SFC1FPortMap.put("3", "1571");
/*      */ 
/*  817 */     SFC1GPortMap.put("1", "OMD");
/*  818 */     SFC1GPortMap.put("2", "EXP");
/*  819 */     SFC1GPortMap.put("3", "1591");
/*      */ 
/*  821 */     SFC1HPortMap.put("1", "OMD");
/*  822 */     SFC1HPortMap.put("2", "EXP");
/*  823 */     SFC1HPortMap.put("3", "1611");
/*      */ 
/*  826 */     PSS1GBEPortMap.put("1", "L1");
/*  827 */     PSS1GBEPortMap.put("2", "L2");
/*  828 */     PSS1GBEPortMap.put("3", "C1");
/*  829 */     PSS1GBEPortMap.put("4", "C2");
/*  830 */     PSS1GBEPortMap.put("5", "C3");
/*  831 */     PSS1GBEPortMap.put("6", "C4");
/*  832 */     PSS1GBEPortMap.put("7", "C5");
/*  833 */     PSS1GBEPortMap.put("8", "C6");
/*  834 */     PSS1GBEPortMap.put("9", "C7");
/*  835 */     PSS1GBEPortMap.put("10", "C8");
/*  836 */     PSS1GBEPortMap.put("11", "C9");
/*  837 */     PSS1GBEPortMap.put("12", "C10");
/*  838 */     PSS1GBEPortMap.put("13", "C11");
/*  839 */     PSS1GBEPortMap.put("14", "C12");
/*      */ 
/*  842 */     PSS1MD4PortMap.put("1", "L1");
/*  843 */     PSS1MD4PortMap.put("2", "L2");
/*  844 */     PSS1MD4PortMap.put("3", "C1");
/*  845 */     PSS1MD4PortMap.put("4", "C2");
/*  846 */     PSS1MD4PortMap.put("5", "C3");
/*  847 */     PSS1MD4PortMap.put("6", "C4");
/*  848 */     PSS1MD4PortMap.put("7", "VA1");
/*  849 */     PSS1MD4PortMap.put("8", "VA2");
/*      */ 
/*  851 */     _11STMM10PortMap.put("1", "L1");
/*  852 */     _11STMM10PortMap.put("2", "C1");
/*  853 */     _11STMM10PortMap.put("3", "C2");
/*  854 */     _11STMM10PortMap.put("4", "C3");
/*  855 */     _11STMM10PortMap.put("5", "C4");
/*  856 */     _11STMM10PortMap.put("6", "C5");
/*  857 */     _11STMM10PortMap.put("7", "C6");
/*  858 */     _11STMM10PortMap.put("8", "C7");
/*  859 */     _11STMM10PortMap.put("9", "C8");
/*  860 */     _11STMM10PortMap.put("10", "C9");
/*  861 */     _11STMM10PortMap.put("11", "C10");
/*      */ 
/*  863 */     _11STAR1PortMap.put("1", "L1");
/*  864 */     _11STAR1PortMap.put("2", "C1");
/*      */ 
/*  866 */     _11STGE12PortMap.put("1", "L1");
/*  867 */     _11STGE12PortMap.put("2", "C1");
/*  868 */     _11STGE12PortMap.put("3", "C2");
/*  869 */     _11STGE12PortMap.put("4", "C3");
/*  870 */     _11STGE12PortMap.put("5", "C4");
/*  871 */     _11STGE12PortMap.put("6", "C5");
/*  872 */     _11STGE12PortMap.put("7", "C6");
/*  873 */     _11STGE12PortMap.put("8", "C7");
/*  874 */     _11STGE12PortMap.put("9", "C8");
/*  875 */     _11STGE12PortMap.put("10", "C9");
/*  876 */     _11STGE12PortMap.put("11", "C10");
/*      */ 
/*  882 */     _11DPGE12PortMap.put("1", "L1");
/*  883 */     _11DPGE12PortMap.put("2", "L2");
/*  884 */     _11DPGE12PortMap.put("3", "C1");
/*  885 */     _11DPGE12PortMap.put("4", "C2");
/*  886 */     _11DPGE12PortMap.put("5", "C3");
/*  887 */     _11DPGE12PortMap.put("6", "C4");
/*  888 */     _11DPGE12PortMap.put("7", "C5");
/*  889 */     _11DPGE12PortMap.put("8", "C6");
/*  890 */     _11DPGE12PortMap.put("9", "C7");
/*  891 */     _11DPGE12PortMap.put("10", "C8");
/*  892 */     _11DPGE12PortMap.put("11", "C9");
/*  893 */     _11DPGE12PortMap.put("12", "C10");
/*  894 */     _11DPGE12PortMap.put("13", "C11");
/*  895 */     _11DPGE12PortMap.put("14", "C12");
/*      */ 
/*  898 */     _43STX4PortMap.put("1", "L1");
/*  899 */     _43STX4PortMap.put("2", "C1");
/*  900 */     _43STX4PortMap.put("3", "C2");
/*  901 */     _43STX4PortMap.put("4", "C3");
/*  902 */     _43STX4PortMap.put("5", "C4");
/*      */ 
/*  905 */     _4DPA4PortMap.put("1", "L1");
/*  906 */     _4DPA4PortMap.put("2", "L2");
/*  907 */     _4DPA4PortMap.put("3", "C1");
/*  908 */     _4DPA4PortMap.put("4", "C2");
/*  909 */     _4DPA4PortMap.put("5", "C3");
/*  910 */     _4DPA4PortMap.put("6", "C4");
/*  911 */     _4DPA4PortMap.put("7", "VA1");
/*  912 */     _4DPA4PortMap.put("8", "VA2");
/*      */ 
/*  915 */     _11QPA4PortMap.put("1", "L1");
/*  916 */     _11QPA4PortMap.put("2", "L2");
/*  917 */     _11QPA4PortMap.put("3", "L3");
/*  918 */     _11QPA4PortMap.put("4", "L4");
/*  919 */     _11QPA4PortMap.put("5", "C1");
/*  920 */     _11QPA4PortMap.put("6", "C2");
/*  921 */     _11QPA4PortMap.put("7", "C3");
/*  922 */     _11QPA4PortMap.put("8", "C4");
/*  923 */     _11QPA4PortMap.put("9", "VA1");
/*  924 */     _11QPA4PortMap.put("10", "VA2");
/*  925 */     _11QPA4PortMap.put("11", "VA3");
/*  926 */     _11QPA4PortMap.put("12", "VA4");
/*      */ 
/*  929 */     _11DPE12PortMap.put("1", "L1");
/*  930 */     _11DPE12PortMap.put("2", "L2");
/*  931 */     _11DPE12PortMap.put("3", "C1");
/*  932 */     _11DPE12PortMap.put("4", "C2");
/*  933 */     _11DPE12PortMap.put("5", "C3");
/*  934 */     _11DPE12PortMap.put("6", "C4");
/*  935 */     _11DPE12PortMap.put("7", "C5");
/*  936 */     _11DPE12PortMap.put("8", "C6");
/*  937 */     _11DPE12PortMap.put("9", "C7");
/*  938 */     _11DPE12PortMap.put("10", "C8");
/*  939 */     _11DPE12PortMap.put("11", "C9");
/*  940 */     _11DPE12PortMap.put("12", "C10");
/*  941 */     _11DPE12PortMap.put("13", "C11");
/*  942 */     _11DPE12PortMap.put("14", "C12");
/*  943 */     _11DPE12PortMap.put("15", "VA1");
/*  944 */     _11DPE12PortMap.put("16", "VA2");
/*      */ 
/*  947 */     _43STA1PPortMap.put("1", "L1");
/*  948 */     _43STA1PPortMap.put("2", "C1");
/*      */ 
/*  951 */     _43STX4PPortMap.put("1", "L1");
/*  952 */     _43STX4PPortMap.put("2", "C1");
/*  953 */     _43STX4PPortMap.put("3", "C2");
/*  954 */     _43STX4PPortMap.put("4", "C3");
/*  955 */     _43STX4PPortMap.put("5", "C4");
/*      */ 
/*  958 */     _4DPA2PortMap.put("1", "L1");
/*  959 */     _4DPA2PortMap.put("2", "L2");
/*  960 */     _4DPA2PortMap.put("3", "C1");
/*  961 */     _4DPA2PortMap.put("4", "C2");
/*      */ 
/*  965 */     SFD8APortMap.put("1", "OMD");
/*  966 */     SFD8APortMap.put("2", "EXP");
/*  967 */     SFD8APortMap.put("3", "9200");
/*  968 */     SFD8APortMap.put("4", "9210");
/*  969 */     SFD8APortMap.put("5", "9220");
/*  970 */     SFD8APortMap.put("6", "9230");
/*  971 */     SFD8APortMap.put("7", "9250");
/*  972 */     SFD8APortMap.put("8", "9260");
/*  973 */     SFD8APortMap.put("9", "9270");
/*  974 */     SFD8APortMap.put("10", "9280");
/*      */ 
/*  976 */     SFD8BPortMap.put("1", "OMD");
/*  977 */     SFD8BPortMap.put("2", "EXP");
/*  978 */     SFD8BPortMap.put("3", "9300");
/*  979 */     SFD8BPortMap.put("4", "9310");
/*  980 */     SFD8BPortMap.put("5", "9320");
/*  981 */     SFD8BPortMap.put("6", "9330");
/*  982 */     SFD8BPortMap.put("7", "9350");
/*  983 */     SFD8BPortMap.put("8", "9360");
/*  984 */     SFD8BPortMap.put("9", "9370");
/*  985 */     SFD8BPortMap.put("10", "9380");
/*      */ 
/*  987 */     SFD8CPortMap.put("1", "OMD");
/*  988 */     SFD8CPortMap.put("2", "EXP");
/*  989 */     SFD8CPortMap.put("3", "9420");
/*  990 */     SFD8CPortMap.put("4", "9430");
/*  991 */     SFD8CPortMap.put("5", "9440");
/*  992 */     SFD8CPortMap.put("6", "9450");
/*  993 */     SFD8CPortMap.put("7", "9470");
/*  994 */     SFD8CPortMap.put("8", "9480");
/*  995 */     SFD8CPortMap.put("9", "9490");
/*  996 */     SFD8CPortMap.put("10", "9500");
/*      */ 
/*  998 */     SFD8DPortMap.put("1", "OMD");
/*  999 */     SFD8DPortMap.put("2", "EXP");
/* 1000 */     SFD8DPortMap.put("3", "9520");
/* 1001 */     SFD8DPortMap.put("4", "9530");
/* 1002 */     SFD8DPortMap.put("5", "9540");
/* 1003 */     SFD8DPortMap.put("6", "9550");
/* 1004 */     SFD8DPortMap.put("7", "9570");
/* 1005 */     SFD8DPortMap.put("8", "9580");
/* 1006 */     SFD8DPortMap.put("9", "9590");
/* 1007 */     SFD8DPortMap.put("10", "9600");
/*      */ 
/* 1009 */     PMDCLPortMap.put("1", "L1");
/*      */ 
/* 1011 */     SFD44BLPortMap.put("1", "9085");
/* 1012 */     SFD44BLPortMap.put("2", "9075");
/* 1013 */     SFD44BLPortMap.put("3", "9065");
/* 1014 */     SFD44BLPortMap.put("4", "9055");
/* 1015 */     SFD44BLPortMap.put("5", "9045");
/* 1016 */     SFD44BLPortMap.put("6", "9035");
/* 1017 */     SFD44BLPortMap.put("7", "9025");
/* 1018 */     SFD44BLPortMap.put("8", "9015");
/* 1019 */     SFD44BLPortMap.put("9", "9005");
/* 1020 */     SFD44BLPortMap.put("10", "8995");
/* 1021 */     SFD44BLPortMap.put("11", "8985");
/* 1022 */     SFD44BLPortMap.put("12", "8975");
/* 1023 */     SFD44BLPortMap.put("13", "8965");
/* 1024 */     SFD44BLPortMap.put("14", "8955");
/* 1025 */     SFD44BLPortMap.put("15", "8945");
/* 1026 */     SFD44BLPortMap.put("16", "8935");
/* 1027 */     SFD44BLPortMap.put("17", "8925");
/* 1028 */     SFD44BLPortMap.put("18", "8915");
/* 1029 */     SFD44BLPortMap.put("19", "8905");
/* 1030 */     SFD44BLPortMap.put("20", "8895");
/* 1031 */     SFD44BLPortMap.put("21", "8885");
/* 1032 */     SFD44BLPortMap.put("22", "8875");
/* 1033 */     SFD44BLPortMap.put("23", "8865");
/* 1034 */     SFD44BLPortMap.put("24", "8855");
/* 1035 */     SFD44BLPortMap.put("25", "8845");
/* 1036 */     SFD44BLPortMap.put("26", "8835");
/* 1037 */     SFD44BLPortMap.put("27", "8825");
/* 1038 */     SFD44BLPortMap.put("28", "8815");
/* 1039 */     SFD44BLPortMap.put("29", "8805");
/* 1040 */     SFD44BLPortMap.put("30", "8795");
/* 1041 */     SFD44BLPortMap.put("31", "8785");
/* 1042 */     SFD44BLPortMap.put("32", "8775");
/* 1043 */     SFD44BLPortMap.put("33", "8765");
/* 1044 */     SFD44BLPortMap.put("34", "8755");
/* 1045 */     SFD44BLPortMap.put("35", "8745");
/* 1046 */     SFD44BLPortMap.put("36", "8735");
/* 1047 */     SFD44BLPortMap.put("37", "8725");
/* 1048 */     SFD44BLPortMap.put("38", "8715");
/* 1049 */     SFD44BLPortMap.put("39", "8705");
/* 1050 */     SFD44BLPortMap.put("40", "8695");
/* 1051 */     SFD44BLPortMap.put("41", "8685");
/* 1052 */     SFD44BLPortMap.put("42", "8675");
/* 1053 */     SFD44BLPortMap.put("43", "8665");
/* 1054 */     SFD44BLPortMap.put("44", "8655");
/* 1055 */     SFD44BLPortMap.put("45", "OMD");
/*      */ 
/* 1058 */     SFD44LPortMap.put("1", "9080");
/* 1059 */     SFD44LPortMap.put("2", "9070");
/* 1060 */     SFD44LPortMap.put("3", "9060");
/* 1061 */     SFD44LPortMap.put("4", "9050");
/* 1062 */     SFD44LPortMap.put("5", "9040");
/* 1063 */     SFD44LPortMap.put("6", "9030");
/* 1064 */     SFD44LPortMap.put("7", "9020");
/* 1065 */     SFD44LPortMap.put("8", "9010");
/* 1066 */     SFD44LPortMap.put("9", "9000");
/* 1067 */     SFD44LPortMap.put("10", "8990");
/* 1068 */     SFD44LPortMap.put("11", "8980");
/* 1069 */     SFD44LPortMap.put("12", "8970");
/* 1070 */     SFD44LPortMap.put("13", "8960");
/* 1071 */     SFD44LPortMap.put("14", "8950");
/* 1072 */     SFD44LPortMap.put("15", "8940");
/* 1073 */     SFD44LPortMap.put("16", "8930");
/* 1074 */     SFD44LPortMap.put("17", "8920");
/* 1075 */     SFD44LPortMap.put("18", "8910");
/* 1076 */     SFD44LPortMap.put("19", "8900");
/* 1077 */     SFD44LPortMap.put("20", "8890");
/* 1078 */     SFD44LPortMap.put("21", "8880");
/* 1079 */     SFD44LPortMap.put("22", "8870");
/* 1080 */     SFD44LPortMap.put("23", "8860");
/* 1081 */     SFD44LPortMap.put("24", "8850");
/* 1082 */     SFD44LPortMap.put("25", "8840");
/* 1083 */     SFD44LPortMap.put("26", "8830");
/* 1084 */     SFD44LPortMap.put("27", "8820");
/* 1085 */     SFD44LPortMap.put("28", "8810");
/* 1086 */     SFD44LPortMap.put("29", "8800");
/* 1087 */     SFD44LPortMap.put("30", "8790");
/* 1088 */     SFD44LPortMap.put("31", "8780");
/* 1089 */     SFD44LPortMap.put("32", "8770");
/* 1090 */     SFD44LPortMap.put("33", "8760");
/* 1091 */     SFD44LPortMap.put("34", "8750");
/* 1092 */     SFD44LPortMap.put("35", "8740");
/* 1093 */     SFD44LPortMap.put("36", "8730");
/* 1094 */     SFD44LPortMap.put("37", "8720");
/* 1095 */     SFD44LPortMap.put("38", "8710");
/* 1096 */     SFD44LPortMap.put("39", "8700");
/* 1097 */     SFD44LPortMap.put("40", "8690");
/* 1098 */     SFD44LPortMap.put("41", "8680");
/* 1099 */     SFD44LPortMap.put("42", "8670");
/* 1100 */     SFD44LPortMap.put("43", "8660");
/* 1101 */     SFD44LPortMap.put("44", "8650");
/* 1102 */     SFD44LPortMap.put("45", "OMD");
/*      */ 
/* 1105 */     _112SCX10PortMap.put("1", "L1");
/* 1106 */     _112SCX10PortMap.put("2", "C1");
/* 1107 */     _112SCX10PortMap.put("3", "C2");
/* 1108 */     _112SCX10PortMap.put("4", "C3");
/* 1109 */     _112SCX10PortMap.put("5", "C4");
/* 1110 */     _112SCX10PortMap.put("6", "C5");
/* 1111 */     _112SCX10PortMap.put("7", "C6");
/* 1112 */     _112SCX10PortMap.put("8", "C7");
/* 1113 */     _112SCX10PortMap.put("9", "C8");
/* 1114 */     _112SCX10PortMap.put("10", "C9");
/* 1115 */     _112SCX10PortMap.put("11", "C10");
/*      */ 
/* 1117 */     _130SNX10PortMap.put("1", "L1");
/* 1118 */     _130SNX10PortMap.put("2", "C1");
/* 1119 */     _130SNX10PortMap.put("3", "C2");
/* 1120 */     _130SNX10PortMap.put("4", "C3");
/* 1121 */     _130SNX10PortMap.put("5", "C4");
/* 1122 */     _130SNX10PortMap.put("6", "C5");
/* 1123 */     _130SNX10PortMap.put("7", "C6");
/* 1124 */     _130SNX10PortMap.put("8", "C7");
/* 1125 */     _130SNX10PortMap.put("9", "C8");
/* 1126 */     _130SNX10PortMap.put("10", "C9");
/* 1127 */     _130SNX10PortMap.put("11", "C10");
/*      */ 
/* 1129 */     _112SCA1PortMap.put("1", "L1");
/* 1130 */     _112SCA1PortMap.put("2", "C1");
/*      */ 
/* 1132 */     _130SCA1PortMap.put("1", "L1");
/* 1133 */     _130SCA1PortMap.put("2", "C1");
/*      */ 
/* 1138 */     AM2017BPortMap.put("1", "SIG");
/*      */ 
/* 1140 */     AM2017BPortMap.put("3", "DCM");
/* 1141 */     AM2017BPortMap.put("4", "LINE");
/* 1142 */     AM2017BPortMap.put("5", "OSC");
/*      */ 
/* 1144 */     AM2325BPortMap.put("1", "SIG");
/*      */ 
/* 1146 */     AM2325BPortMap.put("3", "DCM");
/* 1147 */     AM2325BPortMap.put("4", "LINE");
/* 1148 */     AM2325BPortMap.put("5", "OSC");
/*      */ 
/* 1150 */     WTOCMPortMap.put("1", "IN1");
/* 1151 */     WTOCMPortMap.put("2", "IN2");
/* 1152 */     WTOCMPortMap.put("3", "IN3");
/* 1153 */     WTOCMPortMap.put("4", "IN4");
/*      */ 
/* 1156 */     SFD4APortMap.put("1", "OMD");
/* 1157 */     SFD4APortMap.put("2", "EXP");
/* 1158 */     SFD4APortMap.put("3", "9200");
/* 1159 */     SFD4APortMap.put("4", "9210");
/* 1160 */     SFD4APortMap.put("5", "9220");
/* 1161 */     SFD4APortMap.put("6", "9230");
/*      */ 
/* 1163 */     SFD4BPortMap.put("1", "OMD");
/* 1164 */     SFD4BPortMap.put("2", "EXP");
/* 1165 */     SFD4BPortMap.put("3", "9250");
/* 1166 */     SFD4BPortMap.put("4", "9260");
/* 1167 */     SFD4BPortMap.put("5", "9270");
/* 1168 */     SFD4BPortMap.put("6", "9280");
/*      */ 
/* 1170 */     SFD4CPortMap.put("1", "OMD");
/* 1171 */     SFD4CPortMap.put("2", "EXP");
/* 1172 */     SFD4CPortMap.put("3", "9300");
/* 1173 */     SFD4CPortMap.put("4", "9310");
/* 1174 */     SFD4CPortMap.put("5", "9320");
/* 1175 */     SFD4CPortMap.put("6", "9330");
/*      */ 
/* 1177 */     SFD4DPortMap.put("1", "OMD");
/* 1178 */     SFD4DPortMap.put("2", "EXP");
/* 1179 */     SFD4DPortMap.put("3", "9350");
/* 1180 */     SFD4DPortMap.put("4", "9360");
/* 1181 */     SFD4DPortMap.put("5", "9370");
/* 1182 */     SFD4DPortMap.put("6", "9380");
/*      */ 
/* 1184 */     SFD4EPortMap.put("1", "OMD");
/* 1185 */     SFD4EPortMap.put("2", "EXP");
/* 1186 */     SFD4EPortMap.put("3", "9420");
/* 1187 */     SFD4EPortMap.put("4", "9430");
/* 1188 */     SFD4EPortMap.put("5", "9440");
/* 1189 */     SFD4EPortMap.put("6", "9450");
/*      */ 
/* 1191 */     SFD4FPortMap.put("1", "OMD");
/* 1192 */     SFD4FPortMap.put("2", "EXP");
/* 1193 */     SFD4FPortMap.put("3", "9470");
/* 1194 */     SFD4FPortMap.put("4", "9480");
/* 1195 */     SFD4FPortMap.put("5", "9490");
/* 1196 */     SFD4FPortMap.put("6", "9500");
/*      */ 
/* 1198 */     SFD4GPortMap.put("1", "OMD");
/* 1199 */     SFD4GPortMap.put("2", "EXP");
/* 1200 */     SFD4GPortMap.put("3", "9520");
/* 1201 */     SFD4GPortMap.put("4", "9530");
/* 1202 */     SFD4GPortMap.put("5", "9540");
/* 1203 */     SFD4GPortMap.put("6", "9550");
/*      */ 
/* 1205 */     SFD4HPortMap.put("1", "OMD");
/* 1206 */     SFD4HPortMap.put("2", "EXP");
/* 1207 */     SFD4HPortMap.put("3", "9570");
/* 1208 */     SFD4HPortMap.put("4", "9580");
/* 1209 */     SFD4HPortMap.put("5", "9590");
/* 1210 */     SFD4HPortMap.put("6", "9600");
/*      */ 
/* 1212 */     PSS1P21PortMap.put("1", "L1");
/*      */ 
/* 1214 */     PSS1P21PortMap.put("3", "C1");
/* 1215 */     PSS1P21PortMap.put("4", "C2");
/* 1216 */     PSS1P21PortMap.put("5", "C3");
/* 1217 */     PSS1P21PortMap.put("6", "C4");
/* 1218 */     PSS1P21PortMap.put("7", "C5");
/* 1219 */     PSS1P21PortMap.put("8", "C6");
/* 1220 */     PSS1P21PortMap.put("9", "C7");
/* 1221 */     PSS1P21PortMap.put("10", "C8");
/* 1222 */     PSS1P21PortMap.put("11", "C9");
/* 1223 */     PSS1P21PortMap.put("12", "C10");
/* 1224 */     PSS1P21PortMap.put("13", "C11");
/* 1225 */     PSS1P21PortMap.put("14", "C12");
/* 1226 */     PSS1P21PortMap.put("15", "C13");
/* 1227 */     PSS1P21PortMap.put("16", "C14");
/* 1228 */     PSS1P21PortMap.put("17", "C15");
/* 1229 */     PSS1P21PortMap.put("18", "C16");
/* 1230 */     PSS1P21PortMap.put("19", "C17");
/* 1231 */     PSS1P21PortMap.put("20", "C18");
/* 1232 */     PSS1P21PortMap.put("21", "C19");
/* 1233 */     PSS1P21PortMap.put("22", "C20");
/* 1234 */     PSS1P21PortMap.put("23", "C21");
/*      */ 
/* 1238 */     AM2125APortMap.put("1", "LINEIN");
/* 1239 */     AM2625APortMap.put("1", "LINEIN");
/* 1240 */     AM2032APortMap.put("1", "LINEIN");
/*      */ 
/* 1242 */     AM2125APortMap.put("3", "DCM");
/* 1243 */     AM2125APortMap.put("4", "LINEOUT");
/* 1244 */     AM2125APortMap.put("5", "OSCSFP");
/* 1245 */     AM2125APortMap.put("7", "OSC");
/*      */ 
/* 1247 */     AM2625APortMap.put("3", "DCM");
/* 1248 */     AM2625APortMap.put("4", "LINEOUT");
/* 1249 */     AM2625APortMap.put("5", "OSCSFP");
/* 1250 */     AM2625APortMap.put("7", "OSC");
/*      */ 
/* 1252 */     AM2032APortMap.put("3", "DCM");
/* 1253 */     AM2032APortMap.put("4", "LINEOUT");
/* 1254 */     AM2032APortMap.put("5", "OSCSFP");
/* 1255 */     AM2032APortMap.put("7", "OSC");
/*      */ 
/* 1257 */     RA2PPortMap.put("1", "LINEIN");
/* 1258 */     RA2PPortMap.put("2", "LINEOUT");
/*      */ 
/* 1260 */     MVACPortMap.put("1", "G1");
/* 1261 */     MVACPortMap.put("2", "G2");
/* 1262 */     MVACPortMap.put("3", "G3");
/* 1263 */     MVACPortMap.put("4", "G4");
/* 1264 */     MVACPortMap.put("5", "G5");
/* 1265 */     MVACPortMap.put("6", "G6");
/* 1266 */     MVACPortMap.put("7", "G7");
/* 1267 */     MVACPortMap.put("8", "G8");
/*      */ 
/* 1269 */     _11DPM12PortMap.put("1", "L1");
/* 1270 */     _11DPM12PortMap.put("2", "L2");
/* 1271 */     _11DPM12PortMap.put("3", "C1");
/* 1272 */     _11DPM12PortMap.put("4", "C2");
/* 1273 */     _11DPM12PortMap.put("5", "C3");
/* 1274 */     _11DPM12PortMap.put("6", "C4");
/* 1275 */     _11DPM12PortMap.put("7", "C5");
/* 1276 */     _11DPM12PortMap.put("8", "C6");
/* 1277 */     _11DPM12PortMap.put("9", "C7");
/* 1278 */     _11DPM12PortMap.put("10", "C8");
/* 1279 */     _11DPM12PortMap.put("11", "C9");
/* 1280 */     _11DPM12PortMap.put("12", "C10");
/* 1281 */     _11DPM12PortMap.put("13", "C11");
/* 1282 */     _11DPM12PortMap.put("14", "C12");
/* 1283 */     _11DPM12PortMap.put("15", "VA1");
/* 1284 */     _11DPM12PortMap.put("16", "VA2");
/* 1285 */     _11DPM12PortMap.put("0:1", "ODU1PTF1");
/* 1286 */     _11DPM12PortMap.put("0:2", "ODU1PTF2");
/* 1287 */     _11DPM12PortMap.put("0:3", "ODU1PTF3");
/* 1288 */     _11DPM12PortMap.put("0:4", "ODU1PTF4");
/* 1289 */     _11DPM12PortMap.put("0:5", "ODU1PTF5");
/* 1290 */     _11DPM12PortMap.put("0:6", "ODU1PTF6");
/* 1291 */     _11DPM12PortMap.put("0:7", "ODU1PTF7");
/* 1292 */     _11DPM12PortMap.put("0:8", "ODU1PTF8");
/*      */ 
/* 1295 */     AM2318APortMap.put("1", "LINEIN");
/*      */ 
/* 1297 */     AM2318APortMap.put("3", "OSC");
/* 1298 */     AM2318APortMap.put("4", "LINEOUT");
/* 1299 */     AM2318APortMap.put("5", "OSCSFP");
/*      */ 
/* 1302 */     MESH4PortMap.put("1", "SIGIN");
/* 1303 */     MESH4PortMap.put("2", "SIGOUT1");
/* 1304 */     MESH4PortMap.put("3", "SIGOUT2");
/* 1305 */     MESH4PortMap.put("4", "SIGOUT3");
/* 1306 */     MESH4PortMap.put("5", "SIGOUT4");
/*      */ 
/* 1308 */     ITLUPortMap.put("1", "SIGIN");
/* 1309 */     ITLUPortMap.put("2", "EOUT");
/* 1310 */     ITLUPortMap.put("3", "OOUT");
/*      */ 
/* 1312 */     WR8_88APortMap.put("1", "SIG");
/* 1313 */     WR8_88APortMap.put("2", "THRU");
/* 1314 */     WR8_88APortMap.put("3", "DROPOUT");
/* 1315 */     WR8_88APortMap.put("4", "ADDIN1");
/* 1316 */     WR8_88APortMap.put("5", "ADDIN2");
/* 1317 */     WR8_88APortMap.put("6", "ADDIN3");
/* 1318 */     WR8_88APortMap.put("7", "ADDIN4");
/* 1319 */     WR8_88APortMap.put("8", "ADDIN5");
/* 1320 */     WR8_88APortMap.put("9", "ADDIN6");
/* 1321 */     WR8_88APortMap.put("10", "ADDIN7");
/* 1322 */     WR8_88APortMap.put("11", "ADDIN8");
/* 1323 */     WR8_88APortMap.put("12", "MESHOUT1");
/* 1324 */     WR8_88APortMap.put("13", "MESHOUT2");
/* 1325 */     WR8_88APortMap.put("14", "MESHOUT3");
/*      */ 
/* 1328 */     MVAC8BPortMap.put("1", "L1");
/* 1329 */     MVAC8BPortMap.put("2", "L2");
/* 1330 */     MVAC8BPortMap.put("3", "L3");
/* 1331 */     MVAC8BPortMap.put("4", "L4");
/* 1332 */     MVAC8BPortMap.put("5", "L5");
/* 1333 */     MVAC8BPortMap.put("6", "L6");
/* 1334 */     MVAC8BPortMap.put("7", "L7");
/* 1335 */     MVAC8BPortMap.put("8", "L8");
/* 1336 */     MVAC8BPortMap.put("9", "C1");
/* 1337 */     MVAC8BPortMap.put("10", "C2");
/* 1338 */     MVAC8BPortMap.put("11", "C3");
/* 1339 */     MVAC8BPortMap.put("12", "C4");
/* 1340 */     MVAC8BPortMap.put("13", "C5");
/* 1341 */     MVAC8BPortMap.put("14", "C6");
/* 1342 */     MVAC8BPortMap.put("15", "C7");
/* 1343 */     MVAC8BPortMap.put("16", "C8");
/*      */ 
/* 1346 */     WR2_88PortMap.put("1", "SIG");
/* 1347 */     WR2_88PortMap.put("2", "THRU");
/* 1348 */     WR2_88PortMap.put("3", "DROPOUT");
/* 1349 */     WR2_88PortMap.put("4", "ADDIN");
/*      */ 
/* 1351 */     _11QPE24PortMap.put("1", "X1");
/* 1352 */     _11QPE24PortMap.put("2", "X2");
/* 1353 */     _11QPE24PortMap.put("3", "X3");
/* 1354 */     _11QPE24PortMap.put("4", "X4");
/* 1355 */     _11QPE24PortMap.put("5", "C1");
/* 1356 */     _11QPE24PortMap.put("6", "C2");
/* 1357 */     _11QPE24PortMap.put("7", "C3");
/* 1358 */     _11QPE24PortMap.put("8", "C4");
/* 1359 */     _11QPE24PortMap.put("9", "C5");
/* 1360 */     _11QPE24PortMap.put("10", "C6");
/* 1361 */     _11QPE24PortMap.put("11", "C7");
/* 1362 */     _11QPE24PortMap.put("12", "C8");
/* 1363 */     _11QPE24PortMap.put("13", "C9");
/* 1364 */     _11QPE24PortMap.put("14", "C10");
/* 1365 */     _11QPE24PortMap.put("15", "C11");
/* 1366 */     _11QPE24PortMap.put("16", "C12");
/* 1367 */     _11QPE24PortMap.put("17", "C13");
/* 1368 */     _11QPE24PortMap.put("18", "C14");
/* 1369 */     _11QPE24PortMap.put("19", "C15");
/* 1370 */     _11QPE24PortMap.put("20", "C16");
/* 1371 */     _11QPE24PortMap.put("21", "C17");
/* 1372 */     _11QPE24PortMap.put("22", "C18");
/* 1373 */     _11QPE24PortMap.put("23", "C19");
/* 1374 */     _11QPE24PortMap.put("24", "C20");
/* 1375 */     _11QPE24PortMap.put("25", "C21");
/* 1376 */     _11QPE24PortMap.put("26", "C22");
/*      */ 
/* 1379 */     _11QPE24PortMap.put("29", "VA1");
/* 1380 */     _11QPE24PortMap.put("30", "VA2");
/* 1381 */     _11QPE24PortMap.put("31", "VA3");
/* 1382 */     _11QPE24PortMap.put("32", "VA4");
/*      */ 
/* 1385 */     A2P2125PortMap.put("1", "LINEIN");
/*      */ 
/* 1387 */     A2P2125PortMap.put("4", "LINEOUT");
/* 1388 */     A2P2125PortMap.put("5", "OSCSFP");
/* 1389 */     A2P2125PortMap.put("6", "OSC");
/*      */ 
/* 1392 */     _4QPA8PortMap.put("1", "L1");
/* 1393 */     _4QPA8PortMap.put("2", "L2");
/* 1394 */     _4QPA8PortMap.put("3", "L3");
/* 1395 */     _4QPA8PortMap.put("4", "L4");
/* 1396 */     _4QPA8PortMap.put("5", "C1");
/* 1397 */     _4QPA8PortMap.put("6", "C2");
/* 1398 */     _4QPA8PortMap.put("7", "C3");
/* 1399 */     _4QPA8PortMap.put("8", "C4");
/* 1400 */     _4QPA8PortMap.put("9", "C5");
/* 1401 */     _4QPA8PortMap.put("10", "C6");
/* 1402 */     _4QPA8PortMap.put("11", "C7");
/* 1403 */     _4QPA8PortMap.put("12", "C8");
/* 1404 */     _4QPA8PortMap.put("13", "VA1");
/* 1405 */     _4QPA8PortMap.put("14", "VA2");
/* 1406 */     _4QPA8PortMap.put("15", "VA3");
/* 1407 */     _4QPA8PortMap.put("16", "VA4");
/* 1408 */     _4QPA8PortMap.put("0:1", "ODU1PTF1");
/* 1409 */     _4QPA8PortMap.put("0:2", "ODU1PTF2");
/* 1410 */     _4QPA8PortMap.put("0:3", "ODU1PTF3");
/* 1411 */     _4QPA8PortMap.put("0:4", "ODU1PTF4");
/*      */ 
/* 1413 */     PTPCTLPortMap.put("1", "P1");
/* 1414 */     PTPCTLPortMap.put("2", "P2");
/* 1415 */     PTPCTLPortMap.put("3", "P3");
/* 1416 */     PTPCTLPortMap.put("4", "P4");
/* 1417 */     PTPCTLPortMap.put("5", "P5");
/* 1418 */     PTPCTLPortMap.put("6", "P6");
/* 1419 */     PTPCTLPortMap.put("7", "BITS1");
/* 1420 */     PTPCTLPortMap.put("8", "BITS2");
/* 1421 */     PTPCTLPortMap.put("9", "TOD1");
/* 1422 */     PTPCTLPortMap.put("10", "TOD2");
/*      */ 
/* 1424 */     PTPIOPortMap.put("1", "TP1");
/* 1425 */     PTPIOPortMap.put("2", "TP2");
/* 1426 */     PTPIOPortMap.put("3", "ITP1");
/* 1427 */     PTPIOPortMap.put("4", "ITP2");
/* 1428 */     PTPIOPortMap.put("5", "SIG1");
/* 1429 */     PTPIOPortMap.put("6", "SIG2");
/* 1430 */     PTPIOPortMap.put("7", "LINE1");
/* 1431 */     PTPIOPortMap.put("8", "LINE2");
/*      */ 
/* 1433 */     _112PDM11PortMap.put("1", "L1");
/* 1434 */     _112PDM11PortMap.put("2", "L2");
/* 1435 */     _112PDM11PortMap.put("3", "L3");
/* 1436 */     _112PDM11PortMap.put("4", "L4");
/* 1437 */     _112PDM11PortMap.put("5", "C1");
/* 1438 */     _112PDM11PortMap.put("6", "C2");
/* 1439 */     _112PDM11PortMap.put("7", "C3");
/* 1440 */     _112PDM11PortMap.put("8", "C4");
/* 1441 */     _112PDM11PortMap.put("9", "C5");
/* 1442 */     _112PDM11PortMap.put("10", "C6");
/* 1443 */     _112PDM11PortMap.put("11", "C7");
/* 1444 */     _112PDM11PortMap.put("12", "C8");
/* 1445 */     _112PDM11PortMap.put("13", "C9");
/* 1446 */     _112PDM11PortMap.put("14", "C10");
/*      */ 
/* 1449 */     for (int i = 1; i <= 10; i++)
/*      */     {
/* 1451 */       _10AN10GPortMap.put(Integer.toString(i), Integer.toString(i));
/* 1452 */       _10ET10GPortMap.put(Integer.toString(i), Integer.toString(i));
/*      */     }
/*      */ 
/* 1455 */     for (int i = 1; i <= 24; i++)
/*      */     {
/* 1457 */       _24ANMPortMap.put(Integer.toString(i), Integer.toString(i));
/* 1458 */       _24ANMBPortMap.put(Integer.toString(i), Integer.toString(i));
/* 1459 */       _24ET1GBPortMap.put(Integer.toString(i), Integer.toString(i));
/*      */     }
/*      */ 
/* 1462 */     for (int i = 1; i <= 4; i++)
/*      */     {
/* 1464 */       _4AN10GPortMap.put(Integer.toString(i), Integer.toString(i));
/*      */     }
/*      */ 
/* 1467 */     for (int i = 1; i <= 8; i++)
/*      */     {
/* 1469 */       _8ET1GBPortMap.put(Integer.toString(i), Integer.toString(i));
/*      */     }
/*      */ 
/* 1472 */     for (int i = 1; i <= 160; i++)
/*      */     {
/* 1474 */       _MTC1T9PortMap.put(Integer.toString(i), Integer.toString(i));
/*      */     }
/*      */ 
/* 1477 */     _11QCUPCPortMap.put("1", "L1");
/* 1478 */     _11QCUPCPortMap.put("2", "L2");
/* 1479 */     _11QCUPCPortMap.put("3", "L3");
/* 1480 */     _11QCUPCPortMap.put("4", "L4");
/* 1481 */     _11QCUPCPortMap.put("5", "VA1");
/* 1482 */     _11QCUPCPortMap.put("6", "VA2");
/* 1483 */     _11QCUPCPortMap.put("7", "VA3");
/* 1484 */     _11QCUPCPortMap.put("8", "VA4");
/*      */ 
/* 1486 */     _130SCUPPortMap.put("1", "L1");
/* 1487 */     _130SCUPBPortMap.put("1", "L1");
/*      */ 
/* 1490 */     _112SDX11PortMap.put("1", "L1");
/* 1491 */     _112SDX11PortMap.put("2", "L2");
/* 1492 */     _112SDX11PortMap.put("3", "L3");
/* 1493 */     _112SDX11PortMap.put("4", "L4");
/* 1494 */     _112SDX11PortMap.put("5", "C1");
/* 1495 */     _112SDX11PortMap.put("6", "C2");
/* 1496 */     _112SDX11PortMap.put("7", "C3");
/* 1497 */     _112SDX11PortMap.put("8", "C4");
/* 1498 */     _112SDX11PortMap.put("9", "C5");
/* 1499 */     _112SDX11PortMap.put("10", "C6");
/* 1500 */     _112SDX11PortMap.put("11", "C7");
/* 1501 */     _112SDX11PortMap.put("12", "C8");
/* 1502 */     _112SDX11PortMap.put("13", "C9");
/* 1503 */     _112SDX11PortMap.put("14", "C10");
/* 1504 */     _112SDX11PortMap.put("15", "C11");
/* 1505 */     _112SDX11PortMap.put("16", "C12");
/* 1506 */     _112SDX11PortMap.put("17", "C13");
/* 1507 */     _112SDX11PortMap.put("18", "C14");
/*      */ 
/* 1509 */     _260SCX2PortMap.put("1", "L1");
/* 1510 */     _260SCX2PortMap.put("2", "C1");
/*      */ 
/* 1512 */     _AA2DONWPortMap.put("1", "SIG");
/* 1513 */     _AA2DONWPortMap.put("3", "OSC");
/* 1514 */     _AA2DONWPortMap.put("4", "LINE");
/* 1515 */     _AA2DONWPortMap.put("5", "OSCSFP");
/*      */ 
/* 1517 */     int i = 1;
/* 1518 */     for (int j = 1; j <= 8; i++) {
/* 1519 */       _MCS8X16PortMap.put(String.valueOf(i), "SIG" + j);
/*      */ 
/* 1518 */       j++;
/*      */     }
/*      */ 
/* 1521 */     for (int j = 1; j <= 16; i++) {
/* 1522 */       _MCS8X16PortMap.put(String.valueOf(i), "AD" + j);
/*      */ 
/* 1521 */       j++;
/*      */     }
/*      */ 
/* 1524 */     _MCS8X16PortMap.put(String.valueOf(i++), "AAR1");
/* 1525 */     _MCS8X16PortMap.put(String.valueOf(i), "AAR2");
/*      */ 
/* 1527 */     _A4PSWGPortMap.put("1", "LINEIN");
/* 1528 */     _A4PSWGPortMap.put("2", "OTDRRX");
/* 1529 */     _A4PSWGPortMap.put("3", "OTDRTX");
/* 1530 */     _A4PSWGPortMap.put("4", "LINEOUT");
/* 1531 */     _A4PSWGPortMap.put("5", "OSCSFP");
/* 1532 */     _A4PSWGPortMap.put("6", "OSC");
/*      */ 
/* 1534 */     _ASWGPortMap.put("1", "LINEIN");
/* 1535 */     _ASWGPortMap.put("2", "OTDRRX");
/* 1536 */     _ASWGPortMap.put("3", "OTDRTX");
/* 1537 */     _ASWGPortMap.put("4", "LINEOUT");
/* 1538 */     _ASWGPortMap.put("5", "OSCSFP");
/* 1539 */     _ASWGPortMap.put("6", "OSC");
/*      */ 
/* 1541 */     _WR20TFMPortMap.put("1", "SIG");
/* 1542 */     for (int adt = 1; adt <= 8; adt++) {
/* 1543 */       _WR20TFMPortMap.put(Integer.toString(adt + 1), "ADT" + adt);
/*      */     }
/* 1545 */     for (int ad = 9; ad <= 20; ad++) {
/* 1546 */       _WR20TFMPortMap.put(Integer.toString(ad + 1), "AD" + ad);
/*      */     }
/*      */ 
/* 1549 */     _WR20TFPortMap.putAll(_WR20TFMPortMap);
/*      */ 
/* 1551 */     _WR20TFMPortMap.put("22", "INV");
/* 1552 */     _WR20TFMPortMap.put("23", "DROP1OUT");
/* 1553 */     _WR20TFMPortMap.put("24", "DROP2OUT");
/* 1554 */     _WR20TFMPortMap.put("25", "ADD1IN");
/* 1555 */     _WR20TFMPortMap.put("26", "ADD2IN");
/*      */ 
/* 1559 */     AAR_8APortMap.put("1", "AMPIN1");
/* 1560 */     AAR_8APortMap.put("2", "AMPIN2");
/* 1561 */     AAR_8APortMap.put("3", "AMPIN3");
/* 1562 */     AAR_8APortMap.put("4", "AMPIN4");
/* 1563 */     AAR_8APortMap.put("5", "AMPIN5");
/* 1564 */     AAR_8APortMap.put("6", "AMPIN6");
/* 1565 */     AAR_8APortMap.put("7", "AMPIN7");
/* 1566 */     AAR_8APortMap.put("8", "AMPIN8");
/* 1567 */     AAR_8APortMap.put("9", "AMPOUT1");
/* 1568 */     AAR_8APortMap.put("10", "AMPOUT2");
/* 1569 */     AAR_8APortMap.put("11", "AMPOUT3");
/* 1570 */     AAR_8APortMap.put("12", "AMPOUT4");
/* 1571 */     AAR_8APortMap.put("13", "AMPOUT5");
/* 1572 */     AAR_8APortMap.put("14", "AMPOUT6");
/* 1573 */     AAR_8APortMap.put("15", "AMPOUT7");
/* 1574 */     AAR_8APortMap.put("16", "AMPOUT8");
/* 1575 */     AAR_8APortMap.put("17", "FSM");
/* 1576 */     AAR_8APortMap.put("18", "MCS");
/*      */ 
/* 1580 */     MSH8PortMap.put("1", "WSS1DROP1IN");
/* 1581 */     MSH8PortMap.put("2", "WSS1DROP2IN");
/* 1582 */     MSH8PortMap.put("3", "WSS1ADD1OUT");
/* 1583 */     MSH8PortMap.put("4", "WSS1ADD2OUT");
/* 1584 */     MSH8PortMap.put("5", "WSS2DROP1IN");
/* 1585 */     MSH8PortMap.put("6", "WSS2DROP2IN");
/* 1586 */     MSH8PortMap.put("7", "WSS2ADD1OUT");
/* 1587 */     MSH8PortMap.put("8", "WSS2ADD2OUT");
/* 1588 */     MSH8PortMap.put("9", "WSS3DROP1IN");
/* 1589 */     MSH8PortMap.put("10", "WSS3DROP2IN");
/* 1590 */     MSH8PortMap.put("11", "WSS3ADD1OUT");
/* 1591 */     MSH8PortMap.put("12", "WSS3ADD2OUT");
/* 1592 */     MSH8PortMap.put("13", "WSS4DROP1IN");
/* 1593 */     MSH8PortMap.put("14", "WSS4DROP2IN");
/* 1594 */     MSH8PortMap.put("15", "WSS4ADD1OUT");
/* 1595 */     MSH8PortMap.put("16", "WSS4ADD2OUT");
/* 1596 */     MSH8PortMap.put("17", "WSS5DROP1IN");
/* 1597 */     MSH8PortMap.put("18", "WSS5DROP2IN");
/* 1598 */     MSH8PortMap.put("19", "WSS5ADD1OUT");
/* 1599 */     MSH8PortMap.put("20", "WSS5ADD2OUT");
/* 1600 */     MSH8PortMap.put("21", "WSS6DROP1IN");
/* 1601 */     MSH8PortMap.put("22", "WSS6DROP2IN");
/* 1602 */     MSH8PortMap.put("23", "WSS6ADD1OUT");
/* 1603 */     MSH8PortMap.put("24", "WSS6ADD2OUT");
/* 1604 */     MSH8PortMap.put("25", "WSS7DROP1IN");
/* 1605 */     MSH8PortMap.put("26", "WSS7DROP2IN");
/* 1606 */     MSH8PortMap.put("27", "WSS7ADD1OUT");
/* 1607 */     MSH8PortMap.put("28", "WSS7ADD2OUT");
/* 1608 */     MSH8PortMap.put("29", "WSS8DROP1IN");
/* 1609 */     MSH8PortMap.put("30", "WSS8DROP2IN");
/* 1610 */     MSH8PortMap.put("31", "WSS8ADD1OUT");
/* 1611 */     MSH8PortMap.put("32", "WSS8ADD2OUT");
/* 1612 */     MSH8PortMap.put("33", "WSS1TO4AD1");
/* 1613 */     MSH8PortMap.put("34", "WSS1TO4AD2");
/* 1614 */     MSH8PortMap.put("35", "WSS1TO4AD3");
/* 1615 */     MSH8PortMap.put("36", "WSS1TO4AD4");
/* 1616 */     MSH8PortMap.put("37", "WSS1TO4AD5");
/* 1617 */     MSH8PortMap.put("38", "WSS1TO4AD6");
/* 1618 */     MSH8PortMap.put("39", "WSS1TO4AD7");
/* 1619 */     MSH8PortMap.put("40", "WSS1TO4AD8");
/* 1620 */     MSH8PortMap.put("41", "WSS1TO4AD9");
/* 1621 */     MSH8PortMap.put("42", "WSS1TO4AD10");
/* 1622 */     MSH8PortMap.put("43", "WSS1TO4AD11");
/* 1623 */     MSH8PortMap.put("44", "WSS1TO4AD12");
/* 1624 */     MSH8PortMap.put("45", "WSS5TO8AD1");
/* 1625 */     MSH8PortMap.put("46", "WSS5TO8AD2");
/* 1626 */     MSH8PortMap.put("47", "WSS5TO8AD3");
/* 1627 */     MSH8PortMap.put("48", "WSS5TO8AD4");
/* 1628 */     MSH8PortMap.put("49", "WSS5TO8AD5");
/* 1629 */     MSH8PortMap.put("50", "WSS5TO8AD6");
/* 1630 */     MSH8PortMap.put("51", "WSS5TO8AD7");
/* 1631 */     MSH8PortMap.put("52", "WSS5TO8AD8");
/* 1632 */     MSH8PortMap.put("53", "WSS5TO8AD9");
/* 1633 */     MSH8PortMap.put("54", "WSS5TO8AD10");
/* 1634 */     MSH8PortMap.put("55", "WSS5TO8AD11");
/* 1635 */     MSH8PortMap.put("56", "WSS5TO8AD12");
/*      */ 
/* 1654 */     _PSC1PortMap.put("1", "SIGA");
/* 1655 */     _PSC1PortMap.put("2", "SIGB");
/* 1656 */     _PSC1PortMap.put("3", "SIGC");
/* 1657 */     _PSC1PortMap.put("4", "SIGD");
/* 1658 */     _PSC1PortMap.put("5", "SIGE");
/* 1659 */     _PSC1PortMap.put("6", "A1");
/* 1660 */     _PSC1PortMap.put("7", "A2");
/* 1661 */     _PSC1PortMap.put("8", "A3");
/* 1662 */     _PSC1PortMap.put("9", "A4");
/* 1663 */     _PSC1PortMap.put("10", "A5");
/* 1664 */     _PSC1PortMap.put("11", "A6");
/* 1665 */     _PSC1PortMap.put("12", "B1");
/* 1666 */     _PSC1PortMap.put("13", "B2");
/* 1667 */     _PSC1PortMap.put("14", "B3");
/* 1668 */     _PSC1PortMap.put("15", "B4");
/* 1669 */     _PSC1PortMap.put("16", "B5");
/* 1670 */     _PSC1PortMap.put("17", "B6");
/* 1671 */     _PSC1PortMap.put("18", "C1");
/* 1672 */     _PSC1PortMap.put("19", "C2");
/* 1673 */     _PSC1PortMap.put("20", "C3");
/* 1674 */     _PSC1PortMap.put("21", "C4");
/* 1675 */     _PSC1PortMap.put("22", "C5");
/* 1676 */     _PSC1PortMap.put("23", "C6");
/* 1677 */     _PSC1PortMap.put("24", "D1");
/* 1678 */     _PSC1PortMap.put("25", "D2");
/* 1679 */     _PSC1PortMap.put("26", "D3");
/* 1680 */     _PSC1PortMap.put("27", "D4");
/* 1681 */     _PSC1PortMap.put("28", "D5");
/* 1682 */     _PSC1PortMap.put("29", "D6");
/* 1683 */     _PSC1PortMap.put("30", "E1");
/* 1684 */     _PSC1PortMap.put("31", "E2");
/* 1685 */     _PSC1PortMap.put("32", "E3");
/* 1686 */     _PSC1PortMap.put("33", "E4");
/* 1687 */     _PSC1PortMap.put("34", "E5");
/* 1688 */     _PSC1PortMap.put("35", "E6");
/*      */ 
/* 1706 */     _OTDRPortMap.put("1", "P1");
/* 1707 */     _OTDRPortMap.put("2", "P2");
/* 1708 */     _OTDRPortMap.put("3", "P3");
/* 1709 */     _OTDRPortMap.put("4", "P4");
/* 1710 */     _OTDRPortMap.put("5", "P5");
/* 1711 */     _OTDRPortMap.put("6", "P6");
/* 1712 */     _OTDRPortMap.put("7", "P7");
/* 1713 */     _OTDRPortMap.put("8", "P8");
/*      */ 
/* 1715 */     _11OPE8PortMap.put("1", "X1");
/* 1716 */     _11OPE8PortMap.put("2", "X2");
/* 1717 */     _11OPE8PortMap.put("3", "X3");
/* 1718 */     _11OPE8PortMap.put("4", "X4");
/* 1719 */     _11OPE8PortMap.put("5", "X5");
/* 1720 */     _11OPE8PortMap.put("6", "X6");
/* 1721 */     _11OPE8PortMap.put("7", "C1");
/* 1722 */     _11OPE8PortMap.put("8", "C2");
/* 1723 */     _11OPE8PortMap.put("9", "M1");
/* 1724 */     _11OPE8PortMap.put("10", "M2");
/* 1725 */     _11OPE8PortMap.put("11", "M3");
/* 1726 */     _11OPE8PortMap.put("12", "M4");
/* 1727 */     _11OPE8PortMap.put("13", "VA1");
/* 1728 */     _11OPE8PortMap.put("14", "VA2");
/* 1729 */     _11OPE8PortMap.put("15", "VA3");
/* 1730 */     _11OPE8PortMap.put("16", "VA4");
/* 1731 */     _11OPE8PortMap.put("17", "TOD1");
/*      */ 
/* 1733 */     _11QCE12XPortMap.put("1", "X1");
/* 1734 */     _11QCE12XPortMap.put("2", "X2");
/* 1735 */     _11QCE12XPortMap.put("3", "X3");
/* 1736 */     _11QCE12XPortMap.put("4", "X4");
/* 1737 */     _11QCE12XPortMap.put("5", "C1");
/* 1738 */     _11QCE12XPortMap.put("6", "C2");
/* 1739 */     _11QCE12XPortMap.put("7", "C3");
/* 1740 */     _11QCE12XPortMap.put("8", "C4");
/* 1741 */     _11QCE12XPortMap.put("9", "C5");
/* 1742 */     _11QCE12XPortMap.put("10", "C6");
/* 1743 */     _11QCE12XPortMap.put("11", "C7");
/* 1744 */     _11QCE12XPortMap.put("12", "C8");
/* 1745 */     _11QCE12XPortMap.put("13", "C9");
/* 1746 */     _11QCE12XPortMap.put("14", "C10");
/* 1747 */     _11QCE12XPortMap.put("15", "C11");
/* 1748 */     _11QCE12XPortMap.put("16", "C12");
/* 1749 */     _11QCE12XPortMap.put("17", "C13");
/* 1750 */     _11QCE12XPortMap.put("18", "C14");
/* 1751 */     _11QCE12XPortMap.put("19", "C15");
/* 1752 */     _11QCE12XPortMap.put("20", "C16");
/* 1753 */     _11QCE12XPortMap.put("21", "C17");
/* 1754 */     _11QCE12XPortMap.put("22", "C18");
/* 1755 */     _11QCE12XPortMap.put("23", "C19");
/* 1756 */     _11QCE12XPortMap.put("24", "C20");
/* 1757 */     _11QCE12XPortMap.put("25", "C21");
/* 1758 */     _11QCE12XPortMap.put("26", "C22");
/*      */ 
/* 1761 */     _11QCE12XPortMap.put("29", "M1");
/* 1762 */     _11QCE12XPortMap.put("30", "M2");
/* 1763 */     _11QCE12XPortMap.put("31", "M3");
/* 1764 */     _11QCE12XPortMap.put("32", "M4");
/* 1765 */     _11QCE12XPortMap.put("33", "VA1");
/* 1766 */     _11QCE12XPortMap.put("34", "VA2");
/* 1767 */     _11QCE12XPortMap.put("35", "TOD1");
/*      */ 
/* 1769 */     D5X500OTUMap.put("65", "1");
/* 1770 */     D5X500OTUMap.put("66", "2");
/* 1771 */     D5X500OTUMap.put("67", "3");
/* 1772 */     D5X500OTUMap.put("68", "4");
/* 1773 */     D5X500OTUMap.put("69", "5");
/*      */ 
/* 1775 */     EqptToPortMapTable.put("DCM", DCMPortMap);
/* 1776 */     EqptToPortMapTable.put("ITLB", ITLBPortMap);
/* 1777 */     EqptToPortMapTable.put("ALPHG", ALPHGPortMap);
/* 1778 */     EqptToPortMapTable.put("AHPHG", AHPHGPortMap);
/* 1779 */     EqptToPortMapTable.put("AHPLG", AHPLGPortMap);
/* 1780 */     EqptToPortMapTable.put("CWR8", CWR8PortMap);
/* 1781 */     EqptToPortMapTable.put("CWR8-88", CWR8_88PortMap);
/* 1782 */     EqptToPortMapTable.put("SVAC", SVACPortMap);
/* 1783 */     EqptToPortMapTable.put("OPSA", OPSAPortMap);
/*      */ 
/* 1785 */     EqptToPortMapTable.put("SFD44", SFD44PortMap);
/* 1786 */     EqptToPortMapTable.put("SFD44B", SFD44BPortMap);
/* 1787 */     EqptToPortMapTable.put("SFD5A", SFD5APortMap);
/* 1788 */     EqptToPortMapTable.put("SFD5B", SFD5BPortMap);
/* 1789 */     EqptToPortMapTable.put("SFD5C", SFD5CPortMap);
/* 1790 */     EqptToPortMapTable.put("SFD5D", SFD5DPortMap);
/* 1791 */     EqptToPortMapTable.put("SFD5E", SFD5EPortMap);
/* 1792 */     EqptToPortMapTable.put("SFD5F", SFD5FPortMap);
/* 1793 */     EqptToPortMapTable.put("SFD5G", SFD5GPortMap);
/* 1794 */     EqptToPortMapTable.put("SFD5H", SFD5HPortMap);
/* 1795 */     EqptToPortMapTable.put("SFD10A", SFD10APortMap);
/* 1796 */     EqptToPortMapTable.put("SFD10B", SFD10BPortMap);
/* 1797 */     EqptToPortMapTable.put("SFD10C", SFD10CPortMap);
/* 1798 */     EqptToPortMapTable.put("SFD10D", SFD10DPortMap);
/*      */ 
/* 1800 */     EqptToPortMapTable.put("SFC8", SFC8PortMap);
/* 1801 */     EqptToPortMapTable.put("SFC4A", SFC4APortMap);
/* 1802 */     EqptToPortMapTable.put("SFC4B", SFC4BPortMap);
/* 1803 */     EqptToPortMapTable.put("SFC2A", SFC2APortMap);
/* 1804 */     EqptToPortMapTable.put("SFC2B", SFC2BPortMap);
/* 1805 */     EqptToPortMapTable.put("SFC2C", SFC2CPortMap);
/* 1806 */     EqptToPortMapTable.put("SFC2D", SFC2DPortMap);
/*      */ 
/* 1808 */     EqptToPortMapTable.put("11STAR1", _11STAR1PortMap);
/* 1809 */     EqptToPortMapTable.put("11STMM10", _11STMM10PortMap);
/* 1810 */     EqptToPortMapTable.put("11STGE12", _11STGE12PortMap);
/* 1811 */     EqptToPortMapTable.put("11DPGE12", _11DPGE12PortMap);
/* 1812 */     EqptToPortMapTable.put("43STX4", _43STX4PortMap);
/* 1813 */     EqptToPortMapTable.put("4DPA4", _4DPA4PortMap);
/*      */ 
/* 1815 */     EqptToPortMapTable.put("ALPFGT", ALPFGTPortMap);
/* 1816 */     EqptToPortMapTable.put("A2325A", A2325APortMap);
/* 1817 */     EqptToPortMapTable.put("OSCT", OSCTPortMap);
/* 1818 */     EqptToPortMapTable.put("SFD40", SFD40PortMap);
/* 1819 */     EqptToPortMapTable.put("SFD40B", SFD40BPortMap);
/* 1820 */     EqptToPortMapTable.put("11QPA4", _11QPA4PortMap);
/* 1821 */     EqptToPortMapTable.put("11QTA4", _11QPA4PortMap);
/* 1822 */     EqptToPortMapTable.put("11DPE12", _11DPE12PortMap);
/* 1823 */     EqptToPortMapTable.put("43STA1P", _43STA1PPortMap);
/* 1824 */     EqptToPortMapTable.put("4DPA2", _4DPA2PortMap);
/* 1825 */     EqptToPortMapTable.put("43STX4P", _43STX4PPortMap);
/* 1826 */     EqptToPortMapTable.put("SFD8A", SFD8APortMap);
/* 1827 */     EqptToPortMapTable.put("SFD8B", SFD8BPortMap);
/* 1828 */     EqptToPortMapTable.put("SFD8C", SFD8CPortMap);
/* 1829 */     EqptToPortMapTable.put("SFD8D", SFD8DPortMap);
/*      */ 
/* 1832 */     EqptToPortMapTable.put("DCML", DCMPortMap);
/* 1833 */     EqptToPortMapTable.put("ITLBL", ITLBPortMap);
/* 1834 */     EqptToPortMapTable.put("A2223AL", A2325APortMap);
/* 1835 */     EqptToPortMapTable.put("CWR8-88L", CWR8_88PortMap);
/* 1836 */     EqptToPortMapTable.put("PMDCL", PMDCLPortMap);
/* 1837 */     EqptToPortMapTable.put("SFD44L", SFD44LPortMap);
/* 1838 */     EqptToPortMapTable.put("SFD44BL", SFD44BLPortMap);
/* 1839 */     EqptToPortMapTable.put("43STA1PL", _43STA1PPortMap);
/* 1840 */     EqptToPortMapTable.put("43STX4PL", _43STX4PPortMap);
/*      */ 
/* 1842 */     EqptToPortMapTable.put("43SCA1", _43STA1PPortMap);
/*      */ 
/* 1845 */     EqptToPortMapTable.put("PSS1AHP", AHPHGPortMap);
/*      */ 
/* 1848 */     EqptToPortMapTable.put("SFC1A", SFC1APortMap);
/* 1849 */     EqptToPortMapTable.put("SFC1B", SFC1BPortMap);
/* 1850 */     EqptToPortMapTable.put("SFC1C", SFC1CPortMap);
/* 1851 */     EqptToPortMapTable.put("SFC1D", SFC1DPortMap);
/* 1852 */     EqptToPortMapTable.put("SFC1E", SFC1EPortMap);
/* 1853 */     EqptToPortMapTable.put("SFC1F", SFC1FPortMap);
/* 1854 */     EqptToPortMapTable.put("SFC1G", SFC1GPortMap);
/* 1855 */     EqptToPortMapTable.put("SFC1H", SFC1HPortMap);
/* 1856 */     EqptToPortMapTable.put("PSS1GBE", PSS1GBEPortMap);
/* 1857 */     EqptToPortMapTable.put("PSS1MD4", PSS1MD4PortMap);
/*      */ 
/* 1860 */     EqptToPortMapTable.put("112SCA1", _112SCA1PortMap);
/* 1861 */     EqptToPortMapTable.put("112SCX10", _112SCX10PortMap);
/*      */ 
/* 1864 */     EqptToPortMapTable.put("112SNA1", _112SCA1PortMap);
/* 1865 */     EqptToPortMapTable.put("112SNX10", _112SCX10PortMap);
/* 1866 */     EqptToPortMapTable.put("130SNX10", _130SNX10PortMap);
/*      */ 
/* 1869 */     EqptToPortMapTable.put("AM2017B", AM2017BPortMap);
/* 1870 */     EqptToPortMapTable.put("AM2325B", AM2325BPortMap);
/* 1871 */     EqptToPortMapTable.put("WTOCM", WTOCMPortMap);
/* 1872 */     EqptToPortMapTable.put("43SCT1", _43SCT1PortMap);
/*      */ 
/* 1875 */     EqptToPortMapTable.put("SFD4A", SFD4APortMap);
/* 1876 */     EqptToPortMapTable.put("SFD4B", SFD4BPortMap);
/* 1877 */     EqptToPortMapTable.put("SFD4C", SFD4CPortMap);
/* 1878 */     EqptToPortMapTable.put("SFD4D", SFD4DPortMap);
/* 1879 */     EqptToPortMapTable.put("SFD4E", SFD4EPortMap);
/* 1880 */     EqptToPortMapTable.put("SFD4F", SFD4FPortMap);
/* 1881 */     EqptToPortMapTable.put("SFD4G", SFD4GPortMap);
/* 1882 */     EqptToPortMapTable.put("SFD4H", SFD4HPortMap);
/*      */ 
/* 1884 */     EqptToPortMapTable.put("PSS1P21", PSS1P21PortMap);
/* 1885 */     EqptToPortMapTable.put("1DPP24M", PSS1P21PortMap);
/*      */ 
/* 1888 */     EqptToPortMapTable.put("AM2125A", AM2125APortMap);
/* 1889 */     EqptToPortMapTable.put("AM2625A", AM2625APortMap);
/* 1890 */     EqptToPortMapTable.put("AM2032A", AM2032APortMap);
/* 1891 */     EqptToPortMapTable.put("RA2P", RA2PPortMap);
/* 1892 */     EqptToPortMapTable.put("11DPE12E", _11DPE12PortMap);
/* 1893 */     EqptToPortMapTable.put("11DPM12", _11DPM12PortMap);
/* 1894 */     EqptToPortMapTable.put("112SA1L", _112SCA1PortMap);
/* 1895 */     EqptToPortMapTable.put("112SX10L", _112SCX10PortMap);
/* 1896 */     EqptToPortMapTable.put("MVAC", MVACPortMap);
/*      */ 
/* 1899 */     EqptToPortMapTable.put("AM2125B", AM2125APortMap);
/* 1900 */     EqptToPortMapTable.put("43SCX4", _43STX4PPortMap);
/* 1901 */     EqptToPortMapTable.put("43SCX4L", _43STX4PPortMap);
/* 1902 */     EqptToPortMapTable.put("AM2318A", AM2318APortMap);
/* 1903 */     EqptToPortMapTable.put("MESH4", MESH4PortMap);
/* 1904 */     EqptToPortMapTable.put("ITLU", ITLUPortMap);
/* 1905 */     EqptToPortMapTable.put("WR8-88A", WR8_88APortMap);
/*      */ 
/* 1908 */     EqptToPortMapTable.put("WR8-88AF", WR8_88APortMap);
/* 1909 */     EqptToPortMapTable.put("43SCX4E", _43STX4PPortMap);
/* 1910 */     EqptToPortMapTable.put("43SCGE1", _43STA1PPortMap);
/* 1911 */     EqptToPortMapTable.put("11STAR1A", _11STAR1PortMap);
/* 1912 */     EqptToPortMapTable.put("MVAC8B", MVAC8BPortMap);
/* 1913 */     EqptToPortMapTable.put("11QPEN4", _11QPA4PortMap);
/*      */ 
/* 1916 */     EqptToPortMapTable.put("11DPE12A", _11DPE12PortMap);
/* 1917 */     EqptToPortMapTable.put("WR2-88", WR2_88PortMap);
/* 1918 */     EqptToPortMapTable.put("OPSB", OPSAPortMap);
/* 1919 */     EqptToPortMapTable.put("11QPE24", _11QPE24PortMap);
/*      */ 
/* 1922 */     EqptToPortMapTable.put("130SCA1", _112SCA1PortMap);
/* 1923 */     EqptToPortMapTable.put("130SCX10", _112SCX10PortMap);
/* 1924 */     EqptToPortMapTable.put("WTOCMA", WTOCMPortMap);
/* 1925 */     EqptToPortMapTable.put("WTOCM-F", WTOCMPortMap);
/* 1926 */     EqptToPortMapTable.put("A2P2125", A2P2125PortMap);
/*      */ 
/* 1929 */     EqptToPortMapTable.put("4QPA8", _4QPA8PortMap);
/* 1930 */     EqptToPortMapTable.put("PTPCTL", PTPCTLPortMap);
/* 1931 */     EqptToPortMapTable.put("PTPIO", PTPIOPortMap);
/* 1932 */     EqptToPortMapTable.put("112PDM11", _112PDM11PortMap);
/* 1933 */     EqptToPortMapTable.put("10AN10G", _10AN10GPortMap);
/* 1934 */     EqptToPortMapTable.put("24ANM", _24ANMPortMap);
/* 1935 */     EqptToPortMapTable.put("24ANMB", _24ANMBPortMap);
/* 1936 */     EqptToPortMapTable.put("11QCUPC", _11QCUPCPortMap);
/* 1937 */     EqptToPortMapTable.put("24ET1GB", _24ET1GBPortMap);
/* 1938 */     EqptToPortMapTable.put("8ET1GB", _8ET1GBPortMap);
/* 1939 */     EqptToPortMapTable.put("10ET10G", _10ET10GPortMap);
/* 1940 */     EqptToPortMapTable.put("4AN10G", _4AN10GPortMap);
/* 1941 */     EqptToPortMapTable.put("130SCUP", _130SCUPPortMap);
/* 1942 */     EqptToPortMapTable.put("130SCUPB", _130SCUPBPortMap);
/* 1943 */     EqptToPortMapTable.put("MTC1T9", _MTC1T9PortMap);
/* 1944 */     EqptToPortMapTable.put("130SCA1", _130SCA1PortMap);
/*      */ 
/* 1947 */     EqptToPortMapTable.put("112SDX11", _112SDX11PortMap);
/* 1948 */     EqptToPortMapTable.put("11OPE8", _11OPE8PortMap);
/* 1949 */     EqptToPortMapTable.put("11QCE12X", _11QCE12XPortMap);
/*      */ 
/* 1951 */     EqptToPortMapTable.put("260SCX2", _260SCX2PortMap);
/* 1952 */     EqptToPortMapTable.put("AA2DONW", _AA2DONWPortMap);
/* 1953 */     EqptToPortMapTable.put("MCS8-16", _MCS8X16PortMap);
/* 1954 */     EqptToPortMapTable.put("A4PSWG", _A4PSWGPortMap);
/* 1955 */     EqptToPortMapTable.put("ASWG", _ASWGPortMap);
/* 1956 */     EqptToPortMapTable.put("AAR-8A", AAR_8APortMap);
/* 1957 */     EqptToPortMapTable.put("WR20-TFM", _WR20TFMPortMap);
/* 1958 */     EqptToPortMapTable.put("WR20-TF", _WR20TFPortMap);
/* 1959 */     EqptToPortMapTable.put("MSH8-FSM", MSH8PortMap);
/* 1960 */     EqptToPortMapTable.put("OTDR", _OTDRPortMap);
/* 1961 */     EqptToPortMapTable.put("PSC1-6", _PSC1PortMap);
/*      */ 
			}

}
