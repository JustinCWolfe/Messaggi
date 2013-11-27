--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.1
-- Dumped by pg_dump version 9.3.1
-- Started on 2013-11-27 16:18:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 193 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2094 (class 0 OID 0)
-- Dependencies: 193
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 552 (class 1247 OID 17749)
-- Name: application_platform_attribute_param; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE application_platform_attribute_param AS (
	key character varying,
	value character varying
);


ALTER TYPE public.application_platform_attribute_param OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 17750)
-- Name: application_platform_msg_log; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_msg_log (
    id bigint NOT NULL,
    application_platform_id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    msg_count integer NOT NULL
);


ALTER TABLE public.application_platform_msg_log OWNER TO jcw_dev;

--
-- TOC entry 2095 (class 0 OID 0)
-- Dependencies: 171
-- Name: COLUMN application_platform_msg_log.date; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN application_platform_msg_log.date IS 'Note that all timezones are stored in UTC format.';


--
-- TOC entry 558 (class 1247 OID 17755)
-- Name: application_platform_param; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE application_platform_param AS (
	id bigint,
	application_id bigint,
	platform_id bigint,
	token uuid,
	attributes application_platform_attribute_param[],
	msg_logs application_platform_msg_log
);


ALTER TYPE public.application_platform_param OWNER TO postgres;

--
-- TOC entry 561 (class 1247 OID 17758)
-- Name: device_attribute_param; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE device_attribute_param AS (
	key character varying,
	value character varying
);


ALTER TYPE public.device_attribute_param OWNER TO postgres;

--
-- TOC entry 564 (class 1247 OID 17761)
-- Name: device_param; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE device_param AS (
	id bigint,
	active boolean,
	application_platform_ids bigint[],
	attributes device_attribute_param[]
);


ALTER TYPE public.device_param OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 17762)
-- Name: application; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    name character varying(120) NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.application OWNER TO jcw_dev;

--
-- TOC entry 2096 (class 0 OID 0)
-- Dependencies: 175
-- Name: TABLE application; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE application IS 'Applications will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 176 (class 1259 OID 17766)
-- Name: user; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE "user" (
    id bigint NOT NULL,
    name character varying(120) NOT NULL,
    email character varying(256) NOT NULL,
    phone character varying(50) NOT NULL,
    phone_parsed character varying(50) NOT NULL,
    password character varying(512) NOT NULL,
    locale character varying(10) NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public."user" OWNER TO jcw_dev;

--
-- TOC entry 2097 (class 0 OID 0)
-- Dependencies: 176
-- Name: TABLE "user"; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE "user" IS 'Users will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 2098 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN "user".phone; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone IS 'Phone number as entered by the user.';


--
-- TOC entry 2099 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN "user".phone_parsed; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone_parsed IS 'User entered phone number with all non-numeric data stripped. 

* used for indexing purposes';


--
-- TOC entry 2100 (class 0 OID 0)
-- Dependencies: 176
-- Name: COLUMN "user".password; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".password IS 'SHA-512 salted password.';


--
-- TOC entry 574 (class 1247 OID 17775)
-- Name: user_application_param; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE user_application_param AS (
	"user" "user",
	applications application[]
);


ALTER TYPE public.user_application_param OWNER TO postgres;

--
-- TOC entry 577 (class 1247 OID 17778)
-- Name: user_param; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE user_param AS (
	id bigint,
	email character varying
);


ALTER TYPE public.user_param OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 17779)
-- Name: application_platform_attribute; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_attribute (
    application_platform_id bigint NOT NULL,
    application_platform_attribute_key_key character varying(20) NOT NULL,
    value character varying(1024) NOT NULL
);


ALTER TABLE public.application_platform_attribute OWNER TO jcw_dev;

--
-- TOC entry 206 (class 1255 OID 17785)
-- Name: m_get_application_platform_attribute_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_attribute_by_id(i_id bigint) RETURNS SETOF application_platform_attribute
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform_attribute%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform_attribute.application_platform_id, 
		public.application_platform_attribute.application_platform_attribute_key_key, 
		public.application_platform_attribute.value
	from public.device_attribute
	where public.device_attribute.application_platform_id = i_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_attribute_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 207 (class 1255 OID 17786)
-- Name: m_get_application_platform_attribute_by_id_and_key(bigint, character varying); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_attribute_by_id_and_key(i_id bigint, i_key character varying) RETURNS SETOF application_platform_attribute
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform_attribute%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform_attribute.application_platform_id, 
		public.application_platform_attribute.application_platform_attribute_key_key, 
		public.application_platform_attribute.value
	from public.application_platform_attribute
	where public.application_platform_attribute.application_platform_id = i_id and 
		public.application_platform_attribute.application_platform_attribute_key_key = i_key
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_attribute_by_id_and_key(i_id bigint, i_key character varying) OWNER TO jcw_dev;

