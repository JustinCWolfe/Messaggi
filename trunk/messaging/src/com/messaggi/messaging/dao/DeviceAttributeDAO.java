package com.messaggi.messaging.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

import com.messaggi.dao.DAOException;
import com.messaggi.dao.PersistManager;
import com.messaggi.dao.PersistManager.Insert;
import com.messaggi.dao.PersistManager.Select;
import com.messaggi.dao.PersistManager.Update;
import com.messaggi.messaging.domain.DeviceAttribute;

public class DeviceAttributeDAO implements Insert<DeviceAttribute>, Select<DeviceAttribute>, Update<DeviceAttribute>
{
    private enum SelectBy {
        ID, KEY, ID_AND_KEY
    };

    private class Messages
    {
        private static final String NO_VALID_SELECT_BY_TYPES_IN_PROTOTYPES_MESSAGE = "Must specify id or id+key as select criteria.";
    }

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
        //domainObject.getKey().setKey(rs.getString("device_attribute_key_key"));
        domainObject.setValue(rs.getString("value"));
    }

    private static EnumSet<SelectBy> computeSelectBySetForPrototypes(List<DeviceAttribute> prototypes)
    {
        EnumSet<SelectBy> selectBySet = EnumSet.noneOf(SelectBy.class);
        for (DeviceAttribute prototype : prototypes) {
            if (prototype.getDevice().getId() != null) {
                selectBySet.add(SelectBy.ID);
            }
            if (prototype.getKey() != null) {
                selectBySet.add(SelectBy.KEY);
            }
        }
        return selectBySet;
    }

    // Select implementation
    @Override
    public String getSelectStoredProcedure(List<DeviceAttribute> prototypes) throws DAOException
    {
        EnumSet<SelectBy> selectBySet = computeSelectBySetForPrototypes(prototypes);
        String storedProcedure = null;
        if (selectBySet.contains(SelectBy.ID) && selectBySet.contains(SelectBy.KEY)) {
            selectBy = SelectBy.ID_AND_KEY;
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
        if (selectBy == SelectBy.ID) {
            stmt.setLong(1, domainObject.getDevice().getId());
        } else {
            stmt.setLong(1, domainObject.getDevice().getId());
            stmt.setString(2, domainObject.getKey());
        }
    }

    @Override
    public void afterSelectInitializeDomainObjectFromResultSet(ResultSet rs, DeviceAttribute domainObject)
        throws SQLException
    {
        //domainObject.setId(rs.getLong("id"));
        //domainObject.setName(rs.getString("name"));
        //domainObject.setEmail(rs.getString("email"));
        //domainObject.setPhone(rs.getString("phone"));
        //domainObject.setPhoneParsed(rs.getString("phone_parsed"));
        //domainObject.setPassword(rs.getString("password"));
        //domainObject.setLocale(Locale.forLanguageTag(rs.getString("locale")));
        //domainObject.setActive(rs.getBoolean("active"));
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
        //stmt.setLong(1, domainObject.getId());
        //stmt.setString(2, domainObject.getName());
        //stmt.setString(3, domainObject.getEmail());
        //stmt.setString(4, domainObject.getPhone());
        //stmt.setString(5, domainObject.getPassword());
        //String locale = (domainObject.getLocale() != null) ? domainObject.getLocale().toLanguageTag() : null;
        //stmt.setString(6, locale);
        //stmt.setBoolean(7, domainObject.getActive());
    }

    public List<DeviceAttribute> insertDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException
    {
        return PersistManager.insert(this, newVersions);
    }

    public List<DeviceAttribute> selectDeviceAttribute(List<DeviceAttribute> prototypes) throws DAOException
    {
        return PersistManager.select(this, prototypes);
    }

    public void updateDeviceAttribute(List<DeviceAttribute> newVersions) throws DAOException
    {
        PersistManager.update(this, newVersions);
    }
}
