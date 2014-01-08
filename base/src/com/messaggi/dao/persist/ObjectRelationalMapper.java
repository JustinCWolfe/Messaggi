package com.messaggi.dao.persist;

import java.sql.ResultSet;
import java.util.List;

public interface ObjectRelationalMapper
{
    public interface Get<T>
    {
        String getGetStoredProcedure();

        void afterGetInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws Exception;
    }

    public interface GetAll<T>
    {
        String getGetAllStoredProcedure();

        void afterGetAllInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws Exception;
    }

    public interface Save<T>
    {
        String getSaveStoredProcedure();

        void afterSaveInitializeDomainObjectsFromResultSet(ResultSet rs, List<T> domainObjects) throws Exception;
    }

    public static class Factory
    {
        public enum DomainObjectType {
            Application, ApplicationPlatform, ApplicationPlatformMsgLog, Device, User
        };

        @SuppressWarnings("unchecked")
        public static <T> T create(DomainObjectType adapterType) throws Exception
        {
            switch (adapterType) {
                case Application:
                    return (T) new ApplicationMapper();
                case ApplicationPlatform:
                    return (T) new ApplicationPlatformMapper();
                case ApplicationPlatformMsgLog:
                    return (T) new ApplicationPlatformMsgLogMapper();
                case Device:
                    return (T) new DeviceMapper();
                case User:
                    return (T) new UserMapper();
                default:
                    throw new UnsupportedOperationException();

            }
        }
    }
}

