package pluto.helloWorld;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseFileStorage {
    private final static String JDBC_URL = "jdbc:oracle:thin:baseball_dba/welcome1@oasis.local:1521/orclpdb1";
    private final static String INSERT_SQL = "INSERT INTO MLB_FILE_STORAGE (FILE_ID, FILE_NAME, FILE_PATH, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, FILE_STORAGE) VALUES (MLB_PK_SEQ.NEXTVAL, ?, ?, SYSDATE, 'MING', SYSDATE, 'MING', EMPTY_BLOB())";

    private final static String UPDATE_BLOB_SQL = "SELECT FILE_STORAGE FROM MLB_FILE_STORAGE WHERE FILE_NAME = ? FOR UPDATE";
    public DatabaseFileStorage() {

    }

    public void saveFile(String fileName) throws Exception {

        Connection conn = DriverManager.getConnection(JDBC_URL);

        if(conn != null) {

            insertBlobViaSelectForUpdate(conn, fileName, "/var/tmp/mingtest");

            //conn.commit();
            conn.close();
        }

    }

    private void insertBlobViaSelectForUpdate(final Connection conn, final String fileName, final String filePath)
            throws SQLException, IOException {

        try (final PreparedStatement pstmt = conn.prepareStatement(String.format(INSERT_SQL))) {

            pstmt.setString(1, fileName);
            pstmt.setString(2, filePath);
            pstmt.executeUpdate();
        }

        try (final PreparedStatement pstmt = conn.prepareStatement(String.format(UPDATE_BLOB_SQL))) {

            pstmt.setString(1, fileName);
            try (final ResultSet rset = pstmt.executeQuery()) {

                while (rset.next()) {

                    final Blob blob = rset.getBlob(1);
                    try (final OutputStream out = new BufferedOutputStream(blob.setBinaryStream(1L))) {

                        Path path = Paths.get(filePath + "/" + fileName);
                        byte valueBuf [] = Files.readAllBytes(path);
                        out.write(valueBuf);
                    }
                }
            }

        }
    }

}