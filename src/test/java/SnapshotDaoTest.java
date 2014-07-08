
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.UUID;

import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Closer;
import com.google.common.io.Resources;

public class SnapshotDaoTest {

	public static final String DB_URL = "jdbc:h2:mem:daotest";
	private SnapshotDaoJdbc testee;
	private Connection connection;
	
	@Before
	public void setup() throws Exception {
		Class.forName("org.h2.Driver");
		connection = DriverManager.getConnection(DB_URL);
		importSchema("schema.sql");
		testee = new SnapshotDaoJdbc(connection);
	}
	
	@Test
	public void insertedDataShouldBeReturnByGet() {
		SnapshotKey key = SnapshotKey.builder().syncKey(new SyncKey(UUID.randomUUID().toString()))
			.collectionId(1)
			.deviceId(new DeviceId("myDevice"))
			.build();
		Snapshot snapshot = Snapshot.builder().filterType(FilterType.ALL_ITEMS)
			.uidNext(1234L)
			.addEmail(Email.builder().answered(false).read(true).uid(1212L).date(new Date()).build())
			.addEmail(Email.builder().answered(false).read(false).uid(1552L).date(new Date()).build())
			.build();
		testee.put(key, snapshot);
		assertThat(testee.get(key)).isEqualTo(snapshot);
	}
	
	/**
	 * Imports the SQL schema in the database.<br />
	 * This is done through the help of H2's {@link RunScript} tool.
	 * 
	 * @throws Exception If an error occurs while importing the schema.
	 */
	private void importSchema(String schema) throws Exception {
		Closer closer = Closer.create();
		Reader reader = null;
		InputStream stream = closer.register(Resources.getResource(schema).openStream());
		try {
			reader = closer.register(new InputStreamReader(stream, Charsets.UTF_8));
			RunScript.execute(connection, reader);
		}
		finally {
			closer.close();
		}
	}
}
