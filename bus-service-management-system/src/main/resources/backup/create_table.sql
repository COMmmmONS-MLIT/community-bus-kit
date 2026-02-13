-- テーブル作成クエリ
-- 本システムに必要なテーブル一式を作成します
-- DROPして再作成となるため既存のテーブルがある場合はご注意ください

-- 事業者マスタ
DROP TABLE if exists agencies CASCADE;

CREATE TABLE agencies (
  business_corp_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , name character varying(50) NOT NULL
  , disp_name character varying(50) NOT NULL
  , kana_name character varying(50)
  , en_name character varying(200)
  , department_name character varying(100)
  , "user" character varying(50)
  , phone_number character varying(15)
  , mail_address character varying(256)
  , time_table_url character varying(256) NOT NULL
  , fare_url character varying(256)
  , operation_company_no character varying(13) NOT NULL
  , transportation_type smallint
  , provider_organization_name character varying(256) NOT NULL
  , provider_organization_url character varying(256) NOT NULL
  , provider_organization_contact_email character varying(256)
  , provider_organization_contact_url character varying(256)
  , access_key character(16) NOT NULL
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT agencies_PKC PRIMARY KEY (business_corp_id)
) ;

-- 便ダイヤ明細
DROP TABLE if exists bus_daiyas CASCADE;

CREATE TABLE bus_daiyas (
  bus_daiya_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , bus_service_id integer NOT NULL
  , line_route_bus_stop_id integer NOT NULL
  , bus_stop_arrival_time character varying(4) NOT NULL
  , bus_stop_departure_time character varying(4) NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT bus_daiyas_PKC PRIMARY KEY (bus_daiya_id)
) ;

-- 便
DROP TABLE if exists bus_services CASCADE;

CREATE TABLE bus_services (
  bus_service_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , daiya_revision_id integer NOT NULL
  , line_route_id integer NOT NULL
  , bus_service_trip_id character varying(50) NOT NULL
  , bus_service_trip_headsign character varying(50)
  , bus_service_name character varying(50)
  , calendar_id integer NOT NULL
  , shape_id character varying(50)
  , wheelchair_accessible_type smallint DEFAULT 0
  , bikes_allowed_type smallint DEFAULT 0
  , bus_service_trip_desc character varying(50)
  , bus_service_trip_desc_symbol character varying(50)
  , office_id integer
  , work_number character varying(20)
  , block_id character varying(20)
  , vehicle_id integer
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT bus_services_PKC PRIMARY KEY (bus_service_id)
) ;

-- 停留所マスタ
DROP TABLE if exists bus_stops CASCADE;

CREATE TABLE bus_stops (
  bus_stop_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , business_corp_id integer
  , bus_stop_code character varying(10) NOT NULL
  , service_start_date date NOT NULL
  , service_end_date date DEFAULT '9999-12-31' NOT NULL
  , bus_stop_name character varying(100) NOT NULL
  , bus_stop_kana_name character varying(100) NOT NULL
  , bus_stop_en_name character varying(200)
  , latitude_longitude character varying(50)
  , bus_stop_url character varying(256)
  , indoor_bus_terminal_flag smallint DEFAULT 0
  , bus_stop_type smallint DEFAULT 1 NOT NULL
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT bus_stops_PKC PRIMARY KEY (bus_stop_id)
) ;

-- 運行カレンダー除外日設定
DROP TABLE if exists calendar_cancel_dates CASCADE;

CREATE TABLE calendar_cancel_dates (
  calendar_cancel_date_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , calendar_id integer NOT NULL
  , operation_cancel_date date NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT calendar_cancel_dates_PKC PRIMARY KEY (calendar_cancel_date_id)
) ;

-- 運行カレンダー指定日設定
DROP TABLE if exists calendar_designated_dates CASCADE;

CREATE TABLE calendar_designated_dates (
  calendar_designated_date_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , calendar_id integer NOT NULL
  , operation_designated_date date NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT calendar_designated_dates_PKC PRIMARY KEY (calendar_designated_date_id)
) ;

-- 運行カレンダー明細マスタ
DROP TABLE if exists calendar_details CASCADE;

CREATE TABLE calendar_details (
  calendar_detail_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , calendar_id integer NOT NULL
  , day_week_type character varying(2) NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT calendar_details_PKC PRIMARY KEY (calendar_detail_id)
) ;

-- 運行カレンダーマスタ
DROP TABLE if exists calendars CASCADE;

CREATE TABLE calendars (
  calendar_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , business_corp_id integer NOT NULL
  , week_designatable_flag smallint DEFAULT 1 NOT NULL
  , week_designation_type character varying(1) DEFAULT 0 NOT NULL
  , calendar_type character varying(60) NOT NULL
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT calendars_PKC PRIMARY KEY (calendar_id)
) ;

-- 運賃改定
DROP TABLE if exists charge_revisions CASCADE;

CREATE TABLE charge_revisions (
  charge_revision_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , rosen_id integer NOT NULL
  , service_start_date date NOT NULL
  , service_end_date date DEFAULT '9999-12-31' NOT NULL
  , revision_reason character varying(50) NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT charge_revisions_PKC PRIMARY KEY (charge_revision_id)
) ;

-- コードマスタ
DROP TABLE if exists code_master CASCADE;

CREATE TABLE code_master (
  code_master_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , code_name character varying(200) NOT NULL
  , main_code character(3) NOT NULL
  , sub_code character(4)
  , code_key character varying(200)
  , code_value numeric(12)
  , code_text character varying(200)
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT code_master_PKC PRIMARY KEY (code_master_id)
) ;

-- 日毎便ダイヤ
DROP TABLE if exists daily_bus_daiyas CASCADE;

CREATE TABLE daily_bus_daiyas (
  daily_bus_daiya_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , bus_service_id integer NOT NULL
  , service_date date NOT NULL
  , sales_cancel_flg smallint DEFAULT 0 NOT NULL
  , operation_cancel_flg smallint DEFAULT 0 NOT NULL
  , vehicle_id integer
  , notification text
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT daily_bus_daiyas_PKC PRIMARY KEY (daily_bus_daiya_id)
) ;

-- 日毎便ダイヤ操作履歴
DROP TABLE if exists daily_daiya_operation_histories CASCADE;

CREATE TABLE daily_daiya_operation_histories (
  daily_daiya_operation_history_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , daily_bus_daiya_id integer NOT NULL
  , "operation" character varying(100) NOT NULL
  , "before" text NOT NULL
  , "after" text NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT daily_daiya_operation_histories_PKC PRIMARY KEY (daily_daiya_operation_history_id)
) ;

-- 便ダイヤ改正
DROP TABLE if exists daiya_revisions CASCADE;