--
-- TOC entry 180 (class 1259 OID 17787)
-- Name: application_platform; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform (
    id bigint NOT NULL,
    application_id bigint NOT NULL,
    platform_id bigint NOT NULL,
    token uuid NOT NULL
);


ALTER TABLE public.application_platform OWNER TO jcw_dev;

--
-- TOC entry 2101 (class 0 OID 0)
-- Dependencies: 180
-- Name: TABLE application_platform; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE application_platform IS 'Link table between application and platform.

An application can be linked to multiple platforms.
A platform can be used across multiple applications.';


--
-- TOC entry 2102 (class 0 OID 0)
-- Dependencies: 180
-- Name: COLUMN application_platform.token; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN application_platform.token IS 'Token used by Messaggi service generated off the application platform attributes.  This token will be generated when the service consumer registers their application for the mobile platforms it supports and provides the relevant application/platform identifiers (bundle id for example).

This token will be passed for subsequent service calls.';


--
-- TOC entry 208 (class 1255 OID 17790)
-- Name: m_get_application_platform_by_application_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_by_application_id(i_id bigint) RETURNS SETOF application_platform
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform.id, public.application_platform.application_id, public.application_platform.platform_id, 
		public.application_platform.token
	from public.application_platform
	where public.application_platform.application_id = i_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_by_application_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 209 (class 1255 OID 17791)
-- Name: m_get_application_platform_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_by_id(i_id bigint) RETURNS SETOF application_platform
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform.id, public.application_platform.application_id, public.application_platform.platform_id, 
		public.application_platform.token
	from public.application_platform
	where public.application_platform.application_id = i_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 210 (class 1255 OID 17792)
-- Name: m_get_application_platform_by_platform_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_by_platform_id(i_id bigint) RETURNS SETOF application_platform
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform.id, public.application_platform.application_id, public.application_platform.platform_id, 
		public.application_platform.token
	from public.application_platform
	where public.application_platform.application_id = i_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_by_platform_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 181 (class 1259 OID 17793)
-- Name: application_platform_device; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_device (
    application_platform_id bigint NOT NULL,
    device_id bigint NOT NULL
);


ALTER TABLE public.application_platform_device OWNER TO jcw_dev;

--
-- TOC entry 211 (class 1255 OID 17796)
-- Name: m_get_application_platform_device_by_application_platform_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_device_by_application_platform_id(i_id bigint) RETURNS SETOF application_platform_device
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform_device%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform_device.application_platform_id, public.application_platform_device.device_id
	from public.application_platform_device
	where public.application_platform_device.application_platform_id = i_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_device_by_application_platform_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 212 (class 1255 OID 17797)
-- Name: m_get_application_platform_device_by_device_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_device_by_device_id(i_id bigint) RETURNS SETOF application_platform_device
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform_device%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform_device.application_platform_id, public.application_platform_device.device_id
	from public.application_platform_device
	where public.application_platform_device.device_id = i_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_device_by_device_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 218 (class 1255 OID 17925)
-- Name: m_get_device(device_param[]); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_device(device_param[]) RETURNS SETOF device_param
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $_$
DECLARE
	i_device_param device_param;
	loop_device device;
	loop_attributes device_attribute[];
	return_value device_param;
BEGIN
	foreach i_device_param in array $1
	loop
		for loop_device in
		select public.device.id, public.device.active
		from public.device d
		left join public.application_platform_device apd on d.id = apd.device_id
		where (d.id = i_device_param.id or i_device_param.id is null) and		
			(apd.application_platform_id = any(i_device_param.application_platform_ids) or i_device_param.application_platform_ids is null) and
			d.active = true
		loop		
			loop_attributes := (select array_agg(attributes) from
			(select loop_device.device_id, public.device_attribute.device_attribute_key_key, public.device_attribute.value
			from public.device_attribute
			where public.device_attribute.device_id = loop_device.id) as attributes);

			select loop_device.id, loop_device.active, loop_device.application_platform_attributes, loop_attributes into return_value;
			return next return_value;
		end loop;
	end loop;
	return;
END; $_$;


ALTER FUNCTION public.m_get_device(device_param[]) OWNER TO jcw_dev;

--
-- TOC entry 213 (class 1255 OID 17811)
-- Name: m_get_user(user_param[]); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_user(user_param[]) RETURNS SETOF user_application_param
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $_$
DECLARE
	i_user_param user_param;
	loop_user "user";
	loop_applications application[];
	return_value user_application_param;
