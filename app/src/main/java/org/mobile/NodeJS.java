package org.mobile;

import android.util.Log;
import java.io.File;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class NodeJS {
	static {
		System.loadLibrary("node");
		System.loadLibrary("java2node");
	}

	public native int run(String[] args);
	public native void sendMessage(String message);

	public interface OnNodeListener {
		int MESSAGE_TYPE_RAW_TEXT = 0;
		int MESSAGE_TYPE_JSON_ARRAY = 1;
		int MESSAGE_TYPE_JSON_OBJECT = 2;

		default void onOutput(String message) {
			Log.d("NodeJS Output", message);
		}

		default void onError(String message) {
			Log.d("NodeJS Error", message);
		}

		default void onMessage(int HandledMessage, String rawText, JSONArray jsonArray, JSONObject jsonObject) {
			switch (HandledMessage) {
				case MESSAGE_TYPE_RAW_TEXT :
					Log.d("NodeJS Message", rawText);
					break;

				case MESSAGE_TYPE_JSON_ARRAY :
					Log.d("NodeJS Message", jsonArray.toString());
					break;

				case MESSAGE_TYPE_JSON_OBJECT :
					Log.d("NodeJS Message", jsonObject.toString());
					break;
			}
		}

	}

	private OnNodeListener listener;

	public NodeJS() {
		this(new OnNodeListener() {
		});
	}

	public NodeJS(OnNodeListener listener) {
		this.listener = listener;
	}

	public int runCode(String code) {
		return run(new String[]{"node", "-e", code});
	}

	public int runFile(File file) {
		if (file.exists()) {
			return run(new String[]{"node", file.getAbsolutePath()});
		}

		emitError("file or project not found!");
		return 1;
	}

	public void sendJSON(JSONObject object) {
		sendMessage(object.toString());
	}

	public void sendJSON(JSONArray array) {
		sendMessage(array.toString());
	}

	private void emitOutput(String message) {
		if (listener != null) {
			listener.onOutput(message);
		}
	}

	private void emitError(String message) {
		if (listener != null) {
			listener.onError(message);
		}
	}

	private void emitMessage(String message) {
		if (listener != null) {
			HandledMessage handledMessage = getHandledMessage(message);
			listener.onMessage(handledMessage.type, handledMessage.rawText, handledMessage.array,
					handledMessage.object);
		}
	}

	private HandledMessage getHandledMessage(String message) {
		try {
			JSONArray array = new JSONArray(message);
			return new HandledMessage(OnNodeListener.MESSAGE_TYPE_JSON_ARRAY, null, array, null);
		} catch (JSONException e) {
			try {
				JSONObject object = new JSONObject(message);
				return new HandledMessage(OnNodeListener.MESSAGE_TYPE_JSON_OBJECT, null, null, object);
			} catch (JSONException f) {
				return new HandledMessage(OnNodeListener.MESSAGE_TYPE_RAW_TEXT, message, null, null);
			}
		}
	}

	private class HandledMessage {
		public final int type;
		public final String rawText;
		public final JSONArray array;
		public final JSONObject object;

		public HandledMessage(int type, String rawText, JSONArray array, JSONObject object) {
			this.type = type;
			this.rawText = rawText;
			this.array = array;
			this.object = object;
		}
	}
}