CREATE TABLE daiya_revisions (
  daiya_revision_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , rosen_id integer NOT NULL
  , service_start_date date NOT NULL
  , service_end_date date DEFAULT '9999-12-31' NOT NULL
  , daiya_revision_name character varying(50) NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT daiya_revisions_PKC PRIMARY KEY (daiya_revision_id)
) ;

-- 便ダイヤアップロード履歴
DROP TABLE if exists daiya_upload_histories CASCADE;

CREATE TABLE daiya_upload_histories (
  daiya_upload_history_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , bus_service_id integer NOT NULL
  , file_name character varying(256) NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT daiya_upload_histories_PKC PRIMARY KEY (daiya_upload_history_id)
) ;

-- 祝日カレンダーマスタ
DROP TABLE if exists holidays CASCADE;

CREATE TABLE holidays (
  holiday_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , business_corp_id integer NOT NULL
  , holiday_date date NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT holidays_PKC PRIMARY KEY (holiday_id)
) ;

-- 収入情報
DROP TABLE if exists income_info CASCADE;

CREATE TABLE income_info (
  income_info_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , daily_bus_daiya_id integer NOT NULL
  , passengers_count integer DEFAULT 0 NOT NULL
  , regular_count integer DEFAULT 0 NOT NULL
  , cash_income integer DEFAULT 0 NOT NULL
  , coupon_ticket integer DEFAULT 0 NOT NULL
  , ic_amount integer DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT income_info_PKC PRIMARY KEY (income_info_id)
) ;

-- 路線停留所
DROP TABLE if exists line_bus_stops CASCADE;

CREATE TABLE line_bus_stops (
  rosen_bus_stop_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , rosen_id integer NOT NULL
  , bus_stop_id integer NOT NULL
  , ride_alight_division smallint DEFAULT 2 NOT NULL
  , sequence_no integer DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT line_bus_stops_PKC PRIMARY KEY (rosen_bus_stop_id)
) ;

-- 運賃明細
DROP TABLE if exists line_charge_details CASCADE;

CREATE TABLE line_charge_details (
  line_charge_detail_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , line_charge_id integer NOT NULL
  , rosen_bus_stop_id_from integer NOT NULL
  , rosen_bus_stop_id_to integer NOT NULL
  , line_charge integer NOT NULL
  , ic_price integer
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT line_charge_details_PKC PRIMARY KEY (line_charge_detail_id)
) ;

-- 運賃アップロード履歴
DROP TABLE if exists line_charge_upload_history CASCADE;

CREATE TABLE line_charge_upload_history (
  line_charge_upload_history_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , charge_revision_id integer NOT NULL
  , file_name character varying(50)
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT line_charge_upload_history_PKC PRIMARY KEY (line_charge_upload_history_id)
) ;

-- 運賃
DROP TABLE if exists line_charges CASCADE;

CREATE TABLE line_charges (
  line_charge_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , charge_revision_id integer NOT NULL
  , line_charge_division smallint NOT NULL
  , uniform_charge integer
  , uniform_ic_price integer
  , uniform_transfer_type smallint
  , payment_method smallint NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT line_charges_PKC PRIMARY KEY (line_charge_id)
) ;

-- 系統停留所
DROP TABLE if exists line_route_bus_stops CASCADE;

CREATE TABLE line_route_bus_stops (
  line_route_bus_stop_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , line_route_id integer NOT NULL
  , rosen_bus_stop_id integer NOT NULL
  , signpost_id integer NOT NULL
  , ride_alight_division smallint NOT NULL
  , drop_off_division smallint NOT NULL
  , stop_headsign character varying(50)
  , stop_headsign_en character varying(100)
  , shape_dist_traveled integer DEFAULT 0
  , stop_sequence integer DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT line_route_bus_stops_PKC PRIMARY KEY (line_route_bus_stop_id)
) ;

-- 系統
DROP TABLE if exists line_routes CASCADE;

CREATE TABLE line_routes (
  line_route_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , rosen_id integer NOT NULL
  , line_route_code character varying(10) NOT NULL
  , service_start_date date NOT NULL
  , service_end_date date DEFAULT '9999-12-31' NOT NULL
  , line_route_name character varying(50) NOT NULL
  , line_route_short_name character varying(20)
  , line_route_number character varying(50)
  , line_route_number_en character varying(100)
  , main_stopover_point character varying(50)
  , line_detail_service_division smallint NOT NULL
  , line_route_url character varying(256)
  , line_route_color character varying(10)
  , line_route_text_color character varying(10)
  , line_route_remarks character varying(50)
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT line_routes_PKC PRIMARY KEY (line_route_id)
) ;

-- 路線
DROP TABLE if exists lines CASCADE;

CREATE TABLE lines (
  rosen_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , business_corp_id integer NOT NULL
  , rosen_code character varying(10) NOT NULL
  , service_start_date date NOT NULL
  , service_end_date date DEFAULT '9999-12-31' NOT NULL
  , rosen_name character varying(50) NOT NULL
  , rosen_kana_name character varying(50) NOT NULL
  , rosen_en_name character varying(100)
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT lines_PKC PRIMARY KEY (rosen_id)
) ;

-- メニューマスタ
DROP TABLE if exists menus CASCADE;

CREATE TABLE menus (
  menu_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , menu_name character varying(20) NOT NULL
  , menu_division character(2) NOT NULL
  , menu_code character varying(20) NOT NULL
  , parent_code character varying(20) NOT NULL
  , display_order integer NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT menus_PKC PRIMARY KEY (menu_id)
) ;

-- 営業所マスタ
DROP TABLE if exists offices CASCADE;

CREATE TABLE offices (
  office_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , operating_company_id integer NOT NULL
  , name character varying(100) NOT NULL
  , office_url character varying(256)
  , phone_number character varying(15)
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT offices_PKC PRIMARY KEY (office_id)
) ;

-- 運行会社マスタ
DROP TABLE if exists operating_companies CASCADE;

CREATE TABLE operating_companies (
  operating_company_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , business_corp_id integer NOT NULL
  , operating_name character varying(100) NOT NULL
  , phone_number character varying(15)
  , mail_address character varying(256)
  , url character varying(256)
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT operating_companies_PKC PRIMARY KEY (operating_company_id)
) ;

-- 権限詳細マスタ
DROP TABLE if exists permission_role_details CASCADE;

CREATE TABLE permission_role_details (
  perm_role_detail_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , perm_role_id integer NOT NULL
  , menu_code character varying(15) NOT NULL
  , read_perm_flg smallint DEFAULT 0 NOT NULL
  , write_perm_flg smallint DEFAULT 0 NOT NULL
  , download_perm_flg smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT permission_role_details_PKC PRIMARY KEY (perm_role_detail_id)
) ;

-- 権限マスタ
DROP TABLE if exists permission_roles CASCADE;

CREATE TABLE permission_roles (
  perm_role_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , perm_role_name character varying(20) NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT permission_roles_PKC PRIMARY KEY (perm_role_id)
) ;