BEGIN
	foreach i_user_param in array $1
	loop
		for loop_user in
		select public.user.id, public.user.name, public.user.email, public.user.phone, public.user.phone_parsed, 
			public.user.password, public.user.locale, public.user.active	
		from public.user
		where (public.user.id = i_user_param.id or i_user_param.id is null) and 
			(public.user.email = i_user_param.email or i_user_param.email is null) and
			public.user.active = true
		loop		
			loop_applications := (select array_agg(apps) from 
			(select public.application.id, public.application.user_id, public.application.name, public.application.active
			from public.application
			where public.application.user_id = loop_user.id and public.application.active = true) as apps);
			
			select loop_user, loop_applications into return_value;			
			return next return_value;
		end loop;		
	end loop;
	return;
END; $_$;


ALTER FUNCTION public.m_get_user(user_param[]) OWNER TO jcw_dev;

--
-- TOC entry 214 (class 1255 OID 17812)
-- Name: m_save_application(application[]); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_save_application(application[]) RETURNS SETOF application
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $_$
DECLARE
	i_application public.application%ROWTYPE;
	i_active public.application.active%TYPE;
	return_value public.application%ROWTYPE;
BEGIN	
	foreach i_application in array $1
	loop
		i_active := coalesce(i_application.active, true);	

		update public.application set user_id = i_application.user_id, name = i_application.name, active = i_active
		where id = i_application.id;

		if not found then
			insert into public.application (id, user_id, name, active) 
			values (DEFAULT, i_application.user_id, i_application.name, i_active)
			returning id into i_application.id;
		end if;
		
		select i_application.id, i_application.user_id, i_application.name, i_active into return_value;
		return next return_value;
	end loop;
	return;
END; $_$;


ALTER FUNCTION public.m_save_application(application[]) OWNER TO jcw_dev;

--
-- TOC entry 215 (class 1255 OID 17813)
-- Name: m_save_application_platform(application_platform_param[]); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_save_application_platform(application_platform_param[]) RETURNS SETOF application_platform_param
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $_$
DECLARE
	i_application_platform_param application_platform_param;
	i_application_platform_attribute_param application_platform_attribute_param;
	attributes application_platform_attribute_param[];
	i_msg_log application_platform_msg_log%ROWTYPE;
	msg_logs application_platform_msg_log[];
	return_value application_platform_param;
BEGIN	
	foreach i_application_platform_param in array $1
	loop
		update public.application_platform set application_id = i_application_platform_param.application_id,
			platform_id = i_application_platform_param.application_id, 
			token = i_application_platform_param.token
		where id = i_application_platform_param.id;

		if not found then	
			insert into public.application_platform (id, application_id, platform_id, token)
			values (DEFAULT, i_application_platform_param.application_id, i_application_platform_param.platform_id, i_application_platform_param.token)
			returning id into i_application_platform_param.id;
		end if;

		attributes := null;
		foreach i_application_platform_attribute_param in array i_application_platform_param.attributes
		loop
			update public.application_platform_attributes set value = i_application_platform_attribute_param.value
			where application_platform_id = i_application_platform_param.id and application_platform_attribute_key_key = i_application_platform_attribute_param.key;

			if not found then
				insert into public.application_platform_attributes (application_platform_id, application_platform_attribute_key_key, value)
				values (i_application_platform_param.id, i_application_platform_attribute_param.key, i_application_platform_attribute_param.value);
			end if;
						
			attributes := array_append(attributes, (i_application_platform_param.id, i_application_platform_attribute_param.key, 
				i_application_platform_attribute_param.value)::public.application_platform_attribute_param);
		end loop;

		msg_logs := null;
		foreach i_msg_log in array i_application_platform_param.msg_logs
		loop
			update public.application_platform_msg_log set application_platform_id = i_application_platform_param.id, date = i_msg_log.date,
				msg_count = i_msg_log.msg_count
			where id = i_msg_log.id;

			if not found then
				insert into public.application_platform_msg_log (id, application_platform_id, date, msg_count)
				values (DEFAULT, i_msg_log.application_platform_id, i_msg_log.date, i_msg_log.msg_count)
				returning id into i_msg_log.id;
			end if;

			msg_logs := array_append(msg_logs, (i_msg_log.id, i_msg_log.application_platform_id, i_msg_log.date, i_msg_log.msg_count)::public.application_platform_msg_log);
		end loop;
		
		select i_application_platform_param.id, i_application_platform_param.application_id, i_application_platform_param.platform_id,
			i_application_platform_param.token, attributes, msg_logs into return_value; 		
		return next return_value;
	end loop;
	return;
