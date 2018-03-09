package com.ymd.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.UnsupportedMessageType;
import quickfix.field.AvgPx;
import quickfix.field.CumQty;
import quickfix.field.ExecID;
import quickfix.field.ExecTransType;
import quickfix.field.LeavesQty;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.fix42.ExecutionReport;

public class AcceptorApplication implements Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(AcceptorApplication.class);
	
	public void onCreate(SessionID sessionId) {
		LOGGER.info("AcceptorApplication -- onCreate - SessionID - {}", sessionId);
	}

	public void onLogon(SessionID sessionId) {
		LOGGER.info("AcceptorApplication -- onLogon - SessionID - {}", sessionId);
	}

	public void onLogout(SessionID sessionId) {
		LOGGER.info("AcceptorApplication -- onLogout - SessionID - {}", sessionId);
	}

	public void toAdmin(Message message, SessionID sessionId) {
		LOGGER.info("AcceptorApplication -- toAdmin - SessionID - {}", sessionId);
	}

	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		LOGGER.info("AcceptorApplication -- fromAdmin - SessionID - {}", sessionId);
	}

	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		LOGGER.info("AcceptorApplication -- toApp - SessionID - {}", sessionId);
	}

	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		LOGGER.info("AcceptorApplication -- toApp - SessionID - {}", sessionId);
	}
	
	protected void onMessage(quickfix.fix42.ExecutionReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		LOGGER.info("Received Message {} ", message.toString());
	}

	public static void main(String[] args) throws ConfigError {
		Application acceptorApp = new AcceptorApplication();
		SessionSettings settings = new SessionSettings(AcceptorApplication.class.getResourceAsStream("acceptor.cfg"));
		MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();
		Acceptor acceptor = new SocketAcceptor(acceptorApp, storeFactory, settings, logFactory, messageFactory);
		acceptor.start();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendMessage();
	}
	
	public static void sendMessage() {
		ExecutionReport execMsg = new ExecutionReport();
		execMsg.setField(new OrderID("orderId00001"));
		execMsg.setField(new ExecID("execId00001"));
		execMsg.setField(new ExecTransType('0'));
		execMsg.setField(new OrdStatus('0'));
		execMsg.setField(new Symbol("stock01"));
		execMsg.setField(new Side('1'));
		execMsg.setField(new LeavesQty(200));
		execMsg.setField(new CumQty(4800));
		execMsg.setField(new AvgPx(100));
		try {
			Session.sendToTarget(execMsg, "MOAP", "SSENG");
		} catch (SessionNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