-- 標柱マスタ
DROP TABLE if exists signposts CASCADE;

CREATE TABLE signposts (
  signpost_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , bus_stop_id integer NOT NULL
  , signpost_branch_number character varying(100) NOT NULL
  , signpost_code character varying(100)
  , signpost_name character varying(100) NOT NULL
  , signpost_kana_name character varying(100) NOT NULL
  , signpost_en_name character varying(200)
  , signpost_sub_name character varying(100)
  , address character varying(100)
  , place_name character varying(100)
  , signpost_desc character varying(100)
  , latitude_longitude character varying(50) NOT NULL
  , signpost_url character varying(256)
  , bus_stop_type smallint DEFAULT 0 NOT NULL
  , platform_code character varying(20)
  , wheelchair_accessible_type smallint DEFAULT 0
  , arrival_point character varying(50)
  , arrival_point_radius integer DEFAULT 0
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT signposts_PKC PRIMARY KEY (signpost_id)
) ;

-- 二要素認証履歴
DROP TABLE if exists two_factor_auth_histories CASCADE;

CREATE TABLE two_factor_auth_histories (
  two_factor_auth_history_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , user_id integer NOT NULL
  , auth_code character varying(6) NOT NULL
  , code_issue_datetime timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT two_factor_auth_histories_PKC PRIMARY KEY (two_factor_auth_history_id)
) ;

-- ユーザーマスタ
DROP TABLE if exists users CASCADE;

CREATE TABLE users (
  user_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , last_name character varying(50) NOT NULL
  , first_name character varying(50) NOT NULL
  , retirement_flag smallint DEFAULT 0 NOT NULL
  , retirement_date date
  , email character varying(256) NOT NULL
  , password character varying(512) NOT NULL
  , password_changed_flag smallint DEFAULT 0 NOT NULL
  , account_division character varying(2) NOT NULL
  , business_corp_id integer NOT NULL
  , operating_company_id integer
  , perm_role_id integer
  , auth_failure_count integer DEFAULT 0
  , two_factor_auth_datetime timestamp(6) without time zone
  , account_lock_at timestamp(6) without time zone
  , last_login_at timestamp(6) without time zone
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT users_PKC PRIMARY KEY (user_id)
) ;

-- 車両動態
DROP TABLE if exists vehicle_tracking CASCADE;

CREATE TABLE vehicle_tracking (
  vehicle_tracking_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , gps_serial character varying(100) NOT NULL
  , work_number character varying(20) NOT NULL
  , latitude_longitude character varying(50) NOT NULL
  , direction character varying(50) NOT NULL
  , created_datetime timestamp(6) without time zone
  , bus_service_id integer
  , signpost_id integer
  , delay integer
  , arrival_division smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT vehicle_tracking_PKC PRIMARY KEY (vehicle_tracking_id)
) ;

-- 車両マスタ
DROP TABLE if exists vehicles CASCADE;

CREATE TABLE vehicles (
  vehicle_id serial NOT NULL
  , del_flag integer DEFAULT 0 NOT NULL
  , operating_company_id integer NOT NULL
  , vehicle_name character varying(100) NOT NULL
  , gps_serial character varying(100) NOT NULL
  , japan_positioning_flag smallint DEFAULT 0 NOT NULL
  , available_flag smallint DEFAULT 0 NOT NULL
  , created_user character varying(20) NOT NULL
  , created_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , updated_user character varying(20) NOT NULL
  , updated_at timestamp(6) without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
  , CONSTRAINT vehicles_PKC PRIMARY KEY (vehicle_id)
) ;

COMMENT ON TABLE agencies IS '事業者マスタ';
COMMENT ON COLUMN agencies.business_corp_id IS '事業者ID';
COMMENT ON COLUMN agencies.del_flag IS '削除フラグ';
COMMENT ON COLUMN agencies.name IS '事業者名・自治体名';
COMMENT ON COLUMN agencies.disp_name IS '事業者名・自治体表示名';
COMMENT ON COLUMN agencies.kana_name IS '事業者名・自治体名　かな';
COMMENT ON COLUMN agencies.en_name IS '事業者名・自治体名　英語';
COMMENT ON COLUMN agencies.department_name IS '部署名';
COMMENT ON COLUMN agencies.user IS '担当者名';
COMMENT ON COLUMN agencies.phone_number IS '電話番号';
COMMENT ON COLUMN agencies.mail_address IS '連絡先メール';
COMMENT ON COLUMN agencies.time_table_url IS '路線図、時刻表URL';
COMMENT ON COLUMN agencies.fare_url IS '運賃情報URL';
COMMENT ON COLUMN agencies.operation_company_no IS '法人番号';
COMMENT ON COLUMN agencies.transportation_type IS '交通種別';
COMMENT ON COLUMN agencies.provider_organization_name IS '提供組織名';
COMMENT ON COLUMN agencies.provider_organization_url IS '提供組織URL';
COMMENT ON COLUMN agencies.provider_organization_contact_email IS '提供組織連絡先メール';
COMMENT ON COLUMN agencies.provider_organization_contact_url IS '提供組織連絡先URL';
COMMENT ON COLUMN agencies.access_key IS 'アクセスキー';
COMMENT ON COLUMN agencies.available_flag IS '利用可否区分';
COMMENT ON COLUMN agencies.created_user IS '作成ユーザ';
COMMENT ON COLUMN agencies.created_at IS '作成日';
COMMENT ON COLUMN agencies.updated_user IS '更新ユーザ';
COMMENT ON COLUMN agencies.updated_at IS '更新日';

COMMENT ON TABLE bus_daiyas IS '便ダイヤ明細';
COMMENT ON COLUMN bus_daiyas.bus_daiya_id IS '便ダイヤ明細ID';
COMMENT ON COLUMN bus_daiyas.del_flag IS '削除フラグ';
COMMENT ON COLUMN bus_daiyas.bus_service_id IS '便ID';
COMMENT ON COLUMN bus_daiyas.line_route_bus_stop_id IS '系統停留所ID';
COMMENT ON COLUMN bus_daiyas.bus_stop_arrival_time IS '到着時刻';
COMMENT ON COLUMN bus_daiyas.bus_stop_departure_time IS '出発時刻';
COMMENT ON COLUMN bus_daiyas.created_user IS '作成ユーザ';
COMMENT ON COLUMN bus_daiyas.created_at IS '作成日';
COMMENT ON COLUMN bus_daiyas.updated_user IS '更新ユーザ';
COMMENT ON COLUMN bus_daiyas.updated_at IS '更新日';

