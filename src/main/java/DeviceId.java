import java.io.Serializable;

import com.google.common.base.Objects;

public class DeviceId implements Serializable {
	
	private static final long serialVersionUID = -4097463369254130710L;
	
	private final String deviceId;

	public DeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public final int hashCode(){
		return Objects.hashCode(deviceId);
	}
	
	@Override
	public final boolean equals(Object object){
		if (object instanceof DeviceId) {
			DeviceId that = (DeviceId) object;
			return Objects.equal(this.deviceId, that.deviceId);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("deviceId", deviceId)
			.toString();
	}
}
