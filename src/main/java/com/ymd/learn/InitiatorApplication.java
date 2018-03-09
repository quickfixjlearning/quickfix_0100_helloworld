package com.ymd.learn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;

public class InitiatorApplication implements Application {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitiatorApplication.class);
	
	public void onCreate(SessionID sessionId) {
		LOGGER.info("InitiatorApplication -- onCreate - SessionID - {}", sessionId);
	}

	public void onLogon(SessionID sessionId) {
		LOGGER.info("InitiatorApplication -- onLogon - SessionID - {}", sessionId);
	}
	
	public void onLogout(SessionID sessionId) {
		LOGGER.info("InitiatorApplication -- onLogout - SessionID - {}", sessionId);
	}

	public void toAdmin(Message message, SessionID sessionId) {
		LOGGER.info("InitiatorApplication -- toAdmin - SessionID - {}", sessionId);
	}

	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		LOGGER.info("InitiatorApplication -- fromAdmin - SessionID - {}", sessionId);
	}

	public static void main(String[] args) throws ConfigError {
		InitiatorApplication initiatorApp = new InitiatorApplication();
		SessionSettings settings = new SessionSettings(AcceptorApplication.class.getResourceAsStream("initiator.cfg"));
		MessageStoreFactory storeFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();
		Initiator initiator = new SocketInitiator(initiatorApp, storeFactory, settings, logFactory, messageFactory);
		initiator.start();
		
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

	public void fromApp(Message arg0, SessionID arg1)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		LOGGER.info("InitiatorApplication -- fromApp - SessionID - {}", 1);
	}

	public void toApp(Message arg0, SessionID arg1) throws DoNotSend {
		LOGGER.info("InitiatorApplication -- toApp - SessionID - {}", 2);
	} 
	
	
}
