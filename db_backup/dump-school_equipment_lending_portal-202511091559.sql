--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.5

-- Started on 2025-11-09 15:59:06

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 65922)
-- Name: equipment_availability_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipment_availability_types (
    id integer NOT NULL,
    availability_status character varying(100) NOT NULL,
    description text,
    created_by integer,
    created_at timestamp without time zone DEFAULT now(),
    modified_by integer,
    modified_at timestamp without time zone DEFAULT now()
);


ALTER TABLE public.equipment_availability_types OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 65921)
-- Name: equipment_availability_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipment_availability_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipment_availability_id_seq OWNER TO postgres;

--
-- TOC entry 4997 (class 0 OID 0)
-- Dependencies: 219
-- Name: equipment_availability_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipment_availability_id_seq OWNED BY public.equipment_availability_types.id;


--
-- TOC entry 218 (class 1259 OID 65895)
-- Name: equipment_categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipment_categories (
    id integer NOT NULL,
    category_name character varying(100) NOT NULL,
    description text,
    created_by integer,
    created_at timestamp without time zone DEFAULT now(),
    modified_by integer,
    modified_at timestamp without time zone DEFAULT now()
);


ALTER TABLE public.equipment_categories OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 65894)
-- Name: equipment_categories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipment_categories_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipment_categories_id_seq OWNER TO postgres;

--
-- TOC entry 4998 (class 0 OID 0)
-- Dependencies: 217
-- Name: equipment_categories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipment_categories_id_seq OWNED BY public.equipment_categories.id;


--
-- TOC entry 226 (class 1259 OID 65976)
-- Name: equipment_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipment_requests (
    id integer NOT NULL,
    equipment_id integer NOT NULL,
    quantity_requested integer NOT NULL,
    purpose character varying(255) NOT NULL,
    request_status_id integer DEFAULT 1,
    expected_return_date date,
    created_by integer,
    created_at timestamp without time zone DEFAULT now(),
    modified_by integer,
    modified_at timestamp without time zone DEFAULT now(),
    actual_return_date date,
    CONSTRAINT equipment_requests_quantity_requested_check CHECK ((quantity_requested > 0))
);


ALTER TABLE public.equipment_requests OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 65975)
-- Name: equipment_requests_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipment_requests_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipment_requests_id_seq OWNER TO postgres;

--
-- TOC entry 4999 (class 0 OID 0)
-- Dependencies: 225
-- Name: equipment_requests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipment_requests_id_seq OWNED BY public.equipment_requests.id;


--
-- TOC entry 222 (class 1259 OID 65935)
-- Name: equipments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipments (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    category_id integer,
    availability_id integer,
    total_quantity integer NOT NULL,
    created_by integer,
    created_at timestamp without time zone DEFAULT now(),
    modified_by integer,
    modified_at timestamp without time zone DEFAULT now(),
    description character varying(255),
    quantity_left integer DEFAULT 0,
    CONSTRAINT equipments_quantity_check CHECK ((total_quantity >= 0)),
    CONSTRAINT equipments_quantity_left_check CHECK ((quantity_left >= 0))
);


ALTER TABLE public.equipments OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 65934)
-- Name: equipments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipments_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipments_id_seq OWNER TO postgres;

--
-- TOC entry 5000 (class 0 OID 0)
-- Dependencies: 221
-- Name: equipments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipments_id_seq OWNED BY public.equipments.id;


--
-- TOC entry 224 (class 1259 OID 65967)
-- Name: request_status_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.request_status_types (
    id integer NOT NULL,
    status_type character varying(50) NOT NULL,
    description character varying(255),
    created_by integer,
    created_at timestamp without time zone DEFAULT now(),
    modified_by integer,
    modified_at timestamp without time zone DEFAULT now()
);


ALTER TABLE public.request_status_types OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 65966)
-- Name: request_status_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.request_status_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.request_status_types_id_seq OWNER TO postgres;

--
-- TOC entry 5001 (class 0 OID 0)
-- Dependencies: 223
-- Name: request_status_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.request_status_types_id_seq OWNED BY public.request_status_types.id;


--
-- TOC entry 228 (class 1259 OID 66004)
-- Name: user_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_types (
    id integer NOT NULL,
    user_type character varying(50) NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    created_by integer,
    modified_at timestamp without time zone DEFAULT now(),
    modified_by integer
);


ALTER TABLE public.user_types OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 66003)
-- Name: user_types_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_types_id_seq OWNER TO postgres;

--
-- TOC entry 5002 (class 0 OID 0)
-- Dependencies: 227
-- Name: user_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_types_id_seq OWNED BY public.user_types.id;


