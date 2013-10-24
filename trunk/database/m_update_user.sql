-- Function: "m_update_user"(bigint, character varying, character varying, character varying, character varying, character varying, boolean)

-- DROP FUNCTION "m_update_user"(bigint, character varying, character varying, character varying, character varying, character varying, boolean);

CREATE OR REPLACE FUNCTION "m_update_user"(i_id bigint, i_name character varying, i_email character varying, i_phone character varying, i_password character varying, i_locale character varying, i_active boolean)
  RETURNS void AS
$BODY$
DECLARE	
	i_phone_parsed user.phone_parsed%TYPE;
BEGIN
	i_active := true;
	i_phone_parsed := regexp_replace(i_phone, '[^0-9]', '', 'g');

	update public.user set name = i_name, email = i_email, phone = i_phone, phone_parsed = i_phone_parsed, 
		password = i_password, locale = i_locale, active = i_active
	where id = i_id;
END; $BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 10;
ALTER FUNCTION "m_update_user"(bigint, character varying, character varying, character varying, character varying, character varying, boolean)
  OWNER TO jcw_dev;