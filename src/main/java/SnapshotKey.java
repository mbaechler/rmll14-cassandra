

import java.io.Serializable;


import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class SnapshotKey implements Serializable{

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private SyncKey syncKey;
		private DeviceId deviceId;
		private Integer collectionId;
		
		private Builder() {}
		
		public Builder syncKey(SyncKey syncKey) {
			this.syncKey = syncKey;
			return this;
		}
		public Builder deviceId(DeviceId deviceId) {
			this.deviceId = deviceId;
			return this;
		}
		
		public Builder collectionId(Integer collectionId) {
			this.collectionId = collectionId;
			return this;
		}

		public SnapshotKey build() {
			Preconditions.checkArgument(syncKey != null, "syncKey can't be null or empty");
			Preconditions.checkArgument(deviceId != null, "deviceId can't be null or empty");
			Preconditions.checkArgument(collectionId != null, "collectionId can't be null or empty");
			return new SnapshotKey(syncKey, deviceId, collectionId);
		}
	}
	
	private final SyncKey syncKey;
	private final DeviceId deviceId;
	private final Integer collectionId;

	private SnapshotKey(SyncKey syncKey, DeviceId deviceId, Integer collectionId) {
		this.syncKey = syncKey;
		this.deviceId = deviceId;
		this.collectionId = collectionId;
	}

	public SyncKey getSyncKey() {
		return syncKey;
	}

	public DeviceId getDeviceId() {
		return deviceId;
	}

	public Integer getCollectionId() {
		return collectionId;
	}

	@Override
	public final int hashCode(){
		return Objects.hashCode(syncKey, deviceId, collectionId);
	}
	
	@Override
	public final boolean equals(Object object){
		if (object instanceof SnapshotKey) {
			SnapshotKey that = (SnapshotKey) object;
			return Objects.equal(this.syncKey, that.syncKey)
				&& Objects.equal(this.deviceId, that.deviceId)
				&& Objects.equal(this.collectionId, that.collectionId);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("syncKey", syncKey)
			.add("deviceId", deviceId)
			.add("collectionId", collectionId)
			.toString();
	}
	
}