--
-- TOC entry 230 (class 1259 OID 66015)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    user_type_id integer,
    email text NOT NULL,
    password text NOT NULL,
    created_at timestamp without time zone DEFAULT now() NOT NULL,
    created_by integer,
    modified_at timestamp without time zone DEFAULT now(),
    modified_by integer
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 66014)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 5003 (class 0 OID 0)
-- Dependencies: 229
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 4775 (class 2604 OID 65925)
-- Name: equipment_availability_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_availability_types ALTER COLUMN id SET DEFAULT nextval('public.equipment_availability_id_seq'::regclass);


--
-- TOC entry 4772 (class 2604 OID 65898)
-- Name: equipment_categories id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_categories ALTER COLUMN id SET DEFAULT nextval('public.equipment_categories_id_seq'::regclass);


--
-- TOC entry 4785 (class 2604 OID 65979)
-- Name: equipment_requests id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_requests ALTER COLUMN id SET DEFAULT nextval('public.equipment_requests_id_seq'::regclass);


--
-- TOC entry 4778 (class 2604 OID 65938)
-- Name: equipments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments ALTER COLUMN id SET DEFAULT nextval('public.equipments_id_seq'::regclass);


--
-- TOC entry 4782 (class 2604 OID 65970)
-- Name: request_status_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_status_types ALTER COLUMN id SET DEFAULT nextval('public.request_status_types_id_seq'::regclass);


--
-- TOC entry 4789 (class 2604 OID 66007)
-- Name: user_types id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_types ALTER COLUMN id SET DEFAULT nextval('public.user_types_id_seq'::regclass);


--
-- TOC entry 4792 (class 2604 OID 66018)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 4981 (class 0 OID 65922)
-- Dependencies: 220
-- Data for Name: equipment_availability_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equipment_availability_types (id, availability_status, description, created_by, created_at, modified_by, modified_at) FROM stdin;
1	Available	Ready to be issued or borrowed.	1	2025-11-02 15:53:12.804557	1	2025-11-02 15:53:12.804557
2	Lent Out	Currently borrowed by someone.	1	2025-11-02 15:53:12.804557	1	2025-11-02 15:53:12.804557
3	Under Maintenance	Unavailable due to repairs or service.	1	2025-11-02 15:53:12.804557	1	2025-11-02 15:53:12.804557
5	Unavailable	Temporarily blocked or decommissioned.	1	2025-11-02 15:53:12.804557	1	2025-11-02 15:53:12.804557
\.


--
-- TOC entry 4979 (class 0 OID 65895)
-- Dependencies: 218
-- Data for Name: equipment_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equipment_categories (id, category_name, description, created_by, created_at, modified_by, modified_at) FROM stdin;
1	Electronics	Laptops, projectors, tablets, and related devices.	1	2025-11-02 15:52:58.045024	1	2025-11-02 15:52:58.045024
2	Sports	Balls, rackets, and other sports gear.	1	2025-11-02 15:52:58.045024	1	2025-11-02 15:52:58.045024
3	Laboratory	Scientific tools like microscopes, sensors, meters, etc.	1	2025-11-02 15:52:58.045024	1	2025-11-02 15:52:58.045024
4	Furniture	Desks, chairs, podiums, and other school furniture.	1	2025-11-02 15:52:58.045024	1	2025-11-02 15:52:58.045024
5	Stationery	Markers, pens, whiteboards, and basic office supplies.	1	2025-11-02 15:52:58.045024	1	2025-11-02 15:52:58.045024
\.


--
-- TOC entry 4987 (class 0 OID 65976)
-- Dependencies: 226
-- Data for Name: equipment_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equipment_requests (id, equipment_id, quantity_requested, purpose, request_status_id, expected_return_date, created_by, created_at, modified_by, modified_at, actual_return_date) FROM stdin;
33	9	1	for personal use	3	\N	8	2025-11-09 15:34:57.754629	2	2025-11-09 15:35:39.026831	\N
32	1	2	For chemistry lab experiment	4	2025-11-15	8	2025-11-09 15:32:31.753216	2	2025-11-09 15:36:01.786995	2025-11-09
\.