COMMENT ON TABLE bus_services IS '便';
COMMENT ON COLUMN bus_services.bus_service_id IS '便ID';
COMMENT ON COLUMN bus_services.del_flag IS '削除フラグ';
COMMENT ON COLUMN bus_services.daiya_revision_id IS '便ダイヤ改正ID';
COMMENT ON COLUMN bus_services.line_route_id IS '系統ID';
COMMENT ON COLUMN bus_services.bus_service_trip_id IS '便番号';
COMMENT ON COLUMN bus_services.bus_service_trip_headsign IS '便行先';
COMMENT ON COLUMN bus_services.bus_service_name IS '便名称';
COMMENT ON COLUMN bus_services.calendar_id IS '運行カレンダーID';
COMMENT ON COLUMN bus_services.shape_id IS '描画ID';
COMMENT ON COLUMN bus_services.wheelchair_accessible_type IS '車いす利用区分';
COMMENT ON COLUMN bus_services.bikes_allowed_type IS '自転車持ち込み区分';
COMMENT ON COLUMN bus_services.bus_service_trip_desc IS '便情報';
COMMENT ON COLUMN bus_services.bus_service_trip_desc_symbol IS '便記号';
COMMENT ON COLUMN bus_services.office_id IS '営業所ID';
COMMENT ON COLUMN bus_services.work_number IS '仕業番号';
COMMENT ON COLUMN bus_services.block_id IS '便結合区分';
COMMENT ON COLUMN bus_services.vehicle_id IS '車両ID';
COMMENT ON COLUMN bus_services.created_user IS '作成ユーザ';
COMMENT ON COLUMN bus_services.created_at IS '作成日';
COMMENT ON COLUMN bus_services.updated_user IS '更新ユーザ';
COMMENT ON COLUMN bus_services.updated_at IS '更新日';

COMMENT ON TABLE bus_stops IS '停留所マスタ';
COMMENT ON COLUMN bus_stops.bus_stop_id IS '停留所ID';
COMMENT ON COLUMN bus_stops.del_flag IS '削除フラグ';
COMMENT ON COLUMN bus_stops.business_corp_id IS '事業者ID';
COMMENT ON COLUMN bus_stops.bus_stop_code IS '停留所コード';
COMMENT ON COLUMN bus_stops.service_start_date IS '利用開始年月日';
COMMENT ON COLUMN bus_stops.service_end_date IS '利用終了年月日';
COMMENT ON COLUMN bus_stops.bus_stop_name IS '停留所名称';
COMMENT ON COLUMN bus_stops.bus_stop_kana_name IS '停留所名称　かな';
COMMENT ON COLUMN bus_stops.bus_stop_en_name IS '停留所名称　英語';
COMMENT ON COLUMN bus_stops.latitude_longitude IS '緯度、経度';
COMMENT ON COLUMN bus_stops.bus_stop_url IS '停留所URL';
COMMENT ON COLUMN bus_stops.indoor_bus_terminal_flag IS '屋内バスターミナルフラグ';
COMMENT ON COLUMN bus_stops.bus_stop_type IS '停留所・標柱区分';
COMMENT ON COLUMN bus_stops.available_flag IS '利用可否区分';
COMMENT ON COLUMN bus_stops.created_user IS '作成ユーザ';
COMMENT ON COLUMN bus_stops.created_at IS '作成日';
COMMENT ON COLUMN bus_stops.updated_user IS '更新ユーザ';
COMMENT ON COLUMN bus_stops.updated_at IS '更新日';

COMMENT ON TABLE calendar_cancel_dates IS '運行カレンダー除外日設定';
COMMENT ON COLUMN calendar_cancel_dates.calendar_cancel_date_id IS '運行カレンダー除外日設定ID';
COMMENT ON COLUMN calendar_cancel_dates.del_flag IS '削除フラグ';
COMMENT ON COLUMN calendar_cancel_dates.calendar_id IS '運行カレンダーID';
COMMENT ON COLUMN calendar_cancel_dates.operation_cancel_date IS '運行除外日';
COMMENT ON COLUMN calendar_cancel_dates.created_user IS '作成ユーザ';
COMMENT ON COLUMN calendar_cancel_dates.created_at IS '作成日';
COMMENT ON COLUMN calendar_cancel_dates.updated_user IS '更新ユーザ';
COMMENT ON COLUMN calendar_cancel_dates.updated_at IS '更新日';

COMMENT ON TABLE calendar_designated_dates IS '運行カレンダー指定日設定';
COMMENT ON COLUMN calendar_designated_dates.calendar_designated_date_id IS '運行カレンダー指定日設定ID';
COMMENT ON COLUMN calendar_designated_dates.del_flag IS '削除フラグ';
COMMENT ON COLUMN calendar_designated_dates.calendar_id IS '運行カレンダーID';
COMMENT ON COLUMN calendar_designated_dates.operation_designated_date IS '運行指定日';
COMMENT ON COLUMN calendar_designated_dates.created_user IS '作成ユーザ';
COMMENT ON COLUMN calendar_designated_dates.created_at IS '作成日';
COMMENT ON COLUMN calendar_designated_dates.updated_user IS '更新ユーザ';
COMMENT ON COLUMN calendar_designated_dates.updated_at IS '更新日';

COMMENT ON TABLE calendar_details IS '運行カレンダー明細マスタ';
COMMENT ON COLUMN calendar_details.calendar_detail_id IS '運行カレンダー明細ID';
COMMENT ON COLUMN calendar_details.del_flag IS '削除フラグ';
COMMENT ON COLUMN calendar_details.calendar_id IS '運行カレンダーID';
COMMENT ON COLUMN calendar_details.day_week_type IS '曜日区分';
COMMENT ON COLUMN calendar_details.created_user IS '作成ユーザ';
COMMENT ON COLUMN calendar_details.created_at IS '作成日';
COMMENT ON COLUMN calendar_details.updated_user IS '更新ユーザ';
COMMENT ON COLUMN calendar_details.updated_at IS '更新日';

COMMENT ON TABLE calendars IS '運行カレンダーマスタ';
COMMENT ON COLUMN calendars.calendar_id IS '運行カレンダーID';
COMMENT ON COLUMN calendars.del_flag IS '削除フラグ';
COMMENT ON COLUMN calendars.business_corp_id IS '事業者ID';
COMMENT ON COLUMN calendars.week_designatable_flag IS '曜日指定可否区分''0:不可,1:可';
COMMENT ON COLUMN calendars.week_designation_type IS '曜日指定区分';
COMMENT ON COLUMN calendars.calendar_type IS '運行カレンダー区分';
COMMENT ON COLUMN calendars.available_flag IS '利用可否区分';
COMMENT ON COLUMN calendars.created_user IS '作成ユーザ';
COMMENT ON COLUMN calendars.created_at IS '作成日';
COMMENT ON COLUMN calendars.updated_user IS '更新ユーザ';
COMMENT ON COLUMN calendars.updated_at IS '更新日';

