package com.messaggi.persistence.dao.postgresql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import com.messaggi.persistence.dao.DAOException;
import com.messaggi.persistence.dao.DeviceAttributeDAO;
import com.messaggi.persistence.dao.PersistManager;
import com.messaggi.persistence.dao.PersistManager.Insert;
import com.messaggi.persistence.dao.PersistManager.Select;
import com.messaggi.persistence.dao.PersistManager.Update;
import com.messaggi.persistence.domain.DeviceAttribute;

public class PostgreSQLDeviceAttributeDAO implements DeviceAttributeDAO, Insert<DeviceAttribute>,
        Select<DeviceAttribute>, Update<DeviceAttribute>
{
    private SelectBy selectBy;

    // Insert implementation
    @Override
    public String getInsertStoredProcedure()
    {
        return "{call m_create_device_attribute(?,?,?)}";
    }

    @Override
    public void beforeInsertInitializeStatementFromDomainObject(PreparedStatement stmt, DeviceAttribute domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getDevice().getId());
        stmt.setString(2, domainObject.getKey());
        stmt.setString(3, domainObject.getValue());
    }

    @Override
    public void afterInsertInitializeDomainObjectFromResultSet(ResultSet rs, DeviceAttribute domainObject)
        throws SQLException
    {
        domainObject.getDevice().setId(rs.getLong("device_id"));
        domainObject.getKey().setKey(rs.getString("device_attribute_key_key"));
        domainObject.setValue(rs.getString("value"));
    }

    private static SelectBy computeSelectBySetForPrototypes(List<DeviceAttribute> prototypes)
    {
        for (DeviceAttribute prototype : prototypes) {
            if (prototype.getDevice().getId() != null && prototype.getKey().getKey() != null) {
                return SelectBy.ID_AND_KEY;
            }
 else if (prototype.getKey().getKey() != null) {
                return SelectBy.ID;
            }
        }
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<DeviceAttribute> prototypes) throws DAOException
    {
        EnumSet<SelectBy> selectBySet = computeSelectBySetForPrototypes(prototypes);
        String storedProcedure = null;
        if (selectBySet.contains(SelectBy.ID) && selectBySet.contains(SelectBy.KEY)) {
            selectBy = SelectBy.BOTH;
            storedProcedure = "{call m_get_device_attribute_by_id_and_key(?,?)}";
        } else if (selectBySet.contains(SelectBy.ID)) {
            selectBy = SelectBy.ID;
            storedProcedure = "{call m_get_device_attribute_by_id(?)}";
        } else {
            throw new DAOException(DAOException.ErrorCode.INVALID_CRITERIA,
                    DeviceAttributeDAO.Messages.NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE);
        }
        return storedProcedure;
    }

    @Override
    public void beforeSelectInitializeStatementFromDomainObject(PreparedStatement stmt, DeviceAttribute domainObject)
        throws SQLException
    {
        if (selectBy == SelectBy.EMAIL) {
            stmt.setString(1, domainObject.getEmail());
        } else {
            stmt.setLong(1, domainObject.getId());
        }
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, DeviceAttribute domainObject)
        throws SQLException
    {
        domainObject.setId(rs.getLong("id"));
        domainObject.setName(rs.getString("name"));
        domainObject.setEmail(rs.getString("email"));
        domainObject.setPhone(rs.getString("phone"));
        domainObject.setPhoneParsed(rs.getString("phone_parsed"));
        domainObject.setPassword(rs.getString("password"));
        domainObject.setLocale(Locale.forLanguageTag(rs.getString("locale")));
        domainObject.setActive(rs.getBoolean("active"));
    }

    // Update implementation
    @Override
    public String getUpdateStoredProcedure()
    {
        return "{call m_update_user(?,?,?,?,?,?,?)}";
    }

    @Override
    public void beforeUpdateInitializeStatementFromDomainObject(PreparedStatement stmt, DeviceAttribute domainObject)
        throws SQLException
    {
        stmt.setLong(1, domainObject.getId());
        stmt.setString(2, domainObject.getName());
        stmt.setString(3, domainObject.getEmail());
        stmt.setString(4, domainObject.getPhone());
        stmt.setString(5, domainObject.getPassword());
        String locale = (domainObject.getLocale() != null) ? domainObject.getLocale().toLanguageTag() : null;
        stmt.setString(6, locale);
        stmt.setBoolean(7, domainObject.getActive());
    }

    @Override
    public List<DeviceAttribute> insertDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    @Override
    public List<DeviceAttribute> selectDeviceAttribute(List<DeviceAttribute> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    @Override
    public void updateDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }
}