END; $_$;


ALTER FUNCTION public.m_save_application_platform(application_platform_param[]) OWNER TO jcw_dev;

--
-- TOC entry 217 (class 1255 OID 17814)
-- Name: m_save_device(device_param[]); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_save_device(device_param[]) RETURNS SETOF device_param
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $_$
DECLARE
	i_device_param device_param;
	i_active public.device.active%TYPE;
	i_application_platform_id bigint;
	i_device_attribute_param device_attribute_param;
	return_value device_param;
	attributes device_attribute_param[];
BEGIN	
	foreach i_device_param in array $1
	loop
		i_active := coalesce(i_device_param.active, true);	

		update public.device set active = i_active
		where id = i_device_param.id;

		if not found then	
			insert into public.device (id, active)
			values (nextval('public.device_id_seq'), i_active);
			i_device_param.id := currval('public.device_id_seq');

			insert into public.application_platform_device (application_platform_id, device_id)
			values (i_device_param.application_plaform_id, i_device_param.id);	
		end if;

		foreach i_application_platform_id in array i_device_param.application_platform_ids
		loop		
			if not exists (select * from application_platform_device where device_id = i_device_param.id and 
					application_platform_id = i_application_platform_id) then
				insert into public.application_platform_device (application_platform_id, device_id)
				values (i_application_platform_id, i_device_param.id);	
			end if;
		end loop;

		attributes := null;
		foreach i_device_attribute_param in array i_device_param.attributes
		loop
			update public.device_attributes set value = i_device_attribute_param.value
			where device_id = i_device_param.id and device_attribute_key_key = i_device_attribute_param.key;

			if not found then
				insert into public.device_attributes (device_id, device_attribute_key_key, value)
				values (i_device_param.id, i_device_attribute_param.key, i_device_attribute_param.value);
			end if;
						
			attributes := array_append(attributes, (i_device_param.id, i_device_attribute_param.key, 
				i_device_attribute_param.value)::public.device_attribute_param);
		end loop;
		
		select i_device_param.id, i_active, i_device_param.application_plaform_id, attributes into return_value; 		
		return next return_value;
	end loop;
	return;
END; $_$;


ALTER FUNCTION public.m_save_device(device_param[]) OWNER TO jcw_dev;

--
-- TOC entry 216 (class 1255 OID 17815)
-- Name: m_save_user("user"[]); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_save_user("user"[]) RETURNS SETOF "user"
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $_$
DECLARE
	i_user public.user%ROWTYPE;
	i_active public.user.active%TYPE;
	i_phone_parsed user.phone_parsed%TYPE;	
	return_value public.user%ROWTYPE;
BEGIN	
	foreach i_user in array $1
	loop
		i_active := coalesce(i_user.active, true);
		i_phone_parsed := regexp_replace(i_user.phone, '[^0-9]', '', 'g');		

		update public.user set name = i_user.name, email = i_user.email, phone = i_user.phone, phone_parsed = i_phone_parsed, 
			password = i_user.password, locale = i_user.locale, active = i_active
		where id = i_user.id;

		if not found then			
			insert into public.user (id, name, email, phone, phone_parsed, password, locale, active) 
			values (nextval('public.user_id_seq'), i_user.name, i_user.email, i_user.phone, i_phone_parsed, 
				i_user.password, i_user.locale, i_active);
			i_user.id := currval('public.user_id_seq');			
		end if;

		select i_user.id, i_user.name, i_user.email, i_user.phone, i_phone_parsed, 
				i_user.password, i_user.locale, i_active into return_value; 		
		return next return_value;
	end loop;
	return;
END; $_$;


ALTER FUNCTION public.m_save_user("user"[]) OWNER TO jcw_dev;

--
-- TOC entry 184 (class 1259 OID 17816)
-- Name: application_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2103 (class 0 OID 0)
-- Dependencies: 184
-- Name: application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_id_seq OWNED BY application.id;


--
-- TOC entry 185 (class 1259 OID 17818)
-- Name: application_platform_attribute_key; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_attribute_key (
    key character varying(20) NOT NULL,
    description character varying(1024) NOT NULL
);


ALTER TABLE public.application_platform_attribute_key OWNER TO jcw_dev;

--
-- TOC entry 186 (class 1259 OID 17824)
-- Name: application_platform_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_platform_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2104 (class 0 OID 0)
-- Dependencies: 186
-- Name: application_platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_id_seq OWNED BY application_platform.id;


--
-- TOC entry 187 (class 1259 OID 17826)
-- Name: application_platform_msg_log_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_platform_msg_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_platform_msg_log_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2105 (class 0 OID 0)
-- Dependencies: 187
-- Name: application_platform_msg_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_msg_log_id_seq OWNED BY application_platform_msg_log.id;