COMMENT ON TABLE charge_revisions IS '運賃改定';
COMMENT ON COLUMN charge_revisions.charge_revision_id IS '運賃改定ID';
COMMENT ON COLUMN charge_revisions.del_flag IS '削除フラグ';
COMMENT ON COLUMN charge_revisions.rosen_id IS '路線ID';
COMMENT ON COLUMN charge_revisions.service_start_date IS '運賃改定開始日';
COMMENT ON COLUMN charge_revisions.service_end_date IS '運賃改定終了日';
COMMENT ON COLUMN charge_revisions.revision_reason IS '運賃改定名称';
COMMENT ON COLUMN charge_revisions.created_user IS '作成ユーザ';
COMMENT ON COLUMN charge_revisions.created_at IS '作成日';
COMMENT ON COLUMN charge_revisions.updated_user IS '更新ユーザ';
COMMENT ON COLUMN charge_revisions.updated_at IS '更新日';

COMMENT ON TABLE code_master IS 'コードマスタ';
COMMENT ON COLUMN code_master.code_master_id IS 'コードID';
COMMENT ON COLUMN code_master.del_flag IS '削除フラグ';
COMMENT ON COLUMN code_master.code_name IS 'コード名';
COMMENT ON COLUMN code_master.main_code IS 'メインコード';
COMMENT ON COLUMN code_master.sub_code IS 'サブコード';
COMMENT ON COLUMN code_master.code_key IS 'コードキー';
COMMENT ON COLUMN code_master.code_value IS 'コード値';
COMMENT ON COLUMN code_master.code_text IS 'コードテキスト';
COMMENT ON COLUMN code_master.created_user IS '作成ユーザ';
COMMENT ON COLUMN code_master.created_at IS '作成日';
COMMENT ON COLUMN code_master.updated_user IS '更新ユーザ';
COMMENT ON COLUMN code_master.updated_at IS '更新日';

COMMENT ON TABLE daily_bus_daiyas IS '日毎便ダイヤ';
COMMENT ON COLUMN daily_bus_daiyas.daily_bus_daiya_id IS '日毎便ダイヤID';
COMMENT ON COLUMN daily_bus_daiyas.del_flag IS '削除フラグ';
COMMENT ON COLUMN daily_bus_daiyas.bus_service_id IS '便ID';
COMMENT ON COLUMN daily_bus_daiyas.service_date IS '運行年月日';
COMMENT ON COLUMN daily_bus_daiyas.sales_cancel_flg IS '便休フラグ';
COMMENT ON COLUMN daily_bus_daiyas.operation_cancel_flg IS '運行対象外フラグ';
COMMENT ON COLUMN daily_bus_daiyas.vehicle_id IS '車両ID';
COMMENT ON COLUMN daily_bus_daiyas.notification IS 'お知らせ';
COMMENT ON COLUMN daily_bus_daiyas.created_user IS '作成ユーザ';
COMMENT ON COLUMN daily_bus_daiyas.created_at IS '作成日';
COMMENT ON COLUMN daily_bus_daiyas.updated_user IS '更新ユーザ';
COMMENT ON COLUMN daily_bus_daiyas.updated_at IS '更新日';

COMMENT ON TABLE daily_daiya_operation_histories IS '日毎便ダイヤ操作履歴';
COMMENT ON COLUMN daily_daiya_operation_histories.daily_daiya_operation_history_id IS '日毎便ダイヤ操作履歴ID';
COMMENT ON COLUMN daily_daiya_operation_histories.del_flag IS '削除フラグ';
COMMENT ON COLUMN daily_daiya_operation_histories.daily_bus_daiya_id IS '日毎便ダイヤID';
COMMENT ON COLUMN daily_daiya_operation_histories.operation IS '操作内容';
COMMENT ON COLUMN daily_daiya_operation_histories.before IS '変更前';
COMMENT ON COLUMN daily_daiya_operation_histories.after IS '変更後';
COMMENT ON COLUMN daily_daiya_operation_histories.created_user IS '作成ユーザ';
COMMENT ON COLUMN daily_daiya_operation_histories.created_at IS '作成日';
COMMENT ON COLUMN daily_daiya_operation_histories.updated_user IS '更新ユーザ';
COMMENT ON COLUMN daily_daiya_operation_histories.updated_at IS '更新日';

COMMENT ON TABLE daiya_revisions IS '便ダイヤ改正';
COMMENT ON COLUMN daiya_revisions.daiya_revision_id IS '便ダイヤ改正ID';
COMMENT ON COLUMN daiya_revisions.del_flag IS '削除フラグ';
COMMENT ON COLUMN daiya_revisions.rosen_id IS '路線ID';
COMMENT ON COLUMN daiya_revisions.service_start_date IS '便ダイヤ改正開始日';
COMMENT ON COLUMN daiya_revisions.service_end_date IS '便ダイヤ改正終了日';
COMMENT ON COLUMN daiya_revisions.daiya_revision_name IS 'ダイヤ改正名称';
COMMENT ON COLUMN daiya_revisions.created_user IS '作成ユーザ';
COMMENT ON COLUMN daiya_revisions.created_at IS '作成日';
COMMENT ON COLUMN daiya_revisions.updated_user IS '更新ユーザ';
COMMENT ON COLUMN daiya_revisions.updated_at IS '更新日';

COMMENT ON TABLE daiya_upload_histories IS '便ダイヤアップロード履歴';
COMMENT ON COLUMN daiya_upload_histories.daiya_upload_history_id IS '便ダイヤアップロード履歴ID';
COMMENT ON COLUMN daiya_upload_histories.del_flag IS '削除フラグ';
COMMENT ON COLUMN daiya_upload_histories.bus_service_id IS '便ID';
COMMENT ON COLUMN daiya_upload_histories.file_name IS 'ファイル名';
COMMENT ON COLUMN daiya_upload_histories.created_user IS '作成ユーザ';
COMMENT ON COLUMN daiya_upload_histories.created_at IS '作成日';
COMMENT ON COLUMN daiya_upload_histories.updated_user IS '更新ユーザ';
COMMENT ON COLUMN daiya_upload_histories.updated_at IS '更新日';

COMMENT ON TABLE holidays IS '祝日カレンダーマスタ';
COMMENT ON COLUMN holidays.holiday_id IS '祝日カレンダーID';
COMMENT ON COLUMN holidays.del_flag IS '削除フラグ';
COMMENT ON COLUMN holidays.business_corp_id IS '事業者ID';
COMMENT ON COLUMN holidays.holiday_date IS '祝日年月日';
COMMENT ON COLUMN holidays.created_user IS '作成ユーザ';
COMMENT ON COLUMN holidays.created_at IS '作成日';
COMMENT ON COLUMN holidays.updated_user IS '更新ユーザ';
COMMENT ON COLUMN holidays.updated_at IS '更新日';

