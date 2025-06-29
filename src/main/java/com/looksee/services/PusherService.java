package com.looksee.services;

import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;

public class PusherService {

	@Value( "${pusher.app_id}" )
	private static String app_id;
	
	@Value( "${pusher.key}" )
	private static String key;
	
	@Value( "${pusher.secret}" )
	private static String secret;
	
	@Value("${pusher.cluster}")
	private static String cluster;
	
	private Pusher pusher;
	
	public PusherService() {
		setPusher(new Pusher(app_id, key, secret));
		pusher.setCluster(cluster);
		pusher.setEncrypted(true);
	}

	public Pusher getPusher() {
		return pusher;
	}

	private void setPusher(Pusher pusher) {
		this.pusher = pusher;
	}
	
	public void trigger(String channel, String event_name, String json) {
		pusher.trigger(channel, event_name, json);
	}
}