--
-- TOC entry 4983 (class 0 OID 65935)
-- Dependencies: 222
-- Data for Name: equipments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.equipments (id, name, category_id, availability_id, total_quantity, created_by, created_at, modified_by, modified_at, description, quantity_left) FROM stdin;
2	Football	2	1	15	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Standard football for school sports activities.	15
3	Guitar	3	2	3	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Acoustic guitar used in music practice.	3
4	Projector	4	3	2	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Device for displaying presentations or videos.	2
5	Whiteboard Marker Set	5	1	50	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Set of colored markers for classroom use.	50
6	Chemistry Lab Set	1	2	8	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Equipment set for performing chemistry experiments.	8
7	Basketball	2	1	10	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Ball used for basketball practice and games.	10
8	Drum Set	3	1	1	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Musical instrument set for band performances.	1
9	Laptop	4	1	12	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Portable computer for academic and administrative use.	12
10	Classroom Chair	5	3	40	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Standard chair used in classrooms.	40
1	Microscope	1	1	5	1	2025-11-02 16:57:03.336017	1	2025-11-02 16:57:03.336017	Used for viewing tiny objects and samples.	5
\.


--
-- TOC entry 4985 (class 0 OID 65967)
-- Dependencies: 224
-- Data for Name: request_status_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.request_status_types (id, status_type, description, created_by, created_at, modified_by, modified_at) FROM stdin;
1	Pending	Request just created, waiting for admin/staff approval	1	2025-11-03 16:19:26.452252	1	2025-11-03 16:19:26.452252
2	Approved	Request approved and equipment is lent out	1	2025-11-03 16:19:26.452252	1	2025-11-03 16:19:26.452252
3	Rejected	Request denied by admin/staff	1	2025-11-03 16:19:26.452252	1	2025-11-03 16:19:26.452252
4	Closed	Request process completed	1	2025-11-03 16:19:26.452252	1	2025-11-03 16:19:26.452252
\.


--
-- TOC entry 4989 (class 0 OID 66004)
-- Dependencies: 228
-- Data for Name: user_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_types (id, user_type, created_at, created_by, modified_at, modified_by) FROM stdin;
2	admin	2025-11-08 17:39:55.103441	1	2025-11-08 17:39:55.103441	1
3	staff	2025-11-08 17:39:55.116667	1	2025-11-08 17:39:55.116667	1
4	student	2025-11-08 17:39:55.118925	1	2025-11-08 17:39:55.118925	1
\.


--
-- TOC entry 4991 (class 0 OID 66015)
-- Dependencies: 230
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, first_name, last_name, user_type_id, email, password, created_at, created_by, modified_at, modified_by) FROM stdin;
1	Kishor	Jangir	2	kishorjangir21@gmail.com	$2a$10$0ZeVit6SR2jMKnQT5/6GvuSw.eUbnjEXIgeY82z7AFd6ld4ff6AW2	2025-11-08 21:12:18.383021	\N	2025-11-08 21:12:18.383021	\N
2	Swapnil	Rankawat	3	swapnilrankawat11@gmail.com	$2a$10$y4HTxO7dsO3Kk/ErEdcPf.z2W.EF6hXqZRs1nV4YuxcK33FuPgCMu	2025-11-08 21:15:58.723895	\N	2025-11-08 21:15:58.723895	\N
3	Kartikeya	Rankawat	4	kartikeyarankawat24@gmail.com	$2a$10$jEIOzbRQ0Gqi7W5KHZpJ9e1.F543tJnRTn1zRMJIvq8ipag5Efde6	2025-11-08 21:16:45.768658	\N	2025-11-08 21:16:45.768658	\N
8	Bharat	Jangir	4	bharatjangir41@gmail.com	$2a$10$RNWbhQ9FzF3rv5VCKapPn.1Nk/VIwIdy.R8cNXSDW0NPLsvDB0NoC	2025-11-09 15:31:15.619367	\N	2025-11-09 15:31:15.619367	\N
\.


--
-- TOC entry 5004 (class 0 OID 0)
-- Dependencies: 219
-- Name: equipment_availability_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.equipment_availability_id_seq', 5, true);


--
-- TOC entry 5005 (class 0 OID 0)
-- Dependencies: 217
-- Name: equipment_categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.equipment_categories_id_seq', 7, true);


--
-- TOC entry 5006 (class 0 OID 0)
-- Dependencies: 225
-- Name: equipment_requests_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.equipment_requests_id_seq', 34, true);


--
-- TOC entry 5007 (class 0 OID 0)
-- Dependencies: 221
-- Name: equipments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.equipments_id_seq', 26, true);


--
-- TOC entry 5008 (class 0 OID 0)
-- Dependencies: 223
-- Name: request_status_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.request_status_types_id_seq', 4, true);


--
-- TOC entry 5009 (class 0 OID 0)
-- Dependencies: 227
-- Name: user_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_types_id_seq', 4, true);


--
-- TOC entry 5010 (class 0 OID 0)
-- Dependencies: 229
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 8, true);


--
-- TOC entry 4803 (class 2606 OID 65933)
-- Name: equipment_availability_types equipment_availability_availability_status_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_availability_types
    ADD CONSTRAINT equipment_availability_availability_status_key UNIQUE (availability_status);