COMMENT ON TABLE income_info IS '収入情報';
COMMENT ON COLUMN income_info.income_info_id IS '収入情報ID';
COMMENT ON COLUMN income_info.del_flag IS '削除フラグ';
COMMENT ON COLUMN income_info.daily_bus_daiya_id IS '日毎便ダイヤID';
COMMENT ON COLUMN income_info.passengers_count IS '乗車人数';
COMMENT ON COLUMN income_info.regular_count IS '定期券人数';
COMMENT ON COLUMN income_info.cash_income IS '現金収入';
COMMENT ON COLUMN income_info.coupon_ticket IS '回数券';
COMMENT ON COLUMN income_info.ic_amount IS 'ICカード金額';
COMMENT ON COLUMN income_info.created_user IS '作成ユーザ';
COMMENT ON COLUMN income_info.created_at IS '作成日';
COMMENT ON COLUMN income_info.updated_user IS '更新ユーザ';
COMMENT ON COLUMN income_info.updated_at IS '更新日';

COMMENT ON TABLE line_bus_stops IS '路線停留所';
COMMENT ON COLUMN line_bus_stops.rosen_bus_stop_id IS '路線停留所ID';
COMMENT ON COLUMN line_bus_stops.del_flag IS '削除フラグ';
COMMENT ON COLUMN line_bus_stops.rosen_id IS '路線ID';
COMMENT ON COLUMN line_bus_stops.bus_stop_id IS '停留所ID';
COMMENT ON COLUMN line_bus_stops.ride_alight_division IS '乗降区分';
COMMENT ON COLUMN line_bus_stops.sequence_no IS '運行順';
COMMENT ON COLUMN line_bus_stops.created_user IS '作成ユーザ';
COMMENT ON COLUMN line_bus_stops.created_at IS '作成日';
COMMENT ON COLUMN line_bus_stops.updated_user IS '更新ユーザ';
COMMENT ON COLUMN line_bus_stops.updated_at IS '更新日';

COMMENT ON TABLE line_charge_details IS '運賃明細';
COMMENT ON COLUMN line_charge_details.line_charge_detail_id IS '運賃明細ID';
COMMENT ON COLUMN line_charge_details.del_flag IS '削除フラグ';
COMMENT ON COLUMN line_charge_details.line_charge_id IS '運賃ID';
COMMENT ON COLUMN line_charge_details.rosen_bus_stop_id_from IS '路線停留所ID From';
COMMENT ON COLUMN line_charge_details.rosen_bus_stop_id_to IS '路線停留所ID To';
COMMENT ON COLUMN line_charge_details.line_charge IS '運賃';
COMMENT ON COLUMN line_charge_details.ic_price IS 'ICカード運賃';
COMMENT ON COLUMN line_charge_details.created_user IS '作成ユーザ';
COMMENT ON COLUMN line_charge_details.created_at IS '作成日';
COMMENT ON COLUMN line_charge_details.updated_user IS '更新ユーザ';
COMMENT ON COLUMN line_charge_details.updated_at IS '更新日';

COMMENT ON TABLE line_charge_upload_history IS '運賃アップロード履歴';
COMMENT ON COLUMN line_charge_upload_history.line_charge_upload_history_id IS '運賃アップロード履歴ID';
COMMENT ON COLUMN line_charge_upload_history.del_flag IS '削除フラグ';
COMMENT ON COLUMN line_charge_upload_history.charge_revision_id IS '運賃改定ID';
COMMENT ON COLUMN line_charge_upload_history.file_name IS 'ファイル名';
COMMENT ON COLUMN line_charge_upload_history.created_user IS '作成ユーザ';
COMMENT ON COLUMN line_charge_upload_history.created_at IS '作成日';
COMMENT ON COLUMN line_charge_upload_history.updated_user IS '更新ユーザ';
COMMENT ON COLUMN line_charge_upload_history.updated_at IS '更新日';

COMMENT ON TABLE line_charges IS '運賃';
COMMENT ON COLUMN line_charges.line_charge_id IS '運賃ID';
COMMENT ON COLUMN line_charges.del_flag IS '削除フラグ';
COMMENT ON COLUMN line_charges.charge_revision_id IS '運賃改定ID';
COMMENT ON COLUMN line_charges.line_charge_division IS '運賃区分';
COMMENT ON COLUMN line_charges.uniform_charge IS '均一運賃額';
COMMENT ON COLUMN line_charges.uniform_ic_price IS '均一運賃額_ICカード';
COMMENT ON COLUMN line_charges.uniform_transfer_type IS '均一運賃乗換可否';
COMMENT ON COLUMN line_charges.payment_method IS '支払タイミング';
COMMENT ON COLUMN line_charges.created_user IS '作成ユーザ';
COMMENT ON COLUMN line_charges.created_at IS '作成日';
COMMENT ON COLUMN line_charges.updated_user IS '更新ユーザ';
COMMENT ON COLUMN line_charges.updated_at IS '更新日';

COMMENT ON TABLE line_route_bus_stops IS '系統停留所';
COMMENT ON COLUMN line_route_bus_stops.line_route_bus_stop_id IS '系統停留所ID';
COMMENT ON COLUMN line_route_bus_stops.del_flag IS '削除フラグ';
COMMENT ON COLUMN line_route_bus_stops.line_route_id IS '系統ID';
COMMENT ON COLUMN line_route_bus_stops.rosen_bus_stop_id IS '路線停留所ID';
COMMENT ON COLUMN line_route_bus_stops.signpost_id IS '標柱ID';
COMMENT ON COLUMN line_route_bus_stops.ride_alight_division IS '乗降区分';
COMMENT ON COLUMN line_route_bus_stops.drop_off_division IS '下車区分';
COMMENT ON COLUMN line_route_bus_stops.stop_headsign IS '停留所行先';
COMMENT ON COLUMN line_route_bus_stops.stop_headsign_en IS '停留所行先　英語';
COMMENT ON COLUMN line_route_bus_stops.shape_dist_traveled IS '通算距離';
COMMENT ON COLUMN line_route_bus_stops.stop_sequence IS '運行順序';
COMMENT ON COLUMN line_route_bus_stops.created_user IS '作成ユーザ';
COMMENT ON COLUMN line_route_bus_stops.created_at IS '作成日';
COMMENT ON COLUMN line_route_bus_stops.updated_user IS '更新ユーザ';
COMMENT ON COLUMN line_route_bus_stops.updated_at IS '更新日';

