

public interface SnapshotDao {
	
	Snapshot get(SnapshotKey snapshotKey);

	void put(SnapshotKey snapshotKey, Snapshot snapshot);

}
