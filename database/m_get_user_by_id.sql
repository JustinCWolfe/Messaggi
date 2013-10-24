-- Function: "m_get_user_by_id"(bigint)

-- DROP FUNCTION "m_get_user_by_id"(bigint);

CREATE OR REPLACE FUNCTION "m_get_user_by_id"(i_id bigint)
  RETURNS public.user AS
$BODY$
DECLARE
	return_value public.user%ROWTYPE;
BEGIN
	-- Note that we explicitly don't include the password
	select public.user.id, public.user.name, public.user.email, public.user.phone, public.user.phone_parsed, 
		public.user.locale, public.user.active into return_value	
	from public.user
	where public.user.id = i_id;
	return return_value;
END; $BODY$
  LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER
  COST 10;
ALTER FUNCTION "m_get_user_by_id"(bigint)
  OWNER TO jcw_dev;