COMMENT ON TABLE line_routes IS '系統';
COMMENT ON COLUMN line_routes.line_route_id IS '系統ID';
COMMENT ON COLUMN line_routes.del_flag IS '削除フラグ';
COMMENT ON COLUMN line_routes.rosen_id IS '路線ID';
COMMENT ON COLUMN line_routes.line_route_code IS '系統コード';
COMMENT ON COLUMN line_routes.service_start_date IS '利用開始年月日';
COMMENT ON COLUMN line_routes.service_end_date IS '利用終了年月日';
COMMENT ON COLUMN line_routes.line_route_name IS '系統名称';
COMMENT ON COLUMN line_routes.line_route_short_name IS '系統番号';
COMMENT ON COLUMN line_routes.line_route_number IS '系統番号名称';
COMMENT ON COLUMN line_routes.line_route_number_en IS '系統番号名称　英語';
COMMENT ON COLUMN line_routes.main_stopover_point IS '主な経由地';
COMMENT ON COLUMN line_routes.line_detail_service_division IS '往路、復路区分';
COMMENT ON COLUMN line_routes.line_route_url IS '系統URL';
COMMENT ON COLUMN line_routes.line_route_color IS '系統背景色';
COMMENT ON COLUMN line_routes.line_route_text_color IS '系統文字色';
COMMENT ON COLUMN line_routes.line_route_remarks IS '系統備考';
COMMENT ON COLUMN line_routes.available_flag IS '利用可否区分';
COMMENT ON COLUMN line_routes.created_user IS '作成ユーザ';
COMMENT ON COLUMN line_routes.created_at IS '作成日';
COMMENT ON COLUMN line_routes.updated_user IS '更新ユーザ';
COMMENT ON COLUMN line_routes.updated_at IS '更新日';

COMMENT ON TABLE lines IS '路線';
COMMENT ON COLUMN lines.rosen_id IS '路線ID';
COMMENT ON COLUMN lines.del_flag IS '削除フラグ';
COMMENT ON COLUMN lines.business_corp_id IS '事業者ID';
COMMENT ON COLUMN lines.rosen_code IS '路線コード';
COMMENT ON COLUMN lines.service_start_date IS '利用開始年月日';
COMMENT ON COLUMN lines.service_end_date IS '利用終了年月日';
COMMENT ON COLUMN lines.rosen_name IS '路線名';
COMMENT ON COLUMN lines.rosen_kana_name IS '路線名　かな';
COMMENT ON COLUMN lines.rosen_en_name IS '路線名　英語';
COMMENT ON COLUMN lines.available_flag IS '利用可否区分';
COMMENT ON COLUMN lines.created_user IS '作成ユーザ';
COMMENT ON COLUMN lines.created_at IS '作成日';
COMMENT ON COLUMN lines.updated_user IS '更新ユーザ';
COMMENT ON COLUMN lines.updated_at IS '更新日';

COMMENT ON TABLE menus IS 'メニューマスタ';
COMMENT ON COLUMN menus.menu_id IS 'メニューID';
COMMENT ON COLUMN menus.del_flag IS '削除フラグ';
COMMENT ON COLUMN menus.menu_name IS 'メニュー名';
COMMENT ON COLUMN menus.menu_division IS 'メニュー区分';
COMMENT ON COLUMN menus.menu_code IS 'メニューコード';
COMMENT ON COLUMN menus.parent_code IS '親メニューコード';
COMMENT ON COLUMN menus.display_order IS '表示順';
COMMENT ON COLUMN menus.created_user IS '作成ユーザ';
COMMENT ON COLUMN menus.created_at IS '作成日';
COMMENT ON COLUMN menus.updated_user IS '更新ユーザ';
COMMENT ON COLUMN menus.updated_at IS '更新日';

COMMENT ON TABLE offices IS '営業所マスタ';
COMMENT ON COLUMN offices.office_id IS '営業所ID';
COMMENT ON COLUMN offices.del_flag IS '削除フラグ';
COMMENT ON COLUMN offices.operating_company_id IS '運行会社ID';
COMMENT ON COLUMN offices.name IS '営業所名';
COMMENT ON COLUMN offices.office_url IS '営業所URL';
COMMENT ON COLUMN offices.phone_number IS '営業所電話番号';
COMMENT ON COLUMN offices.available_flag IS '利用可否区分';
COMMENT ON COLUMN offices.created_user IS '作成ユーザ';
COMMENT ON COLUMN offices.created_at IS '作成日';
COMMENT ON COLUMN offices.updated_user IS '更新ユーザ';
COMMENT ON COLUMN offices.updated_at IS '更新日';

COMMENT ON TABLE operating_companies IS '運行会社マスタ';
COMMENT ON COLUMN operating_companies.operating_company_id IS '運行会社ID';
COMMENT ON COLUMN operating_companies.del_flag IS '削除フラグ';
COMMENT ON COLUMN operating_companies.business_corp_id IS '事業者ID';
COMMENT ON COLUMN operating_companies.operating_name IS '運行会社名';
COMMENT ON COLUMN operating_companies.phone_number IS '電話番号';
COMMENT ON COLUMN operating_companies.mail_address IS '連絡先メール';
COMMENT ON COLUMN operating_companies.url IS 'URL';
COMMENT ON COLUMN operating_companies.available_flag IS '利用可否区分';
COMMENT ON COLUMN operating_companies.created_user IS '作成ユーザ';
COMMENT ON COLUMN operating_companies.created_at IS '作成日';
COMMENT ON COLUMN operating_companies.updated_user IS '更新ユーザ';
COMMENT ON COLUMN operating_companies.updated_at IS '更新日';

COMMENT ON TABLE permission_role_details IS '権限詳細マスタ';
COMMENT ON COLUMN permission_role_details.perm_role_detail_id IS '権限詳細ID';
COMMENT ON COLUMN permission_role_details.del_flag IS '削除フラグ';
COMMENT ON COLUMN permission_role_details.perm_role_id IS '権限ロールID';
COMMENT ON COLUMN permission_role_details.menu_code IS 'メニューコード';
COMMENT ON COLUMN permission_role_details.read_perm_flg IS '参照権限フラグ';
COMMENT ON COLUMN permission_role_details.write_perm_flg IS '書込み権限フラグ';
COMMENT ON COLUMN permission_role_details.download_perm_flg IS 'ダウンロード権限フラグ';
COMMENT ON COLUMN permission_role_details.created_user IS '作成ユーザ';
COMMENT ON COLUMN permission_role_details.created_at IS '作成日';
COMMENT ON COLUMN permission_role_details.updated_user IS '更新ユーザ';
COMMENT ON COLUMN permission_role_details.updated_at IS '更新日';

COMMENT ON TABLE permission_roles IS '権限マスタ';
COMMENT ON COLUMN permission_roles.perm_role_id IS '権限ID';
COMMENT ON COLUMN permission_roles.del_flag IS '削除フラグ';
COMMENT ON COLUMN permission_roles.perm_role_name IS '権限名';
COMMENT ON COLUMN permission_roles.created_user IS '作成ユーザ';
COMMENT ON COLUMN permission_roles.created_at IS '作成日';
COMMENT ON COLUMN permission_roles.updated_user IS '更新ユーザ';
COMMENT ON COLUMN permission_roles.updated_at IS '更新日';

