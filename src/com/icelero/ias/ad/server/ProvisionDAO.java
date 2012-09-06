package com.icelero.ias.ad.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.icelero.ias.ad.server.utilities.CryptUtilities;
import com.icelero.ias.ad.server.utilities.DatabaseUtilities;
import com.icelero.ias.ad.server.utilities.StatementUtilities;
import com.icelero.ias.ad.shared.Client;
import com.icelero.ias.ad.shared.Config;
import com.icelero.ias.ad.shared.Policy;
import com.icelero.ias.ad.shared.Provision;
import com.icelero.ias.ad.shared.Software;
import com.icelero.ias.ad.shared.User;

public class ProvisionDAO {
	public User isValidUser(User user) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "select password from dashboard_user where name = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, user.getName());
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (CryptUtilities.checkPassword(user.getPassword(), rs.getString("password")))
					return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return null;
	}

	public Provision getSearchResult(String msisdn) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String query = "select id, state, policyid  from account where id = ? ";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, msisdn);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Provision provisionData = new Provision();
				provisionData.setMsisdn(rs.getString("id"));
				provisionData.setState(rs.getString("state"));
				provisionData.setPolicyid(rs.getString("policyid"));
				provisionData.setClientIDS(getClientIDS(msisdn));
				provisionData.setPolicy(getPolicyDetails(rs.getString("policyid")));
				return provisionData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return null;
	}

	public List<String> getClientIDS(String accountid) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<String> clientIDS = new ArrayList<String>();
		String query = "select clientid from device where accountid= ? ";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, accountid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				clientIDS.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return clientIDS;
	}

	public Client getClientDetails(String clientID) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String query = "select clientid from device where clientid = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, clientID);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Client client = new Client();
				client.setClientID(rs.getString("clientid"));
				return client;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return null;
	}

	public Policy getPolicyDetails(String policyId) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Policy policy = null;
		String query = "select id, name, description, mediaserver_start_time, mediaserver_stop_time, bandwidth, "
				+ "prefetch_content_type, prefetch_start_time, prefetch_stop_time from policy where id = ? ";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, policyId);
			rs = stmt.executeQuery();
			policy = new Policy();
			while (rs.next()) {
				policy.setPolicyID(rs.getString("id"));
				policy.setDescription(rs.getString("description"));
				policy.setName(rs.getString("name"));
				policy.setMediaServerStartTime(rs.getInt("mediaserver_start_time"));
				policy.setMediaServerStopTime(rs.getInt("mediaserver_stop_time"));
				policy.setBandwidth(rs.getInt("bandwidth"));
				policy.setPrefetchContentType(rs.getString("prefetch_content_type"));
				policy.setPrefetchStartTime(rs.getInt("prefetch_start_time"));
				policy.setPrefetchStopTime(rs.getInt("prefetch_stop_time"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return policy;
	}

	public List<Software> getSoftwareIDS() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		List<Software> softwareIDS = new ArrayList<Software>();
		String query = "select id,version from software where status='ACTIVE' order by modified_time desc";
		try {
			con = DatabaseUtilities.getConnection();
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				Software softwareID = new Software();
				softwareID.setSoftwareID(rs.getString("id"));
				softwareID.setVersion(rs.getString("version"));
				softwareIDS.add(softwareID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(pst);
			DatabaseUtilities.closeConnection(con);
		}
		return softwareIDS;
	}

	public Software getSoftwareDetails(String softwareID) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String query = "select id, version, build, mandatory, release_date, notes ,url from software where id = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, softwareID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Software software = new Software();
				software.setSoftwareID(rs.getString("id"));
				software.setVersion(rs.getString("version"));
				software.setBuild(rs.getString("build"));
				software.setMandatory(rs.getString("mandatory"));
				software.setReleaseDate(rs.getDate("release_date"));
				software.setNotes(rs.getString("notes"));
				software.setUrl(rs.getString("url"));

				return software;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return null;
	}

	public boolean deleteClientID(String clientID) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "delete from device where clientid = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, clientID);
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public boolean addSoftwareDetails(Software software, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "insert into software(id, version, notes, build, release_date, mandatory, created_time, modified_time, created_by_user, modified_by_user, url) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, software.getSoftwareID());
			StatementUtilities.setString(stmt, 2, software.getVersion());
			StatementUtilities.setString(stmt, 3, software.getNotes());
			StatementUtilities.setString(stmt, 4, software.getBuild());
			StatementUtilities.setUtilDate(stmt, 5, software.getReleaseDate());
			StatementUtilities.setString(stmt, 6, software.getMandatory());
			StatementUtilities.setTimeStamp(stmt, 7, new Date());
			StatementUtilities.setTimeStamp(stmt, 8, new Date());
			StatementUtilities.setString(stmt, 9, user);
			StatementUtilities.setString(stmt, 10, user);
			StatementUtilities.setString(stmt, 11, software.getUrl());
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public boolean updateSoftwareDetails(Software softwareDetails, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "update software set version = ?, notes = ?, build = ?, mandatory = ?, release_date = ?, modified_time = ?, modified_by_user= ?, url= ?  where id = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, softwareDetails.getVersion());
			StatementUtilities.setString(stmt, 2, softwareDetails.getNotes());
			StatementUtilities.setString(stmt, 3, softwareDetails.getBuild());
			StatementUtilities.setString(stmt, 4, softwareDetails.getMandatory());
			StatementUtilities.setUtilDate(stmt, 5, softwareDetails.getReleaseDate());
			StatementUtilities.setTimeStamp(stmt, 6, new Date());
			StatementUtilities.setString(stmt, 7, user);
			StatementUtilities.setString(stmt, 8, softwareDetails.getUrl());
			StatementUtilities.setString(stmt, 9, softwareDetails.getSoftwareID());
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public boolean updateAccountState(String msisdn, String state) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "update account set state= ? ,modified_time= ? , modified_by_user= ? where id = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, state);
			StatementUtilities.setString(stmt, 2, msisdn);
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public List<Software> searchSoftwareID(String softwareID) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Software> softwareIDS = new ArrayList<Software>();
		String query = "select id,version from software where id like ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, softwareID + "%");
			rs = stmt.executeQuery();
			while (rs.next()) {
				Software software = new Software();
				software.setSoftwareID(rs.getString(1));
				software.setVersion(rs.getString(2));
				softwareIDS.add(software);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return softwareIDS;
	}

	public boolean deleteSoftwareID(String softwareID) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "delete from software where id = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, softwareID);
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public List<Policy> getPolicyIDS() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		List<Policy> policyIDS = new ArrayList<Policy>();
		String query = "select id, name from policy order by modified_time desc";
		try {
			con = DatabaseUtilities.getConnection();
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				Policy policyID = new Policy();
				policyID.setPolicyID(rs.getString("id"));
				policyID.setName(rs.getString("name"));
				policyIDS.add(policyID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(pst);
			DatabaseUtilities.closeConnection(con);
		}
		return policyIDS;
	}

	public List<Policy> searchPolicyID(String policyID) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Policy> policyIDS = new ArrayList<Policy>();
		String query = "select id, name from policy where id like ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, "%" + policyID + "%");
			rs = stmt.executeQuery();
			while (rs.next()) {
				Policy policy = new Policy();
				policy.setPolicyID(rs.getString("id"));
				policy.setName(rs.getString("name"));
				policyIDS.add(policy);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return policyIDS;
	}

	public boolean deletePolicyID(String policyID) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "delete from policy where id = ? ";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, policyID);
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public boolean addPolicyDetails(Policy policyDetails, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "insert into policy(id, name, description, mediaserver_start_time, mediaserver_stop_time, bandwidth, prefetch_content_type, prefetch_start_time, prefetch_stop_time, created_time, modified_time, created_by_user, modified_by_user) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, policyDetails.getPolicyID());
			StatementUtilities.setString(stmt, 2, policyDetails.getName());
			StatementUtilities.setString(stmt, 3, policyDetails.getDescription());
			StatementUtilities.setInt(stmt, 4, policyDetails.getMediaServerStartTime());
			StatementUtilities.setInt(stmt, 5, policyDetails.getMediaServerStopTime());
			StatementUtilities.setInt(stmt, 6, policyDetails.getBandwidth());
			StatementUtilities.setString(stmt, 7, policyDetails.getPrefetchContentType());
			StatementUtilities.setInt(stmt, 8, policyDetails.getPrefetchStartTime());
			StatementUtilities.setInt(stmt, 9, policyDetails.getPrefetchStopTime());
			StatementUtilities.setTimeStamp(stmt, 10, new Date());
			StatementUtilities.setTimeStamp(stmt, 11, new Date());
			StatementUtilities.setString(stmt, 12, user);
			StatementUtilities.setString(stmt, 13, user);
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;

	}

	public boolean updatePolicyDetails(Policy policyDetails, String user) {
		Connection con = null;
		PreparedStatement stmt = null;

		String query = "update policy set name= ?,description= ?, mediaserver_start_time= ?, mediaserver_stop_time= ?, bandwidth= ?, prefetch_content_type= ?,"
				+ "prefetch_start_time= ?, prefetch_stop_time= ?, modified_time= ?, modified_by_user= ? where id=?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, policyDetails.getName());
			StatementUtilities.setString(stmt, 2, policyDetails.getDescription());
			StatementUtilities.setInt(stmt, 3, policyDetails.getMediaServerStartTime());
			StatementUtilities.setInt(stmt, 4, policyDetails.getMediaServerStopTime());
			StatementUtilities.setInt(stmt, 5, policyDetails.getBandwidth());
			StatementUtilities.setString(stmt, 6, policyDetails.getPrefetchContentType());
			StatementUtilities.setInt(stmt, 7, policyDetails.getPrefetchStartTime());
			StatementUtilities.setInt(stmt, 8, policyDetails.getPrefetchStopTime());
			StatementUtilities.setTimeStamp(stmt, 9, new Date());
			StatementUtilities.setString(stmt, 10, user);
			StatementUtilities.setString(stmt, 11, policyDetails.getPolicyID());
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public boolean archiveSoftware(String softwareID) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "update software set status='INACTIVE' , modified_by_user=?, modified_time=?  where id=?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, "");
			StatementUtilities.setTimeStamp(stmt, 2, new Date());
			StatementUtilities.setString(stmt, 3, softwareID);
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public boolean setConfigDetails(Config config, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "insert into config(`key`, value, created_by_user, modified_by_user, created_timestamp, modified_timestamp) values(? ,? ,? ,? ,? ,?)";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, config.getKey());
			StatementUtilities.setString(stmt, 2, config.getValue());
			StatementUtilities.setString(stmt, 3, user);
			StatementUtilities.setString(stmt, 4, user);
			StatementUtilities.setTimeStamp(stmt, 5, new Date());
			StatementUtilities.setTimeStamp(stmt, 6, new Date());
			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public List<Config> getConfigKeys() {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		List<Config> keysList = new ArrayList<Config>();
		String query = "select id,`key` , value from config order by modified_timestamp desc";
		try {
			con = DatabaseUtilities.getConnection();
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				Config config = new Config();
				config.setKey(rs.getString("key"));
				config.setValue(rs.getString("value"));
				config.setValue(rs.getString("id"));
				keysList.add(config);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(pst);
			DatabaseUtilities.closeConnection(con);
		}
		return keysList;
	}

	public boolean updateConfigDetails(Config config, String user) {
		Connection con = null;
		PreparedStatement stmt = null;
		String query = "update config set `key`= ? ,value= ? ,modified_time= ? ,modified_by_user= ? where id = ?";
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);

			StatementUtilities.setString(stmt, 1, config.getKey());
			StatementUtilities.setString(stmt, 2, config.getValue());
			StatementUtilities.setTimeStamp(stmt, 3, new Date());
			StatementUtilities.setString(stmt, 4, config.getValue());
			StatementUtilities.setString(stmt, 5, config.getValue());

			int i = stmt.executeUpdate();
			if (i > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return false;
	}

	public Config getConfigDetails(String configKey) {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String query = "select id , `key`, value from config  where `key` = ?";
		System.out.println("CONFIG KEY"+configKey);
		try {
			con = DatabaseUtilities.getConnection();
			stmt = con.prepareStatement(query);
			StatementUtilities.setString(stmt, 1, configKey);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Config config = new Config();
				config.setId(rs.getInt("id"));
				config.setKey(rs.getString("key"));
				config.setValue(rs.getString("value"));
				System.out.println(config);
				return config;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtilities.closeResultSet(rs);
			DatabaseUtilities.closeStatement(stmt);
			DatabaseUtilities.closeConnection(con);
		}
		return null;
	}

}
