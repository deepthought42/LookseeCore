package com.looksee.services;

import com.pusher.rest.Pusher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * PusherService is a service that provides a Pusher instance for triggering
 * events on channels. It is used to send messages to the client.
 */
public class PusherService {

	@Value( "${pusher.app_id}" )
	private static String app_id;
	
	@Value( "${pusher.key}" )
	private static String key;
	
	@Value( "${pusher.secret}" )
	private static String secret;
	
	@Value("${pusher.cluster}")
	private static String cluster;
	
	@Getter
	@Setter
	private Pusher pusher;
	
	public PusherService() {
		setPusher(new Pusher(app_id, key, secret));
		pusher.setCluster(cluster);
		pusher.setEncrypted(true);
	}

	/**
	 * Triggers an event on a channel
	 *
	 * @param channel the channel to trigger the event on
	 * @param event_name the name of the event
	 * @param json the JSON payload to send
	 */
	public void trigger(String channel, String event_name, String json) {
		pusher.trigger(channel, event_name, json);
	}
}