COMMENT ON TABLE signposts IS '標柱マスタ';
COMMENT ON COLUMN signposts.signpost_id IS '標柱ID';
COMMENT ON COLUMN signposts.del_flag IS '削除フラグ';
COMMENT ON COLUMN signposts.bus_stop_id IS '停留所ID';
COMMENT ON COLUMN signposts.signpost_branch_number IS '標柱枝番';
COMMENT ON COLUMN signposts.signpost_code IS '標柱番号';
COMMENT ON COLUMN signposts.signpost_name IS '標柱名称';
COMMENT ON COLUMN signposts.signpost_kana_name IS '標柱名称　かな';
COMMENT ON COLUMN signposts.signpost_en_name IS '標柱名称　英語';
COMMENT ON COLUMN signposts.signpost_sub_name IS '標柱副名称';
COMMENT ON COLUMN signposts.address IS '標柱住所';
COMMENT ON COLUMN signposts.place_name IS '標柱地先名';
COMMENT ON COLUMN signposts.signpost_desc IS '標柱付加情報';
COMMENT ON COLUMN signposts.latitude_longitude IS '緯度、経度';
COMMENT ON COLUMN signposts.signpost_url IS '標柱URL';
COMMENT ON COLUMN signposts.bus_stop_type IS '停留所・標柱区分';
COMMENT ON COLUMN signposts.platform_code IS 'のりば情報';
COMMENT ON COLUMN signposts.wheelchair_accessible_type IS '車いす利用区分';
COMMENT ON COLUMN signposts.arrival_point IS '到着判定緯度、経度';
COMMENT ON COLUMN signposts.arrival_point_radius IS '到着判定半径';
COMMENT ON COLUMN signposts.available_flag IS '利用可否区分';
COMMENT ON COLUMN signposts.created_user IS '作成ユーザ';
COMMENT ON COLUMN signposts.created_at IS '作成日';
COMMENT ON COLUMN signposts.updated_user IS '更新ユーザ';
COMMENT ON COLUMN signposts.updated_at IS '更新日';

COMMENT ON TABLE two_factor_auth_histories IS '二要素認証履歴';
COMMENT ON COLUMN two_factor_auth_histories.two_factor_auth_history_id IS '二要素認証履歴ID';
COMMENT ON COLUMN two_factor_auth_histories.del_flag IS '削除フラグ';
COMMENT ON COLUMN two_factor_auth_histories.user_id IS 'ユーザID';
COMMENT ON COLUMN two_factor_auth_histories.auth_code IS '認証コード';
COMMENT ON COLUMN two_factor_auth_histories.code_issue_datetime IS 'コード発行日';
COMMENT ON COLUMN two_factor_auth_histories.created_user IS '作成ユーザ';
COMMENT ON COLUMN two_factor_auth_histories.created_at IS '作成日';
COMMENT ON COLUMN two_factor_auth_histories.updated_user IS '更新ユーザ';
COMMENT ON COLUMN two_factor_auth_histories.updated_at IS '更新日';

COMMENT ON TABLE users IS 'ユーザーマスタ';
COMMENT ON COLUMN users.user_id IS 'ユーザID';
COMMENT ON COLUMN users.del_flag IS '削除フラグ';
COMMENT ON COLUMN users.last_name IS '氏名_姓';
COMMENT ON COLUMN users.first_name IS '氏名_名';
COMMENT ON COLUMN users.retirement_flag IS '退職済みフラグ';
COMMENT ON COLUMN users.retirement_date IS '退職年月日';
COMMENT ON COLUMN users.email IS 'ログインID';
COMMENT ON COLUMN users.password IS 'パスワード';
COMMENT ON COLUMN users.password_changed_flag IS '初期パスワード変更済みフラグ';
COMMENT ON COLUMN users.account_division IS 'アカウント区分';
COMMENT ON COLUMN users.business_corp_id IS '事業者ID';
COMMENT ON COLUMN users.operating_company_id IS '運行会社ID';
COMMENT ON COLUMN users.perm_role_id IS '権限ロールID';
COMMENT ON COLUMN users.auth_failure_count IS '認証失敗回数';
COMMENT ON COLUMN users.two_factor_auth_datetime IS '二要素認証日時';
COMMENT ON COLUMN users.account_lock_at IS 'アカウントロック日時';
COMMENT ON COLUMN users.last_login_at IS '最終ログイン日時';
COMMENT ON COLUMN users.created_user IS '作成ユーザ';
COMMENT ON COLUMN users.created_at IS '作成日';
COMMENT ON COLUMN users.updated_user IS '更新ユーザ';
COMMENT ON COLUMN users.updated_at IS '更新日';

COMMENT ON TABLE vehicle_tracking IS '車両動態';
COMMENT ON COLUMN vehicle_tracking.vehicle_tracking_id IS '車両動態ID';
COMMENT ON COLUMN vehicle_tracking.del_flag IS '削除フラグ';
COMMENT ON COLUMN vehicle_tracking.gps_serial IS 'GPSトラッカーシリアル';
COMMENT ON COLUMN vehicle_tracking.work_number IS '仕業番号';
COMMENT ON COLUMN vehicle_tracking.latitude_longitude IS '緯度、経度';
COMMENT ON COLUMN vehicle_tracking.direction IS '運行方向';
COMMENT ON COLUMN vehicle_tracking.created_datetime IS '送信日時';
COMMENT ON COLUMN vehicle_tracking.bus_service_id IS '便ID';
COMMENT ON COLUMN vehicle_tracking.signpost_id IS '標柱ID';
COMMENT ON COLUMN vehicle_tracking.delay IS '遅延時間';
COMMENT ON COLUMN vehicle_tracking.arrival_division IS '到着区分';
COMMENT ON COLUMN vehicle_tracking.created_user IS '作成ユーザ';
COMMENT ON COLUMN vehicle_tracking.created_at IS '作成日';
COMMENT ON COLUMN vehicle_tracking.updated_user IS '更新ユーザ';
COMMENT ON COLUMN vehicle_tracking.updated_at IS '更新日';

COMMENT ON TABLE vehicles IS '車両マスタ';
COMMENT ON COLUMN vehicles.vehicle_id IS '車両ID';
COMMENT ON COLUMN vehicles.del_flag IS '削除フラグ';
COMMENT ON COLUMN vehicles.operating_company_id IS '運行会社ID';
COMMENT ON COLUMN vehicles.vehicle_name IS '車両名';
COMMENT ON COLUMN vehicles.gps_serial IS 'GPSトラッカーシリアル';
COMMENT ON COLUMN vehicles.japan_positioning_flag IS '日本測地フラグ ''0：世界測地系、1：日本測地系';
COMMENT ON COLUMN vehicles.available_flag IS '利用可否区分';
COMMENT ON COLUMN vehicles.created_user IS '作成ユーザ';
COMMENT ON COLUMN vehicles.created_at IS '作成日';
COMMENT ON COLUMN vehicles.updated_user IS '更新ユーザ';
COMMENT ON COLUMN vehicles.updated_at IS '更新日';

