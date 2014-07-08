
import java.io.Serializable;
import java.util.Date;

import com.google.common.base.Objects;

public class Email implements WindowingItem, Serializable {

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private long uid;
		private boolean read;
		private Date date;
		private boolean answered;
		
		private Builder() {
			answered = false;
		}
		
		public Builder uid(long uid) {
			this.uid = uid;
			return this;
		}

		public Builder read(boolean read) {
			this.read = read;
			return this;
		}
		
		public Builder date(Date date) {
			this.date = date;
			return this;
		}
		
		public Builder answered(boolean answered) {
			this.answered = answered;
			return this;
		}
		
		public Email build() {
			return new Email(uid, read, date, answered);
		}

	}
	
	private static final long serialVersionUID = 9022743605981571920L;
	
	private final long uid;
	private final boolean read;
	private final Date date;
	private boolean answered;
	
	private Email(long uid, boolean read, Date date, boolean answered) {
		this.uid = uid;
		this.read = read;
		this.date = date;
		this.answered = answered;
	}

	public long getUid() {
		return uid;
	}
	
	public boolean isRead() {
		return read;
	}
	
	public Date getDate() {
		return date;
	}

	public boolean isAnswered() {
		return answered;
	}
	
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	
	@Override
	public final int hashCode(){
		return Objects.hashCode(uid, read, date, answered);
	}
	
	@Override
	public final boolean equals(Object object){
		if (object instanceof Email) {
			Email that = (Email) object;
			return Objects.equal(this.uid, that.uid)
				&& Objects.equal(this.read, that.read)
				&& Objects.equal(this.date, that.date)
				&& Objects.equal(this.answered, that.answered);
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("uid", uid)
			.add("read", read)
			.add("date", date)
			.add("answered", answered)
			.toString();
	}
	
}
