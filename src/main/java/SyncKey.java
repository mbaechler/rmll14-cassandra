
import java.io.Serializable;

import com.google.common.base.Objects;

public class SyncKey implements Serializable {
	
	private static final long serialVersionUID = -6862413450080182711L;
	
	public static final SyncKey INITIAL_FOLDER_SYNC_KEY = new SyncKey("0"); 
	
	private final String syncKey;

	public SyncKey(String syncKey) {
		this.syncKey = syncKey;
	}
	
	public String getSyncKey() {
		return syncKey;
	}

	@Override
	public final int hashCode(){
		return Objects.hashCode(syncKey);
	}
	
	@Override
	public final boolean equals(Object object){
		if (object instanceof SyncKey) {
			SyncKey that = (SyncKey) object;
			return Objects.equal(this.syncKey, that.syncKey);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("syncKey", syncKey)
			.toString();
	}
}
