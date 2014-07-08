
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Snapshot implements Serializable {
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private FilterType filterType;
		private Long uidNext;
		private Collection<Email> emails;
		
		private Builder() {
			emails = Lists.newArrayList();
		}
		
		public Builder filterType(FilterType filterType) {
			this.filterType = filterType;
			return this;
		}
		
		public Builder uidNext(long uidNext) {
			this.uidNext = uidNext;
			return this;
		}
		
		public Builder emails(Collection<Email> emails) {
			this.emails = ImmutableList.copyOf(emails);
			return this;
		}
		
		public Builder addEmail(Email email) {
			emails.add(email);
			return this;
		}
		
		public Snapshot build() {
			Preconditions.checkState(filterType != null, "filterType can't be null or empty");
			Preconditions.checkState(uidNext != null, "uidNext can't be null");
			return new Snapshot(filterType, uidNext, emails);
		}
	}
	
	private static final long serialVersionUID = -8674207692296869251L;
	
	private final FilterType filterType;
	private final long uidNext;
	private final Collection<Email> emails;
	private final MessageSet messageSet;
	
	protected Snapshot(FilterType filterType, long uidNext, Collection<Email> emails) {
		this.filterType = filterType;
		this.uidNext = uidNext;
		this.emails = emails;
		this.messageSet = generateMessageSet();
	}

	protected MessageSet generateMessageSet() {
		MessageSet.Builder builder = MessageSet.builder();
		for (Email email : emails) {
			builder.add(email.getUid());
		}
		return builder.build();
	}

	public boolean containsAllIds(List<String> serverIds) throws InvalidServerId {
		Preconditions.checkNotNull(serverIds);
		for (String serverId: serverIds) {
			Integer mailUid = new ServerId(serverId).getItemId();
			if (mailUid == null) {
				throw new ProtocolException(String.format("ServerId '%s' must reference an Item", serverId));
			}
			if (!messageSet.contains(mailUid)) {
				return false;
			}
		}
		return true;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public long getUidNext() {
		return uidNext;
	}

	public Collection<Email> getEmails() {
		return emails;
	}

	public MessageSet getMessageSet() {
		return messageSet;
	}
	
	@Override
	public final int hashCode(){
		return Objects.hashCode(filterType, uidNext, emails, messageSet);
	}
	
	@Override
	public final boolean equals(Object object){
		if (object instanceof Snapshot) {
			Snapshot that = (Snapshot) object;
			return  
				Objects.equal(this.filterType, that.filterType) && 
				Objects.equal(this.uidNext, that.uidNext) && 
				Objects.equal(this.emails, that.emails) &&  
				Objects.equal(this.messageSet, that.messageSet); 
		}
		return false;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("filterType", filterType)
			.add("uidNext", uidNext)
			.add("emails", emails)
			.add("messageSet", messageSet)
			.toString();
	}
}
