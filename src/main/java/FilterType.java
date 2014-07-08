
import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * This enum is serialized, take care of changes done there for older version compatibility
 */
public enum FilterType {

	ALL_ITEMS("0"),
	ONE_DAY_BACK("1"),
	THREE_DAYS_BACK("2"),
	ONE_WEEK_BACK("3"),
	TWO_WEEKS_BACK("4"),
	ONE_MONTHS_BACK("5"),
	THREE_MONTHS_BACK("6"),
	SIX_MONTHS_BACK("7"),
	FILTER_BY_NO_INCOMPLETE_TASKS("8");

	private final String specificationValue;
	
	private FilterType(String specificationValue) {
		this.specificationValue = specificationValue;
	}
	
	public String asSpecificationValue() {
		return specificationValue;
	}
	
	public static FilterType fromSpecificationValue(String specificationValue) {
		if (specValueToEnum.containsKey(specificationValue)) {
			return specValueToEnum.get(specificationValue);
		}
		throw new IllegalArgumentException("No filter type for '" + specificationValue + "'");
	}

	public Date getFilteredDateTodayAtMidnight() {
		return getFilteredDate(DateTime.now().withTimeAtStartOfDay().toDate());
	}

	public Date getFilteredDate(Date fromDate) {
		DateTime fromUTCDate = new DateTime(fromDate).withZone(DateTimeZone.UTC);
		switch (this) {
		case ALL_ITEMS:
			return new Date(1);
		case ONE_DAY_BACK:
			return fromUTCDate.minusDays(1).toDate();
		case THREE_DAYS_BACK:
			return fromUTCDate.minusDays(3).toDate();
		case ONE_WEEK_BACK:
			return fromUTCDate.minusWeeks(1).toDate();
		case TWO_WEEKS_BACK:
			return fromUTCDate.minusWeeks(2).toDate();
		case ONE_MONTHS_BACK:
			return fromUTCDate.minusMonths(1).toDate();
		case THREE_MONTHS_BACK:
			return fromUTCDate.minusMonths(3).toDate();
		case SIX_MONTHS_BACK:
			return fromUTCDate.minusMonths(6).toDate();
		case FILTER_BY_NO_INCOMPLETE_TASKS:
			return fromDate;
		}
		throw new IllegalStateException("No filtered date available");
	}

	private static Map<String, FilterType> specValueToEnum;
	
	static {
		Builder<String, FilterType> builder = ImmutableMap.builder();
		for (FilterType filterType : values()) {
			builder.put(filterType.specificationValue, filterType);
		}
		specValueToEnum = builder.build();
	}
}