--
-- TOC entry 183 (class 1259 OID 17806)
-- Name: device; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device (
    id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.device OWNER TO jcw_dev;

--
-- TOC entry 182 (class 1259 OID 17798)
-- Name: device_attribute; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device_attribute (
    device_id bigint NOT NULL,
    device_attribute_key_key character varying(20) NOT NULL,
    value character varying(1024) NOT NULL
);


ALTER TABLE public.device_attribute OWNER TO jcw_dev;

--
-- TOC entry 2106 (class 0 OID 0)
-- Dependencies: 182
-- Name: COLUMN device_attribute.value; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN device_attribute.value IS 'Stores the value for the associated platform specific device identifier key.

For example:

An iOS phone will have a device token so the key will be token, the value will be XYZ.
An Android phone will have an device id so the key will be device_id, the value will be 123.';


--
-- TOC entry 188 (class 1259 OID 17828)
-- Name: device_attribute_key; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device_attribute_key (
    key character varying(20) NOT NULL,
    description character varying(1024) NOT NULL
);


ALTER TABLE public.device_attribute_key OWNER TO jcw_dev;

--
-- TOC entry 2107 (class 0 OID 0)
-- Dependencies: 188
-- Name: COLUMN device_attribute_key.key; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN device_attribute_key.key IS 'Stores the key for the platform specific device identifier.

For example:

An iOS phone will have a device token so the key will be token.
An Android phone will have an device id so the key will be device_id.
';


--
-- TOC entry 189 (class 1259 OID 17834)
-- Name: device_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.device_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2108 (class 0 OID 0)
-- Dependencies: 189
-- Name: device_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE device_id_seq OWNED BY device.id;


--
-- TOC entry 190 (class 1259 OID 17836)
-- Name: platform; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE platform (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    service_name character varying(100) NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.platform OWNER TO jcw_dev;

--
-- TOC entry 2109 (class 0 OID 0)
-- Dependencies: 190
-- Name: TABLE platform; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE platform IS 'Platforms will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 191 (class 1259 OID 17840)
-- Name: platform_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.platform_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2110 (class 0 OID 0)
-- Dependencies: 191
-- Name: platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE platform_id_seq OWNED BY platform.id;


--
-- TOC entry 192 (class 1259 OID 17842)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2111 (class 0 OID 0)
-- Dependencies: 192
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_id_seq OWNED BY "user".id;


--
-- TOC entry 1917 (class 2604 OID 17844)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application ALTER COLUMN id SET DEFAULT nextval('application_id_seq'::regclass);


--
-- TOC entry 1920 (class 2604 OID 17845)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform ALTER COLUMN id SET DEFAULT nextval('application_platform_id_seq'::regclass);


--
-- TOC entry 1915 (class 2604 OID 17846)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_msg_log ALTER COLUMN id SET DEFAULT nextval('application_platform_msg_log_id_seq'::regclass);


--
-- TOC entry 1922 (class 2604 OID 17847)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device ALTER COLUMN id SET DEFAULT nextval('device_id_seq'::regclass);


--
-- TOC entry 1924 (class 2604 OID 17848)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY platform ALTER COLUMN id SET DEFAULT nextval('platform_id_seq'::regclass);


--
-- TOC entry 1919 (class 2604 OID 17849)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);


--
-- TOC entry 2071 (class 0 OID 17762)
-- Dependencies: 175
-- Data for Name: application; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application (id, user_id, name, active) FROM stdin;
3	3	app2	t
4	3	app1	t
5	3	app2	t
6	3	app1	t
7	3	app2	t
1	3	app1	f
2	3	app1	f
17	3	app10.1	f
18	3	app10.1	f
19	3	app10.1	f
21	3	app22	t
22	2	app123	t
\.


--
-- TOC entry 2112 (class 0 OID 0)
-- Dependencies: 184
-- Name: application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_id_seq', 22, true);


--
-- TOC entry 2074 (class 0 OID 17787)
-- Dependencies: 180
-- Data for Name: application_platform; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform (id, application_id, platform_id, token) FROM stdin;
\.


--
-- TOC entry 2073 (class 0 OID 17779)
-- Dependencies: 179
-- Data for Name: application_platform_attribute; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_attribute (application_platform_id, application_platform_attribute_key_key, value) FROM stdin;
\.


--
-- TOC entry 2079 (class 0 OID 17818)
-- Dependencies: 185
-- Data for Name: application_platform_attribute_key; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_attribute_key (key, description) FROM stdin;
\.


--
-- TOC entry 2075 (class 0 OID 17793)
-- Dependencies: 181
-- Data for Name: application_platform_device; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_device (application_platform_id, device_id) FROM stdin;
\.


--
-- TOC entry 2113 (class 0 OID 0)
-- Dependencies: 186
-- Name: application_platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_id_seq', 1, false);


--
-- TOC entry 2070 (class 0 OID 17750)
-- Dependencies: 171
-- Data for Name: application_platform_msg_log; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_msg_log (id, application_platform_id, date, msg_count) FROM stdin;
\.


--
-- TOC entry 2114 (class 0 OID 0)
-- Dependencies: 187
-- Name: application_platform_msg_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_msg_log_id_seq', 1, false);


--
-- TOC entry 2077 (class 0 OID 17806)
-- Dependencies: 183
-- Data for Name: device; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device (id, active) FROM stdin;
3	t
4	t
1	f
2	f
\.


--
-- TOC entry 2076 (class 0 OID 17798)
-- Dependencies: 182
-- Data for Name: device_attribute; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device_attribute (device_id, device_attribute_key_key, value) FROM stdin;
\.


--
-- TOC entry 2082 (class 0 OID 17828)
-- Dependencies: 188
-- Data for Name: device_attribute_key; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device_attribute_key (key, description) FROM stdin;
\.


--
-- TOC entry 2115 (class 0 OID 0)
-- Dependencies: 189
-- Name: device_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('device_id_seq', 4, true);


--
-- TOC entry 2084 (class 0 OID 17836)
-- Dependencies: 190
-- Data for Name: platform; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY platform (id, name, service_name, active) FROM stdin;
\.


--
-- TOC entry 2116 (class 0 OID 0)
-- Dependencies: 191
-- Name: platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('platform_id_seq', 1, false);


--
-- TOC entry 2072 (class 0 OID 17766)
-- Dependencies: 176
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY "user" (id, name, email, phone, phone_parsed, password, locale, active) FROM stdin;
2	Justin C. Wolfe	jcw_222@yahoo.com	617-549-2403	6175492403	password1	US-en	t
3	Armen Solakhyan	asolakhyan@hotmail.com	(617) 335-9866	6173359866	password2	US-en	t
155	User2376969	User2376969@yahoo.com	961-489-3471	9614893471	a6561220-faf9-4378-9305-b4dc1e3e33ff	en_US	f
740	User6319851	User6319851@yahoo.com	417-732-2488	4177322488	8c0e7a0d-43c6-49fd-8947-96d128c62bc7	en_US	f
17	User 6016440	User 6016440@yahoo.com	{0}-{1}-{2}	012	5c260daf-ab28-4476-9a1a-777cd3e4b893	en-US	t
18	User 5576972	User 5576972@yahoo.com	{0}-{1}-{2}	012	df191502-2c1d-4595-bbd5-17b5033f487d	en-US	t
19	User 5716297	User 5716297@yahoo.com	{0}-{1}-{2}	012	d57dc257-2d54-4ce1-982e-e02f6b4b0e13	it-IT	t
20	User 5552328	User 5552328@yahoo.com	424-428-8862	4244288862	18e3e942-231d-43c5-8dae-7a1c3b161d30	en-US	t
21	User 1518687	User 1518687@yahoo.com	854-736-2878	8547362878	555b5ace-0fdd-4ba5-b7f1-1d3be09dfce2	en-US	t
22	User 6150436	User 6150436@yahoo.com	454-267-1718	4542671718	cbaffe4c-6bad-40c6-97f3-1f8f241d1224	it-IT	t
24	User 7897040	User 7897040@yahoo.com	564-912-2767	5649122767	d22e6fb8-1a78-469d-8f98-73ff34e29b4c	en-US	t
25	User 1765148	User 1765148@yahoo.com	788-341-8144	7883418144	c038ee3b-51b4-4a6d-a5a8-dc0fc9165bb4	en-US	t
26	User 5180834	User 5180834@yahoo.com	231-244-5769	2312445769	c1e791a7-341e-4e8a-ab0f-393e0b74fe61	it-IT	t
163	User8531362	User8531362@yahoo.com	858-963-8012	8589638012	9f026f84-0148-4975-b4bf-6dd9221b098c	en_US	f
369	User603086	User603086@yahoo.com	684-488-722	684488722	74f45575-323a-49a0-9e60-b0e104ff80ba	en_US	t
30	User 2926297	User 2926297@yahoo.com	751-968-2709	7519682709	0a3fad32-7b36-46aa-b84f-e11bfbde2d5f	en-US	t
31	User9717055	User9717055@yahoo.com	583-209-9697	5832099697	789ec1ad-6a91-42ab-869c-6e9f54a169e2	en-US	t
32	User2856117	User2856117@yahoo.com	421-360-3224	4213603224	8cf6927f-30a1-4a89-b64c-afa930c83d18	it-IT	t
95	User7214948	User7214948@yahoo.com	807-915-9104	8079159104	e69d2ccc-d375-4890-a9d5-8748a64cc1ba	en_US	f
36	User7864434	User7864434@yahoo.com	165-536-3448	1655363448	f89901e2-aa5a-4357-864a-4906464bc78a	en-US	t
96	User7955275	User7955275@yahoo.com	435-552-9801	4355529801	def8c6d8-65a9-48d7-9c6d-4339199a7869	it_IT	f
555	User2855188	User2855188@yahoo.com	829-750-8942	8297508942	37975e60-c5bf-4dc6-8174-c15d96c19395	en_US	f
103	User6897469	User6897469@yahoo.com	746-712-8006	7467128006	70eac9f5-e3a9-4ede-9cb5-f729daa2c483	en_US	f
104	User8473376	User8473376@yahoo.com	989-136-1068	9891361068	4587f836-045d-45bc-bb0b-133f955f3eb0	it_IT	f
632	User8435458	User8435458@yahoo.com	216-607-6014	2166076014	1b20f75b-d028-4629-8cd5-29d898abf02e	en_US	t
244	User3900595	User3900595@yahoo.com	550-584-6086	5505846086	3c16d262-9c9c-40d1-8bb0-47c458590af3	en_US	f
385	User1214177	User1214177@yahoo.com	206-484-3276	2064843276	f9e56792-06c9-45a4-90e0-a9fe924a9ff5	en_US	t
246	User3267501	User3267501@yahoo.com	164-929-7013	1649297013	02df2d3d-0275-478e-85f0-49cfefaf8548	en_US	f
248	User8914541	User8914541@yahoo.com	174-635-67	17463567	4cb0a2aa-de3a-4873-8e17-644dd0b07e35	en_US	f
568	User2383456	User2383456@yahoo.com	575-100-9725	5751009725	a9f3e8c7-353e-431f-9d56-622252d06332	en_US	f
396	User2602958	User2602958@yahoo.com	814-100-3253	8141003253	ba63acc3-9e51-43fe-a514-05e9b6000579	en_US	t
726	User7753927	User7753927@yahoo.com	832-297-5919	8322975919	7ab339e3-412e-4453-be9b-172621251a82	en_US	f
600	User2157175	User2157175@yahoo.com	113-351-1974	1133511974	d47f601a-27c0-47cc-9f4b-379275f84271	en_US	f
299	User1137189	User1137189@yahoo.com	611-632-3987	6116323987	818e4509-54e4-4863-aea3-b9835a3c5d3a	en_US	t
300	User6555014	User6555014@yahoo.com	860-892-4403	8608924403	215c5958-87af-4cfa-bc47-2841b9a08ed2	it_IT	t
763	justin C. wolfe	jcw_2221@yahoo.com	617-549-2403	6175492403	some-password	US-en	t
766	Justin C. Wolfe	jcw_2222@yahoo.com	617-549-2403	6175492403	some-password	US-en	t
767	Justin C. Wolfe	jcw_22222@yahoo.com	617-549-2403	6175492403	some-password	US-en	t
\.


--
-- TOC entry 2117 (class 0 OID 0)
-- Dependencies: 192
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('user_id_seq', 767, true);


--
-- TOC entry 1929 (class 2606 OID 17851)
-- Name: application-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application
    ADD CONSTRAINT "application-pkey" PRIMARY KEY (id);


--
-- TOC entry 1940 (class 2606 OID 17853)
-- Name: application_platform-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "application_platform-pkey" PRIMARY KEY (id);


--
-- TOC entry 1938 (class 2606 OID 17855)
-- Name: application_platform_attribute-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_attribute
    ADD CONSTRAINT "application_platform_attribute-pkey" PRIMARY KEY (application_platform_id, application_platform_attribute_key_key);


--
-- TOC entry 1950 (class 2606 OID 17857)
-- Name: application_platform_attribute_key-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_attribute_key
    ADD CONSTRAINT "application_platform_attribute_key-pkey" PRIMARY KEY (key);


--
-- TOC entry 1943 (class 2606 OID 17859)
-- Name: application_platform_device-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_device
    ADD CONSTRAINT "application_platform_device-pkey" PRIMARY KEY (application_platform_id, device_id);


--
-- TOC entry 1948 (class 2606 OID 17861)
-- Name: device-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device
    ADD CONSTRAINT "device-pkey" PRIMARY KEY (id);


--
-- TOC entry 1946 (class 2606 OID 17863)
-- Name: device_attribute-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device_attribute
    ADD CONSTRAINT "device_attribute-pkey" PRIMARY KEY (device_id, device_attribute_key_key);


--
-- TOC entry 1952 (class 2606 OID 17865)
-- Name: device_attribute_key-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device_attribute_key
    ADD CONSTRAINT "device_attribute_key-pkey" PRIMARY KEY (key);


--
-- TOC entry 1954 (class 2606 OID 17867)
-- Name: platform-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY platform
    ADD CONSTRAINT "platform-pkey" PRIMARY KEY (id);


--
-- TOC entry 1932 (class 2606 OID 17869)
-- Name: user-email-key; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT "user-email-key" UNIQUE (email);


--
-- TOC entry 1935 (class 2606 OID 17871)
-- Name: user-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT "user-pkey" PRIMARY KEY (id);


--
-- TOC entry 1927 (class 2606 OID 17873)
-- Name: user_msg_log-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_msg_log
    ADD CONSTRAINT "user_msg_log-pkey" PRIMARY KEY (id);


--
-- TOC entry 1941 (class 1259 OID 17874)
-- Name: application_platform-platform_id-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "application_platform-platform_id-idx" ON application_platform USING btree (platform_id);


--
-- TOC entry 1936 (class 1259 OID 17875)
-- Name: application_platform_attribute-application_platform_attribute_k; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "application_platform_attribute-application_platform_attribute_k" ON application_platform_attribute USING btree (application_platform_attribute_key_key);


--
-- TOC entry 1925 (class 1259 OID 17876)
-- Name: application_platform_msg_log-application_platform_id-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "application_platform_msg_log-application_platform_id-idx" ON application_platform_msg_log USING btree (application_platform_id);


--
-- TOC entry 1944 (class 1259 OID 17877)
-- Name: device_attribute-device_attribute_key_key-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "device_attribute-device_attribute_key_key-idx" ON device_attribute USING btree (device_attribute_key_key);


--
-- TOC entry 1930 (class 1259 OID 17878)
-- Name: fki_application-user_id-fkey; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "fki_application-user_id-fkey" ON application USING btree (user_id);


--
-- TOC entry 1933 (class 1259 OID 17879)
-- Name: user-phone_parsed-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "user-phone_parsed-idx" ON "user" USING btree (phone_parsed);


--
-- TOC entry 1956 (class 2606 OID 17880)
-- Name: application-user_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application
    ADD CONSTRAINT "application-user_id-fkey" FOREIGN KEY (user_id) REFERENCES "user"(id) ON UPDATE CASCADE;


--
-- TOC entry 1959 (class 2606 OID 17885)
-- Name: application_platform-application_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "application_platform-application_id-fkey" FOREIGN KEY (application_id) REFERENCES application(id) ON UPDATE CASCADE;


--
-- TOC entry 1960 (class 2606 OID 17890)
-- Name: application_platform-platform_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "application_platform-platform_id-fkey" FOREIGN KEY (platform_id) REFERENCES platform(id) ON UPDATE CASCADE;


--
-- TOC entry 1957 (class 2606 OID 17895)
-- Name: application_platform_attribute-ap_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_attribute
    ADD CONSTRAINT "application_platform_attribute-ap_id-fkey" FOREIGN KEY (application_platform_id) REFERENCES application_platform(id) ON UPDATE CASCADE;


--
-- TOC entry 1958 (class 2606 OID 17900)
-- Name: application_platform_attribute-apak_key-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_attribute
    ADD CONSTRAINT "application_platform_attribute-apak_key-fkey" FOREIGN KEY (application_platform_attribute_key_key) REFERENCES application_platform_attribute_key(key) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1955 (class 2606 OID 17905)
-- Name: application_platform_msg_log-application_platform_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_msg_log
    ADD CONSTRAINT "application_platform_msg_log-application_platform_id-fkey" FOREIGN KEY (application_platform_id) REFERENCES application_platform(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1961 (class 2606 OID 17910)
-- Name: device_attribute-device_attribute_key_key-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device_attribute
    ADD CONSTRAINT "device_attribute-device_attribute_key_key-fkey" FOREIGN KEY (device_attribute_key_key) REFERENCES device_attribute_key(key) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1962 (class 2606 OID 17915)
-- Name: device_attribute-device_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device_attribute
    ADD CONSTRAINT "device_attribute-device_id-fkey" FOREIGN KEY (device_id) REFERENCES device(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2093 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-11-27 16:18:41

--
-- PostgreSQL database dump complete
--

