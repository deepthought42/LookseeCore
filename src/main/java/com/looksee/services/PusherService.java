package com.looksee.services;

import org.springframework.beans.factory.annotation.Value;

import com.pusher.rest.Pusher;

import lombok.Getter;
import lombok.Setter;

/**
 * PusherService is a service that provides a Pusher instance for triggering
 * events on channels. It is used to send messages to the client.
 * 
 * @deprecated This class uses outdated property names and conflicts with the new
 * PusherConfiguration system. Use MessageBroadcaster instead, which will be automatically
 * configured when proper Pusher properties are provided.
 * 
 * The new system uses:
 * - pusher.appId (instead of pusher.app_id)
 * - pusher.key
 * - pusher.secret  
 * - pusher.cluster
 * 
 * Or environment variables:
 * - PUSHER_APP_ID
 * - PUSHER_KEY
 * - PUSHER_SECRET
 * - PUSHER_CLUSTER
 */
@Deprecated
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
	
	/**
	 * Constructor for PusherService
	 * 
	 * @deprecated Use MessageBroadcaster instead
	 */
	@Deprecated
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
	 * 
	 * @deprecated Use MessageBroadcaster instead
	 */
	@Deprecated
	public void trigger(String channel, String event_name, String json) {
		pusher.trigger(channel, event_name, json);
	}
}