--
-- TOC entry 4805 (class 2606 OID 65931)
-- Name: equipment_availability_types equipment_availability_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_availability_types
    ADD CONSTRAINT equipment_availability_pkey PRIMARY KEY (id);


--
-- TOC entry 4799 (class 2606 OID 65906)
-- Name: equipment_categories equipment_categories_category_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_categories
    ADD CONSTRAINT equipment_categories_category_name_key UNIQUE (category_name);


--
-- TOC entry 4801 (class 2606 OID 65904)
-- Name: equipment_categories equipment_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_categories
    ADD CONSTRAINT equipment_categories_pkey PRIMARY KEY (id);


--
-- TOC entry 4811 (class 2606 OID 65984)
-- Name: equipment_requests equipment_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_requests
    ADD CONSTRAINT equipment_requests_pkey PRIMARY KEY (id);


--
-- TOC entry 4807 (class 2606 OID 65943)
-- Name: equipments equipments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments
    ADD CONSTRAINT equipments_pkey PRIMARY KEY (id);


--
-- TOC entry 4809 (class 2606 OID 65974)
-- Name: request_status_types request_status_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_status_types
    ADD CONSTRAINT request_status_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4813 (class 2606 OID 66011)
-- Name: user_types user_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_types
    ADD CONSTRAINT user_types_pkey PRIMARY KEY (id);


--
-- TOC entry 4815 (class 2606 OID 66013)
-- Name: user_types user_types_user_type_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_types
    ADD CONSTRAINT user_types_user_type_key UNIQUE (user_type);


--
-- TOC entry 4817 (class 2606 OID 66026)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4819 (class 2606 OID 66024)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 4826 (class 2606 OID 65985)
-- Name: equipment_requests equipment_requests_equipment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_requests
    ADD CONSTRAINT equipment_requests_equipment_id_fkey FOREIGN KEY (equipment_id) REFERENCES public.equipments(id) ON DELETE CASCADE;


--
-- TOC entry 4827 (class 2606 OID 65990)
-- Name: equipment_requests equipment_requests_request_status_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_requests
    ADD CONSTRAINT equipment_requests_request_status_id_fkey FOREIGN KEY (request_status_id) REFERENCES public.request_status_types(id);


--
-- TOC entry 4822 (class 2606 OID 65954)
-- Name: equipments equipments_availability_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments
    ADD CONSTRAINT equipments_availability_id_fkey FOREIGN KEY (availability_id) REFERENCES public.equipment_availability_types(id);


--
-- TOC entry 4823 (class 2606 OID 65944)
-- Name: equipments equipments_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments
    ADD CONSTRAINT equipments_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.equipment_categories(id);


--
-- TOC entry 4820 (class 2606 OID 66052)
-- Name: equipment_categories fk_equipment_created_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_categories
    ADD CONSTRAINT fk_equipment_created_by FOREIGN KEY (created_by) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4828 (class 2606 OID 66057)
-- Name: equipment_requests fk_equipment_created_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_requests
    ADD CONSTRAINT fk_equipment_created_by FOREIGN KEY (created_by) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4824 (class 2606 OID 66062)
-- Name: equipments fk_equipment_created_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipments
    ADD CONSTRAINT fk_equipment_created_by FOREIGN KEY (created_by) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4825 (class 2606 OID 66067)
-- Name: request_status_types fk_equipment_created_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request_status_types
    ADD CONSTRAINT fk_equipment_created_by FOREIGN KEY (created_by) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4829 (class 2606 OID 66072)
-- Name: user_types fk_equipment_created_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_types
    ADD CONSTRAINT fk_equipment_created_by FOREIGN KEY (created_by) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4821 (class 2606 OID 66047)
-- Name: equipment_availability_types fk_equipment_modified_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipment_availability_types
    ADD CONSTRAINT fk_equipment_modified_by FOREIGN KEY (modified_by) REFERENCES public.users(id) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- TOC entry 4830 (class 2606 OID 66032)
-- Name: users users_created_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_created_by_fkey FOREIGN KEY (created_by) REFERENCES public.users(id);


--
-- TOC entry 4831 (class 2606 OID 66037)
-- Name: users users_modified_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_modified_by_fkey FOREIGN KEY (modified_by) REFERENCES public.users(id);


--
-- TOC entry 4832 (class 2606 OID 66027)
-- Name: users users_user_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_user_type_id_fkey FOREIGN KEY (user_type_id) REFERENCES public.user_types(id);


-- Completed on 2025-11-09 15:59:06

--
-- PostgreSQL database dump complete
--

