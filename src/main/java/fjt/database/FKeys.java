package fjt.database;

public class FKeys {

    private String tableName;
    private String fkTableName;
    private String fkColumnName;
    private Integer fkKeySeq;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TABLE(").append(tableName).append(") contains ").append("FORIEGN_KEY(").append(this.fkTableName).append(":").append(this.fkColumnName).append(":").append(this.fkKeySeq.toString()).append(")");
        return (sb.toString());
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
    }

    public String getFkColumnName() {
        return fkColumnName;
    }

    public void setFkColumnName(String fkColumnName) {
        this.fkColumnName = fkColumnName;
    }

    public Integer getFkKeySeq() {
        return fkKeySeq;
    }

    public void setFkKeySeq(Integer fkKeySeq) {
        this.fkKeySeq = fkKeySeq;
    }
}
