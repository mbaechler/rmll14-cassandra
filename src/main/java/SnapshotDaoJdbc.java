import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

public class SnapshotDaoJdbc implements SnapshotDao {

	private Connection connection;

	public SnapshotDaoJdbc(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void put(SnapshotKey snapshotKey, Snapshot snapshot) {
		try {
			int snapshotId = insertSnapshot(snapshotKey, snapshot);
			insertEmails(snapshotId, snapshot);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	private int insertSnapshot(SnapshotKey snapshotKey, Snapshot snapshot) throws SQLException {
		try (
				PreparedStatement statement = 
				connection.prepareStatement("INSERT INTO snapshot (syncKey, deviceId, collectionId, filterType, uidNext) VALUES (?, ?, ?, ?, ?)")) {
			int index = 1;
			statement.setString(index++, snapshotKey.getSyncKey().getSyncKey());
			statement.setString(index++, snapshotKey.getDeviceId().getDeviceId());
			statement.setInt(index++, snapshotKey.getCollectionId());
			statement.setString(index++, snapshot.getFilterType().asSpecificationValue());
			statement.setLong(index++, snapshot.getUidNext());
			statement.execute();
			try (ResultSet generatedId = statement.getGeneratedKeys()) {
				generatedId.first();
				return generatedId.getInt(1);
			}
		}
	}
	
	private void insertEmails(int snapshotId, Snapshot snapshot) throws SQLException {
		try (PreparedStatement statement =
				connection.prepareStatement("INSERT INTO email_snapshot (snapshotId, uid, read, internalDate, answered) VALUES (?, ?, ?, ?, ?)")) {
			for (Email email: snapshot.getEmails()) {
				int index = 1;
				statement.setInt(index++, snapshotId);
				statement.setLong(index++, email.getUid());
				statement.setBoolean(index++, email.isRead());
				statement.setTimestamp(index++, new Timestamp(email.getDate().getTime()));
				statement.setBoolean(index++, email.isAnswered());
				statement.addBatch();
			}
			statement.executeBatch();
		}
	}

	@Override
	public Snapshot get(SnapshotKey snapshotKey) {
		try (
				PreparedStatement statement = 
				connection.prepareStatement("SELECT id, syncKey, deviceId, collectionId, filterType, uidNext FROM snapshot WHERE "
						+ "syncKey=? AND deviceId=? AND collectionId=?")) {
			int index = 1;
			statement.setString(index++, snapshotKey.getSyncKey().getSyncKey());
			statement.setString(index++, snapshotKey.getDeviceId().getDeviceId());
			statement.setInt(index++, snapshotKey.getCollectionId());
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					Collection<Email> emails = loadEmails(result.getInt("id"));
					return Snapshot.builder()
						.filterType(FilterType.fromSpecificationValue(result.getString("filterType")))
						.uidNext(result.getLong("uidNext"))
						.emails(emails)
						.build();
				}
				return null;
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	
	private Collection<Email> loadEmails(int snapshotId) throws SQLException {
		try (
				PreparedStatement statement = 
					connection.prepareStatement("SELECT uid, read, internalDate, answered FROM email_snapshot WHERE snapshotId=?")) {
			statement.setInt(1, snapshotId);
			try (ResultSet result = statement.executeQuery()) {
				List<Email> emails = Lists.newArrayList();
				while (result.next()) {
					emails.add(Email.builder()
							.answered(result.getBoolean("answered"))
							.read(result.getBoolean("read"))
							.date(new java.util.Date(result.getTimestamp("internalDate").getTime()))
							.uid(result.getLong("uid"))
							.build());
				}
				return emails;
			}
		}
	}
}
